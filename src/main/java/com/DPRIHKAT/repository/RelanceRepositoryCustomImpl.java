package com.DPRIHKAT.repository;

import com.DPRIHKAT.dto.RelanceDetailDTO;
import com.DPRIHKAT.entity.*;
import com.DPRIHKAT.entity.enums.TypeImpot;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

/**
 * Implémentation des méthodes personnalisées du repository RelanceRepository
 */
@Repository
public class RelanceRepositoryCustomImpl implements RelanceRepositoryCustom {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public Optional<RelanceDetailDTO> findRelanceDetailById(UUID id) {
        // Récupérer la relance
        String jpql = "SELECT r FROM Relance r WHERE r.id = :id";
        Relance relance = null;
        
        try {
            relance = (Relance) entityManager.createQuery(jpql)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return Optional.empty();
        }
        
        if (relance == null || relance.getDossierRecouvrement() == null) {
            return Optional.empty();
        }
        
        // Récupérer le dossier de recouvrement et le contribuable associé
        DossierRecouvrement dossier = relance.getDossierRecouvrement();
        Contribuable contribuable = dossier.getContribuable();
        
        if (contribuable == null) {
            return Optional.empty();
        }
        
        // Créer le DTO avec les informations de base
        RelanceDetailDTO dto = new RelanceDetailDTO();
        dto.setId(relance.getId());
        dto.setDateEnvoi(relance.getDateEnvoi());
        dto.setType(relance.getType());
        dto.setStatut(relance.getStatut());
        dto.setContenu(relance.getContenu());
        dto.setCodeQR(relance.getCodeQR());
        
        // Ajouter les informations du dossier de recouvrement
        dto.setDossierRecouvrementId(dossier.getId());
        dto.setTotalDu(dossier.getTotalDu());
        dto.setTotalRecouvre(dossier.getTotalRecouvre());
        dto.setDateOuverture(dossier.getDateOuverture());
        
        // Ajouter les informations du contribuable
        dto.setContribuableId(contribuable.getId());
        dto.setContribuableNom(contribuable.getNom());
        dto.setContribuableAdressePrincipale(contribuable.getAdressePrincipale());
        dto.setContribuableAdresseSecondaire(contribuable.getAdresseSecondaire());
        dto.setContribuableTelephonePrincipal(contribuable.getTelephonePrincipal());
        dto.setContribuableTelephoneSecondaire(contribuable.getTelephoneSecondaire());
        dto.setContribuableEmail(contribuable.getEmail());
        dto.setContribuableNationalite(contribuable.getNationalite());
        dto.setContribuableType(contribuable.getType());
        dto.setContribuableIdentifiant(contribuable.getNumeroIdentificationContribuable());
        
        // Récupérer les propriétés du contribuable
        String jpqlProprietes = "SELECT p FROM Propriete p WHERE p.proprietaire.id = :contribuableId AND p.actif = true";
        List<Propriete> proprietes = entityManager.createQuery(jpqlProprietes, Propriete.class)
                .setParameter("contribuableId", contribuable.getId())
                .getResultList();
        
        // Ajouter les informations des biens et leurs impôts
        List<RelanceDetailDTO.BienDTO> bienDTOs = new ArrayList<>();
        
