package com.bookcorner.service;

import com.bookcorner.mapper.AuthorMapper;
import com.bookcorner.model.catalog.AuthorDetails;
import com.bookcorner.model.catalog.AuthorSummary;
import com.bookcorner.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorDetails getAuthorById(UUID id) {
        return authorRepository.findById(id)
                .map(authorMapper::mapToAuthorDetails)
                .orElseThrow(() -> new NoSuchElementException("Author not found with id: " + id));
    }
}
