package com.reduxr.dto.book;

import com.reduxr.validator.isbn.Isbn;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotBlank
    private String title;
    
    @NotEmpty
    private List<Long> categoryIds;
    
    @NotBlank
    private String author;
    
    @NotBlank
    @Isbn
    private String isbn;
    
    @NotNull
    @Min(0)
    private BigDecimal price;
    
    private String description;
    
    private String coverImage;
}
