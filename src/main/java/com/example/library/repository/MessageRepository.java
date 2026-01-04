package com.example.library.repository;

import com.example.library.entity.SysMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<SysMessage, Long> {
    // 查询某人的所有消息，按时间倒序
    List<SysMessage> findByUserIdOrderByCreateTimeDesc(Long userId);

}