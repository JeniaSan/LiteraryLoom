package com.polishuchenko.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polishuchenko.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.polishuchenko.bookstore.dto.category.CategoryDto;
import com.polishuchenko.bookstore.dto.category.CategoryRequestDto;
import java.math.BigDecimal;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {
    private static MockMvc mockMvc;
    private static final String ADD_CATEGORY_SCRIPT =
            "classpath:database/categories/add-category-detective-to-category-table.sql";
    private static final String DELETE_CATEGORY_SCRIPT =
            "classpath:database/categories/remove-category-detective-from-category-table.sql";
    private static final String ADD_BOOK_SCRIPT =
            "classpath:database/books/add-book-kobzar-to-book-table.sql";
    private static final String DELETE_BOOK_SCRIPT =
            "classpath:database/books/delete-book-kobzar-from-book-table.sql";
    private static final String ADD_CATEGORY_BOOK_SCRIPT =
            "classpath:database/books_categories/add-kobzar-detective-to-category_book-table.sql";
    private static final String DELETE_CATEGORY_BOOK_SCRIPT =
            "classpath:database/books_categories/delete-from-category_book-table.sql";
    private static final String USER_NAME = "user";
    private static final String ADMIN_NAME = "admin";
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 99L;
    private static CategoryDto detectiveResponse;
    private static CategoryRequestDto validRequestDto;
    private static CategoryRequestDto invalidRequestDto;
    private static BookDtoWithoutCategoryIds kobzar;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();

        kobzar = new BookDtoWithoutCategoryIds();
        kobzar.setId(1L);
        kobzar.setTitle("Kobzar");
        kobzar.setAuthor("Taras Shevchenko");
        kobzar.setIsbn("978-3-16-148410-0");
        kobzar.setPrice(BigDecimal.valueOf(50));
        kobzar.setDescription("some description");
        kobzar.setCoverImage("some link");

        detectiveResponse = new CategoryDto();
        detectiveResponse.setId(VALID_ID);
        detectiveResponse.setName("detective");
        detectiveResponse.setDescription("some description");

        validRequestDto = new CategoryRequestDto();
        validRequestDto.setName(detectiveResponse.getName());
        validRequestDto.setDescription(detectiveResponse.getDescription());

        invalidRequestDto = new CategoryRequestDto();
        invalidRequestDto.setName("");
    }

    @WithMockUser(username = ADMIN_NAME, roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = DELETE_CATEGORY_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Create new category and add to DB")
    void create_validRequest_ReturnsCategoryDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/categories")
                        .content(objectMapper.writeValueAsString(validRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), CategoryDto.class);
        assertTrue(EqualsBuilder.reflectionEquals(detectiveResponse, actual, "id"));
    }

    @WithMockUser(username = ADMIN_NAME, roles = {"ADMIN"})
    @Test
    @DisplayName("Create new category with empty name")
    @Sql(
            scripts = DELETE_CATEGORY_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void create_RequestEmptyName_ThrowsException() throws Exception {
        mockMvc.perform(post("/categories")
                        .content(objectMapper.writeValueAsString(invalidRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = USER_NAME)
    @Test
    @Sql(
            scripts = ADD_CATEGORY_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_CATEGORY_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Get list of categories")
    void getAll_ReturnsCategoryDtosList() throws Exception {
        List<CategoryDto> expected = List.of(detectiveResponse);
        MvcResult mvcResult = mockMvc.perform(get("/categories")).andReturn();

        List<CategoryDto> actual = List.of(objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), CategoryDto[].class));

        assertTrue(EqualsBuilder.reflectionEquals(expected.get(0), actual.get(0), "id"));
    }

    @WithMockUser(username = USER_NAME)
    @Test
    @Sql(
            scripts = ADD_CATEGORY_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_CATEGORY_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Get category by valid id")
    void getCategoryById_validId_ReturnsExpectedCategory() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/categories/" + VALID_ID)).andReturn();

        CategoryDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                CategoryDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(detectiveResponse, actual, "id"));
    }

    @WithMockUser(username = USER_NAME)
    @Test
    @Sql(
            scripts = ADD_CATEGORY_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_CATEGORY_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Get category by invalid id")
    void getCategoryById_invalidId_ThrowsException() throws Exception {
        mockMvc.perform(get("/categories/" + INVALID_ID))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = ADMIN_NAME, roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = ADD_CATEGORY_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_CATEGORY_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Update category by valid request")
    void update_validRequest_ReturnsExpectedCategory() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put("/categories/" + VALID_ID)
                        .content(objectMapper.writeValueAsString(validRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), CategoryDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(detectiveResponse, actual, "id"));
    }

    @WithMockUser(username = ADMIN_NAME, roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = ADD_CATEGORY_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_CATEGORY_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Update category by invalid id request")
    void update_invalidRequest_ThrowsException() throws Exception {
        mockMvc.perform(put("/categories/" + INVALID_ID)
                        .content(objectMapper.writeValueAsString(invalidRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = ADMIN_NAME, roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = ADD_CATEGORY_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_CATEGORY_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Delete category by valid id")
    void delete_validId_Successful() throws Exception {
        mockMvc.perform(delete("/categories/" + VALID_ID))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = USER_NAME)
    @Test
    @Sql(
            scripts = {
                    ADD_CATEGORY_SCRIPT, ADD_BOOK_SCRIPT, ADD_CATEGORY_BOOK_SCRIPT
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {DELETE_CATEGORY_BOOK_SCRIPT, DELETE_CATEGORY_SCRIPT, DELETE_BOOK_SCRIPT
            }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Get book by valid category id")
    void getBooksByCategoryId_validId_returnListOfBookDtosWithoutCategoryId() throws Exception {
        List<BookDtoWithoutCategoryIds> expected = List.of(kobzar);
        MvcResult mvcResult = mockMvc.perform(get("/categories/" + VALID_ID + "/books"))
                .andReturn();

        List<BookDtoWithoutCategoryIds> actual = List.of(objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), BookDtoWithoutCategoryIds[].class));

        assertTrue(EqualsBuilder
                .reflectionEquals(expected.get(0), actual.get(0), "id"));
    }
}
