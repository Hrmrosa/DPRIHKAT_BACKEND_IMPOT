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

@Repository
public interface TauxChangeRepository extends JpaRepository<TauxChange, UUID> {
    
    List<TauxChange> findByActifTrue();
    
    @Query("SELECT t FROM TauxChange t WHERE t.deviseSource = :deviseSource AND t.deviseDestination = :deviseDestination AND t.actif = true ORDER BY t.dateEffective DESC")
    Optional<TauxChange> findLatestActiveTauxChange(@Param("deviseSource") Devise deviseSource, @Param("deviseDestination") Devise deviseDestination);
    
    @Query("SELECT t FROM TauxChange t WHERE t.deviseSource = :deviseSource AND t.deviseDestination = :deviseDestination AND t.dateEffective <= :date AND t.actif = true ORDER BY t.dateEffective DESC")
    Optional<TauxChange> findTauxChangeAtDate(@Param("deviseSource") Devise deviseSource, @Param("deviseDestination") Devise deviseDestination, @Param("date") Date date);
    
    @Query("SELECT t FROM TauxChange t ORDER BY t.dateEffective DESC")
    List<TauxChange> findAllTauxChangesOrderByDateDesc();
    
    @Query("SELECT t FROM TauxChange t WHERE t.deviseSource = :deviseSource AND t.deviseDestination = :deviseDestination ORDER BY t.dateEffective DESC")
    List<TauxChange> findAllTauxChangesByDevises(@Param("deviseSource") Devise deviseSource, @Param("deviseDestination") Devise deviseDestination);
}
