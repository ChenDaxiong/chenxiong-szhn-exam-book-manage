package com.chenxiong.szhn.exam.book.manage.common.login;

import com.chenxiong.szhn.exam.book.manage.common.constant.SystemConstant;
import com.chenxiong.szhn.exam.book.manage.common.result.ErrorCode;
import com.chenxiong.szhn.exam.book.manage.common.result.Result;
import com.chenxiong.szhn.exam.book.manage.common.session.SessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录用户拦截器
 * 1. 从 Cookie 中读取 session ID，通过 SessionService 获取 LoginUser 放入 ThreadLocal
 * 2. 对需要认证的接口，未登录直接返回
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;
    private final SessionService sessionService;

    public LoginInterceptor(ObjectMapper objectMapper, SessionService sessionService) {
        this.objectMapper = objectMapper;
        this.sessionService = sessionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String sessionId = getSessionIdFromCookie(request);
        if (sessionId != null) {
            LoginUser loginUser = sessionService.get(sessionId);
            if (loginUser != null) {
                sessionService.refresh(sessionId);
                LoginContextHolder.setLoginUser(loginUser);
                return true;
            }
        }
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(SystemConstant.CHARSET_UTF8);
        response.setStatus(SystemConstant.HTTP_STATUS_OK);
        Result<Void> result = Result.fail(ErrorCode.UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(result));
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        LoginContextHolder.clear();
    }

    /**
     * 从请求 Cookie 中提取 session ID
     */
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
