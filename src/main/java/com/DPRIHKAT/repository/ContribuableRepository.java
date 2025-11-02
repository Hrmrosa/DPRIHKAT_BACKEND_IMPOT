package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Contribuable;
import com.DPRIHKAT.repository.ContribuableRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Interface de repository pour les contribuables
 */
@Repository
public interface ContribuableRepository extends JpaRepository<Contribuable, UUID>, ContribuableRepositoryCustom {
    /**
     * Trouve tous les contribuables actifs avec pagination
     * @param pageable informations de pagination
     * @return page de contribuables actifs
     */
    Page<Contribuable> findByActifTrue(Pageable pageable);
    
    /**
     * Recherche de contribuables par nom avec pagination
     * @param nom nom ou partie du nom à rechercher
     * @param pageable informations de pagination
     * @return page de contribuables correspondant à la recherche
     */
    Page<Contribuable> findByNomContainingIgnoreCase(String nom, Pageable pageable);

    /**
     * Vérifie si un contribuable existe avec le téléphone donné
     * @param telephone numéro de téléphone à vérifier
     * @return true si le téléphone existe déjà
     */
    boolean existsByTelephonePrincipal(String telephone);

    /**
     * Vérifie si un contribuable existe avec l'email donné
     * @param email email à vérifier
     * @return true si l'email existe déjà
     */
    boolean existsByEmail(String email);

    /**
     * Vérifie si un contribuable existe avec le numéro d'identification donné
     * @param numero numéro d'identification à vérifier
     * @return true si le numéro existe déjà
     */
    boolean existsByNumeroIdentificationContribuable(String numero);

    /**
     * Vérifie si un véhicule avec l'immatriculation donnée est déjà associé à un contribuable
     * @param immatriculation numéro d'immatriculation à vérifier
     * @return true si l'immatriculation existe déjà
     */
    boolean existsByVehiculesImmatriculation(String immatriculation);

    /**
     * Trouve un contribuable par son numéro de téléphone principal
     * @param telephone numéro de téléphone à rechercher
     * @return contribuable trouvé
     */
    Optional<Contribuable> findByTelephonePrincipal(String telephone);

    /**
     * Trouve un contribuable par son email
     * @param email email à rechercher
     * @return contribuable trouvé
     */
    Optional<Contribuable> findByEmail(String email);

    /**
     * Trouve un contribuable par son nom exact (insensible à la casse)
     * @param nom nom à rechercher
     * @return contribuable trouvé
     */
    Optional<Contribuable> findByNomIgnoreCase(String nom);

    /**
     * Trouve un contribuable par son numéro d'identification
     * @param numero numéro d'identification à rechercher
     * @return contribuable trouvé
     */
    Optional<Contribuable> findByNumeroIdentificationContribuable(String numero);
}
