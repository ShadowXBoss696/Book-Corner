package com.bookcorner.app;

import com.bookcorner.app.client.OpenLibraryClient;
import com.bookcorner.app.exception.ThirdPartyClientException;
import com.bookcorner.app.model.dto.ExtBookDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/* This is a temporary solution for development */
@Slf4j
@Component
public class ManualTestBed implements CommandLineRunner {

    @Autowired
    private OpenLibraryClient openLibraryClient;

    public void runTest() throws Exception {
        List<String> books = openLibraryClient.searchBooks("the lords of the ring", 1, 5);
        log.info("Books: {}", books);

        books.forEach(bookId -> {
            try {
                Optional<ExtBookDetails> bookDetails = openLibraryClient.fetchBookDetails(bookId);


            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    @Override
    public void run(String... args) throws Exception {
        // <editor-fold desc="Init">
        Instant startTime = Instant.now();

        log.info("**********************************");
        log.info("*        TEST STARTED            *");
        log.info("**********************************");

        try {
            runTest();

            log.info("Done ...   (took {} ms)", Duration.between(startTime, Instant.now()).toMillis());
        } catch (Exception e) {
            log.error("Failed ...   (took {} ms)", Duration.between(startTime, Instant.now()).toMillis(), e);
        }
    }
}
