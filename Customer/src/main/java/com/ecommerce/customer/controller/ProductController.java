package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Product;
//import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Review;
import com.ecommerce.library.model.SubCategory;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.ReviewService;
import com.ecommerce.library.service.SubCategoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;
    private final ReviewService reviewService;

    @GetMapping("/menu")
    public String menu(Model model) {
        model.addAttribute("page", "Products");
        model.addAttribute("title", "SHOP BY SPORT");
        List<CategoryDto> categoryDtos = categoryService.getCategoriesAndSize();
        List<ProductDto> products = productService.products();

        List<Review> reviews = new ArrayList<>();
        for(ProductDto product: products){
            reviews = reviewService.getReviewsByProductId(product.getId());
        }

        model.addAttribute("reviews", reviews);
        model.addAttribute("products", products);
        model.addAttribute("productSize", categoryDtos.size());
        model.addAttribute("categories", categoryDtos);
        return "index";
    }

    @GetMapping("/shop-detail")
    public String shopDetail(Model model) {
        List<CategoryDto> categories = categoryService.getCategoriesAndSize();

        List<SubCategory> allSubCategories = new ArrayList<>();

        for (CategoryDto categoryDto : categories) {
            Long id = categoryDto.getId();

            List<SubCategory> subCategories = subCategoryService.getAllSubCategoriesByCategoryId(id);
            allSubCategories.addAll(subCategories);
        }
        model.addAttribute("categories", categories);
        List<ProductDto> products = productService.randomProduct();
        List<ProductDto> listView = productService.listViewProducts();

        model.addAttribute("productViews", listView);
        model.addAttribute("title", "Shop Detail");
        model.addAttribute("page", "Shop Detail");
        model.addAttribute("products", products);
        model.addAttribute("subcategories", allSubCategories);
        return "shop-detail";
    }

    @GetMapping("/high-price")
    public String filterHighPrice(Model model) {
        List<CategoryDto> categories = categoryService.getCategoriesAndSize();
        model.addAttribute("categories", categories);
        List<ProductDto> products = productService.filterHighProducts();

        List<SubCategory> allSubCategories = new ArrayList<>();

        for (CategoryDto categoryDto : categories) {
            Long id = categoryDto.getId();

            List<SubCategory> subCategories = subCategoryService.getAllSubCategoriesByCategoryId(id);
            allSubCategories.addAll(subCategories);
        }

        model.addAttribute("title", "Shop Detail");
        model.addAttribute("page", "Shop Detail");
        model.addAttribute("products", products);
        model.addAttribute("subcategories", allSubCategories);
        return "shop-detail";
    }

    @GetMapping("/lower-price")
    public String filterLowerPrice(Model model) {
        List<CategoryDto> categories = categoryService.getCategoriesAndSize();
        model.addAttribute("categories", categories);
        List<ProductDto> products = productService.filterLowerProducts();
        List<ProductDto> listView = productService.listViewProducts();
        model.addAttribute("productViews", listView);
        model.addAttribute("title", "Shop Detail");
        model.addAttribute("page", "Shop Detail");
        model.addAttribute("products", products);
        return "shop-detail";
    }

    // @GetMapping("/find-products/{id}")
    // public String findByCategoryId(@PathVariable("id") Long id, Model model) {
    // System.out.println("Entered into find-products");
    // List<CategoryDto> categoryDtos = categoryService.getCategoriesAndSize();
    // List<ProductDto> productDtos = productService.findByCategoryId(id);
    // List<ProductDto> listView = productService.listViewProducts();

    // model.addAttribute("productViews", listView);
    // model.addAttribute("categories", categoryDtos);
    // model.addAttribute("title", productDtos.get(0).getCategory().getName());
    // model.addAttribute("page", "Product-Category");
    // model.addAttribute("products", productDtos);
    // System.out.println("Find by category id: " + productDtos + id);

    // return "shop-detail";
    // }

    @GetMapping("/find-products/{id}")
    public String findByCategoryId(@PathVariable("id") Long id, Model model) {

        List<CategoryDto> categoryDtos = categoryService.getCategoriesAndSize();
        List<ProductDto> productDtos = productService.findByCategoryId(id);
        List<SubCategory> subCategoryList = subCategoryService.getAllSubCategoriesByCategoryId(id);

        model.addAttribute("categories", categoryDtos);
        model.addAttribute("subcategories", subCategoryList);
        model.addAttribute("title", productDtos.get(0).getCategory().getName());
        model.addAttribute("page", "Product-Category");
        model.addAttribute("products", productDtos);

        return "shop-detail";
    }

    // find products by sub category id
    @GetMapping("/find-products-subcategory/{id}")
    public String getBySubcategoryId(@PathVariable("id") Long id,
            Model model) {

        List<CategoryDto> categoryDtos = categoryService.getCategoriesAndSize();
        List<SubCategory> subCategoryList = subCategoryService.getAllSubCategoriesByCategoryId(id);
        // List<ProductDto> productDtos = productService.findByCategoryId(id);

        List<Product> productList = new ArrayList<>();

        List<Product> products = productService.getProductsBySubCategoryId(id);

        for (Product product : products) {
            productList.add(product);
        }

        model.addAttribute("categories", categoryDtos);
        model.addAttribute("products", productList);
        model.addAttribute("subcategories", subCategoryList);
        model.addAttribute("page", "shop-detail");
        // model.addAttribute("products", productDtos);

        return "shop-detail";
    }

    @GetMapping("/search-product")
    public String searchProduct(@RequestParam("keyword") String keyword, Model model) {
        List<CategoryDto> categoryDtos = categoryService.getCategoriesAndSize();
        List<ProductDto> productDtos = productService.searchProducts(keyword);
        List<ProductDto> listView = productService.listViewProducts();
        model.addAttribute("productViews", listView);
        model.addAttribute("categories", categoryDtos);
        model.addAttribute("title", "Search Products");
        model.addAttribute("page", "Result Search");
        model.addAttribute("products", productDtos);
        return "products";
    }

    // here integration review

    @GetMapping("/product-detail/{id}")
    public String details(@PathVariable("id") Long id, Model model) {
        ProductDto product = productService.getById(id);
        List<ProductDto> productDtoList = productService.findAllByCategory(product.getCategory().getName());

        // Reviews of product
        List<Review> reviewList = reviewService.getReviewsByProductId(product.getId());

        int size = reviewList.size();
        int[] rating = new int[size];

        // to store count of total rating count for each rating
        int rating1 = 0, rating2 = 0, rating3 = 0, rating4 = 0, rating5 = 0;

        // to store each review rating into array and count total rating count for each
        // rating
        for (int i = 0; i < size; i++) {
            Review review = reviewList.get(i); // Retrieve the review at index i
            int ratingNumber = review.getRatingNumber();
            rating[i] = ratingNumber;

            // Count total rating count for each rating
            switch (ratingNumber) {
                case 1:
                    rating1++;
                    break;
                case 2:
                    rating2++;
                    break;
                case 3:
                    rating3++;
                    break;
                case 4:
                    rating4++;
                    break;
                case 5:
                    rating5++;
                    break;
                default:
                    break;
            }
        }

        model.addAttribute("products", productDtoList);
        model.addAttribute("title", "Product Detail");
        model.addAttribute("page", "Product Detail");
        model.addAttribute("productDetail", product);
        model.addAttribute("reviewList", reviewList);
        model.addAttribute("size", reviewList.size());

        model.addAttribute("rating1", (double) rating1 / reviewList.size() * 100);
        model.addAttribute("rating2", (double) rating2 / reviewList.size() * 100);
        model.addAttribute("rating3", (double) rating3 / reviewList.size() * 100);
        model.addAttribute("rating4", (double) rating4 / reviewList.size() * 100);
        model.addAttribute("rating5", (double) rating5 / reviewList.size() * 100);

        return "product-detail";
    }
}
