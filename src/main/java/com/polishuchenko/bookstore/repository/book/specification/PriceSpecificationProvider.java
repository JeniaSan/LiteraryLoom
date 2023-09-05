package com.polishuchenko.bookstore.repository.book.specification;

import com.polishuchenko.bookstore.model.Book;
import com.polishuchenko.bookstore.repository.book.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PriceSpecificationProvider implements SpecificationProvider<Book> {
    private static final String KEY = "price";

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(
                root.get(KEY), Arrays.stream(params)
                        .map(Integer::parseInt)
                        .sorted()
                        .toList()
                        .get(params.length - 1));
    }

    @Override
    public String getKey() {
        return KEY;
    }
}
