package com.wemade.common.aop;

import com.wemade.common.exception.ParserException;
import com.wemade.domain.Analysis;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("@annotation(com.wemade.common.aop.AnalysisLogging)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();

        MultipartFile file = null;
        Analysis analysis = null;

        for (Object arg : args) {
            if (arg instanceof MultipartFile f) file = f;
            if (arg instanceof Analysis a) analysis = a;
        }

        String analysisId = (analysis == null) ? "-" : analysis.getId();
        String filename = (file == null || file.getOriginalFilename() == null) ? "-" : file.getOriginalFilename();

        long startNs = System.nanoTime();

        log.info("event=analysis_start analysisId={} filename={}", analysisId, filename);

        try {
            Object result = pjp.proceed();
            log.info("event=analysis_completed analysisId={} durationMs={}", analysisId, durationMs(startNs));
            return result;
        } catch (ParserException e) {
            log.error("event=analysis_failed analysisId={} durationMs={} errorCode={} message={}",
                    analysisId, durationMs(startNs), e.getErrorCode().name(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("event=analysis_failed analysisId={} durationMs={} message={}",
                    analysisId, durationMs(startNs), e.getMessage());

            throw e;
        }
    }

    private long durationMs(long startNs) {
        return (System.nanoTime() - startNs) / 1_000_000;
    }
}
