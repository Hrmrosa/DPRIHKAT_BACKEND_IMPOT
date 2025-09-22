package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Relance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository pour l'entité Relance
 * Étend RelanceRepositoryCustom pour ajouter les méthodes personnalisées
 * permettant de récupérer les informations détaillées des relances
 */
@Repository
public interface RelanceRepository extends RelanceRepositoryCustom, JpaRepository<Relance, UUID> {
    // Méthodes existantes
}
