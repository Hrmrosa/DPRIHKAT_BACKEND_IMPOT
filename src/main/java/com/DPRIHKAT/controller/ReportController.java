package com.DPRIHKAT.controller;

import com.DPRIHKAT.dto.report.ReportDTO;
import com.DPRIHKAT.entity.enums.Role;
import com.DPRIHKAT.service.ReportService;
import com.DPRIHKAT.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/{reportType}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'AGENT_RECOUVREMENT', 'AGENT_CERTIFICAT')")
    public ResponseEntity<?> generateReport(
            @PathVariable String reportType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam Map<String, String> filters,
            Authentication authentication) {
        
        // Déterminer le rôle de l'utilisateur
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElse("CONTRIBUABLE");
        
        try {
            ReportDTO report = reportService.generateReport(
                    Role.valueOf(role),
                    reportType.toUpperCase(),
                    startDate,
                    endDate,
                    filters);
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "report", report,
                    "startDate", startDate,
                    "endDate", endDate
            )));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtil.createErrorResponse(
                    "REPORT_ERROR", 
                    "Erreur lors de la génération du rapport", 
                    e.getMessage()));
        }
    }
}
