package com.DPRIHKAT.config;

import com.DPRIHKAT.entity.*;
import com.DPRIHKAT.entity.enums.*;
import com.DPRIHKAT.repository.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Seeder simplifié qui évite les problèmes d'héritage
 * Crée des données minimales mais fonctionnelles
 */
@Component
@Order(4)
public class FixedDataSeeder implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final AgentRepository agentRepository;
    private final BureauRepository bureauRepository;
    private final DivisionRepository divisionRepository;
    private final ContribuableRepository contribuableRepository;
    private final ProprieteRepository proprieteRepository;
    private final DeclarationRepository declarationRepository;
    private final TaxationRepository taxationRepository;
    private final PasswordEncoder passwordEncoder;

    public FixedDataSeeder(
            UtilisateurRepository utilisateurRepository,
            AgentRepository agentRepository,
            BureauRepository bureauRepository,
            DivisionRepository divisionRepository,
            ContribuableRepository contribuableRepository,
            ProprieteRepository proprieteRepository,
            DeclarationRepository declarationRepository,
            TaxationRepository taxationRepository,
            PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.agentRepository = agentRepository;
        this.bureauRepository = bureauRepository;
        this.divisionRepository = divisionRepository;
        this.contribuableRepository = contribuableRepository;
        this.proprieteRepository = proprieteRepository;
        this.declarationRepository = declarationRepository;
        this.taxationRepository = taxationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Vérifier si des données existent déjà
        if (utilisateurRepository.count() > 0) {
            System.out.println("FixedDataSeeder: données déjà présentes, on saute le seeding.");
            return;
        }

        try {
            System.out.println("FixedDataSeeder: démarrage du seeding...");

            // 1. Créer une division
            Division division = new Division();
            division.setNom("Division Principale");
            division.setCode("DP");
            divisionRepository.save(division);

            // 2. Créer un bureau
            Bureau bureau = new Bureau();
            bureau.setNom("Bureau Principal");
            bureau.setCode("BP");
            bureau.setDivision(division);
            bureauRepository.save(bureau);

            // 3. Créer un agent administrateur
            Agent admin = new Agent();
            admin.setNom("Administrateur Système");
            admin.setSexe(Sexe.M);
            admin.setMatricule("ADMIN-001");
            admin.setBureau(bureau);
            agentRepository.save(admin);

            // 4. Créer un utilisateur administrateur
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setLogin("admin");
            utilisateur.setMotDePasse(passwordEncoder.encode("Tabc@123"));
            utilisateur.setRole(Role.ADMIN);
            utilisateur.setPremierConnexion(false);
            utilisateur.setBloque(false);
            utilisateur.setActif(true);
            utilisateur.setAgent(admin);
            utilisateurRepository.save(utilisateur);

            // 5. Créer un agent taxateur
            Agent taxateur = new Agent();
            taxateur.setNom("Taxateur Principal");
            taxateur.setSexe(Sexe.M);
            taxateur.setMatricule("TAX-001");
            taxateur.setBureau(bureau);
            agentRepository.save(taxateur);

            // 6. Créer un utilisateur taxateur
            Utilisateur userTaxateur = new Utilisateur();
            userTaxateur.setLogin("taxateur");
            userTaxateur.setMotDePasse(passwordEncoder.encode("Tabc@123"));
            userTaxateur.setRole(Role.TAXATEUR);
            userTaxateur.setPremierConnexion(false);
            userTaxateur.setBloque(false);
            userTaxateur.setActif(true);
            userTaxateur.setAgent(taxateur);
            utilisateurRepository.save(userTaxateur);
            
            // 7. Créer un agent receveur
            Agent receveur = new Agent();
            receveur.setNom("Receveur Principal");
            receveur.setSexe(Sexe.F);
            receveur.setMatricule("REC-001");
            receveur.setBureau(bureau);
            agentRepository.save(receveur);
            
            // 8. Créer un utilisateur receveur
            Utilisateur userReceveur = new Utilisateur();
            userReceveur.setLogin("receveur");
            userReceveur.setMotDePasse(passwordEncoder.encode("Tabc@123"));
            userReceveur.setRole(Role.RECEVEUR_DES_IMPOTS);
            userReceveur.setPremierConnexion(false);
            userReceveur.setBloque(false);
            userReceveur.setActif(true);
            userReceveur.setAgent(receveur);
            utilisateurRepository.save(userReceveur);
            
            // 9. Créer un agent contribuable (personne physique)
            Agent agentContribuable = new Agent();
            agentContribuable.setNom("Dupont Jean");
            agentContribuable.setSexe(Sexe.M);
            agentContribuable.setMatricule("CONT-001");
            agentContribuable.setBureau(bureau);
            agentRepository.save(agentContribuable);
            
            // 10. Créer un utilisateur pour le contribuable
            Utilisateur userContribuable = new Utilisateur();
            userContribuable.setLogin("contrib");
            userContribuable.setMotDePasse(passwordEncoder.encode("Tabc@123"));
            userContribuable.setRole(Role.CONTRIBUABLE);
            userContribuable.setPremierConnexion(false);
            userContribuable.setBloque(false);
            userContribuable.setActif(true);
            userContribuable.setAgent(agentContribuable);
            utilisateurRepository.save(userContribuable);
            
            // 11. Créer un agent contribuable (personne morale)
            Agent agentEntreprise = new Agent();
            agentEntreprise.setNom("Société Minière KATANGA SA");
            agentEntreprise.setSexe(Sexe.M); // Par défaut pour les personnes morales
            agentEntreprise.setMatricule("CONT-002");
            agentEntreprise.setBureau(bureau);
            agentRepository.save(agentEntreprise);
            
            // 12. Créer un utilisateur pour l'entreprise
            Utilisateur userEntreprise = new Utilisateur();
            userEntreprise.setLogin("entreprise");
            userEntreprise.setMotDePasse(passwordEncoder.encode("Tabc@123"));
            userEntreprise.setRole(Role.CONTRIBUABLE);
            userEntreprise.setPremierConnexion(false);
            userEntreprise.setBloque(false);
            userEntreprise.setActif(true);
            userEntreprise.setAgent(agentEntreprise);
            utilisateurRepository.save(userEntreprise);

            System.out.println("FixedDataSeeder: seeding terminé avec succès.");
        } catch (Exception e) {
            System.err.println("FixedDataSeeder: erreur lors du seeding: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
