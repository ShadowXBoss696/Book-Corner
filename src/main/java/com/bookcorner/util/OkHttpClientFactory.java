package com.bookcorner.util;

import okhttp3.OkHttpClient;

public class OkHttpClientFactory {

    // Base client, can be customized if needed
    private static final OkHttpClient DEFAULT_CLIENT = new OkHttpClient.Builder()
            .build();

    // Get default client
    public static OkHttpClient defaultClient() {
        return DEFAULT_CLIENT;
    }
}
