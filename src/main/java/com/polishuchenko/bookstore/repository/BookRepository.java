package com.polishuchenko.bookstore.repository;

import com.polishuchenko.bookstore.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
