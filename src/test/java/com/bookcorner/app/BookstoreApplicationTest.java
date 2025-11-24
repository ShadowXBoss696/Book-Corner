package com.bookcorner.app;

import com.bookcorner.app.config.TestContainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestContainersConfiguration.class)
class BookstoreApplicationTest {

    /**
     * A simple test to ensure that the Spring application context loads successfully.
     */
    @Test
    void contextLoads() {
        // Test will pass if the application context loads without exceptions
    }
}
