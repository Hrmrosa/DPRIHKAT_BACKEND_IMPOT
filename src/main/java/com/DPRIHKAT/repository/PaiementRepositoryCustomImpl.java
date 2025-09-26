package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.enums.StatutPaiement;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Repository
public class PaiementRepositoryCustomImpl implements PaiementRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public BigDecimal sumMontantByDateBetween(LocalDate startDate, LocalDate endDate) {
        String jpql = "SELECT CAST(SUM(p.montant) AS java.math.BigDecimal) FROM Paiement p WHERE p.date BETWEEN :startDate AND :endDate";
        
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
            System.err.println("Erreur lors du calcul de la somme des paiements: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    @Override
    public long countPaiementsEnRetard() {
        // Utiliser une requête JPQL avec une condition directe car EN_RETARD n'existe pas dans l'énumération
        String jpql = "SELECT COUNT(p) FROM Paiement p WHERE p.dateEcheance < CURRENT_DATE AND p.statut != :statutValide";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class)
                .setParameter("statutValide", StatutPaiement.VALIDE);
        return query.getSingleResult();
    }

    @Override
    public long countByStatut(StatutPaiement statut) {
        String jpql = "SELECT COUNT(p) FROM Paiement p WHERE p.statut = :statut";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class)
                .setParameter("statut", statut);
        return query.getSingleResult();
    }
}
