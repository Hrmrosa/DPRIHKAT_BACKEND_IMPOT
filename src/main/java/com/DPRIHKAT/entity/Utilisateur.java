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
import java.util.Random;
import java.util.UUID;
import com.DPRIHKAT.entity.enums.Role;

/**
 * Entité représentant un utilisateur du système
 * Un utilisateur peut être associé à un agent
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
    
    private boolean actif = true; // Champ pour la suppression logique

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
        this.actif = true;
    }

    // Méthodes
    public void premierConnexion() {
        // TODO: Forcer changement mot de passe
        this.premierConnexion = false;
    }

    public void bloquerCompte() {
        this.bloque = true;
    }
    
    /**
     * Génère un login pour un contribuable (dpri_c + 4 caractères aléatoires)
     * @return le login généré
     */
    public static String genererLoginContribuable() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder("dpri_c");
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
    
    /**
     * Génère un mot de passe par défaut pour un contribuable (Tabc@123)
     * @return le mot de passe par défaut
     */
    public static String genererMotDePasseContribuable() {
        return "Tabc@123";
    }
    
    /**
     * Crée un utilisateur pour un contribuable avec un login et mot de passe générés automatiquement
     * @param contribuable le contribuable pour lequel créer un utilisateur
     * @return l'utilisateur créé
     */
    public static Utilisateur creerUtilisateurContribuable(Contribuable contribuable) {
        String login = genererLoginContribuable();
        String motDePasse = genererMotDePasseContribuable();
        
        Utilisateur utilisateur = new Utilisateur(login, motDePasse, Role.CONTRIBUABLE);
        utilisateur.setAgent(contribuable); // Le contribuable est un agent
        
        return utilisateur;
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
    
    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }
    
    /**
     * Vérifie si l'utilisateur est un contribuable
     * @return true si l'utilisateur est un contribuable, false sinon
     */
    public boolean isContribuable() {
        return this.role == Role.CONTRIBUABLE && this.agent instanceof Contribuable;
    }
    
    /**
     * Récupère le contribuable associé à cet utilisateur, s'il existe
     * @return le contribuable associé, ou null si l'utilisateur n'est pas un contribuable
     */
    public Contribuable getContribuable() {
        if (isContribuable()) {
            return (Contribuable) this.agent;
        }
        return null;
    }
}
