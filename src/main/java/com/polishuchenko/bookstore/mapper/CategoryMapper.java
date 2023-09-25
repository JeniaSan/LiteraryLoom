package com.polishuchenko.bookstore.mapper;

import com.polishuchenko.bookstore.config.MapperConfiguration;
import com.polishuchenko.bookstore.dto.category.CategoryDto;
import com.polishuchenko.bookstore.dto.category.CategoryRequestDto;
import com.polishuchenko.bookstore.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category requestToEntity(CategoryRequestDto categoryDto);

    Category toEntity(CategoryRequestDto categoryDto);
}
