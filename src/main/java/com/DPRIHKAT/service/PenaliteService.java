package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Declaration;
import com.DPRIHKAT.entity.Penalite;
import com.DPRIHKAT.entity.enums.MotifPenalite;
import com.DPRIHKAT.repository.DeclarationRepository;
import com.DPRIHKAT.repository.PenaliteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PenaliteService {

    @Autowired
    private PenaliteRepository penaliteRepository;

    @Autowired
    private DeclarationRepository declarationRepository;

    /**
     * Calculate and apply penalties for a declaration
     */
    public void calculateAndApplyPenalties(UUID declarationId) {
        Declaration declaration = declarationRepository.findById(declarationId)
                .orElseThrow(() -> new RuntimeException("Déclaration non trouvée"));

        // If declaration date is null, consider as non-submission (25% penalty) and stop
        if (declaration.getDateDeclaration() == null) {
            // Utiliser le montant de la propriété associée à la déclaration
            double penaltyAmount = 0.0;
            if (declaration.getPropriete() != null) {
                penaltyAmount = declaration.getPropriete().getMontantImpot() * 0.25;
            }

            Penalite penalite = new Penalite();
            penalite.setDeclaration(declaration);
            penalite.setMontant(penaltyAmount);
            penalite.setMotif(MotifPenalite.OUBLI);
            penalite.setDateApplication(new Date());
            penalite.setJustification("Pénalité calculée automatiquement pour retard de déclaration");

            penaliteRepository.save(penalite);
            return;
        }

        // Check if declaration is already past due
        LocalDate dueDate = getDueDate(declaration);
        LocalDate currentDate = LocalDate.now();

        if (currentDate.isAfter(dueDate)) {
            // Calculate delay in months
            long monthsDelay = java.time.Period.between(dueDate, currentDate).toTotalMonths();

            if (monthsDelay > 0) {
                // 2% penalty per month for late payment
                double penaltyAmount = 0.0;
                if (declaration.getPropriete() != null) {
                    penaltyAmount = declaration.getPropriete().getMontantImpot() * 0.02 * monthsDelay;
                }

                Penalite penalite = new Penalite();
                penalite.setDeclaration(declaration);
                penalite.setMontant(penaltyAmount);
                penalite.setMotif(MotifPenalite.RETARD);
                penalite.setDateApplication(new Date());
                penalite.setJustification("Pénalité calculée automatiquement pour retard de déclaration");

                penaliteRepository.save(penalite);
            }
        }
    }

    /**
     * Get due date for a declaration based on its type
     */
    private LocalDate getDueDate(Declaration declaration) {
        // For most declarations, due date is February 1st of the following year
        LocalDate declarationDate = declaration.getDateDeclaration().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return LocalDate.of(declarationDate.getYear() + 1, 2, 1);
    }

    /**
     * Adjust penalty manually by authorized personnel
     */
    public Penalite adjustPenalty(UUID penaltyId, double newAmount, String justification) {
        Penalite penalite = penaliteRepository.findById(penaltyId)
                .orElseThrow(() -> new RuntimeException("Pénalité non trouvée"));
        
        penalite.setMontant(newAmount);
        penalite.setJustification(justification);
        
        return penaliteRepository.save(penalite);
    }

    /**
     * Create a manual penalty for a declaration
     */
    public Penalite createPenaliteManuelle(UUID declarationId, double montant, MotifPenalite motif, String justification) {
        Declaration declaration = declarationRepository.findById(declarationId)
                .orElseThrow(() -> new RuntimeException("Déclaration non trouvée"));

        Penalite penalite = new Penalite();
        penalite.setDeclaration(declaration);
        penalite.setMontant(montant);
        penalite.setMotif(motif);
        penalite.setDateApplication(new Date());
        penalite.setJustification(justification);
        
        return penaliteRepository.save(penalite);
    }

    /**
     * Get all penalties for a declaration
     */
    public List<Penalite> getPenaltiesForDeclaration(UUID declarationId) {
        return penaliteRepository.findByDeclarationId(declarationId);
    }

    /**
     * Get all penalties for a contribuable
     */
    public List<Penalite> getPenaltiesForContribuable(UUID contribuableId) {
        return penaliteRepository.findByDeclaration_Propriete_Proprietaire_Id(contribuableId);
    }
}
