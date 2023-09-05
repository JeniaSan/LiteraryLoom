package com.polishuchenko.bookstore.service.impl;

import com.polishuchenko.bookstore.dto.BookDto;
import com.polishuchenko.bookstore.dto.CreateBookRequestDto;
import com.polishuchenko.bookstore.exception.EntityNotFoundException;
import com.polishuchenko.bookstore.mapper.BookMapper;
import com.polishuchenko.bookstore.model.Book;
import com.polishuchenko.bookstore.repository.BookRepository;
import com.polishuchenko.bookstore.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto book) {
        Book model = bookMapper.toModel(book);
        return bookMapper.toDto(bookRepository.save(model));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't get book with id= " + id));
        return bookMapper.toDto(book);
    }
}
