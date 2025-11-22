package com.bookcorner.app.interceptor;

import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.core.functions.CheckedSupplier;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.time.Duration;

/**
 * Interceptor for Open Library API requests.
 * <p>
 * Responsibilities:
 * <ol>
 *   <li>Adds metadata (User-Agent header) to each request.
 *   <li>Implements rate limiting based on Open Library's guidelines.
 *   <li>Implements retry mechanism for handling rate limit exceedance (HTTP 429) and forbidden access (HTTP 403).
 * </ol>
 *
 * @see okhttp3.Interceptor
 */
@Slf4j
public class OpenLibraryInterceptor implements Interceptor {

    private static final String USER_AGENT = "Book-Corner API (arpan.mahanty.007@gmail.com)";

    /// Rate Limiters

    // API endpoints: 180 req/min = 3 req/sec
    private final RateLimiter apiLimiter = RateLimiter.of("api", RateLimiterConfig.custom()
        .limitForPeriod(180)
        .limitRefreshPeriod(Duration.ofMinutes(1))
        .build());

    // Cover images: 400 req/min = ~6.67 req/sec
    private final RateLimiter coverLimiter = RateLimiter.of("cover", RateLimiterConfig.custom()
        .limitForPeriod(400)
        .limitRefreshPeriod(Duration.ofMinutes(1))
        .build());


    /// Retry Configuration

    private final Retry retry = Retry.of("openLibrary", RetryConfig.custom()
        .maxAttempts(3)
        .intervalFunction(IntervalFunction.ofExponentialBackoff(1000, 2))
        .retryOnResult(response ->
            ((Response) response).code() == 429 || ((Response) response).code() == 403)

        .consumeResultBeforeRetryAttempt((numTries, response) -> {
            int statusCode = ((Response) response).code();
            log.warn("Received HTTP status code {} from Open Library API. Retrying ... #{}", statusCode, numTries);

            // Close response to prevent resource leaks
            ((Response) response).close();
        })
        .build());


    /// Interceptor Method

    @Override
    public @NonNull Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();

        // Add additional metadata
        request = request.newBuilder()
            .header("User-Agent", USER_AGENT)       // Identify the application
            .build();

        // Build Supplier chain for Resilience4j
        Request finalRequest = request;
        CheckedSupplier<Response> httpsupplier = () -> chain.proceed(finalRequest);

        // Apply Rate Limiter (based on URL)
        RateLimiter rateLimiter = getRateLimiter(request.url());
        httpsupplier = RateLimiter.decorateCheckedSupplier(rateLimiter, httpsupplier);

        // Apply Retry mechanism
        httpsupplier = Retry.decorateCheckedSupplier(retry, httpsupplier);

        // Execute the chain
        try {
            return httpsupplier.get();
        } catch (IOException e) {
            throw e;
        } catch (Throwable e) {
            throw new IOException("Unexpected error during request execution", e);
        }
    }

    // Helper method to select appropriate Rate Limiter
    private RateLimiter getRateLimiter(HttpUrl httpUrl) {
        String host = httpUrl.host();

        if (host.equalsIgnoreCase("covers.openlibrary.org")) {
            return coverLimiter;    // cover image limiter
        }

        return apiLimiter;  // default to API limiter
    }
}
