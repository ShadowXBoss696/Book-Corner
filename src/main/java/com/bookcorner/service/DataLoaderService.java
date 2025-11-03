package com.bookcorner.service;

import com.bookcorner.client.OpenLibraryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataLoaderService {

    private final OpenLibraryClient externalClient;
}
