package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Plaque;
import com.DPRIHKAT.entity.enums.StatutPlaque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlaqueRepository extends JpaRepository<Plaque, UUID> {
    Optional<Plaque> findFirstByDisponibleTrue();
    List<Plaque> findByDisponibleTrue();
    List<Plaque> findByDisponibleFalse();
    List<Plaque> findByVehicule_Id(UUID vehiculeId);
    List<Plaque> findByStatut(StatutPlaque statut);
    
    Optional<Plaque> findByNumplaque(String numplaque);
    boolean existsByNumplaque(String numplaque);
    
    // Pour la compatibilité avec l'ancien code
    default boolean existsByNumeroPlaque(String numeroPlaque) {
        return existsByNumplaque(numeroPlaque);
    }
}
