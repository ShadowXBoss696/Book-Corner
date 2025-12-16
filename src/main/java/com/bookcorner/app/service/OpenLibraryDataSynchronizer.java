package com.bookcorner.app.service;

import com.bookcorner.app.client.OpenLibraryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenLibraryDataSynchronizer {

    private final OpenLibraryClient olClient;

    public void loadTrendingBooks() {
        String solrQuery = "trending_score_hourly_sum:[1 TO *] readinglog_count:[4 TO *] language:eng -subject:\"content_warning:cover\" -subject:\"content_warning:cover\"";
        loadBooksBySolrQuery(solrQuery, 100);
    }

    private void loadBooksBySolrQuery(String solrQuery, int limit) {
        try {
            List<String> books = olClient.searchBooks(solrQuery, 0, limit);

            // Process the loaded books as needed
            for (int i = 0; i < books.size(); i++) {
                String bookId = books.get(i);
                System.out.println("Loaded book ID (" + (i + 1) + "): " + bookId);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load books from Open Library", e);
        }
    }
}
