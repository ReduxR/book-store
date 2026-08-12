package com.reduxr.service;

import com.reduxr.dto.book.BookDto;
import com.reduxr.dto.book.BookDtoWithoutCategoryIds;
import com.reduxr.dto.book.BookSearchParametersDto;
import com.reduxr.dto.book.CreateBookRequestDto;
import com.reduxr.dto.book.UpdateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto book);
    
    Page<BookDtoWithoutCategoryIds> findAll(Pageable pageable);
    
    List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId);
    
    BookDto findById(Long id);
    
    BookDto updateBook(Long id, UpdateBookRequestDto requestDto);
    
    void deleteBook(Long id);
    
    Page<BookDto> findByParams(BookSearchParametersDto params, Pageable pageable);
}
