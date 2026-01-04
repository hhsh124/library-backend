package com.example.library.dto;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String name; // 真实姓名
}