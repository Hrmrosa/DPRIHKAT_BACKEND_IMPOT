package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Apurement;
import com.DPRIHKAT.entity.enums.StatutApurement;
import com.DPRIHKAT.entity.enums.TypeApurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApurementRepository extends JpaRepository<Apurement, UUID> {
    Apurement findByDeclarationId(UUID declarationId);
    Apurement findByDeclaration_Id(UUID declarationId);
    List<Apurement> findByStatut(StatutApurement statut);
    List<Apurement> findByType(TypeApurement type);
    List<Apurement> findByAgent_Id(UUID agentId);
}
