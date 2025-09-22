package com.DPRIHKAT.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class ProprieteRepositoryCustomImpl implements ProprieteRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public long count() {
        String jpql = "SELECT COUNT(p) FROM Propriete p";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        return query.getSingleResult();
    }
}
