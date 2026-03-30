package com.chenxiong.szhn.exam.book.manage.biz.strategy;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenxiong.szhn.exam.book.manage.biz.api.dto.BookQueryDTO;
import com.chenxiong.szhn.exam.book.manage.biz.api.vo.BookVO;
import com.chenxiong.szhn.exam.book.manage.biz.entity.Book;
import com.chenxiong.szhn.exam.book.manage.biz.entity.BookCategory;
import com.chenxiong.szhn.exam.book.manage.biz.manager.BookCategoryManager;
import com.chenxiong.szhn.exam.book.manage.biz.manager.BookManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 数据库查询策略
 * 路由到db查询，默认走书名和作者都走精确查询
 *
 * @author chenxiong
 * @date 2026/3/29
 */
@Slf4j
@Component("dbBookQueryStrategy")
public class DbBookQueryStrategy implements BookQueryStrategy {

    @Resource
    private BookManager bookManager;

    @Resource
    private BookCategoryManager bookCategoryManager;

    @Override
    public IPage<BookVO> queryBooks(BookQueryDTO queryDTO) {
        Page<Book> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(StrUtil.isNotBlank(queryDTO.getBookName()), Book::getBookName, queryDTO.getBookName());
        wrapper.eq(StrUtil.isNotBlank(queryDTO.getAuthor()), Book::getAuthor, queryDTO.getAuthor());
        wrapper.eq(queryDTO.getCategoryId() != null, Book::getCategoryId, queryDTO.getCategoryId());

        wrapper.orderByDesc(Book::getGmtCreate);

        IPage<Book> bookPage = bookManager.page(page, wrapper);

        IPage<BookVO> voPage = new Page<>(bookPage.getCurrent(), bookPage.getSize(), bookPage.getTotal());
        List<BookVO> voList = bookPage.getRecords().stream()
                .map(this::toBookVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    private BookVO toBookVO(Book book) {
        BookVO vo = new BookVO();
        BeanUtil.copyProperties(book, vo);
        vo.setCategoryName(getCategoryName(book.getCategoryId()));
        return vo;
    }

    private String getCategoryName(Long categoryId) {
        return Optional.ofNullable(categoryId)
                .map(bookCategoryManager::getById)
                .map(BookCategory::getCategoryName)
                .orElse(null);
    }
}
