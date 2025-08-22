package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.ConcessionMinier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConcessionMinierRepository extends JpaRepository<ConcessionMinier, UUID> {
    List<ConcessionMinier> findByTitulaire_Id(UUID titulaireId);
}
