package com.ecommerce.library.dto;

import org.hibernate.mapping.List;

import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.SubCategory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private int currentQuantity;
    private double costPrice;
    private double salePrice;
    private String image;
    
    private Category category;
    private boolean activated;
    private boolean deleted;
    private String currentPage;

    private SubCategory subCategory;

}
