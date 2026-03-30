package com.chenxiong.szhn.exam.book.manage.biz.strategy;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chenxiong.szhn.exam.book.manage.biz.api.dto.BookQueryDTO;
import com.chenxiong.szhn.exam.book.manage.biz.api.vo.BookVO;

/**
 * 图书查询策略接口
 *
 * @author chenxiong
 * @date 2026/3/29
 */
public interface BookQueryStrategy {

    IPage<BookVO> queryBooks(BookQueryDTO queryDTO);
}
