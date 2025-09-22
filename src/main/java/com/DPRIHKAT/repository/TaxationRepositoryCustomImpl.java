package com.DPRIHKAT.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TaxationRepositoryCustomImpl implements TaxationRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public BigDecimal sumMontantByDateBetween(LocalDate startDate, LocalDate endDate) {
        String jpql = "SELECT SUM(t.montant) FROM Taxation t WHERE t.dateTaxation BETWEEN :startDate AND :endDate";
        TypedQuery<BigDecimal> query = entityManager.createQuery(jpql, BigDecimal.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate);
        return query.getSingleResult();
    }

    @Override
    public Map<String, Long> countByTypeImpotBetweenDates(LocalDate startDate, LocalDate endDate) {
        String jpql = "SELECT t.typeImpot, COUNT(t) FROM Taxation t WHERE t.dateTaxation BETWEEN :startDate AND :endDate GROUP BY t.typeImpot";
        List<Object[]> results = entityManager.createQuery(jpql, Object[].class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
        
        Map<String, Long> stats = new HashMap<>();
        results.forEach(result -> stats.put((String) result[0], (Long) result[1]));
        return stats;
    }

    @Override
    public Map<String, BigDecimal> getSummaryByType(LocalDate startDate, LocalDate endDate) {
        String jpql = "SELECT t.typeImpot, SUM(t.montant) FROM Taxation t WHERE t.dateTaxation BETWEEN :startDate AND :endDate GROUP BY t.typeImpot";
        List<Object[]> results = entityManager.createQuery(jpql, Object[].class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
        
        Map<String, BigDecimal> summary = new HashMap<>();
        results.forEach(result -> summary.put((String) result[0], (BigDecimal) result[1]));
        return summary;
    }

    @Override
    public Map<String, BigDecimal> getSummaryByStatut(LocalDate startDate, LocalDate endDate) {
        String jpql = "SELECT t.statut, SUM(t.montant) FROM Taxation t WHERE t.dateTaxation BETWEEN :startDate AND :endDate GROUP BY t.statut";
        List<Object[]> results = entityManager.createQuery(jpql, Object[].class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
        
        Map<String, BigDecimal> summary = new HashMap<>();
        results.forEach(result -> summary.put((String) result[0], (BigDecimal) result[1]));
        return summary;
    }
}
