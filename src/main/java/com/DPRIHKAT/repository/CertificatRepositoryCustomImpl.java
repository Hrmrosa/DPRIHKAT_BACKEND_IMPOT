package com.DPRIHKAT.repository;

import com.DPRIHKAT.dto.CertificatDetailDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CertificatRepositoryCustomImpl implements CertificatRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CertificatDetailDTO> findAllWithDetails() {
        String jpql = "SELECT NEW com.DPRIHKAT.dto.CertificatDetailDTO(c, c.contribuable, c.vehicule, c.propriete, c.taxation, c.agent) " +
                     "FROM Certificat c " +
                     "LEFT JOIN FETCH c.contribuable " +
                     "LEFT JOIN FETCH c.vehicule " +
                     "LEFT JOIN FETCH c.propriete " +
                     "LEFT JOIN FETCH c.taxation " +
                     "LEFT JOIN FETCH c.agent";
        
        return entityManager.createQuery(jpql, CertificatDetailDTO.class).getResultList();
    }

    @Override
    public Optional<CertificatDetailDTO> findCertificatDetailById(UUID id) {
        String jpql = "SELECT NEW com.DPRIHKAT.dto.CertificatDetailDTO(c, c.contribuable, c.vehicule, c.propriete, c.taxation, c.agent) " +
                     "FROM Certificat c " +
                     "LEFT JOIN FETCH c.contribuable " +
                     "LEFT JOIN FETCH c.vehicule " +
                     "LEFT JOIN FETCH c.propriete " +
                     "LEFT JOIN FETCH c.taxation " +
                     "LEFT JOIN FETCH c.agent " +
                     "WHERE c.id = :id";
        
        TypedQuery<CertificatDetailDTO> query = entityManager.createQuery(jpql, CertificatDetailDTO.class);
        query.setParameter("id", id);
        List<CertificatDetailDTO> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<CertificatDetailDTO> findAllCertificatDetails() {
        return findAllWithDetails();
    }

    @Override
    public List<CertificatDetailDTO> findCertificatDetailsByContribuableId(UUID contribuableId) {
        String jpql = "SELECT NEW com.DPRIHKAT.dto.CertificatDetailDTO(c, c.contribuable, c.vehicule, c.propriete, c.taxation, c.agent) " +
                     "FROM Certificat c " +
                     "LEFT JOIN FETCH c.contribuable cont " +
                     "LEFT JOIN FETCH c.vehicule " +
                     "LEFT JOIN FETCH c.propriete " +
                     "LEFT JOIN FETCH c.taxation " +
                     "LEFT JOIN FETCH c.agent " +
                     "WHERE cont.id = :contribuableId";
        
        TypedQuery<CertificatDetailDTO> query = entityManager.createQuery(jpql, CertificatDetailDTO.class);
        query.setParameter("contribuableId", contribuableId);
        return query.getResultList();
    }

    @Override
    public List<CertificatDetailDTO> findCertificatDetailsByVehiculeId(UUID vehiculeId) {
        String jpql = "SELECT NEW com.DPRIHKAT.dto.CertificatDetailDTO(c, c.contribuable, c.vehicule, c.propriete, c.taxation, c.agent) " +
                     "FROM Certificat c " +
                     "LEFT JOIN FETCH c.contribuable " +
                     "LEFT JOIN FETCH c.vehicule v " +
                     "LEFT JOIN FETCH c.propriete " +
                     "LEFT JOIN FETCH c.taxation " +
                     "LEFT JOIN FETCH c.agent " +
                     "WHERE v.id = :vehiculeId";
        
        TypedQuery<CertificatDetailDTO> query = entityManager.createQuery(jpql, CertificatDetailDTO.class);
        query.setParameter("vehiculeId", vehiculeId);
        return query.getResultList();
    }

    @Override
    public List<CertificatDetailDTO> findCertificatDetailsByProprieteId(UUID proprieteId) {
        String jpql = "SELECT NEW com.DPRIHKAT.dto.CertificatDetailDTO(c, c.contribuable, c.vehicule, c.propriete, c.taxation, c.agent) " +
                     "FROM Certificat c " +
                     "LEFT JOIN FETCH c.contribuable " +
                     "LEFT JOIN FETCH c.vehicule " +
                     "LEFT JOIN FETCH c.propriete p " +
                     "LEFT JOIN FETCH c.taxation " +
                     "LEFT JOIN FETCH c.agent " +
                     "WHERE p.id = :proprieteId";
        
        TypedQuery<CertificatDetailDTO> query = entityManager.createQuery(jpql, CertificatDetailDTO.class);
        query.setParameter("proprieteId", proprieteId);
        return query.getResultList();
    }

    @Override
    public List<CertificatDetailDTO> findCertificatDetailsByTaxationId(UUID taxationId) {
        String jpql = "SELECT NEW com.DPRIHKAT.dto.CertificatDetailDTO(c, c.contribuable, c.vehicule, c.propriete, c.taxation, c.agent) " +
                     "FROM Certificat c " +
                     "LEFT JOIN FETCH c.contribuable " +
                     "LEFT JOIN FETCH c.vehicule " +
                     "LEFT JOIN FETCH c.propriete " +
                     "LEFT JOIN FETCH c.taxation t " +
                     "LEFT JOIN FETCH c.agent " +
                     "WHERE t.id = :taxationId";
        
        TypedQuery<CertificatDetailDTO> query = entityManager.createQuery(jpql, CertificatDetailDTO.class);
        query.setParameter("taxationId", taxationId);
        return query.getResultList();
    }
}
