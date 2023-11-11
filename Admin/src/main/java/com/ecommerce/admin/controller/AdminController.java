package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.*;
import com.ecommerce.library.service.AdminService;
import com.ecommerce.library.service.CityService;
import com.ecommerce.library.service.CountryService;
import com.ecommerce.library.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final CustomerService customerService;
    private final CountryService countryService;
    private  final CityService cityService;
    @GetMapping("/customer-account")
    public String CustomerAccount(Model model){
        List<Customer> customers = customerService.AllAccount();
        List<Country> countryList = countryService.findAll();
        List<City> cities = cityService.findAll();
        model.addAttribute("customers", customers);
        model.addAttribute("size", customers.size());
        model.addAttribute("countries", countryList);
        model.addAttribute("cities", cities);
        return "customer-account";
    }
    @GetMapping("/admin-account")
    public String AdminAccount(Model model){
        List<Admin> admins = adminService.AllAccount();
        model.addAttribute("admins", admins);
        model.addAttribute("size", admins.size());
        return "admin-account";
    }
    @RequestMapping(value = "/delete-customer",method = {RequestMethod.PUT, RequestMethod.GET})
    public String deleteCustomer(Long id, RedirectAttributes attributes){
        customerService.deleteAccountC(id);
        attributes.addFlashAttribute("success", "Xóa tài khoản thành công");
        return "redirect:/orders";
    }
    @GetMapping("/update-customer/{username}")
    public String updateCus(@PathVariable("username") String username, Model model, Principal principal){
        CustomerDto customerDto = customerService.getCustomer(username);
        List<Country> countryList = countryService.findAll();
        List<City> cities = cityService.findAll();
        model.addAttribute("title", "Thêm món ăn");
        model.addAttribute("customerDto", customerDto);
        model.addAttribute("countries", countryList);
        model.addAttribute("cities", cities);
        return "update-customer";
    }
    @PostMapping("/update-customer/{username}")
    public String updateProduct(@ModelAttribute("customerDto") CustomerDto customerDto,
                                RedirectAttributes redirectAttributes, Principal principal) {
        try {
            if (principal == null) {
                return "redirect:/login";
            }
            customerService.update(customerDto);
            redirectAttributes.addFlashAttribute("success", "Chỉnh sửa thành công");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server, please try again!");
        }
        return "redirect:/customer-account";
    }
}
