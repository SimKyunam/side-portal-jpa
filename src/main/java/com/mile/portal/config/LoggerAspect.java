package com.mile.portal.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@Aspect
@Component
public class LoggerAspect {

    /**
     * 포인트 컷
     */
    @Pointcut("execution(* com.mile.portal.rest.client.controller.*.*(..) ) && !@annotation(com.mile.portal.util.annotation.NoAspect)")
    public void userControllerAdvice() {
    }

    @Pointcut("execution(* com.mile.portal.rest.client.service.*.*(..) ) && !@annotation(com.mile.portal.util.annotation.NoAspect)")
    public void userServiceAdvice() {
    }

    @Pointcut("execution(* com.mile.portal.rest.mng.controller.*.*(..) ) && !@annotation(com.mile.portal.util.annotation.NoAspect)")
    public void mngControllerAdvice() {
    }

    @Pointcut("execution(* com.mile.portal.rest.mng.service.*.*(..) ) && !@annotation(com.mile.portal.util.annotation.NoAspect)")
    public void mngServiceAdvice() {
    }

    @Pointcut("execution(* com.mile.portal.rest.common.controller.*.*(..) ) && !@annotation(com.mile.portal.util.annotation.NoAspect)")
    public void commControllerAdvice() {
    }

    @Pointcut("execution(* com.mile.portal.rest.common.service.*.*(..) ) && !@annotation(com.mile.portal.util.annotation.NoAspect)")
    public void commServiceAdvice() {
    }

    /**
     * 컨트롤러
     */
    @Around("userControllerAdvice() || mngControllerAdvice() || commControllerAdvice()")
    public Object controllerLogger(ProceedingJoinPoint point) throws Throwable {
        Class clazz = point.getTarget().getClass();
        String[] classNameStr = point.getSignature().getDeclaringTypeName().split("\\.");
        String className = classNameStr[classNameStr.length - 1];
        String methodName = point.getSignature().getName();

        String[] requestUrlInfo = getRequestUrl(point, clazz).split(" ");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        log.info("----------------------------------------------------------------");
        log.info(">>> HTTP method : " + requestUrlInfo[0]);
        log.info(">>> HTTP URL : " + requestUrlInfo[1]);
        log.info(">>> class : {} (method : {})", className, methodName);
        log.info(">>> params : " + params(point));

        Object result = point.proceed(point.getArgs());
        stopWatch.stop();
        log.info(">>> time : {} seconds", Math.round(stopWatch.getTotalTimeSeconds() * 1000) / 1000.0);
        log.info(">>> class : {}(method : {}) API success...", className, methodName);
        log.info("----------------------------------------------------------------");

        return result;
    }

    /**
     * 서비스
     */
    @Around("userServiceAdvice() || mngServiceAdvice() || commServiceAdvice()")
    public Object userServiceLogger(ProceedingJoinPoint point) throws Throwable {
        String[] classNameStr = point.getSignature().getDeclaringTypeName().split("\\.");
        String className = classNameStr[classNameStr.length - 1];
        String methodName = point.getSignature().getName();

        log.info("----------------------------------------------------------------");
        log.info(">>> class : {} (method : {}) start...", className, methodName);
        Object result = point.proceed(point.getArgs());
        log.info(">>> class : {} (method : {}) end...", className, methodName);
        log.info("----------------------------------------------------------------");

        return result;
    }

    private String getRequestUrl(JoinPoint joinPoint, Class clazz) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RequestMapping requestMapping = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
        String baseUrl = requestMapping.value()[0];

        String url = Stream.of(GetMapping.class, PutMapping.class, PostMapping.class,
                PatchMapping.class, DeleteMapping.class, RequestMapping.class)
                .filter(mappingClass -> method.isAnnotationPresent(mappingClass))
                .map(mappingClass -> getUrl(method, mappingClass, baseUrl))
                .findFirst().orElse(null);
        return url;
    }

    /* httpMETHOD + requestURI 를 반환 */
    private String getUrl(Method method, Class<? extends Annotation> annotationClass, String baseUrl) {
        Annotation annotation = method.getAnnotation(annotationClass);
        String[] value;
        String httpMethod = null;
        try {
            value = (String[]) annotationClass.getMethod("value").invoke(annotation);
            httpMethod = (annotationClass.getSimpleName().replace("Mapping", "")).toUpperCase();
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            return null;
        }
        return String.format("%s %s%s", httpMethod, baseUrl, value.length > 0 ? value[0] : "");
    }

    //param 반환
    private Map<String, Object> params(JoinPoint joinPoint) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        String[] parameterNames = codeSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        Map<String, Object> params = new HashMap<>();

        for (int i = 0; i < parameterNames.length; i++) {
            params.put(parameterNames[i], args[i]);
        }
        return params;
    }
}
