package com.DPRIHKAT.dto;

import com.DPRIHKAT.entity.DemandePlaque;
import com.DPRIHKAT.entity.enums.StatutDemande;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * DTO pour la réponse d'une demande de plaque avec les notes de taxation
 * 
 * @author amateur
 */
public class DemandePlaqueResponseDTO {
    
    private UUID demandeId;
    private StatutDemande statut;
    private Date dateDemande;
    private Date dateValidation;
    private Date datePaiement;
    private Date dateLivraison;
    
    // Informations du véhicule
    private UUID vehiculeId;
    private String vehiculeMarque;
    private String vehiculeModele;
    private String vehiculeNumeroChassis;
    private String vehiculeImmatriculation;
    
    // Informations du contribuable
    private UUID contribuableId;
    private String contribuableNom;
    private String contribuableNRC;
    
    // Notes de taxation
    private NoteTaxationDTO notePlaque;
    private NoteTaxationDTO noteVignette;
    
    // Informations de la plaque assignée
    private UUID plaqueId;
    private String plaqueNumero;
    private String plaqueStatut;
    private Date plaqueDateAttribution;
    
    // Message
    private String message;
    
    // Constructeurs
    public DemandePlaqueResponseDTO() {
    }
    
    public DemandePlaqueResponseDTO(DemandePlaque demande, NoteTaxationDTO notePlaque, NoteTaxationDTO noteVignette) {
        this.demandeId = demande.getId();
        this.statut = demande.getStatut();
        this.dateDemande = demande.getDateDemande();
        this.dateValidation = demande.getDateValidation();
        this.datePaiement = demande.getDatePaiement();
        this.dateLivraison = demande.getDateLivraison();
        
        if (demande.getVehicule() != null) {
            this.vehiculeId = demande.getVehicule().getId();
            this.vehiculeMarque = demande.getVehicule().getMarque();
            this.vehiculeModele = demande.getVehicule().getModele();
            this.vehiculeNumeroChassis = demande.getVehicule().getNumeroChassis();
            this.vehiculeImmatriculation = demande.getVehicule().getImmatriculation();
        }
        
        if (demande.getContribuable() != null) {
            this.contribuableId = demande.getContribuable().getId();
            this.contribuableNom = demande.getContribuable().getNom();
            this.contribuableNRC = demande.getContribuable().getNRC();
        }
        
        // Informations de la plaque si assignée
        if (demande.getPlaque() != null) {
            this.plaqueId = demande.getPlaque().getId();
            this.plaqueNumero = demande.getPlaque().getNumplaque();
            this.plaqueStatut = demande.getPlaque().getStatut().toString();
            // La date d'attribution correspond à la date de livraison de la demande
            this.plaqueDateAttribution = demande.getDateLivraison();
        }
        
        this.notePlaque = notePlaque;
        this.noteVignette = noteVignette;
    }
    
    // Getters et Setters
    public UUID getDemandeId() {
        return demandeId;
    }
    
    public void setDemandeId(UUID demandeId) {
        this.demandeId = demandeId;
    }
    
    public StatutDemande getStatut() {
        return statut;
    }
    
    public void setStatut(StatutDemande statut) {
        this.statut = statut;
    }
    
    public Date getDateDemande() {
        return dateDemande;
    }
    
    public void setDateDemande(Date dateDemande) {
        this.dateDemande = dateDemande;
    }
    
    public Date getDateValidation() {
        return dateValidation;
    }
    
    public void setDateValidation(Date dateValidation) {
        this.dateValidation = dateValidation;
    }
    
    public Date getDatePaiement() {
        return datePaiement;
    }
    
    public void setDatePaiement(Date datePaiement) {
        this.datePaiement = datePaiement;
    }
    
    public Date getDateLivraison() {
        return dateLivraison;
    }
    
    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
    }
    
    public UUID getVehiculeId() {
        return vehiculeId;
    }
    
    public void setVehiculeId(UUID vehiculeId) {
        this.vehiculeId = vehiculeId;
    }
    
    public String getVehiculeMarque() {
        return vehiculeMarque;
    }
    
    public void setVehiculeMarque(String vehiculeMarque) {
        this.vehiculeMarque = vehiculeMarque;
    }
    
    public String getVehiculeModele() {
        return vehiculeModele;
    }
    
    public void setVehiculeModele(String vehiculeModele) {
        this.vehiculeModele = vehiculeModele;
    }
    
    public String getVehiculeNumeroChassis() {
        return vehiculeNumeroChassis;
    }
    
    public void setVehiculeNumeroChassis(String vehiculeNumeroChassis) {
        this.vehiculeNumeroChassis = vehiculeNumeroChassis;
    }
    
    public String getVehiculeImmatriculation() {
        return vehiculeImmatriculation;
    }
    
    public void setVehiculeImmatriculation(String vehiculeImmatriculation) {
        this.vehiculeImmatriculation = vehiculeImmatriculation;
    }
    
    public UUID getContribuableId() {
        return contribuableId;
    }
    
    public void setContribuableId(UUID contribuableId) {
        this.contribuableId = contribuableId;
    }
    
    public String getContribuableNom() {
        return contribuableNom;
    }
    
    public void setContribuableNom(String contribuableNom) {
        this.contribuableNom = contribuableNom;
    }
    
    public String getContribuableNRC() {
        return contribuableNRC;
    }
    
    public void setContribuableNRC(String contribuableNRC) {
        this.contribuableNRC = contribuableNRC;
    }
    
    public NoteTaxationDTO getNotePlaque() {
        return notePlaque;
    }
    
    public void setNotePlaque(NoteTaxationDTO notePlaque) {
        this.notePlaque = notePlaque;
    }
    
    public NoteTaxationDTO getNoteVignette() {
        return noteVignette;
    }
    
    public void setNoteVignette(NoteTaxationDTO noteVignette) {
        this.noteVignette = noteVignette;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public UUID getPlaqueId() {
        return plaqueId;
    }
    
    public void setPlaqueId(UUID plaqueId) {
        this.plaqueId = plaqueId;
    }
    
    public String getPlaqueNumero() {
        return plaqueNumero;
    }
    
    public void setPlaqueNumero(String plaqueNumero) {
        this.plaqueNumero = plaqueNumero;
    }
    
    public String getPlaqueStatut() {
        return plaqueStatut;
    }
    
    public void setPlaqueStatut(String plaqueStatut) {
        this.plaqueStatut = plaqueStatut;
    }
    
    public Date getPlaqueDateAttribution() {
        return plaqueDateAttribution;
    }
    
    public void setPlaqueDateAttribution(Date plaqueDateAttribution) {
        this.plaqueDateAttribution = plaqueDateAttribution;
    }
}
