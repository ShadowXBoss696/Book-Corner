package com.bookcorner.controller;

import com.bookcorner.model.catalog.AuthorDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
public class AuthorController {

    @GetMapping("/{uuid}")
    public AuthorDetails getAuthorDetails(@PathVariable UUID uuid) {
        return null;
    }

    @GetMapping("/olid/{identifier}")
    public AuthorDetails findAuthorByOlid(@PathVariable String identifier) {
        return null;
    }
}
