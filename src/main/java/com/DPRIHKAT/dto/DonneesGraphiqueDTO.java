package com.DPRIHKAT.dto;

import java.util.List;
import java.util.Map;

/**
 * DTO pour les données de graphiques
 * Fournit des données structurées prêtes à être utilisées par les bibliothèques de graphiques frontend
 * (Chart.js, Recharts, ApexCharts, etc.)
 * 
 * @author amateur
 */
public class DonneesGraphiqueDTO {
    
    // Données pour graphiques en barres/lignes
    private List<SerieTemporelleDTO> seriesTemporelles;
    
    // Données pour graphiques circulaires (pie/donut)
    private List<DonneeCirculaireDTO> donneesCirculaires;
    
    // Données pour graphiques en barres empilées
    private List<DonneeEmpileeDTO> donneesEmpilees;
    
    // Évolution temporelle (pour graphiques de tendance)
    private EvolutionTemporelleDTO evolutionTemporelle;
    
    // Top contributeurs/agents
    private List<TopItemDTO> topItems;
    
    // Comparaisons
    private ComparaisonDTO comparaison;
    
    public DonneesGraphiqueDTO() {
    }
    
    // Getters et Setters
    public List<SerieTemporelleDTO> getSeriesTemporelles() {
        return seriesTemporelles;
    }
    
    public void setSeriesTemporelles(List<SerieTemporelleDTO> seriesTemporelles) {
        this.seriesTemporelles = seriesTemporelles;
    }
    
    public List<DonneeCirculaireDTO> getDonneesCirculaires() {
        return donneesCirculaires;
    }
    
    public void setDonneesCirculaires(List<DonneeCirculaireDTO> donneesCirculaires) {
        this.donneesCirculaires = donneesCirculaires;
    }
    
    public List<DonneeEmpileeDTO> getDonneesEmpilees() {
        return donneesEmpilees;
    }
    
    public void setDonneesEmpilees(List<DonneeEmpileeDTO> donneesEmpilees) {
        this.donneesEmpilees = donneesEmpilees;
    }
    
    public EvolutionTemporelleDTO getEvolutionTemporelle() {
        return evolutionTemporelle;
    }
    
    public void setEvolutionTemporelle(EvolutionTemporelleDTO evolutionTemporelle) {
        this.evolutionTemporelle = evolutionTemporelle;
    }
    
    public List<TopItemDTO> getTopItems() {
        return topItems;
    }
    
    public void setTopItems(List<TopItemDTO> topItems) {
        this.topItems = topItems;
    }
    
    public ComparaisonDTO getComparaison() {
        return comparaison;
    }
    
    public void setComparaison(ComparaisonDTO comparaison) {
        this.comparaison = comparaison;
    }
    
    /**
     * Série temporelle pour graphiques en ligne/barres
     */
    public static class SerieTemporelleDTO {
        private String nom; // Nom de la série (ex: "Taxations", "Paiements")
        private List<String> labels; // Labels de l'axe X (dates, périodes)
        private List<Double> valeurs; // Valeurs de l'axe Y
        private String couleur; // Couleur suggérée pour le graphique
        
        public SerieTemporelleDTO() {
        }
        
        public SerieTemporelleDTO(String nom, List<String> labels, List<Double> valeurs, String couleur) {
            this.nom = nom;
            this.labels = labels;
            this.valeurs = valeurs;
            this.couleur = couleur;
        }
        
        // Getters et Setters
        public String getNom() {
            return nom;
        }
        
        public void setNom(String nom) {
            this.nom = nom;
        }
        
        public List<String> getLabels() {
            return labels;
        }
        
        public void setLabels(List<String> labels) {
            this.labels = labels;
        }
        
        public List<Double> getValeurs() {
            return valeurs;
        }
        
        public void setValeurs(List<Double> valeurs) {
            this.valeurs = valeurs;
        }
        
        public String getCouleur() {
            return couleur;
        }
        
        public void setCouleur(String couleur) {
            this.couleur = couleur;
        }
    }
    
    /**
     * Données pour graphiques circulaires (pie/donut)
     */
    public static class DonneeCirculaireDTO {
        private String label; // Label du segment
        private Double valeur; // Valeur du segment
        private Double pourcentage; // Pourcentage du total
        private String couleur; // Couleur suggérée
        
        public DonneeCirculaireDTO() {
        }
        
        public DonneeCirculaireDTO(String label, Double valeur, Double pourcentage, String couleur) {
            this.label = label;
            this.valeur = valeur;
            this.pourcentage = pourcentage;
            this.couleur = couleur;
        }
        
        // Getters et Setters
        public String getLabel() {
            return label;
        }
        
        public void setLabel(String label) {
            this.label = label;
        }
        
        public Double getValeur() {
            return valeur;
        }
        
        public void setValeur(Double valeur) {
            this.valeur = valeur;
        }
        
        public Double getPourcentage() {
            return pourcentage;
        }
        
        public void setPourcentage(Double pourcentage) {
            this.pourcentage = pourcentage;
        }
        
        public String getCouleur() {
            return couleur;
        }
        
        public void setCouleur(String couleur) {
            this.couleur = couleur;
        }
    }
    
