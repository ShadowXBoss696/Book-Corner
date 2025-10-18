package com.bookcorner.entity.catalog;

import com.bookcorner.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "work")
@NoArgsConstructor
public class Work extends BaseEntity {
}
