package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/products")
    public String products(Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        List<ProductDto> productDtoList = productService.findAll();
        model.addAttribute("title", "Manage Product");
        model.addAttribute("products", productDtoList);
        model.addAttribute("size", productDtoList.size());
        return "products";
    }

    @GetMapping("/products/{pageNo}")
    public String productsPage(@PathVariable("pageNo") int pageNo, Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        Page<Product> products = productService.pageProducts(pageNo);
        model.addAttribute("title","Quản lý Món ăn");
        model.addAttribute("size",products.getSize());
        model.addAttribute("totalPages",products.getTotalPages());
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("products",products);
        return "products";
    }
    @GetMapping("/search_result/{pageNo}")
    public String searchProducts(@PathVariable("pageNo") int pageNo,
                    @RequestParam("keyword") String keyword,
                    Model model,
                    Principal principal){
        if(principal==null){
            return "redirect:/login";
        }
        Page<Product> products = productService.searchProducts(pageNo,keyword);
        model.addAttribute("title","Tìm Kiếm Món ăn");
        model.addAttribute("size",products.getSize());
        model.addAttribute("totalPages",products.getTotalPages());
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("products",products);
        return "result_products";
    }
// T2 add git file sau 8020
    // Xong phân trang + Tìm kiếm
    @GetMapping("/add_product")
    public String addProductForm(Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        List<Category> categories = categoryService.findAllByActivated();
        model.addAttribute("categories", categories);
        model.addAttribute("product", new ProductDto());
        return "add_product";
    }
    @PostMapping("/save_product")
    public String saveProduct(@ModelAttribute("product")ProductDto productDto,
                              @RequestParam("imageProduct")MultipartFile imageProduct,
                              RedirectAttributes attributes){
        try{
            productService.save(imageProduct, productDto);
            attributes.addFlashAttribute("success", "Thêm món thành công");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Lỗi");
        }
        return "redirect:/products";
    }
    @GetMapping("/update_product/{id}")
    public String updateProductForm(@PathVariable("id") Long id, Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        model.addAttribute("title", "Chỉnh sủa món ăn");
        List<Category> categories = categoryService.findAllByActivated();
        ProductDto productDto = productService.getById(id);
        model.addAttribute("categories", categories);
        model.addAttribute("productDto", productDto);
        return "update_product";
    }
    @PostMapping("/update_product/{id}")
    public String processUpdate(@PathVariable("id") Long id,
                                @ModelAttribute("productDto") ProductDto productDto,
                                @RequestParam("imageProduct")MultipartFile imageProduct,
                                RedirectAttributes attributes
    ){
        try {
            productService.update(imageProduct, productDto);
            attributes.addFlashAttribute("success", "Chỉnh sửa thành công");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Lỗi");
        }
        return "redirect:/products";
        // Lỗi nếu chỉnh sửa không cập nhật ảnh sẽ bị mất hình ban đầu.
    }

    @RequestMapping(value = "/enable_product/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String enableProduct(@PathVariable("id") Long id, RedirectAttributes attributes){
        try {
            productService.enableById(id);
            attributes.addFlashAttribute("success", "Kích hoạt thành công");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Lỗi");
            return  null;
        }
        return "redirect:/products";
    }
    @RequestMapping(value = "/delete_product/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String deleteProduct(@PathVariable("id") Long id, RedirectAttributes attributes){
        try {
            productService.deleteById(id);
            attributes.addFlashAttribute("success", "Hủy Kích hoạt thành công");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Lỗi");
            return  null;
        }
        return "redirect:/products";
    }
    // Xong phần product
}
