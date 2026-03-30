package com.chenxiong.szhn.exam.book.manage.member.api.service;

import com.chenxiong.szhn.exam.book.manage.member.api.dto.UserDTO;
import com.chenxiong.szhn.exam.book.manage.member.api.dto.UserQueryDTO;
import com.chenxiong.szhn.exam.book.manage.member.api.vo.UserVO;
import com.chenxiong.szhn.exam.book.manage.common.result.PageResult;
import com.chenxiong.szhn.exam.book.manage.common.result.ServiceResult;

/**
 * 用户Service接口
 *
 * @author chenxiong
 * @date 2026/3/28
 */
public interface BookUserInfoService {

    ServiceResult<PageResult<UserVO>> pageUserList(UserQueryDTO queryDTO);

    ServiceResult<UserVO> getUserDetail(Long id);

    ServiceResult<Void> addUser(UserDTO dto);

    ServiceResult<Void> updateUser(UserDTO dto);

    ServiceResult<Void> deleteUser(Long id);
}
