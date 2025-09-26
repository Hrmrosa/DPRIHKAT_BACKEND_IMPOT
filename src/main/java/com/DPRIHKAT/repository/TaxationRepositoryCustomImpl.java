package com.DPRIHKAT.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TaxationRepositoryCustomImpl implements TaxationRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public BigDecimal sumMontantByDateBetween(LocalDate startDate, LocalDate endDate) {
        String jpql = "SELECT CAST(SUM(t.montant) AS java.math.BigDecimal) FROM Taxation t WHERE t.dateTaxation BETWEEN :startDate AND :endDate";
        
        // Convertir LocalDate en Date
        Date startDateAsDate = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDateAsDate = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());
        
        try {
            TypedQuery<BigDecimal> query = entityManager.createQuery(jpql, BigDecimal.class)
                    .setParameter("startDate", startDateAsDate)
                    .setParameter("endDate", endDateAsDate);
            
            BigDecimal result = query.getSingleResult();
            return result != null ? result : BigDecimal.ZERO;
        } catch (Exception e) {
            // En cas d'erreur, on log et on retourne zéro
            System.err.println("Erreur lors du calcul de la somme des montants: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    @Override
    public Map<String, Long> countByTypeImpotBetweenDates(LocalDate startDate, LocalDate endDate) {
        String jpql = "SELECT t.typeImpot, COUNT(t) FROM Taxation t WHERE t.dateTaxation BETWEEN :startDate AND :endDate GROUP BY t.typeImpot";
        
        // Convertir LocalDate en Date
        Date startDateAsDate = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDateAsDate = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());
        
        try {
            List<Object[]> results = entityManager.createQuery(jpql, Object[].class)
                    .setParameter("startDate", startDateAsDate)
                    .setParameter("endDate", endDateAsDate)
                    .getResultList();
            
            Map<String, Long> stats = new HashMap<>();
            results.forEach(result -> stats.put(result[0].toString(), (Long) result[1]));
            return stats;
        } catch (Exception e) {
            System.err.println("Erreur lors du comptage par type d'impôt: " + e.getMessage());
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, BigDecimal> getSummaryByType(LocalDate startDate, LocalDate endDate) {
        String jpql = "SELECT t.typeImpot, CAST(SUM(t.montant) AS java.math.BigDecimal) FROM Taxation t WHERE t.dateTaxation BETWEEN :startDate AND :endDate GROUP BY t.typeImpot";
        
        // Convertir LocalDate en Date
        Date startDateAsDate = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDateAsDate = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());
        
        try {
            List<Object[]> results = entityManager.createQuery(jpql, Object[].class)
                    .setParameter("startDate", startDateAsDate)
                    .setParameter("endDate", endDateAsDate)
                    .getResultList();
            
            Map<String, BigDecimal> summary = new HashMap<>();
            results.forEach(result -> {
                Object value = result[1];
                BigDecimal amount;
                if (value instanceof BigDecimal) {
                    amount = (BigDecimal) value;
                } else if (value instanceof Double) {
                    amount = BigDecimal.valueOf((Double) value);
                } else if (value instanceof Long) {
                    amount = BigDecimal.valueOf((Long) value);
                } else if (value instanceof Integer) {
                    amount = BigDecimal.valueOf((Integer) value);
                } else {
                    amount = BigDecimal.ZERO;
                }
                summary.put(result[0].toString(), amount);
            });
            return summary;
        } catch (Exception e) {
            System.err.println("Erreur lors du calcul des montants par type: " + e.getMessage());
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, BigDecimal> getSummaryByStatut(LocalDate startDate, LocalDate endDate) {
        String jpql = "SELECT t.statut, SUM(t.montant) FROM Taxation t WHERE t.dateTaxation BETWEEN :startDate AND :endDate GROUP BY t.statut";
        
        // Convertir LocalDate en Date
        Date startDateAsDate = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDateAsDate = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());
        
        List<Object[]> results = entityManager.createQuery(jpql, Object[].class)
                .setParameter("startDate", startDateAsDate)
                .setParameter("endDate", endDateAsDate)
                .getResultList();
        
        Map<String, BigDecimal> summary = new HashMap<>();
        results.forEach(result -> summary.put(result[0].toString(), (BigDecimal) result[1]));
        return summary;
    }
}
