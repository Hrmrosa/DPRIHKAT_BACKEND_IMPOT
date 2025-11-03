package com.DPRIHKAT.service;

import com.DPRIHKAT.dto.*;
import com.DPRIHKAT.entity.*;
import com.DPRIHKAT.entity.enums.PeriodeRapport;
import com.DPRIHKAT.entity.enums.TypeRapport;
import com.DPRIHKAT.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service pour la génération de rapports analytiques
 * Gère les rapports sur les taxations, paiements, relances, collectes et recouvrements
 * 
 * @author amateur
 */
@Service
@Transactional(readOnly = true)
public class RapportAnalytiqueService {
    
    private final TaxationRepository taxationRepository;
    private final PaiementRepository paiementRepository;
    private final RelanceRepository relanceRepository;
    private final DocumentRecouvrementRepository documentRecouvrementRepository;
    private final DeclarationRepository declarationRepository;
    private final AgentRepository agentRepository;
    
    public RapportAnalytiqueService(
            TaxationRepository taxationRepository,
            PaiementRepository paiementRepository,
            RelanceRepository relanceRepository,
            DocumentRecouvrementRepository documentRecouvrementRepository,
            DeclarationRepository declarationRepository,
            AgentRepository agentRepository) {
        this.taxationRepository = taxationRepository;
        this.paiementRepository = paiementRepository;
        this.relanceRepository = relanceRepository;
        this.documentRecouvrementRepository = documentRecouvrementRepository;
        this.declarationRepository = declarationRepository;
        this.agentRepository = agentRepository;
    }
    
    /**
     * Génère un rapport complet selon les critères spécifiés
     */
    public RapportResponseDTO genererRapport(RapportRequestDTO request) {
        // Calculer les dates de début et fin selon la période
        Date[] dates = calculerDates(request.getPeriode(), request.getDateDebut(), request.getDateFin());
        Date dateDebut = dates[0];
        Date dateFin = dates[1];
        
        RapportResponseDTO response = new RapportResponseDTO();
        response.setTypeRapport(request.getTypeRapport());
        response.setPeriode(request.getPeriode());
        response.setDateDebut(dateDebut);
        response.setDateFin(dateFin);
        
        // Récupérer les informations de l'agent si spécifié
        if (request.getAgentId() != null) {
            Agent agent = agentRepository.findById(request.getAgentId()).orElse(null);
            if (agent != null) {
                response.setAgentNom(agent.getNom());
                response.setAgentMatricule(agent.getMatricule());
            }
        }
        
        // Générer les données selon le type de rapport
        switch (request.getTypeRapport()) {
            case TAXATION:
                genererRapportTaxation(response, dateDebut, dateFin, request.getAgentId());
                break;
            case PAIEMENT:
                genererRapportPaiement(response, dateDebut, dateFin, request.getAgentId());
                break;
            case RELANCE:
                genererRapportRelance(response, dateDebut, dateFin, request.getAgentId());
                break;
            case COLLECTE:
                genererRapportCollecte(response, dateDebut, dateFin, request.getAgentId());
                break;
            case RECOUVREMENT:
                genererRapportRecouvrement(response, dateDebut, dateFin, request.getAgentId());
                break;
            case GLOBAL:
                genererRapportGlobal(response, dateDebut, dateFin, request.getAgentId());
                break;
        }
        
        // Générer les données pour les graphiques
        genererDonneesGraphiques(response, request.getTypeRapport());
        
        return response;
    }
    
