package com.bookcorner.app.client;

import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OpenLibraryClient {

    private static final String BASE_URL = "https://openlibrary.org";

    @Qualifier("openLibraryClient")
    private final OkHttpClient httpClient;

    /**
     * Search for books using the Open Library API and Solr Query.
     *
     * @param solrQuery The Solr query string.
     * @param offset Offset for pagination.
     * @param limit Limit for pagination.
     * @return A list of book IDs matching the search criteria.
     * @throws IOException If an I/O error occurs during the request.
     */
    public List<String> searchBooks(String solrQuery, int offset, int limit) throws IOException {
        // Build the URL for searching books
        HttpUrl url = parseAndGetNewURLBuilder("/search.json")
            .addQueryParameter("q", solrQuery)
            .addQueryParameter("fields", "key,editions")
            .addQueryParameter("offset", String.valueOf(offset))
            .addQueryParameter("limit", String.valueOf(limit))
            .addQueryParameter("lang", "en")
            .build();

        // Create the HTTP request
        Request request = new Request.Builder().url(url).build();

        // Execute the request and get the response
        Optional<String> responseBody = executeRequest(request);
        if (responseBody.isEmpty()) {
            return Collections.emptyList();
        }

        // Parse the response to extract book keys
        List<String> bookKeys = JsonPath.read(responseBody.get(), "$.docs[*].editions.docs[*].key");
        if (bookKeys.isEmpty()) {
            return Collections.emptyList();
        }

        // Extract only the id part from the keys
        return bookKeys.stream()
            .map(key -> key.substring(key.lastIndexOf('/') + 1))
            .toList();
    }

    // Helper method to parse URL and return a new HttpUrl.Builder
    private HttpUrl.Builder parseAndGetNewURLBuilder(String path) throws IllegalArgumentException {
        HttpUrl httpUrl = HttpUrl.parse(BASE_URL + path);
        if (httpUrl == null) {
            throw new IllegalArgumentException("Invalid URL path: " + path);
        }
        return httpUrl.newBuilder();
    }

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
