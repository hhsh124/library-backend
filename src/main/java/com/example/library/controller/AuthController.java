package com.example.library.controller;

import com.example.library.dto.LoginRequest;
import com.example.library.dto.RegisterRequest;
import com.example.library.entity.SysUser;
import com.example.library.repository.SysUserRepository;
import com.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private UserService userService; // 👈 注入 Service

    // 登录接口 (保持原样，直接查库)
    @PostMapping("/login")
    public SysUser login(@RequestBody LoginRequest request) {
        SysUser user = sysUserRepository.findByUsername(request.getUsername());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        return user;
    }

    // 👇👇👇 注册接口 (修改为调用 Service)
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        // 1. 准备数据
        SysUser newUser = new SysUser();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(request.getPassword());
        newUser.setName(request.getName());
        newUser.setRole("USER"); // 强制为普通用户

        // 2. 核心：调用 Service 的 saveUser 方法
        // 这样才能触发 UserService 里的 "sendMessage" 逻辑！
        userService.saveUser(newUser);

        return "注册成功";
    }
}