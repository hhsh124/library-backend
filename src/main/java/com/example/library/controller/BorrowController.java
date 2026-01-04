package com.example.library.controller;

import com.example.library.entity.BorrowRecord;
import com.example.library.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    // 借书 POST /api/borrow
    @PostMapping
    public String borrow(@RequestBody Map<String, Long> request) {
        Long userId = request.get("userId");
        Long bookId = request.get("bookId");
        if (userId == null || bookId == null) {
            throw new RuntimeException("参数缺失");
        }
        borrowService.borrowBook(bookId, userId);
        return "借阅成功";
    }

    // 还书 POST /api/borrow/return/{recordId}
    @PostMapping("/return/{recordId}")
    public String returnBook(@PathVariable Long recordId) {
        borrowService.returnBook(recordId);
        return "归还成功";
    }

    // 查某人记录 GET /api/borrow/my
    @GetMapping("/my")
    public List<BorrowRecord> getMyRecords(@RequestParam Long userId) {
        return borrowService.getMyRecords(userId);
    }

    // 查逾期记录 GET /api/borrow/overdue
    @GetMapping("/overdue")
    public List<BorrowRecord> getOverdueRecords() {
        return borrowService.getOverdueRecords();
    }

    /**
     * 👇 新增：续借接口
     * POST /api/borrow/renew
     * 参数示例：{"recordId": 10, "days": 5}
     */
    @PostMapping("/renew")
    public String renew(@RequestBody Map<String, Object> request) {
        Long recordId = Long.valueOf(request.get("recordId").toString());
        Integer days = Integer.valueOf(request.get("days").toString());

        borrowService.renewBook(recordId, days);
        return "续借成功";
    }
}