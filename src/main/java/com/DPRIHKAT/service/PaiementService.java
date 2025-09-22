package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Declaration;
import com.DPRIHKAT.entity.Paiement;
import com.DPRIHKAT.entity.enums.StatutPaiement;
import com.DPRIHKAT.repository.DeclarationRepository;
import com.DPRIHKAT.repository.PaiementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PaiementService {

    @Autowired
    private PaiementRepository paiementRepository;

    @Autowired
    private DeclarationRepository declarationRepository;

    /**
     * Process a payment for a declaration
     */
    public Paiement processPayment(UUID declarationId, String bordereauBancaire, double montant) {
        Declaration declaration = declarationRepository.findById(declarationId)
                .orElseThrow(() -> new RuntimeException("Déclaration non trouvée"));

        Paiement paiement = new Paiement();
        paiement.setBordereauBancaire(bordereauBancaire);
        paiement.setMontant(montant);
        paiement.setDate(new Date());
        paiement.setStatut(StatutPaiement.VALIDE);

        // Persist payment first to generate ID
        Paiement savedPaiement = paiementRepository.save(paiement);

        // Link payment to declaration (Declaration is owning side)
        savedPaiement.setDeclaration(declaration);
        declaration.setPaiement(savedPaiement);
        declarationRepository.save(declaration);

        return savedPaiement;
    }

    /**
     * Get payment by declaration ID
     */
    public Paiement getPaymentByDeclarationId(UUID declarationId) {
        return paiementRepository.findByDeclarationId(declarationId);
    }

    /**
     * Get all payments
     */
    public List<Paiement> getAllPayments() {
        return paiementRepository.findAll();
    }

    /**
     * Get payments by status
     */
    public List<Paiement> getPaymentsByStatus(StatutPaiement statut) {
        return paiementRepository.findByStatut(statut);
    }
    
    /**
     * Get paginated payments with optional filters
     */
    public Page<Paiement> getPaginatedPayments(
            StatutPaiement statut,
            Date startDate,
            Date endDate,
            Pageable pageable) {
        
        // Utilisation de la méthode corrigée avec des booléens pour indiquer la présence des dates
        boolean hasStartDate = startDate != null;
        boolean hasEndDate = endDate != null;
        
        // Utiliser des dates par défaut si null pour éviter les erreurs SQL
        Date safeStartDate = startDate != null ? startDate : new Date(0); // 1970-01-01
        Date safeEndDate = endDate != null ? endDate : new Date(); // Date actuelle
        
        return paiementRepository.findPaiementsWithFilters(
                statut, 
                hasStartDate, safeStartDate, 
                hasEndDate, safeEndDate, 
                pageable);
    }
}
