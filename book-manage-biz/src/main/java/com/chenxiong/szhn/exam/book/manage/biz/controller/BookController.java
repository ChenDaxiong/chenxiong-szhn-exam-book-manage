package com.chenxiong.szhn.exam.book.manage.biz.controller;

import com.chenxiong.szhn.exam.book.manage.biz.api.dto.BookDTO;
import com.chenxiong.szhn.exam.book.manage.biz.api.dto.BookQueryDTO;
import com.chenxiong.szhn.exam.book.manage.biz.api.service.BookReadService;
import com.chenxiong.szhn.exam.book.manage.biz.api.service.BookWriteService;
import com.chenxiong.szhn.exam.book.manage.biz.api.vo.BookVO;
import com.chenxiong.szhn.exam.book.manage.common.annotation.WebLog;
import com.chenxiong.szhn.exam.book.manage.common.enums.OperationType;
import com.chenxiong.szhn.exam.book.manage.common.login.PermissionUtil;
import com.chenxiong.szhn.exam.book.manage.common.result.PageResult;
import com.chenxiong.szhn.exam.book.manage.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 图书管理Controller
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@RestController
@RequestMapping("/api/book")
@Api(tags = "图书管理")
public class BookController {

    @Resource
    private BookReadService bookReadService;

    @Resource
    private BookWriteService bookWriteService;

    @ApiOperation("分页查询图书列表")
    @GetMapping("/page")
    @WebLog(title = "图书管理", operationType = OperationType.SELECT, logResult = false)
    public Result<PageResult<BookVO>> pageBookList(BookQueryDTO queryDTO) {
        return Result.from(bookReadService.pageBookList(queryDTO));
    }

    @ApiOperation("查询图书详情")
    @GetMapping("/detail/{id}")
    @WebLog(title = "图书管理", operationType = OperationType.SELECT)
    public Result<BookVO> getBookDetail(@ApiParam("图书ID") @PathVariable Long id) {
        return Result.from(bookReadService.getBookDetail(id));
    }

    @ApiOperation("新增图书")
    @PostMapping("/add")
    @WebLog(title = "图书管理", operationType = OperationType.INSERT)
    public Result<Void> addBook(@Validated @RequestBody BookDTO dto) {
        PermissionUtil.checkAdmin();
        bookWriteService.addBook(dto);
        return Result.successMsg("新增图书成功");
    }

    @ApiOperation("修改图书")
    @PutMapping("/update")
    @WebLog(title = "图书管理", operationType = OperationType.UPDATE)
    public Result<Void> updateBook(@Validated @RequestBody BookDTO dto) {
        PermissionUtil.checkAdmin();
        bookWriteService.updateBook(dto);
        return Result.successMsg("修改图书成功");
    }

    @ApiOperation("删除图书")
    @DeleteMapping("/delete/{id}")
    @WebLog(title = "图书管理", operationType = OperationType.DELETE)
    public Result<Void> deleteBook(@ApiParam("图书ID") @PathVariable Long id) {
        PermissionUtil.checkAdmin();
        bookWriteService.deleteBook(id);
        return Result.successMsg("删除图书成功");
    }
}
