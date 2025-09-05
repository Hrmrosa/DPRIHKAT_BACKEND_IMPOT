package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Bureau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.Optional;

@Repository
public interface BureauRepository extends JpaRepository<Bureau, UUID> {
    boolean existsByNom(String nom);
    boolean existsByNomAndIdNot(String nom, UUID id);
    
    /**
     * Trouve un bureau par son nom
     * @param nom Le nom du bureau
     * @return Le bureau correspondant, s'il existe
     */
    Optional<Bureau> findByNom(String nom);
}
