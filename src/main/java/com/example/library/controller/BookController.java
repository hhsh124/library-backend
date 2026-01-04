package com.example.library.controller;

import com.example.library.entity.Book;
import com.example.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService; // 注入 Service 接口

    @GetMapping
    public Page<Book> findByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search) {
        return bookService.getBookList(page, size, search);
    }

    @PostMapping
    public Book save(@RequestBody Book book) {
        return bookService.saveBook(book); // 这里的逻辑如果以后变复杂，Controller也不用改，改Service就行
    }

    @PutMapping
    public Book update(@RequestBody Book book) {
        return bookService.saveBook(book);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}