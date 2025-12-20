package com.bookcorner.app.client;

import com.bookcorner.app.exception.ThirdPartyClientException;
import com.bookcorner.app.model.dto.ExtBookDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OpenLibraryClient {

    private static final String BASE_URL = "https://openlibrary.org";

    @Qualifier("openLibraryClient")
    private final OkHttpClient httpClient;

    private final ObjectMapper objectMapper;

    // *************************************************************
    // Search API | Open Library
    // *************************************************************

    // Reference: https://openlibrary.org/dev/docs/api/search

    /**
     * Search for books using the Open Library API and Solr Query.
     *
     * @param query The Solr query string.
     * @param page page number for pagination (1-based).
     * @param size page size for pagination.
     * @return A list of book IDs matching the search criteria.
     *
     * @throws ThirdPartyClientException If an error occurs during the request.
     */
    public List<String> searchBooks(String query, int page, int size) throws ThirdPartyClientException {
        // Validate input parameters
        validateStringParameter("search query", query);
        validatePaginationParameters(page, size);

        // Build the URL for searching books
        HttpUrl url = parseAndGetNewURLBuilder("/search.json")
            .addQueryParameter("q", query)
            .addQueryParameter("fields", "key,editions")
            .addQueryParameter("page", String.valueOf(page))
            .addQueryParameter("limit", String.valueOf(size))
            .addQueryParameter("lang", "en")
            .build();

        // Create the HTTP request
        Request request = new Request.Builder().url(url).build();

        // Execute the request and get the response
        Optional<String> responseBody = performApiRequest(request);
        if (responseBody.isEmpty()) {
            return List.of();
        }

        // Parse the response to extract book ids
        List<String> bookIds = JsonPath.read(responseBody.get(), "$.docs[*].editions.docs[*].key");
        if (bookIds.isEmpty()) {
            return List.of();
        }

        // Extract and return only the book ID from the full keys
        return bookIds.stream().map(this::extractIdFromKey).toList();
    }


    // *************************************************************
    // Book Details API | Open Library
    // *************************************************************

    // Reference: https://openlibrary.org/dev/docs/api/books

    /**
     * Fetch detailed information about a book using its Open Library ID.
     *
     * @param bookId The Open Library ID of the book.
     * @return An Optional containing the book details if found, otherwise empty.
     *
     * @throws ThirdPartyClientException If an error occurs during the request.
     * @throws JsonProcessingException If an error occurs while parsing the JSON response, returned by the external provider.
     */
    public Optional<ExtBookDetails> fetchBookDetails(String bookId) throws ThirdPartyClientException, JsonProcessingException {
        // Validate input parameter
        validateStringParameter("Book ID", bookId);

        // Build the URL for fetching book details
        HttpUrl url = parseAndGetNewURLBuilder("/books/" + bookId + ".json").build();

        // Create the HTTP request
        Request request = new Request.Builder().url(url).build();

        // Execute the request and get the response
        Optional<String> responseBody = performApiRequest(request);
        if (responseBody.isEmpty()) {
            return Optional.empty();
        }

        // Parse and return the book details
        var bookDetails = parseBookDetailsFromJson(responseBody.get());
        return Optional.of(bookDetails);
    }

    /**
     * Fetch detailed information about a book using its ISBN.
     *
     * @param isbn The ISBN of the book. Both ISBN-10 and ISBN-13 are supported.
     * @return An Optional containing the book details if found, otherwise empty.
     *
     * @throws ThirdPartyClientException If an error occurs during the request.
     * @throws JsonProcessingException If an error occurs while parsing the JSON response, returned by the external provider.
     */
    public Optional<ExtBookDetails> fetchBookDetailsByIsbn(String isbn) throws ThirdPartyClientException, JsonProcessingException {
        // Validate input parameter
        validateStringParameter("ISBN", isbn);

        // Build the URL for fetching book details by ISBN
        HttpUrl url = parseAndGetNewURLBuilder("/isbn/" + isbn + ".json").build();

        // Create the HTTP request
        Request request = new Request.Builder().url(url).build();

        // Execute the request and get the response
        Optional<String> responseBody = performApiRequest(request);
        if (responseBody.isEmpty()) {
            return Optional.empty();
        }

        // Parse and return the book details
        var bookDetails = parseBookDetailsFromJson(responseBody.get());
        return Optional.of(bookDetails);
    }

    // Parse book details from JSON response
    private ExtBookDetails parseBookDetailsFromJson(String bookAsJson) throws JsonProcessingException {
        JsonNode bookNode = objectMapper.readTree(bookAsJson);
        ExtBookDetails bookDetails = new ExtBookDetails();

        bookDetails.setId(extractIdFromKey(bookNode.get("key").asText()));
        bookDetails.setTitle(bookNode.get("title").asText());
        if (bookNode.has("subtitle") && !StringUtils.isBlank(bookNode.get("subtitle").asText())) {
            bookDetails.setTitle(bookDetails.getTitle() + ": " + bookNode.get("subtitle").asText());
        }

        return bookDetails;
    }


    // *************************************************************
    // Author Details API | Open Library
    // *************************************************************

    // Reference: https://openlibrary.org/dev/docs/api/authors

    public void fetchAuthorDetails(String authorId) {

    }


    // *************************************************************
    // Covers API | Open Library
    // *************************************************************

    // Reference: https://openlibrary.org/dev/docs/api/covers

    public void fetchBookCover(String coverId) {

    }

    public void fetchAuthorPhoto(String photoId) {

    }


    // *************************************************************
    // Helper Methods
    // *************************************************************

    // Execute the HTTP request and return the response body as an Optional String
    private Optional<String> performApiRequest(Request request) throws ThirdPartyClientException {
        try (Response response = httpClient.newCall(request).execute()) {

            // Handle unsuccessful responses
            if (!response.isSuccessful()) {
                return handleUnsuccessfulResponse(response);
            }

            // Return the response body
            return Optional.of(response.body().string());
        } catch (IOException e) {
            throw new ThirdPartyClientException("Error occurred while making API request to Open Library", e);
        }
    }

    // Handle unsuccessful HTTP responses and return appropriate results or throw exceptions
    private Optional<String> handleUnsuccessfulResponse(Response response) throws ThirdPartyClientException {
        int statusCode = response.code();

        switch (statusCode) {
            case 404:                       // Not Found
                return Optional.empty();
            default:
                throw new ThirdPartyClientException("Unexpected response from Open Library API: " + response);
        }
    }

    // Helper method to parse URL and return a new HttpUrl.Builder
    private HttpUrl.Builder parseAndGetNewURLBuilder(String path) throws IllegalArgumentException {
        HttpUrl httpUrl = HttpUrl.parse(BASE_URL + path);
        if (httpUrl == null) {
            throw new IllegalArgumentException("Invalid URL path: " + path);
        }
        return httpUrl.newBuilder();
    }

    // Validate parameters is null or empty
    private void validateStringParameter(String paramName, String paramValue) throws IllegalArgumentException {
        if (StringUtils.isBlank(paramValue)) {
            throw new IllegalArgumentException(paramName + " cannot be null or empty.");
        }
    }

    // Validate pagination parameters
    private void validatePaginationParameters(int pageNo, int pageSize) throws IllegalArgumentException {
        if (pageNo < 1) {
            throw new IllegalArgumentException("Page number must be greater than 0.");
        }
        if (pageSize < 1 || pageSize > 100) {
            throw new IllegalArgumentException("Page size must be between 1 and 100.");
        }
    }

    // Extract id from full key string
    private String extractIdFromKey(String key) {
        return key.substring(key.lastIndexOf('/') + 1);
    }
}
