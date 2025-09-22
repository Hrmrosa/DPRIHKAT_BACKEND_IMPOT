# Documentation API - Impôt sur les Concessions Minières (ICM)

## 1. Vue d'ensemble

L'impôt sur les concessions minières (ICM) est un impôt annuel dû par les titulaires de concessions minières. Cette API permet de gérer les concessions minières et de générer les taxations correspondantes.

## 2. Modèle de données

### 2.1 Entités principales

#### ConcessionMinier
```java
class ConcessionMinier {
    UUID id;
    Double nombreCarresMinier;
    Double superficie;
    Date dateAcquisition;
    TypeConcession type; // RECHERCHE, EXPLOITATION
    String annexe;
    Geometry location;
    Contribuable titulaire;
    Double montantImpot;
    Taxation taxation;
}
```

#### Taxation
```java
class Taxation {
    UUID id;
    Date dateTaxation;
    Double montant;
    String exercice;
    Devise devise;
    StatutTaxation statut; // EMISE, PAYEE, ANNULEE
    TypeImpot typeImpot; // ICM pour les concessions minières
    boolean exoneration;
    Date dateEcheance;
    String numeroTaxation;
    Agent agent;
    Contribuable contribuable;
    ConcessionMinier concessionMinier;
}
```

## 3. Endpoints API

### 3.1 Gestion des concessions minières

#### Récupérer toutes les concessions
```
GET /api/concessions
```
**Permissions**: `TAXATEUR`, `RECEVEUR_DES_IMPOTS`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `ADMIN`, `INFORMATICIEN`

**Réponse**:
```json
{
  "success": true,
  "data": {
    "concessions": [
      {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "nombreCarresMinier": 100.0,
        "superficie": 500.0,
        "dateAcquisition": "2025-01-15T00:00:00Z",
        "type": "EXPLOITATION",
        "annexe": "A",
        "titulaire": "123e4567-e89b-12d3-a456-426614174001",
        "montantImpot": 8495.5
      }
    ]
  }
}
```

#### Récupérer une concession par son ID
```
GET /api/concessions/{id}
```
**Permissions**: `TAXATEUR`, `RECEVEUR_DES_IMPOTS`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `ADMIN`, `INFORMATICIEN`

**Réponse**:
```json
{
  "success": true,
  "data": {
    "concession": {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "nombreCarresMinier": 100.0,
      "superficie": 500.0,
      "dateAcquisition": "2025-01-15T00:00:00Z",
      "type": "EXPLOITATION",
      "annexe": "A",
      "titulaire": "123e4567-e89b-12d3-a456-426614174001",
      "montantImpot": 8495.5
    }
  }
}
```

#### Récupérer mes concessions (pour un contribuable)
```
GET /api/concessions/mine
```
**Permissions**: `CONTRIBUABLE`

**Réponse**:
```json
{
  "success": true,
  "data": {
    "concessions": [
      {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "nombreCarresMinier": 100.0,
        "superficie": 500.0,
        "dateAcquisition": "2025-01-15T00:00:00Z",
        "type": "EXPLOITATION",
        "annexe": "A",
        "titulaire": "123e4567-e89b-12d3-a456-426614174001",
        "montantImpot": 8495.5
      }
    ]
  }
}
```

### 3.2 Taxation des concessions minières

#### Taxer une concession
```
POST /api/concessions/{id}/taxer
```
**Permissions**: `TAXATEUR`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `ADMIN`

**Paramètres**:
- `exercice` (optionnel): Année fiscale (par défaut, l'année en cours)

**Réponse**:
```json
{
  "success": true,
  "data": {
    "taxation": {
      "id": "123e4567-e89b-12d3-a456-426614174002",
      "dateTaxation": "2025-09-16T09:30:00Z",
      "montant": 8495.5,
      "exercice": "2025",
      "statut": "EMISE",
      "typeImpot": "ICM",
      "dateEcheance": "2025-10-16T09:30:00Z",
      "numeroTaxation": "t_0001_ICM_HK_2025"
    },
    "message": "Taxation créée avec succès pour la concession minière"
  }
}
```

### 3.3 Gestion des taxations par contribuable

#### Récupérer les taxations d'un contribuable
```
GET /api/contribuables/{id}/taxations
```
**Permissions**: `TAXATEUR`, `RECEVEUR_DES_IMPOTS`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `ADMIN`, `CONTRIBUABLE` (uniquement ses propres taxations)

**Réponse**:
```json
{
  "success": true,
  "data": {
    "taxations": [
      {
        "id": "123e4567-e89b-12d3-a456-426614174002",
        "dateTaxation": "2025-09-16T09:30:00Z",
        "montant": 8495.5,
        "exercice": "2025",
        "statut": "EMISE",
        "typeImpot": "ICM",
        "dateEcheance": "2025-10-16T09:30:00Z",
        "numeroTaxation": "t_0001_ICM_HK_2025"
      }
    ],
    "count": 1
  }
}
```

#### Récupérer mes taxations (pour un contribuable)
```
GET /api/contribuables/me/taxations
```
**Permissions**: `CONTRIBUABLE`

**Réponse**:
```json
{
  "success": true,
  "data": {
    "taxations": [
      {
        "id": "123e4567-e89b-12d3-a456-426614174002",
        "dateTaxation": "2025-09-16T09:30:00Z",
        "montant": 8495.5,
        "exercice": "2025",
        "statut": "EMISE",
        "typeImpot": "ICM",
        "dateEcheance": "2025-10-16T09:30:00Z",
        "numeroTaxation": "t_0001_ICM_HK_2025"
      }
    ],
    "count": 1
  }
}
```

## 4. Calcul de l'impôt

Le calcul de l'impôt sur les concessions minières est basé sur la formule suivante :

```
montantImpot = nombreCarresMinier * 84.955 * taux
```

Où :
- `nombreCarresMinier` : Nombre de carrés miniers de la concession
- `84.955` : Constante de base pour le calcul de l'ICM
- `taux` : Taux applicable selon le type de concession, l'annexe et le type de contribuable

Les taux sont définis dans le fichier `taux_icm.json` et varient selon :
- Le type de concession (recherche ou exploitation)
- L'annexe (A, B, C, etc.)
- Le type de contribuable (personne physique ou morale)

## 5. Codes d'erreur

| Code | Message | Description |
|------|---------|-------------|
| CONCESSIONS_FETCH_ERROR | Erreur lors de la récupération des concessions | Problème lors de la récupération des concessions |
| CONCESSION_NOT_FOUND | Concession non trouvée | La concession demandée n'existe pas |
| INVALID_USER | Utilisateur non valide | L'utilisateur n'a pas les droits nécessaires |
| TAXATION_ERROR | Erreur lors de la taxation de la concession | Problème lors de la création de la taxation |
| TAXATIONS_FETCH_ERROR | Erreur lors de la récupération des taxations | Problème lors de la récupération des taxations |
| ACCESS_DENIED | Accès refusé | L'utilisateur n'a pas les droits d'accès |
