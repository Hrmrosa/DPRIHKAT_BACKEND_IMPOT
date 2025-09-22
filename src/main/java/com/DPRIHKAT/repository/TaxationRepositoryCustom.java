package com.DPRIHKAT.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public interface TaxationRepositoryCustom {
    BigDecimal sumMontantByDateBetween(LocalDate startDate, LocalDate endDate);
    Map<String, Long> countByTypeImpotBetweenDates(LocalDate startDate, LocalDate endDate);
    Map<String, BigDecimal> getSummaryByType(LocalDate startDate, LocalDate endDate);
    Map<String, BigDecimal> getSummaryByStatut(LocalDate startDate, LocalDate endDate);
}
