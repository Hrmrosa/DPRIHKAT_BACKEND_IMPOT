package com.DPRIHKAT.dto;

import java.util.Date;

public class RapportFiscalDTO {
    private Date periodeDebut;
    private Date periodeFin;
    private long nombreDeclarations;
    private long nombrePaiements;
    private long nombrePenalites;
    private long nombreApurements;
    private double totalDeclarations;
    private double totalPaiements;
    private double totalPenalites;
    private double totalApurements;
    private double balance;
    
    // Getters and setters
    public Date getPeriodeDebut() {
        return periodeDebut;
    }
    
    public void setPeriodeDebut(Date periodeDebut) {
        this.periodeDebut = periodeDebut;
    }
    
    public Date getPeriodeFin() {
        return periodeFin;
    }
    
    public void setPeriodeFin(Date periodeFin) {
        this.periodeFin = periodeFin;
    }
    
    public long getNombreDeclarations() {
        return nombreDeclarations;
    }
    
    public void setNombreDeclarations(long nombreDeclarations) {
        this.nombreDeclarations = nombreDeclarations;
    }
    
    public long getNombrePaiements() {
        return nombrePaiements;
    }
    
    public void setNombrePaiements(long nombrePaiements) {
        this.nombrePaiements = nombrePaiements;
    }
    
    public long getNombrePenalites() {
        return nombrePenalites;
    }
    
    public void setNombrePenalites(long nombrePenalites) {
        this.nombrePenalites = nombrePenalites;
    }
    
    public long getNombreApurements() {
        return nombreApurements;
    }
    
    public void setNombreApurements(long nombreApurements) {
        this.nombreApurements = nombreApurements;
    }
    
    public double getTotalDeclarations() {
        return totalDeclarations;
    }
    
    public void setTotalDeclarations(double totalDeclarations) {
        this.totalDeclarations = totalDeclarations;
    }
    
    public double getTotalPaiements() {
        return totalPaiements;
    }
    
    public void setTotalPaiements(double totalPaiements) {
        this.totalPaiements = totalPaiements;
    }
    
    public double getTotalPenalites() {
        return totalPenalites;
    }
    
    public void setTotalPenalites(double totalPenalites) {
        this.totalPenalites = totalPenalites;
    }
    
    public double getTotalApurements() {
        return totalApurements;
    }
    
    public void setTotalApurements(double totalApurements) {
        this.totalApurements = totalApurements;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public void setBalance(double balance) {
        this.balance = balance;
    }
}
