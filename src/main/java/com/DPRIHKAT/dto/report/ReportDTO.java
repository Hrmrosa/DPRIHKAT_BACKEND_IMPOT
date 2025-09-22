package com.DPRIHKAT.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * DTO pour les données des rapports détaillés
 */
public class ReportDTO {
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String typeReport; // TAXATION, APUREMENT, VIGNETTE, PAIEMENT, etc.
    
    // Données détaillées
    private List<ReportItemDTO> items;
    private Map<String, BigDecimal> summaryByType;
    private Map<String, BigDecimal> summaryByRegion;
    private Map<String, BigDecimal> summaryByStatut;
    
    // Totaux
    private BigDecimal totalAmount;
    private int totalItems;
    private int totalCompleted;
    private int totalPending;
    
    // Filtres appliqués
    private Map<String, String> filters;
    
    // Getters et Setters
    public LocalDate getDateDebut() {
        return dateDebut;
    }
    
    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }
    
    public LocalDate getDateFin() {
        return dateFin;
    }
    
    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }
    
    public String getTypeReport() {
        return typeReport;
    }

    public void setTypeReport(String typeReport) {
        this.typeReport = typeReport;
    }

    public List<ReportItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ReportItemDTO> items) {
        this.items = items;
    }

    public Map<String, BigDecimal> getSummaryByType() {
        return summaryByType;
    }

    public void setSummaryByType(Map<String, BigDecimal> summaryByType) {
        this.summaryByType = summaryByType;
    }

    public Map<String, BigDecimal> getSummaryByRegion() {
        return summaryByRegion;
    }

    public void setSummaryByRegion(Map<String, BigDecimal> summaryByRegion) {
        this.summaryByRegion = summaryByRegion;
    }

    public Map<String, BigDecimal> getSummaryByStatut() {
        return summaryByStatut;
    }

    public void setSummaryByStatut(Map<String, BigDecimal> summaryByStatut) {
        this.summaryByStatut = summaryByStatut;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalCompleted() {
        return totalCompleted;
    }

    public void setTotalCompleted(int totalCompleted) {
        this.totalCompleted = totalCompleted;
    }

    public int getTotalPending() {
        return totalPending;
    }

    public void setTotalPending(int totalPending) {
        this.totalPending = totalPending;
    }

    public Map<String, String> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, String> filters) {
        this.filters = filters;
    }
}
