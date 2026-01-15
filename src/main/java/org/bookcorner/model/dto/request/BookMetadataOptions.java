package org.bookcorner.model.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class BookMetadataOptions {

    private String title;
    private String subtitle;
    private Set<String> authorNames;        // Primary Author Only
    private String isbn10;
    private String isbn13;
    private String openLibraryId;
}
