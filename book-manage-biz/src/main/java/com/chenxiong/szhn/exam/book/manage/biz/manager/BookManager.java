package com.chenxiong.szhn.exam.book.manage.biz.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenxiong.szhn.exam.book.manage.biz.entity.Book;
import com.chenxiong.szhn.exam.book.manage.biz.mapper.BookMapper;
import org.springframework.stereotype.Component;

/**
 * 图书数据访问层Manager
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Component
public class BookManager extends ServiceImpl<BookMapper, Book> {
}
