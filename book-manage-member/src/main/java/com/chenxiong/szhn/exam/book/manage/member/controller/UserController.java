package com.chenxiong.szhn.exam.book.manage.member.controller;

import com.chenxiong.szhn.exam.book.manage.member.api.dto.UserDTO;
import com.chenxiong.szhn.exam.book.manage.member.api.dto.UserQueryDTO;
import com.chenxiong.szhn.exam.book.manage.member.api.service.BookUserInfoService;
import com.chenxiong.szhn.exam.book.manage.member.api.vo.UserVO;
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
 * 用户管理控制器（仅管理员可访问）
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户管理")
public class UserController {

    @Resource
    private BookUserInfoService bookUserInfoService;

    @ApiOperation("分页查询用户列表")
    @GetMapping("/page")
    @WebLog(title = "用户管理", operationType = OperationType.SELECT, logResult = false)
    public Result<PageResult<UserVO>> pageUserList(UserQueryDTO queryDTO) {
        PermissionUtil.checkSuperAdmin();
        return Result.from(bookUserInfoService.pageUserList(queryDTO));
    }

    @ApiOperation("查询用户详情")
    @GetMapping("/detail/{id}")
    @WebLog(title = "用户管理", operationType = OperationType.SELECT)
    public Result<UserVO> getUserDetail(@ApiParam("用户ID") @PathVariable Long id) {
        PermissionUtil.checkSuperAdmin();
        return Result.from(bookUserInfoService.getUserDetail(id));
    }

    @ApiOperation("新增用户")
    @PostMapping("/add")
    @WebLog(title = "用户管理", operationType = OperationType.INSERT)
    public Result<Void> addUser(@Validated @RequestBody UserDTO dto) {
        PermissionUtil.checkSuperAdmin();
        bookUserInfoService.addUser(dto);
        return Result.successMsg("新增用户成功");
    }

    @ApiOperation("修改用户")
    @PutMapping("/update")
    @WebLog(title = "用户管理", operationType = OperationType.UPDATE)
    public Result<Void> updateUser(@Validated @RequestBody UserDTO dto) {
        PermissionUtil.checkSuperAdmin();
        bookUserInfoService.updateUser(dto);
        return Result.successMsg("修改用户成功");
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/delete/{id}")
    @WebLog(title = "用户管理", operationType = OperationType.DELETE)
    public Result<Void> deleteUser(@ApiParam("用户ID") @PathVariable Long id) {
        PermissionUtil.checkSuperAdmin();
        bookUserInfoService.deleteUser(id);
        return Result.successMsg("删除用户成功");
    }
}
