package com.example.library.controller;

import com.example.library.entity.SysMessage;
import com.example.library.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    // 获取我的消息
    @GetMapping
    public List<SysMessage> getMyMessages(@RequestParam Long userId) {
        return messageService.getMyMessages(userId);
    }

    // 发送消息 (管理员手动发送/催还接口)
    @PostMapping
    public String sendMessage(@RequestBody Map<String, Object> body) {
        Long userId = Long.valueOf(body.get("userId").toString());
        String content = body.get("content").toString();
        messageService.sendMessage(userId, content);
        return "发送成功";
    }

    // 👇👇👇 新增：删除消息接口
    // 请求方式：DELETE /api/messages/{id}
    @DeleteMapping("/{id}")
    public String deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return "删除成功";
    }
}