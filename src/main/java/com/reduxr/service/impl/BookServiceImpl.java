package com.reduxr.service.impl;

import com.reduxr.dto.book.BookDto;
import com.reduxr.dto.book.BookDtoWithoutCategoryIds;
import com.reduxr.dto.book.BookSearchParametersDto;
import com.reduxr.dto.book.CreateBookRequestDto;
import com.reduxr.dto.book.UpdateBookRequestDto;
import com.reduxr.exception.EntityNotFoundException;
import com.reduxr.mapper.BookMapper;
import com.reduxr.model.Book;
import com.reduxr.model.Category;
import com.reduxr.repository.BookRepository;
import com.reduxr.repository.CategoryRepository;
import com.reduxr.service.BookService;
import com.reduxr.specification.book.BookSpecificationBuilder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper mapper;
    private final BookSpecificationBuilder specificationBuilder;
    private final CategoryRepository categoryRepository;
    
    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = mapper.toModel(requestDto);
        List<Category> categories = categoryRepository.findAllById(requestDto.getCategoryIds());
        book.setCategories(new HashSet<>(categories));
        return mapper.toDto(bookRepository.save(book));
    }
    
    @Override
    public Page<BookDtoWithoutCategoryIds> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(mapper::toDtoWithoutCategories);
    }
    
    @Override
    public BookDto findById(Long id) {
        return mapper.toDto(getBookOrThrow(id));
    }
    
    @Override
    public List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId) {
        return bookRepository.findAllByCategoryId(categoryId).stream()
                .map(mapper::toDtoWithoutCategories)
                .toList();
    }
    
    @Override
    public BookDto updateBook(Long id, UpdateBookRequestDto requestDto) {
        Book book = getBookOrThrow(id);
        Set<Category> categories = 
                new HashSet<>(categoryRepository.findAllById(requestDto.getCategoryIds()));
        
        book.setCategories(categories);
        mapper.updateModelFromDto(requestDto, book);
        
        return mapper.toDto(bookRepository.save(book));
    }
    
    @Override
    public void deleteBook(Long id) {
        Book book = getBookOrThrow(id);
        bookRepository.delete(book);
    }
    
    @Override
    public Page<BookDto> findByParams(BookSearchParametersDto params, Pageable pageable) {
        Specification<Book> specification = specificationBuilder.build(params);
        return bookRepository.findAll(specification, pageable)
                .map(mapper::toDto);
    }
    
    private Book getBookOrThrow(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book by id: " + id));
    }
}
