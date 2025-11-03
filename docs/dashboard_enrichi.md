# Documentation - Dashboard Enrichi

## 📋 Vue d'ensemble

Le module Dashboard a été enrichi avec des données détaillées similaires au module de rapports. Il fournit maintenant des informations complètes sur tous les aspects du système DPRIHKAT : contribuables, taxations, paiements, relances, recouvrements et propriétés.

## 🎯 Objectifs

- Fournir une vue d'ensemble complète du système
- Inclure des données détaillées pour chaque entité
- Préparer les données pour la visualisation graphique
- Faciliter l'analyse et la prise de décision

## 📦 Nouveaux DTOs Créés

### 1. DashboardEnrichiDTO
**Fichier**: `com.DPRIHKAT.dto.DashboardEnrichiDTO`

DTO principal qui encapsule toutes les données du dashboard enrichi.

```java
public class DashboardEnrichiDTO {
    private Date dateGeneration;
    private StatistiquesDashboardDTO statistiquesGlobales;
    private DonneesDashboardContribuablesDTO contribuables;
    private DonneesDashboardTaxationsDTO taxations;
    private DonneesDashboardPaiementsDTO paiements;
    private DonneesDashboardRelancesDTO relances;
    private DonneesDashboardRecouvrementDTO recouvrements;
    private DonneesDashboardProprietesDTO proprietes;
    private DonneesGraphiqueDTO donneesGraphiques;
}
```

### 2. StatistiquesDashboardDTO
**Fichier**: `com.DPRIHKAT.dto.StatistiquesDashboardDTO`

Statistiques globales du système.

#### Champs principaux:

**Contribuables:**
- `nombreTotalContribuables`: Nombre total de contribuables
- `nombreContribuablesActifs`: Contribuables actifs
- `nombreContribuablesInactifs`: Contribuables inactifs

**Taxations:**
- `nombreTotalTaxations`: Nombre total de taxations
- `montantTotalTaxations`: Montant total des taxations
- `nombreTaxationsPayees`: Taxations payées
- `nombreTaxationsEnAttente`: Taxations en attente
- `nombreTaxationsEnRetard`: Taxations en retard

**Paiements:**
- `nombreTotalPaiements`: Nombre total de paiements
- `montantTotalPaiements`: Montant total payé
- `montantPaiementsAujourdhui`: Montant payé aujourd'hui
- `montantPaiementsCeMois`: Montant payé ce mois

**Relances:**
- `nombreTotalRelances`: Nombre total de relances
- `nombreRelancesEnvoyees`: Relances envoyées
- `nombreRelancesEnAttente`: Relances en attente

**Recouvrement:**
- `nombreDossiersRecouvrement`: Nombre de dossiers
- `nombreATD`: Avis de Taxation Directe
- `nombreMED`: Mises en Demeure
- `nombreCommandements`: Commandements de payer
- `montantTotalRecouvrement`: Montant total à recouvrer

**Propriétés:**
- `nombreTotalProprietes`: Total des propriétés
- `nombreProprietesImmobilieres`: Propriétés immobilières
- `nombreVehicules`: Véhicules enregistrés
- `nombrePlaques`: Plaques d'immatriculation
- `nombreConcessionsMinières`: Concessions minières

**Répartitions:**
- `repartitionParTypeImpot`: Map<String, Long>
- `repartitionMontantsParType`: Map<String, Double>
- `repartitionParStatut`: Map<String, Long>

### 3. DonneesDashboardContribuablesDTO
**Fichier**: `com.DPRIHKAT.dto.DonneesDashboardContribuablesDTO`

Données détaillées sur les contribuables.

#### Structure:
```java
public class DonneesDashboardContribuablesDTO {
    private Long total;
    private Long actifs;
    private Long inactifs;
    private Long avecProprietes;
    private Long avecVehicules;
    private Long avecConcessionsMinières;
    private List<ContribuableResumeDTO> topContribuables;
    private List<ContribuableResumeDTO> nouveauxContribuables;
    private List<ContribuableResumeDTO> contribuablesEnRetard;
}
```

