package com.DPRIHKAT.entity;

import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class HistoriqueDossier {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Date date;
    private String action;
    private String agent;

    @ManyToOne
    private DossierRecouvrement dossier;

    // Getters et Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getAgent() { return agent; }
    public void setAgent(String agent) { this.agent = agent; }

    public DossierRecouvrement getDossier() { return dossier; }
    public void setDossier(DossierRecouvrement dossier) { this.dossier = dossier; }
}
