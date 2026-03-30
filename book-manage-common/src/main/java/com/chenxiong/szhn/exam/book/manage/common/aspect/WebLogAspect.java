package com.chenxiong.szhn.exam.book.manage.common.aspect;

import cn.hutool.json.JSONUtil;
import com.chenxiong.szhn.exam.book.manage.common.annotation.WebLog;
import com.chenxiong.szhn.exam.book.manage.common.enums.OperationType;
import com.chenxiong.szhn.exam.book.manage.common.login.LoginContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Controller 操作日志切面
 *
 * @author chenxiong
 * @date 2026/3/29
 */
@Slf4j
@Aspect
@Component
public class WebLogAspect {

    private static final int MAX_PARAM_LENGTH = 500;
    private static final int MAX_RESULT_LENGTH = 300;
    private static final String RESULT_SUCCESS = "SUCCESS";
    private static final String RESULT_FAIL = "FAIL";
    private static final String RESULT_OMITTED = "RESULT_OMITTED";
    private static final String MASK = "******";

    /**
     * 默认敏感字段集合
     */
    private static final Set<String> SENSITIVE_FIELDS = new HashSet<>(Arrays.asList("password"));

    @Around("@annotation(webLog)")
    public Object around(ProceedingJoinPoint joinPoint, WebLog webLog) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = (attributes != null) ? attributes.getRequest() : null;

        String title = webLog.title();
        OperationType operationType = webLog.operationType();
        String uri = (request != null) ? request.getRequestURI() : "";
        String username = getCurrentUsername();
        String params = buildParamsLog(signature.getParameterNames(), joinPoint.getArgs());

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long costTime = System.currentTimeMillis() - startTime;

            String resultStr = webLog.logResult() ? buildResultLog(result) : RESULT_OMITTED;
            log.info("WEB_LOG end User:{} | Module:{} | Operation:{} | Path:{} | Params:{} | Status:{} | Response:{} | Time Cost:{}ms",
                    username, title, operationType.getName(), uri, params, RESULT_SUCCESS, resultStr, costTime);

            return result;
        } catch (Throwable e) {
            long costTime = System.currentTimeMillis() - startTime;

            log.error("WEB_LOG exception User:{} | Module:{} | Operation:{} | Path:{} | Params:{} | Status:{} | Exception:{} | Time Cost:{}ms",
                    username, title, operationType.getName(), uri, params, RESULT_FAIL, e.getMessage(), costTime);

            throw e;
        }
    }

    private String getCurrentUsername() {
        String username = LoginContextHolder.getUsername();
        return (username != null) ? username : "匿名";
    }

    private String buildParamsLog(String[] paramNames, Object[] args) {
        if (args == null || args.length == 0) {
            return "无";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            String name = (paramNames != null && i < paramNames.length) ? paramNames[i] : ("arg" + i);
            sb.append(name).append("=");
            String value = toLogString(args[i], MAX_PARAM_LENGTH);
            value = maskSensitiveFields(value);
            sb.append(value);
        }
        return sb.toString();
    }

    private String buildResultLog(Object result) {
        if (result == null) {
            return "void";
        }
        return toLogString(result, MAX_RESULT_LENGTH);
    }

    /**
     * 对 JSON 字符串中的敏感字段值做掩码替换
     */
    private String maskSensitiveFields(String json) {
        String result = json;
        for (String field : SENSITIVE_FIELDS) {
            result = result.replaceAll(
                    "\"" + field + "\"\\s*:\\s*\"[^\"]*\"",
                    "\"" + field + "\":\"" + MASK + "\""
            );
            result = result.replaceAll(
                    "\"" + field + "\"\\s*:\\s*([^,}\"\\]]+)",
                    "\"" + field + "\":\"" + MASK + "\""
            );
        }
        return result;
    }

    private String toLogString(Object obj, int maxLength) {
        if (obj == null) {
            return "null";
        }
        if (isServletType(obj)) {
            return obj.getClass().getSimpleName();
        }
        try {
            String json = JSONUtil.toJsonStr(obj);
            if (json.length() > maxLength) {
                return json.substring(0, maxLength) + "...(truncated)";
            }
            return json;
        } catch (Exception e) {
            return obj.toString();
        }
    }

    private boolean isServletType(Object obj) {
        return obj instanceof HttpServletRequest
                || obj instanceof HttpServletResponse
                || obj instanceof HttpSession;
    }
}
