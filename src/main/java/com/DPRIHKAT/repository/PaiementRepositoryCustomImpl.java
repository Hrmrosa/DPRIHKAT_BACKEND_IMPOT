package com.DPRIHKAT.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public class PaiementRepositoryCustomImpl implements PaiementRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public BigDecimal sumMontantByDateBetween(LocalDate startDate, LocalDate endDate) {
        String jpql = "SELECT SUM(p.montant) FROM Paiement p WHERE p.date BETWEEN :startDate AND :endDate";
        TypedQuery<BigDecimal> query = entityManager.createQuery(jpql, BigDecimal.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate);
        return query.getSingleResult();
    }

    @Override
    public long countPaiementsEnRetard() {
        String jpql = "SELECT COUNT(p) FROM Paiement p WHERE p.statut = 'EN_RETARD'";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        return query.getSingleResult();
    }

    @Override
    public long countByStatut(String statut) {
        String jpql = "SELECT COUNT(p) FROM Paiement p WHERE p.statut = :statut";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class)
                .setParameter("statut", statut);
        return query.getSingleResult();
    }
}
