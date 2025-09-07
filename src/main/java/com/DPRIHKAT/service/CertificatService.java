package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Certificat;
import com.DPRIHKAT.entity.Declaration;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.Vehicule;
import com.DPRIHKAT.entity.enums.StatutCertificat;
import com.DPRIHKAT.repository.CertificatRepository;
import com.DPRIHKAT.repository.DeclarationRepository;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.DPRIHKAT.repository.VehiculeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CertificatService {

    @Autowired
    private CertificatRepository certificatRepository;

    @Autowired
    private DeclarationRepository declarationRepository;

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    /**
     * Generate a certificate based on a property declaration
     */
    public Certificat generatePropertyCertificate(UUID declarationId, UUID userId) {
        Declaration declaration = declarationRepository.findById(declarationId)
                .orElseThrow(() -> new RuntimeException("Déclaration non trouvée"));

        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Certificat certificat = new Certificat();
        certificat.setNumero(generateCertificateNumber());
        certificat.setDateEmission(new Date());
        // Utiliser le montant de la propriété associée à la déclaration
        if (declaration.getPropriete() != null) {
            certificat.setMontant(declaration.getPropriete().getMontantImpot());
        } else {
            certificat.setMontant(0.0); // Montant par défaut si impossible de déterminer
        }
        certificat.setStatut(StatutCertificat.ACTIF);
        certificat.setActif(true);
        certificat.setDeclaration(declaration);
        certificat.setAgent(utilisateur.getAgent());

        return certificatRepository.save(certificat);
    }

    /**
     * Generate a certificate for a vehicle (without a specific declaration)
     */
    public Certificat generateVehicleCertificate(UUID vehiculeId, UUID userId) {
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new RuntimeException("Véhicule non trouvé"));

        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Certificat certificat = new Certificat();
        certificat.setNumero(generateCertificateNumber());
        certificat.setDateEmission(new Date());
        certificat.setMontant(0.0);
        certificat.setStatut(StatutCertificat.ACTIF);
        certificat.setActif(true);
        certificat.setVehicule(vehicule);
        certificat.setAgent(utilisateur.getAgent());

        return certificatRepository.save(certificat);
    }

    /**
     * Get certificate by ID
     */
    public Certificat getCertificatById(UUID id) {
        return certificatRepository.findById(id).orElse(null);
    }

    /**
     * Get certificates by vehicle
     */
    public List<Certificat> getCertificatsByVehicle(UUID vehiculeId) {
        return certificatRepository.findByVehiculeId(vehiculeId);
    }

    /**
     * Get certificates by declaration
     */
    public List<Certificat> getCertificatsByDeclaration(UUID declarationId) {
        return certificatRepository.findByDeclarationId(declarationId);
    }

    /**
     * Get all certificates
     */
    public List<Certificat> findAll() {
        return certificatRepository.findAll();
    }

    /**
     * Get active certificates
     */
    public List<Certificat> getActiveCertificats() {
        return certificatRepository.findByActifTrue();
    }

    /**
     * Get expired certificates
     */
    public List<Certificat> getExpiredCertificats() {
        return certificatRepository.findByDateExpirationBefore(new Date());
    }

    /**
     * Renew a certificate
     */
    public Certificat renewCertificate(UUID id, UUID userId) {
        Certificat certificat = certificatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificat non trouvé"));

        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Create new certificate
        Certificat newCertificat = new Certificat();
        newCertificat.setNumero(generateCertificateNumber());
        newCertificat.setDateEmission(new Date());
        newCertificat.setMontant(certificat.getMontant());
        newCertificat.setStatut(StatutCertificat.ACTIF);
        newCertificat.setDeclaration(certificat.getDeclaration());
        newCertificat.setVehicule(certificat.getVehicule());
        newCertificat.setAgent(utilisateur.getAgent());

        // Expire the old certificate
        certificat.setStatut(StatutCertificat.EXPIRE);
        certificatRepository.save(certificat);

        return certificatRepository.save(newCertificat);
    }

    /**
     * Cancel a certificate
     */
    public Certificat cancelCertificate(UUID id, UUID userId, String motif) {
        Certificat certificat = certificatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificat non trouvé"));

        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        certificat.setStatut(StatutCertificat.ANNULE);
        certificat.setAgent(utilisateur.getAgent());
        certificat.setMotifAnnulation(motif);

        return certificatRepository.save(certificat);
    }

    /**
     * Save certificate
     */
    public Certificat save(Certificat certificat) {
        return certificatRepository.save(certificat);
    }

    /**
     * Update certificate
     */
    public Certificat update(UUID id, Certificat certificat) {
        if (certificatRepository.existsById(id)) {
            certificat.setId(id);
            return certificatRepository.save(certificat);
        }
        return null;
    }

    /**
     * Delete certificate
     */
    public void deleteById(UUID id) {
        certificatRepository.deleteById(id);
    }

    /**
     * Generate a unique certificate number
     */
    private String generateCertificateNumber() {
        return "CERT-" + System.currentTimeMillis();
    }
}
