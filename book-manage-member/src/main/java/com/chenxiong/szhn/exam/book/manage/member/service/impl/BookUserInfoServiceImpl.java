package com.chenxiong.szhn.exam.book.manage.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenxiong.szhn.exam.book.manage.member.api.dto.UserDTO;
import com.chenxiong.szhn.exam.book.manage.member.api.dto.UserQueryDTO;
import com.chenxiong.szhn.exam.book.manage.member.api.service.BookUserInfoService;
import com.chenxiong.szhn.exam.book.manage.member.api.vo.UserVO;
import com.chenxiong.szhn.exam.book.manage.member.entity.BookUserInfo;
import com.chenxiong.szhn.exam.book.manage.member.manager.BookUserInfoManager;
import com.chenxiong.szhn.exam.book.manage.common.annotation.ServiceLog;
import com.chenxiong.szhn.exam.book.manage.common.constant.SystemConstant;
import com.chenxiong.szhn.exam.book.manage.common.enums.UserRoleEnum;
import com.chenxiong.szhn.exam.book.manage.common.result.ErrorCode;
import com.chenxiong.szhn.exam.book.manage.common.result.PageResult;
import com.chenxiong.szhn.exam.book.manage.common.result.ServiceResult;
import com.chenxiong.szhn.exam.book.manage.common.util.PasswordUtil;
import com.chenxiong.szhn.exam.book.manage.common.util.PasswordValidator;
import com.chenxiong.szhn.exam.book.manage.common.util.ThrowUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户Service实现
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Slf4j
@Service
public class BookUserInfoServiceImpl implements BookUserInfoService {

    @Resource
    private BookUserInfoManager bookUserInfoManager;

    @ServiceLog(value = "分页查询用户", logResult = false)
    @Override
    public ServiceResult<PageResult<UserVO>> pageUserList(UserQueryDTO queryDTO) {
        Page<BookUserInfo> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
        LambdaQueryWrapper<BookUserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(queryDTO.getUsername()), BookUserInfo::getUsername, queryDTO.getUsername());
        wrapper.orderByDesc(BookUserInfo::getGmtCreate);
        IPage<BookUserInfo> userPage = bookUserInfoManager.page(page, wrapper);

        IPage<UserVO> voPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<UserVO> voList = userPage.getRecords().stream()
                .map(this::toUserVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        return ServiceResult.success(PageResult.of(voPage));
    }

    @ServiceLog("查询用户详情")
    @Override
    public ServiceResult<UserVO> getUserDetail(Long id) {
        BookUserInfo user = bookUserInfoManager.getById(id);
        ThrowUtil.throwIfNull(user, ErrorCode.USER_NOT_FOUND);
        return ServiceResult.success(toUserVO(user));
    }

    @ServiceLog(value = "新增用户", sensitiveFields = "password")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResult<Void> addUser(UserDTO dto) {
        ThrowUtil.throwIf(StrUtil.isBlank(dto.getUsername()), ErrorCode.PARAM_ERROR, "用户名不能为空");
        String username = dto.getUsername().trim();
        ThrowUtil.throwIf(bookUserInfoManager.getByUsername(username) != null, ErrorCode.USER_EXISTS);
        ThrowUtil.throwIf(StrUtil.isBlank(dto.getPassword()), ErrorCode.PARAM_ERROR, "密码不能为空");
        PasswordValidator.validate(dto.getPassword());

        BookUserInfo user = new BookUserInfo();
        BeanUtil.copyProperties(dto, user, CopyOptions.create().ignoreNullValue()
                .setIgnoreProperties("id", "password"));
        user.setUsername(username);
        user.setPassword(PasswordUtil.encode(dto.getPassword()));
        user.setNickname(Optional.ofNullable(dto.getNickname())
                .map(String::trim)
                .filter(StrUtil::isNotBlank)
                .orElse(username));
        if (StrUtil.isBlank(dto.getRoleCode())) {
            user.setRoleCode(UserRoleEnum.USER.getCode());
        }
        if (dto.getStatus() == null) {
            user.setStatus(SystemConstant.STATUS_ENABLE);
        }
        bookUserInfoManager.save(user);
        log.info("addUser success,username：{}", username);
        return ServiceResult.success();
    }

    @ServiceLog(value = "修改用户", sensitiveFields = "password")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResult<Void> updateUser(UserDTO dto) {
        ThrowUtil.throwIfNull(dto.getId(), ErrorCode.PARAM_ERROR, "用户ID不能为空");
        BookUserInfo user = bookUserInfoManager.getById(dto.getId());
        ThrowUtil.throwIfNull(user, ErrorCode.USER_NOT_FOUND);

        if (StrUtil.isNotBlank(dto.getUsername()) && !dto.getUsername().trim().equals(user.getUsername())) {
            String newUsername = dto.getUsername().trim();
            ThrowUtil.throwIf(bookUserInfoManager.getByUsername(newUsername) != null, ErrorCode.USER_EXISTS);
            user.setUsername(newUsername);
        }
        if (StrUtil.isNotBlank(dto.getPassword())) {
            PasswordValidator.validate(dto.getPassword());
            user.setPassword(PasswordUtil.encode(dto.getPassword()));
        }
        if (StrUtil.isNotBlank(dto.getNickname())) {
            user.setNickname(dto.getNickname().trim());
        }
        if (StrUtil.isNotBlank(dto.getRoleCode())) {
            user.setRoleCode(dto.getRoleCode());
        }
        if (dto.getStatus() != null) {
            user.setStatus(dto.getStatus());
        }
        bookUserInfoManager.updateById(user);
        log.info("updateUser success,userName{}", user.getUsername());
        return ServiceResult.success();
    }

    @ServiceLog("删除用户")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResult<Void> deleteUser(Long id) {
        BookUserInfo user = bookUserInfoManager.getById(id);
        ThrowUtil.throwIfNull(user, ErrorCode.USER_NOT_FOUND);
        bookUserInfoManager.removeById(id);
        log.info("deleteUser success,userName：{}", user.getUsername());
        return ServiceResult.success();
    }

    private UserVO toUserVO(BookUserInfo user) {
        UserVO vo = new UserVO();
        BeanUtil.copyProperties(user, vo);
        vo.setRoleName(getRoleName(user.getRoleCode()));
        return vo;
    }

    private String getRoleName(String roleCode) {
        return Optional.ofNullable(roleCode)
                .map(code -> {
                    for (UserRoleEnum role : UserRoleEnum.values()) {
                        if (role.getCode().equals(code)) {
                            return role.getName();
                        }
                    }
                    return null;
                })
                .orElse(null);
    }
}