#### ContribuableResumeDTO:
- `id`: Identifiant unique
- `nom`: Nom du contribuable
- `numeroContribuable`: Numéro d'identification
- `typeContribuable`: Type (Physique/Morale)
- `nombreProprietes`: Nombre de propriétés
- `nombreVehicules`: Nombre de véhicules
- `montantTotalTaxe`: Montant total des taxes
- `montantTotalPaye`: Montant total payé
- `montantRestant`: Montant restant à payer
- `statut`: Statut actuel

### 4. DonneesDashboardTaxationsDTO
**Fichier**: `com.DPRIHKAT.dto.DonneesDashboardTaxationsDTO`

Données détaillées sur les taxations.

#### Structure:
```java
public class DonneesDashboardTaxationsDTO {
    private Long total;
    private Long payees;
    private Long enAttente;
    private Long enRetard;
    private Long partiellementPayees;
    private Double montantTotal;
    private Double montantPaye;
    private Double montantRestant;
    private Map<String, Long> repartitionParType;
    private Map<String, Double> montantsParType;
    private Map<String, Long> repartitionParStatut;
    private List<TaxationResumeDTO> taxationsRecentes;
    private List<TaxationResumeDTO> taxationsEnRetard;
    private List<AgentPerformanceDTO> topAgents;
}
```

#### TaxationResumeDTO:
- `id`: Identifiant unique
- `numeroTaxation`: Numéro de taxation
- `typeImpot`: Type d'impôt
- `montant`: Montant de la taxation
- `statut`: Statut (PAYEE, EN_ATTENTE, EN_RETARD)
- `contribuableNom`: Nom du contribuable
- `agentNom`: Nom de l'agent taxateur
- `dateCreation`: Date de création
- `dateEcheance`: Date d'échéance

#### AgentPerformanceDTO:
- `id`: Identifiant de l'agent
- `nom`: Nom complet
- `matricule`: Matricule
- `nombreTaxations`: Nombre de taxations effectuées
- `montantTotal`: Montant total taxé
- `pourcentage`: Pourcentage du total

### 5. DonneesDashboardPaiementsDTO
**Fichier**: `com.DPRIHKAT.dto.DonneesDashboardPaiementsDTO`

Données détaillées sur les paiements.

#### Structure:
```java
public class DonneesDashboardPaiementsDTO {
    private Long total;
    private Double montantTotal;
    private Double montantAujourdhui;
    private Double montantCeMois;
    private Double montantCetteAnnee;
    private Map<String, Long> repartitionParMode;
    private Map<String, Double> montantsParMode;
    private List<PaiementResumeDTO> paiementsRecents;
    private Map<String, Double> evolutionMensuelle;
}
```

#### PaiementResumeDTO:
- `id`: Identifiant unique
- `montant`: Montant du paiement
- `mode`: Mode de paiement (ESPECES, CHEQUE, VIREMENT, etc.)
- `statut`: Statut du paiement
- `contribuableNom`: Nom du contribuable
- `datePaiement`: Date du paiement
- `reference`: Référence du paiement

### 6. DonneesDashboardRelancesDTO
**Fichier**: `com.DPRIHKAT.dto.DonneesDashboardRelancesDTO`

Données détaillées sur les relances.

#### Structure:
```java
public class DonneesDashboardRelancesDTO {
    private Long total;
    private Long envoyees;
    private Long enAttente;
    private Long repondues;
    private Map<String, Long> repartitionParType;
    private List<RelanceResumeDTO> relancesRecentes;
    private List<RelanceResumeDTO> relancesEnAttente;
    private Double tauxReponse;
}
```

#### RelanceResumeDTO:
- `id`: Identifiant unique
- `type`: Type de relance
- `statut`: Statut (ENVOYEE, EN_ATTENTE, REPONDUE)
- `contribuableNom`: Nom du contribuable
- `dateEnvoi`: Date d'envoi
- `contenu`: Contenu de la relance

### 7. DonneesDashboardRecouvrementDTO
**Fichier**: `com.DPRIHKAT.dto.DonneesDashboardRecouvrementDTO`

Données détaillées sur les recouvrements (ATD, MED, Commandements, Saisies).

