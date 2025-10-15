package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.DocumentRecouvrement;
import com.DPRIHKAT.entity.enums.StatutDocumentRecouvrement;
import com.DPRIHKAT.entity.enums.TypeDocumentRecouvrement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentRecouvrementRepository extends JpaRepository<DocumentRecouvrement, UUID> {

    List<DocumentRecouvrement> findByType(TypeDocumentRecouvrement type);
    
    List<DocumentRecouvrement> findByStatut(StatutDocumentRecouvrement statut);
    
    List<DocumentRecouvrement> findByContribuableId(UUID contribuableId);
    
    List<DocumentRecouvrement> findByDossierRecouvrementId(UUID dossierRecouvrementId);
    
    List<DocumentRecouvrement> findByTypeAndContribuableId(TypeDocumentRecouvrement type, UUID contribuableId);
    
    List<DocumentRecouvrement> findByTypeAndDossierRecouvrementId(TypeDocumentRecouvrement type, UUID dossierRecouvrementId);
    
    List<DocumentRecouvrement> findByDateGenerationBetween(Date debut, Date fin);
    
    List<DocumentRecouvrement> findByDateEcheanceBefore(Date date);
    
    List<DocumentRecouvrement> findByStatutAndDateEcheanceBefore(StatutDocumentRecouvrement statut, Date date);
    
    @Query("SELECT d FROM DocumentRecouvrement d WHERE d.type = :type AND d.statut = :statut AND d.dateEcheance < :date")
    List<DocumentRecouvrement> findDocumentsEnRetard(
            @Param("type") TypeDocumentRecouvrement type,
            @Param("statut") StatutDocumentRecouvrement statut,
            @Param("date") Date date);
    
    @Query("SELECT d FROM DocumentRecouvrement d WHERE d.type = :type AND d.contribuable.id = :contribuableId ORDER BY d.dateGeneration DESC")
    List<DocumentRecouvrement> findLatestDocumentsByTypeAndContribuable(
            @Param("type") TypeDocumentRecouvrement type,
            @Param("contribuableId") UUID contribuableId);
    
    // Méthodes avec pagination
    Page<DocumentRecouvrement> findByType(TypeDocumentRecouvrement type, Pageable pageable);
    
    Page<DocumentRecouvrement> findByStatut(StatutDocumentRecouvrement statut, Pageable pageable);
    
    Page<DocumentRecouvrement> findByContribuableId(UUID contribuableId, Pageable pageable);
    
    Page<DocumentRecouvrement> findByDossierRecouvrementId(UUID dossierRecouvrementId, Pageable pageable);
    
    @Query("SELECT d FROM DocumentRecouvrement d WHERE " +
           "(:type IS NULL OR d.type = :type) AND " +
           "(:statut IS NULL OR d.statut = :statut) AND " +
           "(:contribuableId IS NULL OR d.contribuable.id = :contribuableId) AND " +
           "(:dossierRecouvrementId IS NULL OR d.dossierRecouvrement.id = :dossierRecouvrementId) AND " +
           "(:dateDebutParam = false OR d.dateGeneration >= :dateDebut) AND " +
           "(:dateFinParam = false OR d.dateGeneration <= :dateFin)")
    Page<DocumentRecouvrement> findDocumentsWithFilters(
            @Param("type") TypeDocumentRecouvrement type,
            @Param("statut") StatutDocumentRecouvrement statut,
            @Param("contribuableId") UUID contribuableId,
            @Param("dossierRecouvrementId") UUID dossierRecouvrementId,
            @Param("dateDebutParam") boolean hasDateDebut,
            @Param("dateDebut") Date dateDebut,
            @Param("dateFinParam") boolean hasDateFin,
            @Param("dateFin") Date dateFin,
            Pageable pageable);
}
