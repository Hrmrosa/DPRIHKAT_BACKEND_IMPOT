/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.DPRIHKAT.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.util.UUID;
import com.DPRIHKAT.entity.enums.Role;

/**
 *
 * @author amateur
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String login;

    @JsonIgnore
    private String motDePasse;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean premierConnexion;

    private boolean bloque;

    @OneToOne
    @JoinColumn(name = "contribuable_id", nullable = true)
    private Contribuable contribuable;

    @OneToOne
    @JoinColumn(name = "agent_id", nullable = true)
    private Agent agent;

    public Utilisateur() {
    }

    public Utilisateur(String login, String motDePasse, Role role) {
        this.login = login;
        this.motDePasse = motDePasse;
        this.role = role;
        this.premierConnexion = true;
        this.bloque = false;
    }

    // Méthodes
    public void premierConnexion() {
        // TODO: Forcer changement mot de passe
        this.premierConnexion = false;
    }

    public void bloquerCompte() {
        this.bloque = true;
    }

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isPremierConnexion() {
        return premierConnexion;
    }

    public void setPremierConnexion(boolean premierConnexion) {
        this.premierConnexion = premierConnexion;
    }

    public boolean isBloque() {
        return bloque;
    }

    public void setBloque(boolean bloque) {
        this.bloque = bloque;
    }

    public Contribuable getContribuable() {
        return contribuable;
    }

    public void setContribuable(Contribuable contribuable) {
        this.contribuable = contribuable;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }
}
