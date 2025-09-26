package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Apurement;
import com.DPRIHKAT.entity.enums.StatutApurement;
import com.DPRIHKAT.entity.enums.TypeApurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApurementRepository extends JpaRepository<Apurement, UUID> {
    @Query("SELECT a FROM Apurement a WHERE a.taxation.declaration.id = :declarationId")
    Apurement findByDeclarationId(@Param("declarationId") UUID declarationId);
    
    List<Apurement> findByDeclaration_Id(UUID declarationId);
    
    Apurement findByPaiement_Id(UUID paiementId);
    
    List<Apurement> findByStatut(StatutApurement statut);
    List<Apurement> findByType(TypeApurement type);
    List<Apurement> findByAgent_Id(UUID agentId);
}
