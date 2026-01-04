package com.example.library.service;

import com.example.library.entity.Book;
import com.example.library.entity.BorrowRecord;
import com.example.library.entity.SysUser;
import com.example.library.repository.BookRepository;
import com.example.library.repository.BorrowRecordRepository;
import com.example.library.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BorrowService {

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private SysUserRepository sysUserRepository;

    /**
     * 1. 借阅图书
     */
    @Transactional
    public void borrowBook(Long bookId, Long userId) {
        // A. 检查图书是否存在且可借
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("图书不存在"));

        if (!"可借".equals(book.getStatus())) {
            throw new RuntimeException("该书状态不可借（" + book.getStatus() + "）");
        }

        // B. 检查用户是否存在
        SysUser user = sysUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // C. 创建借阅记录
        BorrowRecord record = new BorrowRecord();
        record.setBook(book);
        record.setUser(user);
        record.setBorrowDate(LocalDateTime.now());

        // 👇👇👇 改回来了：默认借阅 15 天
        record.setDueDate(LocalDateTime.now().plusDays(15));

        borrowRecordRepository.save(record);

        // D. 更新图书状态为 "已借"
        book.setStatus("已借");
        bookRepository.save(book);
    }

    /**
     * 2. 归还图书
     */
    @Transactional
    public void returnBook(Long recordId) {
        // A. 找到借阅记录
        BorrowRecord record = borrowRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("借阅记录不存在"));

        // B. 校验是否已经还过
        if (record.getReturnDate() != null) {
            throw new RuntimeException("该书已归还，请勿重复操作");
        }

        // C. 设置归还时间为当前
        record.setReturnDate(LocalDateTime.now());
        borrowRecordRepository.save(record);

        // D. 图书状态改回 "可借"
        Book book = record.getBook();
        book.setStatus("可借");
        bookRepository.save(book);
    }

    /**
     * 3. 查询某人的借阅记录 (按时间倒序)
     */
    public List<BorrowRecord> getMyRecords(Long userId) {
        return borrowRecordRepository.findByUserIdOrderByBorrowDateDesc(userId);
    }

    /**
     * 4. 查询所有逾期记录 (管理员用)
     * 条件：未归还 且 应还时间 < 当前时间
     */
    public List<BorrowRecord> getOverdueRecords() {
        return borrowRecordRepository.findByReturnDateIsNullAndDueDateBefore(LocalDateTime.now());
    }

    /**
     * 5. 续借图书
     */
    @Transactional
    public void renewBook(Long recordId, int days) {
        // A. 找到记录
        BorrowRecord record = borrowRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("记录不存在"));

        // B. 校验：只有未归还的书才能续借
        if (record.getReturnDate() != null) {
            throw new RuntimeException("该书已归还，无法续借");
        }

        // C. 校验：如果已经逾期，禁止续借
        if (record.getDueDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("该书已逾期，请先归还");
        }

        // D. 延长应还时间
        record.setDueDate(record.getDueDate().plusDays(days));

        borrowRecordRepository.save(record);
    }
}