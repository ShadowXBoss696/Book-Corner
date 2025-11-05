package com.bookcorner.util;

import okhttp3.OkHttpClient;

public class OkHttpClientFactory {

    // Base client, can be customized if needed
    private static final OkHttpClient BASE_CLIENT = new OkHttpClient.Builder()
            .build();

    public static OkHttpClient getHttpClient() {
        return BASE_CLIENT;
    }
}
