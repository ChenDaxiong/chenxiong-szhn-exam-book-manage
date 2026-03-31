package com.chenxiong.szhn.exam.book.manage.common.annotation;

import java.lang.annotation.*;

/**
 * 图书列表缓存失效注解
 *
 * @author chenxiong
 * @date 2026/3/31
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ClearBookListCache {
}
