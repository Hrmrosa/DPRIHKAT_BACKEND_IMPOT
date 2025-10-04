package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.*;
import com.DPRIHKAT.entity.enums.Role;
import com.DPRIHKAT.entity.enums.StatutControle;
import com.DPRIHKAT.entity.enums.StatutDeclaration;
import com.DPRIHKAT.entity.enums.TypeControle;
import com.DPRIHKAT.entity.enums.TypeContribuable;
import com.DPRIHKAT.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
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

    private static final Logger logger = LoggerFactory.getLogger(ControleFiscalService.class);

    public List<Map<String, Object>> findAnomalies(UUID userId) {
        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        List<Declaration> unpaid = declarationRepository.findByPaiementIsNull();

        if (utilisateur.getRole() == Role.ADMIN ||
            utilisateur.getRole() == Role.DIRECTEUR ||
            utilisateur.getRole() == Role.INFORMATICIEN) {
            return unpaid.stream()
                    .map(declaration -> {
                        Map<String, Object> anomalyData = new HashMap<>();
                        anomalyData.put("id", declaration.getId());
                        anomalyData.put("dateDeclaration", declaration.getDateDeclaration());
                        anomalyData.put("montant", declaration.getMontant());
                        anomalyData.put("impot", declaration.getImpot() != null ? Map.of(
                            "type", declaration.getImpot().getType(),
                            "taux", declaration.getImpot().getTaux()
                        ) : null);
                        anomalyData.put("penalites", declaration.getPenalites().stream()
                            .map(p -> Map.of("montant", p.getMontant(), "motif", p.getMotif()))
                            .collect(Collectors.toList()));

                        Contribuable contribuable = declaration.getContribuable();
                        anomalyData.put("contribuableDetails", Map.of(
                            "id", contribuable.getId(),
                            "nomComplet", contribuable.getNom() ,
                            "adresse", contribuable.getAdressePrincipale(),
                            "telephone", contribuable.getTelephonePrincipal(),
                            "email", contribuable.getEmail(),
                            "typeContribuable", contribuable.getTypeContribuable()
                        ));

                        if (declaration.getPropriete() != null) {
                            Propriete propriete = declaration.getPropriete();
                            anomalyData.put("proprieteDetails", Map.of(
                                "id", propriete.getId(),
                                "adresse", propriete.getAdresse(),
                                "superficie", propriete.getSuperficie(),
                                "valeurLocative", propriete.getValeurLocative(),
                                "typePropriete", propriete.getTypePropriete()
                            ));
                        }

                        return anomalyData;
                    })
                    .collect(Collectors.toList());
        }

        Agent agent = utilisateur.getAgent();
        if (agent == null) {
            throw new RuntimeException("Agent non associé à l'utilisateur");
        }
        Bureau bureau = agent.getBureau();
        if (bureau == null) {
            throw new RuntimeException("Bureau non associé à l'agent");
        }

        return unpaid.stream()
                .filter(d -> d.getAgentValidateur() != null && d.getAgentValidateur().getBureau() != null)
                .filter(d -> bureau.getId().equals(d.getAgentValidateur().getBureau().getId()))
                .map(declaration -> {
                    Map<String, Object> anomalyData = new HashMap<>();
                    anomalyData.put("id", declaration.getId());
                    anomalyData.put("dateDeclaration", declaration.getDateDeclaration());
                    anomalyData.put("montant", declaration.getMontant());
                    anomalyData.put("impot", declaration.getImpot() != null ? Map.of(
                        "type", declaration.getImpot().getType(),
                        "taux", declaration.getImpot().getTaux()
                    ) : null);
                    anomalyData.put("penalites", declaration.getPenalites().stream()
                        .map(p -> Map.of("montant", p.getMontant(), "motif", p.getMotif()))
                        .collect(Collectors.toList()));

                    Contribuable contribuable = declaration.getContribuable();
                    anomalyData.put("contribuableDetails", Map.of(
                        "id", contribuable.getId(),
                        "nomComplet", contribuable.getNom() ,
                        "adresse", contribuable.getAdressePrincipale(),
                        "telephone", contribuable.getTelephonePrincipal(),
                        "email", contribuable.getEmail(),
                        "typeContribuable", contribuable.getTypeContribuable()
                    ));

                    if (declaration.getPropriete() != null) {
                        Propriete propriete = declaration.getPropriete();
                        anomalyData.put("proprieteDetails", Map.of(
                            "id", propriete.getId(),
                            "adresse", propriete.getAdresse(),
                            "superficie", propriete.getSuperficie(),
                            "valeurLocative", propriete.getValeurLocative(),
                            "typePropriete", propriete.getTypePropriete()
                        ));
                    }

                    return anomalyData;
                })
                .collect(Collectors.toList());
    }

    public Map<String, Object> generateFiscalReport(UUID userId, Date startDate, Date endDate) {
        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        Map<String, Object> report = new HashMap<>();

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

            final Set<UUID> declarationIdsInBureau = declarations.stream().map(Declaration::getId).collect(Collectors.toSet());
            final Set<UUID> paymentIdsInBureau = declarations.stream()
                    .map(Declaration::getPaiement)
                    .filter(Objects::nonNull)
                    .map(Paiement::getId)
                    .collect(Collectors.toSet());
            paiements = paiements.stream()
                    .filter(p -> paymentIdsInBureau.contains(p.getId()))
                    .collect(Collectors.toList());

            penalites = penalites.stream()
                    .filter(pe -> pe.getDeclaration() != null && declarationIdsInBureau.contains(pe.getDeclaration().getId()))
                    .collect(Collectors.toList());

            apurements = apurements.stream()
                    .filter(a -> a.getDateDemande() != null && !a.getDateDemande().before(startDate) && !a.getDateDemande().after(endDate))
                    .filter(a -> a.getDeclaration() != null && declarationIdsInBureau.contains(a.getDeclaration().getId()))
                    .collect(Collectors.toList());
        } else {
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

    public List<Map<String, Object>> getTopContributors(UUID userId, int limit) {
        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

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
            if (agent == null) throw new RuntimeException("Agent non associé");
            Bureau bureau = agent.getBureau();
            if (bureau == null) throw new RuntimeException("Bureau non associé");
            
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
                .filter(d -> d.getStatut() == StatutDeclaration.EN_ATTENTE)
                .count();
        long validatedDeclarations = declarations.stream()
                .filter(d -> d.getStatut() == StatutDeclaration.VALIDEE)
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

    private Map<String, Object> mapToDTO(ControleFiscal controle) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", controle.getId());
        dto.put("type", controle.getType());
        dto.put("statut", controle.getStatut());
        dto.put("dateCreation", controle.getDateCreation());
        dto.put("dateCloture", controle.getDateCloture());
        dto.put("observations", controle.getObservations());
        dto.put("justificatifs", controle.getJustificatifs());
        
        if (controle.getDeclaration() != null) {
            Map<String, Object> declarationInfo = new HashMap<>();
            declarationInfo.put("id", controle.getDeclaration().getId());
            declarationInfo.put("type", controle.getDeclaration().getTypeImpot());
            declarationInfo.put("montant", controle.getDeclaration().getMontant());
            dto.put("declaration", declarationInfo);
        }
        
        if (controle.getAgentInitiateur() != null) {
            Map<String, Object> agentInfo = new HashMap<>();
            agentInfo.put("id", controle.getAgentInitiateur().getId());
            agentInfo.put("nom", controle.getAgentInitiateur().getNom());
            agentInfo.put("matricule", controle.getAgentInitiateur().getMatricule());
            dto.put("agentInitiateur", agentInfo);
        }
        
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

    public List<ControleFiscal> getControlesFiscauxByStatut(StatutControle statut) {
        return controleFiscalRepository.findByStatut(statut);
    }

    public List<ControleFiscal> getControlesFiscauxByType(TypeControle type) {
        return controleFiscalRepository.findByType(type);
    }

    public List<ControleFiscal> getControlesFiscauxByDateRange(Date startDate, Date endDate) {
        return controleFiscalRepository.findByDateCreationBetween(startDate, endDate);
    }

    public List<ControleFiscal> findAll() {
        return controleFiscalRepository.findAll();
    }

    public ControleFiscal findById(UUID id) {
        return controleFiscalRepository.findById(id).orElse(null);
    }

    public ControleFiscal save(ControleFiscal controleFiscal) {
        return controleFiscalRepository.save(controleFiscal);
    }

    public ControleFiscal update(UUID id, ControleFiscal controleFiscal) {
        controleFiscal.setId(id);
        return controleFiscalRepository.save(controleFiscal);
    }

    public void deleteById(UUID id) {
        controleFiscalRepository.deleteById(id);
    }

    public List<ControleFiscal> getControlesForAgent(UUID agentId) {
        return controleFiscalRepository.findByAgentInitiateurId(agentId);
    }

    public List<ControleFiscal> getControlesForDeclaration(UUID declarationId) {
        return controleFiscalRepository.findByDeclarationId(declarationId);
    }

    public ControleFiscal createControle(ControleFiscal controle, UUID agentId) {
        Utilisateur utilisateur = utilisateurRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        controle.setAgentInitiateur(utilisateur.getAgent()); 
        controle.setDateCreation(new Date());
        controle.setStatut(StatutControle.EN_COURS);
        
        return controleFiscalRepository.save(controle);
    }

    public Agent getAgentInitiateur(ControleFiscal controle) {
        return controle.getAgentInitiateur();
    }

    public Declaration getDeclaration(ControleFiscal controle) {
        return controle.getDeclaration();
    }

    public List<ControleFiscal> getAllControlesWithFilters(
            UUID agentId,
            UUID bureauId,
            Date dateDebut,
            Date dateFin,
            String statut) {
        
        return controleFiscalRepository.findAll();
    }

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

    public List<ControleFiscal> getControlesByStatut(String statut) {
        try {
            StatutControle statutEnum = StatutControle.valueOf(statut);
            return controleFiscalRepository.findByStatut(statutEnum);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Statut invalide: " + statut);
        }
    }

    public List<ControleFiscal> getControlesByDateRange(Date startDate, Date endDate) {
        return controleFiscalRepository.findByDateCreationBetween(startDate, endDate);
    }

    public List<ControleFiscal> getControlesByBureau(UUID bureauId) {
        return controleFiscalRepository.findByDeclarationContribuableBureauId(bureauId);
    }

    public List<ControleFiscal> getControlesByDivision(UUID divisionId) {
        return controleFiscalRepository.findByDeclarationContribuableBureauDivisionId(divisionId);
    }

    public List<Contribuable> findInsolvables(UUID userId) {
        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -3);
        Date threeMonthsAgo = cal.getTime();
        
        List<Declaration> unpaidDeclarations = declarationRepository.findByPaiementIsNullAndDateDeclarationBefore(threeMonthsAgo);
        
        if (utilisateur.getRole() != Role.ADMIN && 
            utilisateur.getRole() != Role.DIRECTEUR && 
            utilisateur.getRole() != Role.INFORMATICIEN) {
            
            Agent agent = utilisateur.getAgent();
            if (agent == null) throw new RuntimeException("Agent non associé");
            Bureau bureau = agent.getBureau();
            if (bureau == null) throw new RuntimeException("Bureau non associé");
            
            unpaidDeclarations = unpaidDeclarations.stream()
                .filter(d -> d.getAgentValidateur() != null && d.getAgentValidateur().getBureau() != null)
                .filter(d -> bureau.getId().equals(d.getAgentValidateur().getBureau().getId()))
                .collect(Collectors.toList());
        }
        
        Map<Contribuable, Long> contribuableCounts = unpaidDeclarations.stream()
            .filter(d -> d.getContribuable() != null)
            .collect(Collectors.groupingBy(Declaration::getContribuable, Collectors.counting()));
        
        return contribuableCounts.entrySet().stream()
            .filter(e -> e.getValue() >= 2)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    private Map<String, Object> buildResponse(Map<Contribuable, List<Declaration>> contribuableDeclarations, int page, int size) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", Map.of(
            "insolvables", contribuableDeclarations,
            "currentPage", page,
            "totalItems", contribuableDeclarations.size(),
            "totalPages", (int) Math.ceil((double) contribuableDeclarations.size() / size)
        ));
        response.put("error", null);
        response.put("meta", Map.of(
            "timestamp", Instant.now().toString(),
            "version", "1.0.0"
        ));

        logger.info("{} insolvables trouvés", contribuableDeclarations.size());
        return response;
    }

    public Map<String, Object> findInsolvablesPaginated(UUID userId, int page, int size) {
        try {
            logger.info("Début de la recherche des insolvables pour l'utilisateur: {}", userId);
            
            // Vérification des paramètres
            if (page < 0 || size <= 0) {
                throw new IllegalArgumentException("Paramètres de pagination invalides");
            }

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -3);
            
            logger.debug("Recherche des déclarations impayées avant: {}", cal.getTime());
            List<Declaration> unpaidDeclarations = declarationRepository.findByPaiementIsNullAndDateDeclarationBefore(cal.getTime());
            logger.debug("{} déclarations impayées trouvées", unpaidDeclarations.size());

            Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("Utilisateur non trouvé: {}", userId);
                    return new RuntimeException("Utilisateur non trouvé");
                });

            Map<Contribuable, List<Declaration>> contribuableDeclarations = unpaidDeclarations.stream()
                .filter(d -> d.getContribuable() != null)
                .collect(Collectors.groupingBy(Declaration::getContribuable));

            logger.info("{} contribuables avec déclarations impayées trouvés", contribuableDeclarations.size());
            return buildResponse(contribuableDeclarations, page, size);
            
        } catch (Exception e) {
            logger.error("Erreur critique lors de la recherche des insolvables", e);
            throw new RuntimeException("Erreur technique lors de la récupération des insolvables", e);
        }
    }

    private Double calculerTotalDette(Contribuable contribuable) {
        return declarationRepository.findByContribuableAndPaiementIsNull(contribuable)
            .stream()
            .mapToDouble(Declaration::getMontant)
            .sum();
    }

    private String getLastDeclarationDate(List<Declaration> declarations) {
        return declarations.stream()
            .map(Declaration::getDateDeclaration)
            .max(Date::compareTo)
            .map(Date::toString)
            .orElse(null);
    }

    private List<Map<String, Object>> getBiensDetails(List<Declaration> declarations) {
        return declarations.stream()
            .map(decl -> {
                Map<String, Object> bien = new HashMap<>();
                bien.put("id", decl.getPropriete() != null ? decl.getPropriete().getId() : decl.getVehicule().getId());
                bien.put("type", decl.getPropriete() != null ? "IMMEUBLE" : "VEHICULE");
                bien.put("adresse", decl.getPropriete() != null ? decl.getPropriete().getAdresse() : decl.getVehicule().getImmatriculation());
                bien.put("valeur", decl.getPropriete() != null ? decl.getPropriete().getValeurLocative() : decl.getVehicule().getValeur());
                
                bien.put("declarations", List.of(Map.of(
                    "id", decl.getId(),
                    "date", decl.getDateDeclaration(),
                    "montant", decl.getMontant(),
                    "impot", decl.getImpot() != null ? Map.of(
                        "type", decl.getImpot().getType(),
                        "taux", decl.getImpot().getTaux()
                    ) : null,
                    "penalites", decl.getPenalites().stream()
                        .map(p -> Map.of("montant", p.getMontant(), "motif", p.getMotif()))
                        .collect(Collectors.toList())
                )));
                
                return bien;
            })
            .collect(Collectors.toList());
    }
}
