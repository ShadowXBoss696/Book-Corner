package com.bookcorner.app.config;

import com.bookcorner.app.interceptor.OpenLibraryInterceptor;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Configuration
public class ClientConfig {

    /**
     * Default HTTP client configuration, using common settings.
     */
    @Bean
    @Primary
    public OkHttpClient httpClient() {
        return new OkHttpClient();
    }

    /**
     * Custom HTTP client for Open Library API with specific timeouts and connection pool settings.
     */
    @Bean
    @Qualifier("openLibraryClient")
    public OkHttpClient openLibraryHttpClient(OkHttpClient httpClient) {
        return httpClient.newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(5, 5, TimeUnit.MINUTES))     // Max 5 idle connections, 5 minutes keep-alive
            .addInterceptor(new OpenLibraryInterceptor())   // Add custom interceptor
            .build();
    }
}
