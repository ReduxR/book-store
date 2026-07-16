package com.reduxr.mapper;

import com.reduxr.config.MapperConfig;
import com.reduxr.dto.BookDto;
import com.reduxr.dto.CreateBookRequestDto;
import com.reduxr.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);
    
    Book toModel(CreateBookRequestDto requestDto);
}
