package com.bookcorner.entity.catalog;

import com.bookcorner.entity.base.BaseEntity;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
public class Book extends BaseEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "subtitle")
    private String subtitle;

    @Column(name = "description")
    private String description;

    @Column(name = "olid")
    private String olid;

    @ManyToMany
    @JoinTable(
        name = "book_author",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();

    @Column(name = "publishers")
    private Set<String> publishers = new HashSet<>();

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Column(name = "isbn_10")
    private String isbn10;

    @Column(name = "isbn_13")
    private String isbn13;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Column(name = "page_count")
    private Integer pageCount;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Column(name = "price_usd", nullable = false)
    private Double price;

    @Column(name = "categories")
    private Set<String> categories;
}
