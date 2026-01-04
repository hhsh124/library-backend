package com.example.library.service;

import com.example.library.entity.Book;
import org.springframework.data.domain.Page;

public interface BookService {
    // 分页查询
    Page<Book> getBookList(int page, int size, String search);

    // 新增或保存
    Book saveBook(Book book);

    // 删除
    void deleteBook(Long id);

    // 👇 建议加上这个查询单本详情的方法
    Book getBookById(Long id);
}