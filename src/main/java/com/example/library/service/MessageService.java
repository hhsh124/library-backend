package com.example.library.service;

import com.example.library.entity.SysMessage;
import com.example.library.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    // 1. 发送消息
    public void sendMessage(Long userId, String content) {
        SysMessage message = new SysMessage();
        message.setUserId(userId);
        message.setContent(content);
        messageRepository.save(message);
    }

    // 2. 获取某人的消息
    public List<SysMessage> getMyMessages(Long userId) {
        return messageRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }

    // 3. 标记为已读 (可选功能)
    public void markAsRead(Long id) {
        messageRepository.findById(id).ifPresent(msg -> {
            msg.setIsRead(true);
            messageRepository.save(msg);
        });
    }

    // 👇👇👇 新增：删除单条消息的方法
    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }
}