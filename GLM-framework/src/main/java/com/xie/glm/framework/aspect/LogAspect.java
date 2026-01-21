package com.xie.glm.framework.aspect;

import com.alibaba.fastjson2.JSON;
import com.xie.glm.common.annotation.Log;
import com.xie.glm.common.annotation.NoLog;
import com.xie.glm.common.enums.BusinessType;
import com.xie.glm.common.util.SecurityUtil;
import com.xie.glm.framework.event.OperLogEvent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 日志切面
 *
 * <p>自动记录 Controller 层方法的操作日志，包括：
 * <ul>
 *   <li>自动记录所有 Controller 方法的基础信息（URL、方法名、IP 等）</li>
 *   <li>支持通过 @Log 注解自定义日志标题和业务类型</li>
 *   <li>支持通过 @NoLog 注解排除特定方法的日志记录</li>
 *   <li>自动捕获异常并记录错误信息</li>
 *   <li>记录方法执行耗时</li>
 * </ul>
 *
 * <p>使用示例：
 * <pre>{@code
 * // 使用 @Log 注解自定义日志
 * @Log(title = "用户管理", businessType = BusinessType.INSERT)
 * @PostMapping
 * public Result<Void> create(@RequestBody UserCreateDTO dto) {
 *     userService.create(dto);
 *     return Result.success();
 * }
 *
 * // 使用 @NoLog 注解排除日志
 * @NoLog
 * @GetMapping("/health")
 * public Result<Void> health() {
 *     return Result.success();
 * }
 * }</pre>
 *
 * @author xie
 */
@Aspect
@Component
public class LogAspect {

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 排除记录的参数类型
     */
    private static final Class<?>[] EXCLUDE_PARAM_TYPES = {
            HttpServletRequest.class,
            HttpServletResponse.class,
            org.springframework.web.multipart.MultipartFile.class,
            org.springframework.web.multipart.MultipartRequest.class
    };

    private final ApplicationEventPublisher eventPublisher;

    /**
     * 构造函数
     *
     * @param eventPublisher Spring 事件发布器，用于发布操作日志事件
     */
    public LogAspect(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * 配置织入点
     *
     * <p>切入所有 Controller 层的方法
     */
    @Pointcut("execution(* com.xie.glm.admin.controller..*.*(..))")
    public void logPointcut() {
    }

    /**
     * 环绕通知：记录操作日志
     *
     * <p>处理流程：
     * <ol>
     *   <li>检查方法或类上是否有 @NoLog 注解，有则跳过日志记录</li>
     *   <li>检查方法上是否有 @Log 注解，有则使用注解配置的参数</li>
     *   <li>收集请求信息（URL、IP、方法名等）</li>
     *   <li>记录方法执行前的时间戳</li>
     *   <li>执行目标方法</li>
     *   <li>记录方法执行后的结果和耗时</li>
     *   <li>如果方法抛出异常，记录异常信息</li>
     *   <li>发布操作日志事件，由监听器处理保存</li>
     * </ol>
     *
     * @param joinPoint 织入点
     * @return 方法执行结果
     * @throws Throwable 方法执行异常
     */
    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 检查是否需要排除日志记录
        if (isExcludeLog(joinPoint)) {
            return joinPoint.proceed();
        }

        // 获取 @Log 注解
        Log logAnnotation = getLogAnnotation(joinPoint);

        // 如果没有 @Log 注解，则不记录详细日志
        if (logAnnotation == null) {
            return joinPoint.proceed();
        }

        // 构建操作日志对象
        Map<String, Object> operLogData = buildOperLogData(joinPoint, logAnnotation);

        Object result = null;
        Throwable exception = null;

        try {
            // 执行目标方法
            result = joinPoint.proceed();

            // 记录返回结果（如果配置了保存结果）
            if (logAnnotation.saveResult()) {
                operLogData.put("jsonResult", JSON.toJSONString(result));
            }

            operLogData.put("status", 0); // 成功
            operLogData.put("statusName", "成功");

        } catch (Throwable e) {
            exception = e;
            operLogData.put("status", 1); // 失败
            operLogData.put("statusName", "失败");
            operLogData.put("errorMsg", e.getMessage());
            throw e;
        } finally {
            // 计算耗时
            long costTime = System.currentTimeMillis() - startTime;
            operLogData.put("costTime", costTime);
            operLogData.put("operTime", LocalDateTime.now());

            // 发布日志事件
            publishOperLogEvent(operLogData);

            log.debug("操作日志记录完成，耗时: {}ms", costTime);
        }

        return result;
    }

