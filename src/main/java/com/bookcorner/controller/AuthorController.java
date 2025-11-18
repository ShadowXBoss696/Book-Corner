package com.bookcorner.controller;

import com.bookcorner.model.catalog.AuthorDetails;
import com.bookcorner.model.catalog.AuthorSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
public class AuthorController {

    @GetMapping
    public List<AuthorSummary> searchAuthors(@RequestParam(name = "q") String query) {
        return null;
    }

    @GetMapping("/{uuid}")
    public AuthorDetails getAuthorDetails(@PathVariable UUID uuid) {
        return null;
    }

    @GetMapping("/olid/{identifier}")
    public AuthorDetails findAuthorByOlid(@PathVariable String identifier) {
        return null;
    }
}