        for (Propriete propriete : proprietes) {
            RelanceDetailDTO.BienDTO bienDTO = new RelanceDetailDTO.BienDTO();
            bienDTO.setId(propriete.getId());
            bienDTO.setType(propriete.getType());
            bienDTO.setLocalite(propriete.getLocalite());
            bienDTO.setRangLocalite(propriete.getRangLocalite());
            bienDTO.setSuperficie(propriete.getSuperficie());
            bienDTO.setAdresse(propriete.getAdresse());
            bienDTO.setMontantImpot(propriete.getMontantImpot());
            
            // Récupérer les impôts associés à cette propriété
            String jpqlProprieteImpots = "SELECT pi FROM ProprieteImpot pi WHERE pi.propriete.id = :proprieteId AND pi.actif = true";
            List<ProprieteImpot> proprieteImpots = entityManager.createQuery(jpqlProprieteImpots, ProprieteImpot.class)
                    .setParameter("proprieteId", propriete.getId())
                    .getResultList();
            
            List<RelanceDetailDTO.ImpotDTO> impotDTOs = new ArrayList<>();
            
            for (ProprieteImpot proprieteImpot : proprieteImpots) {
                // Récupérer les taxations associées à ce lien propriété-impôt
                String jpqlTaxations = "SELECT t FROM Taxation t WHERE t.proprieteImpot.id = :proprieteImpotId AND t.actif = true";
                List<Taxation> taxations = entityManager.createQuery(jpqlTaxations, Taxation.class)
                        .setParameter("proprieteImpotId", proprieteImpot.getId())
                        .getResultList();
                
                for (Taxation taxation : taxations) {
                    RelanceDetailDTO.ImpotDTO impotDTO = new RelanceDetailDTO.ImpotDTO();
                    impotDTO.setId(proprieteImpot.getNatureImpot().getId());
                    impotDTO.setLibelle(proprieteImpot.getNatureImpot().getNom());
                    impotDTO.setTypeImpot(taxation.getTypeImpot());
                    impotDTO.setMontant(taxation.getMontant());
                    impotDTO.setTauxImposition(proprieteImpot.getTauxImposition());
                    impotDTO.setExercice(taxation.getExercice());
                    impotDTO.setDateEcheance(taxation.getDateEcheance());
                    impotDTO.setTaxationId(taxation.getId());
                    
                    impotDTOs.add(impotDTO);
                }
            }
            
            bienDTO.setImpots(impotDTOs);
            bienDTOs.add(bienDTO);
        }
        
        dto.setBiens(bienDTOs);
        
        return Optional.of(dto);
    }
    
    @Override
    public List<RelanceDetailDTO> findAllRelanceDetails() {
        // Récupérer toutes les relances
        String jpql = "SELECT r FROM Relance r";
        List<Relance> relances = entityManager.createQuery(jpql, Relance.class).getResultList();
        
        List<RelanceDetailDTO> dtos = new ArrayList<>();
        
        for (Relance relance : relances) {
            findRelanceDetailById(relance.getId()).ifPresent(dtos::add);
        }
        
        return dtos;
    }
    
    @Override
    public List<RelanceDetailDTO> findRelanceDetailsByContribuableId(UUID contribuableId) {
        // Récupérer le dossier de recouvrement du contribuable
        String jpqlDossier = "SELECT d FROM DossierRecouvrement d WHERE d.contribuable.id = :contribuableId";
        DossierRecouvrement dossier = null;
        
        try {
            dossier = (DossierRecouvrement) entityManager.createQuery(jpqlDossier)
                    .setParameter("contribuableId", contribuableId)
                    .getSingleResult();
        } catch (Exception e) {
            return Collections.emptyList();
        }
        
        if (dossier == null) {
            return Collections.emptyList();
        }
        
        // Récupérer les relances associées à ce dossier
        String jpqlRelances = "SELECT r FROM Relance r WHERE r.dossierRecouvrement.id = :dossierId";
        List<Relance> relances = entityManager.createQuery(jpqlRelances, Relance.class)
                .setParameter("dossierId", dossier.getId())
                .getResultList();
        
        List<RelanceDetailDTO> dtos = new ArrayList<>();
        
        for (Relance relance : relances) {
            findRelanceDetailById(relance.getId()).ifPresent(dtos::add);
        }
        
        return dtos;
    }
    
    @Override
    public long countByStatutAndDateEnvoiBetween(String statut, LocalDate startDate, LocalDate endDate) {
        String jpql = "SELECT COUNT(r) FROM Relance r WHERE r.statut = :statut AND r.dateEnvoi BETWEEN :startDate AND :endDate";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class)
                .setParameter("statut", statut)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate);
        return query.getSingleResult();
    }
}
