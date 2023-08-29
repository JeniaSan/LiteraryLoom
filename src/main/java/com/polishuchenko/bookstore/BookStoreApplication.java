package com.polishuchenko.bookstore;

import com.polishuchenko.bookstore.model.Book;
import com.polishuchenko.bookstore.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookStoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book kobzar = new Book();
            kobzar.setTitle("Kobzar");
            kobzar.setAuthor("Taras Shevchenko");
            kobzar.setPrice(BigDecimal.valueOf(200));
            kobzar.setIsbn("9789664391136");

            bookService.save(kobzar);
            bookService.findAll().forEach(System.out::println);
        };
    }
}