#### Structure:
```java
public class DonneesDashboardRecouvrementDTO {
    private Long totalDossiers;
    private Long nombreATD;
    private Long nombreMED;
    private Long nombreCommandements;
    private Long nombreSaisies;
    private Double montantTotal;
    private Double montantRecouvre;
    private Double montantRestant;
    private Map<String, Long> repartitionParType;
    private Map<String, Double> montantsParType;
    private Map<String, Long> repartitionParStatut;
    private List<DocumentRecouvrementResumeDTO> documentsRecents;
    private List<DossierRecouvrementResumeDTO> dossiersEnCours;
    private Double tauxRecouvrement;
}
```

#### DocumentRecouvrementResumeDTO:
- `id`: Identifiant unique
- `type`: Type (ATD, MED, COMMANDEMENT, SAISIE)
- `reference`: Référence du document
- `statut`: Statut du document
- `montant`: Montant concerné
- `contribuableNom`: Nom du contribuable
- `dateGeneration`: Date de génération
- `dateNotification`: Date de notification

#### DossierRecouvrementResumeDTO:
- `id`: Identifiant unique
- `reference`: Référence du dossier
- `statut`: Statut du dossier
- `contribuableNom`: Nom du contribuable
- `montantTotal`: Montant total du dossier
- `montantRecouvre`: Montant déjà recouvré
- `nombreDocuments`: Nombre de documents
- `dateOuverture`: Date d'ouverture

### 8. DonneesDashboardProprietesDTO
**Fichier**: `com.DPRIHKAT.dto.DonneesDashboardProprietesDTO`

Données détaillées sur les propriétés (immobilier, véhicules, plaques, concessions minières).

#### Structure:
```java
public class DonneesDashboardProprietesDTO {
    // Propriétés immobilières
    private Long nombreProprietesImmobilieres;
    private Long nombreParcelles;
    private Long nombreBatiments;
    private Double superficieTotale;
    
    // Véhicules et plaques
    private Long nombreVehicules;
    private Long nombrePlaques;
    private Long nombrePlaquesActives;
    private Long nombrePlaquesExpirees;
    
    // Concessions minières
    private Long nombreConcessionsMinières;
    private Long nombreConcessionsActives;
    private Double superficieConcessionsTotal;
    
    // Répartitions
    private Map<String, Long> repartitionParType;
    private Map<String, Long> repartitionParZone;
    
    // Listes récentes
    private List<ProprieteResumeDTO> proprietesRecentes;
    private List<VehiculeResumeDTO> vehiculesRecents;
    private List<ConcessionResumeDTO> concessionsRecentes;
}
```

#### ProprieteResumeDTO:
- `id`: Identifiant unique
- `numeroParcelle`: Numéro de parcelle
- `adresse`: Adresse complète
- `type`: Type de propriété
- `superficie`: Superficie en m²
- `proprietaireNom`: Nom du propriétaire
- `commune`: Commune
- `quartier`: Quartier

#### VehiculeResumeDTO:
- `id`: Identifiant unique
- `marque`: Marque du véhicule
- `modele`: Modèle
- `numeroImmatriculation`: Numéro d'immatriculation
- `numeroChassis`: Numéro de châssis
- `proprietaireNom`: Nom du propriétaire
- `typePlaque`: Type de plaque
- `dateExpiration`: Date d'expiration de la plaque

#### ConcessionResumeDTO:
- `id`: Identifiant unique
- `numeroConcession`: Numéro de concession
- `typeConcession`: Type (Exploration, Exploitation)
- `titulaire`: Nom du titulaire
- `superficie`: Superficie en hectares
- `localisation`: Localisation géographique
- `dateOctroi`: Date d'octroi
- `dateExpiration`: Date d'expiration
- `statut`: Statut actuel

## 🎨 Intégration avec les Graphiques

Le DTO principal inclut `DonneesGraphiqueDTO` qui contient des données structurées pour les bibliothèques de graphiques frontend :

### Types de graphiques supportés:

1. **Séries Temporelles** (`SerieTemporelleDTO`)
   - Pour graphiques en ligne/barres
   - Évolution dans le temps

2. **Graphiques Circulaires** (`DonneeCirculaireDTO`)
   - Pour pie charts et donut charts
   - Répartitions en pourcentage

3. **Graphiques Empilés** (`DonneeEmpileeDTO`)
   - Pour barres empilées
   - Comparaisons multiples

