package com.polishuchenko.bookstore.repository.book;

import com.polishuchenko.bookstore.model.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Query(value = "SELECT * FROM book b JOIN category_book cb ON b.id = cb.book_id"
            + " WHERE cb.category_id = :id", nativeQuery = true)
    List<Book> findAllByCategoryId(Long id);
}
