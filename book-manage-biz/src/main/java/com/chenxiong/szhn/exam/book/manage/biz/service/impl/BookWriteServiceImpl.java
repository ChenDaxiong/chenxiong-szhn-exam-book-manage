package com.chenxiong.szhn.exam.book.manage.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import com.chenxiong.szhn.exam.book.manage.biz.api.dto.BookDTO;
import com.chenxiong.szhn.exam.book.manage.biz.api.service.BookWriteService;
import com.chenxiong.szhn.exam.book.manage.biz.entity.Book;
import com.chenxiong.szhn.exam.book.manage.biz.manager.BookManager;
import com.chenxiong.szhn.exam.book.manage.common.annotation.ServiceLog;
import com.chenxiong.szhn.exam.book.manage.common.constant.SystemConstant;
import com.chenxiong.szhn.exam.book.manage.common.result.ErrorCode;
import com.chenxiong.szhn.exam.book.manage.common.result.ServiceResult;
import com.chenxiong.szhn.exam.book.manage.common.util.ThrowUtil;
import com.chenxiong.szhn.exam.book.manage.common.cache.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 图书写入 Service 实现
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Slf4j
@Service
public class BookWriteServiceImpl implements BookWriteService {

    @Resource
    private BookManager bookManager;

    @Resource
    private CacheService cacheService;

    @ServiceLog("新增图书")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResult<Void> addBook(BookDTO dto) {
        ThrowUtil.throwIf(StrUtil.isBlank(dto.getBookName()), ErrorCode.PARAM_ERROR, "图书名称不能为空");

        long count = bookManager.lambdaQuery().eq(Book::getBookName, dto.getBookName().trim()).count();
        ThrowUtil.throwIf(count > 0, ErrorCode.BOOK_EXISTS);

        Book book = new Book();
        BeanUtil.copyProperties(dto, book, CopyOptions.create()
                .ignoreNullValue()
                .setIgnoreProperties("id"));
        book.setBookName(dto.getBookName().trim());
        book.setStock(Optional.ofNullable(dto.getStock()).orElse(SystemConstant.DEFAULT_STOCK));
        book.setStatus(Optional.ofNullable(dto.getStatus()).orElse(SystemConstant.STATUS_ENABLE));

        bookManager.save(book);
        cacheService.evictByPrefix(BookReadServiceImpl.CACHE_KEY_BOOK_LIST);
        log.info("addBook success，bookName：{}", book.getBookName());
        return ServiceResult.success();
    }

    @ServiceLog("修改图书")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResult<Void> updateBook(BookDTO dto) {
        ThrowUtil.throwIfNull(dto.getId(), ErrorCode.PARAM_ERROR, "图书ID不能为空");

        Book book = bookManager.getById(dto.getId());
        ThrowUtil.throwIfNull(book, ErrorCode.BOOK_NOT_FOUND);

        if (StrUtil.isNotBlank(dto.getBookName())) {
            String newName = dto.getBookName().trim();
            if (!newName.equals(book.getBookName())) {
                long count = bookManager.lambdaQuery().eq(Book::getBookName, newName).count();
                ThrowUtil.throwIf(count > 0, ErrorCode.BOOK_EXISTS);
            }
        }

        BeanUtil.copyProperties(dto, book, CopyOptions.create()
                .ignoreNullValue()
                .setIgnoreProperties("id"));

        bookManager.updateById(book);
        cacheService.evictByPrefix(BookReadServiceImpl.CACHE_KEY_BOOK_LIST);
        log.info("updateBook success，ID：{}，bookName：{}", book.getId(), book.getBookName());
        return ServiceResult.success();
    }

    @ServiceLog("删除图书")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResult<Void> deleteBook(Long id) {
        Book book = bookManager.getById(id);
        ThrowUtil.throwIfNull(book, ErrorCode.BOOK_NOT_FOUND);

        bookManager.removeById(id);
        cacheService.evictByPrefix(BookReadServiceImpl.CACHE_KEY_BOOK_LIST);
        log.info("deleteBook success，ID：{}，bookName：{}",  book.getId(), book.getBookName());
        return ServiceResult.success();
    }
}
