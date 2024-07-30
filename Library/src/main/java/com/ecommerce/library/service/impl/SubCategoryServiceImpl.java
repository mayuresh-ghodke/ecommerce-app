package com.ecommerce.library.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.library.model.SubCategory;
import com.ecommerce.library.repository.SubCategoryRepository;
import com.ecommerce.library.service.SubCategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;

    @Override
    public SubCategory save(SubCategory subCategory) {
       
        return subCategoryRepository.save(subCategory);

    }

    @Override
    public List<SubCategory> findAllSubCategories()throws Exception {
        List<SubCategory> list = subCategoryRepository.findAll();
        return list;
    }

    @Override
    public List<SubCategory> getAllSubCategoriesByCategoryId(Long id) {
        
        List<SubCategory> subCategories = subCategoryRepository.findAllByCategoryId(id);
        
        return subCategories;
    }

    
}
