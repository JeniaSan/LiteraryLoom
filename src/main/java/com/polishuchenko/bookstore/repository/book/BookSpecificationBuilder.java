package com.polishuchenko.bookstore.repository.book;

import com.polishuchenko.bookstore.dto.book.BookSearchParameters;
import com.polishuchenko.bookstore.model.Book;
import com.polishuchenko.bookstore.repository.SpecificationBuilder;
import com.polishuchenko.bookstore.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book, BookSearchParameters> {
    private static final String AUTHOR_KEY = "author";
    private static final String ISBN_KEY = "isbn";
    private static final String PRICE_KEY = "price";
    private static final String TITLE_KEY = "title";
    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> specification = Specification.where(null);
        if (isParamsPresent(searchParameters.author())) {
            specification = updateSpecification(
                    specification, AUTHOR_KEY, searchParameters.author());
        }
        if (isParamsPresent(searchParameters.isbn())) {
            specification = updateSpecification(
                    specification, ISBN_KEY, searchParameters.isbn());
        }
        if (isParamsPresent(searchParameters.price())) {
            specification = updateSpecification(
                    specification, PRICE_KEY, searchParameters.price());
        }
        if (isParamsPresent(searchParameters.title())) {
            specification = updateSpecification(
                    specification, TITLE_KEY, searchParameters.title());
        }
        return specification;
    }

    private boolean isParamsPresent(String[] params) {
        return params != null && params.length > 0;
    }

    private Specification<Book> updateSpecification(
            Specification<Book> specification, String key, String[] params) {
        return specification.and(specificationProviderManager.getSpecificationProvider(key)
                .getSpecification(params));
    }
}
