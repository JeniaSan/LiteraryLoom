package com.polishuchenko.bookstore.repository.book.specification;

import com.polishuchenko.bookstore.model.Book;
import com.polishuchenko.bookstore.repository.book.SpecificationProvider;
import java.util.Comparator;
import java.util.stream.Stream;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PriceSpecificationProvider implements SpecificationProvider<Book> {
    private static final String KEY = "price";

    @Override
    public Specification<Book> getSpecification(String[] params) {
        if (params.length == 1) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(
                    root.get(KEY), Stream.of(params)
                            .map(Integer::parseInt)
                            .max(Comparator.comparingInt(Integer::intValue))
                            .orElse(Integer.MAX_VALUE));
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
            criteriaBuilder.greaterThanOrEqualTo(root.get(KEY), Stream.of(params)
                    .map(Integer::parseInt)
                    .min(Comparator.comparingInt(Integer::intValue))
                    .get()),
                criteriaBuilder.lessThanOrEqualTo(root.get(KEY), Stream.of(params)
                        .map(Integer::parseInt)
                        .max(Comparator.comparingInt(Integer::intValue))
                        .get()));
    }

    @Override
    public String getKey() {
        return KEY;
    }
}

