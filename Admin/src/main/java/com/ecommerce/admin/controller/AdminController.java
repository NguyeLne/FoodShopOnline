package com.ecommerce.admin.controller;

import com.ecommerce.library.model.Admin;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.service.AdminService;
import com.ecommerce.library.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final CustomerService customerService;
    @GetMapping("/customer-account")
    public String CustomerAccount(Model model){
        List<Customer> customers = customerService.AllAccount();
        model.addAttribute("customers", customers);
        model.addAttribute("size", customers.size());
        return "customer-account";
    }
    @GetMapping("/admin-account")
    public String AdminAccount(Model model){
        List<Admin> admins = adminService.AllAccount();
        model.addAttribute("admins", admins);
        model.addAttribute("size", admins.size());
        return "admin-account";
    }
}
