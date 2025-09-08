package com.DPRIHKAT.dto;

import com.DPRIHKAT.entity.Propriete;
import com.DPRIHKAT.entity.enums.TypeContribuable;
import java.util.List;
import java.util.UUID;

/**
 * DTO pour représenter les informations de base d'un contribuable
 */
public class ContribuableSimpleDTO {
    private UUID id;
    private String nom;
    private String adressePrincipale;
    private String adresseSecondaire;
    private String telephonePrincipal;
    private String telephoneSecondaire;
    private String email;
    private TypeContribuable type;
    private String idNat;
    private String nrc;
    private String sigle;
    private BiensPaginationDTO biens;

    public ContribuableSimpleDTO() {
    }

    public ContribuableSimpleDTO(UUID id, String nom, String adressePrincipale, String adresseSecondaire, String telephonePrincipal, String telephoneSecondaire, String email, TypeContribuable type, String idNat, String nrc, String sigle, BiensPaginationDTO biens) {
        this.id = id;
        this.nom = nom;
        this.adressePrincipale = adressePrincipale;
        this.adresseSecondaire = adresseSecondaire;
        this.telephonePrincipal = telephonePrincipal;
        this.telephoneSecondaire = telephoneSecondaire;
        this.email = email;
        this.type = type;
        this.idNat = idNat;
        this.nrc = nrc;
        this.sigle = sigle;
        this.biens = biens;
    }

    // Getters
    public UUID getId() { return id; }
    public String getNom() { return nom; }
    public String getAdressePrincipale() { return adressePrincipale; }
    public String getAdresseSecondaire() { return adresseSecondaire; }
    public String getTelephonePrincipal() { return telephonePrincipal; }
    public String getTelephoneSecondaire() { return telephoneSecondaire; }
    public String getEmail() { return email; }
    public TypeContribuable getType() { return type; }
    public String getIdNat() { return idNat; }
    public String getNrc() { return nrc; }
    public String getSigle() { return sigle; }
    public BiensPaginationDTO getBiens() { return biens; }

    // Setters
    public void setId(UUID id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setAdressePrincipale(String adressePrincipale) { this.adressePrincipale = adressePrincipale; }
    public void setAdresseSecondaire(String adresseSecondaire) { this.adresseSecondaire = adresseSecondaire; }
    public void setTelephonePrincipal(String telephonePrincipal) { this.telephonePrincipal = telephonePrincipal; }
    public void setTelephoneSecondaire(String telephoneSecondaire) { this.telephoneSecondaire = telephoneSecondaire; }
    public void setEmail(String email) { this.email = email; }
    public void setType(TypeContribuable type) { this.type = type; }
    public void setIdNat(String idNat) { this.idNat = idNat; }
    public void setNrc(String nrc) { this.nrc = nrc; }
    public void setSigle(String sigle) { this.sigle = sigle; }
    public void setBiens(BiensPaginationDTO biens) { this.biens = biens; }
}
