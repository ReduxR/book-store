package com.reduxr.service;

import com.reduxr.dto.BookDto;
import com.reduxr.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto book);
    
    List<BookDto> findAll();
    
    BookDto findById(Long id);
}
