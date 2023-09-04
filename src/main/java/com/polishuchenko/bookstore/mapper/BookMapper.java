package com.polishuchenko.bookstore.mapper;

import com.polishuchenko.bookstore.config.MapperConfig;
import com.polishuchenko.bookstore.dto.BookDto;
import com.polishuchenko.bookstore.dto.CreateBookRequestDto;
import com.polishuchenko.bookstore.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    @Mapping(target = "id", ignore = true)
    Book toModel(CreateBookRequestDto bookRequestDto);
}
