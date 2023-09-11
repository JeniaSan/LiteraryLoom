package com.polishuchenko.bookstore.controller;

import com.polishuchenko.bookstore.dto.book.BookDto;
import com.polishuchenko.bookstore.dto.book.BookSearchParameters;
import com.polishuchenko.bookstore.dto.book.CreateBookRequestDto;
import com.polishuchenko.bookstore.service.book.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book management", description = "Endpoints for managing books")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/books")
public class BookController {
    private final BookService bookService;

    @Operation(summary = "Get all books", description = "Get a list of all available books")
    @GetMapping
    public List<BookDto> getAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @Operation(summary = "Get book by id", description = "Get book by id")
    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getById(id);
    }

    @Operation(summary = "Create a new book",
            description = "Create a new book and insert to database")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto bookDto) {
        return bookService.save(bookDto);
    }

    @Operation(summary = "Delete book by id", description = "Delete book by id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @Operation(summary = "Update a book", description = "Update a book to the request values")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable Long id,
                              @RequestBody @Valid CreateBookRequestDto createBookRequestDto) {
        return bookService.updateById(id, createBookRequestDto);
    }

    @Operation(summary = "Search books by params",
            description = "Search books by params. All params : author, isbn, price, title")
    @GetMapping("/search")
    public List<BookDto> searchBooks(BookSearchParameters searchParameters) {
        return bookService.search(searchParameters);
    }
}
