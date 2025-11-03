package com.bookcorner.client;

import com.bookcorner.config.HttpClientConfig;
import com.bookcorner.model.olclient.BookResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@Import(HttpClientConfig.class)
class OpenLibraryClientTest {

    @InjectMocks
    private OpenLibraryClient client;

    @Test
    void searchBooks_shouldReturnEditionIds_whenSuccessful() throws IOException {
        List<String> results = client.searchBooks("Diary of a Wimpy Kid", 10);      // Popular book title
        assertNotNull(results, "The result should not be null");
        assertFalse(results.isEmpty(), "The result should not be empty");
        assertTrue(results.stream().allMatch(id -> id != null && !id.isEmpty()), "All edition IDs should be non-null and non-empty");
    }

    @Test
    void searchBooks_shouldReturnEmptyList_whenNoResultsFound() throws IOException {
        List<String> results = client.searchBooks("asdlkfjasldkfjalksdjflkasjdf", 10);  // Gibberish query
        assertNotNull(results, "The result should not be null");
        assertTrue(results.isEmpty(), "The result should be empty for a gibberish query");
    }

    @Test
    void fetchBookByOLID_shouldReturnBookResponse_whenSuccessful() throws IOException {
        Optional<BookResponse> bookResponse = client.fetchBookByOLID("OL37736939M");    // Diary of a Wimpy Kid: Dog Days
        assertTrue(bookResponse.isPresent(), "BookResponse should be present for a valid OLID");

        BookResponse book = bookResponse.get();
        assertTrue(StringUtils.isNotBlank(book.getTitle()), "Book title should not be blank");
    }

    @Test
    void fetchBookByOLID_shouldReturnEmptyOptional_whenNoResultsFound() throws IOException {
        Optional<BookResponse> bookResponse = client.fetchBookByOLID("OL00000000M");    // Non-existent OLID
        assertFalse(bookResponse.isPresent(), "BookResponse should be empty for an invalid OLID");
    }
}
