package com.bookcorner.entity.catalog;

import com.bookcorner.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "authors")
public class Author extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "bio")
    private String bio;

    @Column(name = "olid")
    private String olid;

    @ManyToMany(mappedBy = "authors")
    private Set<Book> books = new HashSet<>();
}
