package com.ecommerce.library.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDto {
    @Size(min = 3, max = 10, message = "Họ phải trong khoảng 3 - 10 từ")
    private String firstName;
    @Size(min = 3, max = 10, message = "Tên phải trong khoảng 3 - 10 từ")
    private String lastName;
    private String username;
    @Size(min = 5, max = 10, message = "Mật khẩu phải chứa ít nhất 5 ký tự")
    private String password;
    private String repeatPassword;
}
