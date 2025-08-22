package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Agent;
import com.DPRIHKAT.entity.ConcessionMinier;
import com.DPRIHKAT.entity.Declaration;
import com.DPRIHKAT.entity.Propriete;
import com.DPRIHKAT.entity.enums.TypeImpot;
import com.DPRIHKAT.repository.DeclarationRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TaxationService {

    @Autowired
    private DeclarationRepository declarationRepository;

    @Autowired
    private DeclarationService declarationService;

    /**
     * Generate tax note for a property
     */
    public Declaration generateTaxNoteForProperty(Propriete propriete, Agent agent) throws IOException {
        // Check if a tax note already exists for this property and year
        int year = LocalDate.now().getYear();
        int count = declarationRepository.countByProprieteAndTypeAndAnnee(
                propriete.getId(),
                TypeImpot.IF,  // Assuming IF for property taxation
                year);
        
        if (count > 0) {
            throw new RuntimeException("Une note de taxation pour ce bien et cette année existe déjà");
        }

        // Create a declaration for taxation
        Declaration declaration = new Declaration();
        declaration.setTypeImpot(TypeImpot.IF);
        declaration.setPropriete(propriete); // Utiliser setPropriete au lieu de setLocation
        declaration.setAgentValidateur(agent);
        
        // Calculate tax amount
        double montant = calculateTaxForProperty(propriete);
        declaration.setMontant(montant);
        
        // Set status to TAXED
        // declaration.setStatut(StatutDeclaration.TAXEE); // Uncomment when StatutDeclaration is available
        
        return declarationRepository.save(declaration);
    }

    /**
     * Generate tax note for a mining concession
     */
    public Declaration generateTaxNoteForConcession(ConcessionMinier concession, Agent agent) throws IOException {
        // Check if a tax note already exists for this concession and year
        int year = LocalDate.now().getYear();
        int count = declarationRepository.countByConcessionAndTypeAndAnnee(
                concession.getId(),
                TypeImpot.ICM,
                year);
        
        if (count > 0) {
            throw new RuntimeException("Une note de taxation pour cette concession et cette année existe déjà");
        }

        // Create a declaration for taxation
        Declaration declaration = new Declaration();
        declaration.setTypeImpot(TypeImpot.ICM);
        declaration.setConcession(concession); // Utiliser setConcession
        declaration.setAgentValidateur(agent);
        
        // Calculate tax amount
        double montant = calculateTaxForConcession(concession);
        declaration.setMontant(montant);
        
        // Set status to TAXED
        // declaration.setStatut(StatutDeclaration.TAXEE); // Uncomment when StatutDeclaration is available
        
        return declarationRepository.save(declaration);
    }

    /**
     * Calculate IF tax for a property based on rates in taux_if.json
     */
    public double calculateTaxForProperty(Propriete propriete) throws IOException {
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

    /**
     * Calculate ICM tax for a mining concession based on rates in taux_icm.json
     */
    public double calculateTaxForConcession(ConcessionMinier concession) throws IOException {
        // Load ICM rates from JSON file
        ClassPathResource resource = new ClassPathResource("taux_icm.json");
        InputStream inputStream = resource.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(inputStream);

        String concessionType = concession.getType().name().toLowerCase();
        String annexe = concession.getAnnexe();
        String contribuableType = concession.getTitulaire().getType().name().toLowerCase();

        JsonNode rateNode = rootNode.path(concessionType).path(annexe).path(contribuableType);
        if (rateNode.isMissingNode()) {
            throw new RuntimeException("Taux ICM non trouvé pour le type de concession: " + concessionType + ", annexe: " + annexe + ", contribuable: " + contribuableType);
        }

        double rate = rateNode.asDouble();
        
        // Rate per hectare
        return rate * concession.getNombreCarresMinier();
    }

    /**
     * Calculate IRL tax (22% of property value)
     */
    public double calculateIRL(Propriete propriete) {
        // 22% rate for IRL
        return propriete.getMontantImpot() * 0.22;
    }

    /**
     * Calculate IRV tax based on vehicle characteristics
     */
    public double calculateIRV(double puissanceCV, double poids) {
        // Base rate + additional based on weight
        double baseRate = 54.0; // Base rate for <2.5T person
        
        if (poids < 2500) { // <2.5T
            return baseRate + (19.0); // TSCR
        } else {
            // For heavier vehicles, adjust calculation as needed
            return baseRate + (poids / 1000) * 10; // Example calculation
        }
    }

    /**
     * Get all taxed declarations for an agent
     */
    public List<Declaration> getTaxedDeclarationsForAgent(UUID agentId) {
        // This would need a custom query in DeclarationRepository
        // return declarationRepository.findByAgentValidateurIdAndStatut(agentId, StatutDeclaration.TAXEE);
        return null; // Placeholder
    }

    public double calculateIF(Declaration declaration) {
        try {
            Propriete propriete = declaration.getPropriete();
            if (propriete == null) {
                throw new RuntimeException("Aucune propriété associée à la déclaration IF");
            }
            return calculateTaxForProperty(propriete);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du calcul de l'IF: " + e.getMessage());
        }
    }
    
    public double calculateICM(Declaration declaration) {
        try {
            ConcessionMinier concession = declaration.getConcession();
            if (concession == null) {
                throw new RuntimeException("Aucune concession associée à la déclaration ICM");
            }
            return calculateTaxForConcession(concession);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du calcul de l'ICM: " + e.getMessage());
        }
    }

    /**
     * Load tax rules from JSON file
     */
    private JsonNode loadTaxRules(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(fileName);
        InputStream inputStream = resource.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(inputStream);
    }

    /**
     * Check if surface is in range
     */
    private boolean isSurfaceInRange(double surface, String range) {
        // Parse range string like "0-100", "101-200", etc.
        if (range.contains("-")) {
            String[] parts = range.split("-");
            double min = Double.parseDouble(parts[0]);
            double max = Double.parseDouble(parts[1]);
            return surface >= min && surface <= max;
        } else if (range.startsWith(">")) {
            double min = Double.parseDouble(range.substring(1));
            return surface > min;
        } else if (range.startsWith("<")) {
            double max = Double.parseDouble(range.substring(1));
            return surface < max;
        }
        return false;
    }
}
