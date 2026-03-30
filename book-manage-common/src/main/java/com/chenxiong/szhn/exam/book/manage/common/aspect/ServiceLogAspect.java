package com.chenxiong.szhn.exam.book.manage.common.aspect;

import cn.hutool.json.JSONUtil;
import com.chenxiong.szhn.exam.book.manage.common.annotation.ServiceLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Service层日志切面
 *
 * @author chenxiong
 * @date 2026/3/29
 */
@Slf4j
@Aspect
@Component
public class ServiceLogAspect {

    private static final int MAX_PARAM_LENGTH = 500;
    private static final int MAX_RESULT_LENGTH = 300;
    private static final String RESULT_SUCCESS = "SUCCESS";
    private static final String RESULT_FAIL = "FAILED";
    private static final String MASK = "******";
    private static final String RESULT_OMITTED = "RESULT_OMITTED";

    @Around("@annotation(serviceLog)")
    public Object around(ProceedingJoinPoint joinPoint, ServiceLog serviceLog) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // 接口名：取实现的第一个接口，没有则取类名
        Class<?> implClass = joinPoint.getTarget().getClass();
        Class<?>[] interfaces = implClass.getInterfaces();
        String interfaceName = (interfaces.length > 0) ? interfaces[0].getSimpleName() : implClass.getSimpleName();

        String methodName = signature.getName();
        String description = serviceLog.value();
        String descTag = description.isEmpty() ? "" : ("[" + description + "] ");
        Set<String> sensitiveFields = new HashSet<>(Arrays.asList(serviceLog.sensitiveFields()));

        // 入参
        String params = buildParamsLog(signature.getParameterNames(), joinPoint.getArgs(), sensitiveFields);

        log.info("SERVICE_LOG start，{}{}#{} - request: {}", descTag, interfaceName, methodName, params);

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long costTime = System.currentTimeMillis() - startTime;

            // 出参
            String resultStr = serviceLog.logResult() ? buildResultLog(result) : RESULT_OMITTED;
            log.info("SERVICE_LOG end ,{}{}#{} - resultCode: {} | response: {} | cost: {}ms",
                    descTag, interfaceName, methodName, RESULT_SUCCESS, resultStr, costTime);
            return result;
        } catch (Throwable e) {
            long costTime = System.currentTimeMillis() - startTime;
            log.error("SERVICE_LOG exception,{}{}#{} - resultCode: {} | errMSg: {} | cost: {}ms",
                    descTag, interfaceName, methodName, RESULT_FAIL, e.getMessage(), costTime);
            throw e;
        }
    }

    /**
     * 构建入参日志，对敏感字段做掩码处理,密码场景
     */
    private String buildParamsLog(String[] paramNames, Object[] args, Set<String> sensitiveFields) {
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
            // 对 JSON 中的敏感字段做脱敏
            if (!sensitiveFields.isEmpty()) {
                value = maskSensitiveFields(value, sensitiveFields);
            }
            sb.append(value);
        }
        return sb.toString();
    }

    /**
     * 构建出参日志
     */
    private String buildResultLog(Object result) {
        if (result == null) {
            return "void";
        }
        return toLogString(result, MAX_RESULT_LENGTH);
    }

    /**
     * 对 JSON 字符串中的敏感字段值做掩码替换
     */
    private String maskSensitiveFields(String json, Set<String> sensitiveFields) {
        String result = json;
        for (String field : sensitiveFields) {
            // 匹配 "field":"value" 格式（字符串值）
            result = result.replaceAll(
                    "\"" + field + "\"\\s*:\\s*\"[^\"]*\"",
                    "\"" + field + "\":\"" + MASK + "\""
            );
            // 匹配 "field":nonStringValue 格式（数字等非字符串值）
            result = result.replaceAll(
                    "\"" + field + "\"\\s*:\\s*([^,}\"\\]]+)",
                    "\"" + field + "\":\"" + MASK + "\""
            );
        }
        return result;
    }

    /**
     * 对象转日志字符串，Servlet 类型只打印类名，其他转 JSON 并截断
     */
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
