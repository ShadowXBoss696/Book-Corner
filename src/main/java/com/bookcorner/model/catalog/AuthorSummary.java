package com.bookcorner.model.catalog;

import lombok.Data;

import java.util.UUID;

@Data
public class AuthorSummary {
    private UUID id;
    private String name;
}
