package com.reduxr;

import com.reduxr.model.Book;
import com.reduxr.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
    @Autowired
    private BookService bookService;
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setTitle("Book");
            book.setAuthor("Author");
            book.setIsbn("ISBN1234");
            book.setPrice(BigDecimal.valueOf(30));
            
            bookService.save(book);
            
            Book book2 = new Book();
            book2.setTitle("Book2");
            book2.setAuthor("Author2");
            book2.setIsbn("ISBN12345");
            book2.setPrice(BigDecimal.valueOf(35));
            
            bookService.save(book2);
            bookService.findAll().forEach(System.out::println);
        };
    }
}
