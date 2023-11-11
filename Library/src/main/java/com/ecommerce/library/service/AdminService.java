package com.ecommerce.library.service;

import com.ecommerce.library.dto.AdminDto;
import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.Admin;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;

import java.util.List;

public interface AdminService {
    Admin save(AdminDto adminDto);

    Admin findByUsername(String username);

    List<Admin> AllAccount();


    ///////
    void deleteAccount(Long id);

}
