package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.*;
import com.DPRIHKAT.entity.enums.Role;
import com.DPRIHKAT.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ControleFiscalService {

    @Autowired
    private DeclarationRepository declarationRepository;

    @Autowired
    private PaiementRepository paiementRepository;

    @Autowired
    private PenaliteRepository penaliteRepository;

    @Autowired
    private ApurementRepository apurementRepository;

    @Autowired
    private PlaqueRepository plaqueRepository;

    @Autowired
    private CertificatRepository certificatRepository;

    @Autowired
    private VignetteRepository vignetteRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private BureauRepository bureauRepository;

    @Autowired
    private DivisionRepository divisionRepository;

    @Autowired
    private ControleFiscalRepository controleFiscalRepository;

    /**
     * Find declarations with anomalies (unpaid, late, etc.)
     * Filtered by bureau for non-admin users
     */
    public List<Declaration> findAnomalies(UUID userId) {
        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        // For admin, directeur, and informaticien, show all unpaid declarations
        List<Declaration> unpaid = declarationRepository.findByPaiementIsNull();

        if (utilisateur.getRole() == Role.ADMIN ||
            utilisateur.getRole() == Role.DIRECTEUR ||
            utilisateur.getRole() == Role.INFORMATICIEN) {
            return unpaid;
        }

        Agent agent = utilisateur.getAgent();
        if (agent == null) {
            throw new RuntimeException("Agent non associé à l'utilisateur");
        }
        Bureau bureau = agent.getBureau();
        if (bureau == null) {
            throw new RuntimeException("Bureau non associé à l'agent");
        }

        // Filter by bureau using the declaration's agentValidateur bureau as proxy
        return unpaid.stream()
                .filter(d -> d.getAgentValidateur() != null && d.getAgentValidateur().getBureau() != null)
                .filter(d -> bureau.getId().equals(d.getAgentValidateur().getBureau().getId()))
                .collect(Collectors.toList());
    }

    /**
     * Generate fiscal report for a period
     * Filtered by bureau for non-admin users
     */
    public Map<String, Object> generateFiscalReport(UUID userId, Date startDate, Date endDate) {
        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        Map<String, Object> report = new HashMap<>();

        // Base datasets
        List<Declaration> declarations = declarationRepository.findByDateBetween(startDate, endDate);
        List<Paiement> paiements = paiementRepository.findByDateBetween(startDate, endDate);
        List<Penalite> penalites = penaliteRepository.findByDateApplicationBetween(startDate, endDate);
        List<Apurement> apurements = apurementRepository.findAll();

        if (utilisateur.getRole() != Role.ADMIN &&
            utilisateur.getRole() != Role.DIRECTEUR &&
            utilisateur.getRole() != Role.INFORMATICIEN) {
            Agent agent = utilisateur.getAgent();
            if (agent == null) throw new RuntimeException("Agent non associé à l'utilisateur");
            Bureau bureau = agent.getBureau();
            if (bureau == null) throw new RuntimeException("Bureau non associé à l'agent");

            declarations = declarations.stream()
                    .filter(d -> d.getAgentValidateur() != null && d.getAgentValidateur().getBureau() != null)
                    .filter(d -> bureau.getId().equals(d.getAgentValidateur().getBureau().getId()))
                    .collect(Collectors.toList());

            // Filter paiements by declaration bureau using declaration link
            final Set<UUID> declarationIdsInBureau = declarations.stream().map(Declaration::getId).collect(Collectors.toSet());
            final Set<UUID> paymentIdsInBureau = declarations.stream()
                    .map(Declaration::getPaiement)
                    .filter(Objects::nonNull)
                    .map(Paiement::getId)
                    .collect(Collectors.toSet());
            paiements = paiements.stream()
                    .filter(p -> paymentIdsInBureau.contains(p.getId()))
                    .collect(Collectors.toList());

            // Filter penalites by bureau using declaration link
            penalites = penalites.stream()
                    .filter(pe -> pe.getDeclaration() != null && declarationIdsInBureau.contains(pe.getDeclaration().getId()))
                    .collect(Collectors.toList());

            // Filter apurements by date and bureau using declaration link
            apurements = apurements.stream()
                    .filter(a -> a.getDateDemande() != null && !a.getDateDemande().before(startDate) && !a.getDateDemande().after(endDate))
                    .filter(a -> a.getDeclaration() != null && declarationIdsInBureau.contains(a.getDeclaration().getId()))
                    .collect(Collectors.toList());
        } else {
            // For admin-level roles, filter apurements only by date range
            apurements = apurements.stream()
                    .filter(a -> a.getDateDemande() != null && !a.getDateDemande().before(startDate) && !a.getDateDemande().after(endDate))
                    .collect(Collectors.toList());
        }

        double totalPaiements = paiements.stream().mapToDouble(p -> p.getMontant() != null ? p.getMontant() : 0.0).sum();
        double totalPenalites = penalites.stream().mapToDouble(pe -> pe.getMontant() != null ? pe.getMontant() : 0.0).sum();

        report.put("totalDeclarations", declarations.size());
        report.put("totalPaiements", totalPaiements);
        report.put("totalPenalites", totalPenalites);
        report.put("totalApurements", apurements.size());

        return report;
    }

    /**
     * Get top contributors by payment amount
     * Filtered by bureau for non-admin users
     */
    public List<Map<String, Object>> getTopContributors(UUID userId, int limit) {
        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Build from declarations with payments
        List<Declaration> allDeclWithPayment = declarationRepository.findAll().stream()
                .filter(d -> d.getPaiement() != null)
                .collect(Collectors.toList());

        if (utilisateur.getRole() != Role.ADMIN &&
            utilisateur.getRole() != Role.DIRECTEUR &&
            utilisateur.getRole() != Role.INFORMATICIEN) {
            Agent agent = utilisateur.getAgent();
            if (agent == null) throw new RuntimeException("Agent non associé à l'utilisateur");
            Bureau bureau = agent.getBureau();
            if (bureau == null) throw new RuntimeException("Bureau non associé à l'agent");
            allDeclWithPayment = allDeclWithPayment.stream()
                    .filter(d -> d.getAgentValidateur() != null && d.getAgentValidateur().getBureau() != null)
                    .filter(d -> bureau.getId().equals(d.getAgentValidateur().getBureau().getId()))
                    .collect(Collectors.toList());
        }

        Map<UUID, Double> totalsByContribuable = new HashMap<>();
        Map<UUID, String> namesByContribuable = new HashMap<>();

        for (Declaration d : allDeclWithPayment) {
            if (d.getPropriete() == null || d.getPropriete().getProprietaire() == null) continue;
            UUID cid = d.getPropriete().getProprietaire().getId();
            String nom = d.getPropriete().getProprietaire().getNom();
            double montant = d.getPaiement().getMontant() != null ? d.getPaiement().getMontant() : 0.0;
            totalsByContribuable.merge(cid, montant, Double::sum);
            namesByContribuable.putIfAbsent(cid, nom);
        }

        return totalsByContribuable.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .limit(Math.max(0, limit))
                .map(e -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", e.getKey());
                    m.put("nom", namesByContribuable.get(e.getKey()));
                    m.put("totalMontant", e.getValue());
                    return m;
                })
                .collect(Collectors.toList());
    }

    /**
     * Get delinquent contributors (with unpaid declarations)
     * Filtered by bureau for non-admin users
     */
    public List<Map<String, Object>> getDelinquentContributors(UUID userId) {
        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        List<Declaration> unpaid = declarationRepository.findByPaiementIsNull();

        if (utilisateur.getRole() != Role.ADMIN &&
            utilisateur.getRole() != Role.DIRECTEUR &&
            utilisateur.getRole() != Role.INFORMATICIEN) {
            Agent agent = utilisateur.getAgent();
            if (agent == null) throw new RuntimeException("Agent non associé à l'utilisateur");
            Bureau bureau = agent.getBureau();
            if (bureau == null) throw new RuntimeException("Bureau non associé à l'agent");
            unpaid = unpaid.stream()
                    .filter(d -> d.getAgentValidateur() != null && d.getAgentValidateur().getBureau() != null)
                    .filter(d -> bureau.getId().equals(d.getAgentValidateur().getBureau().getId()))
                    .collect(Collectors.toList());
        }

        Map<UUID, String> delinquentMap = new HashMap<>();
        for (Declaration d : unpaid) {
            if (d.getPropriete() == null || d.getPropriete().getProprietaire() == null) continue;
            Contribuable c = d.getPropriete().getProprietaire();
            delinquentMap.putIfAbsent(c.getId(), c.getNom());
        }

        return delinquentMap.entrySet().stream()
                .map(e -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", e.getKey());
                    m.put("nom", e.getValue());
                    return m;
                })
                .collect(Collectors.toList());
    }

    /**
     * Get dashboard statistics
     * Filtered by bureau for non-admin users
     */
    public Map<String, Object> getDashboardStats(UUID userId) {
        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        Map<String, Object> stats = new HashMap<>();

        List<Declaration> declarations = declarationRepository.findAll();
        List<Paiement> paiements = paiementRepository.findAll();

        if (utilisateur.getRole() != Role.ADMIN &&
            utilisateur.getRole() != Role.DIRECTEUR &&
            utilisateur.getRole() != Role.INFORMATICIEN) {
            Agent agent = utilisateur.getAgent();
            if (agent == null) throw new RuntimeException("Agent non associé à l'utilisateur");
            Bureau bureau = agent.getBureau();
            if (bureau == null) throw new RuntimeException("Bureau non associé à l'agent");

            declarations = declarations.stream()
                    .filter(d -> d.getAgentValidateur() != null && d.getAgentValidateur().getBureau() != null)
                    .filter(d -> bureau.getId().equals(d.getAgentValidateur().getBureau().getId()))
                    .collect(Collectors.toList());

            final Set<UUID> paymentIdsInBureau = declarations.stream()
                    .map(Declaration::getPaiement)
                    .filter(Objects::nonNull)
                    .map(Paiement::getId)
                    .collect(Collectors.toSet());
            paiements = paiements.stream()
                    .filter(p -> paymentIdsInBureau.contains(p.getId()))
                    .collect(Collectors.toList());
        }

        long totalDeclarations = declarations.size();
        double totalPaiements = paiements.stream().mapToDouble(p -> p.getMontant() != null ? p.getMontant() : 0.0).sum();
        long totalContribuables = declarations.stream()
                .map(d -> d.getPropriete() != null ? d.getPropriete().getProprietaire() : null)
                .filter(Objects::nonNull)
                .map(Contribuable::getId)
                .distinct()
                .count();

        long pendingDeclarations = declarations.stream()
                .filter(d -> d.getStatut() == com.DPRIHKAT.entity.enums.StatutDeclaration.EN_ATTENTE)
                .count();
        long validatedDeclarations = declarations.stream()
                .filter(d -> d.getStatut() == com.DPRIHKAT.entity.enums.StatutDeclaration.VALIDEE)
                .count();

        stats.put("totalDeclarations", totalDeclarations);
        stats.put("totalPaiements", totalPaiements);
        stats.put("totalContribuables", totalContribuables);
        stats.put("pendingDeclarations", pendingDeclarations);
        stats.put("validatedDeclarations", validatedDeclarations);

        return stats;
    }

    public List<ControleFiscal> getControlesFiscauxByBureau(UUID bureauId) {
        return controleFiscalRepository.findByDeclarationContribuableBureauId(bureauId);
    }

    // Helper method to map ControleFiscal to ControleFiscalDTO
    private Map<String, Object> mapToDTO(ControleFiscal controle) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", controle.getId());
        dto.put("type", controle.getType());
        dto.put("statut", controle.getStatut());
        dto.put("dateCreation", controle.getDateCreation());
        dto.put("dateCloture", controle.getDateCloture());
        dto.put("observations", controle.getObservations());
        dto.put("justificatifs", controle.getJustificatifs());
        
        // Add declaration info
        if (controle.getDeclaration() != null) {
            Map<String, Object> declarationInfo = new HashMap<>();
            declarationInfo.put("id", controle.getDeclaration().getId());
            declarationInfo.put("type", controle.getDeclaration().getTypeImpot());
            declarationInfo.put("montant", controle.getDeclaration().getMontant());
            dto.put("declaration", declarationInfo);
        }
        
        // Add agent initiateur info
        if (controle.getAgentInitiateur() != null) {
            Map<String, Object> agentInfo = new HashMap<>();
            agentInfo.put("id", controle.getAgentInitiateur().getId());
            agentInfo.put("nom", controle.getAgentInitiateur().getNom());
            agentInfo.put("matricule", controle.getAgentInitiateur().getMatricule());
            dto.put("agentInitiateur", agentInfo);
        }
        
        // Add agent validateur info
        if (controle.getAgentValidateur() != null) {
            Map<String, Object> agentInfo = new HashMap<>();
            agentInfo.put("id", controle.getAgentValidateur().getId());
            agentInfo.put("nom", controle.getAgentValidateur().getNom());
            agentInfo.put("matricule", controle.getAgentValidateur().getMatricule());
            dto.put("agentValidateur", agentInfo);
        }
        
        return dto;
    }

    public List<ControleFiscal> getControlesFiscauxByAgent(UUID agentId) {
        return controleFiscalRepository.findByAgentInitiateurId(agentId);
    }

    public List<ControleFiscal> getControlesFiscauxByDeclaration(UUID declarationId) {
        return controleFiscalRepository.findByDeclarationId(declarationId);
    }

    public List<ControleFiscal> getControlesFiscauxByStatut(com.DPRIHKAT.entity.enums.StatutControle statut) {
        return controleFiscalRepository.findByStatut(statut);
    }

    public List<ControleFiscal> getControlesFiscauxByType(com.DPRIHKAT.entity.enums.TypeControle type) {
        return controleFiscalRepository.findByType(type);
    }

    public List<ControleFiscal> getControlesFiscauxByDateRange(Date startDate, Date endDate) {
        return controleFiscalRepository.findByDateCreationBetween(startDate, endDate);
    }

    // Get all controles fiscaux
    public List<ControleFiscal> findAll() {
        return controleFiscalRepository.findAll();
    }

    // Get controle fiscal by ID
    public ControleFiscal findById(UUID id) {
        return controleFiscalRepository.findById(id).orElse(null);
    }

    // Save controle fiscal
    public ControleFiscal save(ControleFiscal controleFiscal) {
        return controleFiscalRepository.save(controleFiscal);
    }

    // Update controle fiscal
    public ControleFiscal update(UUID id, ControleFiscal controleFiscal) {
        controleFiscal.setId(id);
        return controleFiscalRepository.save(controleFiscal);
    }

    // Delete controle fiscal
    public void deleteById(UUID id) {
        controleFiscalRepository.deleteById(id);
    }

    // Get all controls for a specific agent
    public List<ControleFiscal> getControlesForAgent(UUID agentId) {
        return controleFiscalRepository.findByAgentInitiateurId(agentId);
    }

    // Get all controls for a specific declaration
    public List<ControleFiscal> getControlesForDeclaration(UUID declarationId) {
        return controleFiscalRepository.findByDeclarationId(declarationId);
    }

    // Create a new control
    public ControleFiscal createControle(ControleFiscal controle, UUID agentId) {
        Utilisateur utilisateur = utilisateurRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        controle.setAgentInitiateur(utilisateur.getAgent()); // Utiliser getAgent() pour obtenir l'entité Agent
        controle.setDateCreation(new Date());
        controle.setStatut(com.DPRIHKAT.entity.enums.StatutControle.EN_COURS);
        
        return controleFiscalRepository.save(controle);
    }

    // Get agent initiateur for a control
    public Agent getAgentInitiateur(ControleFiscal controle) {
        return controle.getAgentInitiateur();
    }

    // Get declaration for a control
    public Declaration getDeclaration(ControleFiscal controle) {
        return controle.getDeclaration();
    }

    // Get all controls with filtering options
    public List<ControleFiscal> getAllControlesWithFilters(
            UUID agentId,
            UUID bureauId,
            Date dateDebut,
            Date dateFin,
            String statut) {
        
        // Implementation would depend on your specific filtering needs
        // This is a simplified example
        return controleFiscalRepository.findAll();
    }

    // Get statistics for controls
    public Map<String, Object> getControleStatistics(UUID bureauId, UUID divisionId) {
        Map<String, Object> stats = new HashMap<>();
        
        if (bureauId != null) {
            stats.put("totalByBureau", controleFiscalRepository.countByDeclarationContribuableBureauId(bureauId));
        }
        
        if (divisionId != null) {
            stats.put("totalByDivision", controleFiscalRepository.countByDeclarationContribuableBureauDivisionId(divisionId));
        }
        
        stats.put("totalControls", controleFiscalRepository.count());
        stats.put("controlsByStatus", controleFiscalRepository.countByStatutGroupedByStatut());
        
        return stats;
    }

    // Get controls by status
    public List<ControleFiscal> getControlesByStatut(String statut) {
        // Convert string to enum
        try {
            com.DPRIHKAT.entity.enums.StatutControle statutEnum = com.DPRIHKAT.entity.enums.StatutControle.valueOf(statut);
            return controleFiscalRepository.findByStatut(statutEnum);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Statut invalide: " + statut);
        }
    }

    // Get controls by date range
    public List<ControleFiscal> getControlesByDateRange(Date startDate, Date endDate) {
        return controleFiscalRepository.findByDateCreationBetween(startDate, endDate);
    }

    // Get controls by bureau
    public List<ControleFiscal> getControlesByBureau(UUID bureauId) {
        return controleFiscalRepository.findByDeclarationContribuableBureauId(bureauId);
    }

    // Get controls by division
    public List<ControleFiscal> getControlesByDivision(UUID divisionId) {
        return controleFiscalRepository.findByDeclarationContribuableBureauDivisionId(divisionId);
    }
}
