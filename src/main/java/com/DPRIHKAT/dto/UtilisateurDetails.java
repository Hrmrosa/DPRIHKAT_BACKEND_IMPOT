package com.DPRIHKAT.dto;

import com.DPRIHKAT.entity.Agent;
import com.DPRIHKAT.entity.Contribuable;
import com.DPRIHKAT.entity.enums.Role;

import java.util.UUID;

/**
 * DTO contenant les détails d'un utilisateur
 * Utilisé pour les mises à jour et les transferts de données
 * 
 * @author amateur
 */
public class UtilisateurDetails {
    
    private UUID id;
    private String login;
    private Role role;
    private boolean premierConnexion;
    private boolean bloque;
    private boolean actif;
    private Contribuable contribuable;
    private Agent agent;

    public UtilisateurDetails() {
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

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
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
