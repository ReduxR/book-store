package com.reduxr.controller;

import com.reduxr.dto.BookDto;
import com.reduxr.dto.BookSearchParametersDto;
import com.reduxr.dto.CreateBookRequestDto;
import com.reduxr.dto.UpdateBookRequestDto;
import com.reduxr.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    
    @GetMapping
    public List<BookDto> getAll() {
        return bookService.findAll();
    }
    
    @GetMapping("{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }
    
    @GetMapping("/search")
    public List<BookDto> searchBooks(BookSearchParametersDto searchParameters) {
        return bookService.findByParams(searchParameters);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto createBook(@RequestBody CreateBookRequestDto requestDto) {
        return bookService.save(requestDto);
    }
    
    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable Long id, @RequestBody UpdateBookRequestDto requestDto) {
        return bookService.updateBook(id, requestDto);
    }
    
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
