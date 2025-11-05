package com.bookcorner.model.catalog;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class BookSummary {
    // Basic Information
    private UUID id;
    private String fullTitle;
    private String coverImageUrl;
    private Set<AuthorSummary> authors;
    private Integer publicationYear;
}
