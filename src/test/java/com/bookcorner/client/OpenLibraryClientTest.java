package com.bookcorner.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OpenLibraryClientTest {

    @InjectMocks
    private OpenLibraryClient openLibraryClient;

    @Test
    void contextLoads() {
        assertNotNull(openLibraryClient, "OpenLibraryClient should be instantiated by Spring");
    }

}
