package com.DPRIHKAT.aspect;

import com.DPRIHKAT.service.AuditLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditLoggingAspect {

    @Autowired
    private AuditLogService auditLogService;

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getMapping() {}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMapping() {}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putMapping() {}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void deleteMapping() {}

    @AfterReturning("getMapping()")
    public void logAfterGet(JoinPoint joinPoint) {
        String entity = extractEntityName(joinPoint);
        auditLogService.logRead(entity, null);
    }

    @AfterReturning("postMapping()")
    public void logAfterPost(JoinPoint joinPoint) {
        String entity = extractEntityName(joinPoint);
        auditLogService.logCreate(entity, null);
    }

    @AfterReturning("putMapping()")
    public void logAfterPut(JoinPoint joinPoint) {
        String entity = extractEntityName(joinPoint);
        auditLogService.logUpdate(entity, null);
    }

    @AfterReturning("deleteMapping()")
    public void logAfterDelete(JoinPoint joinPoint) {
        String entity = extractEntityName(joinPoint);
        auditLogService.logDelete(entity, null);
    }

    private String extractEntityName(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        // Simplification - en pratique il faudrait une logique plus sophistiquée
        if (methodName.contains("Taxation")) return "TAXATION";
        if (methodName.contains("Paiement")) return "PAIEMENT";
        if (methodName.contains("Contribuable")) return "CONTRIBUABLE";
        return "UNKNOWN";
    }
}
