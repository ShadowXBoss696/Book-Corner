package com.bookcorner.app.model.entity;

import com.bookcorner.app.model.enums.AuthorRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "book_authors")
public class BookAuthor extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private AuthorRole role;

    @Column(name = "ordinal", nullable = false)
    private Integer ordinal;
}
