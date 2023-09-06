package com.polishuchenko.bookstore.dto.book;

import com.polishuchenko.bookstore.dto.SearchParameters;

public record BookSearchParameters(String[] author, String[] isbn,
                                   String[] price, String[] title) implements SearchParameters {
}
