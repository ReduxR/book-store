package com.reduxr.mapper;

import com.reduxr.config.MapperConfig;
import com.reduxr.dto.BookDto;
import com.reduxr.dto.CreateBookRequestDto;
import com.reduxr.dto.UpdateBookRequestDto;
import com.reduxr.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);
    
    Book toModel(CreateBookRequestDto requestDto);
    
    void updateModelFromDto(UpdateBookRequestDto requestDto, @MappingTarget Book book);
}
