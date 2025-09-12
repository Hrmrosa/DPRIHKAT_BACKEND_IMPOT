package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.TauxChange;
import com.DPRIHKAT.entity.enums.Devise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour l'entité TauxChange
 */
@Repository
public interface TauxChangeRepository extends JpaRepository<TauxChange, UUID> {
    
    /**
     * Trouve tous les taux de change actifs
     * @return la liste des taux de change actifs
     */
    List<TauxChange> findByActifTrue();
    
    /**
     * Trouve le taux de change actif le plus récent pour une paire de devises
     * @param deviseSource la devise source
     * @param deviseDestination la devise destination
     * @return le taux de change actif le plus récent
     */
    @Query("SELECT t FROM TauxChange t WHERE t.deviseSource = :deviseSource AND t.deviseDestination = :deviseDestination AND t.actif = true ORDER BY t.dateEffective DESC")
    Optional<TauxChange> findLatestActiveTauxChange(@Param("deviseSource") Devise deviseSource, @Param("deviseDestination") Devise deviseDestination);
    
    /**
     * Trouve le taux de change actif à une date donnée pour une paire de devises
     * @param deviseSource la devise source
     * @param deviseDestination la devise destination
     * @param date la date
     * @return le taux de change actif à la date donnée
     */
    @Query("SELECT t FROM TauxChange t WHERE t.deviseSource = :deviseSource AND t.deviseDestination = :deviseDestination AND t.dateEffective <= :date AND t.actif = true ORDER BY t.dateEffective DESC")
    Optional<TauxChange> findTauxChangeAtDate(@Param("deviseSource") Devise deviseSource, @Param("deviseDestination") Devise deviseDestination, @Param("date") Date date);
}
