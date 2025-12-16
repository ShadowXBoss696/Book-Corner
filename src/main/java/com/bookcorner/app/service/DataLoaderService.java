package com.bookcorner.app.service;

import com.bookcorner.app.client.OpenLibraryClient;
import com.bookcorner.app.model.entity.Book;
import com.bookcorner.app.repository.BookRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataLoaderService {

    private final BookRepository bookRepository;

    private final OpenLibraryClient olClient;
    private final ObjectMapper objectMapper;

    @Transactional
    public Optional<Book> loadBookByOlid(String olid) {
        try {
            Optional<String> detailsJSON = olClient.getBookDetailsByOlid(olid);
            if (detailsJSON.isEmpty()) {
                throw new RuntimeException("No such book found.");
            }

            JsonNode node = objectMapper.readTree(detailsJSON.get());
            Book book = new Book();

            book.setTitle(node.get("title").asText());
            if (node.has("subtitle")) {
                book.setTitle(book.getTitle() + ": " + node.get("subtitle").asText());
            }

            book.setOlid(olid);
            book = bookRepository.save(book);

            log.info("Loaded book details for OLID: {}", olid);

            return Optional.of(book);
        } catch (Exception e) {
            log.error("Error loading book details for OLID: {}", olid, e);
        }
        return Optional.empty();
    }
}
