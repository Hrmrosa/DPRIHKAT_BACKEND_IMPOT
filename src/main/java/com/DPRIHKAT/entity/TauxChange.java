package com.DPRIHKAT.entity;

import com.DPRIHKAT.entity.enums.Devise;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Entité représentant un taux de change entre devises
 * Utilisé pour convertir les montants entre USD et CDF
 * 
 * @author amateur
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TauxChange {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEffective;
    
    private Double taux;
    
    @Enumerated(EnumType.STRING)
    private Devise deviseSource;
    
    @Enumerated(EnumType.STRING)
    private Devise deviseDestination;
    
    private boolean actif = true;
    
    // Agent qui a défini ce taux de change
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id")
    private Agent agent;
    
    public TauxChange() {
    }
    
    public TauxChange(Date dateEffective, Double taux, Devise deviseSource, Devise deviseDestination, Agent agent) {
        this.dateEffective = dateEffective;
        this.taux = taux;
        this.deviseSource = deviseSource;
        this.deviseDestination = deviseDestination;
        this.agent = agent;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDateEffective() {
        return dateEffective;
    }

    public void setDateEffective(Date dateEffective) {
        this.dateEffective = dateEffective;
    }

    public Double getTaux() {
        return taux;
    }

    public void setTaux(Double taux) {
        this.taux = taux;
    }

    public Devise getDeviseSource() {
        return deviseSource;
    }

    public void setDeviseSource(Devise deviseSource) {
        this.deviseSource = deviseSource;
    }

    public Devise getDeviseDestination() {
        return deviseDestination;
    }

    public void setDeviseDestination(Devise deviseDestination) {
        this.deviseDestination = deviseDestination;
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
}
