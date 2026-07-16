package com.reduxr.service.impl;

import com.reduxr.dto.BookDto;
import com.reduxr.dto.CreateBookRequestDto;
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
        Book book = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book by id: " + id));
        return mapper.toDto(book);
    }
}
