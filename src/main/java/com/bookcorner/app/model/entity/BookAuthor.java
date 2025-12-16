package com.bookcorner.app.model.entity;

import com.bookcorner.app.model.entity.base.BaseEntity;
import com.bookcorner.app.model.enums.AuthorRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "book_authors")
public class BookAuthor extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "role", nullable = false)
    private AuthorRole role;

    @Column(name = "ordinal", nullable = false)
    private Integer ordinal;
}
