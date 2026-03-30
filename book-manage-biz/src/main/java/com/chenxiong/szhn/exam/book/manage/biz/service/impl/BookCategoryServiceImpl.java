package com.chenxiong.szhn.exam.book.manage.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.chenxiong.szhn.exam.book.manage.biz.api.dto.BookCategoryDTO;
import com.chenxiong.szhn.exam.book.manage.biz.api.service.BookCategoryService;
import com.chenxiong.szhn.exam.book.manage.biz.api.vo.BookCategoryVO;
import com.chenxiong.szhn.exam.book.manage.biz.entity.BookCategory;
import com.chenxiong.szhn.exam.book.manage.biz.manager.BookCategoryManager;
import com.chenxiong.szhn.exam.book.manage.common.annotation.ServiceLog;
import com.chenxiong.szhn.exam.book.manage.common.constant.SystemConstant;
import com.chenxiong.szhn.exam.book.manage.common.result.ErrorCode;
import com.chenxiong.szhn.exam.book.manage.common.result.ServiceResult;
import com.chenxiong.szhn.exam.book.manage.common.util.ThrowUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 图书分类 Service 实现
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Slf4j
@Service
public class BookCategoryServiceImpl implements BookCategoryService {

    @Resource
    private BookCategoryManager bookCategoryManager;

    @ServiceLog("查询所有分类")
    @Override
    public ServiceResult<List<BookCategoryVO>> listAllCategories() {
        List<BookCategory> categories = bookCategoryManager.lambdaQuery()
                .orderByAsc(BookCategory::getSortOrder).list();
        List<BookCategoryVO> voList = categories.stream()
                .map(this::toBookCategoryVO)
                .collect(Collectors.toList());
        return ServiceResult.success(voList);
    }

    @ServiceLog("查询分类详情")
    @Override
    public ServiceResult<BookCategoryVO> getCategoryDetail(Long id) {
        BookCategory category = bookCategoryManager.getById(id);
        ThrowUtil.throwIfNull(category, ErrorCode.CATEGORY_NOT_FOUND);
        return ServiceResult.success(toBookCategoryVO(category));
    }

    @ServiceLog("新增分类")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResult<Void> addCategory(BookCategoryDTO dto) {
        BookCategory category = new BookCategory();
        BeanUtil.copyProperties(dto, category, CopyOptions.create().ignoreNullValue().setIgnoreProperties("id"));
        category.setSortOrder(Optional.ofNullable(dto.getSortOrder()).orElse(SystemConstant.DEFAULT_SORT_ORDER));
        category.setParentId(Optional.ofNullable(dto.getParentId()).orElse(SystemConstant.TOP_PARENT_ID));
        bookCategoryManager.save(category);
        log.info("addCategory success：{}", dto.getCategoryName());
        return ServiceResult.success();
    }

    @ServiceLog("修改分类")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResult<Void> updateCategory(BookCategoryDTO dto) {
        ThrowUtil.throwIfNull(dto.getId(), ErrorCode.PARAM_ERROR, "分类ID不能为空");
        BookCategory category = bookCategoryManager.getById(dto.getId());
        ThrowUtil.throwIfNull(category, ErrorCode.CATEGORY_NOT_FOUND);
        BeanUtil.copyProperties(dto, category, CopyOptions.create().ignoreNullValue().setIgnoreProperties("id"));
        bookCategoryManager.updateById(category);
        log.info("updateCategory success：{}", category.getCategoryName());
        return ServiceResult.success();
    }

    @ServiceLog("删除分类")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResult<Void> deleteCategory(Long id) {
        BookCategory category = bookCategoryManager.getById(id);
        ThrowUtil.throwIfNull(category, ErrorCode.CATEGORY_NOT_FOUND);
        long childCount = bookCategoryManager.lambdaQuery().eq(BookCategory::getParentId, id).count();
        ThrowUtil.throwIf(childCount > 0, ErrorCode.CATEGORY_HAS_CHILDREN);
        bookCategoryManager.removeById(id);
        log.info("deleteCategory success：{}", category.getCategoryName());
        return ServiceResult.success();
    }

    private BookCategoryVO toBookCategoryVO(BookCategory category) {
        BookCategoryVO vo = new BookCategoryVO();
        BeanUtil.copyProperties(category, vo);
        return vo;
    }
}
