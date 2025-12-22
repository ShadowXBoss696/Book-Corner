package com.bookcorner.app.mapper;

import com.bookcorner.app.model.dto.BookSummary;
import com.bookcorner.app.model.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public BookSummary convertToBookSummary(Book book) {
        if (book == null) {
            return null;
        }

        BookSummary summary = new BookSummary();
        summary.setId(book.getId());
        summary.setTitle(book.getTitle());
        return summary;
    }
}
