package com.chenxiong.szhn.exam.book.manage.biz.controller;

import com.chenxiong.szhn.exam.book.manage.biz.api.service.BookBorrowService;
import com.chenxiong.szhn.exam.book.manage.biz.api.vo.BorrowRecordVO;
import com.chenxiong.szhn.exam.book.manage.common.annotation.WebLog;
import com.chenxiong.szhn.exam.book.manage.common.enums.OperationType;
import com.chenxiong.szhn.exam.book.manage.common.login.LoginUser;
import com.chenxiong.szhn.exam.book.manage.common.login.PermissionUtil;
import com.chenxiong.szhn.exam.book.manage.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenxiong
 * @date 2026/3/31
 */
@RestController
@RequestMapping("/api/borrow")
@Api(tags = "图书借阅")
public class BookBorrowController {
    @Resource
    private BookBorrowService bookBorrowService;

    @ApiOperation("借阅图书")
    @PostMapping("/{bookId}")
    @WebLog(title = "借阅", operationType = OperationType.INSERT)
    public Result<Void> borrowBook(@ApiParam("图书ID") @PathVariable Long bookId) {
        LoginUser loginUser = PermissionUtil.checkLogin();
        return Result.from(bookBorrowService.borrowBook(bookId,loginUser.getUserId()));
    }

    @ApiOperation("归还图书")
    @PostMapping("/return/{bookId}")
    @WebLog(title = "借阅", operationType = OperationType.UPDATE)
    public Result<Void> returnBook(@ApiParam("图书ID") @PathVariable Long bookId) {
        LoginUser loginUser = PermissionUtil.checkLogin();
        return Result.from(bookBorrowService.returnBook(bookId,loginUser.getUserId()));
    }

    @ApiOperation("查询我的借阅列表")
    @GetMapping("/mylist")
    @WebLog(title = "借阅", operationType = OperationType.SELECT)
    public Result<List<BorrowRecordVO>> myBorrowList() {
        LoginUser loginUser = PermissionUtil.checkLogin();
        return Result.from(bookBorrowService.myBorrowList(loginUser.getUserId()));
    }
}
