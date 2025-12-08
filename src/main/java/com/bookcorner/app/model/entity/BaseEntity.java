package com.bookcorner.app.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Base entity class for all domain entities in the application.
 * Provides common functionality like ID generation, equality comparison,
 * and string representation suitable for JPA entities.
 *
 * <p>All concrete entities should extend this class to inherit standard
 * persistence behavior and entity identification logic.</p>
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Primary key identifier for the entity. This is generated based on the IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Compares this entity to another object for equality for proper JPA/Hibernate behavior. This also handles
     * proxy objects created by Hibernate.
     *
     * <p>Two entities are considered equal if they are of the same persistent class and have same Non-Null ID.</p>
     *
     * @param obj the object to compare with
     * @return true if the objects are of the same class and have the same ID, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) return false;

        BaseEntity that = (BaseEntity) obj;
        return this.getId() != null && Objects.equals(this.getId(), that.getId());
    }

    /**
     * Generates a hash code for the entity based on its persistent class and ID.
     *
     * @return the hash code of the entity
     */
    @Override
    public int hashCode() {
        Class<?> persistentClass = Hibernate.getClass(this);
        return Objects.hash(persistentClass, getId());
    }

    /**
     * Provides a string representation of the entity including its class name and ID.
     */
    @Override
    public String toString() {
        Class<?> persistentClass = Hibernate.getClass(this);
        return persistentClass.getSimpleName() + " [id=" + getId() + "]";
    }
}
