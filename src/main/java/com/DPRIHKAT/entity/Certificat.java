package com.DPRIHKAT.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "certificat")
public class Certificat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String numero;

    @ManyToOne
    @JoinColumn(name = "agent_emetteur_id", nullable = false)
    private Utilisateur agentEmetteur;

    @Column(nullable = false)
    private LocalDateTime dateEmission;

    @Column(nullable = false)
    private LocalDateTime dateExpiration;

    private String contenu;

    @Column(nullable = false)
    private boolean valide = true;

    @ManyToOne
    @JoinColumn(name = "vehicule_id")
    private Vehicule vehicule;

    public Certificat() {
    }

    public Certificat(String numero, Utilisateur agentEmetteur, LocalDateTime dateEmission, LocalDateTime dateExpiration) {
        this.numero = numero;
        this.agentEmetteur = agentEmetteur;
        this.dateEmission = dateEmission;
        this.dateExpiration = dateExpiration;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Utilisateur getAgentEmetteur() {
        return agentEmetteur;
    }

    public void setAgentEmetteur(Utilisateur agentEmetteur) {
        this.agentEmetteur = agentEmetteur;
    }

    public LocalDateTime getDateEmission() {
        return dateEmission;
    }

    public void setDateEmission(LocalDateTime dateEmission) {
        this.dateEmission = dateEmission;
    }

    public LocalDateTime getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDateTime dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public boolean isValide() {
        return valide;
    }

    public void setValide(boolean valide) {
        this.valide = valide;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }
}
