package com.polishuchenko.bookstore.service;

import com.polishuchenko.bookstore.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
