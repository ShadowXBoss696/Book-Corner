package com.bookcorner.app.service;

import com.bookcorner.app.model.dto.BookSummary;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Test
    void listAllTrendingBooks() {
        var trendingBooks = bookService.listAllTrendingBooks();
        assertNotNull(trendingBooks);
        assertFalse(trendingBooks.isEmpty(), "Trending books list should not be empty");

        for (int i = 0; i < trendingBooks.size(); i++) {
            var book = trendingBooks.get(i);
            System.out.println((i+1) + ": " + book.getTitle());
        }
    }
}
