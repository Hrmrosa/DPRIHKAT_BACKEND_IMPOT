package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Declaration;
import com.DPRIHKAT.entity.Paiement;
import com.DPRIHKAT.entity.Taxation;
import com.DPRIHKAT.entity.enums.StatutPaiement;
import com.DPRIHKAT.repository.DeclarationRepository;
import com.DPRIHKAT.repository.PaiementRepository;
import com.DPRIHKAT.repository.TaxationRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TaxationRepository taxationRepository;

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
        // Dans la nouvelle architecture, les paiements sont liés aux taxations plutôt qu'aux déclarations
        // Nous allons lier le paiement à la taxation lors de la sauvegarde

        // Persist payment first to generate ID
        Paiement savedPaiement = paiementRepository.save(paiement);

        // Link payment to taxation (Taxation is owning side in new architecture)
        // Find the taxation associated with the declaration
        List<Taxation> taxations = taxationRepository.findByDeclarationAndActifTrue(declaration);
        if (!taxations.isEmpty()) {
            Taxation taxation = taxations.get(0); // Use the first taxation found
            taxation.setPaiement(savedPaiement); // Link taxation to payment
            taxationRepository.save(taxation);
            savedPaiement.setTaxation(taxation); // Link payment to taxation
        }

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
}
