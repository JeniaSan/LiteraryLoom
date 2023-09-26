package com.polishuchenko.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.polishuchenko.bookstore.dto.category.CategoryDto;
import com.polishuchenko.bookstore.dto.category.CategoryRequestDto;
import com.polishuchenko.bookstore.exception.EntityNotFoundException;
import com.polishuchenko.bookstore.mapper.CategoryMapper;
import com.polishuchenko.bookstore.model.Category;
import com.polishuchenko.bookstore.repository.category.CategoryRepository;
import com.polishuchenko.bookstore.service.category.impl.CategoryServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {
    private static final Long INVALID_ID = 99L;
    private static final Long VALID_ID = 1L;
    private static Category detective;
    private static CategoryDto detectiveDto;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeAll
    public static void setUp() {
        detective = new Category();
        detective.setId(VALID_ID);
        detective.setName("detective");

        detectiveDto = new CategoryDto();
        detectiveDto.setId(detective.getId());
        detectiveDto.setName(detective.getName());
    }

    @Test
    @DisplayName("Find all categories")
    public void findAll_ReturnsListWithAllCategories() {
        when(categoryRepository.findAll()).thenReturn(List.of(detective));
        when(categoryMapper.toDto(ArgumentMatchers.any())).thenReturn(detectiveDto);

        List<CategoryDto> actual = categoryService.findAll();
        assertEquals(List.of(detectiveDto), actual);
    }

    @Test
    @DisplayName("Get category by category id")
    public void getById_validId_ReturnsExpectedCategory() {
        when(categoryRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(detective));
        when(categoryMapper.toDto(ArgumentMatchers.any())).thenReturn(detectiveDto);

        CategoryDto actual = categoryService.getById(detectiveDto.getId());
        assertEquals(detectiveDto, actual);
    }

    @Test
    @DisplayName("Throw exception because category id is invalid")
    public void getById_invalidId_ThrowsException() {
        assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(INVALID_ID));
    }

    @Test
    @DisplayName("Save category to DB")
    public void save_validCategory_ReturnsExpectedCategory() {
        CategoryRequestDto categoryDto = new CategoryRequestDto();
        categoryDto.setName(detective.getName());

        when(categoryRepository.save(ArgumentMatchers.any())).thenReturn(detective);
        when(categoryMapper.toDto(ArgumentMatchers.any())).thenReturn(detectiveDto);

        CategoryDto actual = categoryService.save(categoryDto);
        assertEquals(detectiveDto, actual);
    }

    @Test
    @DisplayName("Update by valid id")
    public void update_validId_ReturnsExpectedCategory() {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setName("New name");
        Category detective = new Category();
        detective.setId(VALID_ID);
        detective.setName(categoryRequestDto.getName());
        CategoryDto detectiveDto = new CategoryDto();
        detectiveDto.setId(detective.getId());
        detectiveDto.setName(detective.getName());

        when(categoryMapper.toEntity(ArgumentMatchers.any())).thenReturn(detective);
        when(categoryRepository.save(ArgumentMatchers.any())).thenReturn(detective);
        when(categoryMapper.toDto(ArgumentMatchers.any())).thenReturn(detectiveDto);

        CategoryDto actual = categoryService.update(detectiveDto.getId(),
                categoryRequestDto);
        assertEquals(detectiveDto, actual);
    }

    @Test
    @DisplayName("Delete by valid id")
    public void delete_validId_DoesntThrowException() {
        assertDoesNotThrow(() -> categoryService.deleteById(detective.getId()));
    }
}
