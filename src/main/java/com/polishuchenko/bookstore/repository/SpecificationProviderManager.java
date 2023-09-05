package com.polishuchenko.bookstore.repository;

import com.polishuchenko.bookstore.repository.book.SpecificationProvider;

public interface SpecificationProviderManager<T> {
    SpecificationProvider<T> getSpecificationProvider(String key);
}
