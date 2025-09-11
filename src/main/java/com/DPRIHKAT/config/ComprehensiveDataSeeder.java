package com.DPRIHKAT.config;

import com.DPRIHKAT.entity.*;
import com.DPRIHKAT.entity.enums.*;
import com.DPRIHKAT.repository.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Seeder complet pour créer les données essentielles de l'application
 * Implémente la distinction entre déclaration et taxation
 * Crée 15 contribuables avec plusieurs propriétés à Lubumbashi
 */
@Component
@Order(10)
public class ComprehensiveDataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ComprehensiveDataSeeder.class);

    // Coordonnées approximatives de Lubumbashi (centre et quartiers)
    private static final double[][] LUBUMBASHI_COORDS = {
            {-11.6642, 27.4661}, // Centre-ville
            {-11.6580, 27.4790}, // Quartier Golf
            {-11.6780, 27.4550}, // Quartier Bel-Air
            {-11.6900, 27.4700}, // Quartier Kamalondo
            {-11.6750, 27.4900}, // Quartier Kampemba
            {-11.6500, 27.5000}, // Quartier Kenya
            {-11.6400, 27.4800}, // Quartier Katuba
            {-11.6300, 27.4600}, // Quartier Ruashi
            {-11.6800, 27.5100}, // Quartier Annexe
            {-11.7000, 27.4800}  // Quartier Industriel
    };

    // Noms des quartiers de Lubumbashi
    private static final String[] QUARTIERS = {
            "Centre-ville", "Golf", "Bel-Air", "Kamalondo", "Kampemba",
            "Kenya", "Katuba", "Ruashi", "Annexe", "Industriel"
    };

    // Noms des avenues
    private static final String[] AVENUES = {
            "Av. Mobutu", "Av. Kasavubu", "Av. Lumumba", "Av. Kabila", "Av. Sendwe",
            "Av. Likasi", "Av. Kolwezi", "Av. Kambove", "Av. Kasai", "Av. Lomami",
            "Av. Luapula", "Av. Maniema", "Av. Katanga", "Av. Shaba", "Av. Kivu"
    };

    // Prénoms congolais
    private static final String[] PRENOMS = {
            "Jean", "Marie", "Joseph", "Pierre", "Paul",
            "Jacques", "Thérèse", "François", "Antoinette", "Michel",
            "Alphonse", "Bernadette", "Charles", "Dieudonné", "Emmanuel",
            "Françoise", "Gabriel", "Henriette", "Innocent", "Jeanne"
    };

    // Noms de famille congolais
    private static final String[] NOMS = {
            "Kabila", "Tshisekedi", "Mobutu", "Kasa-Vubu", "Lumumba",
            "Mukwege", "Mabele", "Ilunga", "Mutombo", "Kasongo",
            "Mwamba", "Kalala", "Tshimanga", "Mulumba", "Kabeya",
            "Nkongolo", "Tshibangu", "Kapinga", "Mbuyi", "Ngalula"
    };

    // Noms d'entreprises
    private static final String[] ENTREPRISES = {
            "Minière du Katanga", "Cimenterie de Lubumbashi", "Transport Express RDC",
            "Banque Commerciale du Congo", "Brasserie de Lubumbashi",
            "Société Textile du Katanga", "Hôtel Karavia", "Ferme Kipushi",
            "Imprimerie Moderne", "Boulangerie La Congolaise"
    };

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private BureauRepository bureauRepository;

    @Autowired
    private DivisionRepository divisionRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private ContribuableRepository contribuableRepository;

    @Autowired
    private ProprieteRepository proprieteRepository;

    @Autowired
    private ConcessionMinierRepository concessionMinierRepository;

    @Autowired
    private NatureImpotRepository natureImpotRepository;

    @Autowired
    private DeclarationRepository declarationRepository;

    @Autowired
    private TaxationRepository taxationRepository;

    @Autowired
    private PaiementRepository paiementRepository;

    @Autowired
    private DossierRecouvrementRepository dossierRecouvrementRepository;

    @Autowired
    private RelanceRepository relanceRepository;

    @Autowired
    private PoursuiteRepository poursuiteRepository;

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Autowired
    private PlaqueRepository plaqueRepository;

    @Autowired
    private CertificatRepository certificatRepository;

    @Autowired
    private VignetteRepository vignetteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Vérifier si des données existent déjà
        if (utilisateurRepository.count() > 0) {
            logger.info("ComprehensiveDataSeeder: données déjà présentes, on saute le seeding.");
            return;
        }

        try {
            logger.info("ComprehensiveDataSeeder: démarrage du seeding...");

            // Créer les divisions
            List<Division> divisions = createDivisions();
            divisionRepository.saveAll(divisions);
            
            // Créer les bureaux
            List<Bureau> bureaux = createBureaux(divisions);
            bureauRepository.saveAll(bureaux);
            
            // Créer les agents (sauf contribuables)
            List<Agent> agents = createAgents(bureaux);
            agentRepository.saveAll(agents);
            
            // Réactiver le seeding des contribuables
            logger.info("Réactivation du seeding des contribuables");
            List<Contribuable> contribuables = createContribuables(bureaux);
            
            // Sauvegarder les contribuables un par un avec gestion des erreurs
            List<Contribuable> savedContribuables = new ArrayList<>();
            for (Contribuable contribuable : contribuables) {
                try {
                    // Sauvegarder d'abord l'entité Agent
                    Agent savedAgent = agentRepository.save(contribuable);
                    
                    // Puis sauvegarder l'entité Contribuable
                    if (savedAgent instanceof Contribuable) {
                        savedContribuables.add((Contribuable) savedAgent);
                        logger.info("Contribuable sauvegardé avec succès: {}", contribuable.getNom());
                    }
                } catch (Exception e) {
                    logger.error("Erreur lors de la sauvegarde du contribuable {}: {}", contribuable.getNom(), e.getMessage());
                }
            }
            contribuables = savedContribuables;
            logger.info("{} contribuables sauvegardés", contribuables.size());
            
            // Créer les utilisateurs
            List<Utilisateur> utilisateurs = createUtilisateurs(agents, contribuables);
            utilisateurRepository.saveAll(utilisateurs);
            
            // Créer des propriétés pour les contribuables
            List<Propriete> proprietes = createProprietes(contribuables);
            proprieteRepository.saveAll(proprietes);

            // Créer des concessions minières
            List<ConcessionMinier> concessions = createConcessions(contribuables);
            concessionMinierRepository.saveAll(concessions);

            // Créer des natures d'impôt
            List<NatureImpot> naturesImpot = createNaturesImpot();
            natureImpotRepository.saveAll(naturesImpot);

            // Associer les natures d'impôt aux propriétés
            associateNaturesImpotToProprietes(proprietes, naturesImpot);

            // Créer des déclarations
            List<Declaration> declarations = createDeclarations(proprietes, concessions, agents, contribuables);
            declarationRepository.saveAll(declarations);

            // Créer des taxations
            List<Taxation> taxations = createTaxations(declarations, agents.get(0), naturesImpot);
            taxationRepository.saveAll(taxations);

            // Créer des paiements
            List<Paiement> paiements = createPaiements(taxations);
            paiementRepository.saveAll(paiements);

            // Créer des dossiers de recouvrement
            List<DossierRecouvrement> dossiers = createDossiersRecouvrement(contribuables);
            dossierRecouvrementRepository.saveAll(dossiers);

            // Créer des relances
            List<Relance> relances = createRelances(dossiers);
            relanceRepository.saveAll(relances);

            // Créer des poursuites
            List<Poursuite> poursuites = createPoursuites(dossiers, agents.get(0));
            poursuiteRepository.saveAll(poursuites);

            // Créer des véhicules
            List<Vehicule> vehicules = createVehicules(contribuables);
            vehiculeRepository.saveAll(vehicules);

            // Créer des plaques
            List<Plaque> plaques = createPlaques(vehicules);
            plaqueRepository.saveAll(plaques);

            // Créer des certificats
            List<Certificat> certificats = createCertificats(vehicules, declarations, agents.get(1));
            certificatRepository.saveAll(certificats);

            // Créer des vignettes
            List<Vignette> vignettes = createVignettes(vehicules, agents.get(1));
            vignetteRepository.saveAll(vignettes);

            logger.info("ComprehensiveDataSeeder: seeding terminé avec succès.");
        } catch (Exception e) {
            logger.error("ComprehensiveDataSeeder: erreur lors du seeding: " + e.getMessage(), e);
        }
    }

    /**
     * Crée des divisions
     */
    private List<Division> createDivisions() {
        List<Division> divisions = new ArrayList<>();
        
        // Division des Recettes
        Division d1 = new Division();
        d1.setNom("Division des Recettes");
        d1.setCode("DR");
        divisions.add(d1);
        
        // Division de la Fiscalité
        Division d2 = new Division();
        d2.setNom("Division de la Fiscalité");
        d2.setCode("DF");
        divisions.add(d2);
        
        // Division du Recouvrement
        Division d3 = new Division();
        d3.setNom("Division du Recouvrement");
        d3.setCode("DRE");
        divisions.add(d3);
        
        logger.info("Création de {} divisions", divisions.size());
        return divisions;
    }

    /**
     * Crée des bureaux
     */
    private List<Bureau> createBureaux(List<Division> divisions) {
        List<Bureau> bureaux = new ArrayList<>();
        
        // Bureau des Recettes Foncières
        Bureau b1 = new Bureau();
        b1.setNom("Bureau des Recettes Foncières");
        b1.setCode("BRF");
        b1.setDivision(divisions.get(0));
        bureaux.add(b1);
        
        // Bureau des Recettes Minières
        Bureau b2 = new Bureau();
        b2.setNom("Bureau des Recettes Minières");
        b2.setCode("BRM");
        b2.setDivision(divisions.get(0));
        bureaux.add(b2);
        
        // Bureau de la Fiscalité Immobilière
        Bureau b3 = new Bureau();
        b3.setNom("Bureau de la Fiscalité Immobilière");
        b3.setCode("BFI");
        b3.setDivision(divisions.get(1));
        bureaux.add(b3);
        
        // Bureau de la Fiscalité Minière
        Bureau b4 = new Bureau();
        b4.setNom("Bureau de la Fiscalité Minière");
        b4.setCode("BFM");
        b4.setDivision(divisions.get(1));
        bureaux.add(b4);
        
        // Bureau du Recouvrement
        Bureau b5 = new Bureau();
        b5.setNom("Bureau du Recouvrement");
        b5.setCode("BR");
        b5.setDivision(divisions.get(2));
        bureaux.add(b5);
        
        // Bureau des Contribuables
        Bureau b6 = new Bureau();
        b6.setNom("Bureau des Contribuables");
        b6.setCode("BC");
        b6.setDivision(divisions.get(2));
        bureaux.add(b6);
        
        logger.info("Création de {} bureaux", bureaux.size());
        return bureaux;
    }

    /**
     * Crée des agents (sauf contribuables)
     */
    private List<Agent> createAgents(List<Bureau> bureaux) {
        List<Agent> agents = new ArrayList<>();
        Random random = new Random(42);
        
        // Créer 5 agents avec différents rôles
        String[] noms = {"Mutombo Jean", "Kabila Marie", "Tshisekedi Pierre", "Mobutu Paul", "Lumumba Jacques"};
        Role[] roles = {Role.ADMIN, Role.DIRECTEUR, Role.TAXATEUR, Role.APUREUR, Role.CHEF_DE_BUREAU};
        
        for (int i = 0; i < noms.length; i++) {
            Agent agent = new Agent();
            agent.setNom(noms[i]);
            agent.setSexe(i % 2 == 0 ? Sexe.M : Sexe.F);
            agent.setMatricule("AGT-" + String.format("%03d", i + 1));
            agent.setBureau(bureaux.get(random.nextInt(bureaux.size())));
            agents.add(agent);
        }
        
        logger.info("Création de {} agents", agents.size());
        return agents;
    }

    /**
     * Crée 15 contribuables (12 personnes physiques et 3 personnes morales)
     */
    private List<Contribuable> createContribuables(List<Bureau> bureaux) {
        List<Contribuable> contribuables = new ArrayList<>();
        Random random = new Random(42);
        Bureau bureauContribuables = bureaux.stream()
                .filter(b -> "Bureau des Contribuables".equals(b.getNom()))
                .findFirst()
                .orElse(bureaux.get(0));
        
        // Créer 12 contribuables personnes physiques
        for (int i = 0; i < 12; i++) {
            String prenom = PRENOMS[i % PRENOMS.length];
            String nom = NOMS[i % NOMS.length];
            
            Contribuable contribuable = new Contribuable();
            
            // Initialiser d'abord les champs de Agent
            contribuable.setNom(nom + " " + prenom);
            contribuable.setSexe(i % 2 == 0 ? Sexe.M : Sexe.F);
            contribuable.setMatricule("CONT-" + String.format("%03d", i + 1));
            contribuable.setBureau(bureauContribuables);
            
            // Puis les champs spécifiques à Contribuable
            contribuable.setAdressePrincipale(AVENUES[i % AVENUES.length] + " " + (i * 10 + 5) + ", " + QUARTIERS[i % QUARTIERS.length]);
            contribuable.setTelephonePrincipal("+243820" + String.format("%06d", 100000 + i));
            contribuable.setEmail(prenom.toLowerCase() + "." + nom.toLowerCase() + "@example.com");
            contribuable.setNationalite("RDC");
            contribuable.setType(TypeContribuable.PERSONNE_PHYSIQUE);
            contribuable.setIdNat("IDNAT-" + String.format("%06d", 100000 + i));
            contribuable.setNumeroIdentificationContribuable("NIF-" + String.format("%09d", 100000000 + i));
            
            contribuables.add(contribuable);
        }
        
        // Créer 3 contribuables personnes morales
        for (int i = 0; i < 3; i++) {
            String nomEntreprise = ENTREPRISES[i % ENTREPRISES.length] + " " + (i + 1) + " SARL";
            
            Contribuable contribuable = new Contribuable();
            
            // Initialiser les champs de Agent
            contribuable.setNom(nomEntreprise);
            contribuable.setSexe(Sexe.M); // Par défaut pour les personnes morales
            contribuable.setMatricule("CONT-" + String.format("%03d", 12 + i + 1));
            contribuable.setBureau(bureauContribuables);
            
            // Initialiser les champs spécifiques à Contribuable
            contribuable.setAdressePrincipale(AVENUES[i % AVENUES.length] + " " + (i * 20 + 10) + ", " + QUARTIERS[(i + 5) % QUARTIERS.length]);
            contribuable.setAdresseSecondaire("Zone Industrielle " + (i + 1) + ", Lubumbashi");
            contribuable.setTelephonePrincipal("+243990" + String.format("%06d", 200000 + i));
            contribuable.setTelephoneSecondaire("+243815" + String.format("%06d", 200000 + i));
            contribuable.setEmail("contact@" + nomEntreprise.toLowerCase().replace(" ", "").replace("-", "") + ".cd");
            contribuable.setNationalite("RDC");
            contribuable.setType(TypeContribuable.PERSONNE_MORALE);
            contribuable.setIdNat("IDNAT-PM-" + String.format("%04d", 1000 + i));
            contribuable.setNRC("RCCM-CD/LUB/" + (2020 + i) + "-B-" + String.format("%04d", 1000 + i));
            contribuable.setSigle(nomEntreprise.substring(0, 3).toUpperCase() + String.format("%02d", i + 1));
            contribuable.setNumeroIdentificationContribuable("NIF-PM-" + String.format("%07d", 1000000 + i));
            
            contribuables.add(contribuable);
        }
        
        logger.info("Création de {} contribuables", contribuables.size());
        return contribuables;
    }

    /**
     * Crée des utilisateurs pour les agents et les contribuables
     */
    private List<Utilisateur> createUtilisateurs(List<Agent> agents, List<Contribuable> contribuables) {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        Random random = new Random(42);
        
        // Créer des utilisateurs pour les agents
        Role[] roles = {Role.ADMIN, Role.DIRECTEUR, Role.TAXATEUR, Role.APUREUR, Role.CHEF_DE_BUREAU};
        for (int i = 0; i < agents.size(); i++) {
            Agent agent = agents.get(i);
            
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setLogin("agent" + (i + 1));
            utilisateur.setMotDePasse(passwordEncoder.encode("Tabc@123"));
            utilisateur.setRole(roles[i % roles.length]);
            utilisateur.setPremierConnexion(false);
            utilisateur.setBloque(false);
            utilisateur.setActif(true);
            utilisateur.setAgent(agent);
            
            utilisateurs.add(utilisateur);
            agent.setUtilisateur(utilisateur);
        }
        
        // Créer des utilisateurs pour les contribuables avec le format dpri_c + 4 caractères aléatoires
        for (Contribuable contribuable : contribuables) {
            String username = generateUsername();
            String password = "Tabc@123"; // Mot de passe par défaut
            
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setLogin(username);
            utilisateur.setMotDePasse(passwordEncoder.encode(password));
            utilisateur.setRole(Role.CONTRIBUABLE);
            utilisateur.setPremierConnexion(true);
            utilisateur.setBloque(false);
            utilisateur.setActif(true);
            utilisateur.setContribuable(contribuable);
            contribuable.setUtilisateur(utilisateur);
            
            utilisateurs.add(utilisateur);
            
            // Envoyer les identifiants par email (simulation)
            logger.info("Envoi des identifiants à {} : username={}, password={}", 
                    contribuable.getEmail(), username, password);
        }
        
        logger.info("Création de {} utilisateurs", utilisateurs.size());
        return utilisateurs;
    }
    
    /**
     * Génère un nom d'utilisateur unique pour un contribuable
     * @return Le nom d'utilisateur généré
     */
    private String generateUsername() {
        String prefix = "dpri_c";
        String randomChars = generateRandomChars(4);
        String username = prefix + randomChars;
        
        // Vérifier si le nom d'utilisateur existe déjà
        while (utilisateurRepository.findByLogin(username).isPresent()) {
            randomChars = generateRandomChars(4);
            username = prefix + randomChars;
        }
        
        return username;
    }

    /**
     * Génère une chaîne aléatoire de caractères
     * @param length La longueur de la chaîne
     * @return La chaîne générée
     */
    private String generateRandomChars(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        
        return sb.toString();
    }

    /**
     * Crée des propriétés pour les contribuables avec coordonnées géographiques de Lubumbashi
     * Chaque contribuable aura au moins 2 propriétés
     */
    private List<Propriete> createProprietes(List<Contribuable> contribuables) {
        List<Propriete> proprietes = new ArrayList<>();
        Random random = new Random(42);
        GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
        
        // Types de propriétés disponibles
        TypePropriete[] types = {TypePropriete.VI, TypePropriete.AP, TypePropriete.TE, TypePropriete.DEPOT, TypePropriete.CH, TypePropriete.CITERNE};
        
        // Pour chaque contribuable, créer au moins 2 propriétés
        for (int i = 0; i < contribuables.size(); i++) {
            Contribuable contribuable = contribuables.get(i);
            int nbProprietes = 2 + random.nextInt(3); // Entre 2 et 4 propriétés par contribuable
            
            for (int j = 0; j < nbProprietes; j++) {
                // Sélectionner un quartier aléatoire de Lubumbashi
                int quartierIndex = random.nextInt(QUARTIERS.length);
                String quartier = QUARTIERS[quartierIndex];
                
                // Coordonnées de base du quartier
                double baseLatitude = LUBUMBASHI_COORDS[quartierIndex][0];
                double baseLongitude = LUBUMBASHI_COORDS[quartierIndex][1];
                
                // Ajouter une petite variation aléatoire (±0.005 degrés, environ 500m)
                double latitude = baseLatitude + (random.nextDouble() - 0.5) * 0.01;
                double longitude = baseLongitude + (random.nextDouble() - 0.5) * 0.01;
                
                // Créer un point géographique
                Point point = gf.createPoint(new Coordinate(longitude, latitude));
                
                // Sélectionner un type de propriété aléatoire
                TypePropriete type = types[random.nextInt(types.length)];
                
                // Calculer une superficie en fonction du type
                double superficie = 0.0;
                switch (type) {
                    case VI:
                        superficie = 200 + random.nextDouble() * 800; // Entre 200 et 1000 m²
                        break;
                    case AP:
                        superficie = 50 + random.nextDouble() * 150; // Entre 50 et 200 m²
                        break;
                    case TE:
                        superficie = 1000 + random.nextDouble() * 9000; // Entre 1000 et 10000 m²
                        break;
                    case DEPOT:
                        superficie = 500 + random.nextDouble() * 1500; // Entre 500 et 2000 m²
                        break;
                    case CH:
                        superficie = 100 + random.nextDouble() * 400; // Entre 100 et 500 m²
                        break;
                    case CITERNE:
                        superficie = 50 + random.nextDouble() * 150; // Entre 50 et 200 m²
                        break;
                    default:
                        superficie = 100 + random.nextDouble() * 900; // Entre 100 et 1000 m²
                }
                
                // Créer la propriété
                Propriete propriete = new Propriete();
                propriete.setType(type);
                propriete.setLocalite(quartier);
                propriete.setRangLocalite(1 + random.nextInt(3)); // Rang entre 1 et 3
                propriete.setSuperficie(superficie);
                propriete.setAdresse(AVENUES[random.nextInt(AVENUES.length)] + " " + (random.nextInt(100) + 1) + ", " + quartier);
                propriete.setProprietaire(contribuable);
                propriete.setLocation(point);
                propriete.setActif(true);
                propriete.setDeclare(true);
                propriete.setDeclarationEnLigne(random.nextBoolean());
                
                // Calculer un montant d'impôt approximatif basé sur la superficie
                double montantImpot = superficie * (0.5 + random.nextDouble() * 1.5);
                propriete.setMontantImpot(montantImpot);
                
                proprietes.add(propriete);
            }
        }
        
        logger.info("Création de {} propriétés avec coordonnées géographiques à Lubumbashi", proprietes.size());
        return proprietes;
    }

    /**
     * Crée des concessions minières pour les contribuables personnes morales
     */
    private List<ConcessionMinier> createConcessions(List<Contribuable> contribuables) {
        List<ConcessionMinier> concessions = new ArrayList<>();
        Random random = new Random(42);
        
        // Filtrer les contribuables personnes morales
        List<Contribuable> entreprises = contribuables.stream()
                .filter(c -> c.getType() == TypeContribuable.PERSONNE_MORALE)
                .collect(Collectors.toList());
        
        // Pour chaque entreprise, créer 1 ou 2 concessions minières
        for (Contribuable entreprise : entreprises) {
            int nbConcessions = 1 + random.nextInt(2); // 1 ou 2 concessions par entreprise
            
            for (int i = 0; i < nbConcessions; i++) {
                ConcessionMinier concession = new ConcessionMinier();
                
                // Type de concession aléatoire
                TypeConcession[] types = TypeConcession.values();
                TypeConcession type = types[random.nextInt(types.length)];
                concession.setType(type);
                
                // Nombre de carrés miniers (entre 5 et 20)
                double nombreCarres = 5 + random.nextDouble() * 15;
                concession.setNombreCarresMinier(nombreCarres);
                
                // Date d'acquisition (entre 1 et 5 ans dans le passé)
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.YEAR, -1 * (1 + random.nextInt(5)));
                concession.setDateAcquisition(cal.getTime());
                
                // Annexe
                concession.setAnnexe("Annexe-" + entreprise.getSigle() + "-" + (i + 1));
                
                // Titulaire
                concession.setTitulaire(entreprise);
                
                // Montant de l'impôt (basé sur le nombre de carrés miniers)
                double tauxBase = type == TypeConcession.EXPLOITATION ? 85.0 : 42.5;
                double montantImpot = nombreCarres * tauxBase;
                concession.setMontantImpot(montantImpot);
                
                concessions.add(concession);
            }
        }
        
        logger.info("Création de {} concessions minières", concessions.size());
        return concessions;
    }

    /**
     * Crée des natures d'impôt à partir du fichier impots.json
     */
    private List<NatureImpot> createNaturesImpot() {
        List<NatureImpot> naturesImpot = new ArrayList<>();
        
        // Impôt Foncier (IF)
        NatureImpot ni1 = new NatureImpot();
        ni1.setCode("IF");
        ni1.setNom("Impôt Foncier");
        ni1.setDescription("Impôt sur les propriétés foncières");
        ni1.setActif(true);
        naturesImpot.add(ni1);
        
        // Impôt sur Revenu Locatif (IRL)
        NatureImpot ni2 = new NatureImpot();
        ni2.setCode("IRL");
        ni2.setNom("Impôt sur Revenu Locatif");
        ni2.setDescription("Impôt sur les revenus locatifs des propriétés");
        ni2.setActif(true);
        naturesImpot.add(ni2);
        
        // Impôt sur Concessions Minières (ICM)
        NatureImpot ni3 = new NatureImpot();
        ni3.setCode("ICM");
        ni3.setNom("Impôt sur Concessions Minières");
        ni3.setDescription("Impôt sur les concessions minières");
        ni3.setActif(true);
        naturesImpot.add(ni3);
        
        // Impôt Réel sur Véhicule (IRV)
        NatureImpot ni4 = new NatureImpot();
        ni4.setCode("IRV");
        ni4.setNom("Impôt Réel sur Véhicule");
        ni4.setDescription("Impôt réel sur les véhicules ou vignette");
        ni4.setActif(true);
        naturesImpot.add(ni4);
        
        // Retenue Locative (RL)
        NatureImpot ni5 = new NatureImpot();
        ni5.setCode("RL");
        ni5.setNom("Retenue Locative");
        ni5.setDescription("Retenue locative correspondant à 20% de l'IRL");
        ni5.setActif(true);
        naturesImpot.add(ni5);
        
        logger.info("Création de {} natures d'impôt", naturesImpot.size());
        return naturesImpot;
    }
    
    /**
     * Associe les natures d'impôt aux propriétés
     */
    private void associateNaturesImpotToProprietes(List<Propriete> proprietes, List<NatureImpot> naturesImpot) {
        Random random = new Random(42);
        
        // Pour chaque propriété, associer au moins une nature d'impôt
        for (Propriete propriete : proprietes) {
            // Toutes les propriétés ont l'impôt foncier (IF)
            NatureImpot impotFoncier = naturesImpot.stream()
                    .filter(ni -> "IF".equals(ni.getCode()))
                    .findFirst()
                    .orElse(naturesImpot.get(0));
            
            propriete.ajouterNatureImpot(impotFoncier);
            
            // 50% des propriétés ont aussi l'impôt sur revenu locatif (IRL)
            if (random.nextBoolean()) {
                NatureImpot impotLocatif = naturesImpot.stream()
                        .filter(ni -> "IRL".equals(ni.getCode()))
                        .findFirst()
                        .orElse(null);
                
                if (impotLocatif != null) {
                    propriete.ajouterNatureImpot(impotLocatif);
                    
                    // Si une propriété a l'IRL, elle a aussi la retenue locative (RL)
                    NatureImpot retenueLocative = naturesImpot.stream()
                            .filter(ni -> "RL".equals(ni.getCode()))
                            .findFirst()
                            .orElse(null);
                    
                    if (retenueLocative != null) {
                        propriete.ajouterNatureImpot(retenueLocative);
                    }
                }
            }
        }
        
        logger.info("Association des natures d'impôt aux propriétés terminée");
    }

    /**
     * Crée des déclarations pour les propriétés et concessions
     * La déclaration est le fait d'enregistrer un bien au nom d'un contribuable
     */
    private List<Declaration> createDeclarations(List<Propriete> proprietes, List<ConcessionMinier> concessions, 
                                              List<Agent> agents, List<Contribuable> contribuables) {
        List<Declaration> declarations = new ArrayList<>();
        Random random = new Random(42);
        Calendar cal = Calendar.getInstance();
        
        // Sélectionner un agent validateur (rôle TAXATEUR ou CHEF_DE_BUREAU)
        Agent agentValidateur = agents.get(0); // Par défaut, le premier agent
        
        // Pour chaque propriété, créer une déclaration
        for (Propriete propriete : proprietes) {
            // Date de déclaration (entre 1 et 12 mois dans le passé)
            cal.setTime(new Date());
            cal.add(Calendar.MONTH, -1 * (1 + random.nextInt(12)));
            Date dateDeclaration = cal.getTime();
            
            // Statut de la déclaration
            StatutDeclaration statut = random.nextDouble() < 0.8 ? 
                    StatutDeclaration.VALIDEE : StatutDeclaration.SOUMISE;
            
            // Source de la déclaration (en ligne ou à l'administration)
            SourceDeclaration source = propriete.isDeclarationEnLigne() ? 
                    SourceDeclaration.EN_LIGNE : SourceDeclaration.ADMINISTRATION;
            
            // Créer la déclaration
            Declaration declaration = new Declaration();
            declaration.setDateDeclaration(dateDeclaration);
            declaration.setStatut(statut);
            declaration.setSource(source);
            declaration.setPropriete(propriete);
            declaration.setContribuable(propriete.getProprietaire());
            
            // Si la déclaration est validée, associer un agent validateur
            if (statut == StatutDeclaration.VALIDEE) {
                declaration.setAgentValidateur(agentValidateur);
            }
            
            declaration.setActif(true);
            declarations.add(declaration);
        }
        
        // Pour chaque concession, créer une déclaration
        for (ConcessionMinier concession : concessions) {
            // Date de déclaration (entre 1 et 12 mois dans le passé)
            cal.setTime(new Date());
            cal.add(Calendar.MONTH, -1 * (1 + random.nextInt(12)));
            Date dateDeclaration = cal.getTime();
            
            // Statut de la déclaration (toujours validée pour les concessions)
            StatutDeclaration statut = StatutDeclaration.VALIDEE;
            
            // Source de la déclaration (toujours à l'administration pour les concessions)
            SourceDeclaration source = SourceDeclaration.ADMINISTRATION;
            
            // Créer la déclaration
            Declaration declaration = new Declaration();
            declaration.setDateDeclaration(dateDeclaration);
            declaration.setStatut(statut);
            declaration.setSource(source);
            declaration.setConcession(concession);
            declaration.setContribuable(concession.getTitulaire());
            declaration.setAgentValidateur(agentValidateur);
            declaration.setActif(true);
            declarations.add(declaration);
        }
        
        logger.info("Création de {} déclarations", declarations.size());
        return declarations;
    }

    /**
     * Crée des taxations pour les déclarations validées
     * La taxation est le fait de taxer un bien déjà déclaré
     */
    private List<Taxation> createTaxations(List<Declaration> declarations, Agent agentTaxateur, List<NatureImpot> naturesImpot) {
        List<Taxation> taxations = new ArrayList<>();
        Random random = new Random(42);
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        
        // Pour chaque déclaration validée, créer une taxation
        for (Declaration declaration : declarations) {
            if (declaration.getStatut() == StatutDeclaration.VALIDEE) {
                // Date de taxation (après la date de déclaration)
                cal.setTime(declaration.getDateDeclaration());
                cal.add(Calendar.DAY_OF_MONTH, 1 + random.nextInt(30)); // Entre 1 et 30 jours après la déclaration
                Date dateTaxation = cal.getTime();
                
                // Déterminer le type d'impôt en fonction du bien
                TypeImpot typeImpot;
                NatureImpot natureImpot = null;
                
                if (declaration.getPropriete() != null) {
                    // Pour les propriétés, utiliser l'impôt foncier (IF)
                    typeImpot = TypeImpot.IF;
                    natureImpot = naturesImpot.stream()
                            .filter(ni -> "IF".equals(ni.getCode()))
                            .findFirst()
                            .orElse(null);
                } else if (declaration.getConcession() != null) {
                    // Pour les concessions, utiliser l'impôt sur concessions minières (ICM)
                    typeImpot = TypeImpot.ICM;
                    natureImpot = naturesImpot.stream()
                            .filter(ni -> "ICM".equals(ni.getCode()))
                            .findFirst()
                            .orElse(null);
                } else {
                    // Cas par défaut
                    typeImpot = TypeImpot.IF;
                }
                
                // Calculer le montant de la taxation
                double montant = 0.0;
                if (declaration.getPropriete() != null) {
                    // Pour les propriétés, calculer en fonction de la superficie
                    montant = declaration.getPropriete().getSuperficie() * (0.5 + random.nextDouble());
                } else if (declaration.getConcession() != null) {
                    // Pour les concessions, calculer en fonction du nombre de carrés miniers
                    montant = declaration.getConcession().getNombreCarresMinier() * 85.0;
                } else {
                    // Valeur par défaut
                    montant = 1000.0 + random.nextDouble() * 9000.0;
                }
                
                // Créer la taxation
                Taxation taxation = new Taxation();
                taxation.setDateTaxation(dateTaxation);
                taxation.setMontant(montant);
                taxation.setExercice(String.valueOf(currentYear));
                taxation.setStatut(StatutTaxation.VALIDEE);
                taxation.setTypeImpot(typeImpot);
                taxation.setExoneration(false);
                taxation.setDeclaration(declaration);
                taxation.setAgent(agentTaxateur);
                
                // Associer la nature d'impôt si disponible
                if (natureImpot != null) {
                    taxation.setNatureImpot(natureImpot);
                }
                
                taxation.setActif(true);
                taxations.add(taxation);
            }
        }
        
        logger.info("Création de {} taxations", taxations.size());
        return taxations;
    }

    /**
     * Crée des paiements pour les taxations validées
     */
    private List<Paiement> createPaiements(List<Taxation> taxations) {
        List<Paiement> paiements = new ArrayList<>();
        Random random = new Random(42);
        
        // Pour chaque taxation validée, créer un paiement
        for (Taxation taxation : taxations) {
            if (taxation.getStatut() == StatutTaxation.VALIDEE) {
                // 80% des taxations sont payées
                if (random.nextDouble() < 0.8) {
                    // Date de paiement (entre 1 et 30 jours après la taxation)
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(taxation.getDateTaxation());
                    cal.add(Calendar.DAY_OF_MONTH, 1 + random.nextInt(30));
                    Date datePaiement = cal.getTime();
                    
                    // Mode de paiement aléatoire
                    ModePaiement[] modes = ModePaiement.values();
                    ModePaiement mode = modes[random.nextInt(modes.length)];
                    
                    // Créer le paiement
                    Paiement paiement = new Paiement();
                    paiement.setDate(datePaiement);
                    paiement.setMontant(taxation.getMontant());
                    paiement.setMode(mode);
                    paiement.setStatut(StatutPaiement.VALIDE);
                    paiement.setBordereauBancaire("BORD-" + String.format("%06d", random.nextInt(1000000)));
                    paiement.setTaxation(taxation);
                    paiement.setActif(true);
                    
                    paiements.add(paiement);
                    
                    // Mettre à jour le statut de la taxation
                    taxation.setStatut(StatutTaxation.PAYEE);
                    taxation.setPaiement(paiement);
                }
            }
        }
        
        logger.info("Création de {} paiements", paiements.size());
        return paiements;
    }
    
    /**
     * Crée des dossiers de recouvrement pour les contribuables
     */
    private List<DossierRecouvrement> createDossiersRecouvrement(List<Contribuable> contribuables) {
        List<DossierRecouvrement> dossiers = new ArrayList<>();
        Random random = new Random(42);
        
        // Pour 20% des contribuables, créer un dossier de recouvrement
        for (Contribuable contribuable : contribuables) {
            if (random.nextDouble() < 0.2) {
                // Montant total dû (entre 10000 et 100000)
                double totalDu = 10000 + random.nextDouble() * 90000;
                
                // Montant recouvré (entre 0 et 50% du montant dû)
                double totalRecouvre = totalDu * random.nextDouble() * 0.5;
                
                // Date d'ouverture (entre 1 et 6 mois dans le passé)
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MONTH, -1 * (1 + random.nextInt(6)));
                Date dateOuverture = cal.getTime();
                
                // Créer le dossier de recouvrement
                DossierRecouvrement dossier = new DossierRecouvrement();
                dossier.setTotalDu(totalDu);
                dossier.setTotalRecouvre(totalRecouvre);
                dossier.setDateOuverture(dateOuverture);
                dossier.setContribuable(contribuable);
                
                dossiers.add(dossier);
            }
        }
        
        logger.info("Création de {} dossiers de recouvrement", dossiers.size());
        return dossiers;
    }
    
    /**
     * Crée des relances pour les dossiers de recouvrement
     */
    private List<Relance> createRelances(List<DossierRecouvrement> dossiers) {
        List<Relance> relances = new ArrayList<>();
        Random random = new Random(42);
        
        // Pour chaque dossier, créer entre 1 et 3 relances
        for (DossierRecouvrement dossier : dossiers) {
            int nbRelances = 1 + random.nextInt(3);
            
            for (int i = 0; i < nbRelances; i++) {
                // Date d'envoi (entre 1 et 30 jours après l'ouverture du dossier)
                Calendar cal = Calendar.getInstance();
                cal.setTime(dossier.getDateOuverture());
                cal.add(Calendar.DAY_OF_MONTH, (i + 1) * (1 + random.nextInt(30)));
                Date dateEnvoi = cal.getTime();
                
                // Type de relance aléatoire
                TypeRelance[] types = TypeRelance.values();
                TypeRelance type = types[random.nextInt(types.length)];
                
                // Statut de la relance
                StatutRelance statut = random.nextDouble() < 0.8 ? 
                        StatutRelance.ENVOYEE : StatutRelance.LU;
                
                // Créer la relance
                Relance relance = new Relance();
                relance.setDateEnvoi(dateEnvoi);
                relance.setType(type);
                relance.setStatut(statut);
                relance.setContenu("Veuillez régulariser votre situation fiscale dans les plus brefs délais.");
                relance.setDossierRecouvrement(dossier);
                
                relances.add(relance);
            }
        }
        
        logger.info("Création de {} relances", relances.size());
        return relances;
    }
    
    /**
     * Crée des poursuites pour les dossiers de recouvrement
     */
    private List<Poursuite> createPoursuites(List<DossierRecouvrement> dossiers, Agent agent) {
        List<Poursuite> poursuites = new ArrayList<>();
        Random random = new Random(42);
        
        // Pour 50% des dossiers, créer une poursuite
        for (DossierRecouvrement dossier : dossiers) {
            if (random.nextDouble() < 0.5) {
                // Date de lancement (entre 1 et 30 jours après l'ouverture du dossier)
                Calendar cal = Calendar.getInstance();
                cal.setTime(dossier.getDateOuverture());
                cal.add(Calendar.DAY_OF_MONTH, 1 + random.nextInt(30));
                Date dateLancement = cal.getTime();
                
                // Type de poursuite aléatoire
                TypePoursuite[] types = TypePoursuite.values();
                TypePoursuite type = types[random.nextInt(types.length)];
                
                // Statut de la poursuite
                StatutPoursuite statut = random.nextDouble() < 0.7 ? 
                        StatutPoursuite.EN_COURS : StatutPoursuite.CLOTUREE;
                
                // Montant recouvré (entre 0 et 50% du montant dû)
                double montantRecouvre = statut == StatutPoursuite.CLOTUREE ?
                        dossier.getTotalDu() * (0.5 + random.nextDouble() * 0.5) : 0.0;
                
                // Créer la poursuite
                Poursuite poursuite = new Poursuite();
                poursuite.setType(type);
                poursuite.setDateLancement(dateLancement);
                poursuite.setStatut(statut);
                poursuite.setMontantRecouvre(montantRecouvre);
                poursuite.setAgentInitiateur(agent);
                poursuite.setDossierRecouvrement(dossier);
                
                poursuites.add(poursuite);
            }
        }
        
        logger.info("Création de {} poursuites", poursuites.size());
        return poursuites;
    }
    
    /**
     * Crée des véhicules pour les contribuables
     */
    private List<Vehicule> createVehicules(List<Contribuable> contribuables) {
        List<Vehicule> vehicules = new ArrayList<>();
        Random random = new Random(42);
        
        // Marques et modèles de véhicules
        String[][] marquesModeles = {
                {"TOYOTA", "Hilux"}, {"TOYOTA", "Land Cruiser"}, {"TOYOTA", "Corolla"},
                {"MERCEDES", "Classe C"}, {"MERCEDES", "Sprinter"}, {"MERCEDES", "GLE"},
                {"NISSAN", "Patrol"}, {"NISSAN", "Navara"}, {"NISSAN", "X-Trail"},
                {"MITSUBISHI", "Pajero"}, {"MITSUBISHI", "L200"}, {"MITSUBISHI", "ASX"},
                {"FORD", "Ranger"}, {"FORD", "Explorer"}, {"FORD", "F-150"}
        };
        
        // Pour chaque contribuable, créer entre 0 et 2 véhicules
        for (Contribuable contribuable : contribuables) {
            int nbVehicules = random.nextInt(3); // Entre 0 et 2 véhicules par contribuable
            
            for (int i = 0; i < nbVehicules; i++) {
                // Sélectionner une marque et un modèle aléatoires
                int index = random.nextInt(marquesModeles.length);
                String marque = marquesModeles[index][0];
                String modele = marquesModeles[index][1];
                
                // Année (entre 2010 et 2023)
                int annee = 2010 + random.nextInt(14);
                
                // Immatriculation
                String immatriculation = String.format("%03d", random.nextInt(1000)) + 
                        (char)('A' + random.nextInt(26)) + 
                        (char)('A' + random.nextInt(26)) + 
                        String.format("%02d", random.nextInt(100));
                
                // Numéro de châssis
                String numeroChassis = marque.substring(0, 3).toUpperCase() + "-" + 
                        String.format("%09d", random.nextInt(1000000000));
                
                // Date d'enregistrement (entre 1 et 5 ans dans le passé)
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.YEAR, -1 * (1 + random.nextInt(5)));
                Date dateEnregistrement = cal.getTime();
                
                // Créer le véhicule
                Vehicule vehicule = new Vehicule();
                vehicule.setMarque(marque);
                vehicule.setModele(modele);
                vehicule.setAnnee(annee);
                vehicule.setImmatriculation(immatriculation);
                vehicule.setNumeroChassis(numeroChassis);
                vehicule.setDateEnregistrement(dateEnregistrement);
                vehicule.setProprietaire(contribuable);
                
                vehicules.add(vehicule);
            }
        }
        
        logger.info("Création de {} véhicules", vehicules.size());
        return vehicules;
    }
    
    /**
     * Crée des plaques pour les véhicules
     */
    private List<Plaque> createPlaques(List<Vehicule> vehicules) {
        List<Plaque> plaques = new ArrayList<>();
        Random random = new Random(42);
        
        // Pour chaque véhicule, créer une plaque
        for (Vehicule vehicule : vehicules) {
            // Numéro de série
            String numeroSerie = "SER-" + vehicule.getImmatriculation();
            
            // Créer la plaque
            Plaque plaque = new Plaque();
            plaque.setNumeroSerie(numeroSerie);
            plaque.setDisponible(false);
            plaque.setNumplaque(vehicule.getImmatriculation());
            plaque.setVehicule(vehicule);
            plaque.setCodeQR("QR-" + UUID.randomUUID().toString());
            plaque.setDocument("plaque_" + vehicule.getImmatriculation() + ".pdf");
            
            plaques.add(plaque);
        }
        
        logger.info("Création de {} plaques", plaques.size());
        return plaques;
    }
    
    /**
     * Crée des certificats pour les véhicules
     */
    private List<Certificat> createCertificats(List<Vehicule> vehicules, List<Declaration> declarations, Agent agent) {
        List<Certificat> certificats = new ArrayList<>();
        Random random = new Random(42);
        
        // Pour chaque véhicule, créer un certificat
        for (int i = 0; i < vehicules.size(); i++) {
            Vehicule vehicule = vehicules.get(i);
            
            // Numéro de certificat
            String numero = "CERT-" + String.format("%06d", i + 1);
            
            // Date d'émission (entre 1 et 12 mois dans le passé)
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1 * (1 + random.nextInt(12)));
            Date dateEmission = cal.getTime();
            
            // Date d'expiration (1 an après la date d'émission)
            cal.setTime(dateEmission);
            cal.add(Calendar.YEAR, 1);
            Date dateExpiration = cal.getTime();
            
            // Montant (entre 100 et 500)
            double montant = 100 + random.nextDouble() * 400;
            
            // Créer le certificat
            Certificat certificat = new Certificat();
            certificat.setNumero(numero);
            certificat.setDateEmission(dateEmission);
            certificat.setDateExpiration(dateExpiration);
            certificat.setMontant(montant);
            certificat.setVehicule(vehicule);
            certificat.setAgent(agent);
            
            // Associer une déclaration si disponible
            if (i < declarations.size()) {
                certificat.setDeclaration(declarations.get(i));
            }
            
            certificats.add(certificat);
        }
        
        logger.info("Création de {} certificats", certificats.size());
        return certificats;
    }
    
    /**
     * Crée des vignettes pour les véhicules
     */
    private List<Vignette> createVignettes(List<Vehicule> vehicules, Agent agent) {
        List<Vignette> vignettes = new ArrayList<>();
        Random random = new Random(42);
        
        // Pour chaque véhicule, créer une vignette
        for (int i = 0; i < vehicules.size(); i++) {
            Vehicule vehicule = vehicules.get(i);
            
            // Numéro de vignette
            String numero = "VIG-" + String.format("%06d", i + 1);
            
            // Date d'émission (entre 1 et 12 mois dans le passé)
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1 * (1 + random.nextInt(12)));
            Date dateEmission = cal.getTime();
            
            // Date d'expiration (1 an après la date d'émission)
            cal.setTime(dateEmission);
            cal.add(Calendar.YEAR, 1);
            Date dateExpiration = cal.getTime();
            
            // Montant (entre 50 et 200)
            double montant = 50 + random.nextDouble() * 150;
            
            // Créer la vignette
            Vignette vignette = new Vignette();
            vignette.setNumero(numero);
            vignette.setDateEmission(dateEmission);
            vignette.setDateExpiration(dateExpiration);
            vignette.setMontant(montant);
            vignette.setVehicule(vehicule);
            vignette.setAgent(agent);
            vignette.setCodeQR("QR-" + UUID.randomUUID().toString());
            vignette.setDocument("vignette_" + numero + ".pdf");
            
            vignettes.add(vignette);
        }
        
        logger.info("Création de {} vignettes", vignettes.size());
        return vignettes;
    }
}
