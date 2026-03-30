package com.chenxiong.szhn.exam.book.manage.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 图书管理系统启动类
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@SpringBootApplication(scanBasePackages = "com.chenxiong.szhn.exam.book.manage")
@EnableTransactionManagement
public class BookManageApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(BookManageApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BookManageApplication.class);
    }
}
