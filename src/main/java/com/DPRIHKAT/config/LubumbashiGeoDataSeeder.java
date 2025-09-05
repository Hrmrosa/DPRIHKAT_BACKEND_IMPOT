package com.DPRIHKAT.config;

import com.DPRIHKAT.entity.Contribuable;
import com.DPRIHKAT.entity.Propriete;
import com.DPRIHKAT.entity.enums.TypeContribuable;
import com.DPRIHKAT.entity.enums.TypePropriete;
import com.DPRIHKAT.repository.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Classe pour générer des données de test géographiques pour Lubumbashi
 */
@Component
@Order(20) // S'exécute après le TestDataSeeder standard
public class LubumbashiGeoDataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(LubumbashiGeoDataSeeder.class);

    @Autowired
    private ContribuableRepository contribuableRepository;

    @Autowired
    private ProprieteRepository proprieteRepository;

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

    @Override
    public void run(String... args) throws Exception {
        // Vérifier si les données existent déjà
        if (contribuableRepository.count() > 10) {
            logger.info("Données géographiques de Lubumbashi déjà présentes, on saute le seeding.");
            return;
        }

        logger.info("Début du seeding des données géographiques de Lubumbashi...");

        // Créer la factory pour les points géographiques
        GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);

        // Créer les contribuables
        List<Contribuable> contribuables = new ArrayList<>();
        
        // Contribuables personnes physiques
        for (int i = 0; i < 12; i++) {
            String prenom = PRENOMS[i % PRENOMS.length];
            String nom = NOMS[i % NOMS.length];
            
            Contribuable contribuable = new Contribuable(
                    nom + " " + prenom,
                    AVENUES[i % AVENUES.length] + " " + (i * 10 + 5),
                    QUARTIERS[i % QUARTIERS.length],
                    "+243820" + String.format("%06d", 100000 + i),
                    null,
                    prenom.toLowerCase() + "." + nom.toLowerCase() + "@example.com",
                    "RDC",
                    TypeContribuable.PERSONNE_PHYSIQUE,
                    "IDNAT-" + String.format("%06d", 100000 + i),
                    null,
                    null,
                    "NIF-" + String.format("%09d", 100000000 + i)
            );
            contribuables.add(contribuable);
        }
        
        // Contribuables personnes morales
        for (int i = 0; i < 3; i++) {
            String nomEntreprise = ENTREPRISES[i % ENTREPRISES.length] + " " + (i + 1) + " SARL";
            
            Contribuable contribuable = new Contribuable(
                    nomEntreprise,
                    AVENUES[i % AVENUES.length] + " " + (i * 20 + 10),
                    QUARTIERS[(i + 5) % QUARTIERS.length],
                    "+243990" + String.format("%06d", 200000 + i),
                    "+243815" + String.format("%06d", 200000 + i),
                    "contact@" + nomEntreprise.toLowerCase().replace(" ", "").replace("-", "") + ".cd",
                    "RDC",
                    TypeContribuable.PERSONNE_MORALE,
                    "IDNAT-PM-" + String.format("%04d", 1000 + i),
                    "RCCM-CD/LUB/" + (2020 + i) + "-B-" + String.format("%04d", 1000 + i),
                    nomEntreprise.substring(0, 3).toUpperCase() + String.format("%02d", i + 1),
                    "NIF-PM-" + String.format("%07d", 1000000 + i)
            );
            contribuables.add(contribuable);
        }
        
        // Sauvegarder les contribuables
        contribuables = contribuableRepository.saveAll(contribuables);
        logger.info("{} contribuables créés pour Lubumbashi", contribuables.size());
        
        // Créer les propriétés
        List<Propriete> proprietes = new ArrayList<>();
        Random random = new Random(42); // Seed fixe pour la reproductibilité
        
        // Types de propriétés
        TypePropriete[] types = {TypePropriete.VI, TypePropriete.AP, TypePropriete.TE, TypePropriete.DEPOT};
        
        // Créer au moins 30 propriétés
        for (int i = 0; i < 35; i++) {
            Contribuable proprietaire = contribuables.get(i % contribuables.size());
            int quartierIndex = i % QUARTIERS.length;
            String quartier = QUARTIERS[quartierIndex];
            
            // Coordonnées de base du quartier
            double baseLatitude = LUBUMBASHI_COORDS[quartierIndex][0];
            double baseLongitude = LUBUMBASHI_COORDS[quartierIndex][1];
            
            // Ajouter une petite variation aléatoire (±0.005 degrés, environ 500m)
            double latitude = baseLatitude + (random.nextDouble() - 0.5) * 0.01;
            double longitude = baseLongitude + (random.nextDouble() - 0.5) * 0.01;
            
            TypePropriete type = types[i % types.length];
            double superficie = 100 + random.nextDouble() * 900; // Entre 100 et 1000 m²
            
            if (type == TypePropriete.TE) {
                superficie = 1000 + random.nextDouble() * 9000; // Terrains plus grands
            } else if (type == TypePropriete.DEPOT) {
                superficie = 500 + random.nextDouble() * 4500; // Bâtiments commerciaux
            }
            
            String adresse = AVENUES[i % AVENUES.length] + " " + (i * 5 + 1) + ", " + quartier;
            
            Propriete propriete = new Propriete();
            propriete.setType(type);
            propriete.setLocalite(quartier);
            propriete.setRangLocalite(1 + (i % 3)); // Rang entre 1 et 3
            propriete.setSuperficie(superficie);
            propriete.setAdresse(adresse);
            propriete.setProprietaire(proprietaire);
            propriete.setMontantImpot(superficie * (0.5 + random.nextDouble())); // Montant d'impôt basé sur la superficie
            propriete.setLocation(gf.createPoint(new Coordinate(longitude, latitude)));
            
            proprietes.add(propriete);
        }
        
        // Sauvegarder les propriétés
        proprieteRepository.saveAll(proprietes);
        logger.info("{} propriétés créées avec coordonnées géographiques à Lubumbashi", proprietes.size());
        
        logger.info("Seeding des données géographiques de Lubumbashi terminé avec succès");
    }
}
