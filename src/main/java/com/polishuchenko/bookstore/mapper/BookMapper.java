package com.polishuchenko.bookstore.mapper;

import com.polishuchenko.bookstore.config.MapperConfiguration;
import com.polishuchenko.bookstore.dto.book.BookDto;
import com.polishuchenko.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.polishuchenko.bookstore.dto.book.CreateBookRequestDto;
import com.polishuchenko.bookstore.model.Book;
import com.polishuchenko.bookstore.model.Category;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfiguration.class)
public interface BookMapper {
    BookDto toDto(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", ignore = true)
    Book toModel(CreateBookRequestDto bookRequestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        bookDto.setCategoriesIds(book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));
    }

    default Set<Category> mapToCategorySet(Set<Long> categoryIds) {
        return categoryIds.stream()
                .map(id -> {
                    Category category = new Category();
                    category.setId(id);
                    return category;
                })
                .collect(Collectors.toSet());
    }
}
