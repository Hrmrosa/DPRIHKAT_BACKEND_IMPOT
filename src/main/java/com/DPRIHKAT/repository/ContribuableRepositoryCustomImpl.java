package com.DPRIHKAT.repository;

import com.DPRIHKAT.dto.ContribuableDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ContribuableRepositoryCustomImpl implements ContribuableRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ContribuableDTO> findAllWithDetails() {
        // Implémentation simplifiée pour le moment
        return new ArrayList<>();
    }

    @Override
    public long countByActifTrue() {
        String jpql = "SELECT COUNT(c) FROM Contribuable c WHERE c.actif = true";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        return query.getSingleResult();
    }

    @Override
    public long countByActifFalse() {
        String jpql = "SELECT COUNT(c) FROM Contribuable c WHERE c.actif = false";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        return query.getSingleResult();
    }
}
