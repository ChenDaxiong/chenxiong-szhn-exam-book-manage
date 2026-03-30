package com.chenxiong.szhn.exam.book.manage.biz.controller;

import com.chenxiong.szhn.exam.book.manage.biz.api.dto.BookCategoryDTO;
import com.chenxiong.szhn.exam.book.manage.biz.api.service.BookCategoryService;
import com.chenxiong.szhn.exam.book.manage.biz.api.vo.BookCategoryVO;
import com.chenxiong.szhn.exam.book.manage.common.annotation.WebLog;
import com.chenxiong.szhn.exam.book.manage.common.enums.OperationType;
import com.chenxiong.szhn.exam.book.manage.common.login.PermissionUtil;
import com.chenxiong.szhn.exam.book.manage.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 图书分类控制器
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@RestController
@RequestMapping("/api/category")
@Api(tags = "图书分类管理")
public class BookCategoryController {

    @Resource
    private BookCategoryService bookCategoryService;

    @ApiOperation("查询所有分类")
    @GetMapping("/list")
    @WebLog(title = "分类管理", operationType = OperationType.SELECT)
    public Result<List<BookCategoryVO>> listCategories() {
        return Result.from(bookCategoryService.listAllCategories());
    }

    @ApiOperation("查询分类详情")
    @GetMapping("/detail/{id}")
    @WebLog(title = "分类管理", operationType = OperationType.SELECT)
    public Result<BookCategoryVO> getCategoryDetail(@ApiParam("分类ID") @PathVariable Long id) {
        return Result.from(bookCategoryService.getCategoryDetail(id));
    }

    @ApiOperation("新增分类")
    @PostMapping("/add")
    @WebLog(title = "分类管理", operationType = OperationType.INSERT)
    public Result<Void> addCategory(@Validated @RequestBody BookCategoryDTO dto) {
        PermissionUtil.checkAdmin();
        bookCategoryService.addCategory(dto);
        return Result.successMsg("新增分类成功");
    }

    @ApiOperation("修改分类")
    @PutMapping("/update")
    @WebLog(title = "分类管理", operationType = OperationType.UPDATE)
    public Result<Void> updateCategory(@Validated @RequestBody BookCategoryDTO dto) {
        PermissionUtil.checkAdmin();
        bookCategoryService.updateCategory(dto);
        return Result.successMsg("修改分类成功");
    }

    @ApiOperation("删除分类")
    @DeleteMapping("/delete/{id}")
    @WebLog(title = "分类管理", operationType = OperationType.DELETE)
    public Result<Void> deleteCategory(@ApiParam("分类ID") @PathVariable Long id) {
        PermissionUtil.checkAdmin();
        bookCategoryService.deleteCategory(id);
        return Result.successMsg("删除分类成功");
    }
}
