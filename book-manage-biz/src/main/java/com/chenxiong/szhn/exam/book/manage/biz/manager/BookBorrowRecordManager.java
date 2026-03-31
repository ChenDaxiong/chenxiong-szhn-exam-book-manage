package com.chenxiong.szhn.exam.book.manage.biz.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenxiong.szhn.exam.book.manage.biz.entity.BookBorrowRecord;
import com.chenxiong.szhn.exam.book.manage.biz.mapper.BookBorrowRecordMapper;
import org.springframework.stereotype.Component;

/**
 * 图书借阅记录Manager
 * @author chenxiong
 * @date 2026/3/31
 */
@Component
public class BookBorrowRecordManager extends ServiceImpl<BookBorrowRecordMapper, BookBorrowRecord> {
}

