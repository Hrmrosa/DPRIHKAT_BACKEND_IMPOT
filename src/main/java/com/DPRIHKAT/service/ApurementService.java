package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Apurement;
import com.DPRIHKAT.entity.Declaration;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.enums.StatutApurement;
import com.DPRIHKAT.entity.enums.TypeApurement;
import com.DPRIHKAT.repository.ApurementRepository;
import com.DPRIHKAT.repository.DeclarationRepository;
import com.DPRIHKAT.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ApurementService {

    @Autowired
    private ApurementRepository apurementRepository;

    @Autowired
    private DeclarationRepository declarationRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    /**
     * Create an apurement for a declaration (overload used by controller)
     */
    public Apurement createApurement(UUID declarationId, UUID userId, TypeApurement type) {
        return createApurement(declarationId, userId, type, null);
    }

    /**
     * Create an apurement for a declaration with optional motif
     * L'apurement est directement validé lors de l'enregistrement manuel
     */
    public Apurement createApurement(UUID declarationId, UUID agentId, TypeApurement type, String motif) {
        // Get declaration and agent
        Declaration declaration = declarationRepository.findById(declarationId)
                .orElseThrow(() -> new RuntimeException("Déclaration non trouvée"));

        Utilisateur utilisateur = utilisateurRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Create apurement
        Apurement apurement = new Apurement();
        apurement.setType(type);
        apurement.setMotif(motif);
        apurement.setDateDemande(new Date());
        apurement.setDeclaration(declaration);
        apurement.setAgent(utilisateur.getAgent());
        
        // Définir directement comme validé au lieu de provisoire
        apurement.setStatut(StatutApurement.ACCEPTEE);
        apurement.setDateValidation(new Date());
        apurement.setAgentValidateur(utilisateur.getAgent());
        
        return apurementRepository.save(apurement);
    }

    /**
     * Get apurement by ID
     */
    public Apurement findById(UUID id) {
        return apurementRepository.findById(id).orElse(null);
    }

    /**
     * Get all apurements (generic)
     */
    public List<Apurement> findAll() {
        return apurementRepository.findAll();
    }

    /**
     * Get all apurements (method name used by controller list endpoint)
     */
    public List<Apurement> getAllApurements() {
        return findAll();
    }

    /**
     * Get apurements by status
     */
    public List<Apurement> getApurementsByStatus(StatutApurement statut) {
        return apurementRepository.findByStatut(statut);
    }

    /**
     * Get apurements by type
     */
    public List<Apurement> getApurementsByType(TypeApurement type) {
        return apurementRepository.findByType(type);
    }

    /**
     * Get apurement by declaration id (used by controller)
     */
    public Apurement getApurementByDeclarationId(UUID declarationId) {
        List<Apurement> apurements = apurementRepository.findByDeclaration_Id(declarationId);
        return apurements.isEmpty() ? null : apurements.get(0);
    }

    /**
     * Get apurements by agent
     */
    public List<Apurement> getApurementsByAgent(UUID agentId) {
        return apurementRepository.findByAgent_Id(agentId);
    }

    /**
     * Validate an apurement
     */
    public Apurement validateApurement(UUID id, UUID userId) {
        Apurement apurement = apurementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Apurement non trouvé"));

        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        apurement.setStatut(StatutApurement.ACCEPTEE);
        apurement.setAgentValidateur(utilisateur.getAgent());
        apurement.setDateValidation(new Date());

        return apurementRepository.save(apurement);
    }

    /**
     * Reject an apurement
     */
    public Apurement reject(UUID id, UUID agentId, String motifRejet) {
        Apurement apurement = apurementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Apurement non trouvé"));

        Utilisateur utilisateur = utilisateurRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        apurement.setStatut(StatutApurement.REJETEE);
        apurement.setAgentValidateur(utilisateur.getAgent());
        apurement.setMotifRejet(motifRejet);

        return apurementRepository.save(apurement);
    }

    // Save apurement
    public Apurement save(Apurement apurement) {
        return apurementRepository.save(apurement);
    }

    // Update apurement
    public Apurement update(UUID id, Apurement apurement) {
        apurement.setId(id);
        return apurementRepository.save(apurement);
    }

    // Delete apurement
    public void deleteById(UUID id) {
        apurementRepository.deleteById(id);
    }
}
