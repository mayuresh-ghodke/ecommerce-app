package com.ecommerce.admin.controller;

import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.SubCategory;
import com.ecommerce.library.dto.SubCategoryDto;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.SubCategoryService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SubCategoryController {

    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;

    @GetMapping("/sub-category")
    public String subCategories(Model model, Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        List<Category> categories = categoryService.findALl();
        
        model.addAttribute("categories", categories);
        model.addAttribute("size", categories.size());
        model.addAttribute("categoryNew", new Category());
        model.addAttribute("title","Add Subcategory");
        return "sub-category";
    }

    @GetMapping("/subcategories")
    public String viewSubCategories(Model model, Principal principal)throws Exception {
        if (principal == null) {
            return "redirect:/login";
        }
        List<Category> categories = categoryService.findAllByActivatedTrue();
        model.addAttribute("title", "Manage Sub Category");
        List<SubCategory> list = subCategoryService.findAllSubCategories();
        model.addAttribute("subcategorylist", list);
        model.addAttribute("categories", categories);
        model.addAttribute("size", list.size());

        return "subcategories";
    }

    @PostMapping("/save-sub-category")
    public String saveSubCategory(@ModelAttribute("subCategory") SubCategory subCategory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        
        Long categoryId = subCategory.getCategory().getId();
        Category category = categoryService.findCategoryById(categoryId); // Fetch the category object from the database
        subCategory.setCategory(category);

        // // Save the subcategory
        subCategoryService.save(subCategory);

        return "sub-category"; 
    }


    @GetMapping("/subcategories/{categoryId}")
    public ResponseEntity<List<SubCategoryDto>> getSubcategories(@PathVariable("categoryId") Long categoryId) {
        System.out.println("Category for subcategory: " + categoryId);

        List<SubCategoryDto> dtoList = new ArrayList<>();

        // Populate subcategoryList
        List<SubCategory> subcategoryList = subCategoryService.getAllSubCategoriesByCategoryId(categoryId);
        
        for(SubCategory sb: subcategoryList){
            dtoList.add(new SubCategoryDto(sb.getId(), sb.getName()));
        }

        // Return the subcategoryList with appropriate HTTP status
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    // To display on subcategories page
    @GetMapping("/get-subcategories-by-category")
    public String getSubCategoriesByCategory(@RequestParam("category") Long id, Principal principal, Model model){

        if(principal == null){
            return "redirect:/login";
        }

        Category category = categoryService.findCategoryById(id);
        List<SubCategory> subCategories = subCategoryService.getAllSubCategoriesByCategoryId(id);
        List<Category> categories = categoryService.findAllByActivatedTrue();

        model.addAttribute("subcategorylist", subCategories);
        model.addAttribute("categories", categories);
        model.addAttribute("size", subCategories.size());
        model.addAttribute("title",category.getName()+" Subcategories");
        return "subcategories";
    }
    
}
