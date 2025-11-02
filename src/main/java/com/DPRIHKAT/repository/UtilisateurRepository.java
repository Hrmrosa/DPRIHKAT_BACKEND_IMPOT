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
    Optional<Utilisateur> findByEmail(String email);
    Optional<Utilisateur> findByMatricule(String matricule);
    
    Boolean existsByLogin(String login);
    Boolean existsByEmail(String email);
    Boolean existsByMatricule(String matricule);

    Page<Utilisateur> findByRole(Role role, Pageable pageable);

    @Query("SELECT u FROM Utilisateur u WHERE " +
           "LOWER(u.nomComplet) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.matricule) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.login) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Utilisateur> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT u FROM Utilisateur u WHERE u.role = :role AND " +
           "(LOWER(u.nomComplet) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.matricule) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.login) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Utilisateur> findByRoleAndSearchTerm(@Param("role") Role role, @Param("searchTerm") String searchTerm, Pageable pageable);
}
