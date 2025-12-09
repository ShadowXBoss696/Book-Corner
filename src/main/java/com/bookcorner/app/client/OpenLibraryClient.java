package com.bookcorner.app.client;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OpenLibraryClient {

    private static final String BASE_URL = "https://openlibrary.org";

    @Qualifier("openLibraryClient")
    private final OkHttpClient httpClient;

    // Execute an HTTP request and return the response body as an Optional String
    private Optional<String> executeRequest(Request request) throws IOException {
        try (Response response = httpClient.newCall(request).execute()) {
            // 404 Not Found
            if (response.code() == 404) {
                return Optional.empty();
            }

            // Other Unsuccessful Responses
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response: " + response);
            }

            // Successful Response (return body as string)
            return Optional.of(response.body().string());
        }
    }
}
