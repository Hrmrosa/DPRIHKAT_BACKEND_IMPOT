package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.DashboardConfig;
import com.DPRIHKAT.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DashboardConfigRepository extends JpaRepository<DashboardConfig, UUID> {
    Optional<DashboardConfig> findByUtilisateur(Utilisateur utilisateur);
}
