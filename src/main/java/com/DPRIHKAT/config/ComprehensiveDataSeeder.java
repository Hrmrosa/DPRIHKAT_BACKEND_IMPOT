package com.DPRIHKAT.config;

import com.DPRIHKAT.entity.*;
import com.DPRIHKAT.entity.enums.Role;
import com.DPRIHKAT.entity.enums.Sexe;
import com.DPRIHKAT.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class ComprehensiveDataSeeder implements CommandLineRunner {

    @Autowired
    private CertificatRepository certificatRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (isDataAlreadySeeded()) {
            System.out.println("ComprehensiveDataSeeder: données déjà présentes, on saute le seeding.");
            return;
        }

        // Créer et récupérer l'admin dans une transaction séparée
        Utilisateur admin = createAdminUser();
        // Attendre que la transaction soit commitée
        admin = utilisateurRepository.findById(admin.getId()).orElseThrow();
        // Créer les certificats avec l'admin récupéré
        createCertificats(admin);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Utilisateur createAdminUser() {
        Utilisateur admin = new Utilisateur();
        admin.setNomComplet("Administrateur Système");
        admin.setSexe(Sexe.M);
        admin.setGrade("Admin");
        admin.setMatricule("ADMIN001");
        admin.setEmail("admin@dprihkat.cd");
        admin.setLogin("admin");
        admin.setMotDePasse(passwordEncoder.encode("admin123"));
        admin.setRole(Role.ADMIN);
        
        return utilisateurRepository.save(admin);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createCertificats(Utilisateur agentEmetteur) {
        // Pour chaque certificat à créer
        Certificat certificat = new Certificat();
        certificat.setNumero("CERT-" + UUID.randomUUID().toString().substring(0, 8));
        certificat.setDateEmission(LocalDateTime.now());
        certificat.setDateExpiration(LocalDateTime.now().plusYears(1));
        certificat.setValide(true);
        certificat.setContenu("Contenu du certificat");
        certificat.setAgentEmetteur(agentEmetteur);

        certificatRepository.save(certificat);
    }

    private boolean isDataAlreadySeeded() {
        // Vérifiez si les données existent déjà
        return certificatRepository.count() > 0 || utilisateurRepository.count() > 0;
    }
}