    /**
     * 判断是否需要排除日志记录
     *
     * @param joinPoint 织入点
     * @return true=排除, false=不排除
     */
    private boolean isExcludeLog(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 检查方法上的 @NoLog 注解
        if (method.isAnnotationPresent(NoLog.class)) {
            return true;
        }

        // 检查类上的 @NoLog 注解
        Class<?> targetClass = joinPoint.getTarget().getClass();
        if (targetClass.isAnnotationPresent(NoLog.class)) {
            return true;
        }

        return false;
    }

    /**
     * 获取方法上的 @Log 注解
     *
     * <p>优先获取方法上的注解，如果没有则获取类上的注解
     *
     * @param joinPoint 织入点
     * @return @Log 注解，如果没有则返回 null
     */
    private Log getLogAnnotation(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 优先获取方法上的注解
        if (method.isAnnotationPresent(Log.class)) {
            return method.getAnnotation(Log.class);
        }

        // 获取类上的注解
        Class<?> targetClass = joinPoint.getTarget().getClass();
        if (targetClass.isAnnotationPresent(Log.class)) {
            return targetClass.getAnnotation(Log.class);
        }

        return null;
    }

    /**
     * 构建操作日志数据
     *
     * @param joinPoint     织入点
     * @param logAnnotation @Log 注解
     * @return 操作日志数据 Map
     */
    private Map<String, Object> buildOperLogData(ProceedingJoinPoint joinPoint, Log logAnnotation) {
        Map<String, Object> data = new HashMap<>();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = joinPoint.getTarget().getClass();

        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();

            data.put("operUrl", request.getRequestURI());
            data.put("requestMethod", request.getMethod());
            data.put("operIp", getIpAddr(request));
            // operLocation 需要第三方 IP 库，此处留空
        }

        // 设置模块标题
        if (!logAnnotation.title().isEmpty()) {
            data.put("title", logAnnotation.title());
        } else {
            data.put("title", targetClass.getSimpleName());
        }

        // 设置业务类型
        data.put("businessType", logAnnotation.businessType().getCode());
        data.put("businessTypeName", logAnnotation.businessType().getDescription());

        // 设置操作类别
        data.put("operatorType", logAnnotation.operatorType());
        data.put("operatorTypeName", getOperatorTypeName(logAnnotation.operatorType()));

        // 设置方法信息
        data.put("method", String.format("%s.%s", targetClass.getName(), method.getName()));

        // 设置操作人员
        data.put("operName", SecurityUtil.getCurrentUsername());

        // 设置请求参数
        if (logAnnotation.saveParam()) {
            data.put("operParam", getRequestParams(joinPoint));
        }

        return data;
    }

    /**
     * 获取请求参数
     *
     * @param joinPoint 织入点
     * @return JSON 格式的请求参数
     */
    private String getRequestParams(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        if (args == null || args.length == 0) {
            return "";
        }

        String params = Arrays.stream(args)
                .filter(arg -> !isExcludeParamType(arg))
                .map(JSON::toJSONString)
                .collect(Collectors.joining(", "));

        return params;
    }

    /**
     * 判断参数类型是否需要排除
     *
     * @param arg 参数对象
     * @return true=排除, false=不排除
     */
    private boolean isExcludeParamType(Object arg) {
        if (arg == null) {
            return true;
        }

        for (Class<?> excludeType : EXCLUDE_PARAM_TYPES) {
            if (excludeType.isInstance(arg)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获取操作类别名称
     *
     * @param operatorType 操作类别代码
     * @return 操作类别名称
     */
    private String getOperatorTypeName(int operatorType) {
        return switch (operatorType) {
            case 1 -> "后台用户";
            case 2 -> "手机端用户";
            default -> "其它";
        };
    }

    /**
     * 获取客户端 IP 地址
     *
     * @param request HTTP 请求
     * @return IP 地址
     */
    private String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 处理多个 IP 的情况（X-Forwarded-For 可能包含多个 IP）
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    /**
     * 发布操作日志事件
     *
     * @param operLogData 操作日志数据
     */
    private void publishOperLogEvent(Map<String, Object> operLogData) {
        try {
            String jsonData = JSON.toJSONString(operLogData);
            OperLogEvent event = new OperLogEvent(this, jsonData, operLogData);
            eventPublisher.publishEvent(event);
        } catch (Exception e) {
            log.error("发布操作日志事件失败: {}", e.getMessage(), e);
        }
    }
}
