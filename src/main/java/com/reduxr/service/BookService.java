package com.reduxr.service;

import com.reduxr.dto.BookDto;
import com.reduxr.dto.BookSearchParametersDto;
import com.reduxr.dto.CreateBookRequestDto;
import com.reduxr.dto.UpdateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto book);
    
    List<BookDto> findAll();
    
    BookDto findById(Long id);
    
    BookDto updateBook(Long id, UpdateBookRequestDto requestDto);
    
    void deleteBook(Long id);
    
    List<BookDto> findByParams(BookSearchParametersDto params);
}
