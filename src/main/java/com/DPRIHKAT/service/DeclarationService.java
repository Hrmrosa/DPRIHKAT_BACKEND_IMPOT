package com.DPRIHKAT.service;

import com.DPRIHKAT.dto.DeclarationRequest;
import com.DPRIHKAT.entity.Agent;
import com.DPRIHKAT.entity.ConcessionMinier;
import com.DPRIHKAT.entity.Contribuable;
import com.DPRIHKAT.entity.Declaration;
import com.DPRIHKAT.entity.Propriete;
import com.DPRIHKAT.entity.enums.SourceDeclaration;
import com.DPRIHKAT.entity.enums.StatutDeclaration;
import com.DPRIHKAT.entity.enums.TypeImpot;
import com.DPRIHKAT.repository.ConcessionMinierRepository;
import com.DPRIHKAT.repository.DeclarationRepository;
import com.DPRIHKAT.repository.ProprieteRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class DeclarationService {

    private static final Logger logger = LoggerFactory.getLogger(DeclarationService.class);

    @Autowired
    private DeclarationRepository declarationRepository;

    @Autowired
    private ProprieteRepository proprieteRepository;

    @Autowired
    private ConcessionMinierRepository concessionMinierRepository;

    /**
     * Create a new declaration (for contribuables)
     */
    public Declaration createDeclaration(DeclarationRequest declarationRequest, Contribuable contribuable) {
        Declaration declaration = new Declaration();
        declaration.setDate(new Date());
        declaration.setStatut(StatutDeclaration.EN_ATTENTE);
        declaration.setTypeImpot(declarationRequest.getTypeImpot());
        declaration.setSource(SourceDeclaration.EN_LIGNE);
        declaration.setContribuable(contribuable);

        // Set property or concession based on tax type
        if (declarationRequest.getTypeImpot() == TypeImpot.IF || 
            declarationRequest.getTypeImpot() == TypeImpot.IRL || 
            declarationRequest.getTypeImpot() == TypeImpot.RL) {
            
            Propriete propriete = proprieteRepository.findById(declarationRequest.getProprieteId())
                    .orElseThrow(() -> new RuntimeException("Propriété non trouvée"));
            declaration.setPropriete(propriete);
            
            // Set location if provided
            if (declarationRequest.getLocation() != null) {
                try {
                    Point point = convertLocationToPoint(declarationRequest.getLocation());
                    propriete.setLocation(point);
                    proprieteRepository.save(propriete);
                    logger.info("Location updated for property: {}", propriete.getId());
                } catch (Exception e) {
                    logger.error("Error updating location for property: {}", e.getMessage());
                }
            }
            
            // Calculate tax amount for property
            try {
                double montant = calculateTaxForProperty(propriete, declarationRequest.getTypeImpot());
                declaration.setMontant(montant);
            } catch (IOException e) {
                throw new RuntimeException("Erreur lors du calcul de l'impôt: " + e.getMessage());
            }
        } else if (declarationRequest.getTypeImpot() == TypeImpot.ICM) {
            ConcessionMinier concession = concessionMinierRepository.findById(declarationRequest.getConcessionId())
                    .orElseThrow(() -> new RuntimeException("Concession non trouvée"));
            declaration.setConcession(concession);
            
            // Calculate tax amount for concession
            try {
                double montant = calculateTaxForConcession(concession);
                declaration.setMontant(montant);
            } catch (IOException e) {
                throw new RuntimeException("Erreur lors du calcul de l'impôt: " + e.getMessage());
            }
        }

        return declarationRepository.save(declaration);
    }

    /**
     * Create a manual declaration (for agents)
     */
    public Declaration createManualDeclaration(DeclarationRequest declarationRequest, Agent agent) {
        Declaration declaration = new Declaration();
        declaration.setDate(new Date());
        declaration.setStatut(StatutDeclaration.VALIDEE);
        declaration.setTypeImpot(declarationRequest.getTypeImpot());
        declaration.setAgentValidateur(agent);
        declaration.setSource(SourceDeclaration.ADMINISTRATION);

        // Set property or concession based on tax type
        if (declarationRequest.getTypeImpot() == TypeImpot.IF || 
            declarationRequest.getTypeImpot() == TypeImpot.IRL || 
            declarationRequest.getTypeImpot() == TypeImpot.RL) {
            
            Propriete propriete = proprieteRepository.findById(declarationRequest.getProprieteId())
                    .orElseThrow(() -> new RuntimeException("Propriété non trouvée"));
            declaration.setPropriete(propriete);
            
            // Set location if provided
            if (declarationRequest.getLocation() != null) {
                try {
                    Point point = convertLocationToPoint(declarationRequest.getLocation());
                    propriete.setLocation(point);
                    proprieteRepository.save(propriete);
                    logger.info("Location updated for property: {}", propriete.getId());
                } catch (Exception e) {
                    logger.error("Error updating location for property: {}", e.getMessage());
                }
            }
            
            // Set tax amount
            declaration.setMontant(declarationRequest.getMontant());
        } else if (declarationRequest.getTypeImpot() == TypeImpot.ICM) {
            ConcessionMinier concession = concessionMinierRepository.findById(declarationRequest.getConcessionId())
                    .orElseThrow(() -> new RuntimeException("Concession non trouvée"));
            declaration.setConcession(concession);
            
            // Set tax amount
            declaration.setMontant(declarationRequest.getMontant());
        }

        return declarationRepository.save(declaration);
    }

    /**
     * Convert Map<String, Double> location to Point
     */
    private Point convertLocationToPoint(Map<String, Double> location) {
        Double longitude = location.get("longitude");
        Double latitude = location.get("latitude");
        
        if (longitude == null || latitude == null) {
            throw new IllegalArgumentException("Location must contain longitude and latitude");
        }
        
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        return geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }

    private double calculateTaxForProperty(Propriete propriete, TypeImpot typeImpot) throws IOException {
        if (typeImpot == TypeImpot.IF) {
            return calculateIFTax(propriete);
        } else if (typeImpot == TypeImpot.IRL || typeImpot == TypeImpot.RL) {
            // 22% rate for IRL/RL
            return propriete.getMontantImpot() * 0.22;
        }
        return 0.0;
    }

    private double calculateIFTax(Propriete propriete) throws IOException {
        // Load IF rates from JSON file
        ClassPathResource resource = new ClassPathResource("taux_if.json");
        InputStream inputStream = resource.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(inputStream);

        String propertyType = propriete.getType().name().toLowerCase();
        String rang = "rang" + propriete.getRangLocalite();
        String contribuableType = propriete.getProprietaire().getType().name().toLowerCase();

        JsonNode rateNode = rootNode.path(propertyType).path(rang).path(contribuableType);
        if (rateNode.isMissingNode()) {
            throw new RuntimeException("Taux IF non trouvé pour le type de propriété: " + propertyType + ", rang: " + rang + ", contribuable: " + contribuableType);
        }

        double rate = rateNode.asDouble();
        
        // Calculate based on property type
        if ("villas".equals(propertyType) || "commercial".equals(propertyType)) {
            // Rate per square meter
            return rate * propriete.getSuperficie();
        } else if ("appartements".equals(propertyType) || "domestic".equals(propertyType)) {
            // Fixed rate
            return rate;
        }
        
        return 0.0;
    }

    private double calculateTaxForConcession(ConcessionMinier concession) throws IOException {
        // Load ICM rates from JSON file
        ClassPathResource resource = new ClassPathResource("taux_icm.json");
        InputStream inputStream = resource.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(inputStream);

        String concessionType = concession.getType().name().toLowerCase();
        JsonNode rateNode = rootNode.path(concessionType);
        if (rateNode.isMissingNode()) {
            throw new RuntimeException("Taux ICM non trouvé pour le type de concession: " + concessionType);
        }

        double rate = rateNode.asDouble();
        
        // Calculate based on number of mining squares
        return rate * concession.getNombreCarresMinier();
    }

    /**
     * Get declaration by ID
     */
    public Declaration findById(UUID id) {
        return declarationRepository.findById(id).orElse(null);
    }

    /**
     * Get all declarations
     */
    public Iterable<Declaration> findAll() {
        return declarationRepository.findAll();
    }

    /**
     * Save declaration
     */
    public Declaration save(Declaration declaration) {
        return declarationRepository.save(declaration);
    }

    /**
     * Update declaration
     */
    public Declaration update(UUID id, Declaration declaration) {
        if (declarationRepository.existsById(id)) {
            declaration.setId(id);
            return declarationRepository.save(declaration);
        }
        return null;
    }

    /**
     * Delete declaration
     */
    public void deleteById(UUID id) {
        declarationRepository.deleteById(id);
    }
}
