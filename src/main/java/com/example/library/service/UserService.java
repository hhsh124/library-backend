package com.example.library.service;

import com.example.library.entity.SysUser;
import com.example.library.repository.BorrowRecordRepository;
import com.example.library.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Autowired
    private MessageService messageService;

    /**
     * 1. 分页查询用户列表
     */
    public Page<SysUser> getUserList(int page, int size, String search) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by("id").descending());
        if (search == null || search.isEmpty()) {
            return sysUserRepository.findAll(pageRequest);
        } else {
            return sysUserRepository.findByUsernameContainingOrNameContaining(search, search, pageRequest);
        }
    }

    /**
     * 2. 新增或更新用户 (包含消息通知逻辑)
     */
    public SysUser saveUser(SysUser user) {
        boolean isNew = (user.getId() == null); // 标记是否是新用户

        if (isNew) {
            // A. 查重
            SysUser exist = sysUserRepository.findByUsername(user.getUsername());
            if (exist != null) {
                throw new RuntimeException("用户名 '" + user.getUsername() + "' 已存在");
            }
            // B. 默认密码
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                user.setPassword("123456");
            }
        } else {
            // 更新逻辑：如果不传密码则保持原样
            SysUser oldUser = sysUserRepository.findById(user.getId()).orElse(null);
            if (oldUser != null && (user.getPassword() == null || user.getPassword().isEmpty())) {
                user.setPassword(oldUser.getPassword());
            }
        }

        // C. 保存到数据库
        SysUser savedUser = sysUserRepository.save(user);

        // D. 如果是新注册用户，发送通知
        if (isNew) {
            // 1. 给【新用户】发欢迎消息
            String welcomeMsg = "欢迎您，" + savedUser.getName() + "！欢迎加入智慧图书馆，祝您阅读愉快。";
            messageService.sendMessage(savedUser.getId(), welcomeMsg);

            // 2. 给【所有管理员】发通知 (如果注册的是普通用户)
            if ("USER".equals(savedUser.getRole())) {
                List<SysUser> admins = sysUserRepository.findByRole("ADMIN");
                for (SysUser admin : admins) {
                    String notifyMsg = "【新用户注册】用户 " + savedUser.getName() + " (" + savedUser.getUsername() + ") 已成功注册。";
                    messageService.sendMessage(admin.getId(), notifyMsg);
                }
            }
        }

        return savedUser;
    }

    /**
     * 3. 删除用户 (级联删除借阅记录)
     */
    @Transactional
    public void deleteUser(Long id) {
        // 先删借阅记录，防止外键报错
        borrowRecordRepository.deleteByUserId(id);
        // 再删人
        sysUserRepository.deleteById(id);
    }

    /**
     * 4. 管理员重置密码 (重置为 123456)
     */
    public void resetPassword(Long id) {
        SysUser user = sysUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setPassword("123456");
        sysUserRepository.save(user);
    }

    /**
     * 5. 用户修改密码 (验证旧密码)
     */
    public void updatePassword(Long userId, String oldPwd, String newPwd) {
        SysUser user = sysUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 验证旧密码
        if (!user.getPassword().equals(oldPwd)) {
            throw new RuntimeException("原密码错误，请重新输入");
        }

        // 更新新密码
        user.setPassword(newPwd);
        sysUserRepository.save(user);
    }
}