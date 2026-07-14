package com.reduxr.service;

import com.reduxr.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);
    
    List<Book> findAll();
}
