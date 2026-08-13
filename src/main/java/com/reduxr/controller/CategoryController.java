package com.reduxr.controller;

import com.reduxr.dto.book.BookDtoWithoutCategoryIds;
import com.reduxr.dto.category.CategoryDto;
import com.reduxr.dto.category.CreateCategoryRequestDto;
import com.reduxr.dto.category.UpdateCategoryRequestDto;
import com.reduxr.service.BookService;
import com.reduxr.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category management", description = "Endpoints for managing categories")
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;
    
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get categories", description = "Get all categories")
    @GetMapping
    public Page<CategoryDto> getAllCategories(@ParameterObject Pageable pageable) {
        return categoryService.findAllCategories(pageable);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get category by id")
    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.findCategoryById(id);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get books by category id")
    @GetMapping ("/{id}/books")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(@PathVariable Long id) {
        return bookService.findAllByCategoryId(id);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create category", description = "Create a new category")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Valid CreateCategoryRequestDto requestDto) {
        return categoryService.saveCategory(requestDto);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update category", 
            description = "Update parameters of an existing category")
    @PutMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable Long id,
                                      @RequestBody @Valid UpdateCategoryRequestDto requestDto) {
        return categoryService.updateCategory(id, requestDto);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete category", description = "Marks category as " 
            + "deleted and makes it unreachable for API (Can be accessed directly from DB only)")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
