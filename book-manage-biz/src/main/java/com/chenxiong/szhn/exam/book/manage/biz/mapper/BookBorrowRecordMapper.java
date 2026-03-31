package com.chenxiong.szhn.exam.book.manage.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chenxiong.szhn.exam.book.manage.biz.entity.BookBorrowRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 图书借阅记录Mapper
 *
 * @author chenxiong
 * @date 2026/3/31
 */
@Mapper
public interface BookBorrowRecordMapper extends BaseMapper<BookBorrowRecord> {
}
