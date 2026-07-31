package com.reduxr.service.impl;

import com.reduxr.dto.book.BookDto;
import com.reduxr.dto.book.BookSearchParametersDto;
import com.reduxr.dto.book.CreateBookRequestDto;
import com.reduxr.dto.book.UpdateBookRequestDto;
import com.reduxr.exception.EntityNotFoundException;
import com.reduxr.mapper.BookMapper;
import com.reduxr.model.Book;
import com.reduxr.repository.BookRepository;
import com.reduxr.service.BookService;
import com.reduxr.specification.book.BookSpecificationBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository repository;
    private final BookMapper mapper;
    private final BookSpecificationBuilder specificationBuilder;
    
    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = repository.save(mapper.toModel(requestDto));
        return mapper.toDto(book);
    }
    
    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDto);
    }
    
    @Override
    public BookDto findById(Long id) {
        return mapper.toDto(getBookOrThrow(id));
    }
    
    @Override
    public BookDto updateBook(Long id, UpdateBookRequestDto requestDto) {
        Book book = getBookOrThrow(id);
        mapper.updateModelFromDto(requestDto, book);
        Book saved = repository.save(book);
        return mapper.toDto(saved);
    }
    
    @Override
    public void deleteBook(Long id) {
        Book book = getBookOrThrow(id);
        repository.delete(book);
    }
    
    @Override
    public Page<BookDto> findByParams(BookSearchParametersDto params, Pageable pageable) {
        Specification<Book> specification = specificationBuilder.build(params);
        return repository.findAll(specification, pageable)
                .map(mapper::toDto);
    }
    
    private Book getBookOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book by id: " + id));
    }
}
