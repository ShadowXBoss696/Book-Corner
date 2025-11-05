package com.bookcorner.service;

import com.bookcorner.entity.catalog.Author;
import com.bookcorner.entity.catalog.Book;
import com.bookcorner.model.catalog.AuthorDetails;
import com.bookcorner.model.catalog.AuthorSummary;
import com.bookcorner.model.catalog.BookDetails;
import com.bookcorner.model.catalog.BookSummary;
import com.bookcorner.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogService {

    private final BookRepository bookRepository;

    // Search for books by query string
    public List<BookSummary> searchBooks(String query) {
        return new ArrayList<>();
    }

    public BookDetails getBookDetails(UUID bookId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new NoSuchElementException("No book found with ID: " + bookId));
        return mapToBookDetails(book);
    }

    /// Mappers

    private BookSummary mapToBookSummary(Book book) {
        BookSummary summary = new BookSummary();
        summary.setId(book.getId());
        String fullTitle = book.getTitle();
        if (book.getSubtitle() != null && !book.getSubtitle().isEmpty()) {
            fullTitle += ": " + book.getSubtitle();
        }
        summary.setFullTitle(fullTitle);
        summary.setCoverImageUrl(book.getCoverImageUrl());
        summary.setAuthors(
            book.getAuthors().stream()
                .map(this::mapToAuthorSummary)
                .collect(Collectors.toSet())
        );
        summary.setPublicationYear(book.getPublicationYear());
        return summary;
    }

    private BookDetails mapToBookDetails(Book book) {
        BookDetails details = new BookDetails();
        details.setId(book.getId());
        details.setTitle(book.getTitle());
        details.setSubtitle(book.getSubtitle());
        details.setDescription(book.getDescription());
        details.setCoverImageUrl(book.getCoverImageUrl());
        details.setAuthors(
            book.getAuthors().stream()
                .map(this::mapToAuthorSummary)
                .collect(Collectors.toSet())
        );
        details.setPublishers(book.getPublishers());
        details.setPublicationYear(book.getPublicationYear());
        details.setPageCount(book.getPageCount());
        details.setOlid(book.getOlid());
        details.setIsbn10(book.getIsbn10());
        details.setIsbn13(book.getIsbn13());
        details.setStockQuantity(book.getStockQuantity());
        details.setPrice(book.getPrice());
        return details;
    }

    private AuthorSummary mapToAuthorSummary(Author author) {
        AuthorSummary summary = new AuthorSummary();
        summary.setId(author.getId());
        summary.setName(author.getName());
        return summary;
    }

    private AuthorDetails mapToAuthorDetails(Author author) {
        AuthorDetails details = new AuthorDetails();
        details.setId(author.getId());
        details.setName(author.getName());
        details.setBio(author.getBio());
        details.setOlid(author.getOlid());
        return details;
    }
}
