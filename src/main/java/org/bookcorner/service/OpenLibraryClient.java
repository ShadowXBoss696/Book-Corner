package org.bookcorner.service;

import lombok.extern.slf4j.Slf4j;
import org.bookcorner.model.dto.BookMetadata;
import org.bookcorner.model.dto.request.BookMetadataOptions;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class OpenLibraryClient {

    public Optional<BookMetadata> fetchBookMetadata(BookMetadataOptions options) {
        return Optional.empty();
    }
}
