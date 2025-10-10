package com.DPRIHKAT.entity;

import com.DPRIHKAT.entity.enums.Role;
import com.DPRIHKAT.entity.enums.Sexe;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.UUID;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Utilisateur {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank(message = "Le nom complet est obligatoire")
    private String nomComplet;
    
    @NotNull(message = "Le sexe est obligatoire")
    @Enumerated(EnumType.STRING)
    private Sexe sexe;
    
    @NotBlank(message = "Le grade est obligatoire")
    private String grade;
    
    @NotBlank(message = "Le matricule est obligatoire")
    private String matricule;
    
    @Email(message = "L'email doit être valide")
    private String email;

    @NotBlank(message = "Le login est obligatoire")
    private String login;

    @JsonIgnore
    private String motDePasse;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean premierConnexion;
    private boolean bloque;
    private boolean actif = true;

    // Champs optionnels
    private String adresse;
    private String telephone;

    @OneToOne
    @JoinColumn(name = "agent_id", nullable = true)
    private Agent agent;

    @OneToOne
    @JoinColumn(name = "contribuable_id", nullable = true)
    private Contribuable contribuable;

    // Getters/Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getNomComplet() { return nomComplet; }
    public void setNomComplet(String nomComplet) { this.nomComplet = nomComplet; }
    
    public Sexe getSexe() { return sexe; }
    public void setSexe(Sexe sexe) { this.sexe = sexe; }
    
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    
    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    
    public boolean isPremierConnexion() { return premierConnexion; }
    public void setPremierConnexion(boolean premierConnexion) { this.premierConnexion = premierConnexion; }
    
    public boolean isBloque() { return bloque; }
    public void setBloque(boolean bloque) { this.bloque = bloque; }
    
    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
    
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public Agent getAgent() { return agent; }
    public void setAgent(Agent agent) { this.agent = agent; }
    
    public Contribuable getContribuable() { return contribuable; }
    public void setContribuable(Contribuable contribuable) { this.contribuable = contribuable; }

    // Méthodes métier
    public boolean isContribuable() {
        return this.role == Role.CONTRIBUABLE && this.contribuable != null;
    }

    public boolean hasAdminRole() {
        return this.role == Role.ADMIN || 
               this.role == Role.DIRECTEUR || 
               this.role == Role.INFORMATICIEN;
    }

    public Bureau getBureau() {
        return this.agent != null ? this.agent.getBureau() : null;
    }
}
