package com.polishuchenko.bookstore.repository.book.specification;

import com.polishuchenko.bookstore.model.Book;
import com.polishuchenko.bookstore.repository.book.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    private static final String KEY = "title";

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root.get(KEY).in(Arrays.stream(params).toArray());
    }

    @Override
    public String getKey() {
        return KEY;
    }
}
