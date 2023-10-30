package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    @RequestMapping(value = {"/index","/"}, method = RequestMethod.GET)
    public String home(Model model){
        return "home";
    }
    @GetMapping("/home")
    public String index(Model model){
        model.addAttribute("page", "Products");
        model.addAttribute("title", "Menu");
        List<Category> categories = categoryService.findALl();
        List<ProductDto> products = productService.allProduct();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "index";
    }
}