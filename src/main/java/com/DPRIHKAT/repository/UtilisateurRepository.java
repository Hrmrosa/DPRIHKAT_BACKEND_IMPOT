package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, UUID> {
    Optional<Utilisateur> findByLogin(String login);
    Boolean existsByLogin(String login);
    Page<Utilisateur> findByRole(Role role, Pageable pageable);
    
    @Query("SELECT u FROM Utilisateur u WHERE u.login LIKE %:searchTerm% OR u.role LIKE %:searchTerm%")
    Page<Utilisateur> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
}
