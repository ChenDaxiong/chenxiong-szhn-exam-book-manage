package com.chenxiong.szhn.exam.book.manage.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenxiong.szhn.exam.book.manage.biz.api.dto.BookQueryDTO;
import com.chenxiong.szhn.exam.book.manage.biz.api.service.BookReadService;
import com.chenxiong.szhn.exam.book.manage.biz.api.vo.BookVO;
import com.chenxiong.szhn.exam.book.manage.biz.entity.Book;
import com.chenxiong.szhn.exam.book.manage.biz.entity.BookCategory;
import com.chenxiong.szhn.exam.book.manage.biz.manager.BookCategoryManager;
import com.chenxiong.szhn.exam.book.manage.biz.manager.BookManager;
import com.chenxiong.szhn.exam.book.manage.biz.strategy.BookQueryStrategyFactory;
import com.chenxiong.szhn.exam.book.manage.common.annotation.ServiceLog;
import com.chenxiong.szhn.exam.book.manage.common.result.ErrorCode;
import com.chenxiong.szhn.exam.book.manage.common.result.PageResult;
import com.chenxiong.szhn.exam.book.manage.common.result.ServiceResult;
import com.chenxiong.szhn.exam.book.manage.common.util.ThrowUtil;
import com.chenxiong.szhn.exam.book.manage.common.cache.CacheService;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 图书读取 Service
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Slf4j
@Service
public class BookReadServiceImpl implements BookReadService {

    public static final String CACHE_KEY_BOOK_LIST = "book:list:";

    private static final TypeReference<Page<BookVO>> PAGE_TYPE_REF = new TypeReference<Page<BookVO>>() {};

    @Resource
    private BookManager bookManager;

    @Resource
    private BookCategoryManager bookCategoryManager;

    @Resource
    private BookQueryStrategyFactory bookQueryStrategyFactory;

    @Resource
    private CacheService cacheService;

    @ServiceLog(value = "分页查询图书", logResult = false)
    @Override
    public ServiceResult<PageResult<BookVO>> pageBookList(BookQueryDTO queryDTO) {
        // 1. 构建缓存 key
        String cacheKey = buildListCacheKey(queryDTO);
        // 2. 缓存查询
        Page<BookVO> cached = cacheService.get(cacheKey, PAGE_TYPE_REF);
        if (cached != null) {
            return ServiceResult.success(PageResult.of(cached));
        }
        // 3. 缓存未命中，走db或es查询
        IPage<BookVO> voPage = bookQueryStrategyFactory.getStrategy(queryDTO).queryBooks(queryDTO);
        // 4. 写入缓存
        cacheService.put(cacheKey, voPage);
        return ServiceResult.success(PageResult.of(voPage));
    }

    @ServiceLog("查询图书详情")
    @Override
    public ServiceResult<BookVO> getBookDetail(Long id) {
        Book book = bookManager.getById(id);
        ThrowUtil.throwIfNull(book, ErrorCode.BOOK_NOT_FOUND);
        return ServiceResult.success(toBookVO(book));
    }

    private String buildListCacheKey(BookQueryDTO queryDTO) {
        return CACHE_KEY_BOOK_LIST
                + queryDTO.getCurrent() + ":"
                + queryDTO.getSize() + ":"
                + Optional.ofNullable(queryDTO.getBookName()).orElse("") + ":"
                + Optional.ofNullable(queryDTO.getAuthor()).orElse("") + ":"
                + Optional.ofNullable(queryDTO.getCategoryId()).map(String::valueOf).orElse("");
    }

    private BookVO toBookVO(Book book) {
        BookVO vo = new BookVO();
        BeanUtil.copyProperties(book, vo);
        vo.setCategoryName(getCategoryName(book.getCategoryId()));
        return vo;
    }

    private String getCategoryName(Long categoryId) {
        return Optional.ofNullable(categoryId)
                .map(id -> {
                    try {
                        BookCategory cat = bookCategoryManager.getById(id);
                        return cat != null ? cat.getCategoryName() : null;
                    } catch (Exception e) {
                        return null;
                    }
                })
                .orElse(null);
    }
}
