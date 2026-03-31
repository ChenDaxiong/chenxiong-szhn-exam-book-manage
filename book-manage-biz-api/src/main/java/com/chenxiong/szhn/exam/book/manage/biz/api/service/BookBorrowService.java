package com.chenxiong.szhn.exam.book.manage.biz.api.service;

import com.chenxiong.szhn.exam.book.manage.biz.api.vo.BorrowRecordVO;
import com.chenxiong.szhn.exam.book.manage.common.result.ServiceResult;

import java.util.List;

/**
 * 图书借阅service
 *
 * @author chenxiong
 * @date 2026/3/31
 */
public interface BookBorrowService {

    /**
     * 借阅图书
     *
     * @param bookId
     * @return
     */
    ServiceResult<Void> borrowBook(Long bookId,Long userId);

    /**
     * 归还图书
     *
     * @param bookId
     * @return
     */
    ServiceResult<Void> returnBook(Long bookId,Long userId);

    /**
     * 查询当前用户的借阅列表
     *
     * @return
     */
    ServiceResult<List<BorrowRecordVO>> myBorrowList(Long userId);

}
