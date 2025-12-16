package com.bookcorner.app.service;

import com.bookcorner.app.client.OpenLibraryClient;
import com.bookcorner.app.mapper.BookMapper;
import com.bookcorner.app.model.dto.BookSummary;
import com.bookcorner.app.model.entity.Book;
import com.bookcorner.app.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

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

    private BookSummary getBookByIsbn(String isbn) {
        return null;
    }

    private BookSummary getBookByOlid(String olid) {
        Book book = bookRepository.findByOlid(olid)
            .or(() -> dataLoaderService.loadBookByOlid(olid))
            .orElseThrow(() -> new RuntimeException("Book with OLID " + olid + " not found"));

        return bookMapper.convertToBookSummary(book);
    }
}
