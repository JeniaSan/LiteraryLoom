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
        if (searchParameters.author() != null && searchParameters.author().length > 0) {
            specification = specification.and(
                    specificationProviderManager.getSpecificationProvider(AUTHOR_KEY)
                            .getSpecification(searchParameters.author()));
        }
        if (searchParameters.isbn() != null && searchParameters.isbn().length > 0) {
            specification = specification.and(
                    specificationProviderManager.getSpecificationProvider(ISBN_KEY)
                            .getSpecification(searchParameters.isbn()));
        }
        if (searchParameters.price() != null && searchParameters.price().length > 0) {
            specification = specification.and(
                    specificationProviderManager.getSpecificationProvider(PRICE_KEY)
                            .getSpecification(searchParameters.price()));
        }
        if (searchParameters.title() != null && searchParameters.title().length > 0) {
            specification = specification.and(
                    specificationProviderManager.getSpecificationProvider(TITLE_KEY)
                            .getSpecification(searchParameters.title()));
        }
        return specification;
    }
}
