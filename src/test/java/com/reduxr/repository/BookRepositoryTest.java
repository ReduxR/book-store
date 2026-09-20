package com.reduxr.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.reduxr.model.Book;
import com.reduxr.model.Category;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

@DataJpaTest
@Testcontainers
public class BookRepositoryTest {
    @Container
    @ServiceConnection
    protected static final MySQLContainer container = new MySQLContainer("mysql");
    
    @Autowired
    private BookRepository bookRepository;
    
    @Test
    @DisplayName("Find books by their category id")
    @Sql(scripts = "classpath:database/insert-books-with-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = "classpath:database/remove-books-with-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void findByCategoryId_CategoryExists_ReturnsTwoBookIds() {
        Long categoryId = 1L;
        
        Set<Long> expectedBookIds = Set.of(1L, 3L);
        Set<Long> actualBookIds = bookRepository.findAllByCategoryId(categoryId).stream()
                .map(Book::getId)
                .collect(Collectors.toSet());
        
        assertEquals(expectedBookIds, actualBookIds);
    }
    
    @Test
    @DisplayName("Wrong category id returns empty set")
    void findByCategoryId_WrongCategoryId_ReturnsEmptySet() {
        Long categoryId = 70L;
        
        Set<Long> actualIds = bookRepository.findAllByCategoryId(categoryId).stream()
                .map(Book::getId)
                .collect(Collectors.toSet());
        
        assertTrue(actualIds.isEmpty());
    }
    
    @Test
    @DisplayName("Find book by id with its categories")
    @Sql(scripts = "classpath:database/insert-books-with-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = "classpath:database/remove-books-with-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void findById_BookExists_ReturnsBookWithCategoryIds() {
        Long bookId = 1L;
        
        Book book = bookRepository.findById(bookId).orElseThrow();
        
        Set<Long> expectedCategoryIds = Set.of(1L, 2L);
        
        Set<Long> actualCategoryIds = book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        
        assertEquals(expectedCategoryIds, actualCategoryIds);
    }
}
