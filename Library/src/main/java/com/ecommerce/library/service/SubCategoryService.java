package com.ecommerce.library.service;

import java.util.List;

import com.ecommerce.library.model.SubCategory;

public interface SubCategoryService {

    SubCategory save(SubCategory subCategory);

    //void deleteById(Long id);

    List<SubCategory> findAllSubCategories()throws Exception;

    List<SubCategory> getAllSubCategoriesByCategoryId(Long id);


    
} 