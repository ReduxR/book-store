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
import com.reduxr.dto.book.BookDto;
import com.reduxr.dto.book.BookDtoWithoutCategoryIds;
import com.reduxr.dto.book.BookSearchParametersDto;
import com.reduxr.dto.book.CreateBookRequestDto;
import com.reduxr.dto.book.UpdateBookRequestDto;
import com.reduxr.security.jwt.JwtAuthenticationFilter;
import com.reduxr.service.BookService;
import java.math.BigDecimal;
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
        controllers = BookController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class
        )
)
@Import(SecurityTestConfig.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private BookService bookService;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Test
    @DisplayName("Get all books and return OK status")
    @WithMockUser(username = "user", roles = "USER")
    void getAllBooks_ValidRequest_ReturnsOkStatus() throws Exception {
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds1 = new BookDtoWithoutCategoryIds();
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds2 = new BookDtoWithoutCategoryIds();
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds3 = new BookDtoWithoutCategoryIds();
        
        Pageable pageable = PageRequest.of(0, 20);
        Page<BookDtoWithoutCategoryIds> page = new PageImpl<>(
                List.of(bookDtoWithoutCategoryIds1,
                        bookDtoWithoutCategoryIds2,
                        bookDtoWithoutCategoryIds3), pageable, 3);
        
        when(bookService.findAll(pageable)).thenReturn(page);
        
        mockMvc.perform(get("/books")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.number").value(0));
        
        verify(bookService).findAll(pageable);
    }
    
    @Test
    @DisplayName("Get book by id and return OK status")
    @WithMockUser(username = "user", roles = "USER")
    void getBookById_ValidId_ReturnsOkStatus() throws Exception {
        Long id = 1L;
        BookDto bookDto = new BookDto();
        
        when(bookService.findById(id)).thenReturn(bookDto);
        
        mockMvc.perform(get("/books/{id}", id))
                .andExpect(status().isOk());
        
        verify(bookService).findById(id);
    }
    
    @Test
    @DisplayName("Search books by parameters and return OK status")
    @WithMockUser(username = "user", roles = "USER")
    void searchBooks_ValidParameters_ReturnsOkStatus() throws Exception {
        BookSearchParametersDto parametersDto =
                new BookSearchParametersDto(null, null, null);
        BookDto bookDto = new BookDto();
        
        Pageable pageable = PageRequest.of(0, 20);
        Page<BookDto> page =
                new PageImpl<>(List.of(bookDto), pageable, 3);
        
        when(bookService.findByParams(parametersDto, pageable))
                .thenReturn(page);
        
        mockMvc.perform(get("/books/search")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk());
        
        verify(bookService).findByParams(parametersDto, pageable);
    }
    
    @Test
    @DisplayName("Create book and return CREATED status")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void createBook_ValidRequest_ReturnsCreatedStatus() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Test Title");
        requestDto.setAuthor("Test Author");
        requestDto.setCategoryIds(List.of(1L, 2L));
        requestDto.setIsbn("1234567890");
        requestDto.setPrice(BigDecimal.TEN);
        
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Test Title");
        
        when(bookService.save(requestDto)).thenReturn(bookDto);
        
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Title"));
        
        verify(bookService).save(requestDto);
    }
    
    @Test
    @DisplayName("Update book and return OK status")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void updateBook_ValidRequest_ReturnsOkStatusAndUpdatesBook() throws Exception {
        UpdateBookRequestDto requestDto = new UpdateBookRequestDto();
        requestDto.setTitle("Test Title");
        requestDto.setCategoryIds(List.of(1L, 2L));
        requestDto.setAuthor("Test Author");
        requestDto.setPrice(BigDecimal.TEN);
        
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Test Title");
        
        when(bookService.updateBook(1L, requestDto)).thenReturn(bookDto);
        
        mockMvc.perform(put("/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"));
        
        verify(bookService).updateBook(1L, requestDto);
    }
    
    @Test
    @DisplayName("Delete book and return NO CONTENT status")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteBook_ValidRequest_ReturnsNoContentStatus() throws Exception {
        mockMvc.perform(delete("/books/{id}", 1L))
                .andExpect(status().isNoContent());
        
        verify(bookService).deleteBook(1L);
    }
}
