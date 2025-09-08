package com.DPRIHKAT.entity;

import com.DPRIHKAT.entity.enums.StatutControle;
import com.DPRIHKAT.entity.enums.TypeControle;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "controles_fiscaux")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ControleFiscal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeControle type;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutControle statut;
    
    @Column(name = "date_creation")
    private Date dateCreation;
    
    @Column(name = "date_cloture")
    private Date dateCloture;
    
    @Column(name = "observations")
    private String observations;
    
    @Column(name = "justificatifs")
    private String justificatifs;
    
    @ManyToOne
    @JoinColumn(name = "declaration_id")
    private Declaration declaration;
    
    @ManyToOne
    @JoinColumn(name = "agent_initiateur_id")
    private Agent agentInitiateur;
    
    @ManyToOne
    @JoinColumn(name = "agent_validateur_id")
    private Agent agentValidateur;
    
    // Constructors
    public ControleFiscal() {}
    
    public ControleFiscal(TypeControle type, Declaration declaration, Agent agentInitiateur) {
        this.type = type;
        this.statut = StatutControle.EN_COURS;
        this.dateCreation = new Date();
        this.declaration = declaration;
        this.agentInitiateur = agentInitiateur;
    }
    
    // Getters and setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public TypeControle getType() {
        return type;
    }
    
    public void setType(TypeControle type) {
        this.type = type;
    }
    
    public StatutControle getStatut() {
        return statut;
    }
    
    public void setStatut(StatutControle statut) {
        this.statut = statut;
    }
    
    public Date getDateCreation() {
        return dateCreation;
    }
    
    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }
    
    public Date getDateCloture() {
        return dateCloture;
    }
    
    public void setDateCloture(Date dateCloture) {
        this.dateCloture = dateCloture;
    }
    
    public String getObservations() {
        return observations;
    }
    
    public void setObservations(String observations) {
        this.observations = observations;
    }
    
    public String getJustificatifs() {
        return justificatifs;
    }
    
    public void setJustificatifs(String justificatifs) {
        this.justificatifs = justificatifs;
    }
    
    public Declaration getDeclaration() {
        return declaration;
    }
    
    public void setDeclaration(Declaration declaration) {
        this.declaration = declaration;
    }
    
    public Agent getAgentInitiateur() {
        return agentInitiateur;
    }
    
    public void setAgentInitiateur(Agent agentInitiateur) {
        this.agentInitiateur = agentInitiateur;
    }
    
    public Agent getAgentValidateur() {
        return agentValidateur;
    }
    
    public void setAgentValidateur(Agent agentValidateur) {
        this.agentValidateur = agentValidateur;
    }
}
