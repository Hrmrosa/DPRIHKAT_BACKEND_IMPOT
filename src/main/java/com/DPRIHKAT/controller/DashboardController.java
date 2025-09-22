package com.DPRIHKAT.controller;

import com.DPRIHKAT.dto.dashboard.DashboardDTO;
import com.DPRIHKAT.entity.enums.Role;
import com.DPRIHKAT.service.DashboardService;
import com.DPRIHKAT.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'AGENT_RECOUVREMENT', 'AGENT_CERTIFICAT', 'CONTRIBUABLE')")
    public ResponseEntity<?> getDashboardData(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Authentication authentication) {
        
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("CONTRIBUABLE")
                .replace("ROLE_", "");
        
        LocalDate dateDebut = startDate != null ? startDate : LocalDate.now();
        LocalDate dateFin = endDate != null ? endDate : LocalDate.now();
        
        try {
            DashboardDTO dashboard = dashboardService.generateDashboardData(
                    Role.valueOf(role), 
                    dateDebut, 
                    dateFin);
            
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "dashboard", dashboard,
                    "dateDebut", dateDebut,
                    "dateFin", dateFin
            )));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtil.createErrorResponse(
                    "DASHBOARD_ERROR", 
                    "Erreur lors de la génération du dashboard", 
                    e.getMessage()));
        }
    }
}
