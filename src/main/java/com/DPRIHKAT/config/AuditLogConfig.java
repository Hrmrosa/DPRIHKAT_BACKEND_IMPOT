package com.DPRIHKAT.config;

import com.DPRIHKAT.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
public class AuditLogConfig {

    @Autowired
    private AuditLogRepository auditLogRepository;

    // Purge les logs de plus de 90 jours
    @Scheduled(cron = "0 0 1 * * ?") // Tous les jours à 1h du matin
    public void purgeOldLogs() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(90);
        auditLogRepository.deleteByTimestampBefore(cutoffDate);
    }
}
