package com.reduxr.mapper;

import com.reduxr.config.MapperConfig;
import com.reduxr.dto.book.BookDto;
import com.reduxr.dto.book.BookDtoWithoutCategoryIds;
import com.reduxr.dto.book.CreateBookRequestDto;
import com.reduxr.dto.book.UpdateBookRequestDto;
import com.reduxr.model.Book;
import com.reduxr.model.Category;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);
    
    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);
    
    Book toModel(CreateBookRequestDto requestDto);
    
    void updateModelFromDto(UpdateBookRequestDto requestDto, @MappingTarget Book book);
    
    @Named("bookFromId") 
    default Book bookFromId(Long id) {
        if (id == null) {
            return null;
        }
        Book book = new Book();
        book.setId(id);
        return book;
    }
    
    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        List<Long> list = book.getCategories().stream()
                .map(Category::getId)
                .toList();
        bookDto.setCategoryIds(list);
    }
}
