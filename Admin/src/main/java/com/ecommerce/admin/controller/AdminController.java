package com.ecommerce.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @GetMapping("/categories")
    public String categories(Model model){
        model.addAttribute("title", "Category");
        return "categories";
    }
    @GetMapping("/cards")
    public String cards(Model model){
        model.addAttribute("title", "Cards");
        return "cards";
    }
}
