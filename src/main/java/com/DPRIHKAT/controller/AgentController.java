package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.Agent;
import com.DPRIHKAT.service.AgentService;
import com.DPRIHKAT.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/agents")
public class AgentController {

    @Autowired
    private AgentService agentService;

/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
    /**
     * @return Une liste de tous les agents enregistrés dans la base de données
     */
/* <<<<<<<<<<  f4c66416-88e8-45fe-9bb7-5330a12ee615  >>>>>>>>>>> */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN')")
    public ResponseEntity<?> getAllAgents() {
        try {
            List<Agent> agents = agentService.findAll();
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("agents", agents)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("AGENTS_FETCH_ERROR", "Erreur lors de la récupération des agents", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN', 'CHEF_DE_BUREAU', 'CHEF_DE_DIVISION')")
    public ResponseEntity<?> getAgentById(@PathVariable UUID id) {
        try {
            Agent agent = agentService.findById(id);
            if (agent == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("AGENT_NOT_FOUND", "Agent non trouvé", "Aucun agent trouvé avec l'ID fourni"));
            }
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("agent", agent)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("AGENT_FETCH_ERROR", "Erreur lors de la récupération de l'agent", e.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN')")
    public ResponseEntity<?> createAgent(@RequestBody Agent agent) {
        try {
            Agent createdAgent = agentService.save(agent);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("agent", createdAgent)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("AGENT_CREATE_ERROR", "Erreur lors de la création de l'agent", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN')")
    public ResponseEntity<?> updateAgent(@PathVariable UUID id, @RequestBody Agent agent) {
        try {
            Agent updatedAgent = agentService.update(id, agent);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("agent", updatedAgent)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("AGENT_UPDATE_ERROR", "Erreur lors de la mise à jour de l'agent", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'INFORMATICIEN')")
    public ResponseEntity<?> deleteAgent(@PathVariable UUID id) {
        try {
            agentService.deleteById(id);
            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("message", "Agent supprimé avec succès")));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("AGENT_DELETE_ERROR", "Erreur lors de la suppression de l'agent", e.getMessage()));
        }
    }
}
