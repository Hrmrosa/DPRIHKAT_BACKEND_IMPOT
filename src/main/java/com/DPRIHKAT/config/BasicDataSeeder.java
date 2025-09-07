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

import java.util.Calendar;
import java.util.Date;

/**
 * Seeder minimal pour créer les données essentielles de l'application
 * Évite les problèmes d'héritage tout en créant les données nécessaires
 * 
 * DÉSACTIVÉ en raison de problèmes d'héritage persistants
 * Remplacé par SimpleDataSeeder
 */
// @Component - Désactivé pour éviter les problèmes d'héritage
@Order(5) // Exécuté avant tout autre seeder
public class BasicDataSeeder implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final AgentRepository agentRepository;
    private final BureauRepository bureauRepository;
    private final DivisionRepository divisionRepository;
    private final ProprieteRepository proprieteRepository;
    private final ConcessionMinierRepository concessionMinierRepository;
    private final NatureImpotRepository natureImpotRepository;
    private final DeclarationRepository declarationRepository;
    private final TaxationRepository taxationRepository;
    private final PaiementRepository paiementRepository;
    private final ContribuableRepository contribuableRepository;
    private final PasswordEncoder passwordEncoder;

    public BasicDataSeeder(
            UtilisateurRepository utilisateurRepository,
            AgentRepository agentRepository,
            BureauRepository bureauRepository,
            DivisionRepository divisionRepository,
            ProprieteRepository proprieteRepository,
            ConcessionMinierRepository concessionMinierRepository,
            NatureImpotRepository natureImpotRepository,
            DeclarationRepository declarationRepository,
            TaxationRepository taxationRepository,
            PaiementRepository paiementRepository,
            ContribuableRepository contribuableRepository,
            PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.agentRepository = agentRepository;
        this.bureauRepository = bureauRepository;
        this.divisionRepository = divisionRepository;
        this.proprieteRepository = proprieteRepository;
        this.concessionMinierRepository = concessionMinierRepository;
        this.natureImpotRepository = natureImpotRepository;
        this.declarationRepository = declarationRepository;
        this.taxationRepository = taxationRepository;
        this.paiementRepository = paiementRepository;
        this.contribuableRepository = contribuableRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Vérifier si des données existent déjà
        if (utilisateurRepository.count() > 0) {
            System.out.println("BasicDataSeeder: données déjà présentes, on saute le seeding.");
            return;
        }

        try {
            System.out.println("BasicDataSeeder: démarrage du seeding...");

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
            
            // 9. Créer un contribuable personne physique (sans utiliser l'héritage)
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
            
            // 11. Créer un contribuable personne morale (sans utiliser l'héritage)
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
            
            // 13. Créer des propriétés
            GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
            
            // Propriété 1 - Villa pour la personne physique
            Propriete p1 = new Propriete();
            p1.setType(TypePropriete.VI);
            p1.setLocalite("Katuba");
            p1.setRangLocalite(2);
            p1.setSuperficie(350.0);
            p1.setAdresse("Parcelle 123, Q. Katuba");
            Contribuable contribuable1 = new Contribuable();
            contribuable1.setId(agentContribuable.getId());
            contribuable1.setNom(agentContribuable.getNom());
            contribuable1.setSexe(agentContribuable.getSexe());
            contribuable1.setMatricule(agentContribuable.getMatricule());
            contribuable1.setBureau(agentContribuable.getBureau());
            contribuable1.setAdressePrincipale("123 Avenue Principale");
            contribuable1.setTelephonePrincipal("+243820000001");
            contribuable1.setEmail("jean.dupont@example.com");
            contribuable1.setNationalite("RDC");
            contribuable1.setType(TypeContribuable.PERSONNE_PHYSIQUE);
            contribuable1.setIdNat("IDNAT-001");
            contribuable1.setNumeroIdentificationContribuable("NIF-100200300");
            contribuableRepository.save(contribuable1);
            p1.setProprietaire(contribuable1);
            p1.setMontantImpot(350.0 * 1.25);
            p1.setLocation(gf.createPoint(new Coordinate(27.46885, -11.66492)));
            p1.setActif(true);
            p1.setDeclarationEnLigne(true);
            p1.setDeclare(true);
            proprieteRepository.save(p1);
            
            // Propriété 2 - Appartement pour la personne physique
            Propriete p2 = new Propriete();
            p2.setType(TypePropriete.AP);
            p2.setLocalite("Kenya");
            p2.setRangLocalite(1);
            p2.setSuperficie(120.0);
            p2.setAdresse("Immeuble 7, Av. Université");
            p2.setProprietaire(contribuable1);
            p2.setMontantImpot(120.0 * 1.5);
            p2.setLocation(gf.createPoint(new Coordinate(27.47000, -11.66300)));
            p2.setActif(true);
            p2.setDeclarationEnLigne(true);
            p2.setDeclare(true);
            proprieteRepository.save(p2);
            
            // Propriété 3 - Terrain pour l'entreprise
            Propriete p3 = new Propriete();
            p3.setType(TypePropriete.TE);
            p3.setLocalite("Zone Industrielle");
            p3.setRangLocalite(1);
            p3.setSuperficie(5000.0);
            p3.setAdresse("Parcelle 456, Zone Industrielle");
            Contribuable contribuableEntreprise = new Contribuable();
            contribuableEntreprise.setId(agentEntreprise.getId());
            contribuableEntreprise.setNom(agentEntreprise.getNom());
            contribuableEntreprise.setSexe(agentEntreprise.getSexe());
            contribuableEntreprise.setMatricule(agentEntreprise.getMatricule());
            contribuableEntreprise.setBureau(agentEntreprise.getBureau());
            contribuableEntreprise.setAdressePrincipale("456 Avenue Industrielle");
            contribuableEntreprise.setAdresseSecondaire("Zone Minière 5");
            contribuableEntreprise.setTelephonePrincipal("+243990000001");
            contribuableEntreprise.setTelephoneSecondaire("+243820000002");
            contribuableEntreprise.setEmail("contact@katangamining.cd");
            contribuableEntreprise.setNationalite("RDC");
            contribuableEntreprise.setType(TypeContribuable.PERSONNE_MORALE);
            contribuableEntreprise.setIdNat("IDNAT-002");
            contribuableEntreprise.setNRC("RCCM-CD/LUA/2020-B-1234");
            contribuableEntreprise.setSigle("KAT");
            contribuableEntreprise.setNumeroIdentificationContribuable("NIF-200300400");
            contribuableRepository.save(contribuableEntreprise);
            p3.setProprietaire(contribuableEntreprise);
            p3.setMontantImpot(5000.0 * 0.8);
            p3.setLocation(gf.createPoint(new Coordinate(27.48000, -11.67000)));
            p3.setActif(true);
            p3.setDeclarationEnLigne(true);
            p3.setDeclare(true);
            proprieteRepository.save(p3);
            
            // 14. Créer des concessions minières
            // Concession 1 - Exploitation
            ConcessionMinier c1 = new ConcessionMinier();
            c1.setNombreCarresMinier(12.0);
            c1.setDateAcquisition(new Date());
            c1.setType(TypeConcession.EXPLOITATION);
            c1.setAnnexe("annexe1");
            c1.setTitulaire(contribuableEntreprise);
            c1.setMontantImpot(12.0 * 84.955 * 2.0);
            concessionMinierRepository.save(c1);
            
            // Concession 2 - Recherche
            ConcessionMinier c2 = new ConcessionMinier();
            c2.setNombreCarresMinier(5.0);
            c2.setDateAcquisition(new Date());
            c2.setType(TypeConcession.RECHERCHE);
            c2.setAnnexe("annexe2");
            c2.setTitulaire(contribuableEntreprise);
            c2.setMontantImpot(5.0 * 84.955 * 1.5);
            concessionMinierRepository.save(c2);
            
            // 15. Créer des natures d'impôt
            // Impôt Foncier
            NatureImpot ni1 = new NatureImpot();
            ni1.setCode(TypeImpot.IF.toString());
            ni1.setNom("Impôt Foncier");
            ni1.setDescription("Impôt sur les propriétés foncières");
            ni1.setActif(true);
            natureImpotRepository.save(ni1);
            
            // Impôt sur Revenu Locatif
            NatureImpot ni2 = new NatureImpot();
            ni2.setCode(TypeImpot.IRL.toString());
            ni2.setNom("Impôt sur Revenu Locatif");
            ni2.setDescription("Impôt sur les revenus locatifs des propriétés");
            ni2.setActif(true);
            natureImpotRepository.save(ni2);
            
            // Impôt sur Concessions Minières
            NatureImpot ni3 = new NatureImpot();
            ni3.setCode(TypeImpot.ICM.toString());
            ni3.setNom("Impôt sur Concessions Minières");
            ni3.setDescription("Impôt sur les concessions minières");
            ni3.setActif(true);
            natureImpotRepository.save(ni3);
            
            // 16. Créer des déclarations
            // Déclaration pour la première propriété
            Declaration d1 = new Declaration();
            d1.setDateDeclaration(new Date());
            d1.setStatut(StatutDeclaration.VALIDEE);
            d1.setPropriete(p1);
            d1.setContribuable(contribuable1);
            d1.setAgentValidateur(taxateur);
            d1.setSource(SourceDeclaration.EN_LIGNE);
            d1.setActif(true);
            declarationRepository.save(d1);
            
            // Déclaration pour la deuxième propriété
            Declaration d2 = new Declaration();
            d2.setDateDeclaration(new Date());
            d2.setStatut(StatutDeclaration.SOUMISE);
            d2.setPropriete(p2);
            d2.setContribuable(contribuable1);
            d2.setSource(SourceDeclaration.EN_LIGNE);
            d2.setActif(true);
            declarationRepository.save(d2);
            
            // Déclaration pour la concession minière
            Declaration d3 = new Declaration();
            d3.setDateDeclaration(new Date());
            d3.setStatut(StatutDeclaration.VALIDEE);
            d3.setConcession(c1);
            d3.setContribuable(contribuableEntreprise);
            d3.setAgentValidateur(taxateur);
            d3.setSource(SourceDeclaration.ADMINISTRATION);
            d3.setActif(true);
            declarationRepository.save(d3);
            
            // 17. Créer des taxations
            Calendar cal = Calendar.getInstance();
            int currentYear = cal.get(Calendar.YEAR);
            
            // Taxation pour la première déclaration
            Taxation t1 = new Taxation();
            t1.setDateTaxation(new Date());
            t1.setMontant(p1.getMontantImpot());
            t1.setExercice(String.valueOf(currentYear));
            t1.setStatut(StatutTaxation.VALIDEE);
            t1.setTypeImpot(TypeImpot.IF);
            t1.setExoneration(false);
            t1.setDeclaration(d1);
            t1.setAgentTaxateur(taxateur);
            t1.setActif(true);
            taxationRepository.save(t1);
            
            // Taxation pour la concession minière
            Taxation t2 = new Taxation();
            t2.setDateTaxation(new Date());
            t2.setMontant(c1.getMontantImpot());
            t2.setExercice(String.valueOf(currentYear));
            t2.setStatut(StatutTaxation.VALIDEE);
            t2.setTypeImpot(TypeImpot.ICM);
            t2.setExoneration(false);
            t2.setDeclaration(d3);
            t2.setAgentTaxateur(taxateur);
            t2.setActif(true);
            taxationRepository.save(t2);
            
            // 18. Créer des paiements
            // Paiement pour la première taxation
            Paiement p = new Paiement();
            p.setDate(new Date());
            p.setMontant(t1.getMontant());
            p.setMode(ModePaiement.BANQUE);
            p.setStatut(StatutPaiement.VALIDE);
            p.setBordereauBancaire("BORD-123456");
            p.setTaxation(t1);
            p.setActif(true);
            paiementRepository.save(p);
            
            // Mettre à jour le statut de la taxation
            t1.setStatut(StatutTaxation.PAYEE);
            t1.setPaiement(p);
            taxationRepository.save(t1);

            System.out.println("BasicDataSeeder: seeding terminé avec succès.");
        } catch (Exception e) {
            System.err.println("BasicDataSeeder: erreur lors du seeding: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
