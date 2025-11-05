package com.bookcorner.model.olclient;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class BookResponse {

    private String title;
    private String subtitle;
    private String description;
    private Set<String> authorIds;
    private List<String> publishers;
    private Integer publishYear;
    private String isbn10;
    private String isbn13;
    private String coverImageId;
    private Integer pageCount;
    private Set<String> categories;
}
