package com.reduxr.controller;

import com.reduxr.dto.BookDto;
import com.reduxr.dto.BookSearchParametersDto;
import com.reduxr.dto.CreateBookRequestDto;
import com.reduxr.dto.UpdateBookRequestDto;
import com.reduxr.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book management", description = "Endpoints for managing books")
@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    
    @Operation(summary = "Get all books", description = "Get a list of all books")
    @GetMapping
    public List<BookDto> getAllBooks(@ParameterObject Pageable pageable) {
        return bookService.findAll(pageable);
    }
    
    @Operation(summary = "Find book by id", description = "Find a book by its id")
    @GetMapping("{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }
    
    @Operation(summary = "Search book by params", description = "Search a book by parameters")
    @GetMapping("/search")
    public List<BookDto> searchBooks(
            BookSearchParametersDto searchParameters, 
            @ParameterObject Pageable pageable) {
        return bookService.findByParams(searchParameters, pageable);
    }
    
    @Operation(summary = "Create book", description = "Create a new book")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.save(requestDto);
    }
    
    @Operation(summary = "Update book", description = "Update parameters of an existing book")
    @PutMapping("/{id}")
    public BookDto updateBook(
            @PathVariable Long id, 
            @RequestBody @Valid UpdateBookRequestDto requestDto) {
        return bookService.updateBook(id, requestDto);
    }
    
    @Operation(summary = "Delete book", description = "Marks book as " 
            + "deleted and makes it unreachable for API (Can be accessed directly from DB only)")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}
