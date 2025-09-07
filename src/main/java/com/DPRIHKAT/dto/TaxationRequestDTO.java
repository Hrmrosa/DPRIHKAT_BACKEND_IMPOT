package com.DPRIHKAT.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * DTO pour les requêtes de création de taxation
 * Version adaptée à la nouvelle architecture où la taxation est liée à une déclaration
 */
public class TaxationRequestDTO {

    @NotNull(message = "L'ID de la déclaration est obligatoire")
    private UUID declarationId;

    @NotNull(message = "L'ID de la nature d'impôt est obligatoire")
    private UUID natureImpotId;

    @NotNull(message = "L'exercice fiscal est obligatoire")
    private String exercice;

    public TaxationRequestDTO() {
    }

    public TaxationRequestDTO(UUID declarationId, UUID natureImpotId, String exercice) {
        this.declarationId = declarationId;
        this.natureImpotId = natureImpotId;
        this.exercice = exercice;
    }

    public UUID getDeclarationId() {
        return declarationId;
    }

    public void setDeclarationId(UUID declarationId) {
        this.declarationId = declarationId;
    }

    public UUID getNatureImpotId() {
        return natureImpotId;
    }

    public void setNatureImpotId(UUID natureImpotId) {
        this.natureImpotId = natureImpotId;
    }

    public String getExercice() {
        return exercice;
    }

    public void setExercice(String exercice) {
        this.exercice = exercice;
    }
}
