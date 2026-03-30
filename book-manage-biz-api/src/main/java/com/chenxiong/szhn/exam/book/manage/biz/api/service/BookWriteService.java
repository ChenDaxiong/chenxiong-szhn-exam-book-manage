package com.chenxiong.szhn.exam.book.manage.biz.api.service;

import com.chenxiong.szhn.exam.book.manage.biz.api.dto.BookDTO;
import com.chenxiong.szhn.exam.book.manage.common.result.ServiceResult;

/**
 * 图书写入Service接口
 *
 * @author chenxiong
 * @date 2026/3/28
 */
public interface BookWriteService {

    ServiceResult<Void> addBook(BookDTO dto);

    ServiceResult<Void> updateBook(BookDTO dto);

    ServiceResult<Void> deleteBook(Long id);
}
