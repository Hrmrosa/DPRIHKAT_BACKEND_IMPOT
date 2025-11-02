package com.DPRIHKAT.repository;

import com.DPRIHKAT.dto.CertificatDetailDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CertificatRepositoryCustom {
    List<CertificatDetailDTO> findAllWithDetails();
    Optional<CertificatDetailDTO> findCertificatDetailById(UUID id);
    List<CertificatDetailDTO> findAllCertificatDetails();
    List<CertificatDetailDTO> findCertificatDetailsByContribuableId(UUID contribuableId);
    List<CertificatDetailDTO> findCertificatDetailsByVehiculeId(UUID vehiculeId);
    List<CertificatDetailDTO> findCertificatDetailsByProprieteId(UUID proprieteId);
    List<CertificatDetailDTO> findCertificatDetailsByTaxationId(UUID taxationId);
}
