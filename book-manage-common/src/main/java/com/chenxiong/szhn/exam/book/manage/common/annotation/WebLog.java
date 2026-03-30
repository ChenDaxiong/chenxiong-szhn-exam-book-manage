package com.chenxiong.szhn.exam.book.manage.common.annotation;

import com.chenxiong.szhn.exam.book.manage.common.aspect.WebLogAspect;
import com.chenxiong.szhn.exam.book.manage.common.enums.OperationType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Controller 操作日志注解
 * 拦截controller层
 *
 * @author chenxiong
 * @date 2026/3/28
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebLog {


    String title() default "";


    OperationType operationType() default OperationType.OTHER;

    /**
     * 是否打印出参，默认true
     */
    boolean logResult() default true;
}
