package com.example.library.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "borrow_record")
public class BorrowRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 关联图书：多对一
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id")
    private Book book;

    // 关联用户：多对一
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private SysUser user;

    // 借出时间
    private LocalDateTime borrowDate;

    // 归还时间 (为空表示还没还)
    private LocalDateTime returnDate;

    // 应还时间 (用于判断逾期)
    @Column(name = "due_date")
    private LocalDateTime dueDate;
}