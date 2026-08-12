package com.reduxr.service;

import com.reduxr.dto.category.CategoryDto;
import com.reduxr.dto.category.CreateCategoryRequestDto;
import com.reduxr.dto.category.UpdateCategoryRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Page<CategoryDto> findAllCategories(Pageable pageable);
    
    CategoryDto findCategoryById(Long id);
    
    CategoryDto saveCategory(CreateCategoryRequestDto requestDto);
    
    CategoryDto updateCategory(Long id, UpdateCategoryRequestDto requestDto);
    
    void deleteCategory(Long id);
}
