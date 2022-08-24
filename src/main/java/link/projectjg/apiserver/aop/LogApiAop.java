package link.projectjg.apiserver.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.UUID;


@Slf4j
@Aspect
@Component
public class LogApiAop {

    @Pointcut("execution(* link.projectjg.apiserver.controller..*.*(..))")
    private void api(){}

    @AfterReturning(value = "api()", returning = "returnObj")
    public void afterReturnLog(JoinPoint joinPoint, Object returnObj) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        MDC.put("traceId", UUID.randomUUID().toString().substring(0,13));
        Method method = getMethod(joinPoint);
        log.info("======= method name = {} =======", method.getName());
        log.info("URI: ({}) {}", request.getMethod(), request.getRequestURI());

        Object[] args = joinPoint.getArgs();
        if (args.length <= 0) log.info("no parameter");
        for (Object arg : args) {
            if (arg != null) {
                log.info("parameter type = {}", arg.getClass().getSimpleName());
                log.info("parameter value = {}", arg);
            }
        }
        if (returnObj != null) {
            log.info("return type = {}", returnObj.getClass());
            log.info("return value = {}", returnObj);
        }

        MDC.clear();
    }

    // JoinPoint로 메서드 정보 가져오기
    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }
}
