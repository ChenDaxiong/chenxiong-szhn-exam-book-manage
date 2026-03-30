package com.chenxiong.szhn.exam.book.manage.member.service.impl;

import cn.hutool.core.util.StrUtil;
import com.chenxiong.szhn.exam.book.manage.common.exception.BusinessException;
import com.chenxiong.szhn.exam.book.manage.member.api.dto.LoginDTO;
import com.chenxiong.szhn.exam.book.manage.member.api.dto.RegisterDTO;
import com.chenxiong.szhn.exam.book.manage.member.api.service.AuthService;
import com.chenxiong.szhn.exam.book.manage.member.entity.BookUserInfo;
import com.chenxiong.szhn.exam.book.manage.member.manager.BookUserInfoManager;
import com.chenxiong.szhn.exam.book.manage.common.annotation.ServiceLog;
import com.chenxiong.szhn.exam.book.manage.common.constant.SystemConstant;
import com.chenxiong.szhn.exam.book.manage.common.enums.UserRoleEnum;
import com.chenxiong.szhn.exam.book.manage.common.login.LoginContextHolder;
import com.chenxiong.szhn.exam.book.manage.common.login.LoginUser;
import com.chenxiong.szhn.exam.book.manage.common.result.ErrorCode;
import com.chenxiong.szhn.exam.book.manage.common.result.ServiceResult;
import com.chenxiong.szhn.exam.book.manage.common.util.PasswordUtil;
import com.chenxiong.szhn.exam.book.manage.common.util.PasswordValidator;
import com.chenxiong.szhn.exam.book.manage.common.util.ThrowUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 认证ServiceImpl,包含用户注册登录
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private BookUserInfoManager bookUserInfoManager;

    @ServiceLog(value = "用户注册", sensitiveFields = "password")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResult<LoginUser> register(RegisterDTO dto) {
        // 1. 基础校验
        baseValidate(dto);
        String username = dto.getUsername().trim();
        String nickname = dto.getNickname().trim();

        ThrowUtil.throwIf(bookUserInfoManager.getByUsername(username) != null, ErrorCode.USER_EXISTS);
        PasswordValidator.validate(dto.getPassword());

        BookUserInfo user = new BookUserInfo();
        user.setUsername(username);
        user.setPassword(PasswordUtil.encode(dto.getPassword()));
        user.setNickname(nickname);
        user.setRoleCode(UserRoleEnum.USER.getCode());
        user.setStatus(SystemConstant.STATUS_ENABLE);
        bookUserInfoManager.save(user);

        log.info("register success：{}", username);
        return ServiceResult.success(buildLoginUser(user));
    }

    @ServiceLog(value = "用户登录", sensitiveFields = "password")
    @Override
    public ServiceResult<LoginUser> login(LoginDTO dto) {
        BookUserInfo user = bookUserInfoManager.getByUsername(dto.getUsername());
        ThrowUtil.throwIf(user == null || !PasswordUtil.matches(dto.getPassword(), user.getPassword()),
                ErrorCode.LOGIN_FAILED);
        ThrowUtil.throwIf(user.getStatus() != null && user.getStatus() == SystemConstant.STATUS_DISABLE,
                ErrorCode.ACCOUNT_DISABLED);

        log.info("login success：{}", user.getUsername());
        return ServiceResult.success(buildLoginUser(user));
    }

    @ServiceLog("获取当前登录用户")
    @Override
    public ServiceResult<LoginUser> getCurrentUser() {
        LoginUser loginUser = LoginContextHolder.getLoginUser();
        ThrowUtil.throwIf(loginUser == null, ErrorCode.UNAUTHORIZED);
        return ServiceResult.success(loginUser);
    }

    public static void baseValidate(RegisterDTO registerDTO) {
        ThrowUtil.throwIf(registerDTO == null || StringUtils.isBlank(registerDTO.getUsername()), ErrorCode.PARAM_ERROR);
        ThrowUtil.throwIf(registerDTO.getUsername().length() < SystemConstant.USER_NAME_MIN_LENGTH
                , ErrorCode.PARAM_ERROR, "用户名不能少于" + SystemConstant.USER_NAME_MIN_LENGTH + "个字符");
        ThrowUtil.throwIf(registerDTO.getUsername().length() > SystemConstant.USER_NAME_MAX_LENGTH
                , ErrorCode.PARAM_ERROR, "用户名长度不能超过" + SystemConstant.USER_NAME_MAX_LENGTH + "个字符");
    }

    private LoginUser buildLoginUser(BookUserInfo user) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getId());
        loginUser.setUsername(user.getUsername());
        loginUser.setNickname(user.getNickname());
        loginUser.setRoleCode(user.getRoleCode());
        return loginUser;
    }
}
