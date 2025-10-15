package com.DPRIHKAT.entity;

import com.DPRIHKAT.entity.enums.ModePaiement;
import com.DPRIHKAT.entity.enums.StatutPaiement;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Entité représentant un paiement effectué par un contribuable
 * 
 * @author amateur
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Double montant;

    private Date date;

    @Enumerated(EnumType.STRING)
    private StatutPaiement statut;

    @Enumerated(EnumType.STRING)
    private ModePaiement mode;

    private String bordereauBancaire;

    private String numeroCompte;

    private String nomBanque;

    private String codeQR;

    private boolean actif = true;

    @ManyToOne
    @JoinColumn(name = "contribuable_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Contribuable contribuable;

    @OneToMany(mappedBy = "paiement")
    private List<Declaration> declarations = new ArrayList<>();

    @OneToMany(mappedBy = "paiement")
    private List<Penalite> penalites = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "dossier_recouvrement_id")
    private DossierRecouvrement dossier;

    @ManyToOne
    @JoinColumn(name = "taxation_id")
    private Taxation taxation;

    @OneToMany(mappedBy = "paiement")
    private List<Declaration> declaration = new ArrayList<>();

    public Paiement() {
    }

    public Paiement(Double montant, Date date, StatutPaiement statut, ModePaiement mode) {
        this.montant = montant;
        this.date = date;
        this.statut = statut;
        this.mode = mode;
        this.actif = true;
    }

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public StatutPaiement getStatut() {
        return statut;
    }

    public void setStatut(StatutPaiement statut) {
        this.statut = statut;
    }

    public ModePaiement getMode() {
        return mode;
    }

    public void setMode(ModePaiement mode) {
        this.mode = mode;
    }

    public String getBordereauBancaire() {
        return bordereauBancaire;
    }

    public void setBordereauBancaire(String bordereauBancaire) {
        this.bordereauBancaire = bordereauBancaire;
    }

    public String getNumeroCompte() {
        return numeroCompte;
    }

    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    public String getNomBanque() {
        return nomBanque;
    }

    public void setNomBanque(String nomBanque) {
        this.nomBanque = nomBanque;
    }

    public String getCodeQR() {
        return codeQR;
    }

    public void setCodeQR(String codeQR) {
        this.codeQR = codeQR;
    }

    public Contribuable getContribuable() {
        return contribuable;
    }

    public void setContribuable(Contribuable contribuable) {
        this.contribuable = contribuable;
    }

    public List<Declaration> getDeclarations() {
        return declarations;
    }

    public void setDeclarations(List<Declaration> declarations) {
        this.declarations = declarations;
    }

    public List<Penalite> getPenalites() {
        return penalites;
    }

    public void setPenalites(List<Penalite> penalites) {
        this.penalites = penalites;
    }

    public DossierRecouvrement getDossier() {
        return dossier;
    }

    public void setDossier(DossierRecouvrement dossier) {
        this.dossier = dossier;
    }

    /**
     * Alias pour getDate pour compatibilité avec le service CollecteService
     *
     * @return La date du paiement
     */
    public Date getDatePaiement() {
        return this.date;
    }

    /**
     * Récupère la référence du paiement (bordereauBancaire)
     *
     * @return La référence du paiement
     */
    public String getReference() {
        return this.bordereauBancaire;
    }

    /**
     * Récupère la taxation associée à ce paiement
     *
     * @return La taxation associée
     */
    public Taxation getTaxation() {
        return this.taxation;
    }

    /**
     * Définit la taxation associée à ce paiement
     *
     * @param taxation La taxation à associer
     */
    public void setTaxation(Taxation taxation) {
        this.taxation = taxation;
    }

    /**
     * Vérifie si le paiement est actif
     *
     * @return true si le paiement est actif, false sinon
     */
    public boolean isActif() {
        return this.actif;
    }

    /**
     * Définit si le paiement est actif
     *
     * @param actif true si le paiement est actif, false sinon
     */
    public void setActif(boolean actif) {
        this.actif = actif;
    }

    /**
     * Récupère la première déclaration associée à ce paiement
     *
     * @return La première déclaration associée
     */
    public Declaration getDeclaration() {
        if (this.declarations != null && !this.declarations.isEmpty()) {
            return this.declarations.get(0);
        }
        return null;
    }

    /**
     * Définit la déclaration associée à ce paiement
     *
     * @param declaration La déclaration à associer
     */
    public void setDeclaration(Declaration declaration) {
        if (this.declarations == null) {
            this.declarations = new ArrayList<>();
        }
        this.declarations.add(declaration);
    }
}