    /**
     * Génère un rapport sur les taxations
     */
    private void genererRapportTaxation(RapportResponseDTO response, Date dateDebut, Date dateFin, UUID agentId) {
        List<Taxation> taxations;
        
        if (agentId != null) {
            taxations = taxationRepository.findByDateTaxationBetweenAndActifTrue(dateDebut, dateFin)
                    .stream()
                    .filter(t -> t.getAgent() != null && t.getAgent().getId().equals(agentId))
                    .collect(Collectors.toList());
        } else {
            taxations = taxationRepository.findByDateTaxationBetweenAndActifTrue(dateDebut, dateFin);
        }
        
        // Convertir en DTOs
        List<RapportTaxationDTO> taxationDTOs = taxations.stream()
                .map(this::convertirTaxationEnDTO)
                .collect(Collectors.toList());
        
        response.setTaxations(taxationDTOs);
        
        // Calculer les statistiques
        RapportResponseDTO.StatistiquesGlobalesDTO stats = new RapportResponseDTO.StatistiquesGlobalesDTO();
        stats.setNombreTaxations((long) taxations.size());
        stats.setMontantTotalTaxations(taxations.stream()
                .mapToDouble(t -> t.getMontant() != null ? t.getMontant() : 0.0)
                .sum());
        
        // Répartition par type d'impôt
        Map<String, Long> repartitionParType = taxations.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getTypeImpot() != null ? t.getTypeImpot().name() : "NON_SPECIFIE",
                        Collectors.counting()
                ));
        stats.setRepartitionParType(repartitionParType);
        
        Map<String, Double> repartitionMontants = taxations.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getTypeImpot() != null ? t.getTypeImpot().name() : "NON_SPECIFIE",
                        Collectors.summingDouble(t -> t.getMontant() != null ? t.getMontant() : 0.0)
                ));
        stats.setRepartitionMontantsParType(repartitionMontants);
        
        response.setStatistiquesGlobales(stats);
    }
    
    /**
     * Génère un rapport sur les paiements
     */
    private void genererRapportPaiement(RapportResponseDTO response, Date dateDebut, Date dateFin, UUID agentId) {
        List<Paiement> paiements = paiementRepository.findByDateBetween(dateDebut, dateFin);
        
        // Filtrer par agent si nécessaire (via la taxation associée)
        if (agentId != null) {
            paiements = paiements.stream()
                    .filter(p -> p.getTaxation() != null && 
                                 p.getTaxation().getAgent() != null && 
                                 p.getTaxation().getAgent().getId().equals(agentId))
                    .collect(Collectors.toList());
        }
        
        // Convertir en DTOs
        List<RapportPaiementDTO> paiementDTOs = paiements.stream()
                .map(this::convertirPaiementEnDTO)
                .collect(Collectors.toList());
        
        response.setPaiements(paiementDTOs);
        
        // Calculer les statistiques
        RapportResponseDTO.StatistiquesGlobalesDTO stats = response.getStatistiquesGlobales();
        if (stats == null) {
            stats = new RapportResponseDTO.StatistiquesGlobalesDTO();
        }
        
        stats.setNombrePaiements((long) paiements.size());
        stats.setMontantTotalPaiements(paiements.stream()
                .mapToDouble(p -> p.getMontant() != null ? p.getMontant() : 0.0)
                .sum());
        
        response.setStatistiquesGlobales(stats);
    }
    
    /**
     * Génère un rapport sur les relances
     */
    private void genererRapportRelance(RapportResponseDTO response, Date dateDebut, Date dateFin, UUID agentId) {
        List<Relance> relances = relanceRepository.findAll().stream()
                .filter(r -> r.getDateEnvoi() != null && 
                            !r.getDateEnvoi().before(dateDebut) && 
                            !r.getDateEnvoi().after(dateFin))
                .collect(Collectors.toList());
        
        // Convertir en DTOs
        List<RapportRelanceDTO> relanceDTOs = relances.stream()
                .map(this::convertirRelanceEnDTO)
                .collect(Collectors.toList());
        
        response.setRelances(relanceDTOs);
        
        // Calculer les statistiques
        RapportResponseDTO.StatistiquesGlobalesDTO stats = response.getStatistiquesGlobales();
        if (stats == null) {
            stats = new RapportResponseDTO.StatistiquesGlobalesDTO();
        }
        
        stats.setNombreRelances((long) relances.size());
        
        response.setStatistiquesGlobales(stats);
    }
    
    /**
     * Génère un rapport sur les actes de recouvrement
     */
    private void genererRapportRecouvrement(RapportResponseDTO response, Date dateDebut, Date dateFin, UUID agentId) {
        List<DocumentRecouvrement> documents = documentRecouvrementRepository.findByDateGenerationBetween(dateDebut, dateFin);
        
        // Filtrer par agent si nécessaire
        if (agentId != null) {
            documents = documents.stream()
                    .filter(d -> (d.getAgentGenerateur() != null && d.getAgentGenerateur().getId().equals(agentId)) ||
                                 (d.getAgentNotificateur() != null && d.getAgentNotificateur().getId().equals(agentId)))
                    .collect(Collectors.toList());
        }
        
        // Convertir en DTOs
        List<RapportRecouvrementDTO> recouvrementDTOs = documents.stream()
                .map(this::convertirRecouvrementEnDTO)
                .collect(Collectors.toList());
        
        response.setRecouvrements(recouvrementDTOs);
        
        // Calculer les statistiques
        RapportResponseDTO.StatistiquesGlobalesDTO stats = response.getStatistiquesGlobales();
        if (stats == null) {
            stats = new RapportResponseDTO.StatistiquesGlobalesDTO();
        }
        
        stats.setNombreActesRecouvrement((long) documents.size());
        stats.setMontantTotalRecouvrement(documents.stream()
                .mapToDouble(d -> d.getMontantTotal() != null ? d.getMontantTotal() : 0.0)
                .sum());
        
        response.setStatistiquesGlobales(stats);
    }
    
    /**
     * Génère un rapport sur la collecte de données
     */
    private void genererRapportCollecte(RapportResponseDTO response, Date dateDebut, Date dateFin, UUID agentId) {
        List<RapportCollecteDTO> collectes = new ArrayList<>();
        
        // Collecter les déclarations
        List<Declaration> declarations = declarationRepository.findAll().stream()
                .filter(d -> d.getDateDeclaration() != null && 
                            !d.getDateDeclaration().before(dateDebut) && 
                            !d.getDateDeclaration().after(dateFin))
                .collect(Collectors.toList());
        
        if (agentId != null) {
            declarations = declarations.stream()
                    .filter(d -> d.getAgentValidateur() != null && d.getAgentValidateur().getId().equals(agentId))
                    .collect(Collectors.toList());
        }
        
        for (Declaration declaration : declarations) {
            RapportCollecteDTO dto = new RapportCollecteDTO();
            dto.setId(declaration.getId());
            dto.setDateCollecte(declaration.getDateDeclaration());
            dto.setTypeCollecte("DECLARATION");
            dto.setMontant(declaration.getMontant());
            dto.setStatut(declaration.getStatut() != null ? declaration.getStatut().name() : null);
            dto.setTypeImpot(declaration.getTypeImpot() != null ? declaration.getTypeImpot().name() : null);
            
            if (declaration.getAgentValidateur() != null) {
                dto.setNomAgent(declaration.getAgentValidateur().getNom());
                dto.setMatriculeAgent(declaration.getAgentValidateur().getMatricule());
            }
            
            if (declaration.getContribuable() != null) {
                dto.setNomContribuable(declaration.getContribuable().getNom());
                dto.setNumeroContribuable(declaration.getContribuable().getNumeroIdentificationContribuable());
            }
            
            collectes.add(dto);
        }
        
        response.setCollectes(collectes);
        
        // Calculer les statistiques
        RapportResponseDTO.StatistiquesGlobalesDTO stats = response.getStatistiquesGlobales();
        if (stats == null) {
            stats = new RapportResponseDTO.StatistiquesGlobalesDTO();
        }
        
        stats.setNombreCollectes((long) collectes.size());
        
        response.setStatistiquesGlobales(stats);
    }
    
    /**
     * Génère un rapport global incluant tous les types
     */
    private void genererRapportGlobal(RapportResponseDTO response, Date dateDebut, Date dateFin, UUID agentId) {
        genererRapportTaxation(response, dateDebut, dateFin, agentId);
        genererRapportPaiement(response, dateDebut, dateFin, agentId);
        genererRapportRelance(response, dateDebut, dateFin, agentId);
        genererRapportRecouvrement(response, dateDebut, dateFin, agentId);
        genererRapportCollecte(response, dateDebut, dateFin, agentId);
    }
    
    /**
     * Calcule les dates de début et fin selon la période spécifiée
     */
    private Date[] calculerDates(PeriodeRapport periode, Date dateDebut, Date dateFin) {
        if (periode == PeriodeRapport.PERSONNALISEE) {
            return new Date[]{dateDebut, dateFin};
        }
        
        LocalDate now = LocalDate.now();
        LocalDate debut;
        LocalDate fin = now;
        
        switch (periode) {
            case JOUR:
                debut = now;
                break;
            case SEMAINE:
                debut = now.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
                fin = now.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
                break;
            case MOIS:
                debut = now.with(TemporalAdjusters.firstDayOfMonth());
                fin = now.with(TemporalAdjusters.lastDayOfMonth());
                break;
            case TRIMESTRE:
                int mois = now.getMonthValue();
                int debutTrimestre = ((mois - 1) / 3) * 3 + 1;
                debut = now.withMonth(debutTrimestre).with(TemporalAdjusters.firstDayOfMonth());
                fin = debut.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());
                break;
            case SEMESTRE:
                mois = now.getMonthValue();
                int debutSemestre = mois <= 6 ? 1 : 7;
                debut = now.withMonth(debutSemestre).with(TemporalAdjusters.firstDayOfMonth());
                fin = debut.plusMonths(5).with(TemporalAdjusters.lastDayOfMonth());
                break;
            case ANNEE:
                debut = now.with(TemporalAdjusters.firstDayOfYear());
                fin = now.with(TemporalAdjusters.lastDayOfYear());
                break;
            default:
                debut = now.minusDays(30);
        }
        
        return new Date[]{
            Date.from(debut.atStartOfDay(ZoneId.systemDefault()).toInstant()),
            Date.from(fin.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant())
        };
    }
    
    // Méthodes de conversion
    
    private RapportTaxationDTO convertirTaxationEnDTO(Taxation taxation) {
        RapportTaxationDTO dto = new RapportTaxationDTO();
        dto.setId(taxation.getId());
        dto.setNumeroTaxation(taxation.getNumeroTaxation());
        dto.setDateTaxation(taxation.getDateTaxation());
        dto.setMontant(taxation.getMontant());
        dto.setDevise(taxation.getDevise() != null ? taxation.getDevise().name() : null);
        dto.setStatut(taxation.getStatut());
        dto.setTypeImpot(taxation.getTypeImpot());
        dto.setExercice(taxation.getExercice());
        dto.setExoneration(taxation.isExoneration());
        dto.setMotifExoneration(taxation.getMotifExoneration());
        
        if (taxation.getAgent() != null) {
            dto.setNomAgent(taxation.getAgent().getNom());
            dto.setMatriculeAgent(taxation.getAgent().getMatricule());
        }
        
        Contribuable contribuable = taxation.getContribuable();
        if (contribuable == null && taxation.getContribuableDirect() != null) {
            contribuable = taxation.getContribuableDirect();
        }
        
        if (contribuable != null) {
            dto.setNomContribuable(contribuable.getNom());
            dto.setNumeroContribuable(contribuable.getNumeroIdentificationContribuable());
        }
        
        return dto;
    }
    
    private RapportPaiementDTO convertirPaiementEnDTO(Paiement paiement) {
        RapportPaiementDTO dto = new RapportPaiementDTO();
        dto.setId(paiement.getId());
        dto.setDatePaiement(paiement.getDate());
        dto.setMontant(paiement.getMontant());
        dto.setStatut(paiement.getStatut());
        dto.setMode(paiement.getMode());
        dto.setBordereauBancaire(paiement.getBordereauBancaire());
        dto.setNomBanque(paiement.getNomBanque());
        dto.setNumeroCompte(paiement.getNumeroCompte());
        
        if (paiement.getContribuable() != null) {
            dto.setNomContribuable(paiement.getContribuable().getNom());
            dto.setNumeroContribuable(paiement.getContribuable().getNumeroIdentificationContribuable());
        }
        
        if (paiement.getTaxation() != null) {
            dto.setNumeroTaxation(paiement.getTaxation().getNumeroTaxation());
        }
        
        return dto;
    }
    
    private RapportRelanceDTO convertirRelanceEnDTO(Relance relance) {
        RapportRelanceDTO dto = new RapportRelanceDTO();
        dto.setId(relance.getId());
        dto.setDateEnvoi(relance.getDateEnvoi());
        dto.setType(relance.getType());
        dto.setStatut(relance.getStatut());
        dto.setContenu(relance.getContenu());
        
        if (relance.getDossierRecouvrement() != null) {
            dto.setDossierId(relance.getDossierRecouvrement().getId());
            
            if (relance.getDossierRecouvrement().getContribuable() != null) {
                dto.setNomContribuable(relance.getDossierRecouvrement().getContribuable().getNom());
                dto.setNumeroContribuable(relance.getDossierRecouvrement().getContribuable().getNumeroIdentificationContribuable());
            }
        }
        
        return dto;
    }
    
    private RapportRecouvrementDTO convertirRecouvrementEnDTO(DocumentRecouvrement document) {
        RapportRecouvrementDTO dto = new RapportRecouvrementDTO();
        dto.setId(document.getId());
        dto.setType(document.getType());
        dto.setStatut(document.getStatut());
        dto.setDateGeneration(document.getDateGeneration());
        dto.setDateNotification(document.getDateNotification());
        dto.setReference(document.getReference());
        dto.setMontantPrincipal(document.getMontantPrincipal());
        dto.setMontantPenalites(document.getMontantPenalites());
        dto.setMontantTotal(document.getMontantTotal());
        
        if (document.getContribuable() != null) {
            dto.setNomContribuable(document.getContribuable().getNom());
            dto.setNumeroContribuable(document.getContribuable().getNumeroIdentificationContribuable());
        }
        
        if (document.getAgentGenerateur() != null) {
            dto.setNomAgentGenerateur(document.getAgentGenerateur().getNom());
            dto.setMatriculeAgentGenerateur(document.getAgentGenerateur().getMatricule());
        }
        
        if (document.getAgentNotificateur() != null) {
            dto.setNomAgentNotificateur(document.getAgentNotificateur().getNom());
            dto.setMatriculeAgentNotificateur(document.getAgentNotificateur().getMatricule());
        }
        
        return dto;
    }
    
    /**
     * Génère les données pour les graphiques
     */
    private void genererDonneesGraphiques(RapportResponseDTO response, TypeRapport typeRapport) {
        DonneesGraphiqueDTO donneesGraphiques = new DonneesGraphiqueDTO();
        
        // Générer les données circulaires (pie chart) pour la répartition
        if (response.getStatistiquesGlobales() != null && 
            response.getStatistiquesGlobales().getRepartitionParType() != null) {
            
            List<DonneesGraphiqueDTO.DonneeCirculaireDTO> donneesCirculaires = new ArrayList<>();
            Map<String, Long> repartition = response.getStatistiquesGlobales().getRepartitionParType();
            Map<String, Double> repartitionMontants = response.getStatistiquesGlobales().getRepartitionMontantsParType();
            
            long total = repartition.values().stream().mapToLong(Long::longValue).sum();
            String[] couleurs = {"#3B82F6", "#10B981", "#F59E0B", "#EF4444", "#8B5CF6", "#EC4899"};
            int index = 0;
            
            for (Map.Entry<String, Long> entry : repartition.entrySet()) {
                double pourcentage = total > 0 ? (entry.getValue() * 100.0 / total) : 0;
                Double montant = repartitionMontants != null ? repartitionMontants.get(entry.getKey()) : null;
                
                donneesCirculaires.add(new DonneesGraphiqueDTO.DonneeCirculaireDTO(
                    entry.getKey(),
                    montant != null ? montant : entry.getValue().doubleValue(),
                    pourcentage,
                    couleurs[index % couleurs.length]
                ));
                index++;
            }
            
            donneesGraphiques.setDonneesCirculaires(donneesCirculaires);
        }
        
        // Générer les séries temporelles selon le type de rapport
        genererSeriesTemporelles(donneesGraphiques, response, typeRapport);
        
        // Générer le top items (top contributeurs, agents, etc.)
        genererTopItems(donneesGraphiques, response, typeRapport);
        
        // Générer l'évolution temporelle
        genererEvolutionTemporelle(donneesGraphiques, response, typeRapport);
        
        response.setDonneesGraphiques(donneesGraphiques);
    }
    
    /**
     * Génère les séries temporelles pour graphiques en ligne/barres
     */
    private void genererSeriesTemporelles(DonneesGraphiqueDTO donneesGraphiques, 
                                          RapportResponseDTO response, 
                                          TypeRapport typeRapport) {
        List<DonneesGraphiqueDTO.SerieTemporelleDTO> series = new ArrayList<>();
        
        if (typeRapport == TypeRapport.TAXATION && response.getTaxations() != null) {
            // Grouper par type d'impôt
            Map<String, List<RapportTaxationDTO>> parType = response.getTaxations().stream()
                .collect(Collectors.groupingBy(t -> t.getTypeImpot() != null ? t.getTypeImpot().name() : "AUTRE"));
            
            String[] couleurs = {"#3B82F6", "#10B981", "#F59E0B", "#EF4444"};
            int index = 0;
            
            for (Map.Entry<String, List<RapportTaxationDTO>> entry : parType.entrySet()) {
                List<String> labels = entry.getValue().stream()
                    .map(t -> t.getNumeroTaxation())
                    .limit(10)
                    .collect(Collectors.toList());
                
                List<Double> valeurs = entry.getValue().stream()
                    .map(t -> t.getMontant() != null ? t.getMontant() : 0.0)
                    .limit(10)
                    .collect(Collectors.toList());
                
                series.add(new DonneesGraphiqueDTO.SerieTemporelleDTO(
                    entry.getKey(),
                    labels,
                    valeurs,
                    couleurs[index % couleurs.length]
                ));
                index++;
            }
        } else if (typeRapport == TypeRapport.PAIEMENT && response.getPaiements() != null) {
            // Grouper par mode de paiement
            Map<String, List<RapportPaiementDTO>> parMode = response.getPaiements().stream()
                .collect(Collectors.groupingBy(p -> p.getMode() != null ? p.getMode().name() : "AUTRE"));
            
            String[] couleurs = {"#3B82F6", "#10B981", "#F59E0B"};
            int index = 0;
            
            for (Map.Entry<String, List<RapportPaiementDTO>> entry : parMode.entrySet()) {
                List<String> labels = entry.getValue().stream()
                    .map(p -> p.getDatePaiement() != null ? p.getDatePaiement().toString() : "")
                    .limit(10)
                    .collect(Collectors.toList());
                
                List<Double> valeurs = entry.getValue().stream()
                    .map(p -> p.getMontant() != null ? p.getMontant() : 0.0)
                    .limit(10)
                    .collect(Collectors.toList());
                
                series.add(new DonneesGraphiqueDTO.SerieTemporelleDTO(
                    entry.getKey(),
                    labels,
                    valeurs,
                    couleurs[index % couleurs.length]
                ));
                index++;
            }
        }
        
        donneesGraphiques.setSeriesTemporelles(series);
    }
    
    /**
     * Génère le top items (contributeurs, agents, etc.)
     */
    private void genererTopItems(DonneesGraphiqueDTO donneesGraphiques, 
                                  RapportResponseDTO response, 
                                  TypeRapport typeRapport) {
        List<DonneesGraphiqueDTO.TopItemDTO> topItems = new ArrayList<>();
        
        if (typeRapport == TypeRapport.TAXATION && response.getTaxations() != null) {
            // Top agents par montant taxé
            Map<String, Double> parAgent = response.getTaxations().stream()
                .filter(t -> t.getNomAgent() != null)
                .collect(Collectors.groupingBy(
                    RapportTaxationDTO::getNomAgent,
                    Collectors.summingDouble(t -> t.getMontant() != null ? t.getMontant() : 0.0)
                ));
            
            double total = parAgent.values().stream().mapToDouble(Double::doubleValue).sum();
            int rang = 1;
            
            for (Map.Entry<String, Double> entry : parAgent.entrySet()
                    .stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .limit(10)
                    .collect(Collectors.toList())) {
                
                long quantite = response.getTaxations().stream()
                    .filter(t -> entry.getKey().equals(t.getNomAgent()))
                    .count();
                
                double pourcentage = total > 0 ? (entry.getValue() * 100.0 / total) : 0;
                
                topItems.add(new DonneesGraphiqueDTO.TopItemDTO(
                    entry.getKey(),
                    entry.getValue(),
                    quantite,
                    pourcentage,
                    rang++
                ));
            }
        } else if (typeRapport == TypeRapport.PAIEMENT && response.getPaiements() != null) {
            // Top contributeurs par montant payé
            Map<String, Double> parContribuable = response.getPaiements().stream()
                .filter(p -> p.getNomContribuable() != null)
                .collect(Collectors.groupingBy(
                    RapportPaiementDTO::getNomContribuable,
                    Collectors.summingDouble(p -> p.getMontant() != null ? p.getMontant() : 0.0)
                ));
            
            double total = parContribuable.values().stream().mapToDouble(Double::doubleValue).sum();
            int rang = 1;
            
            for (Map.Entry<String, Double> entry : parContribuable.entrySet()
                    .stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .limit(10)
                    .collect(Collectors.toList())) {
                
                long quantite = response.getPaiements().stream()
                    .filter(p -> entry.getKey().equals(p.getNomContribuable()))
                    .count();
                
                double pourcentage = total > 0 ? (entry.getValue() * 100.0 / total) : 0;
                
                topItems.add(new DonneesGraphiqueDTO.TopItemDTO(
                    entry.getKey(),
                    entry.getValue(),
                    quantite,
                    pourcentage,
                    rang++
                ));
            }
        }
        
        donneesGraphiques.setTopItems(topItems);
    }
    
    /**
     * Génère l'évolution temporelle
     */
    private void genererEvolutionTemporelle(DonneesGraphiqueDTO donneesGraphiques, 
                                            RapportResponseDTO response, 
                                            TypeRapport typeRapport) {
        DonneesGraphiqueDTO.EvolutionTemporelleDTO evolution = new DonneesGraphiqueDTO.EvolutionTemporelleDTO();
        
        if (typeRapport == TypeRapport.TAXATION && response.getTaxations() != null) {
            // Grouper par date
            Map<String, List<RapportTaxationDTO>> parDate = response.getTaxations().stream()
                .filter(t -> t.getDateTaxation() != null)
                .collect(Collectors.groupingBy(t -> t.getDateTaxation().toString().substring(0, 10)));
            
            List<String> periodes = new ArrayList<>(parDate.keySet());
            Collections.sort(periodes);
            
            List<Double> montants = periodes.stream()
                .map(date -> parDate.get(date).stream()
                    .mapToDouble(t -> t.getMontant() != null ? t.getMontant() : 0.0)
                    .sum())
                .collect(Collectors.toList());
            
            List<Long> quantites = periodes.stream()
                .map(date -> (long) parDate.get(date).size())
                .collect(Collectors.toList());
            
            double moyenne = montants.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            
            // Calculer la tendance (simple : comparaison premier/dernier)
            double tendance = 0.0;
            if (montants.size() >= 2) {
                double premier = montants.get(0);
                double dernier = montants.get(montants.size() - 1);
                if (premier > 0) {
                    tendance = ((dernier - premier) / premier) * 100.0;
                }
            }
            
            evolution.setPeriodes(periodes);
            evolution.setMontants(montants);
            evolution.setQuantites(quantites);
            evolution.setMoyenne(moyenne);
            evolution.setTendance(tendance);
        } else if (typeRapport == TypeRapport.PAIEMENT && response.getPaiements() != null) {
            // Grouper par date
            Map<String, List<RapportPaiementDTO>> parDate = response.getPaiements().stream()
                .filter(p -> p.getDatePaiement() != null)
                .collect(Collectors.groupingBy(p -> p.getDatePaiement().toString().substring(0, 10)));
            
            List<String> periodes = new ArrayList<>(parDate.keySet());
            Collections.sort(periodes);
            
            List<Double> montants = periodes.stream()
                .map(date -> parDate.get(date).stream()
                    .mapToDouble(p -> p.getMontant() != null ? p.getMontant() : 0.0)
                    .sum())
                .collect(Collectors.toList());
            
            List<Long> quantites = periodes.stream()
                .map(date -> (long) parDate.get(date).size())
                .collect(Collectors.toList());
            
            double moyenne = montants.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            
            double tendance = 0.0;
            if (montants.size() >= 2) {
                double premier = montants.get(0);
                double dernier = montants.get(montants.size() - 1);
                if (premier > 0) {
                    tendance = ((dernier - premier) / premier) * 100.0;
                }
            }
            
            evolution.setPeriodes(periodes);
            evolution.setMontants(montants);
            evolution.setQuantites(quantites);
            evolution.setMoyenne(moyenne);
            evolution.setTendance(tendance);
        }
        
        donneesGraphiques.setEvolutionTemporelle(evolution);
    }
}
