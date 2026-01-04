package com.example.library.controller; // 为了方便，直接放在 controller 包里

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 作用：拦截所有 controller 抛出的异常，把异常信息直接返回给前端
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        // e.getMessage() 就是我们在代码里写的 "密码错误"、"用户不存在" 等文字
        // 这里的 status(400) 表示这是一个“客户端请求错误”，不是服务器崩溃
        return ResponseEntity.status(400).body(e.getMessage());
    }
}