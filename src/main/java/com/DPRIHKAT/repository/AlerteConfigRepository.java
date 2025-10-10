package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.AlerteConfig;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.enums.TypeAlerte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AlerteConfigRepository extends JpaRepository<AlerteConfig, UUID> {
    List<AlerteConfig> findByUtilisateur(Utilisateur utilisateur);
    List<AlerteConfig> findByUtilisateurAndActive(Utilisateur utilisateur, boolean active);
    List<AlerteConfig> findByTypeAlerteAndActive(TypeAlerte typeAlerte, boolean active);
}
