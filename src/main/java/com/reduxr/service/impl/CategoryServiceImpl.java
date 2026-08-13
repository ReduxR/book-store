package com.reduxr.service.impl;

import com.reduxr.dto.category.CategoryDto;
import com.reduxr.dto.category.CreateCategoryRequestDto;
import com.reduxr.dto.category.UpdateCategoryRequestDto;
import com.reduxr.exception.EntityNotFoundException;
import com.reduxr.mapper.CategoryMapper;
import com.reduxr.model.Category;
import com.reduxr.repository.CategoryRepository;
import com.reduxr.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    
    @Override
    public Page<CategoryDto> findAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::toDto);
    }
    
    @Override
    public CategoryDto findCategoryById(Long id) {
        return categoryMapper.toDto(getCategoryOrThrow(id));
    }
    
    @Override
    public CategoryDto saveCategory(CreateCategoryRequestDto requestDto) {
        Category category = categoryRepository.save(categoryMapper.toModel(requestDto));
        return categoryMapper.toDto(category);
    }
    
    @Override
    public CategoryDto updateCategory(Long id, UpdateCategoryRequestDto requestDto) {
        Category category = getCategoryOrThrow(id);
        categoryMapper.updateModelFromDto(requestDto, category);
        Category saved = categoryRepository.save(category);
        return categoryMapper.toDto(saved);
    }
    
    @Override
    public void deleteCategory(Long id) {
        Category category = getCategoryOrThrow(id);
        categoryRepository.delete(category);
    }
    
    private Category getCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find category " 
                        + "by id: " + id));
    }
}
