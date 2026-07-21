package com.reduxr.service.impl;

import com.reduxr.dto.BookDto;
import com.reduxr.dto.CreateBookRequestDto;
import com.reduxr.dto.UpdateBookRequestDto;
import com.reduxr.exception.DataProcessingException;
import com.reduxr.exception.EntityNotFoundException;
import com.reduxr.mapper.BookMapper;
import com.reduxr.model.Book;
import com.reduxr.repository.BookRepository;
import com.reduxr.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository repository;
    private final BookMapper mapper;
    
    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = repository.save(mapper.toModel(requestDto));
        return mapper.toDto(book);
    }
    
    @Override
    public List<BookDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .toList();
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
        if (id == null) {
            throw new DataProcessingException("Can't delete book with null id");
        }
        repository.deleteById(id);
    }
    
    private Book getBookOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book by id: " + id));
    }
}
