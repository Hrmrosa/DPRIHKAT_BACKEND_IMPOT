package com.DPRIHKAT.entity;

import com.DPRIHKAT.entity.enums.ModePaiement;
import com.DPRIHKAT.entity.enums.Role;
import com.DPRIHKAT.entity.enums.Sexe;
import com.DPRIHKAT.entity.enums.StatutDeclaration;
import com.DPRIHKAT.entity.enums.StatutPaiement;
import com.DPRIHKAT.entity.enums.TypeContribuable;
import com.DPRIHKAT.entity.enums.TypeImpot;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * Entité représentant un contribuable
 * Un contribuable est également un agent dans le système
 * 
 * @author amateur
 */
@Entity
@Table(name = "contribuable")
@PrimaryKeyJoinColumn(name = "agent_id")
@DiscriminatorValue("CONTRIBUABLE")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Contribuable extends Agent {

    private String adressePrincipale;

    private String adresseSecondaire;

    private String telephonePrincipal;

    private String telephoneSecondaire;

    private String email;

    private String nationalite;

    @Enumerated(EnumType.STRING)
    private TypeContribuable type;

    private String idNat;

    @JsonProperty("NRC")
    private String NRC;

    private String sigle;

    private String numeroIdentificationContribuable;
    
    private boolean actif = true; // Champ pour la suppression logique
    
    private String codeQR;

    // Désactivé temporairement pour résoudre l'erreur ArrayIndexOutOfBoundsException
    /*
    @OneToMany(mappedBy = "proprietaire", fetch = FetchType.LAZY)
    private List<Propriete> proprietes = new ArrayList<>();

    @OneToOne(mappedBy = "contribuable", fetch = FetchType.LAZY)
    private DossierRecouvrement dossierRecouvrement;

    @OneToOne(mappedBy = "agent", fetch = FetchType.LAZY)
    private Utilisateur utilisateur;
    
    // Déclarations faites par ce contribuable
    @OneToMany(mappedBy = "contribuable", fetch = FetchType.LAZY)
    private List<Declaration> declarations = new ArrayList<>();
    */
    
    // Taxations associées à ce contribuable
    @OneToMany(mappedBy = "contribuable", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Taxation> taxations = new ArrayList<>();
    
    // Concessions minières de ce contribuable
    @OneToMany(mappedBy = "titulaire", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ConcessionMinier> concessions = new ArrayList<>();

    public Contribuable() {
        super();
    }

    public Contribuable(String nom, String adressePrincipale, String adresseSecondaire, String telephonePrincipal, String telephoneSecondaire, String email, String nationalite, TypeContribuable type, String idNat, String NRC, String sigle, String numeroIdentificationContribuable) {
        super(nom, Sexe.M, "CONTRIB-" + numeroIdentificationContribuable, null);
        this.adressePrincipale = adressePrincipale;
        this.adresseSecondaire = adresseSecondaire;
        this.telephonePrincipal = telephonePrincipal;
        this.telephoneSecondaire = telephoneSecondaire;
        this.email = email;
        this.nationalite = nationalite;
        this.type = type;
        this.idNat = idNat;
        this.NRC = NRC;
        this.sigle = sigle;
        this.numeroIdentificationContribuable = numeroIdentificationContribuable;
        this.actif = true;
    }

    public void declarerImpôtEnLigne() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if (month != Calendar.JANUARY || (month == Calendar.JANUARY && day < 2) || month == Calendar.FEBRUARY && day > 1) {
            throw new IllegalStateException("La déclaration en ligne est autorisée uniquement du 2 janvier au 1er février.");
        }
        if (utilisateur == null || !List.of(Role.CONTRIBUABLE).contains(utilisateur.getRole())) {
            throw new IllegalStateException("Seuls les contribuables avec le rôle CONTRIBUTOR peuvent déclarer en ligne.");
        }
        // Logique de déclaration en ligne
    }

    // Getters et Setters
    public String getAdressePrincipale() {
        return adressePrincipale;
    }

    public void setAdressePrincipale(String adressePrincipale) {
        this.adressePrincipale = adressePrincipale;
    }

    public String getAdresseSecondaire() {
        return adresseSecondaire;
    }

    public void setAdresseSecondaire(String adresseSecondaire) {
        this.adresseSecondaire = adresseSecondaire;
    }

    public String getTelephonePrincipal() {
        return telephonePrincipal;
    }

    public void setTelephonePrincipal(String telephonePrincipal) {
        this.telephonePrincipal = telephonePrincipal;
    }

    public String getTelephoneSecondaire() {
        return telephoneSecondaire;
    }

    public void setTelephoneSecondaire(String telephoneSecondaire) {
        this.telephoneSecondaire = telephoneSecondaire;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public TypeContribuable getType() {
        return type;
    }

    public void setType(TypeContribuable type) {
        this.type = type;
    }

    public String getIdNat() {
        return idNat;
    }

    public void setIdNat(String idNat) {
        this.idNat = idNat;
    }

    public String getNRC() {
        return NRC;
    }

    public void setNRC(String NRC) {
        this.NRC = NRC;
    }

    public String getSigle() {
        return sigle;
    }

    public void setSigle(String sigle) {
        this.sigle = sigle;
    }

    public String getNumeroIdentificationContribuable() {
        return numeroIdentificationContribuable;
    }

    public void setNumeroIdentificationContribuable(String numeroIdentificationContribuable) {
        this.numeroIdentificationContribuable = numeroIdentificationContribuable;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public String getCodeQR() {
        return codeQR;
    }

    public void setCodeQR(String codeQR) {
        this.codeQR = codeQR;
    }

    @Override
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    @Override
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
    
    public List<Taxation> getTaxations() {
        return taxations;
    }

    public void setTaxations(List<Taxation> taxations) {
        this.taxations = taxations;
    }
    
    public List<ConcessionMinier> getConcessions() {
        return concessions;
    }

    public void setConcessions(List<ConcessionMinier> concessions) {
        this.concessions = concessions;
    }
}
