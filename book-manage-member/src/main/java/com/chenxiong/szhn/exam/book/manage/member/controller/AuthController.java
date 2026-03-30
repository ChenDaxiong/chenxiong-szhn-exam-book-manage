package com.chenxiong.szhn.exam.book.manage.member.controller;

import com.chenxiong.szhn.exam.book.manage.member.api.dto.LoginDTO;
import com.chenxiong.szhn.exam.book.manage.member.api.dto.RegisterDTO;
import com.chenxiong.szhn.exam.book.manage.member.api.service.AuthService;
import com.chenxiong.szhn.exam.book.manage.common.annotation.WebLog;
import com.chenxiong.szhn.exam.book.manage.common.constant.SystemConstant;
import com.chenxiong.szhn.exam.book.manage.common.enums.OperationType;
import com.chenxiong.szhn.exam.book.manage.common.login.LoginContextHolder;
import com.chenxiong.szhn.exam.book.manage.common.login.LoginUser;
import com.chenxiong.szhn.exam.book.manage.common.result.Result;
import com.chenxiong.szhn.exam.book.manage.common.result.ServiceResult;
import com.chenxiong.szhn.exam.book.manage.common.session.SessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * authController
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@RestController
@RequestMapping("/api/auth")
@Api(tags = "认证管理")
public class AuthController {

    @Resource
    private AuthService authService;

    @Resource
    private SessionService sessionService;

    @ApiOperation("用户注册")
    @PostMapping("/register")
    @WebLog(title = "认证管理", operationType = OperationType.INSERT)
    public Result<LoginUser> register(@Validated @RequestBody RegisterDTO dto, HttpServletResponse response) {
        ServiceResult<LoginUser> serviceResult = authService.register(dto);
        if (serviceResult.isSuccess()) {
            createSession(serviceResult.getData(), response);
        }
        return Result.from(serviceResult);
    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    @WebLog(title = "认证管理", operationType = OperationType.LOGIN)
    public Result<LoginUser> login(@Validated @RequestBody LoginDTO dto, HttpServletResponse response) {
        ServiceResult<LoginUser> serviceResult = authService.login(dto);
        if (serviceResult.isSuccess()) {
            createSession(serviceResult.getData(), response);
        }
        return Result.from(serviceResult);
    }

    @ApiOperation("用户登出")
    @PostMapping("/logout")
    @WebLog(title = "认证管理", operationType = OperationType.LOGOUT)
    public Result<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        String sessionId = getSessionIdFromCookie(request);
        if (sessionId != null) {
            sessionService.remove(sessionId);
        }
        clearSessionCookie(response);
        LoginContextHolder.clear();
        return Result.successMsg("登出成功");
    }

    @ApiOperation("获取当前登录用户信息")
    @GetMapping("/current")
    @WebLog(title = "认证管理", operationType = OperationType.SELECT)
    public Result<LoginUser> currentUser() {
        return Result.from(authService.getCurrentUser());
    }

    private void createSession(LoginUser loginUser, HttpServletResponse response) {
        String sessionId = UUID.randomUUID().toString().replace("-", "");
        sessionService.set(sessionId, loginUser);
        LoginContextHolder.setLoginUser(loginUser);

        Cookie cookie = new Cookie(SystemConstant.SESSION_COOKIE_NAME, sessionId);
        cookie.setPath(SystemConstant.SESSION_COOKIE_PATH);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(-1);
        response.addCookie(cookie);
    }

    private void clearSessionCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(SystemConstant.SESSION_COOKIE_NAME, "");
        cookie.setPath(SystemConstant.SESSION_COOKIE_PATH);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    private String getSessionIdFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (SystemConstant.SESSION_COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
