package com.chenxiong.szhn.exam.book.manage.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chenxiong.szhn.exam.book.manage.biz.entity.Book;
import org.apache.ibatis.annotations.Mapper;

/**
 * 图书 Mapper
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Mapper
public interface BookMapper extends BaseMapper<Book> {
}
