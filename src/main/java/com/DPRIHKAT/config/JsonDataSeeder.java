package com.DPRIHKAT.config;

import com.DPRIHKAT.entity.*;
import com.DPRIHKAT.entity.enums.*;
import com.DPRIHKAT.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.*;

/**
 * Seeder qui lit les données depuis le fichier datas.json dans les ressources
 */
@Component
@Order(6)
public class JsonDataSeeder implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final AgentRepository agentRepository;
    private final BureauRepository bureauRepository;
    private final DivisionRepository divisionRepository;
    private final ContribuableRepository contribuableRepository;
    private final ProprieteRepository proprieteRepository;
    private final PasswordEncoder passwordEncoder;

    public JsonDataSeeder(
            UtilisateurRepository utilisateurRepository,
            AgentRepository agentRepository,
            BureauRepository bureauRepository,
            DivisionRepository divisionRepository,
            ContribuableRepository contribuableRepository,
            ProprieteRepository proprieteRepository,
            PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.agentRepository = agentRepository;
        this.bureauRepository = bureauRepository;
        this.divisionRepository = divisionRepository;
        this.contribuableRepository = contribuableRepository;
        this.proprieteRepository = proprieteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Vérifier si des données existent déjà
        if (utilisateurRepository.count() > 0) {
            System.out.println("JsonDataSeeder: données déjà présentes, on saute le seeding.");
            return;
        }

        try {
            System.out.println("JsonDataSeeder: démarrage du seeding...");
            
            // Charger le fichier JSON depuis les ressources
            ClassPathResource resource = new ClassPathResource("datas.json");
            InputStream inputStream = resource.getInputStream();
            
            // Parser le JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(inputStream);
            
            // Créer les données de base
            Map<String, Object> baseData = createBaseData();
            Bureau bureau = (Bureau) baseData.get("bureau");
            
            // Créer les contribuables et propriétés à partir des données JSON
            createContribuablesAndProperties(rootNode, bureau);
            
            System.out.println("JsonDataSeeder: seeding terminé avec succès.");
        } catch (Exception e) {
            System.err.println("JsonDataSeeder: erreur lors du seeding: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private Map<String, Object> createBaseData() {
        Map<String, Object> baseData = new HashMap<>();
        
        try {
            // Créer une division
            Division division = new Division();
            division.setNom("Division Principale");
            division.setCode("DP");
            divisionRepository.save(division);

            // Créer un bureau
            Bureau bureau = new Bureau();
            bureau.setNom("Bureau Principal");
            bureau.setCode("BP");
            bureau.setDivision(division);
            bureauRepository.save(bureau);
            
            baseData.put("division", division);
            baseData.put("bureau", bureau);
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la création des données de base: " + e.getMessage());
            e.printStackTrace();
        }
        
        return baseData;
    }
    
    private void createContribuablesAndProperties(JsonNode rootNode, Bureau bureau) {
        GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
        
        try {
            // Parcourir tous les contribuables dans le JSON
            if (rootNode.isArray()) {
                for (JsonNode contribuableNode : rootNode) {
                    String nom = contribuableNode.get("contribuable").asText();
                    JsonNode biensNode = contribuableNode.get("biens");
                    JsonNode localisationNode = contribuableNode.get("localisation");
                    JsonNode adresseNode = contribuableNode.get("adresse");
                    
                    // Créer un agent pour le contribuable
                    Agent agent = createAgent(nom, bureau);
                    
                    // Créer un utilisateur pour le contribuable
                    Utilisateur utilisateur = createUtilisateur(agent, nom);
                    
                    // Créer le contribuable
                    Contribuable contribuable = createContribuable(nom, agent, bureau, adresseNode);
                    
                    // Créer les propriétés du contribuable
                    createProperties(contribuable, biensNode, localisationNode, adresseNode, gf);
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la création des contribuables et propriétés: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private Agent createAgent(String nom, Bureau bureau) {
        Agent agent = new Agent();
        agent.setNom(nom);
        agent.setSexe(Sexe.M); // Valeur par défaut
        agent.setMatricule("CONT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        agent.setBureau(bureau);
        return agentRepository.save(agent);
    }
    
    private Utilisateur createUtilisateur(Agent agent, String nom) {
        Utilisateur utilisateur = new Utilisateur();
        // Créer un login basé sur le nom (en minuscules, sans espaces)
        String login = nom.toLowerCase().replaceAll("[^a-zA-Z0-9]", "");
        if (login.length() > 20) {
            login = login.substring(0, 20);
        } else if (login.isEmpty()) {
            login = "user" + UUID.randomUUID().toString().substring(0, 8);
        }
        utilisateur.setLogin(login);
        utilisateur.setMotDePasse(passwordEncoder.encode("Tabc@123"));
        utilisateur.setRole(Role.CONTRIBUABLE);
        utilisateur.setPremierConnexion(false);
        utilisateur.setBloque(false);
        utilisateur.setActif(true);
        utilisateur.setAgent(agent);
        return utilisateurRepository.save(utilisateur);
    }
    
    private Contribuable createContribuable(String nom, Agent agent, Bureau bureau, JsonNode adresseNode) {
        Contribuable contribuable = new Contribuable();
        contribuable.setNom(nom);
        contribuable.setSexe(Sexe.M); // Valeur par défaut
        contribuable.setMatricule(agent.getMatricule());
        contribuable.setBureau(bureau);
        
        // Déterminer le type de contribuable (personne physique par défaut)
        contribuable.setType(TypeContribuable.PERSONNE_PHYSIQUE);
        
        // Ajouter l'adresse si disponible
        if (adresseNode != null) {
            String commune = adresseNode.has("commune") ? adresseNode.get("commune").asText() : "";
            String quartier = adresseNode.has("quartier") ? adresseNode.get("quartier").asText() : "";
            String avenue = adresseNode.has("avenue") ? adresseNode.get("avenue").asText() : "";
            String numeroParcelle = adresseNode.has("numero_parcelle") ? adresseNode.get("numero_parcelle").asText() : "";
            
            String adresseComplete = String.format("%s, %s, %s, Parcelle %s", 
                commune, quartier, avenue, numeroParcelle);
            contribuable.setAdressePrincipale(adresseComplete);
        }
        
        contribuable.setTelephonePrincipal("+243" + generateRandomPhoneNumber());
        contribuable.setEmail(generateEmail(nom));
        contribuable.setNationalite("Congolaise");
        contribuable.setIdNat("IDNAT-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        contribuable.setNumeroIdentificationContribuable("NIF-" + generateRandomNIF());
        
        return contribuableRepository.save(contribuable);
    }
    
    private void createProperties(Contribuable contribuable, JsonNode biensNode, JsonNode localisationNode, JsonNode adresseNode, GeometryFactory gf) {
        try {
            if (biensNode != null) {
                // Créer des propriétés en fonction des biens
                int viCount = biensNode.has("Vi") ? biensNode.get("Vi").asInt() : 0;
                int apCount = biensNode.has("AP") ? biensNode.get("AP").asInt() : 0;
                int atCount = biensNode.has("AT") ? biensNode.get("AT").asInt() : 0;
                
                // Créer les villas
                for (int i = 0; i < viCount; i++) {
                    createProperty(contribuable, TypePropriete.VI, i+1, localisationNode, adresseNode, gf);
                }
                
                // Créer les appartements
                for (int i = 0; i < apCount; i++) {
                    createProperty(contribuable, TypePropriete.AP, i+1, localisationNode, adresseNode, gf);
                }
                
                // Créer les terrains
                for (int i = 0; i < atCount; i++) {
                    createProperty(contribuable, TypePropriete.TE, i+1, localisationNode, adresseNode, gf);
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la création des propriétés: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private Propriete createProperty(Contribuable contribuable, TypePropriete type, int index, JsonNode localisationNode, JsonNode adresseNode, GeometryFactory gf) {
        Propriete propriete = new Propriete();
        propriete.setType(type);
        
        // Définir la localité et le rang
        if (adresseNode != null && adresseNode.has("commune")) {
            propriete.setLocalite(adresseNode.get("commune").asText());
        } else {
            propriete.setLocalite("Lubumbashi");
        }
        propriete.setRangLocalite(1);
        
        // Définir la superficie en fonction du type
        double superficie = 0;
        double montantImpot = 0;
        switch (type) {
            case VI: // Villa
                superficie = 200 + (index * 50); // 200 à 650 m²
                montantImpot = superficie * 1.25;
                break;
            case AP: // Appartement
                superficie = 80 + (index * 20); // 80 à 280 m²
                montantImpot = superficie * 1.5;
                break;
            case TE: // Terrain
                superficie = 500 + (index * 250); // 500 à 2750 m²
                montantImpot = superficie * 0.8;
                break;
            default:
                superficie = 100;
                montantImpot = superficie * 1.0;
        }
        
        propriete.setSuperficie(superficie);
        propriete.setMontantImpot(montantImpot);
        
        // Définir l'adresse
        if (adresseNode != null) {
            String quartier = adresseNode.has("quartier") ? adresseNode.get("quartier").asText() : "";
            String avenue = adresseNode.has("avenue") ? adresseNode.get("avenue").asText() : "";
            String numeroParcelle = adresseNode.has("numero_parcelle") ? adresseNode.get("numero_parcelle").asText() : "";
            
            String adresseComplete = String.format("%s, %s, Parcelle %s - Propriété %d", 
                quartier, avenue, numeroParcelle, index);
            propriete.setAdresse(adresseComplete);
        }
        
        // Définir la localisation géographique
        if (localisationNode != null && 
            localisationNode.has("latitude") && !localisationNode.get("latitude").isNull() &&
            localisationNode.has("longitude") && !localisationNode.get("longitude").isNull()) {
            
            double latitude = localisationNode.get("latitude").asDouble();
            double longitude = localisationNode.get("longitude").asDouble();
            propriete.setLocation(gf.createPoint(new Coordinate(longitude, latitude)));
        } else {
            // Coordonnées par défaut pour Lubumbashi
            propriete.setLocation(gf.createPoint(new Coordinate(27.4791, -11.6609)));
        }
        
        propriete.setProprietaire(contribuable);
        propriete.setActif(true);
        propriete.setDeclarationEnLigne(true);
        propriete.setDeclare(true);
        
        return proprieteRepository.save(propriete);
    }
    
    private String generateRandomPhoneNumber() {
        Random random = new Random();
        StringBuilder phone = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            phone.append(random.nextInt(10));
        }
        return phone.toString();
    }
    
    private String generateRandomNIF() {
        Random random = new Random();
        StringBuilder nif = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            nif.append(random.nextInt(10));
        }
        return nif.toString();
    }
    
    private String generateEmail(String nom) {
        // Convertir le nom en email valide
        String emailNom = nom.toLowerCase().replaceAll("[^a-zA-Z0-9]", ".");
        if (emailNom.startsWith(".")) emailNom = emailNom.substring(1);
        if (emailNom.endsWith(".")) emailNom = emailNom.substring(0, emailNom.length() - 1);
        
        // Si le nom est vide ou ne contient pas de caractères valides
        if (emailNom.isEmpty()) {
            emailNom = "contribuable" + UUID.randomUUID().toString().substring(0, 8);
        }
        
        return emailNom + "@example.com";
    }
}
