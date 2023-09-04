package com.polishuchenko.bookstore.service;

import com.polishuchenko.bookstore.dto.BookDto;
import com.polishuchenko.bookstore.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    List<BookDto> findAll();

    BookDto getById(Long id);
}
