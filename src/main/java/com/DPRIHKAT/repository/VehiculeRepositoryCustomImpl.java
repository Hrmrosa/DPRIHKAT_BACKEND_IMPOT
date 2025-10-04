package com.DPRIHKAT.repository;

import com.DPRIHKAT.dto.VehiculeDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/**
 * Implémentation des méthodes personnalisées du repository VehiculeRepository
 */
@Repository
public class VehiculeRepositoryCustomImpl implements VehiculeRepositoryCustom {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public Optional<VehiculeDTO> findVehiculeDTOById(UUID id) {
        String jpql = "SELECT new com.DPRIHKAT.dto.VehiculeDTO(" +
                "v.id, v.immatriculation, v.marque, v.modele, v.annee, " +
                "v.numeroChassis, v.genre, v.categorie, v.puissanceFiscale, " +
                "v.unitePuissance, v.dateEnregistrement, " +
                "v.proprietaire.id, v.proprietaire.nom) " +
                "FROM Vehicule v WHERE v.id = :id";
        
        try {
            VehiculeDTO dto = (VehiculeDTO) entityManager.createQuery(jpql)
                    .setParameter("id", id)
                    .getSingleResult();
            return Optional.of(dto);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    @Override
    public List<VehiculeDTO> findAllVehiculeDTOs() {
        String jpql = "SELECT new com.DPRIHKAT.dto.VehiculeDTO(" +
                "v.id, v.immatriculation, v.marque, v.modele, v.annee, " +
                "v.numeroChassis, v.genre, v.categorie, v.puissanceFiscale, " +
                "v.unitePuissance, v.dateEnregistrement, " +
                "v.proprietaire.id, v.proprietaire.nom) " +
                "FROM Vehicule v";
        
        return entityManager.createQuery(jpql, VehiculeDTO.class).getResultList();
    }
    
    @Override
    public List<VehiculeDTO> findVehiculeDTOsByProprietaireId(UUID proprietaireId) {
        String jpql = "SELECT new com.DPRIHKAT.dto.VehiculeDTO(" +
                "v.id, v.immatriculation, v.marque, v.modele, v.annee, " +
                "v.numeroChassis, v.genre, v.categorie, v.puissanceFiscale, " +
                "v.unitePuissance, v.dateEnregistrement, " +
                "v.proprietaire.id, v.proprietaire.nom) " +
                "FROM Vehicule v WHERE v.proprietaire.id = :proprietaireId";
        
        return entityManager.createQuery(jpql, VehiculeDTO.class)
                .setParameter("proprietaireId", proprietaireId)
                .getResultList();
    }
    
    @Override
    public long count() {
        String jpql = "SELECT COUNT(v) FROM Vehicule v";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        return query.getSingleResult();
    }
    
    @Override
    public boolean existsByImmatriculation(String immatriculation) {
        String jpql = "SELECT COUNT(v) > 0 FROM Vehicule v WHERE v.immatriculation = :immatriculation";
        TypedQuery<Boolean> query = entityManager.createQuery(jpql, Boolean.class)
                .setParameter("immatriculation", immatriculation);
        return query.getSingleResult();
    }
    
    @Override
    public int changerProprietaire(UUID vehiculeId, UUID nouveauProprietaireId) {
        String jpql = "UPDATE Vehicule v SET v.proprietaire.id = :nouveauProprietaireId WHERE v.id = :vehiculeId";
        
        Query query = entityManager.createQuery(jpql);
        query.setParameter("nouveauProprietaireId", nouveauProprietaireId);
        query.setParameter("vehiculeId", vehiculeId);
        
        return query.executeUpdate();
    }
}
