package com.example.library.repository;

import com.example.library.entity.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SysUserRepository extends JpaRepository<SysUser, Long> {
    SysUser findByUsername(String username);

    Page<SysUser> findByUsernameContainingOrNameContaining(String username, String name, Pageable pageable);

    // 👇👇👇 新增：根据角色查询所有用户 (用来找所有管理员)
    List<SysUser> findByRole(String role);
}