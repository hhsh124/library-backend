package com.example.library.controller;

import com.example.library.entity.SysUser;
import com.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 获取用户列表
    @GetMapping
    public Page<SysUser> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search) {
        return userService.getUserList(page, size, search);
    }

    // 新增用户
    @PostMapping
    public SysUser add(@RequestBody SysUser user) {
        return userService.saveUser(user);
    }

    // 更新用户
    @PutMapping
    public SysUser update(@RequestBody SysUser user) {
        return userService.saveUser(user);
    }

    // 删除用户
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    // 管理员重置密码
    @PostMapping("/{id}/reset-password")
    public String resetPassword(@PathVariable Long id) {
        userService.resetPassword(id);
        return "密码已重置为 123456";
    }

    // 用户修改密码 (个人中心用)
    @PostMapping("/change-password")
    public String changePassword(@RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        String oldPwd = request.get("oldPwd").toString();
        String newPwd = request.get("newPwd").toString();

        userService.updatePassword(userId, oldPwd, newPwd);
        return "密码修改成功";
    }
}