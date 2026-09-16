package com.reduxr.repository;

import com.reduxr.model.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @EntityGraph(attributePaths = "categories")
    Page<Book> findAll(Specification<Book> specification, Pageable pageable);
    
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
