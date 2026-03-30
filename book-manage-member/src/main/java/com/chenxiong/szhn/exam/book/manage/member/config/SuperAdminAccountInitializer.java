package com.chenxiong.szhn.exam.book.manage.member.config;

import com.chenxiong.szhn.exam.book.manage.member.entity.BookUserInfo;
import com.chenxiong.szhn.exam.book.manage.member.manager.BookUserInfoManager;
import com.chenxiong.szhn.exam.book.manage.common.constant.SystemConstant;
import com.chenxiong.szhn.exam.book.manage.common.enums.UserRoleEnum;
import com.chenxiong.szhn.exam.book.manage.common.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 超级管理员账号初始化器，密码需要加密，系统初始化时
 *
 * @author chenxiong
 * @date 2026/3/29
 */
@Slf4j
@Component
public class SuperAdminAccountInitializer {

    @Resource
    private BookUserInfoManager bookUserInfoManager;

    @EventListener(ApplicationReadyEvent.class)
    public void initDefaultAccounts() {
        initSuperAdmin();
    }

    private void initSuperAdmin() {
        try {
            if (bookUserInfoManager.getByUsername(SystemConstant.DEFAULT_SUPER_ADMIN_USERNAME) != null) {
                return;
            }
            BookUserInfo superAdmin = new BookUserInfo();
            superAdmin.setUsername(SystemConstant.DEFAULT_SUPER_ADMIN_USERNAME);
            superAdmin.setPassword(PasswordUtil.encode(SystemConstant.DEFAULT_SUPER_ADMIN_PASSWORD));
            superAdmin.setNickname(SystemConstant.DEFAULT_SUPER_ADMIN_NICKNAME);
            superAdmin.setRoleCode(UserRoleEnum.SUPER_ADMIN.getCode());
            superAdmin.setStatus(SystemConstant.STATUS_ENABLE);
            bookUserInfoManager.save(superAdmin);
        } catch (Throwable e) {
            log.error("initSuperAdmin failed", e);
            // 失败不影响项目启动
        }
    }

}
