package com.bookcorner.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CatalogService {

    /*
    // Repositories and Mappers
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    // External Clients and Services
    private final OpenLibraryClient olClient;
    private final DataLoaderService dataLoaderService;

    public List<BookSummary> listAllTrendingBooks() {
        String solrQuery = "trending_score_hourly_sum:[1 TO *] readinglog_count:[4 TO *] language:eng -subject:\"content_warning:cover\" -subject:\"content_warning:cover\"";

        try {
            List<String> trendingBooks = olClient.searchBooks(solrQuery, 0, 1000);
            return trendingBooks.stream().map(this::getBookByOlid).toList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch trending books list", e);
        }
    }

    private BookSummary getBookByOlid(String olid) {
        Book book = bookRepository.findByOlid(olid)
            // .or(() -> dataLoaderService.loadBookByOlid(olid))
            .orElseThrow(() -> new RuntimeException("Book with OLID " + olid + " not found"));

        return bookMapper.convertToBookSummary(book);
    }
    */
}
