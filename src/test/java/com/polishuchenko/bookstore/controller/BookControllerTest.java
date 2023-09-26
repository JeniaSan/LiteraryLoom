package com.polishuchenko.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polishuchenko.bookstore.dto.book.BookDto;
import com.polishuchenko.bookstore.dto.book.CreateBookRequestDto;
import com.polishuchenko.bookstore.model.Book;
import java.math.BigDecimal;
import java.util.HashSet;
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
public class BookControllerTest {
    private static MockMvc mockMvc;
    private static final String ADD_BOOK_SCRIPT =
            "classpath:database/books/add-book-kobzar-to-book-table.sql";
    private static final String DELETE_BOOK_SCRIPT =
            "classpath:database/books/delete-book-kobzar-from-book-table.sql";
    private static final String USER_NAME = "user";
    private static final String ADMIN_NAME = "admin";
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 99L;
    private static Book kobzar;
    private static BookDto kobzarDto;
    private static CreateBookRequestDto kobzarRequestDto;
    private static CreateBookRequestDto invalidRequestDto;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();

        kobzar = new Book();
        kobzar.setId(VALID_ID);
        kobzar.setTitle("Kobzar");
        kobzar.setAuthor("Taras Shevchenko");
        kobzar.setIsbn("978-3-16-148410-0");
        kobzar.setPrice(BigDecimal.valueOf(50));
        kobzar.setDescription("some description");
        kobzar.setCoverImage("some link");

        kobzarRequestDto = new CreateBookRequestDto();
        kobzarRequestDto.setTitle(kobzar.getTitle());
        kobzarRequestDto.setAuthor(kobzar.getAuthor());
        kobzarRequestDto.setIsbn(kobzar.getIsbn());
        kobzarRequestDto.setPrice(kobzar.getPrice());
        kobzarRequestDto.setDescription(kobzar.getDescription());
        kobzarRequestDto.setCoverImage(kobzar.getCoverImage());
        kobzarRequestDto.setCategoriesIds(new HashSet<>());

        invalidRequestDto = new CreateBookRequestDto();
        invalidRequestDto.setTitle("");
        invalidRequestDto.setAuthor(kobzar.getAuthor());
        invalidRequestDto.setIsbn(kobzar.getIsbn());
        invalidRequestDto.setPrice(kobzar.getPrice());
        invalidRequestDto.setDescription(kobzar.getDescription());
        invalidRequestDto.setCoverImage(kobzar.getCoverImage());

        kobzarDto = new BookDto();
        kobzarDto.setId(kobzar.getId());
        kobzarDto.setTitle(kobzar.getTitle());
        kobzarDto.setAuthor(kobzar.getAuthor());
        kobzarDto.setIsbn(kobzar.getIsbn());
        kobzarDto.setPrice(kobzar.getPrice());
        kobzarDto.setDescription(kobzar.getDescription());
        kobzarDto.setCoverImage(kobzar.getCoverImage());
    }

    @WithMockUser(username = USER_NAME)
    @Test
    @Sql(
            scripts = ADD_BOOK_SCRIPT,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_BOOK_SCRIPT,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Get list of books")
    void getAll_validRequestDto_ReturnsBookDtosList() throws Exception {
        List<BookDto> expected = List.of(kobzarDto);
        MvcResult mvcResult = mockMvc.perform(get("/books")).andReturn();

        List<BookDto> actual = List.of(objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), BookDto[].class));

        assertTrue(EqualsBuilder.reflectionEquals(
                expected.get(0), actual.get(0), "id", "categoriesIds"));
    }

    @WithMockUser(username = USER_NAME)
    @Test
    @Sql(
            scripts = ADD_BOOK_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_BOOK_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Get book dto by valid id")
    void getBookById_validId_ReturnsExpectedBook() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/books/" + VALID_ID)).andReturn();

        BookDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                BookDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(kobzarDto, actual, "id", "categoriesIds"));
    }

    @WithMockUser(username = USER_NAME)
    @Test
    @Sql(
            scripts = ADD_BOOK_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_BOOK_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Get book by invalid id")
    void getBookById_invalidId_ThrowsException() throws Exception {
        mockMvc.perform(get("/books/" + INVALID_ID))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = ADMIN_NAME, roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = DELETE_BOOK_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Create new book and add to DB")
    void create_validRequest_ReturnsBookDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/books")
                        .content(objectMapper.writeValueAsString(kobzarRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), BookDto.class);
        assertTrue(EqualsBuilder.reflectionEquals(kobzarDto, actual, "id", "categoriesIds"));
    }

    @WithMockUser(username = ADMIN_NAME, roles = {"ADMIN"})
    @Test
    @DisplayName("Create new category with empty name")
    @Sql(
            scripts = DELETE_BOOK_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void create_RequestEmptyTitle_ThrowsException() throws Exception {
        mockMvc.perform(post("/books")
                        .content(objectMapper.writeValueAsString(invalidRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = ADMIN_NAME, roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = ADD_BOOK_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_BOOK_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Delete book by valid id")
    void delete_validId_Successful() throws Exception {
        mockMvc.perform(delete("/books/" + VALID_ID))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = ADMIN_NAME, roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = ADD_BOOK_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_BOOK_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Update book by valid request")
    void update_validRequest_ReturnsExpectedBook() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put("/books/" + VALID_ID)
                        .content(objectMapper.writeValueAsString(kobzarRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), BookDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(kobzarDto, actual, "id", "categoriesIds"));
    }

    @WithMockUser(username = ADMIN_NAME, roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = ADD_BOOK_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_BOOK_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Update book by invalid id request")
    void update_invalidRequest_ThrowsException() throws Exception {
        mockMvc.perform(put("/books/" + INVALID_ID)
                        .content(objectMapper.writeValueAsString(invalidRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = USER_NAME)
    @Test
    @Sql(
            scripts = ADD_BOOK_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_BOOK_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Find all books by valid params")
    public void getAllBooks_validParams_ReturnsBooksList() throws Exception {
        List<BookDto> expected = List.of(kobzarDto);
        MvcResult mvcResult = mockMvc.perform(get("/books/search?author=" + kobzar.getAuthor()))
                .andReturn();

        List<BookDto> actual = List.of(objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), BookDto[].class));

        assertTrue(EqualsBuilder.reflectionEquals(
                expected.get(0), actual.get(0), "id", "categoriesIds"));
    }
}
