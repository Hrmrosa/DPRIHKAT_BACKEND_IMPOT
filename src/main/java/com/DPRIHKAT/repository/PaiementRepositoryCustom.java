package com.DPRIHKAT.repository;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface PaiementRepositoryCustom {
    BigDecimal sumMontantByDateBetween(LocalDate startDate, LocalDate endDate);
    long countPaiementsEnRetard();
    long countByStatut(String statut);
}
