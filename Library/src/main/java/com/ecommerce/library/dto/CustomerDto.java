package com.ecommerce.library.dto;

import com.ecommerce.library.model.City;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    @Size(min = 2, max = 10, message = "Họ cần ít nhất 2 ký tự !")
    private String firstName;

    @Size(min = 2, max = 10, message = "Tên cần ít nhất 2 ký tự !")
    private String lastName;
    private String username;
    @Size(min = 5, max = 15, message = "Mật khẩu cần ít nhất 5 ký tự !")
    private String password;

    @Size(min = 10, max = 15, message = "Số điện thoại phải trong khoảng 5-10 ký tự !")
    private String phoneNumber;

    private String address;
    private String confirmPassword;
    private City city;
    private String image;
    private String country;

}
