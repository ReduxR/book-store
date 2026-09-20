package com.reduxr.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.reduxr.service.impl.BookServiceImpl;
import com.reduxr.specification.book.BookSpecificationBuilder;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private BookServiceImpl bookService;
    
    @Test
    @DisplayName("Save correct book and return its DTO")
    void save_ValidRequest_ReturnsBookDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        Book book = new Book();
        Category category1 = new Category();
        Category category2 = new Category();
        BookDto expectedDto = new BookDto();
        
        when(bookMapper.toModel(requestDto)).thenReturn(book);
        
        when(bookMapper.toDto(book)).thenReturn(expectedDto);
        
        when(categoryRepository.findAllById(requestDto.getCategoryIds()))
                .thenReturn(List.of(category1, category2));
        
        when(bookRepository.save(book)).thenReturn(book);
        
        BookDto actualDto = bookService.save(requestDto);
        
        assertEquals(expectedDto, actualDto);
        assertEquals(
                Set.of(category1, category2),
                book.getCategories()
        );
        
        verify(bookRepository).save(book);
    }
    
    @Test
    @DisplayName("Find all books and return books without categories DTO")
    void findAll_ValidPage_ReturnsMappedDtos() {
        Pageable pageable = PageRequest.of(0, 3);
        
        Book book1 = new Book();
        Book book2 = new Book();
        Book book3 = new Book();
        
        BookDtoWithoutCategoryIds dto1 = new BookDtoWithoutCategoryIds();
        BookDtoWithoutCategoryIds dto2 = new BookDtoWithoutCategoryIds();
        BookDtoWithoutCategoryIds dto3 = new BookDtoWithoutCategoryIds();
        
        Page<Book> pageFromDb = new PageImpl<>(
                List.of(book1, book2, book3), 
                pageable, 
                3);
        
        when(bookRepository.findAll(pageable)).thenReturn(pageFromDb);
        
        when(bookMapper.toDtoWithoutCategories(book1)).thenReturn(dto1);
        when(bookMapper.toDtoWithoutCategories(book2)).thenReturn(dto2);
        when(bookMapper.toDtoWithoutCategories(book3)).thenReturn(dto3);
        
        Page<BookDtoWithoutCategoryIds> actualPage = bookService.findAll(pageable);
        
        assertEquals(List.of(dto1, dto2, dto3), actualPage.getContent());
        assertEquals(0, actualPage.getNumber());
        assertEquals(3, actualPage.getSize());
        assertEquals(3, actualPage.getTotalElements());
        
        verify(bookRepository).findAll(pageable);
        verify(bookMapper).toDtoWithoutCategories(book1);
        verify(bookMapper).toDtoWithoutCategories(book2);
        verify(bookMapper).toDtoWithoutCategories(book3);
    }
    
    @Test
    @DisplayName("Find book by valid id and return book DTO")
    void findById_ValidId_ReturnsBookDto() {
        Book expectedBook = new Book();
        BookDto expectedDto = new BookDto();
        
        when(bookMapper.toDto(expectedBook)).thenReturn(expectedDto);
        when(bookRepository.findById(expectedBook.getId()))
                .thenReturn(Optional.of(expectedBook));
        
        BookDto actualDto = bookService.findById(expectedBook.getId());
        
        assertEquals(expectedDto, actualDto);
        verify(bookMapper).toDto(expectedBook);
    }
    
    @Test
    @DisplayName("Search by wrong id throws exception")
    void findById_WrongId_ThrowsException() {
        Long wrongId = -100L;
        
        when(bookRepository.findById(wrongId)).thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, 
                () -> bookService.findById(wrongId));
    }
    
    @Test
    @DisplayName("Find book by category id and return book without categories DTO")
    void findAllByCategoryId_ValidId_ReturnsBookWithoutCategoriesDtos() {
        Long categoryId = 1L;
        
        Book book1 = new Book();
        Book book2 = new Book();
        
        BookDtoWithoutCategoryIds dto1 = new BookDtoWithoutCategoryIds();
        BookDtoWithoutCategoryIds dto2 = new BookDtoWithoutCategoryIds();
        
        when(bookMapper.toDtoWithoutCategories(book1)).thenReturn(dto1);
        when(bookMapper.toDtoWithoutCategories(book2)).thenReturn(dto2);
        
        when(bookRepository.findAllByCategoryId(categoryId))
                .thenReturn(List.of(book1, book2));
        
        List<BookDtoWithoutCategoryIds> actualList = 
                bookService.findAllByCategoryId(categoryId);
        
        assertEquals(List.of(dto1, dto2), actualList);
        
        verify(bookMapper).toDtoWithoutCategories(book1);
        verify(bookMapper).toDtoWithoutCategories(book2);
        verify(bookRepository).findAllByCategoryId(categoryId);
    }
    
    @Test
    @DisplayName("Update book with valid parameters and return book DTO")
    void updateBook_ValidIdAndRequest_ReturnsBookDto() {
        Book existingBook = new Book();
        existingBook.setId(1L);
        
        Category category1 = new Category();
        category1.setId(1L);
        Category category2 = new Category();
        category2.setId(2L);
        
        UpdateBookRequestDto requestDto = new UpdateBookRequestDto();
        BookDto expected = new BookDto();
        
        when(categoryRepository.findAllById(requestDto.getCategoryIds()))
                .thenReturn(List.of(category1, category2));
        
        when(bookMapper.toDto(existingBook)).thenReturn(expected);
        when(bookRepository.save(existingBook)).thenReturn(existingBook);
        
        when(bookRepository.findById(existingBook.getId()))
                .thenReturn(Optional.of(existingBook));
        
        doAnswer(invocation -> {
            Book book = invocation.getArgument(1, Book.class);
            book.setTitle("Updated Title");
            return null;
        }).when(bookMapper).updateModelFromDto(requestDto, existingBook);
        
        BookDto actual = bookService.updateBook(existingBook.getId(), requestDto);
        
        assertEquals("Updated Title", existingBook.getTitle());
        assertEquals(expected, actual);
        assertEquals(Set.of(category1, category2), existingBook.getCategories());
        
        verify(bookMapper).toDto(existingBook);
        verify(bookMapper).updateModelFromDto(requestDto, existingBook);
        verify(bookRepository).save(existingBook);
        verify(categoryRepository).findAllById(requestDto.getCategoryIds());
    }
    
    @Test
    @DisplayName("Delete book with valid id")
    void deleteBook_ValidId_DeletesBook() {
        Book existingBook = new Book();
        
        when(bookRepository.findById(existingBook.getId()))
                .thenReturn(Optional.of(existingBook));
        
        bookService.deleteBook(existingBook.getId());
        
        verify(bookRepository).delete(existingBook);
    }
    
    @Test
    @DisplayName("Delete by wrong id should throw exception")
    void deleteBook_WrongId_ThrowsException() {
        Long wrongId = -100L;
        
        when(bookRepository.findById(wrongId))
                .thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, 
                () -> bookService.deleteBook(wrongId));
        
        verify(bookRepository, never()).delete((Book) any());
    }
    
    @Test
    @DisplayName("Find books by valid parameters and return page of book DTOs")
    void findByParams_ValidParams_ReturnsPageBookDtos() {
        BookSearchParametersDto parametersDto = 
                new BookSearchParametersDto(null, null, null);
        
        Specification<Book> bookSpecification =
                (root, cq, cb) -> null;
        
        Book book1 = new Book();
        Book book2 = new Book();
        BookDto bookDto1 = new BookDto();
        BookDto bookDto2 = new BookDto();
        
        Pageable pageable = PageRequest.of(0, 2);
        
        Page<Book> pageFromDb = 
                new PageImpl<>(List.of(book1, book2), pageable, 2);
        
        when(bookSpecificationBuilder.build(parametersDto)).thenReturn(bookSpecification);
        when(bookRepository.findAll(bookSpecification, pageable)).thenReturn(pageFromDb);
        when(bookMapper.toDto(book1)).thenReturn(bookDto1);
        when(bookMapper.toDto(book2)).thenReturn(bookDto2);
        
        Page<BookDto> actualPage = bookService.findByParams(parametersDto, pageable);
        
        assertEquals(List.of(bookDto1, bookDto2), actualPage.getContent());
        assertEquals(0, actualPage.getNumber());
        assertEquals(2, actualPage.getSize());
        assertEquals(2, actualPage.getTotalElements());
        
        verify(bookSpecificationBuilder).build(parametersDto);
        verify(bookRepository).findAll(bookSpecification, pageable);
        verify(bookMapper).toDto(book1);
        verify(bookMapper).toDto(book2);
    }
}
