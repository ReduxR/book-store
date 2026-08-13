package com.reduxr.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateCategoryRequestDto {
    @NotBlank
    private String name;
    
    private String description;
}
