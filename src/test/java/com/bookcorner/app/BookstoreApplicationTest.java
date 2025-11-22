package com.bookcorner.app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class BookstoreApplicationTest {

    /**
     * A simple test to ensure that the Spring application context loads successfully.
     */
    @Test
    void contextLoads() {
        // Test will pass if the application context loads without exceptions
    }
}
