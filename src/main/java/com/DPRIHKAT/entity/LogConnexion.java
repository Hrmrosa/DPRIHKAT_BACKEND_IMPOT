package com.DPRIHKAT.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "log_connexion")
public class LogConnexion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "utilisateur_id")
    private UUID utilisateurId;

    @Column(name = "login")
    private String login;

    @Column(name = "date_connexion", nullable = false)
    private LocalDateTime dateConnexion;

    @Column(name = "adresse_ip", nullable = false)
    private String adresseIp;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "reussi", nullable = false)
    private boolean reussi;

    @Column(name = "raison")
    private String raison;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(UUID utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public LocalDateTime getDateConnexion() {
        return dateConnexion;
    }

    public void setDateConnexion(LocalDateTime dateConnexion) {
        this.dateConnexion = dateConnexion;
    }

    public String getAdresseIp() {
        return adresseIp;
    }

    public void setAdresseIp(String adresseIp) {
        this.adresseIp = adresseIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public boolean isReussi() {
        return reussi;
    }

    public void setReussi(boolean reussi) {
        this.reussi = reussi;
    }

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }
}
