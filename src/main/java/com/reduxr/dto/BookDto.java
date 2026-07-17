package com.reduxr.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.math.BigDecimal;
import lombok.Data;

@Data
@JsonPropertyOrder("id")
public class BookDto {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;
}
