package com.DPRIHKAT.dto;

import com.DPRIHKAT.entity.enums.TypePropriete;
import java.util.List;
import java.util.UUID;

/**
 * DTO pour la création d'une propriété avec choix d'impôt
 * 
 * @author amateur
 */
public class ProprieteCreationDTO {
    
    private String designation;
    private TypePropriete type;
    private String localite;
    private Integer rangLocalite;
    private Double superficie;
    private String adresse;
    private Integer nombreNiveaux;
    private Integer nombrePieces;
    private String dateConstruction;
    private Double latitude;
    private Double longitude;
    private String numeroParcelle;
    private UUID proprietaireId;
    private List<UUID> naturesImpotIds;

    // Constructeurs
    public ProprieteCreationDTO() {
    }
    
    public ProprieteCreationDTO(String designation, TypePropriete type, String localite, Integer rangLocalite, Double superficie, 
            String adresse, Integer nombreNiveaux, Integer nombrePieces, String dateConstruction, Double latitude, Double longitude, 
            String numeroParcelle, UUID proprietaireId, List<UUID> naturesImpotIds) {
        this.designation = designation;
        this.type = type;
        this.localite = localite;
        this.rangLocalite = rangLocalite;
        this.superficie = superficie;
        this.adresse = adresse;
        this.nombreNiveaux = nombreNiveaux;
        this.nombrePieces = nombrePieces;
        this.dateConstruction = dateConstruction;
        this.latitude = latitude;
        this.longitude = longitude;
        this.numeroParcelle = numeroParcelle;
        this.proprietaireId = proprietaireId;
        this.naturesImpotIds = naturesImpotIds;
    }
    
    // Getters et Setters
    public String getDesignation() {
        return designation;
    }
    
    public void setDesignation(String designation) {
        this.designation = designation;
    }
    
    public TypePropriete getType() {
        return type;
    }
    
    public void setType(TypePropriete type) {
        this.type = type;
    }
    
    public String getLocalite() {
        return localite;
    }
    
    public void setLocalite(String localite) {
        this.localite = localite;
    }
    
    public Integer getRangLocalite() {
        return rangLocalite;
    }
    
    public void setRangLocalite(Integer rangLocalite) {
        this.rangLocalite = rangLocalite;
    }
    
    public Double getSuperficie() {
        return superficie;
    }
    
    public void setSuperficie(Double superficie) {
        this.superficie = superficie;
    }
    
    public String getAdresse() {
        return adresse;
    }
    
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    
    public Integer getNombreNiveaux() {
        return nombreNiveaux;
    }
    
    public void setNombreNiveaux(Integer nombreNiveaux) {
        this.nombreNiveaux = nombreNiveaux;
    }
    
    public Integer getNombrePieces() {
        return nombrePieces;
    }
    
    public void setNombrePieces(Integer nombrePieces) {
        this.nombrePieces = nombrePieces;
    }
    
    public String getDateConstruction() {
        return dateConstruction;
    }
    
    public void setDateConstruction(String dateConstruction) {
        this.dateConstruction = dateConstruction;
    }
    
    public Double getLatitude() {
        return latitude;
    }
    
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    
    public Double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    
    public String getNumeroParcelle() {
        return numeroParcelle;
    }
    
    public void setNumeroParcelle(String numeroParcelle) {
        this.numeroParcelle = numeroParcelle;
    }
    
    public UUID getProprietaireId() {
        return proprietaireId;
    }
    
    public void setProprietaireId(UUID proprietaireId) {
        this.proprietaireId = proprietaireId;
    }
    
    public List<UUID> getNaturesImpotIds() {
        return naturesImpotIds;
    }
    
    public void setNaturesImpotIds(List<UUID> naturesImpotIds) {
        this.naturesImpotIds = naturesImpotIds;
    }
}
