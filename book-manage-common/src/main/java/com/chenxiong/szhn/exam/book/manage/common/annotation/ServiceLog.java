package com.chenxiong.szhn.exam.book.manage.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Service 方法日志注解
 * 拦截service层
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceLog {

    /**
     * 业务操作描述
     */
    String value() default "";

    /**
     * 需要脱敏的字段
     */
    String[] sensitiveFields() default {};

    /**
     * 是否打印出参，默认true
     */
    boolean logResult() default true;
}
