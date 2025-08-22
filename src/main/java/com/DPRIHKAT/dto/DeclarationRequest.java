package com.DPRIHKAT.dto;

import com.DPRIHKAT.entity.enums.TypeImpot;
import org.locationtech.jts.geom.Geometry;

import java.util.Date;
import java.util.UUID;

public class DeclarationRequest {
    private Date date;
    private Double montant;
    private TypeImpot typeImpot;
    private boolean exoneration;
    private UUID proprieteId;
    private UUID concessionId;
    private Geometry location; // For geolocation

    // Getters and setters
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public TypeImpot getTypeImpot() {
        return typeImpot;
    }

    public void setTypeImpot(TypeImpot typeImpot) {
        this.typeImpot = typeImpot;
    }

    public boolean isExoneration() {
        return exoneration;
    }

    public void setExoneration(boolean exoneration) {
        this.exoneration = exoneration;
    }

    public UUID getProprieteId() {
        return proprieteId;
    }

    public void setProprieteId(UUID proprieteId) {
        this.proprieteId = proprieteId;
    }

    public UUID getConcessionId() {
        return concessionId;
    }

    public void setConcessionId(UUID concessionId) {
        this.concessionId = concessionId;
    }

    public Geometry getLocation() {
        return location;
    }

    public void setLocation(Geometry location) {
        this.location = location;
    }
}
