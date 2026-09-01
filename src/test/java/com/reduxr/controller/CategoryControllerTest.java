package com.reduxr.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.reduxr.config.SecurityTestConfig;
import com.reduxr.dto.book.BookDtoWithoutCategoryIds;
import com.reduxr.dto.category.CategoryDto;
import com.reduxr.dto.category.CreateCategoryRequestDto;
import com.reduxr.dto.category.UpdateCategoryRequestDto;
import com.reduxr.security.jwt.JwtAuthenticationFilter;
import com.reduxr.service.BookService;
import com.reduxr.service.CategoryService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(
        controllers = CategoryController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class
        )
)
@Import(SecurityTestConfig.class)
public class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private CategoryService categoryService;
    
    @MockitoBean
    private BookService bookService;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Test
    @DisplayName("Get all categories and return OK status")
    @WithMockUser(username = "user", roles = "USER")
    void getAllCategories_ValidRequest_ReturnsOkStatus() throws Exception {
        CategoryDto categoryDto1 = new CategoryDto(1L, "Category 1", null);
        CategoryDto categoryDto2 = new CategoryDto(2L, "Category 2", null);
        CategoryDto categoryDto3 = new CategoryDto(3L, "Category 3", null);
        
        Pageable pageable = PageRequest.of(0, 20);
        Page<CategoryDto> page = new PageImpl<>(
                List.of(categoryDto1,
                        categoryDto2,
                        categoryDto3), pageable, 3);
        
        when(categoryService.findAllCategories(pageable)).thenReturn(page);
        
        mockMvc.perform(get("/categories")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.number").value(0));
        
        verify(categoryService).findAllCategories(pageable);
    }
    
    @Test
    @DisplayName("Get category by id and return OK status")
    @WithMockUser(username = "user", roles = "USER")
    void getCategoryById_ValidId_ReturnsOkStatus() throws Exception {
        Long id = 1L;
        CategoryDto categoryDto = new CategoryDto(id, "Category 1", null);
        
        when(categoryService.findCategoryById(id))
                .thenReturn(categoryDto);
        
        mockMvc.perform(get("/categories/{id}", id))
                .andExpect(status().isOk());
        
        verify(categoryService).findCategoryById(id);
    }
    
    @Test
    @DisplayName("get books by category id and return OK status")
    @WithMockUser(username = "user", roles = "USER")
    void getBooksByCategoryId_ValidId_ReturnsOkStatus() throws Exception {
        Long id = 1L;
        BookDtoWithoutCategoryIds bookDto1 = new BookDtoWithoutCategoryIds();
        BookDtoWithoutCategoryIds bookDto2 = new BookDtoWithoutCategoryIds();
        BookDtoWithoutCategoryIds bookDto3 = new BookDtoWithoutCategoryIds();
        
        List<BookDtoWithoutCategoryIds> bookDtoList =
                List.of(bookDto1, bookDto2, bookDto3);
        
        when(bookService.findAllByCategoryId(id))
                .thenReturn(bookDtoList);
        
        mockMvc.perform(get("/categories/{id}/books", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
        
        verify(bookService).findAllByCategoryId(id);
    }
    
    @Test
    @DisplayName("Create category and return CREATED status")
    @WithMockUser(username = "admin", roles = "ADMIN") 
    void createCategory_ValidRequest_ReturnsCreatedStatus() throws Exception {
        Long id = 1L;
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("Test Name");
        
        CategoryDto categoryDto = new CategoryDto(id, "Test Name", null);
        
        when(categoryService.saveCategory(requestDto)).thenReturn(categoryDto);
        
        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Name"));
        
        verify(categoryService).saveCategory(requestDto);
    }
    
    @Test
    @DisplayName("Update category and return OK status")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void updateCategory_ValidRequest_ReturnsOkStatus() throws Exception {
        Long id = 1L;
        UpdateCategoryRequestDto requestDto = new UpdateCategoryRequestDto();
        requestDto.setName("Test Name");
        
        CategoryDto categoryDto = new CategoryDto(id, "Test Name", null);
        
        when(categoryService.updateCategory(id, requestDto)).thenReturn(categoryDto);
        
        mockMvc.perform(put("/categories/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Name"));
        
        verify(categoryService).updateCategory(id, requestDto);
    }
    
    @Test
    @DisplayName("Delete category and return NO CONTENT status")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteCategory_ValidRequest_ReturnsNoContentStatus() throws Exception {
        Long id = 1L;
        
        mockMvc.perform(delete("/categories/{id}", id))
                .andExpect(status().isNoContent());
        
        verify(categoryService).deleteCategory(id);
    }
}
