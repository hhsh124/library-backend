package com.example.library.repository;

import com.example.library.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByUserIdOrderByBorrowDateDesc(Long userId);

    List<BorrowRecord> findByReturnDateIsNullAndDueDateBefore(LocalDateTime now);

    // 👇👇👇 新增：根据用户ID删除记录
    void deleteByUserId(Long userId);
}