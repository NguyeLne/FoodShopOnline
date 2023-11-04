package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/products")
    public String products(Model model){
        return "shop";
    }
    @GetMapping("/menu")
    public String index(Model model){
        model.addAttribute("page", "Sản phẩm");
        model.addAttribute("title", "Menu");
        List<Category> categories = categoryService.findALl();
        List<ProductDto> products = productService.allProduct();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "index";
    }
    @GetMapping("/shop-detail")
    public String shopDetail(Model model) {
        List<CategoryDto> categories = categoryService.getCategoriesAndSize();
        model.addAttribute("categories", categories);
        List<ProductDto> products = productService.randomProduct();
        List<ProductDto> listView = productService.listViewProducts();
        model.addAttribute("productViews", listView);
        model.addAttribute("title", "Cửa hàng");
        model.addAttribute("page", "Cửa hàng");
        model.addAttribute("products", products);
        return "shop-detail";
    }

    @GetMapping("/product-detail/{id}")
    public String details(@PathVariable("id") Long id, Model model) {
        ProductDto product = productService.getById(id);
        List<ProductDto> productDtoList = productService.findAllByCategory(product.getCategory().getName());
        model.addAttribute("products", productDtoList);
        model.addAttribute("title", "Thông tin sản phẩm");
        model.addAttribute("page", "Thông tin sản phẩm");
        model.addAttribute("productDetail", product);
        return "product-detail";
    }
    @GetMapping("/find-products/{id}")
    public String productsInCategory(@PathVariable("id") Long id, Model model) {
        List<CategoryDto> categoryDtos = categoryService.getCategoriesAndSize();
        List<ProductDto> productDtos = productService.findByCategoryId(id);
        List<ProductDto> listView = productService.listViewProducts();
        model.addAttribute("productViews", listView);
        model.addAttribute("categories", categoryDtos);
        model.addAttribute("title", productDtos.get(0).getCategory().getName());
        model.addAttribute("page", "Mặt hàng sản phẩm");
        model.addAttribute("products", productDtos);
        return "products";
    }
    @GetMapping("/search-product")
    public String searchProduct(@RequestParam("keyword") String keyword, Model model) {
        List<CategoryDto> categoryDtos = categoryService.getCategoriesAndSize();
        List<ProductDto> productDtos = productService.searchProducts(keyword);
        List<ProductDto> listView = productService.listViewProducts();
        model.addAttribute("productViews", listView);
        model.addAttribute("categories", categoryDtos);
        model.addAttribute("title", "Tìm kiếm sản phẩm");
        model.addAttribute("page", "Kết quả tìm kiếm");
        model.addAttribute("products", productDtos);
        return "products";
    }
    @GetMapping("/high-price")
    public String filterHighPrice(Model model) {
        List<CategoryDto> categories = categoryService.getCategoriesAndSize();
        model.addAttribute("categories", categories);
        List<ProductDto> products = productService.filterHighProducts();
        List<ProductDto> listView = productService.listViewProducts();
        model.addAttribute("title", "Cửa hàng");
        model.addAttribute("page", "Cửa hàng");
        model.addAttribute("productViews", listView);
        model.addAttribute("products", products);
        return "shop-detail";
    }


    @GetMapping("/lower-price")
    public String filterLowerPrice(Model model) {
        List<CategoryDto> categories = categoryService.getCategoriesAndSize();
        model.addAttribute("categories", categories);
        List<ProductDto> products = productService.filterLowerProducts();
        List<ProductDto> listView = productService.listViewProducts();
        model.addAttribute("productViews", listView);
        model.addAttribute("title", "Cửa hàng");
        model.addAttribute("page", "Cửa hàng");
        model.addAttribute("products", products);
        return "shop-detail";
    }
}
