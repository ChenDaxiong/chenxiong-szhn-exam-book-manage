package com.chenxiong.szhn.exam.book.manage.member.api.service;

import com.chenxiong.szhn.exam.book.manage.member.api.dto.LoginDTO;
import com.chenxiong.szhn.exam.book.manage.member.api.dto.RegisterDTO;
import com.chenxiong.szhn.exam.book.manage.common.login.LoginUser;
import com.chenxiong.szhn.exam.book.manage.common.result.ServiceResult;

/**
 * 认证Service接口，包含用户注册登录
 *
 * @author chenxiong
 * @date 2026/3/28
 */
public interface AuthService {

    /**
     * 用户注册
     *
     * @param dto
     * @return
     */
    ServiceResult<LoginUser> register(RegisterDTO dto);

    /**
     * 用户登录
     *
     * @param dto
     * @return
     */
    ServiceResult<LoginUser> login(LoginDTO dto);

    /**
     * 获取当前登录用户
     *
     * @return
     */
    ServiceResult<LoginUser> getCurrentUser();
}
