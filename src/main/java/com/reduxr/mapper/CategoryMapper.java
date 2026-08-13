package com.reduxr.mapper;

import com.reduxr.config.MapperConfig;
import com.reduxr.dto.category.CategoryDto;
import com.reduxr.dto.category.CreateCategoryRequestDto;
import com.reduxr.dto.category.UpdateCategoryRequestDto;
import com.reduxr.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);
    
    Category toModel(CreateCategoryRequestDto requestDto);
    
    void updateModelFromDto(UpdateCategoryRequestDto requestDto, @MappingTarget Category category);
}
