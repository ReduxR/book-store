package com.reduxr.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.reduxr.dto.category.CategoryDto;
import com.reduxr.dto.category.CreateCategoryRequestDto;
import com.reduxr.dto.category.UpdateCategoryRequestDto;
import com.reduxr.exception.EntityNotFoundException;
import com.reduxr.mapper.CategoryMapper;
import com.reduxr.model.Category;
import com.reduxr.repository.CategoryRepository;
import com.reduxr.service.impl.CategoryServiceImpl;
import java.util.List;
import java.util.Optional;
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

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    
    @Mock
    private CategoryMapper categoryMapper;
    
    @InjectMocks
    private CategoryServiceImpl categoryService;
    
    @Test
    @DisplayName("Find all categories and return category DTO page")
    void findAllCategories_ValidPage_ReturnsCategoryDtoPage() {
        CategoryDto categoryDto1 = new CategoryDto(1L, "Test category 1", null);
        CategoryDto categoryDto2 = new CategoryDto(2L, "Test category 2", null);
        CategoryDto categoryDto3 = new CategoryDto(3L, "Test category 3", null);
        
        Category category1 = new Category();
        Category category2 = new Category();
        Category category3 = new Category();
        
        Pageable pageable = PageRequest.of(0, 3);
        Page<Category> page = new PageImpl<>(
                List.of(category1, category2, category3),
                pageable,
                3);
        
        when(categoryRepository.findAll(pageable)).thenReturn(page);
        when(categoryMapper.toDto(category1)).thenReturn(categoryDto1);
        when(categoryMapper.toDto(category2)).thenReturn(categoryDto2);
        when(categoryMapper.toDto(category3)).thenReturn(categoryDto3);
        
        Page<CategoryDto> actualPage = categoryService.findAllCategories(pageable);
        
        assertEquals(List.of(categoryDto1, categoryDto2, categoryDto3), actualPage.getContent());
        assertEquals(0, actualPage.getNumber());
        assertEquals(3, actualPage.getSize());
        assertEquals(3, actualPage.getTotalElements());
        
        verify(categoryRepository).findAll(pageable);
        verify(categoryMapper).toDto(category1);
        verify(categoryMapper).toDto(category2);
        verify(categoryMapper).toDto(category3);
    }
    
    @Test
    
    @DisplayName("Find category by id and return category DTO")
    void findCategoryById_ValidId_ReturnsCategoryDto() {
        Long id = 1L;
        Category category = new Category();
        CategoryDto expectedDto = new CategoryDto(1L, "Test category", null);
        
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expectedDto);
        
        CategoryDto actualDto = categoryService.findCategoryById(id);
        
        assertEquals(expectedDto, actualDto);
        
        verify(categoryRepository).findById(id);
        verify(categoryMapper).toDto(category);
    }
    
    @Test
    @DisplayName("Search by invalid category id throws exception")
    void findCategoryById_InvalidId_ThrowsException() {
        Long id = 999L;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());
        
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.findCategoryById(id)
        );
        
        assertEquals("Can't find category by id: " + id, exception.getMessage());
        verify(categoryRepository).findById(id);
    }
    
    @Test
    @DisplayName("Save valid category and return category DTO")
    void saveCategory_ValidRequest_ReturnsCategoryDto() {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        Category category = new Category();
        CategoryDto expectedDto = new CategoryDto(1L, "Test category", null);
        
        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expectedDto);
        
        CategoryDto actualDto = categoryService.saveCategory(requestDto);
        
        assertEquals(expectedDto, actualDto);
        
        verify(categoryMapper).toModel(requestDto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
        
    }
    
    @Test
    @DisplayName("Update category with valid params and return category DTO")
    void updateCategory_ValidIdAndRequest_ReturnsCategoryDto() {
        Long id = 1L;
        UpdateCategoryRequestDto requestDto = new UpdateCategoryRequestDto();
        Category category = new Category();
        CategoryDto expectedDto = new CategoryDto(1L, "Updated category", null);
        
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expectedDto);
        
        CategoryDto actualDto = categoryService.updateCategory(id, requestDto);
        
        assertEquals(expectedDto, actualDto);
        
        verify(categoryRepository).findById(id);
        verify(categoryMapper).updateModelFromDto(requestDto, category);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
    }
    
    @Test
    @DisplayName("Update category with invalid id throws exception")
    void updateCategory_InvalidId_ThrowsException() {
        Long id = 999L;
        UpdateCategoryRequestDto requestDto = new UpdateCategoryRequestDto();
        
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());
        
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.updateCategory(id, requestDto)
        );
        
        assertEquals("Can't find category by id: " + id, exception.getMessage());
        
        verify(categoryRepository).findById(id);
    }
    
    @Test
    @DisplayName("Delete category by valid id deletes category")
    void deleteCategory_ValidId_DeletesCategory() {
        Long id = 1L;
        
        when(categoryRepository.existsById(id)).thenReturn(true);
        
        categoryService.deleteCategory(id);
        
        verify(categoryRepository).existsById(id);
        verify(categoryRepository).deleteById(id);
    }
    
    @Test
    @DisplayName("Delete category by invalid id throws exception")
    void deleteCategory_InvalidId_ThrowsException() {
        Long id = 999L;
        
        when(categoryRepository.existsById(id)).thenReturn(false);
        
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.deleteCategory(id)
        );
        
        assertEquals("Can't find category by id: " + id, exception.getMessage());
        
        verify(categoryRepository).existsById(id);
    }
}
