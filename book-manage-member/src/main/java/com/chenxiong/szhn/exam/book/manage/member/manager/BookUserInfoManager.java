package com.chenxiong.szhn.exam.book.manage.member.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenxiong.szhn.exam.book.manage.member.entity.BookUserInfo;
import com.chenxiong.szhn.exam.book.manage.member.mapper.BookUserInfoMapper;
import org.springframework.stereotype.Component;

/**
 * 用户Manager（数据访问层）
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Component
public class BookUserInfoManager extends ServiceImpl<BookUserInfoMapper, BookUserInfo> {

    /**
     * 根据用户名查询用户
     */
    public BookUserInfo getByUsername(String username) {
        return this.lambdaQuery().eq(BookUserInfo::getUsername, username).one();
    }
}
