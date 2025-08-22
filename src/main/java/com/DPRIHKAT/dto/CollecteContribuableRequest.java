package com.DPRIHKAT.dto;

import java.util.List;

public class CollecteContribuableRequest {
    private String nom;
    private String adressePrincipale;
    private String adresseSecondaire;
    private String telephonePrincipal;
    private String telephoneSecondaire;
    private String email;
    private String nationalite;
    private String type; // TypeContribuable enum name
    private String idNat;
    private String nrc;
    private String sigle;
    private String numeroIdentificationContribuable;
    private List<BienDTO> biens;

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getAdressePrincipale() { return adressePrincipale; }
    public void setAdressePrincipale(String adressePrincipale) { this.adressePrincipale = adressePrincipale; }

    public String getAdresseSecondaire() { return adresseSecondaire; }
    public void setAdresseSecondaire(String adresseSecondaire) { this.adresseSecondaire = adresseSecondaire; }

    public String getTelephonePrincipal() { return telephonePrincipal; }
    public void setTelephonePrincipal(String telephonePrincipal) { this.telephonePrincipal = telephonePrincipal; }

    public String getTelephoneSecondaire() { return telephoneSecondaire; }
    public void setTelephoneSecondaire(String telephoneSecondaire) { this.telephoneSecondaire = telephoneSecondaire; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNationalite() { return nationalite; }
    public void setNationalite(String nationalite) { this.nationalite = nationalite; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getIdNat() { return idNat; }
    public void setIdNat(String idNat) { this.idNat = idNat; }

    public String getNrc() { return nrc; }
    public void setNrc(String nrc) { this.nrc = nrc; }

    public String getSigle() { return sigle; }
    public void setSigle(String sigle) { this.sigle = sigle; }

    public String getNumeroIdentificationContribuable() { return numeroIdentificationContribuable; }
    public void setNumeroIdentificationContribuable(String numeroIdentificationContribuable) { this.numeroIdentificationContribuable = numeroIdentificationContribuable; }

    public List<BienDTO> getBiens() { return biens; }
    public void setBiens(List<BienDTO> biens) { this.biens = biens; }
}
