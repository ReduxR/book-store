package com.reduxr.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class UpdateBookRequestDto {
    @NotBlank
    private String title;
    
    @NotEmpty
    private List<Long> categoryIds;
    
    @NotBlank
    private String author;
    
    @NotNull
    @Min(0)
    private BigDecimal price;
    
    private String description;
    
    private String coverImage;
}
