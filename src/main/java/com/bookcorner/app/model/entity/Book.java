package com.bookcorner.app.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "books")
@NoArgsConstructor
public class Book extends BaseEntity {
}
