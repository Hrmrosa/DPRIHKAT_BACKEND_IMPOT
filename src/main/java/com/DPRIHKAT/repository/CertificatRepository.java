package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Certificat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface CertificatRepository extends JpaRepository<Certificat, UUID> {
    List<Certificat> findByDeclarationId(UUID declarationId);
    List<Certificat> findByVehiculeId(UUID vehiculeId);
    List<Certificat> findByActifTrue();
    List<Certificat> findByDateExpirationBefore(Date date);
}
