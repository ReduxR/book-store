package com.reduxr.service;

import com.reduxr.dto.BookDto;
import com.reduxr.dto.BookSearchParametersDto;
import com.reduxr.dto.CreateBookRequestDto;
import com.reduxr.dto.UpdateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto book);
    
    Page<BookDto> findAll(Pageable pageable);
    
    BookDto findById(Long id);
    
    BookDto updateBook(Long id, UpdateBookRequestDto requestDto);
    
    void deleteBook(Long id);
    
    Page<BookDto> findByParams(BookSearchParametersDto params, Pageable pageable);
}
