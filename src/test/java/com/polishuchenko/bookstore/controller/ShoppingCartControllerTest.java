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
import com.polishuchenko.bookstore.dto.cartitem.CartItemRequestDto;
import com.polishuchenko.bookstore.dto.cartitem.CartItemResponseDto;
import com.polishuchenko.bookstore.dto.cartitem.UpdateCartItemDto;
import com.polishuchenko.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.polishuchenko.bookstore.model.User;
import java.math.BigDecimal;
import java.util.HashSet;
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
public class ShoppingCartControllerTest {
    private static MockMvc mockMvc;
    private static final int DEFAULT_TEST_QUANTITY = 10;
    private static final String USER_NAME = "user";
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 99L;
    private static final String ADD_USER_SCRIPT =
            "classpath:database/user/add-user-into-users-table.sql";
    private static final String ADD_CART_ITEM_SCRIPT =
            "classpath:database/cart_item/add-cart-item-into-table.sql";
    private static final String ADD_SHOPPING_CART_SCRIPT =
            "classpath:database/shopping_cart/add-shopping-cart-into-table.sql";
    private static final String DELETE_USER_SCRIPT =
            "classpath:database/user/delete-user-from-users-table.sql";
    private static final String DELETE_CART_ITEM_SCRIPT =
            "classpath:database/cart_item/delete-cart-item-from-table.sql";
    private static final String DELETE_SHOPPING_CART_SCRIPT =
            "classpath:database/shopping_cart/delete-shopping-cart-from-table.sql";
    private static final String ADD_BOOK_SCRIPT =
            "classpath:database/books/add-book-kobzar-to-book-table.sql";
    private static final String DELETE_BOOK_SCRIPT =
            "classpath:database/books/delete-book-kobzar-from-book-table.sql";
    private static CartItemRequestDto kobzarItemRequest;
    private static UpdateCartItemDto kobzarUpdateDto;
    private static BookDtoWithoutCategoryIds kobzar;
    private static ShoppingCartDto shoppingCartDto;
    private static User user;
    private static CartItemResponseDto kobzarBookItemDto;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();

        user = new User();
        user.setId(VALID_ID);
        user.setEmail("email@gmail.com");
        user.setFirstName("user");
        user.setLastName("lastName");
        user.setDeleted(false);
        user.setPassword("password");
        user.setRoles(new HashSet<>());
        user.setShippingAddress("shippingAddress");

        kobzar = new BookDtoWithoutCategoryIds();
        kobzar.setId(VALID_ID);
        kobzar.setTitle("Kobzar");
        kobzar.setAuthor("Taras Shevchenko");
        kobzar.setIsbn("978-3-16-148410-0");
        kobzar.setPrice(BigDecimal.valueOf(50));
        kobzar.setDescription("some description");
        kobzar.setCoverImage("some link");

        shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setId(VALID_ID);
        shoppingCartDto.setUserId(user.getId());
        shoppingCartDto.setCartItems(new HashSet<>());

        kobzarItemRequest = new CartItemRequestDto(VALID_ID, DEFAULT_TEST_QUANTITY);

        kobzarBookItemDto = new CartItemResponseDto();
        kobzarBookItemDto.setBookId(kobzar.getId());
        kobzarBookItemDto.setQuantity(DEFAULT_TEST_QUANTITY);
        kobzarBookItemDto.setId(VALID_ID);
        kobzarBookItemDto.setBookTitle(kobzar.getTitle());

        kobzarUpdateDto = new UpdateCartItemDto();
        kobzarUpdateDto.setQuantity(DEFAULT_TEST_QUANTITY);
    }

    @WithMockUser(username = USER_NAME)
    @Test
    @Sql(
            scripts = {ADD_USER_SCRIPT, ADD_SHOPPING_CART_SCRIPT},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {DELETE_USER_SCRIPT, DELETE_SHOPPING_CART_SCRIPT},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Get shopping cart")
    void getShoppingCart_returnsExpectedCart() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/cart")).andReturn();

        ShoppingCartDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), ShoppingCartDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(shoppingCartDto, actual, "id"));
    }

    @WithMockUser(username = USER_NAME)
    @Test
    @Sql(
            scripts = {ADD_USER_SCRIPT, ADD_SHOPPING_CART_SCRIPT, ADD_BOOK_SCRIPT},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {DELETE_USER_SCRIPT, DELETE_SHOPPING_CART_SCRIPT,
                    DELETE_CART_ITEM_SCRIPT, DELETE_BOOK_SCRIPT},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Add new cart item to shopping cart")
    void addCartItem_validRequest_returnsExpectedCartItem() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/cart")
                        .content(objectMapper.writeValueAsString(kobzarItemRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CartItemResponseDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), CartItemResponseDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(kobzarBookItemDto, actual, "id"));
    }

    @WithMockUser(username = USER_NAME)
    @Test
    @DisplayName("Update cart item")
    @Sql(
            scripts = {ADD_USER_SCRIPT, ADD_BOOK_SCRIPT,
                    ADD_SHOPPING_CART_SCRIPT, ADD_CART_ITEM_SCRIPT},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {DELETE_USER_SCRIPT, DELETE_BOOK_SCRIPT,
                    DELETE_SHOPPING_CART_SCRIPT, DELETE_CART_ITEM_SCRIPT},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void update_validRequest_returnsUpdatedCartItem() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put("/cart/cart-items/" + VALID_ID)
                        .content(objectMapper.writeValueAsString(kobzarUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CartItemResponseDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), CartItemResponseDto.class);
        assertTrue(EqualsBuilder.reflectionEquals(kobzarBookItemDto, actual, "id"));
    }

    @WithMockUser(username = USER_NAME)
    @Test
    @DisplayName("Update cart item by invalid id")
    @Sql(
            scripts = {ADD_USER_SCRIPT, ADD_BOOK_SCRIPT,
                    ADD_SHOPPING_CART_SCRIPT, ADD_CART_ITEM_SCRIPT},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {DELETE_USER_SCRIPT, DELETE_BOOK_SCRIPT,
                    DELETE_SHOPPING_CART_SCRIPT, DELETE_CART_ITEM_SCRIPT},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void update_invalidRequest_throwsException() throws Exception {
        mockMvc.perform(put("/cart/cart-items/" + INVALID_ID)
                        .content(objectMapper.writeValueAsString(kobzarUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = USER_NAME)
    @Test
    @Sql(
            scripts = {ADD_USER_SCRIPT, ADD_SHOPPING_CART_SCRIPT, ADD_CART_ITEM_SCRIPT},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {DELETE_USER_SCRIPT, DELETE_SHOPPING_CART_SCRIPT, DELETE_CART_ITEM_SCRIPT},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Delete cart item by valid id")
    void delete_validId_successful() throws Exception {
        mockMvc.perform(delete("/cart/cart-items/" + VALID_ID))
                .andExpect(status().isOk());
    }
}
