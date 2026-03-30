package com.chenxiong.szhn.exam.book.manage.common.login;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录用户信息
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Data
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 角色
     */
    private String roleCode;
}
