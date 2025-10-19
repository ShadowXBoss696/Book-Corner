package com.bookcorner.service;

import com.bookcorner.entity.catalog.Book;
import com.bookcorner.repository.BookRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DataLoaderService {

    private final OkHttpClient okHttpClient;
    private final ObjectMapper mapper;

    private final BookRepository bookRepository;

    public void loadData() {
        String olid = "OL25646464M"; // Example OLID

        JsonNode bookData = getBookDataByOLID(olid);
        System.out.println("Fetched Book Data: " + bookData.toPrettyString());

        // Further processing and saving to the repository can be done here
        Book book = bookRepository.findByOlid(olid).orElseGet(Book::new);
        book.setOlid(olid);

        String title = bookData.get("title").asText();
        if (bookData.has("subtitle")) {
            title += ": " + bookData.get("subtitle").asText();
        }
        book.setTitle(title);

        book.setDescription(getAs(bookData, "description", String.class));

        book.setStockQuantity(1);
        book.setIsbn10(getAs(bookData, "isbn_10", List.class));
        book.setIsbn13(getAs(bookData, "isbn_13", List.class));

        bookRepository.save(book);
    }

    private <T> T getAs(JsonNode node, String fieldName, Class<T> valueType) {
        if (node.has(fieldName)) {
            return mapper.convertValue(node.get(fieldName), valueType);
        }
        return null;
    }

    private JsonNode getBookDataByOLID(@NonNull String olid) {
        Request request = new Request.Builder()
                .url("https://openlibrary.org/books/" + olid + ".json")
                .build();
        return performRequest(request);
    }

    private JsonNode performRequest(Request request) {
        try (var response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed to fetch data from Open Library: " + response.message() + " [Status: " + response.code() + "]");
            }

            String responseBody = response.body().string();
            return mapper.readTree(responseBody);
        } catch (Exception e) {
            throw new RuntimeException("Error during data loading from Open Library", e);
        }
    }
}
