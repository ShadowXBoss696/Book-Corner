package com.bookcorner.util;

import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OkHttpClientFactoryTest {

    @Test
    void defaultClient_notNull() {
        assertNotNull(OkHttpClientFactory.defaultClient(), "Default client should not be null");
    }

    @Test
    void defaultClient_singleton() {
        OkHttpClient client1 = OkHttpClientFactory.defaultClient();
        OkHttpClient client2 = OkHttpClientFactory.defaultClient();
        assertSame(client1, client2, "Default client should be a singleton instance");
    }

    @Test
    void defaultClient_isConfigurable() {
        OkHttpClient client = OkHttpClientFactory.defaultClient();
        OkHttpClient newClient = client.newBuilder()
                .retryOnConnectionFailure(true)
                .build();
        assertNotSame(client, newClient, "New client should be a different instance after configuration");
        assertSame(client.connectionPool(), newClient.connectionPool(), "Connection pool should be shared");
    }
}
