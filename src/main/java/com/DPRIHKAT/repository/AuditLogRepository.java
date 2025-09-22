package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, String> {

    default Page<AuditLog> search(
            String username, 
            String action, 
            String entityType,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable) {
        
        if (username == null) username = "";
        if (action == null) action = "";
        if (entityType == null) entityType = "";
        
        // Gestion des dates limites pour éviter les valeurs hors plage
        LocalDateTime safeStartDate = startDate != null ? startDate : LocalDateTime.of(1970, 1, 1, 0, 0);
        LocalDateTime safeEndDate = endDate != null ? endDate : LocalDateTime.now().plusYears(10);
        
        return findByUsernameContainingAndActionContainingAndEntityTypeContainingAndTimestampBetween(
            username, action, entityType, 
            safeStartDate,
            safeEndDate,
            pageable);
    }
    
    Page<AuditLog> findByUsernameContainingAndActionContainingAndEntityTypeContainingAndTimestampBetween(
        String username, String action, String entityType,
        LocalDateTime startDate, LocalDateTime endDate,
        Pageable pageable);

    @Modifying
    @Query("DELETE FROM AuditLog l WHERE l.timestamp < :cutoffDate")
    void deleteByTimestampBefore(@Param("cutoffDate") LocalDateTime cutoffDate);
}
