package com.reduxr.repository;

import com.reduxr.model.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Query(value = """ 
            SELECT b 
            FROM Book b
            JOIN FETCH b.categories c
            where c.id = :categoryId
            """)
    List<Book> findAllByCategoryId(@Param("categoryId") Long categoryId);
    
    @EntityGraph(attributePaths = "categories")
    Optional<Book> findById(Long id);
}
