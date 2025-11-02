package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Certificat;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.enums.Role;
import com.DPRIHKAT.repository.CertificatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CertificatService {

    @Autowired
    private CertificatRepository certificatRepository;

    @Transactional
    public String emettreCertificat(Utilisateur agent) {
        if (!agent.getRole().equals(Role.RECEVEUR_DES_IMPOTS) && !agent.getRole().equals(Role.APUREUR)) {
            throw new IllegalStateException("Seuls les receveurs des impôts et les apureurs peuvent émettre des certificats");
        }

        // Génération d'un numéro unique pour le certificat
        String numeroCertificat = String.format("CERT-%s-%s",
                LocalDateTime.now().toString().replace(":", "-"),
                UUID.randomUUID().toString().substring(0, 8));

        // Création du certificat
        Certificat certificat = new Certificat(
            numeroCertificat,
            agent,
            LocalDateTime.now(),
            LocalDateTime.now().plusYears(1)  // Validité d'un an
        );

        // Sauvegarde dans la base de données
        certificatRepository.save(certificat);

        return numeroCertificat;
    }

    public boolean verifierCertificat(String numeroCertificat) {
        if (numeroCertificat == null || numeroCertificat.trim().isEmpty()) {
            return false;
        }

        return certificatRepository.findByNumero(numeroCertificat)
            .map(certificat -> 
                certificat.isValide() && 
                certificat.getDateExpiration().isAfter(LocalDateTime.now())
            )
            .orElse(false);
    }
}
