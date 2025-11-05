package com.bookcorner.controller;

import com.bookcorner.model.catalog.BookDetails;
import com.bookcorner.model.catalog.BookSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    @GetMapping
    public List<BookSummary> searchBooks(@RequestParam(name = "q") String query) {
        return null;
    }

    @GetMapping("/{uuid}")
    public BookDetails getBookDetails(@PathVariable UUID uuid) {
        return null;
    }

    @GetMapping("/isbn/{identifier}")
    public BookDetails findBookByIsbn(@PathVariable String identifier) {
        return null;
    }

    @GetMapping("/olid/{identifier}")
    public BookDetails findBookByOlid(@PathVariable String identifier) {
        return null;
    }
}
