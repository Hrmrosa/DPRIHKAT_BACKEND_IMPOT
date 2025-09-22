package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.AuditLog;
import com.DPRIHKAT.repository.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    public void logAction(String action, String entityType, String entityId, String details) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "SYSTEM";
        
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ipAddress = request.getRemoteAddr();
        
        AuditLog log = new AuditLog();
        log.setUsername(username);
        log.setAction(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setDetails(details);
        log.setTimestamp(LocalDateTime.now());
        log.setIpAddress(ipAddress);
        
        auditLogRepository.save(log);
    }

    // Méthodes pratiques pour les actions courantes
    public void logLogin() {
        logAction("LOGIN", "USER", null, "User logged in");
    }

    public void logLogout() {
        logAction("LOGOUT", "USER", null, "User logged out");
    }

    public void logCreate(String entityType, String entityId) {
        logAction("CREATE", entityType, entityId, "Entity created");
    }

    public void logRead(String entityType, String entityId) {
        logAction("READ", entityType, entityId, "Entity accessed");
    }

    public void logUpdate(String entityType, String entityId) {
        logAction("UPDATE", entityType, entityId, "Entity updated");
    }

    public void logDelete(String entityType, String entityId) {
        logAction("DELETE", entityType, entityId, "Entity deleted");
    }
}
