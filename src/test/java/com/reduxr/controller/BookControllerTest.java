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

import com.reduxr.dto.book.CreateBookRequestDto;
import com.reduxr.dto.book.UpdateBookRequestDto;
import com.reduxr.model.Book;
import com.reduxr.repository.BookRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

public class BookControllerTest extends AbstractControllerTest {
    @Autowired 
    private BookRepository bookRepository;
    
    @Test
    @DisplayName("Get all books and return OK status")
    @WithMockUser(username = "user", roles = "USER")
    void getAllBooks_ValidRequest_ReturnsOkStatus() throws Exception {
        mockMvc.perform(get("/books")
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
    @DisplayName("Get book by id and return OK status")
    @WithMockUser(username = "user", roles = "USER")
    void getBookById_ValidId_ReturnsOkStatus() throws Exception {
        Long bookId = 1L;
        
        mockMvc.perform(get("/books/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.title").value("The Hobbit"));
    }
    
    @Test
    @DisplayName("Search books by parameters and return OK status")
    @WithMockUser(username = "user", roles = "USER")
    void searchBooks_ValidParameters_ReturnsOkStatus() throws Exception {
        String titleSearchParam = "The Hobbit";
        
        mockMvc.perform(get("/books/search")
                        .param("page", "0")
                        .param("size", "20")
                        .param("title", titleSearchParam))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].title").value(titleSearchParam));
    }
    
    @Test
    @DisplayName("Create book and return CREATED status")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void createBook_ValidRequest_ReturnsCreatedStatus() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Test Title")
                .setAuthor("Test Author")
                .setCategoryIds(List.of(1L, 2L))
                .setIsbn("1234567890")
                .setPrice(BigDecimal.TEN);
        
        String response = mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        
        Book savedBook = objectMapper.readValue(response, Book.class);
        assertTrue(savedBook.getId() > 0);
        assertTrue(bookRepository.existsById(savedBook.getId()));
        
        Book bookFromDatabase = bookRepository.findById(savedBook.getId()).orElseThrow();
        assertEquals("Test Title", bookFromDatabase.getTitle());
        assertEquals("Test Author", bookFromDatabase.getAuthor());
        assertEquals("1234567890", bookFromDatabase.getIsbn());
        assertEquals(BigDecimal.TEN, bookFromDatabase.getPrice());
    }
    
    @Test
    @DisplayName("Update book and return OK status")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void updateBook_ValidRequest_ReturnsOkStatusAndUpdatesBook() throws Exception {
        Long bookIdToUpdate = 1L;
        
        UpdateBookRequestDto requestDto = new UpdateBookRequestDto();
        requestDto.setTitle("Updated Title");
        requestDto.setCategoryIds(List.of(1L, 2L, 3L));
        requestDto.setAuthor("Updated Author");
        requestDto.setPrice(BigDecimal.ONE);
        
        String response = mockMvc.perform(put("/books/{id}", bookIdToUpdate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        
        Book updatedBook = objectMapper.readValue(response, Book.class);
        assertEquals(bookIdToUpdate, updatedBook.getId());
        assertEquals("Updated Title", updatedBook.getTitle());
        
        Book bookFromDatabase = bookRepository.findById(bookIdToUpdate).orElseThrow();
        assertEquals("Updated Title", bookFromDatabase.getTitle());
        assertEquals("Updated Author", bookFromDatabase.getAuthor());
        assertEquals(BigDecimal.ONE, bookFromDatabase.getPrice());
    }
    
    @Test
    @DisplayName("Delete book and return NO CONTENT status")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteBook_ValidRequest_ReturnsNoContentStatus() throws Exception {
        Long bookIdToDelete = 1L;
        
        mockMvc.perform(delete("/books/{id}", bookIdToDelete))
                .andExpect(status().isNoContent());
        
        assertEquals(2, bookRepository.count());
        assertFalse(bookRepository.existsById(bookIdToDelete));
    }
}
