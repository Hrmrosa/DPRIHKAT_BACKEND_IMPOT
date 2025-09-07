package com.DPRIHKAT.config;

import com.DPRIHKAT.entity.*;
import com.DPRIHKAT.entity.enums.*;
import com.DPRIHKAT.repository.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Seeder pour créer les données essentielles de l'application
 * Évite les relations complexes qui peuvent causer des erreurs
 */
// @Component
@Order(10)
public class DataSeeder implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final BureauRepository bureauRepository;
    private final DivisionRepository divisionRepository;
    private final AgentRepository agentRepository;
    private final ContribuableRepository contribuableRepository;
    private final ProprieteRepository proprieteRepository;
    private final ConcessionMinierRepository concessionMinierRepository;
    private final NatureImpotRepository natureImpotRepository;
    private final ProprieteImpotRepository proprieteImpotRepository;
    private final DeclarationRepository declarationRepository;
    private final TaxationRepository taxationRepository;
    private final PaiementRepository paiementRepository;
    private final DossierRecouvrementRepository dossierRecouvrementRepository;
    private final RelanceRepository relanceRepository;
    private final PoursuiteRepository poursuiteRepository;
    private final VehiculeRepository vehiculeRepository;
    private final PlaqueRepository plaqueRepository;
    private final CertificatRepository certificatRepository;
    private final VignetteRepository vignetteRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(
            UtilisateurRepository utilisateurRepository,
            BureauRepository bureauRepository,
            DivisionRepository divisionRepository,
            AgentRepository agentRepository,
            ContribuableRepository contribuableRepository,
            ProprieteRepository proprieteRepository,
            ConcessionMinierRepository concessionMinierRepository,
            NatureImpotRepository natureImpotRepository,
            ProprieteImpotRepository proprieteImpotRepository,
            DeclarationRepository declarationRepository,
            TaxationRepository taxationRepository,
            PaiementRepository paiementRepository,
            DossierRecouvrementRepository dossierRecouvrementRepository,
            RelanceRepository relanceRepository,
            PoursuiteRepository poursuiteRepository,
            VehiculeRepository vehiculeRepository,
            PlaqueRepository plaqueRepository,
            CertificatRepository certificatRepository,
            VignetteRepository vignetteRepository,
            PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.bureauRepository = bureauRepository;
        this.divisionRepository = divisionRepository;
        this.agentRepository = agentRepository;
        this.contribuableRepository = contribuableRepository;
        this.proprieteRepository = proprieteRepository;
        this.concessionMinierRepository = concessionMinierRepository;
        this.natureImpotRepository = natureImpotRepository;
        this.proprieteImpotRepository = proprieteImpotRepository;
        this.declarationRepository = declarationRepository;
        this.taxationRepository = taxationRepository;
        this.paiementRepository = paiementRepository;
        this.dossierRecouvrementRepository = dossierRecouvrementRepository;
        this.relanceRepository = relanceRepository;
        this.poursuiteRepository = poursuiteRepository;
        this.vehiculeRepository = vehiculeRepository;
        this.plaqueRepository = plaqueRepository;
        this.certificatRepository = certificatRepository;
        this.vignetteRepository = vignetteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Vérifier si des données existent déjà
        if (utilisateurRepository.count() > 0) {
            System.out.println("DataSeeder: données déjà présentes, on saute le seeding.");
            return;
        }

        try {
            System.out.println("DataSeeder: démarrage du seeding...");

            // Créer les divisions
            List<Division> divisions = createDivisions();
            divisionRepository.saveAll(divisions);
            
            // Créer les bureaux
            List<Bureau> bureaux = createBureaux(divisions);
            bureauRepository.saveAll(bureaux);
            
            // Créer les agents (sauf contribuables)
            List<Agent> agents = createAgents(bureaux);
            agentRepository.saveAll(agents);
            
            // Créer les contribuables avec la nouvelle approche
            List<Contribuable> contribuables = createContribuables(bureaux);
            contribuableRepository.saveAll(contribuables);
            
            // Créer les utilisateurs
            List<Utilisateur> utilisateurs = createUtilisateurs(agents, contribuables);
            utilisateurRepository.saveAll(utilisateurs);
            
            // 9. Créer des propriétés
            List<Propriete> proprietes = createProprietes(contribuables.get(0), contribuables.get(1));
            proprieteRepository.saveAll(proprietes);

            // 10. Créer des concessions minières
            List<ConcessionMinier> concessions = createConcessions(contribuables.get(1));
            concessionMinierRepository.saveAll(concessions);

            // 11. Créer des natures d'impôt
            List<NatureImpot> naturesImpot = createNaturesImpot();
            natureImpotRepository.saveAll(naturesImpot);

            // 12. Créer des liens propriété-impôt
            List<ProprieteImpot> proprieteImpots = createProprieteImpots(proprietes, naturesImpot);
            proprieteImpotRepository.saveAll(proprieteImpots);

            // 13. Créer des déclarations
            List<Declaration> declarations = createDeclarations(proprietes, concessions, agents.get(0), contribuables.get(0), contribuables.get(1));
            declarationRepository.saveAll(declarations);

            // 14. Créer des taxations
            List<Taxation> taxations = createTaxations(declarations, agents.get(0));
            taxationRepository.saveAll(taxations);

            // 15. Créer des paiements
            List<Paiement> paiements = createPaiements(taxations);
            paiementRepository.saveAll(paiements);

            // 16. Créer des dossiers de recouvrement
            List<DossierRecouvrement> dossiers = createDossiersRecouvrement(contribuables.get(0), contribuables.get(1));
            dossierRecouvrementRepository.saveAll(dossiers);

            // 17. Créer des relances
            List<Relance> relances = createRelances(dossiers);
            relanceRepository.saveAll(relances);

            // 18. Créer des poursuites
            List<Poursuite> poursuites = createPoursuites(dossiers, agents.get(0));
            poursuiteRepository.saveAll(poursuites);

            // 19. Créer des véhicules
            List<Vehicule> vehicules = createVehicules(contribuables.get(0), contribuables.get(1));
            vehiculeRepository.saveAll(vehicules);

            // 20. Créer des plaques
            List<Plaque> plaques = createPlaques(vehicules);
            plaqueRepository.saveAll(plaques);

            // 21. Créer des certificats
            List<Certificat> certificats = createCertificats(vehicules, declarations, agents.get(1));
            certificatRepository.saveAll(certificats);

            // 22. Créer des vignettes
            List<Vignette> vignettes = createVignettes(vehicules, agents.get(1));
            vignetteRepository.saveAll(vignettes);

            System.out.println("DataSeeder: seeding terminé avec succès.");
        } catch (Exception e) {
            System.err.println("DataSeeder: erreur lors du seeding: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Crée des divisions
     */
    private List<Division> createDivisions() {
        List<Division> divisions = new ArrayList<>();
        
        // Division 1
        Division d1 = new Division();
        d1.setNom("Division Principale");
        d1.setCode("DP");
        divisions.add(d1);
        
        // Division 2
        Division d2 = new Division();
        d2.setNom("Division Secondaire");
        d2.setCode("DS");
        divisions.add(d2);
        
        return divisions;
    }

    /**
     * Crée des bureaux
     */
    private List<Bureau> createBureaux(List<Division> divisions) {
        List<Bureau> bureaux = new ArrayList<>();
        
        // Bureau 1
        Bureau b1 = new Bureau();
        b1.setNom("Bureau Principal");
        b1.setCode("BP");
        b1.setDivision(divisions.get(0));
        bureaux.add(b1);
        
        // Bureau 2
        Bureau b2 = new Bureau();
        b2.setNom("Bureau Secondaire");
        b2.setCode("BS");
        b2.setDivision(divisions.get(1));
        bureaux.add(b2);
        
        return bureaux;
    }

    /**
     * Crée des agents (sauf contribuables)
     */
    private List<Agent> createAgents(List<Bureau> bureaux) {
        List<Agent> agents = new ArrayList<>();
        
        // Agent 1
        Agent a1 = new Agent();
        a1.setNom("Agent Principal");
        a1.setSexe(Sexe.M);
        a1.setMatricule("AGT-001");
        a1.setBureau(bureaux.get(0));
        agents.add(a1);
        
        // Agent 2
        Agent a2 = new Agent();
        a2.setNom("Agent Secondaire");
        a2.setSexe(Sexe.F);
        a2.setMatricule("AGT-002");
        a2.setBureau(bureaux.get(1));
        agents.add(a2);
        
        return agents;
    }

    /**
     * Crée des contribuables avec gestion correcte de l'héritage
     */
    private List<Contribuable> createContribuables(List<Bureau> bureaux) {
        List<Contribuable> contribuables = new ArrayList<>();
        Random random = new Random(42);
        
        // Créer un contribuable personne physique
        Contribuable personnePhysique = new Contribuable();
        
        // Initialiser d'abord les champs de Agent
        personnePhysique.setNom("Dupont Jean");
        personnePhysique.setSexe(Sexe.M);
        personnePhysique.setMatricule("CONT-001");
        personnePhysique.setBureau(bureaux.get(0));
        
        // Puis les champs spécifiques à Contribuable
        personnePhysique.setAdressePrincipale("123 Avenue Principale");
        personnePhysique.setTelephonePrincipal("+243820000001");
        personnePhysique.setEmail("jean.dupont@example.com");
        personnePhysique.setNationalite("RDC");
        personnePhysique.setType(TypeContribuable.PERSONNE_PHYSIQUE);
        personnePhysique.setIdNat("IDNAT-001");
        personnePhysique.setNumeroIdentificationContribuable("NIF-100200300");
        
        contribuables.add(personnePhysique);
        
        // Créer un contribuable personne morale
        Contribuable personneMorale = new Contribuable();
        
        // Initialiser les champs de Agent
        personneMorale.setNom("Société Minière KATANGA SA");
        personneMorale.setSexe(Sexe.M); // Par défaut pour les personnes morales
        personneMorale.setMatricule("CONT-002");
        personneMorale.setBureau(bureaux.get(0));
        
        // Initialiser les champs spécifiques à Contribuable
        personneMorale.setAdressePrincipale("456 Avenue Industrielle");
        personneMorale.setAdresseSecondaire("Zone Minière 5");
        personneMorale.setTelephonePrincipal("+243990000001");
        personneMorale.setTelephoneSecondaire("+243820000002");
        personneMorale.setEmail("contact@katangamining.cd");
        personneMorale.setNationalite("RDC");
        personneMorale.setType(TypeContribuable.PERSONNE_MORALE);
        personneMorale.setIdNat("IDNAT-002");
        personneMorale.setNRC("RCCM-CD/LUA/2020-B-1234");
        personneMorale.setSigle("KAT");
        personneMorale.setNumeroIdentificationContribuable("NIF-200300400");
        
        contribuables.add(personneMorale);
        
        return contribuables;
    }

    /**
     * Crée des utilisateurs
     */
    private List<Utilisateur> createUtilisateurs(List<Agent> agents, List<Contribuable> contribuables) {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        
        // Utilisateur 1
        Utilisateur u1 = new Utilisateur();
        u1.setLogin("agent");
        u1.setMotDePasse(passwordEncoder.encode("Tabc@123"));
        u1.setRole(Role.TAXATEUR);
        u1.setPremierConnexion(false);
        u1.setBloque(false);
        u1.setActif(true);
        u1.setAgent(agents.get(0));
        utilisateurs.add(u1);
        
        // Utilisateur 2
        Utilisateur u2 = new Utilisateur();
        u2.setLogin("contribuable");
        u2.setMotDePasse(passwordEncoder.encode("Tabc@123"));
        u2.setRole(Role.CONTRIBUABLE);
        u2.setPremierConnexion(false);
        u2.setBloque(false);
        u2.setActif(true);
        u2.setAgent(contribuables.get(0));
        utilisateurs.add(u2);
        
        return utilisateurs;
    }

    /**
     * Crée des propriétés pour les contribuables
     */
    private List<Propriete> createProprietes(Contribuable personne, Contribuable entreprise) {
        List<Propriete> proprietes = new ArrayList<>();
        GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);

        // Propriété 1 - Villa pour la personne physique
        Propriete p1 = new Propriete();
        p1.setType(TypePropriete.VI);
        p1.setLocalite("Katuba");
        p1.setRangLocalite(2);
        p1.setSuperficie(350.0);
        p1.setAdresse("Parcelle 123, Q. Katuba");
        p1.setProprietaire(personne);
        p1.setMontantImpot(350.0 * 1.25);
        p1.setLocation(gf.createPoint(new Coordinate(11.65, -7.45)));
        p1.setActif(true);
        p1.setDeclarationEnLigne(true);
        p1.setDeclare(true);
        proprietes.add(p1);

        // Propriété 2 - Appartement pour la personne physique
        Propriete p2 = new Propriete();
        p2.setType(TypePropriete.AP);
        p2.setLocalite("Kenya");
        p2.setRangLocalite(1);
        p2.setSuperficie(120.0);
        p2.setAdresse("Immeuble 7, Av. Université");
        p2.setProprietaire(personne);
        p2.setMontantImpot(120.0 * 1.5);
        p2.setLocation(gf.createPoint(new Coordinate(11.67, -7.43)));
        p2.setActif(true);
        p2.setDeclarationEnLigne(true);
        p2.setDeclare(true);
        proprietes.add(p2);

        // Propriété 3 - Terrain pour l'entreprise
        Propriete p3 = new Propriete();
        p3.setType(TypePropriete.TE);
        p3.setLocalite("Zone Industrielle");
        p3.setRangLocalite(1);
        p3.setSuperficie(5000.0);
        p3.setAdresse("Parcelle 456, Zone Industrielle");
        p3.setProprietaire(entreprise);
        p3.setMontantImpot(5000.0 * 0.8);
        p3.setLocation(gf.createPoint(new Coordinate(11.70, -7.50)));
        p3.setActif(true);
        p3.setDeclarationEnLigne(true);
        p3.setDeclare(true);
        proprietes.add(p3);

        return proprietes;
    }

    /**
     * Crée des concessions minières pour l'entreprise
     */
    private List<ConcessionMinier> createConcessions(Contribuable entreprise) {
        List<ConcessionMinier> concessions = new ArrayList<>();

        // Concession 1 - Exploitation
        ConcessionMinier c1 = new ConcessionMinier();
        c1.setNombreCarresMinier(12.0);
        c1.setDateAcquisition(new Date());
        c1.setType(TypeConcession.EXPLOITATION);
        c1.setAnnexe("annexe1");
        c1.setTitulaire(entreprise);
        c1.setMontantImpot(12.0 * 84.955 * 2.0);
        concessions.add(c1);

        // Concession 2 - Recherche
        ConcessionMinier c2 = new ConcessionMinier();
        c2.setNombreCarresMinier(5.0);
        c2.setDateAcquisition(new Date());
        c2.setType(TypeConcession.RECHERCHE);
        c2.setAnnexe("annexe2");
        c2.setTitulaire(entreprise);
        c2.setMontantImpot(5.0 * 84.955 * 1.5);
        concessions.add(c2);

        return concessions;
    }

    /**
     * Crée des natures d'impôt
     */
    private List<NatureImpot> createNaturesImpot() {
        List<NatureImpot> naturesImpot = new ArrayList<>();

        // Impôt Foncier
        NatureImpot ni1 = new NatureImpot();
        ni1.setCode(TypeImpot.IF.toString());
        ni1.setNom("Impôt Foncier");
        ni1.setDescription("Impôt sur les propriétés foncières");
        ni1.setActif(true);
        naturesImpot.add(ni1);

        // Impôt sur Revenu Locatif
        NatureImpot ni2 = new NatureImpot();
        ni2.setCode(TypeImpot.IRL.toString());
        ni2.setNom("Impôt sur Revenu Locatif");
        ni2.setDescription("Impôt sur les revenus locatifs des propriétés");
        ni2.setActif(true);
        naturesImpot.add(ni2);

        // Impôt sur Concessions Minières
        NatureImpot ni3 = new NatureImpot();
        ni3.setCode(TypeImpot.ICM.toString());
        ni3.setNom("Impôt sur Concessions Minières");
        ni3.setDescription("Impôt sur les concessions minières");
        ni3.setActif(true);
        naturesImpot.add(ni3);

        // Impôt sur Revenus des Véhicules
        NatureImpot ni4 = new NatureImpot();
        ni4.setCode(TypeImpot.IRV.toString());
        ni4.setNom("Impôt sur Revenus des Véhicules");
        ni4.setDescription("Impôt sur les revenus générés par les véhicules");
        ni4.setActif(true);
        naturesImpot.add(ni4);

        // Redevance Logement
        NatureImpot ni5 = new NatureImpot();
        ni5.setCode(TypeImpot.RL.toString());
        ni5.setNom("Redevance Logement");
        ni5.setDescription("Redevance sur les logements");
        ni5.setActif(true);
        naturesImpot.add(ni5);

        return naturesImpot;
    }

    /**
     * Crée des liens entre propriétés et natures d'impôt
     */
    private List<ProprieteImpot> createProprieteImpots(List<Propriete> proprietes, List<NatureImpot> naturesImpot) {
        List<ProprieteImpot> proprieteImpots = new ArrayList<>();
        Random random = new Random(42);

        // Pour chaque propriété, créer des liens avec des natures d'impôt
        for (Propriete p : proprietes) {
            // Toutes les propriétés ont l'impôt foncier (IF)
            ProprieteImpot pi1 = new ProprieteImpot();
            pi1.setPropriete(p);
            pi1.setNatureImpot(naturesImpot.get(0)); // IF
            pi1.setTauxImposition(0.5 + random.nextDouble() * 1.0); // Taux entre 0.5 et 1.5
            pi1.setActif(true);
            proprieteImpots.add(pi1);

            // 50% des propriétés ont aussi l'impôt sur revenu locatif (IRL)
            if (random.nextBoolean()) {
                ProprieteImpot pi2 = new ProprieteImpot();
                pi2.setPropriete(p);
                pi2.setNatureImpot(naturesImpot.get(1)); // IRL
                pi2.setTauxImposition(1.0 + random.nextDouble() * 1.5); // Taux entre 1.0 et 2.5
                pi2.setActif(true);
                proprieteImpots.add(pi2);
            }
        }

        return proprieteImpots;
    }

    /**
     * Crée des déclarations pour les propriétés et concessions
     */
    private List<Declaration> createDeclarations(List<Propriete> proprietes, List<ConcessionMinier> concessions, Agent taxateur, Contribuable personne, Contribuable entreprise) {
        List<Declaration> declarations = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        
        // Déclaration pour la première propriété (personne physique)
        Declaration d1 = new Declaration();
        d1.setDateDeclaration(new Date());
        d1.setStatut(StatutDeclaration.VALIDEE);
        d1.setPropriete(proprietes.get(0));
        d1.setContribuable(personne);
        d1.setAgentValidateur(taxateur);
        d1.setSource(SourceDeclaration.EN_LIGNE);
        d1.setActif(true);
        declarations.add(d1);
        
        // Déclaration pour la deuxième propriété (personne physique)
        Declaration d2 = new Declaration();
        d2.setDateDeclaration(new Date());
        d2.setStatut(StatutDeclaration.SOUMISE);
        d2.setPropriete(proprietes.get(1));
        d2.setContribuable(personne);
        d2.setSource(SourceDeclaration.EN_LIGNE);
        d2.setActif(true);
        declarations.add(d2);
        
        // Déclaration pour la troisième propriété (entreprise)
        Declaration d3 = new Declaration();
        d3.setDateDeclaration(new Date());
        d3.setStatut(StatutDeclaration.VALIDEE);
        d3.setPropriete(proprietes.get(2));
        d3.setContribuable(entreprise);
        d3.setAgentValidateur(taxateur);
        d3.setSource(SourceDeclaration.ADMINISTRATION);
        d3.setActif(true);
        declarations.add(d3);
        
        // Déclaration pour la première concession minière
        Declaration d4 = new Declaration();
        d4.setDateDeclaration(new Date());
        d4.setStatut(StatutDeclaration.VALIDEE);
        d4.setConcession(concessions.get(0));
        d4.setContribuable(entreprise);
        d4.setAgentValidateur(taxateur);
        d4.setSource(SourceDeclaration.ADMINISTRATION);
        d4.setActif(true);
        declarations.add(d4);
        
        return declarations;
    }
    
    /**
     * Crée des taxations pour les déclarations
     */
    private List<Taxation> createTaxations(List<Declaration> declarations, Agent taxateur) {
        List<Taxation> taxations = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        Random random = new Random(42);
        
        // Pour chaque déclaration validée, créer une taxation
        for (Declaration d : declarations) {
            if (d.getStatut() == StatutDeclaration.VALIDEE) {
                Taxation t = new Taxation();
                t.setDateTaxation(new Date());
                
                // Utiliser une valeur fixe ou calculée en fonction du type de bien
                double montant = 0.0;
                if (d.getPropriete() != null) {
                    // Si c'est une propriété, calculer en fonction de la superficie
                    montant = d.getPropriete().getSuperficie() * (0.5 + random.nextDouble());
                } else if (d.getConcession() != null) {
                    // Si c'est une concession, calculer en fonction du nombre de carrés miniers
                    montant = d.getConcession().getNombreCarresMinier() * 85.0;
                } else {
                    // Valeur par défaut
                    montant = 1000.0 + random.nextDouble() * 9000.0;
                }
                
                t.setMontant(montant);
                t.setExercice(String.valueOf(currentYear));
                t.setStatut(StatutTaxation.VALIDEE);
                t.setTypeImpot(d.getPropriete() != null ? TypeImpot.IF : TypeImpot.ICM);
                t.setExoneration(false);
                t.setDeclaration(d);
                t.setAgentTaxateur(taxateur);
                t.setActif(true);
                taxations.add(t);
            }
        }
        
        return taxations;
    }
    
    /**
     * Crée des paiements pour les taxations
     */
    private List<Paiement> createPaiements(List<Taxation> taxations) {
        List<Paiement> paiements = new ArrayList<>();
        Random random = new Random(42);
        
        // Pour chaque taxation validée, créer un paiement
        for (Taxation t : taxations) {
            if (t.getStatut() == StatutTaxation.VALIDEE) {
                Paiement p = new Paiement();
                p.setDate(new Date());
                p.setMontant(t.getMontant());
                
                // Mode de paiement aléatoire
                ModePaiement[] modes = ModePaiement.values();
                p.setMode(modes[random.nextInt(modes.length)]);
                
                p.setStatut(StatutPaiement.VALIDE);
                p.setBordereauBancaire("BORD-" + String.format("%06d", random.nextInt(1000000)));
                p.setTaxation(t);
                p.setActif(true);
                
                paiements.add(p);
                
                // Mettre à jour le statut de la taxation
                t.setStatut(StatutTaxation.PAYEE);
                t.setPaiement(p);
            }
        }
        
        return paiements;
    }
    
    /**
     * Crée des dossiers de recouvrement pour les contribuables
     */
    private List<DossierRecouvrement> createDossiersRecouvrement(Contribuable personne, Contribuable entreprise) {
        List<DossierRecouvrement> dossiers = new ArrayList<>();
        
        // Dossier pour la personne physique
        DossierRecouvrement d1 = new DossierRecouvrement();
        d1.setTotalDu(500000.0);
        d1.setTotalRecouvre(50000.0);
        d1.setDateOuverture(new Date());
        d1.setContribuable(personne);
        dossiers.add(d1);
        
        return dossiers;
    }
    
    /**
     * Crée des relances pour les dossiers de recouvrement
     */
    private List<Relance> createRelances(List<DossierRecouvrement> dossiers) {
        List<Relance> relances = new ArrayList<>();
        
        // Pour chaque dossier, créer une relance
        for (DossierRecouvrement d : dossiers) {
            Relance r = new Relance();
            r.setDateEnvoi(new Date());
            r.setType(TypeRelance.EMAIL);
            r.setStatut(StatutRelance.ENVOYEE);
            r.setContenu("Veuillez régulariser votre situation fiscale dans les plus brefs délais.");
            r.setDossierRecouvrement(d);
            relances.add(r);
        }
        
        return relances;
    }
    
    /**
     * Crée des poursuites pour les dossiers de recouvrement
     */
    private List<Poursuite> createPoursuites(List<DossierRecouvrement> dossiers, Agent agent) {
        List<Poursuite> poursuites = new ArrayList<>();
        
        // Pour chaque dossier, créer une poursuite
        for (DossierRecouvrement d : dossiers) {
            Poursuite p = new Poursuite();
            p.setType(TypePoursuite.SAISIE_IMMOBILIERE);
            p.setDateLancement(new Date());
            p.setStatut(StatutPoursuite.EN_COURS);
            p.setMontantRecouvre(0.0);
            p.setAgentInitiateur(agent);
            p.setDossierRecouvrement(d);
            poursuites.add(p);
        }
        
        return poursuites;
    }

    /**
     * Crée des véhicules pour les contribuables
     */
    private List<Vehicule> createVehicules(Contribuable personne, Contribuable entreprise) {
        List<Vehicule> vehicules = new ArrayList<>();
        
        // Véhicule pour la personne physique
        Vehicule v1 = new Vehicule();
        v1.setMarque("TOYOTA");
        v1.setModele("Hilux");
        v1.setAnnee(2022);
        v1.setImmatriculation("123AB45");
        v1.setNumeroChassis("TOY-123456789");
        v1.setDateEnregistrement(new Date());
        v1.setProprietaire(personne);
        vehicules.add(v1);
        
        // Véhicule pour l'entreprise
        Vehicule v2 = new Vehicule();
        v2.setMarque("MERCEDES");
        v2.setModele("Sprinter");
        v2.setAnnee(2021);
        v2.setImmatriculation("456CD78");
        v2.setNumeroChassis("MER-987654321");
        v2.setDateEnregistrement(new Date());
        v2.setProprietaire(entreprise);
        vehicules.add(v2);
        
        return vehicules;
    }
    
    /**
     * Crée des plaques pour les véhicules
     */
    private List<Plaque> createPlaques(List<Vehicule> vehicules) {
        List<Plaque> plaques = new ArrayList<>();
        
        // Pour chaque véhicule, créer une plaque
        for (Vehicule v : vehicules) {
            Plaque p = new Plaque();
            p.setNumeroSerie("SER-" + v.getImmatriculation());
            p.setDisponible(false);
            p.setNumplaque(v.getImmatriculation());
            p.setVehicule(v);
            plaques.add(p);
        }
        
        return plaques;
    }
    
    /**
     * Crée des certificats pour les véhicules
     */
    private List<Certificat> createCertificats(List<Vehicule> vehicules, List<Declaration> declarations, Agent receveur) {
        List<Certificat> certificats = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        
        // Pour chaque véhicule, créer un certificat
        for (int i = 0; i < vehicules.size(); i++) {
            Vehicule v = vehicules.get(i);
            
            Certificat c = new Certificat();
            c.setNumero("CERT-" + String.format("%06d", i + 1));
            
            // Date d'émission (aujourd'hui)
            c.setDateEmission(new Date());
            
            // Date d'expiration (1 an plus tard)
            cal.setTime(new Date());
            cal.add(Calendar.YEAR, 1);
            c.setDateExpiration(cal.getTime());
            
            c.setMontant(250.0);
            
            // Déclaration associée (si disponible)
            if (i < declarations.size()) {
                c.setDeclaration(declarations.get(i));
            }
            
            c.setVehicule(v);
            c.setAgent(receveur);
            
            certificats.add(c);
        }
        
        return certificats;
    }
    
    /**
     * Crée des vignettes pour les véhicules
     */
    private List<Vignette> createVignettes(List<Vehicule> vehicules, Agent receveur) {
        List<Vignette> vignettes = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        
        // Pour chaque véhicule, créer une vignette
        for (int i = 0; i < vehicules.size(); i++) {
            Vehicule v = vehicules.get(i);
            
            Vignette vg = new Vignette();
            vg.setNumero("VIG-" + String.format("%06d", i + 1));
            
            // Date d'émission (aujourd'hui)
            vg.setDateEmission(new Date());
            
            // Date d'expiration (1 an plus tard)
            cal.setTime(new Date());
            cal.add(Calendar.YEAR, 1);
            vg.setDateExpiration(cal.getTime());
            
            vg.setMontant(120.0);
            vg.setVehicule(v);
            vg.setAgent(receveur);
            
            vignettes.add(vg);
        }
        
        return vignettes;
    }
}
