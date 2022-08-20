package link.projectjg.apiserver.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Slf4j
@Aspect
@Component
public class LogErrorAop {

    @Pointcut("execution(* link.projectjg.apiserver.exception.advice..*.*(..))")
    private void error(){}

    @Around("error()")
    public void addTraceIdErrorLog(ProceedingJoinPoint joinPoint) throws Throwable {
        MDC.put("traceId", UUID.randomUUID().toString().substring(0,13));
        joinPoint.proceed();
        MDC.clear();
    }

}
