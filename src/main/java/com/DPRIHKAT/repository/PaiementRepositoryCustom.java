package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.enums.StatutPaiement;
import java.math.BigDecimal;
import java.time.LocalDate;

public interface PaiementRepositoryCustom {
    BigDecimal sumMontantByDateBetween(LocalDate startDate, LocalDate endDate);
    long countPaiementsEnRetard();
    long countByStatut(StatutPaiement statut);
}
