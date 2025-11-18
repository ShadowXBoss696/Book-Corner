package com.bookcorner.service;

import com.bookcorner.client.OpenLibraryClient;
import com.bookcorner.model.catalog.BookSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final OpenLibraryClient openLibraryClient;

    public List<BookSummary> searchBooks(String query, long offset) {
        try {
            // Delegate to OpenLibraryClient to perform the search
            List<String> olBookIDs = openLibraryClient.searchBooks(query, offset, 1000);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
