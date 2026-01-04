package com.example.library.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sys_message")
public class SysMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // 接收人
    private String content; // 内容
    private Boolean isRead = false; // 默认未读
    private LocalDateTime createTime = LocalDateTime.now();
}