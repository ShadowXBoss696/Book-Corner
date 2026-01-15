package org.bookcorner.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class BookMetadata {

    // Basic Information
    private String title;
    private String subtitle;
    private String description;
    private Set<AuthorMetadata> authors;
    private String coverImageUrl;

    // Publication Details
    private String publisher;
    private Integer publishedYear;
    private String language;
    private Integer pageCount;

    // Series Information
    private String seriesName;
    private Integer seriesIndex;
    private Integer seriesTotal;

    // External Identifiers
    private String isbn10;
    private String isbn13;
    private String asin;
    private String openLibraryId;
    private String googleBooksId;
    private String goodreadsId;

    // Additional Metadata
    private Set<String> categories;
    private Set<String> moods;
    private Set<String> tags;
    private Set<BookReview> reviews;
}
