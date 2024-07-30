package com.ecommerce.library.dto;

import java.util.List;

import com.ecommerce.library.model.SubCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;
    public Long productSize;

}