    /**
     * Données pour graphiques en barres empilées
     */
    public static class DonneeEmpileeDTO {
        private String categorie; // Catégorie (ex: "Janvier", "Agent1")
        private Map<String, Double> valeurs; // Valeurs par sous-catégorie
        
        public DonneeEmpileeDTO() {
        }
        
        public DonneeEmpileeDTO(String categorie, Map<String, Double> valeurs) {
            this.categorie = categorie;
            this.valeurs = valeurs;
        }
        
        // Getters et Setters
        public String getCategorie() {
            return categorie;
        }
        
        public void setCategorie(String categorie) {
            this.categorie = categorie;
        }
        
        public Map<String, Double> getValeurs() {
            return valeurs;
        }
        
        public void setValeurs(Map<String, Double> valeurs) {
            this.valeurs = valeurs;
        }
    }
    
    /**
     * Évolution temporelle pour graphiques de tendance
     */
    public static class EvolutionTemporelleDTO {
        private List<String> periodes; // Périodes (jours, semaines, mois)
        private List<Double> montants; // Montants par période
        private List<Long> quantites; // Quantités par période
        private Double tendance; // Tendance (positive/négative en %)
        private Double moyenne; // Moyenne sur la période
        
        public EvolutionTemporelleDTO() {
        }
        
        // Getters et Setters
        public List<String> getPeriodes() {
            return periodes;
        }
        
        public void setPeriodes(List<String> periodes) {
            this.periodes = periodes;
        }
        
        public List<Double> getMontants() {
            return montants;
        }
        
        public void setMontants(List<Double> montants) {
            this.montants = montants;
        }
        
        public List<Long> getQuantites() {
            return quantites;
        }
        
        public void setQuantites(List<Long> quantites) {
            this.quantites = quantites;
        }
        
        public Double getTendance() {
            return tendance;
        }
        
        public void setTendance(Double tendance) {
            this.tendance = tendance;
        }
        
        public Double getMoyenne() {
            return moyenne;
        }
        
        public void setMoyenne(Double moyenne) {
            this.moyenne = moyenne;
        }
    }
    
    /**
     * Top items (contributeurs, agents, types)
     */
    public static class TopItemDTO {
        private String nom; // Nom de l'item
        private Double valeur; // Valeur (montant ou quantité)
        private Long quantite; // Quantité associée
        private Double pourcentage; // Pourcentage du total
        private Integer rang; // Rang dans le classement
        
        public TopItemDTO() {
        }
        
        public TopItemDTO(String nom, Double valeur, Long quantite, Double pourcentage, Integer rang) {
            this.nom = nom;
            this.valeur = valeur;
            this.quantite = quantite;
            this.pourcentage = pourcentage;
            this.rang = rang;
        }
        
        // Getters et Setters
        public String getNom() {
            return nom;
        }
        
        public void setNom(String nom) {
            this.nom = nom;
        }
        
        public Double getValeur() {
            return valeur;
        }
        
        public void setValeur(Double valeur) {
            this.valeur = valeur;
        }
        
        public Long getQuantite() {
            return quantite;
        }
        
        public void setQuantite(Long quantite) {
            this.quantite = quantite;
        }
        
        public Double getPourcentage() {
            return pourcentage;
        }
        
        public void setPourcentage(Double pourcentage) {
            this.pourcentage = pourcentage;
        }
        
        public Integer getRang() {
            return rang;
        }
        
        public void setRang(Integer rang) {
            this.rang = rang;
        }
    }
    
    /**
     * Comparaison entre périodes ou catégories
     */
    public static class ComparaisonDTO {
        private String label1; // Label première catégorie
        private String label2; // Label deuxième catégorie
        private Double valeur1; // Valeur première catégorie
        private Double valeur2; // Valeur deuxième catégorie
        private Double difference; // Différence absolue
        private Double pourcentageDifference; // Différence en pourcentage
        private String tendance; // "HAUSSE", "BAISSE", "STABLE"
        
        public ComparaisonDTO() {
        }
        
        // Getters et Setters
        public String getLabel1() {
            return label1;
        }
        
        public void setLabel1(String label1) {
            this.label1 = label1;
        }
        
        public String getLabel2() {
            return label2;
        }
        
        public void setLabel2(String label2) {
            this.label2 = label2;
        }
        
        public Double getValeur1() {
            return valeur1;
        }
        
        public void setValeur1(Double valeur1) {
            this.valeur1 = valeur1;
        }
        
        public Double getValeur2() {
            return valeur2;
        }
        
        public void setValeur2(Double valeur2) {
            this.valeur2 = valeur2;
        }
        
        public Double getDifference() {
            return difference;
        }
        
        public void setDifference(Double difference) {
            this.difference = difference;
        }
        
        public Double getPourcentageDifference() {
            return pourcentageDifference;
        }
        
        public void setPourcentageDifference(Double pourcentageDifference) {
            this.pourcentageDifference = pourcentageDifference;
        }
        
        public String getTendance() {
            return tendance;
        }
        
        public void setTendance(String tendance) {
            this.tendance = tendance;
        }
    }
}
