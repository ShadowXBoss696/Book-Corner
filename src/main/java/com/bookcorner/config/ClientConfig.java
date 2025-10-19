package com.bookcorner.config;

import com.bookcorner.util.OkHttpClientFactory;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

    @Bean
    public OkHttpClient httpClient() {
        return OkHttpClientFactory.defaultClient();
    }
}
