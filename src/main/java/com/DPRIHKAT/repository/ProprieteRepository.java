package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Propriete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProprieteRepository extends JpaRepository<Propriete, UUID> {
    List<Propriete> findByProprietaire_Id(UUID proprietaireId);
}
