package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, UUID>, VehiculeRepositoryCustom {
    // Méthodes existantes
}
