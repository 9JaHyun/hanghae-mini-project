package com.miniproject.common.logging;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class AOPLogging {

    @Pointcut("execution(* com.miniproject.*.controller..*.*(..))")
    private void cut() {
    }

    @Before("cut()")
    public void beforeParameterLog(JoinPoint joinPoint) {
        // 메서드 정보 받아오기
        Method method = getMethod(joinPoint);
        log.info("GET HTTP REQUEST: {}", method.getName());

        // 파라미터 받아오기
        Object[] args = joinPoint.getArgs();
        if (args.length <= 0) {
            log.info("no parameter");
        }
        for (Object arg : args) {
            log.info("=========> parameter type: {}", arg.getClass().getSimpleName());
            log.info("=========> parameter value: {}", arg);
        }
    }

    @AfterReturning(value = "cut()", returning = "returnObj")
    public void afterReturnLog(JoinPoint joinPoint, Object returnObj) {
        // 메서드 정보 받아오기

        Method method = getMethod(joinPoint);
        log.info("Create Return Object: {}", method.getName());

        log.info("=========> return type: {}", returnObj.getClass().getSimpleName());
        log.info("=========> return value: {}", returnObj);
        log.info("SEND HTTP RESPONSE: {}", method.getName());
    }

    // JoinPoint로 메서드 정보 가져오기
    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }
}
