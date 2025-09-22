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
    List<Plaque> findByVehiculeId(UUID vehiculeId);
    List<Plaque> findByStatut(StatutPlaque statut);
}
