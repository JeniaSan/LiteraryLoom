package com.polishuchenko.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.polishuchenko.bookstore.dto.cartitem.CartItemRequestDto;
import com.polishuchenko.bookstore.dto.cartitem.CartItemResponseDto;
import com.polishuchenko.bookstore.dto.cartitem.UpdateCartItemDto;
import com.polishuchenko.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.polishuchenko.bookstore.mapper.CartItemMapper;
import com.polishuchenko.bookstore.mapper.ShoppingCartMapper;
import com.polishuchenko.bookstore.model.Book;
import com.polishuchenko.bookstore.model.CartItem;
import com.polishuchenko.bookstore.model.ShoppingCart;
import com.polishuchenko.bookstore.model.User;
import com.polishuchenko.bookstore.repository.book.BookRepository;
import com.polishuchenko.bookstore.repository.cartitem.CartItemRepository;
import com.polishuchenko.bookstore.repository.shoppingcart.ShoppingCartRepository;
import com.polishuchenko.bookstore.repository.user.UserRepository;
import com.polishuchenko.bookstore.service.shoppingcart.impl.ShoppingCartServiceImpl;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceImplTest {
    private static final Long VALID_ID = 1L;
    private static final int DEFAULT_TEST_QUANTITY = 10;
    private static final int UPDATE_TEST_QUANTITY = 100;
    private static Book kobzar;
    private static CartItem kobzarBookItem;
    private static CartItemResponseDto kobzarBookItemDto;
    private static CartItemRequestDto kobzarItemRequest;
    private static ShoppingCart shoppingCart;
    private static ShoppingCartDto shoppingCartDto;
    private static User user;
    private static Authentication authentication;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private CartItemMapper cartItemMapper;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserRepository userRepository;

    @BeforeAll
    public static void setUp() {
        user = new User();
        user.setId(VALID_ID);
        user.setEmail("email@gmail.com");
        user.setFirstName("name");
        user.setLastName("lastName");
        user.setDeleted(false);
        user.setPassword("password");
        user.setRoles(new HashSet<>());
        user.setShippingAddress("shippingAddress");

        kobzar = new Book();
        kobzar.setId(VALID_ID);
        kobzar.setTitle("Kobzar");
        kobzar.setAuthor("Taras Shevchenko");
        kobzar.setIsbn("978-3-16-148410-0");
        kobzar.setPrice(BigDecimal.valueOf(50));
        kobzar.setDescription("some description");
        kobzar.setCoverImage("some link");

        kobzarBookItem = new CartItem();
        kobzarBookItem.setBook(kobzar);
        kobzarBookItem.setQuantity(DEFAULT_TEST_QUANTITY);
        kobzarBookItem.setId(VALID_ID);
        kobzarBookItem.setDeleted(false);
        kobzarBookItem.setShoppingCart(shoppingCart);

        kobzarBookItemDto = new CartItemResponseDto();
        kobzarBookItemDto.setBookId(kobzar.getId());
        kobzarBookItemDto.setQuantity(DEFAULT_TEST_QUANTITY);
        kobzarBookItemDto.setId(VALID_ID);
        kobzarBookItemDto.setBookTitle(kobzar.getTitle());

        kobzarItemRequest = new CartItemRequestDto(kobzar.getId(), DEFAULT_TEST_QUANTITY);

        shoppingCart = new ShoppingCart();
        shoppingCart.setId(VALID_ID);
        shoppingCart.setUser(user);
        shoppingCart.setDeleted(false);
        shoppingCart.setCartItems(new HashSet<>());

        shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setId(VALID_ID);
        shoppingCartDto.setUserId(user.getId());
        shoppingCartDto.setCartItems(new HashSet<>());

        authentication = new UsernamePasswordAuthenticationToken(user, null);
    }

    @Test
    @DisplayName("Update by valid id")
    public void updateById_validId_returnsExpected() {
        UpdateCartItemDto updateCartItemDto = new UpdateCartItemDto();
        updateCartItemDto.setQuantity(UPDATE_TEST_QUANTITY);

        CartItem cartItem = new CartItem();
        cartItem.setId(VALID_ID);
        cartItem.setQuantity(updateCartItemDto.getQuantity());

        CartItemResponseDto cartItemResponseDto = new CartItemResponseDto();
        cartItemResponseDto.setId(cartItem.getId());
        cartItemResponseDto.setQuantity(updateCartItemDto.getQuantity());

        when(cartItemRepository.findCartItemById(any())).thenReturn(Optional.of(cartItem));

        CartItem updatedCartItem = new CartItem();
        cartItem.setId(VALID_ID);
        cartItem.setQuantity(UPDATE_TEST_QUANTITY);

        when(cartItemRepository.save(any())).thenReturn(updatedCartItem);
        when(cartItemMapper.toDto(any())).thenReturn(cartItemResponseDto);

        CartItemResponseDto actual = shoppingCartService.update(VALID_ID, updateCartItemDto);
        assertEquals(cartItemResponseDto, actual);
    }

    @Test
    @DisplayName("Delete by valid id")
    public void delete_validId_doesntThrowsException() {
        assertDoesNotThrow(() -> shoppingCartService.delete(VALID_ID));
    }

    @Test
    @DisplayName("Clear shopping cart")
    public void clear_validCart_successful() {
        when(shoppingCartRepository.findShoppingCartByUserId(anyLong())).thenReturn(shoppingCart);

        assertDoesNotThrow(() -> shoppingCartService.clear(shoppingCart, authentication));
    }

    @Test
    @DisplayName("Get shopping cart")
    public void getShoppingCart_returnsExpectedShoppingCart() {
        when(userRepository.findUserByUsername(any())).thenReturn(user);
        when(shoppingCartRepository.findShoppingCartByUserId(anyLong())).thenReturn(shoppingCart);
        when(shoppingCartMapper.toDto(ArgumentMatchers.any())).thenReturn(shoppingCartDto);

        ShoppingCartDto actual = shoppingCartService.getShoppingCart(authentication);
        assertEquals(shoppingCartDto, actual);
    }

    @Test
    @DisplayName("Add cart item to shopping cart")
    public void addCartItem_validRequest_returnsExpectedItem() {
        when(userRepository.findUserByUsername(any())).thenReturn(user);
        when(shoppingCartRepository.findShoppingCartByUserId(anyLong())).thenReturn(shoppingCart);
        when(bookRepository.findById(any())).thenReturn(Optional.of(kobzar));
        when(cartItemRepository.save(any())).thenReturn(kobzarBookItem);
        when(cartItemMapper.toDto(kobzarBookItem)).thenReturn(kobzarBookItemDto);

        CartItemResponseDto actual = shoppingCartService
                .addCartItem(kobzarItemRequest, authentication);

        assertEquals(actual, kobzarBookItemDto);
    }

    @Test
    @DisplayName("Returns current user shopping cart")
    public void getCurrentUserCart_returnsExpectedCart() {
        when(shoppingCartRepository.findShoppingCartByUserId(anyLong())).thenReturn(shoppingCart);
        when(cartItemRepository.getCartItemsByShoppingCartId(anyLong()))
                .thenReturn(Set.of(kobzarBookItem));

        ShoppingCart actual = shoppingCartService.getCurrentUserCart(user);
        assertEquals(actual, shoppingCart);
    }

    @Test
    @DisplayName("Create a new shopping cart")
    public void createShoppingCart_validRequest_successful() {
        assertDoesNotThrow(() -> shoppingCartService.createShoppingCart(user));
    }
}
