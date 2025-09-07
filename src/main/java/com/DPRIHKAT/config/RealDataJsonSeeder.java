/*package com.DPRIHKAT.config;

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
            createPropertiesFromJson(
                contribuableId, 
                biensNode, 
                localisationNode, 
                adresseNode, 
                gf
            );
        }
    }
    
    private int createPropertiesFromJson(UUID contribuableId, JsonNode biensNode, 
                                   JsonNode localisationNode, JsonNode adresseNode,
                                   GeometryFactory gf) {
        int nbProprietes = 0;
        
        // Extraire les coordonnées
        Double latitude = localisationNode.has("latitude") && !localisationNode.get("latitude").isNull() 
            ? localisationNode.get("latitude").asDouble() : null;
        Double longitude = localisationNode.has("longitude") && !localisationNode.get("longitude").isNull()
            ? localisationNode.get("longitude").asDouble() : null;
        
        // Traiter tous les types de propriétés
        // Villas (VI)
        int viCount = biensNode.has("Vi") ? biensNode.get("Vi").asInt() : 0;
        for (int i = 0; i < viCount; i++) {
            createProperty(contribuableId, TypePropriete.VI, i+1, latitude, longitude, adresseNode, gf);
            nbProprietes++;
        }
        
        // Appartements (AP)
        int apCount = biensNode.has("AP") ? biensNode.get("AP").asInt() : 0;
        for (int i = 0; i < apCount; i++) {
            createProperty(contribuableId, TypePropriete.AP, i+1, latitude, longitude, adresseNode, gf);
            nbProprietes++;
        }
        
        // Terrains (AT/TE)
        int atCount = biensNode.has("AT") ? biensNode.get("AT").asInt() : 0;
        for (int i = 0; i < atCount; i++) {
            createProperty(contribuableId, TypePropriete.TE, i+1, latitude, longitude, adresseNode, gf);
            nbProprietes++;
        }
        
        // Citernes
        int citernesCount = biensNode.has("citernes") ? biensNode.get("citernes").asInt() : 0;
        for (int i = 0; i < citernesCount; i++) {
            createProperty(contribuableId, TypePropriete.CITERNE, i+1, latitude, longitude, adresseNode, gf);
            nbProprietes++;
        }
        
        // Bâtiments (CH)
        int batimentsCount = biensNode.has("batiments") ? biensNode.get("batiments").asInt() : 0;
        for (int i = 0; i < batimentsCount; i++) {
            createProperty(contribuableId, TypePropriete.CH, i+1, latitude, longitude, adresseNode, gf);
            nbProprietes++;
        }
        
        // Entrepôts
        int entrepotsCount = biensNode.has("entrepots") ? biensNode.get("entrepots").asInt() : 0;
        for (int i = 0; i < entrepotsCount; i++) {
            createProperty(contribuableId, TypePropriete.ENTREPOT, i+1, latitude, longitude, adresseNode, gf);
            nbProprietes++;
        }
        
        // Dépôts
        int depotsCount = biensNode.has("depots") ? biensNode.get("depots").asInt() : 0;
        for (int i = 0; i < depotsCount; i++) {
            createProperty(contribuableId, TypePropriete.DEPOT, i+1, latitude, longitude, adresseNode, gf);
            nbProprietes++;
        }
        
        // Hangars
        int hangarsCount = biensNode.has("angars") ? biensNode.get("angars").asInt() : 0;
        for (int i = 0; i < hangarsCount; i++) {
            createProperty(contribuableId, TypePropriete.HANGAR, i+1, latitude, longitude, adresseNode, gf);
            nbProprietes++;
        }
        
        // Chantiers (utiliser CH car CHANTIER n'existe pas dans l'énumération)
        int chantiersCount = biensNode.has("chantiers") ? biensNode.get("chantiers").asInt() : 0;
        for (int i = 0; i < chantiersCount; i++) {
            createProperty(contribuableId, TypePropriete.CH, i+1, latitude, longitude, adresseNode, gf);
            nbProprietes++;
        }
        
        // Antennes
        int antennesCount = biensNode.has("antennes") ? biensNode.get("antennes").asInt() : 0;
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
            
            // Extraire les valeurs d'adresse avec limitation de longueur
            String quartier = adresseNode.has("quartier") ? truncateString(adresseNode.get("quartier").asText(), 50) : "";
            String avenue = adresseNode.has("avenue") ? truncateString(adresseNode.get("avenue").asText(), 50) : "";
            String numeroParcelle = adresseNode.has("numero_parcelle") ? truncateString(adresseNode.get("numero_parcelle").asText(), 20) : "";
            String commune = adresseNode.has("commune") ? truncateString(adresseNode.get("commune").asText(), 50) : "Lubumbashi";
            
            // Construire l'adresse complète avec limitation de longueur
            String adresse = truncateString(String.format("%s, %s, Parcelle %s", quartier, avenue, numeroParcelle), 250);
            
            // Créer la géométrie
            String wktPoint = "POINT(" + 
                (longitude != null ? longitude : 27.4791) + " " + 
                (latitude != null ? latitude : -11.6609) + ")";
            
            // Calculer superficie et montant d'impôt selon le type
            double superficie = calculateSuperficie(type, index);
            double montantImpot = calculateMontantImpot(type, superficie);
            
            // Insertion SQL avec declaration_en_ligne et declare
            String sql = "INSERT INTO propriete (id, type, localite, superficie, " +
                         "adresse, montant_impot, location, actif, proprietaire_id, declaration_en_ligne, declare) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ST_GeomFromText(?, 4326), ?, ?, ?, ?)";
            
            jdbcTemplate.update(sql,
                id, type.toString(), 
                commune,
                superficie, adresse, montantImpot, wktPoint, true, contribuableId,
                false, // Valeur par défaut pour declaration_en_ligne
                false  // Valeur par défaut pour declare
            );
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String truncateString(String input, int maxLength) {
        if (input == null) {
            return "";
        }
        return input.length() <= maxLength ? input : input.substring(0, maxLength);
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
*/
