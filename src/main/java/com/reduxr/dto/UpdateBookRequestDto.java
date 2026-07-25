package com.reduxr.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class UpdateBookRequestDto {
    @NotBlank
    private String title;
    
    @NotBlank
    private String author;
    
    @NotNull
    @Min(0)
    private BigDecimal price;
    
    private String description;
    
    private String coverImage;
}
