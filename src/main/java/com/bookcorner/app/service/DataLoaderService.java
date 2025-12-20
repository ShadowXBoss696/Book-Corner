package com.bookcorner.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataLoaderService {






    /*
    private final BookRepository bookRepository;

    private final OpenLibraryClient olClient;
    private final ObjectMapper objectMapper;

    public Optional<Book> fetchBookByOlid(@NonNull String olid) {
        return Optional.empty();
    }

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
    */
}
