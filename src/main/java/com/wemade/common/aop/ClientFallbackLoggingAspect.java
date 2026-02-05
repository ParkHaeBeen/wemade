package com.wemade.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ClientFallbackLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(ClientFallbackLoggingAspect.class);

    @Around("@annotation(com.wemade.common.aop.ClientFallbackLogging)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();

        String ip = "-";

        for (Object arg : args) {
            if (arg instanceof String ipArg) ip = ipArg;
        }

        try {
            return pjp.proceed();
        } catch (Exception e) {
            log.warn("event=ipinfo_fallback_failed ip={} message={}", ip, e.getMessage(), e);
            throw e;
        }
    }
}
