package com.DPRIHKAT.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class DeclarationRepositoryCustomImpl implements DeclarationRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public long countDeclarationsEnRetard() {
        String jpql = "SELECT COUNT(d) FROM Declaration d WHERE d.statut = 'EN_RETARD'";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        return query.getSingleResult();
    }

    @Override
    public long countByStatut(String statut) {
        String jpql = "SELECT COUNT(d) FROM Declaration d WHERE d.statut = :statut";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class)
                .setParameter("statut", statut);
        return query.getSingleResult();
    }
}
