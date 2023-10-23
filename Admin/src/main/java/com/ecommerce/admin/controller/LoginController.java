package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.AdminDto;
import com.ecommerce.library.model.Admin;
import com.ecommerce.library.service.impl.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class LoginController {
    @Autowired
    private AdminServiceImpl adminService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @GetMapping("/login")
    public String loginForm(Model model){
        model.addAttribute("title","Login Admin");
        return "login";
    }

//    @RequestMapping("/index")
//    public String home(Model model){
//        model.addAttribute("title","Home Admin");
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
//            return "redirect:/login";
//        }
//        return "index";
//    }
    @RequestMapping("/index")
    public String home(Model model){
        model.addAttribute("title", "Home Page");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof  AnonymousAuthenticationToken){
            return "redirect:/login";
        }
        return "index";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("title","Dang ky Admin");
        model.addAttribute("adminDto",new AdminDto());
        return "register";
    }
    @GetMapping("/forgot-password")
    public String forgotPassword(Model model){
        model.addAttribute("title","Quen mat khau");
        return "forgot-password";
    }

    @PostMapping("/register-new")
    public String addNewAdmin(@Valid @ModelAttribute("adminDto") AdminDto adminDto,
                              BindingResult result,
                              Model model
                              ){
        try {
//            session.removeAttribute("message");
            if (result.hasErrors()){
                model.addAttribute("adminDto",adminDto);
                result.toString();
                return "register";
            }
            String username = adminDto.getUserName();
            Admin admin = adminService.findByUserName(username);
            if (admin != null){
                model.addAttribute("adminDto",adminDto);
                System.out.println("Admin khong rong");
                model.addAttribute("emailError", "Email da duoc dang ky truoc");
                return "register";
            }
            if (adminDto.getPassWord().equals(adminDto.getRepPassWord())){
                adminDto.setPassWord(passwordEncoder.encode(adminDto.getPassWord()));
                adminService.save(adminDto);
                System.out.println("Thanh cong");
                model.addAttribute("success", "Dang ky thanh cong");
                model.addAttribute("adminDto",adminDto);
            }else {
                model.addAttribute("adminDto",adminDto);
                model.addAttribute("passWordError","Mat khau khong trung khop");
                System.out.println("Mat khau khong trung khop");
                return "register";
            }

        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("errors","Error Server");
        }
        return "register";
    }

}
