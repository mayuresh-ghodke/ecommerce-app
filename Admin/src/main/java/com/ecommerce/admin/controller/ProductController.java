package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.SubCategory;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.SubCategoryService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final CategoryService categoryService;

    private final SubCategoryService subCategoryService;

    @GetMapping("/products")
    public String products(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        List<SubCategory> subCategoryList = new ArrayList<>();
        List<ProductDto> products = productService.allProduct();
        List<CategoryDto> categories = categoryService.getCategoriesAndSize();
        for (CategoryDto categoryDto : categories) {
            subCategoryList = subCategoryService.getAllSubCategoriesByCategoryId(categoryDto.getId());

        }

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("size", products.size());
        model.addAttribute("subcategories", subCategoryList);
        model.addAttribute("title","Manage Products");

        return "products";
    }

    @GetMapping("/products/{pageNo}")
    public String allProducts(@PathVariable("pageNo") int pageNo, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        Page<ProductDto> products = productService.getAllProducts(pageNo);
        model.addAttribute("title", "Manage Products");
        model.addAttribute("size", products.getSize());
        model.addAttribute("products", products);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", products.getTotalPages());
        return "products";
    }

    @GetMapping("/search-products/{pageNo}")
    public String searchProduct(@PathVariable("pageNo") int pageNo,
            @RequestParam(value = "keyword") String keyword,
            Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        Page<ProductDto> products = productService.searchProducts(pageNo, keyword);
        model.addAttribute("title", "Result Search Products");
        model.addAttribute("size", products.getSize());
        model.addAttribute("products", products);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", products.getTotalPages());
        return "product-result";

    }

    @GetMapping("/add-product")
    public String addProductPage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        model.addAttribute("title", "Add Product");
        List<Category> categories = categoryService.findAllByActivatedTrue();
        model.addAttribute("categories", categories);
        model.addAttribute("productDto", new ProductDto());
        return "add-product";
    }

    @PostMapping("/save-product")
    public String saveProduct(@ModelAttribute("productDto") ProductDto product,
            @RequestParam("imageProduct") MultipartFile imageProduct,
            RedirectAttributes redirectAttributes, Principal principal) {
        try {
            if (principal == null) {
                return "redirect:/login";
            }
            productService.save(imageProduct, product);
            redirectAttributes.addFlashAttribute("success", "Product Added Successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to add the product.!");
        }
        return "redirect:/add-product";
    }

    @GetMapping("/update-product/{id}")
    public String updateProductForm(@PathVariable("id") Long id, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        List<Category> categories = categoryService.findAllByActivatedTrue();
        ProductDto productDto = productService.getById(id);
        SubCategory subCategory = productDto.getSubCategory();

        model.addAttribute("title", "Update Product");
        model.addAttribute("categories", categories);
        model.addAttribute("productDto", productDto);
        model.addAttribute("subCategory", subCategory);
        return "update-product";
    }

    @PostMapping("/update-product/{id}")
    public String updateProduct(@ModelAttribute("productDto") ProductDto productDto,
            @RequestParam("imageProduct") MultipartFile imageProduct,
            RedirectAttributes redirectAttributes, Principal principal) {
        try {
            if (principal == null) {
                return "redirect:/login";
            }
            productService.update(imageProduct, productDto);
            redirectAttributes.addFlashAttribute("success", "Update successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server, please try again!");
        }
        return "redirect:/products";
    }

    @RequestMapping(value = "/enable-product", method = { RequestMethod.PUT, RequestMethod.GET })
    public String enabledProduct(Long id, RedirectAttributes redirectAttributes, Principal principal) {
        try {
            if (principal == null) {
                return "redirect:/login";
            }
            productService.enableById(id);
            redirectAttributes.addFlashAttribute("success", "Enabled successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Enabled failed!");
        }
        return "redirect:/products/0";
    }

    @RequestMapping(value = "/delete-product", method = { RequestMethod.PUT, RequestMethod.GET })
    public String deletedProduct(Long id, RedirectAttributes redirectAttributes, Principal principal) {
        try {
            if (principal == null) {
                return "redirect:/login";
            }
            productService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Deleted failed!");
        }
        return "redirect:/products/0";
    }

    @GetMapping("/get-by-category")
    public String getProductByCategory(@RequestParam("product_categories") Long id,
            Model model, Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        Category category = categoryService.findCategoryById(id);
        

        List<ProductDto> productDtos = productService.findByCategoryId(id);
        List<Category> categories = categoryService.findAllByActivatedTrue();
        List<SubCategory> subcategories = subCategoryService.getAllSubCategoriesByCategoryId(id);

        model.addAttribute("products", productDtos);
        model.addAttribute("categories", categories);
        model.addAttribute("size", productDtos.size());
        model.addAttribute("subcategories", subcategories);
        model.addAttribute("category", category);
        model.addAttribute("title","Manage Products");

        return "products";

    }

    @GetMapping("/get-by-subcategory")
    public String getProductBySubCategory(@RequestParam("product_subcategories") Long id,
            Model model, Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        List<Product> products = productService.getProductsBySubCategoryId(id);
        List<Category> categories = categoryService.findAllByActivatedTrue();
        List<SubCategory> subcategories = subCategoryService.getAllSubCategoriesByCategoryId(id);

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("size", products.size());
        model.addAttribute("subcategories", subcategories);
        model.addAttribute("title","Manage Products");

        return "products";

    }

}