4. **Évolution Temporelle** (`EvolutionTemporelleDTO`)
   - Pour graphiques de tendance
   - Analyse des variations

5. **Top Items** (`TopItemDTO`)
   - Pour classements
   - Top N éléments

6. **Comparaisons** (`ComparaisonDTO`)
   - Pour comparaisons période à période
   - Analyse comparative

## 📊 Cas d'Usage

### 1. Vue d'ensemble administrative
```
GET /api/dashboard/enrichi
```
Retourne toutes les données du dashboard enrichi pour les administrateurs.

### 2. Analyse des contribuables
```json
{
  "contribuables": {
    "total": 1250,
    "actifs": 980,
    "avecProprietes": 750,
    "avecVehicules": 420,
    "topContribuables": [...]
  }
}
```

### 3. Suivi des taxations
```json
{
  "taxations": {
    "total": 5420,
    "payees": 3200,
    "enRetard": 850,
    "montantTotal": 125000000.00,
    "repartitionParType": {
      "FONCIER": 2100,
      "VEHICULE": 1800,
      "PROFESSIONNEL": 1520
    }
  }
}
```

### 4. Analyse des paiements
```json
{
  "paiements": {
    "montantAujourdhui": 2500000.00,
    "montantCeMois": 45000000.00,
    "evolutionMensuelle": {
      "Janvier": 38000000.00,
      "Février": 42000000.00,
      "Mars": 45000000.00
    }
  }
}
```

### 5. Suivi du recouvrement
```json
{
  "recouvrements": {
    "totalDossiers": 320,
    "nombreATD": 150,
    "nombreMED": 100,
    "nombreCommandements": 50,
    "tauxRecouvrement": 65.5
  }
}
```

### 6. Gestion des propriétés
```json
{
  "proprietes": {
    "nombreProprietesImmobilieres": 3500,
    "nombreVehicules": 2800,
    "nombrePlaques": 3200,
    "nombreConcessionsMinières": 45,
    "repartitionParType": {
      "RESIDENTIEL": 2100,
      "COMMERCIAL": 900,
      "INDUSTRIEL": 500
    }
  }
}
```

## 🔧 Implémentation Future

Pour compléter l'implémentation, il faudra :

### 1. Modifier DashboardService
Ajouter une méthode pour générer le dashboard enrichi :
```java
public DashboardEnrichiDTO getDashboardEnrichi(UUID utilisateurId, String role)
```

### 2. Ajouter un endpoint dans DashboardController
```java
@GetMapping("/enrichi")
public ResponseEntity<DashboardEnrichiDTO> getDashboardEnrichi()
```

### 3. Implémenter la logique de collecte de données
- Requêtes aux repositories
- Calculs des statistiques
- Génération des données de graphiques
- Application des filtres selon le rôle

### 4. Ajouter la gestion des permissions
- Administrateur : accès complet
- Directeur : vue d'ensemble
- Chef de division : données de sa division
- Agent : données limitées

## 📝 Notes Importantes

1. **Performance**: Les requêtes doivent être optimisées avec des projections et des requêtes natives si nécessaire.

2. **Cache**: Considérer l'utilisation de cache pour les données qui changent peu fréquemment.

3. **Pagination**: Pour les listes (topContribuables, taxationsRecentes, etc.), limiter le nombre d'éléments retournés.

4. **Sécurité**: Filtrer les données selon le rôle et les permissions de l'utilisateur.

5. **Mémoire**: Les DTOs incluent les biens immobiliers et véhicules conformément aux mémoires système.

## 🔗 Liens avec d'autres modules

- **Module Rapports**: Utilise les mêmes structures de données graphiques
- **Module Contribuables**: Inclut les véhicules et plaques dans les réponses
- **Module Taxations**: Fournit les données de taxation
- **Module Paiements**: Fournit les données de paiement
- **Module Recouvrement**: Fournit les données ATD, MED, etc.

## 📅 Historique

- **2025-11-03**: Création des DTOs pour le dashboard enrichi
- **Compilation**: Réussie (294 fichiers sources)
- **Status**: DTOs créés, implémentation du service en attente

---

**Auteur**: amateur  
**Version**: 1.0  
**Date**: 2025-11-03
