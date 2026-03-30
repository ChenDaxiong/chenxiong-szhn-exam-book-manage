package com.chenxiong.szhn.exam.book.manage.biz.api.service;

import com.chenxiong.szhn.exam.book.manage.biz.api.dto.BookCategoryDTO;
import com.chenxiong.szhn.exam.book.manage.biz.api.vo.BookCategoryVO;
import com.chenxiong.szhn.exam.book.manage.common.result.ServiceResult;

import java.util.List;

/**
 * 图书分类Service接口
 *
 * @author chenxiong
 * @date 2026/3/28
 */
public interface BookCategoryService {

    ServiceResult<List<BookCategoryVO>> listAllCategories();

    ServiceResult<BookCategoryVO> getCategoryDetail(Long id);

    ServiceResult<Void> addCategory(BookCategoryDTO dto);

    ServiceResult<Void> updateCategory(BookCategoryDTO dto);

    ServiceResult<Void> deleteCategory(Long id);
}
