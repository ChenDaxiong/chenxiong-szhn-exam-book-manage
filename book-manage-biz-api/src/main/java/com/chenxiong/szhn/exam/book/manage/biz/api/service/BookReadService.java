package com.chenxiong.szhn.exam.book.manage.biz.api.service;

import com.chenxiong.szhn.exam.book.manage.biz.api.dto.BookQueryDTO;
import com.chenxiong.szhn.exam.book.manage.biz.api.vo.BookVO;
import com.chenxiong.szhn.exam.book.manage.common.result.PageResult;
import com.chenxiong.szhn.exam.book.manage.common.result.ServiceResult;

/**
 * 图书读取Service接口
 *
 * @author chenxiong
 * @date 2026/3/28
 */
public interface BookReadService {

    ServiceResult<PageResult<BookVO>> pageBookList(BookQueryDTO queryDTO);

    ServiceResult<BookVO> getBookDetail(Long id);
}
