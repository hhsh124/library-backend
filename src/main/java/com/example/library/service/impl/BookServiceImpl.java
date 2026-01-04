package com.example.library.service.impl; // 如果你在 service 包下，就去掉 .impl

import com.example.library.entity.Book;
import com.example.library.repository.BookRepository;
import com.example.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Page<Book> getBookList(int page, int size, String search) {
        // 👇👇👇 核心修改在这里：
        // Sort.by("location").ascending() 表示按【馆藏位置】A-Z 正序排列
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by("location").ascending());

        if (search == null || search.isEmpty()) {
            return bookRepository.findAll(pageRequest);
        } else {
            // 注意：确保你的 Repository 里有 findByTitleContainingOrAuthorContaining 方法
            return bookRepository.findByTitleContainingOrAuthorContaining(search, search, pageRequest);
        }
    }

    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }
}