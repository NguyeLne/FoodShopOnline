package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.AdminDto;
import com.ecommerce.library.model.Admin;
import com.ecommerce.library.repository.AdminRepository;
import com.ecommerce.library.repository.RoleRepository;
import com.ecommerce.library.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Admin findByUserName(String username) {
        return adminRepository.findByUserName(username);
    }

    @Override
    public Admin save(AdminDto adminDto) {
        Admin admin = new Admin();
        admin.setFirstName(adminDto.getFirstName());
        admin.setLastName(adminDto.getLastName());
        admin.setUserName(adminDto.getUserName());
        admin.setPassWord(adminDto.getPassWord());
        admin.setRoles(Arrays.asList(roleRepository.findByName("ADMIN")));
        return adminRepository.save(admin);
    }
}
