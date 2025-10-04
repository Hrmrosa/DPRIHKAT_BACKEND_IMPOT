package com.DPRIHKAT.entity;

import com.DPRIHKAT.entity.enums.TypeImpot;
import jakarta.persistence.*;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Impot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private TypeImpot type;
    
    private Double taux;
    private String description;

    // Getters et Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public TypeImpot getType() { return type; }
    public void setType(TypeImpot type) { this.type = type; }

    public Double getTaux() { return taux; }
    public void setTaux(Double taux) { this.taux = taux; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
