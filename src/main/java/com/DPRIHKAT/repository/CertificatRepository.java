package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Certificat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CertificatRepository extends JpaRepository<Certificat, UUID>, CertificatRepositoryCustom {
    Optional<Certificat> findByNumero(String numero);
    boolean existsByNumero(String numero);
}
