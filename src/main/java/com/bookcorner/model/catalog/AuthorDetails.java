package com.bookcorner.model.catalog;

import lombok.Data;

import java.util.UUID;

@Data
public class AuthorDetails {
    private UUID id;
    private String name;
    private String bio;
    private String olid;
}
