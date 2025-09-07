package com.DPRIHKAT.config;

import com.DPRIHKAT.entity.*;
import com.DPRIHKAT.entity.enums.*;
import com.DPRIHKAT.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.*;

/**
 * Seeder pour les données réelles des contribuables depuis le fichier GeoJSON
 * Utilise des requêtes SQL directes pour éviter les problèmes d'héritage
 */
@Component
@Order(10)
public class RealDataJsonSeeder implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;
    
    private final JdbcTemplate jdbcTemplate;
    private final BureauRepository bureauRepository;
    private final AgentRepository agentRepository;
    private final ContribuableRepository contribuableRepository;

    public RealDataJsonSeeder(
            JdbcTemplate jdbcTemplate,
            BureauRepository bureauRepository,
            AgentRepository agentRepository,
            ContribuableRepository contribuableRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.bureauRepository = bureauRepository;
        this.agentRepository = agentRepository;
        this.contribuableRepository = contribuableRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        try {
            ClassPathResource resource = new ClassPathResource("new_datas.json");
            InputStream inputStream = resource.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(inputStream);
            
            Bureau bureau = bureauRepository.findAll().stream().findFirst().orElseThrow();
            
            if (rootNode.isArray()) {
                processGeoJsonFeatures(rootNode, bureau);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void processGeoJsonFeatures(JsonNode featuresNode, Bureau bureau) {
        GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
        
        for (JsonNode contribuableNode : featuresNode) {
            // Nouvelle structure de données
            String nom = contribuableNode.get("contribuable").asText();
            JsonNode biensNode = contribuableNode.get("biens");
            JsonNode localisationNode = contribuableNode.get("localisation");
            JsonNode adresseNode = contribuableNode.get("adresse");

            // Créer l'agent et le contribuable
            Agent agent = createAgent(nom, bureau);
            entityManager.flush();
            UUID contribuableId = createContribuable(agent, nom, bureau);

            // Créer les propriétés
            createPropertiesFromGeoJson(
                contribuableId, 
                biensNode, 
                localisationNode, 
                adresseNode, 
                gf
            );
        }
    }
    
    private int createPropertiesFromGeoJson(UUID contribuableId, JsonNode biensNode, 
                                   JsonNode localisationNode, JsonNode adresseNode,
                                   GeometryFactory gf) {
        int nbProprietes = 0;
        
        // Extraire les coordonnées
        Double latitude = null;
        Double longitude = null;
        if (localisationNode != null && localisationNode.isArray() && localisationNode.size() >= 2) {
            longitude = localisationNode.get(0).asDouble();
            latitude = localisationNode.get(1).asDouble();
        }
        
        // Extraire les nombres de propriétés par type
        int viCount = biensNode.has("Nombre des Vi") ? 
            (biensNode.get("Nombre des Vi").asText().equals("") ? 0 : Integer.parseInt(biensNode.get("Nombre des Vi").asText())) : 0;
        
        int apCount = biensNode.has("Nombre des AP") ? 
            (biensNode.get("Nombre des AP").asText().equals("") ? 0 : Integer.parseInt(biensNode.get("Nombre des AP").asText())) : 0;
        
        int atCount = biensNode.has("Nombre des AT") ? 
            (biensNode.get("Nombre des AT").asText().equals("") ? 0 : Integer.parseInt(biensNode.get("Nombre des AT").asText())) : 0;
        
        int citernesCount = biensNode.has("Nombre des citernes") ? 
            (biensNode.get("Nombre des citernes").asText().equals("") ? 0 : Integer.parseInt(biensNode.get("Nombre des citernes").asText())) : 0;
        
        int antennesCount = biensNode.has("Nombre des antennes") ? 
            (biensNode.get("Nombre des antennes").asText().equals("") ? 0 : Integer.parseInt(biensNode.get("Nombre des antennes").asText())) : 0;
        
        System.out.println("Biens: Vi=" + viCount + ", AP=" + apCount + ", AT=" + atCount + 
                          ", citernes=" + citernesCount + ", antennes=" + antennesCount);
        
        // Créer les villas
        for (int i = 0; i < viCount; i++) {
            createProperty(contribuableId, TypePropriete.VI, i+1, latitude, longitude, adresseNode, gf);
            nbProprietes++;
        }
        
        // Créer les appartements
        for (int i = 0; i < apCount; i++) {
            createProperty(contribuableId, TypePropriete.AP, i+1, latitude, longitude, adresseNode, gf);
            nbProprietes++;
        }
        
        // Créer les terrains
        for (int i = 0; i < atCount; i++) {
            createProperty(contribuableId, TypePropriete.TE, i+1, latitude, longitude, adresseNode, gf);
            nbProprietes++;
        }
        
        // Créer les citernes
        for (int i = 0; i < citernesCount; i++) {
            createProperty(contribuableId, TypePropriete.CITERNE, i+1, latitude, longitude, adresseNode, gf);
            nbProprietes++;
        }
        
        // Créer les antennes
        for (int i = 0; i < antennesCount; i++) {
            createProperty(contribuableId, TypePropriete.ANTENNE, i+1, latitude, longitude, adresseNode, gf);
            nbProprietes++;
        }
        
        return nbProprietes;
    }
    
    private void createProperty(UUID contribuableId, TypePropriete type, int index, 
                         Double latitude, Double longitude, 
                         JsonNode adresseNode, GeometryFactory gf) {
        try {
            UUID id = UUID.randomUUID();
            
            // Construire l'adresse complète
            String adresse = String.format("%s, %s, Parcelle %s",
                adresseNode.has("quartier") ? adresseNode.get("quartier").asText() : "",
                adresseNode.has("avenue") ? adresseNode.get("avenue").asText() : "",
                adresseNode.has("numero_parcelle") ? adresseNode.get("numero_parcelle").asText() : "");
            
            // Créer la géométrie
            String wktPoint = "POINT(" + 
                (longitude != null ? longitude : 27.4791) + " " + 
                (latitude != null ? latitude : -11.6609) + ")";
            
            // Calculer superficie et montant d'impôt selon le type
            double superficie = calculateSuperficie(type, index);
            double montantImpot = calculateMontantImpot(type, superficie);
            
            // Insertion SQL
            String sql = "INSERT INTO propriete (id, type, localite, superficie, " +
                         "adresse, montant_impot, location, actif, proprietaire_id) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ST_GeomFromText(?, 4326), ?, ?)";
            
            jdbcTemplate.update(sql,
                id, type.toString(), 
                adresseNode.get("commune").asText(),
                superficie, adresse, montantImpot, wktPoint, true, contribuableId);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private Agent createAgent(String nom, Bureau bureau) {
        Agent agent = new Agent();
        agent.setNom(nom);
        agent.setSexe(Sexe.M); // Valeur par défaut
        agent.setMatricule("CONTRIB-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        agent.setBureau(bureau);
        Agent savedAgent = agentRepository.save(agent);
        System.out.println("Agent créé: " + savedAgent.getNom() + " avec ID: " + savedAgent.getId());
        return savedAgent;
    }
    
    private UUID createContribuable(Agent agent, String nom, Bureau bureau) {
        try {
            // Générer un UUID pour le contribuable
            UUID contribuableId = agent.getId();
            
            // Construire l'adresse principale
            String adressePrincipale = "Lubumbashi";
            
            // Générer un numéro de téléphone aléatoire
            String telephonePrincipal = "+243" + generateRandomPhoneNumber();
            
            // Générer un email
            String email = generateEmail(nom);
            
            // Générer un numéro d'identification contribuable
            String numeroIdentificationContribuable = "NIF-" + generateRandomNIF();
            
            // Insérer directement dans la table contribuable
            String sql = "INSERT INTO contribuable (agent_id, adresse_principale, email, id_nat, nationalite, " +
                         "numero_identification_contribuable, sigle, telephone_principal, type, actif) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            jdbcTemplate.update(sql, 
                contribuableId, 
                adressePrincipale, 
                email, 
                "IDNAT-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase(), 
                "Congolaise", 
                numeroIdentificationContribuable, 
                "", // sigle
                telephonePrincipal, 
                "PERSONNE_PHYSIQUE", // type
                true // actif
            );
            
            System.out.println("Contribuable créé avec ID: " + contribuableId);
            return contribuableId;
        } catch (Exception e) {
            System.err.println("Erreur lors de la création du contribuable: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
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
    
    private double calculateSuperficie(TypePropriete type, int index) {
        double superficie = 0;
        switch (type) {
            case VI: // Villa
                superficie = 200 + (index * 50); // 200 à 650 m²
                break;
            case AP: // Appartement
                superficie = 80 + (index * 20); // 80 à 280 m²
                break;
            case TE: // Terrain
                superficie = 500 + (index * 250); // 500 à 2750 m²
                break;
            default:
                superficie = 100;
        }
        return superficie;
    }
    
    private double calculateMontantImpot(TypePropriete type, double superficie) {
        double montantImpot = 0;
        switch (type) {
            case VI: // Villa
                montantImpot = superficie * 1.25;
                break;
            case AP: // Appartement
                montantImpot = superficie * 1.5;
                break;
            case TE: // Terrain
                montantImpot = superficie * 0.8;
                break;
            default:
                montantImpot = superficie * 1.0;
        }
        return montantImpot;
    }
}
