package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.LogConnexion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LogConnexionRepository extends JpaRepository<LogConnexion, UUID> {

    /**
     * Trouve les derniers logs de connexion, triés par date de connexion décroissante
     * @param limit Nombre maximum de logs à récupérer
     * @return Liste des logs de connexion
     */
    @Query(value = "SELECT l FROM LogConnexion l ORDER BY l.dateConnexion DESC LIMIT :limit")
    List<LogConnexion> findTopByOrderByDateConnexionDesc(@Param("limit") int limit);

    /**
     * Trouve les logs de connexion d'un utilisateur spécifique, triés par date de connexion décroissante
     * @param utilisateurId ID de l'utilisateur
     * @param limit Nombre maximum de logs à récupérer
     * @return Liste des logs de connexion
     */
    @Query(value = "SELECT l FROM LogConnexion l WHERE l.utilisateurId = :utilisateurId ORDER BY l.dateConnexion DESC LIMIT :limit")
    List<LogConnexion> findByUtilisateurIdOrderByDateConnexionDesc(@Param("utilisateurId") UUID utilisateurId, @Param("limit") int limit);
}
