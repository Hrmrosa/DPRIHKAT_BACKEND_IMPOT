package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Vehicule;
import com.DPRIHKAT.entity.Vignette;
import com.DPRIHKAT.entity.enums.StatutVignette;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface VignetteRepository extends JpaRepository<Vignette, UUID> {
    List<Vignette> findByVehicule(Vehicule vehicule);
    List<Vignette> findByActifTrue();
    List<Vignette> findByDateExpirationBefore(Date date);
    List<Vignette> findByStatut(StatutVignette statut);
    List<Vignette> findByStatutAndDateExpirationBefore(StatutVignette statut, Date date);
}
