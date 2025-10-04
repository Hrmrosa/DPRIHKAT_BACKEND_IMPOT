package com.DPRIHKAT.controller;

import com.DPRIHKAT.service.RecouvrementService;
import com.DPRIHKAT.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/dossiers-recouvrement")
public class RecouvrementController {

    @Autowired
    private RecouvrementService recouvrementService;

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT_RECOUVREMENT', 'CONTROLLEUR')")
    public ResponseEntity<?> searchDossiers(
            @RequestParam(required = false) UUID contribuableId,
            @RequestParam(required = false) String statut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateFin) {
        return recouvrementService.searchDossiers(contribuableId, statut, dateDebut, dateFin);
    }

    @GetMapping("/{id}/export")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT_RECOUVREMENT')")
    public ResponseEntity<?> exportDossier(@PathVariable UUID id) {
        return recouvrementService.exportDossier(id);
    }
}
