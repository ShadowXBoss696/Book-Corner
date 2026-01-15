package org.bookcorner.model.dto;

import lombok.Builder;
import lombok.Data;
import org.bookcorner.model.enums.AuthorRole;

@Data
@Builder
public class AuthorMetadata {

    // Basic Information
    private String name;
    private String bio;
    private String profileImageUrl;

    // Life Events
    private String birthDate;
    private String deathDate;

    // External Identifiers
    private String openLibraryId;

    // Book-Author Relationship
    private AuthorRole role; // e.g., Author, Editor, Illustrator
}
