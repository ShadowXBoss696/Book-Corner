package com.bookcorner.model.catalog;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class BookDetails {
    // Basic Information
    private UUID id;
    private String title;
    private String subtitle;
    private String description;
    private String coverImageUrl;
    private Set<String> categories;

    // Publication Details
    private Set<AuthorSummary> authors;
    private Set<String> publishers;
    private Integer publicationYear;
    private Integer pageCount;

    // Identifiers
    private String olid;
    private String isbn10;
    private String isbn13;

    // Inventory and Pricing
    private Integer stockQuantity;
    private Double price;
}
