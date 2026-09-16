package com.reduxr.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.reduxr.dto.category.CreateCategoryRequestDto;
import com.reduxr.dto.category.UpdateCategoryRequestDto;
import com.reduxr.model.Category;
import com.reduxr.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
public class CategoryControllerTest extends AbstractControllerTest {
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Test
    @DisplayName("Get all categories and return OK status")
    @WithMockUser(username = "user", roles = "USER")
    void getAllCategories_ValidRequest_ReturnsOkStatus() throws Exception {
        mockMvc.perform(get("/categories")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.number").value(0));
    }
    
    @Test
    @DisplayName("Get category by id and return OK status")
    @WithMockUser(username = "user", roles = "USER")
    void getCategoryById_ValidId_ReturnsOkStatus() throws Exception {
        Long categoryId = 1L;
        
        mockMvc.perform(get("/categories/{id}", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoryId))
                .andExpect(jsonPath("$.name").value("Fantasy"));
    }
    
    @Test
    @DisplayName("Get books by category id and return OK status")
    @WithMockUser(username = "user", roles = "USER")
    void getBooksByCategoryId_ValidId_ReturnsOkStatus() throws Exception {
        Long categoryId = 1L;
        
        mockMvc.perform(get("/categories/{id}/books", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }
    
    @Test
    @DisplayName("Create category and return CREATED status")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void createCategory_ValidRequest_ReturnsCreatedStatus() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("Test Category");
        
        String response = mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Category"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        
        Category savedCategory =
                objectMapper.readValue(response, Category.class);
        
        assertTrue(savedCategory.getId() > 0);
        assertTrue(categoryRepository.existsById(savedCategory.getId()));
        
        Category categoryFromDatabase =
                categoryRepository.findById(savedCategory.getId()).orElseThrow();
        
        assertEquals("Test Category", categoryFromDatabase.getName());
    }
    
    @Test
    @DisplayName("Update category and return OK status")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void updateCategory_ValidRequest_ReturnsOkStatus() throws Exception {
        Long categoryIdToUpdate = 1L;
        
        UpdateCategoryRequestDto requestDto = new UpdateCategoryRequestDto();
        requestDto.setName("Updated Category");
        
        String response = mockMvc.perform(put("/categories/{id}", categoryIdToUpdate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoryIdToUpdate))
                .andExpect(jsonPath("$.name").value("Updated Category"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        
        Category updatedCategory =
                objectMapper.readValue(response, Category.class);
        
        assertEquals(categoryIdToUpdate, updatedCategory.getId());
        assertEquals("Updated Category", updatedCategory.getName());
        
        Category categoryFromDatabase =
                categoryRepository.findById(categoryIdToUpdate).orElseThrow();
        
        assertEquals("Updated Category", categoryFromDatabase.getName());
    }
    
    @Test
    @DisplayName("Delete category and return NO CONTENT status")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteCategory_ValidRequest_ReturnsNoContentStatus() throws Exception {
        Long categoryIdToDelete = 3L;
        
        mockMvc.perform(delete("/categories/{id}", categoryIdToDelete))
                .andExpect(status().isNoContent());
        
        assertEquals(2, categoryRepository.count());
        assertFalse(categoryRepository.existsById(categoryIdToDelete));
    }
}
