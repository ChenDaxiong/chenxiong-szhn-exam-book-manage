package com.chenxiong.szhn.exam.book.manage.biz.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenxiong.szhn.exam.book.manage.biz.entity.BookCategory;
import com.chenxiong.szhn.exam.book.manage.biz.mapper.BookCategoryMapper;
import org.springframework.stereotype.Component;

/**
 * 图书分类数据访问层Manager
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Component
public class BookCategoryManager extends ServiceImpl<BookCategoryMapper, BookCategory> {
}
