package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.DossierRecouvrement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DossierRecouvrementRepository extends JpaRepository<DossierRecouvrement, UUID> {
}
