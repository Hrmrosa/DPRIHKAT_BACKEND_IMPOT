package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Penalite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface PenaliteRepository extends JpaRepository<Penalite, UUID> {
    List<Penalite> findByDeclarationId(UUID declarationId);
    List<Penalite> findByDeclaration_Propriete_Proprietaire_Id(UUID contribuableId);
    List<Penalite> findByDateApplicationBetween(Date startDate, Date endDate);
}
