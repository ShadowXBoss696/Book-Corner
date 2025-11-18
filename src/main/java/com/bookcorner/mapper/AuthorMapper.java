package com.bookcorner.mapper;

import com.bookcorner.entity.catalog.Author;
import com.bookcorner.model.catalog.AuthorDetails;
import com.bookcorner.model.catalog.AuthorSummary;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

    // For list views
    public AuthorSummary mapToAuthorSummary(Author author) {
        AuthorSummary summary = new AuthorSummary();
        summary.setId(author.getId());
        summary.setName(author.getName());
        return summary;
    }

    // For detailed views
    public AuthorDetails mapToAuthorDetails(Author author) {
        AuthorDetails details = new AuthorDetails();
        details.setId(author.getId());
        details.setName(author.getName());
        details.setBio(author.getBio());
        details.setOlid(author.getOlid());
        return details;
    }
}
