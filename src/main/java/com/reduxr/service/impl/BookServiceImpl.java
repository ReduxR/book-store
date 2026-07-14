package com.reduxr.service.impl;

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
    
    @Override
    public Book save(Book book) {
        return repository.save(book);
    }
    
    @Override
    public List<Book> findAll() {
        return repository.findAll();
    }
}
