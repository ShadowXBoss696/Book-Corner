package com.bookcorner.mapper;

import com.bookcorner.entity.catalog.Book;
import com.bookcorner.model.catalog.BookDetails;
import com.bookcorner.model.catalog.BookSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookMapper {

    private final AuthorMapper authorMapper;

    // For list views
    public BookSummary mapToBookSummary(Book book) {
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
                .map(authorMapper::mapToAuthorSummary)
                .collect(Collectors.toSet())
        );
        summary.setPublicationYear(book.getPublicationYear());
        return summary;
    }

    // For detailed views
    public BookDetails mapToBookDetails(Book book) {
        BookDetails details = new BookDetails();
        details.setId(book.getId());
        details.setTitle(book.getTitle());
        details.setSubtitle(book.getSubtitle());
        details.setDescription(book.getDescription());
        details.setCoverImageUrl(book.getCoverImageUrl());
        details.setAuthors(
            book.getAuthors().stream()
                .map(authorMapper::mapToAuthorSummary)
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
        details.setCategories(book.getCategories());
        return details;
    }
}
