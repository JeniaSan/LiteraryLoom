package com.polishuchenko.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.polishuchenko.bookstore.dto.book.BookDto;
import com.polishuchenko.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.polishuchenko.bookstore.dto.book.BookSearchParameters;
import com.polishuchenko.bookstore.dto.book.CreateBookRequestDto;
import com.polishuchenko.bookstore.exception.EntityNotFoundException;
import com.polishuchenko.bookstore.mapper.BookMapper;
import com.polishuchenko.bookstore.model.Book;
import com.polishuchenko.bookstore.repository.book.BookRepository;
import com.polishuchenko.bookstore.repository.book.BookSpecificationBuilder;
import com.polishuchenko.bookstore.service.book.impl.BookServiceImpl;
import java.math.BigDecimal;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {
    private static final Long INVALID_ID = 99L;
    private static final Long VALID_ID = 1L;
    private static Book kobzar;
    private static BookDto kobzarDto;
    private static CreateBookRequestDto kobzarRequestDto;
    private static BookDtoWithoutCategoryIds kobzarDtoWithoutCategory;
    @Mock
    private BookSpecificationBuilder specificationBuilder;

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @BeforeAll
    public static void setUp() {
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

        kobzarDto = new BookDto();
        kobzarDto.setId(kobzar.getId());
        kobzarDto.setTitle(kobzar.getTitle());
        kobzarDto.setAuthor(kobzar.getAuthor());
        kobzarDto.setIsbn(kobzar.getIsbn());
        kobzarDto.setPrice(kobzar.getPrice());
        kobzarDto.setDescription(kobzar.getDescription());
        kobzarDto.setCoverImage(kobzar.getCoverImage());

        kobzarDtoWithoutCategory = new BookDtoWithoutCategoryIds();
        kobzarDtoWithoutCategory.setId(kobzar.getId());
        kobzarDtoWithoutCategory.setId(1L);
        kobzarDtoWithoutCategory.setAuthor(kobzar.getAuthor());
        kobzarDtoWithoutCategory.setIsbn(kobzar.getIsbn());
        kobzarDtoWithoutCategory.setTitle(kobzar.getTitle());
        kobzarDtoWithoutCategory.setCoverImage(kobzar.getCoverImage());
        kobzarDtoWithoutCategory.setPrice(kobzar.getPrice());
        kobzarDtoWithoutCategory.setDescription(kobzar.getDescription());
    }

    @Test
    @DisplayName("Save book to DB")
    public void save_validBook_returnsExpectedBook() {
        when(bookMapper.toModel(kobzarRequestDto)).thenReturn(kobzar);
        when(bookRepository.save(any(Book.class))).thenReturn(kobzar);
        when(bookMapper.toDto(kobzar)).thenReturn(kobzarDto);

        BookDto actual = bookService.save(kobzarRequestDto);

        verify(bookRepository).save(kobzar);
        assertNotNull(actual);
        assertEquals(actual, kobzarDto);
    }

    @Test
    @DisplayName("Find all books")
    public void findAll_returnsBooksList() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(kobzar);
        List<BookDto> bookDtos = List.of(kobzarDto);

        when(bookRepository.findAll(pageable)).thenReturn(new PageImpl<>(books));
        when(bookMapper.toDto(any(Book.class))).thenReturn(bookDtos.get(0));

        List<BookDto> actual = bookService.findAll(pageable);

        verify(bookRepository).findAll(pageable);
        assertEquals(bookDtos, actual);
    }

    @Test
    @DisplayName("Get book by valid id")
    public void getById_validId_returnsExpectedBook() {
        when(bookRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(kobzar));
        when(bookMapper.toDto(ArgumentMatchers.any())).thenReturn(kobzarDto);

        BookDto actual = bookService.getById(kobzarDto.getId());
        assertEquals(kobzarDto, actual);
    }

    @Test
    @DisplayName("Throw exception because book id is invalid")
    public void getById_invalidId_throwsException() {
        assertThrows(EntityNotFoundException.class,
                () -> bookService.getById(INVALID_ID));
    }

    @Test
    @DisplayName("Delete by valid id")
    public void delete_validId_successful() {
        assertDoesNotThrow(() -> bookService.deleteById(kobzar.getId()));
    }

    @Test
    @DisplayName("Update by valid id")
    public void updateById_validId_returnsExpected() {
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setAuthor("New Author");
        Book book = new Book();
        book.setId(kobzar.getId());
        book.setAuthor(bookRequestDto.getAuthor());
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setAuthor(book.getAuthor());

        when(bookMapper.toModel(ArgumentMatchers.any())).thenReturn(book);
        when(bookRepository.save(ArgumentMatchers.any())).thenReturn(book);
        when(bookMapper.toDto(ArgumentMatchers.any())).thenReturn(bookDto);

        BookDto actual = bookService.updateById(book.getId(),
                bookRequestDto);
        assertEquals(bookDto, actual);
    }

    @Test
    @DisplayName("Find all books by params")
    public void getAllBooks_validParams_returnsBooksList() {
        BookSearchParameters searchParameters = new BookSearchParameters(
                new String[]{"kobzar"}, new String[0], new String[0], new String[0]);
        Specification<Book> specification = mock(Specification.class);
        List<Book> books = List.of(kobzar);

        when(specificationBuilder.build(searchParameters)).thenReturn(specification);
        when(bookRepository.findAll(specification)).thenReturn(books);
        when(bookMapper.toDto(kobzar)).thenReturn(kobzarDto);

        List<BookDto> bookDtos = List.of(kobzarDto);
        List<BookDto> actual = bookService.search(searchParameters);
        assertEquals(bookDtos, actual);
    }

    @Test
    @DisplayName("Find all books by valid category id")
    public void getAllBooks_validCategoryId_returnsBooksList() {
        List<Book> books = List.of(kobzar);
        final List<BookDtoWithoutCategoryIds> bookDtos = List.of(kobzarDtoWithoutCategory);

        when(bookRepository.findAllByCategories_Id(anyLong())).thenReturn(books);
        when(bookMapper.toDtoWithoutCategories(kobzar)).thenReturn(kobzarDtoWithoutCategory);

        List<BookDtoWithoutCategoryIds> actual = bookService.findAllByCategoryId(VALID_ID);

        verify(bookRepository).findAllByCategories_Id(VALID_ID);
        verify(bookMapper).toDtoWithoutCategories(kobzar);

        assertEquals(bookDtos, actual);
    }
}

