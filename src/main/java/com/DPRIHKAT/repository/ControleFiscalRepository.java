package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.ControleFiscal;
import com.DPRIHKAT.entity.enums.StatutControle;
import com.DPRIHKAT.entity.enums.TypeControle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public interface ControleFiscalRepository extends JpaRepository<ControleFiscal, UUID> {
    List<ControleFiscal> findByAgentInitiateurId(UUID agentId);
    List<ControleFiscal> findByDeclarationId(UUID declarationId);
    List<ControleFiscal> findByStatut(StatutControle statut);
    List<ControleFiscal> findByType(TypeControle type);
    List<ControleFiscal> findByDateCreationBetween(Date startDate, Date endDate);
    
    @Query("SELECT c FROM ControleFiscal c WHERE c.declaration.agentValidateur.bureau.id = :bureauId")
    List<ControleFiscal> findByDeclarationContribuableBureauId(@Param("bureauId") UUID bureauId);
    
    @Query("SELECT c FROM ControleFiscal c WHERE c.declaration.agentValidateur.bureau.division.id = :divisionId")
    List<ControleFiscal> findByDeclarationContribuableBureauDivisionId(@Param("divisionId") UUID divisionId);
    
    @Query("SELECT COUNT(c) FROM ControleFiscal c WHERE c.declaration.agentValidateur.bureau.id = :bureauId")
    Long countByDeclarationContribuableBureauId(@Param("bureauId") UUID bureauId);
    
    @Query("SELECT COUNT(c) FROM ControleFiscal c WHERE c.declaration.agentValidateur.bureau.division.id = :divisionId")
    Long countByDeclarationContribuableBureauDivisionId(@Param("divisionId") UUID divisionId);
    
    Long countByStatut(StatutControle statut);
    
    @Query("SELECT c.statut, COUNT(c) FROM ControleFiscal c GROUP BY c.statut")
    List<Object[]> countByStatutGroupedByStatut();
}
