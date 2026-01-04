package com.example.library.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String isbn;      // 新增
    private String title;
    private String author;
    private String publisher; // 新增
    private String location;  // 新增
    private String status;    // 新增
    private BigDecimal price;
    private LocalDate publishDate;
}