package com.bookcorner.app.model.entity.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;                                            // Primary key field

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) return false;
        BaseEntity that = (BaseEntity) obj;

        // Two entities are considered equal if they have the same non-null ids
        return this.getId() != null && Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        // Use the persistent class and id for hash code generation
        Class<?> persistentClass = Hibernate.getClass(this);
        return Objects.hash(persistentClass, getId());
    }

    @Override
    public String toString() {
        // Default format: ClassName [id=ID]
        Class<?> persistentClass = Hibernate.getClass(this);
        return persistentClass.getSimpleName() + " [id=" + getId() + "]";
    }
}
