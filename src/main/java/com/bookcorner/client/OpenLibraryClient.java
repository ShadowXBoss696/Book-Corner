package com.bookcorner.client;

import com.bookcorner.model.olclient.AuthorResponse;
import com.bookcorner.model.olclient.BookResponse;
import com.bookcorner.util.OkHttpClientFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.core.functions.CheckedSupplier;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenLibraryClient {

    private static final String BASE_URL = "https://openlibrary.org";

    private final OkHttpClient httpClient = OkHttpClientFactory.getHttpClient().newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .readTimeout(Duration.ofSeconds(30))
        .writeTimeout(Duration.ofSeconds(30))
        .connectionPool(new ConnectionPool(5, 5, TimeUnit.MINUTES))
        .build();

    private final ObjectMapper mapper = new ObjectMapper();


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

    // Retry Configurations
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


    /// Client Functions

     /**
     * Searches for books matching the given query and returns a list of edition IDs.
     *
     * @param query the search query
     * @param limit the maximum number of results to return
     * @return a list of edition IDs matching the query from Open Library
     * @throws IOException if an connection error occurs during the request
     */
    public List<String> searchBooks(String query, int limit) throws IOException {
        // Prepare URL
        HttpUrl url = HttpUrl.get(BASE_URL + "/search.json").newBuilder()
            .addQueryParameter("q", query)
            .addQueryParameter("fields", "key,editions")
            .addQueryParameter("limit", "" + limit)
            .addQueryParameter("lang", "en")
            .build();

        // Build Request
        Request request = new Request.Builder().url(url).build();

        // Execute Request
        Optional<String> response = executeRequest(request);
        if (response.isEmpty()) {
            return Collections.emptyList();
        }

        // Extract edition keys using JsonPath
        List<String> editionKeys = JsonPath.read(response.get(), "$.docs[*].editions.docs[*].key");
        if (editionKeys == null || editionKeys.isEmpty()) {
            return Collections.emptyList();
        }

        // Extract only the id part from the keys and return
        return editionKeys.stream()
            .map(key -> key.substring(key.lastIndexOf('/') + 1))
            .collect(Collectors.toList());
    }

    /**
     * Fetches detailed book information by its Open Library ID (OLID).
     *
     * @param olid the Open Library ID of the book
     * @return an Optional containing {@link BookResponse} if found, otherwise empty
     * @throws IOException if a connection error occurs during the request
     */
    public Optional<BookResponse> fetchBookByOLID(String olid) throws IOException {
        // Prepare URL
        HttpUrl url = HttpUrl.get(BASE_URL + "/books/" + olid + ".json");

        // Build Request
        Request request = new Request.Builder().url(url).build();

        // Execute Request
        Optional<String> response = executeRequest(request);
        if (response.isEmpty()) {
            return Optional.empty();
        }

        // Process Response and return
        JsonNode node = mapper.readTree(response.get());
        return mapToBookResponse(node);
    }

    /**
     * Fetches detailed author information by their Open Library ID (OLID).
     *
     * @param olid the Open Library ID of the author
     * @return an Optional containing {@link AuthorResponse} if found, otherwise empty
     * @throws IOException if a connection error occurs during the request
     */
    public Optional<AuthorResponse> fetchAuthorByOLID(String olid) throws IOException {
        // Prepare URL
        HttpUrl url = HttpUrl.get(BASE_URL + "/authors/" + olid + ".json");

        // Build Request
        Request request = new Request.Builder().url(url).build();

        // Execute Request
        Optional<String> response = executeRequest(request);
        if (response.isEmpty()) {
            return Optional.empty();
        }

        // Process Response and return
        JsonNode node = mapper.readTree(response.get());
        return mapToAuthorResponse(node);
    }


    /// Private Helper Methods

    // Executes the given HTTP request with rate-limiting and retry mechanisms applied
    private Optional<String> executeRequest(Request request) throws IOException {
        // Add metadata
        Request updatedRequest = request.newBuilder()
            .addHeader("User-Agent", "Book-Corner API (arpan.mahanty.007@gmail.com)")
            .build();

        // Build Supplier Chain
        CheckedSupplier<Response> httpSupplier = () -> httpClient.newCall(updatedRequest).execute();

        // Apply Rate-Limiter
        RateLimiter rateLimiter = getRateLimiter(request.url());
        httpSupplier = RateLimiter.decorateCheckedSupplier(rateLimiter, httpSupplier);

        // Apply Retry-Mechanics
        httpSupplier = Retry.decorateCheckedSupplier(retry, httpSupplier);

        // Execute the request
        try (Response response = httpSupplier.get()) {
            if (!response.isSuccessful()) {

                // Handle 404 Not Found gracefully
                if (response.code() == 404) {
                    return Optional.empty();
                }

                throw new IOException("Unexpected response: " + response);
            }

            // Return response body as string
            String responseBody = response.body().string();
            return Optional.of(responseBody);

        } catch (Throwable e) {
            throw new IOException("Request execution failed: " + request.url(), e);
        }
    }

    // Selects the appropriate rate limiter based on the request URL
    private RateLimiter getRateLimiter(HttpUrl httpUrl) throws IOException {
        String host = httpUrl.host();

        if (host.equalsIgnoreCase("covers.openlibrary.org")) {
            return coverLimiter;
        }

        return apiLimiter;  // default to API limiter
    }

    // Maps a JsonNode to a BookResponse object
    private Optional<BookResponse> mapToBookResponse(JsonNode node) {
        if (node == null || node.isNull()) {
            return Optional.empty();
        }

        BookResponse bookResponse = new BookResponse();

        bookResponse.setTitle(node.path("title").asText());
        bookResponse.setSubtitle(node.path("subtitle").asText(null));
        bookResponse.setDescription(extractDescText(node, "description"));

        Set<String> authorIds = node.path("authors").findValuesAsText("key").stream()
            .map(key -> key.substring(key.lastIndexOf('/') + 1))
            .collect(Collectors.toSet());
        bookResponse.setAuthorIds(authorIds);

        bookResponse.setPublishers(mapper.convertValue(node.get("publishers"), new TypeReference<>() {}));
        bookResponse.setPublishYear(extractYearFromDateString(node.path("publish_date").asText(null)));

        bookResponse.setIsbn10(extractNthItemFromArray(node, "isbn_10", 0));
        bookResponse.setIsbn13(extractNthItemFromArray(node, "isbn_13", 0));

        bookResponse.setCoverImageId(extractNthItemFromArray(node, "covers", 0));
        bookResponse.setPageCount(node.has("number_of_pages") ? node.path("number_of_pages").asInt() : null);

        bookResponse.setCategories(mapper.convertValue(node.get("subjects"), new TypeReference<>() {}));

        return Optional.of(bookResponse);
    }

    // Maps a JsonNode to an AuthorResponse object
    private Optional<AuthorResponse> mapToAuthorResponse(JsonNode node) {
        if (node == null || node.isNull()) {
            return Optional.empty();
        }

        AuthorResponse authorResponse = new AuthorResponse();

        authorResponse.setName(node.path("name").asText());
        authorResponse.setBio(extractDescText(node, "bio"));

        return Optional.of(authorResponse);
    }

    private String extractDescText(JsonNode node, String fieldName) {
        JsonNode descNode = node.get(fieldName);
        if (descNode == null || descNode.isNull()) {
            return null;
        }

        if (descNode.isTextual()) {
            return descNode.asText();
        } else if (descNode.isObject() && descNode.has("value")) {
            return descNode.get("value").asText();
        }

        return null;
    }

    // Extracts the year from a date string
    private Integer extractYearFromDateString(String dateStr) {
        if (dateStr == null) {
            return null;
        }
        String[] parts = dateStr.split("\\D+");
        for (String part : parts) {
            if (part.length() >= 4) {   // Look for a 4-digit year
                try {
                    return Integer.parseInt(part.substring(0, 4));
                } catch (NumberFormatException e) {
                    // Ignore and continue
                }
            }
        }
        return null;
    }

    // Extracts the nth item from a JSON array field
    private String extractNthItemFromArray(JsonNode node, String fieldName, int i) {
        JsonNode arrayNode = node.path(fieldName);
        if (arrayNode.isArray() && arrayNode.size() > i) {
            return arrayNode.get(i).asText(null);
        }
        return null;
    }
}
