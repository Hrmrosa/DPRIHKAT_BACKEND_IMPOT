# Documentation Complète - Gestion et Taxation des Véhicules

## Table des matières
1. [Modèle de Données](#1-modèle-de-données)
2. [API Véhicules](#2-api-véhicules)
3. [API Taxation](#3-api-taxation)
4. [Processus Complet](#4-processus-complet)
5. [Configuration](#5-configuration)

## 1. Modèle de Données

### 1.1 Entités Principales

#### Vehicule
```java
@Entity
public class Vehicule {
    @Id
    @GeneratedValue
    private UUID id;
    
    private String immatriculation;
    private String marque;
    private String modele;
    private Integer annee;
    private Double puissanceFiscale;
    
    // Champs pour la taxation
    private String genre;
    private String categorie;
    
    @ManyToOne
    private Contribuable proprietaire;
    
    @OneToMany(mappedBy = "vehicule")
    private List<Vignette> vignettes;
}
```

#### Vignette
```java
@Entity
public class Vignette {
    @Id
    @GeneratedValue
    private UUID id;
    
    private String numero;
    private Date dateEmission;
    private Date dateExpiration;
    private Double montant;
    private StatutVignette statut;
    
    @ManyToOne
    private Vehicule vehicule;
    
    @ManyToOne
    private Agent agent;
    
    @OneToOne
    private Taxation taxation;
}
```

#### Taxation
```java
@Entity
public class Taxation {
    @Id
    @GeneratedValue
    private UUID id;
    
    private Date dateTaxation;
    private Double montant;
    private String exercice;
    private StatutTaxation statut;
    private TypeImpot typeImpot; // IRV pour les vignettes
    
    @ManyToOne
    private Contribuable contribuable;
}
```

## 2. API Véhicules

### 2.1 Créer un Véhicule
```
POST /api/vehicules
Content-Type: application/json
Authorization: Bearer {token}

{
  "immatriculation": "AB-123-CD",
  "marque": "Toyota",
  "modele": "Corolla",
  "annee": 2020,
  "puissanceFiscale": 5,
  "proprietaire": {
    "id": "123e4567-e89b-12d3-a456-426614174000"
  }
}
```

**Permissions**: `AGENT_DE_PLAQUES`, `TAXATEUR`, `ADMIN`

**Réponse**:
```json
{
  "success": true,
  "data": {
    "vehicule": {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "immatriculation": "AB-123-CD",
      "marque": "Toyota",
      "modele": "Corolla"
    },
    "message": "Véhicule créé avec succès"
  }
}
```

### 2.2 Récupérer un Véhicule
```
GET /api/vehicules/{id}
Authorization: Bearer {token}
```

**Permissions**: `AGENT_DE_PLAQUES`, `TAXATEUR`, `ADMIN`, `CONTRIBUABLE` (uniquement ses propres véhicules)

**Réponse**:
```json
{
  "success": true,
  "data": {
    "vehicule": {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "immatriculation": "AB-123-CD",
      "marque": "Toyota",
      "modele": "Corolla",
      "annee": 2020,
      "puissanceFiscale": 5,
      "genre": "Véhicule particulier",
      "categorie": "Personne Physique",
      "proprietaire": {
        "id": "123e4567-e89b-12d3-a456-426614174001",
        "nom": "Dupont"
      }
    }
  }
}
```

### 2.3 Lister les Véhicules d'un Contribuable
```
GET /api/contribuables/{id}/vehicules
Authorization: Bearer {token}
```

**Permissions**: `AGENT_DE_PLAQUES`, `TAXATEUR`, `ADMIN`, `CONTRIBUABLE` (uniquement ses propres véhicules)

**Réponse**:
```json
{
  "success": true,
  "data": {
    "vehicules": [
      {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "immatriculation": "AB-123-CD",
        "marque": "Toyota",
        "modele": "Corolla"
      },
      {
        "id": "123e4567-e89b-12d3-a456-426614174002",
        "immatriculation": "EF-456-GH",
        "marque": "Renault",
        "modele": "Clio"
      }
    ],
    "count": 2
  }
}
```

### 2.4 Mettre à Jour un Véhicule
```
PUT /api/vehicules/{id}
Content-Type: application/json
Authorization: Bearer {token}

{
  "marque": "Toyota",
  "modele": "Corolla Sport",
  "annee": 2021
}
```

**Permissions**: `AGENT_DE_PLAQUES`, `TAXATEUR`, `ADMIN`

**Réponse**:
```json
{
  "success": true,
  "data": {
    "vehicule": {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "immatriculation": "AB-123-CD",
      "marque": "Toyota",
      "modele": "Corolla Sport",
      "annee": 2021
    },
    "message": "Véhicule mis à jour avec succès"
  }
}
```

### 2.5 Classifier un Véhicule
```
PUT /api/vehicules/{id}/classification
Content-Type: application/x-www-form-urlencoded
Authorization: Bearer {token}

genre=Véhicule particulier&categorie=Personne Physique
```

**Permissions**: `AGENT_DE_PLAQUES`, `TAXATEUR`, `ADMIN`

**Réponse**:
```json
{
  "success": true,
  "data": {
    "vehicule": {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "genre": "Véhicule particulier",
      "categorie": "Personne Physique"
    },
    "message": "Classification du véhicule mise à jour avec succès"
  }
}
```

### 2.6 Mutation de véhicule entre contribuables

Change le propriétaire d'un véhicule d'un contribuable à un autre.

- **URL**: `/api/vehicules/{vehiculeId}/changer-proprietaire`
- **Méthode**: `PUT`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`
- **Corps de la requête**:
```json
{
  "nouveauProprietaireId": "uuid-string"
}
```

#### Réponse en cas de succès
```json
{
  "success": true,
  "message": "Propriétaire du véhicule mis à jour"
}
```

#### Réponse en cas d'erreur
```json
{
  "success": false,
  "error": {
    "code": "VEHICULE_NOT_FOUND",
    "message": "Véhicule ou nouveau propriétaire non trouvé"
  }
}
```

### 2.7 Supprimer un Véhicule
```
DELETE /api/vehicules/{id}
Authorization: Bearer {token}
```

**Permissions**: `ADMIN`

**Réponse**:
```json
{
  "success": true,
  "data": {
    "message": "Véhicule supprimé avec succès"
  }
}
```

## 3. API Taxation

### 3.1 Générer une Vignette
```
POST /api/vignettes
Content-Type: application/json
Authorization: Bearer {token}

{
  "vehiculeId": "123e4567-e89b-12d3-a456-426614174000",
  "agentId": "123e4567-e89b-12d3-a456-426614174003"
}
```

**Permissions**: `TAXATEUR`, `ADMIN`

**Réponse**:
```json
{
  "success": true,
  "data": {
    "vignette": {
      "id": "123e4567-e89b-12d3-a456-426614174004",
      "numero": "VIG-2025-12345",
      "dateEmission": "2025-09-17T14:00:00Z",
      "dateExpiration": "2026-09-17T14:00:00Z",
      "montant": 54.0,
      "statut": "ACTIVE"
    },
    "taxation": {
      "id": "123e4567-e89b-12d3-a456-426614174005",
      "numeroTaxation": "VIG_12345_VEHICULE_PARTICULIER_PERSONNE_PHYSIQUE_2025",
      "montant": 54.0,
      "statut": "EN_ATTENTE"
    },
    "message": "Vignette générée avec succès"
  }
}
```

### 3.2 Récupérer les Vignettes d'un Véhicule
```
GET /api/vehicules/{id}/vignettes
Authorization: Bearer {token}
```

**Permissions**: `AGENT_DE_PLAQUES`, `TAXATEUR`, `ADMIN`, `CONTRIBUABLE` (uniquement ses propres véhicules)

**Réponse**:
```json
{
  "success": true,
  "data": {
    "vignettes": [
      {
        "id": "123e4567-e89b-12d3-a456-426614174004",
        "numero": "VIG-2025-12345",
        "dateEmission": "2025-09-17T14:00:00Z",
        "dateExpiration": "2026-09-17T14:00:00Z",
        "montant": 54.0,
        "statut": "ACTIVE"
      },
      {
        "id": "123e4567-e89b-12d3-a456-426614174006",
        "numero": "VIG-2024-67890",
        "dateEmission": "2024-09-17T14:00:00Z",
        "dateExpiration": "2025-09-17T14:00:00Z",
        "montant": 50.0,
        "statut": "EXPIREE"
      }
    ],
    "count": 2
  }
}
```

### 3.3 Payer une Taxation
```
POST /api/taxations/{id}/payer
Content-Type: application/json
Authorization: Bearer {token}

{
  "montant": 54.0,
  "modePaiement": "ESPECES",
  "reference": "REF-12345"
}
```

**Permissions**: `RECEVEUR_DES_IMPOTS`, `ADMIN`

**Réponse**:
```json
{
  "success": true,
  "data": {
    "taxation": {
      "id": "123e4567-e89b-12d3-a456-426614174005",
      "statut": "PAYEE",
      "paiement": {
        "id": "123e4567-e89b-12d3-a456-426614174007",
        "montant": 54.0,
        "datePaiement": "2025-09-17T14:30:00Z",
        "modePaiement": "ESPECES",
        "reference": "REF-12345"
      }
    },
    "message": "Paiement enregistré avec succès"
  }
}
```

## 4. Processus Complet

### 4.1 Workflow de Taxation d'un Véhicule
1. **Enregistrement du véhicule**
   ```
   POST /api/vehicules
   ```

2. **Classification du véhicule**
   ```
   PUT /api/vehicules/{id}/classification
   ```

3. **Génération de la vignette**
   ```
   POST /api/vignettes
   ```

4. **Paiement de la taxation**
   ```
   POST /api/taxations/{id}/payer
   ```

5. **Impression de la vignette**
   ```
   GET /api/vignettes/{id}/imprimer
   ```

### 4.2 Calcul du Montant de la Taxation
Le calcul du montant de la taxation est basé sur plusieurs critères définis dans le fichier `taux_IRV.json` :

1. **Genre du véhicule** : Véhicule particulier, utilitaire, etc.
2. **Catégorie du contribuable** : Personne physique ou morale
3. **Puissance fiscale** : Répartie en plages (1-10 CV, 11-15 CV, +15 CV)

Exemple de calcul :
```java
// Dans VignetteService
public Double calculerTaxation(Vehicule vehicule) {
    String genre = vehicule.getGenre();
    String categorie = vehicule.getCategorie();
    Double puissance = vehicule.getPuissanceFiscale();
    
    // Déterminer la plage de puissance
    String plage = determinePlagePuissance(puissance);
    
    // Récupérer les taux dans taux_IRV.json
    JsonNode tauxNode = getTauxFromJson(genre, categorie, plage);
    
    // Calculer le montant
    Double vignette = tauxNode.get("vignette").asDouble();
    Double tscr = tauxNode.get("tscr").asDouble();
    
    return vignette + tscr;
}
```

## 5. Configuration

### 5.1 Structure du Fichier taux_IRV.json
```json
{
  "taxes_circulation": {
    "categories": [
      {
        "categorie": "Personnes Physiques",
        "vehicules": [
          {
            "puissance": "1-10 CV",
            "taux_usd": {
              "vignette": 35,
              "tscr": 19,
              "total": 54
            },
            "taux_cdf": "Contre-valeur au cours vendeur du jour de paiement"
          },
          {
            "puissance": "11-15 CV",
            "taux_usd": {
              "vignette": 44,
              "tscr": 25,
              "total": 69
            },
            "taux_cdf": "Contre-valeur au cours vendeur du jour de paiement"
          },
          {
            "puissance": "+15 CV",
            "taux_usd": {
              "vignette": 49,
              "tscr": 25,
              "total": 74
            },
            "taux_cdf": "Contre-valeur au cours vendeur du jour de paiement"
          }
        ]
      },
      {
        "categorie": "Personnes Morales",
        "vehicules": [
          {
            "puissance": "1-10 CV",
            "taux_usd": {
              "vignette": 64,
              "tscr": 35,
              "total": 99
            },
            "taux_cdf": "Contre-valeur au cours vendeur du jour de paiement"
          }
        ]
      }
    ]
  }
}
```

### 5.2 Bonnes Pratiques
1. **Validation des données** : Toujours valider l'immatriculation et la puissance fiscale
2. **Classification** : Classifier le véhicule avant de générer une vignette
3. **Taux** : Vérifier régulièrement la cohérence des taux dans `taux_IRV.json`
4. **Numéros de taxation** : Utiliser un format standardisé pour les numéros de taxation
5. **Permissions** : Respecter strictement les permissions définies pour chaque endpoint
