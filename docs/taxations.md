# Documentation API - Gestion des Taxations

Cette documentation détaille les endpoints disponibles pour la gestion des taxations dans l'API DPRIHKAT.

## Vue d'ensemble

Les taxations représentent les impôts calculés et appliqués sur les propriétés déclarées par les contribuables. Elles sont générées à partir des déclarations validées et permettent de suivre le processus de recouvrement des impôts.

## Base URL

```
/api/taxations
```

## Endpoints

### 1. Récupérer toutes les taxations

Récupère la liste de toutes les taxations enregistrées dans le système.

- **URL**: `/api/taxations`
- **Méthode**: `GET`
- **Rôles autorisés**: `TAXATEUR`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `ADMIN`
- **Paramètres**: Aucun

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "taxations": [
      {
        "id": "uuid-string",
        "dateTaxation": "2025-02-15T10:30:45.123Z",
        "montant": 1200000.00,
        "exercice": "2025",
        "statut": "EN_COURS",
        "typeImpot": "IF",
        "exoneration": false,
        "motifExoneration": null,
        "dateEcheance": "2025-03-15T00:00:00.000Z",
        "actif": true,
        "declaration": {
          "id": "uuid-string",
          "dateDeclaration": "2025-01-15T10:30:45.123Z"
        },
        "propriete": {
          "id": "uuid-string",
          "designation": "Résidence principale",
          "adresse": "Avenue 123, Quartier Centre-ville"
        },
        "contribuable": {
          "id": "uuid-string",
          "nom": "Nom du contribuable"
        }
      }
    ]
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "TAXATION_FETCH_ERROR",
    "message": "Erreur lors de la récupération des taxations",
    "details": "Message d'erreur détaillé"
  }
}
```

### 2. Récupérer toutes les taxations actives

Récupère la liste de toutes les taxations actives dans le système.

- **URL**: `/api/taxations/actives`
- **Méthode**: `GET`
- **Rôles autorisés**: `TAXATEUR`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `ADMIN`
- **Paramètres**: Aucun

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "taxations": [
      {
        "id": "uuid-string",
        "dateTaxation": "2025-02-15T10:30:45.123Z",
        "montant": 1200000.00,
        "exercice": "2025",
        "statut": "EN_COURS",
        "typeImpot": "IF",
        "exoneration": false,
        "motifExoneration": null,
        "dateEcheance": "2025-03-15T00:00:00.000Z",
        "actif": true,
        "declaration": {
          "id": "uuid-string",
          "dateDeclaration": "2025-01-15T10:30:45.123Z"
        },
        "propriete": {
          "id": "uuid-string",
          "designation": "Résidence principale",
          "adresse": "Avenue 123, Quartier Centre-ville"
        },
        "contribuable": {
          "id": "uuid-string",
          "nom": "Nom du contribuable"
        }
      }
    ]
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "TAXATION_FETCH_ERROR",
    "message": "Erreur lors de la récupération des taxations actives",
    "details": "Message d'erreur détaillé"
  }
}
```

### 3. Récupérer une taxation par son ID

Récupère les détails d'une taxation spécifique à partir de son identifiant unique.

- **URL**: `/api/taxations/{id}`
- **Méthode**: `GET`
- **Rôles autorisés**: `TAXATEUR`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `ADMIN`
- **Paramètres**:
  - `id` (path): UUID de la taxation à récupérer

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "taxation": {
      "id": "uuid-string",
      "dateTaxation": "2025-02-15T10:30:45.123Z",
      "montant": 1200000.00,
      "exercice": "2025",
      "statut": "EN_COURS",
      "typeImpot": "IF",
      "exoneration": false,
      "motifExoneration": null,
      "dateEcheance": "2025-03-15T00:00:00.000Z",
      "actif": true,
      "declaration": {
        "id": "uuid-string",
        "dateDeclaration": "2025-01-15T10:30:45.123Z"
      },
      "propriete": {
        "id": "uuid-string",
        "designation": "Résidence principale",
        "adresse": "Avenue 123, Quartier Centre-ville"
      },
      "contribuable": {
        "id": "uuid-string",
        "nom": "Nom du contribuable"
      }
    }
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "TAXATION_FETCH_ERROR",
    "message": "Erreur lors de la récupération de la taxation",
    "details": "Message d'erreur détaillé"
  }
}
```

### 4. Récupérer les taxations par propriété

Récupère la liste des taxations pour une propriété spécifique.

- **URL**: `/api/taxations/by-propriete/{proprieteId}`
- **Méthode**: `GET`
- **Rôles autorisés**: `TAXATEUR`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `ADMIN`
- **Paramètres**:
  - `proprieteId` (path): UUID de la propriété

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "taxations": [
      {
        "id": "uuid-string",
        "dateTaxation": "2025-02-15T10:30:45.123Z",
        "montant": 1200000.00,
        "exercice": "2025",
        "statut": "EN_COURS",
        "typeImpot": "IF",
        "exoneration": false,
        "motifExoneration": null,
        "dateEcheance": "2025-03-15T00:00:00.000Z",
        "actif": true,
        "declaration": {
          "id": "uuid-string",
          "dateDeclaration": "2025-01-15T10:30:45.123Z"
        },
        "propriete": {
          "id": "uuid-string",
          "designation": "Résidence principale",
          "adresse": "Avenue 123, Quartier Centre-ville"
        },
        "contribuable": {
          "id": "uuid-string",
          "nom": "Nom du contribuable"
        }
      }
    ]
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "TAXATION_FETCH_ERROR",
    "message": "Erreur lors de la récupération des taxations pour la propriété",
    "details": "Message d'erreur détaillé"
  }
}
```

### 5. Récupérer les taxations par exercice

Récupère la liste des taxations pour un exercice fiscal spécifique.

- **URL**: `/api/taxations/by-exercice/{exercice}`
- **Méthode**: `GET`
- **Rôles autorisés**: `TAXATEUR`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `ADMIN`
- **Paramètres**:
  - `exercice` (path): Année fiscale (ex: 2025)

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "taxations": [
      {
        "id": "uuid-string",
        "dateTaxation": "2025-02-15T10:30:45.123Z",
        "montant": 1200000.00,
        "exercice": "2025",
        "statut": "EN_COURS",
        "typeImpot": "IF",
        "exoneration": false,
        "motifExoneration": null,
        "dateEcheance": "2025-03-15T00:00:00.000Z",
        "actif": true,
        "declaration": {
          "id": "uuid-string",
          "dateDeclaration": "2025-01-15T10:30:45.123Z"
        },
        "propriete": {
          "id": "uuid-string",
          "designation": "Résidence principale",
          "adresse": "Avenue 123, Quartier Centre-ville"
        },
        "contribuable": {
          "id": "uuid-string",
          "nom": "Nom du contribuable"
        }
      }
    ]
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "TAXATION_FETCH_ERROR",
    "message": "Erreur lors de la récupération des taxations pour l'exercice",
    "details": "Message d'erreur détaillé"
  }
}
```

### 6. Récupérer les taxations par type d'impôt

Récupère la liste des taxations pour un type d'impôt spécifique.

- **URL**: `/api/taxations/by-type-impot/{typeImpot}`
- **Méthode**: `GET`
- **Rôles autorisés**: `TAXATEUR`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `ADMIN`
- **Paramètres**:
  - `typeImpot` (path): Type d'impôt (ex: IF, IRL, ICM)

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "taxations": [
      {
        "id": "uuid-string",
        "dateTaxation": "2025-02-15T10:30:45.123Z",
        "montant": 1200000.00,
        "exercice": "2025",
        "statut": "EN_COURS",
        "typeImpot": "IF",
        "exoneration": false,
        "motifExoneration": null,
        "dateEcheance": "2025-03-15T00:00:00.000Z",
        "actif": true,
        "declaration": {
          "id": "uuid-string",
          "dateDeclaration": "2025-01-15T10:30:45.123Z"
        },
        "propriete": {
          "id": "uuid-string",
          "designation": "Résidence principale",
          "adresse": "Avenue 123, Quartier Centre-ville"
        },
        "contribuable": {
          "id": "uuid-string",
          "nom": "Nom du contribuable"
        }
      }
    ]
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "TAXATION_FETCH_ERROR",
    "message": "Erreur lors de la récupération des taxations pour le type d'impôt",
    "details": "Message d'erreur détaillé"
  }
}
```

### 7. Créer une nouvelle taxation

Crée une nouvelle taxation dans le système.

- **URL**: `/api/taxations`
- **Méthode**: `POST`
- **Rôles autorisés**: `TAXATEUR`, `CHEF_DE_BUREAU`, `ADMIN`
- **Corps de la requête**:

```json
{
  "declarationId": "uuid-string",
  "montant": 1200000.00,
  "exercice": "2025",
  "typeImpot": "IF",
  "exoneration": false,
  "motifExoneration": null,
  "dateEcheance": "2025-03-15T00:00:00.000Z"
}
```

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "taxation": {
      "id": "uuid-string",
      "dateTaxation": "2025-02-15T10:30:45.123Z",
      "montant": 1200000.00,
      "exercice": "2025",
      "statut": "EN_COURS",
      "typeImpot": "IF",
      "exoneration": false,
      "motifExoneration": null,
      "dateEcheance": "2025-03-15T00:00:00.000Z",
      "actif": true,
      "declaration": {
        "id": "uuid-string",
        "dateDeclaration": "2025-01-15T10:30:45.123Z"
      },
      "propriete": {
        "id": "uuid-string",
        "designation": "Résidence principale",
        "adresse": "Avenue 123, Quartier Centre-ville"
      },
      "contribuable": {
        "id": "uuid-string",
        "nom": "Nom du contribuable"
      }
    }
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "TAXATION_CREATE_ERROR",
    "message": "Erreur lors de la création de la taxation",
    "details": "Message d'erreur détaillé"
  }
}
```

### 8. Mettre à jour une taxation

Met à jour les informations d'une taxation existante.

- **URL**: `/api/taxations/{id}`
- **Méthode**: `PUT`
- **Rôles autorisés**: `TAXATEUR`, `CHEF_DE_BUREAU`, `ADMIN`
- **Paramètres**:
  - `id` (path): UUID de la taxation à mettre à jour
- **Corps de la requête**:

```json
{
  "montant": 1300000.00,
  "exoneration": true,
  "motifExoneration": "Exonération pour travaux de rénovation énergétique",
  "dateEcheance": "2025-04-15T00:00:00.000Z",
  "statut": "EXONEREE"
}
```

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "taxation": {
      "id": "uuid-string",
      "dateTaxation": "2025-02-15T10:30:45.123Z",
      "montant": 1300000.00,
      "exercice": "2025",
      "statut": "EXONEREE",
      "typeImpot": "IF",
      "exoneration": true,
      "motifExoneration": "Exonération pour travaux de rénovation énergétique",
      "dateEcheance": "2025-04-15T00:00:00.000Z",
      "actif": true,
      "declaration": {
        "id": "uuid-string",
        "dateDeclaration": "2025-01-15T10:30:45.123Z"
      },
      "propriete": {
        "id": "uuid-string",
        "designation": "Résidence principale",
        "adresse": "Avenue 123, Quartier Centre-ville"
      },
      "contribuable": {
        "id": "uuid-string",
        "nom": "Nom du contribuable"
      }
    }
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "TAXATION_UPDATE_ERROR",
    "message": "Erreur lors de la mise à jour de la taxation",
    "details": "Message d'erreur détaillé"
  }
}
```

### 9. Supprimer une taxation

Supprime une taxation du système (suppression logique).

- **URL**: `/api/taxations/{id}`
- **Méthode**: `DELETE`
- **Rôles autorisés**: `CHEF_DE_BUREAU`, `ADMIN`
- **Paramètres**:
  - `id` (path): UUID de la taxation à supprimer

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "message": "Taxation supprimée avec succès"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "TAXATION_DELETE_ERROR",
    "message": "Erreur lors de la suppression de la taxation",
    "details": "Message d'erreur détaillé"
  }
}
```

## Structure de l'entité Taxation

| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant unique de la taxation |
| dateTaxation | Date | Date de création de la taxation |
| montant | Double | Montant de la taxation |
| exercice | String | Année fiscale concernée par la taxation |
| statut | Enum (StatutTaxation) | Statut de la taxation (EN_COURS, PAYEE, EXONEREE, ANNULEE) |
| typeImpot | Enum (TypeImpot) | Type d'impôt concerné (IF, IRL, ICM, etc.) |
| exoneration | boolean | Indique si la taxation bénéficie d'une exonération |
| motifExoneration | String | Motif de l'exonération (si applicable) |
| dateEcheance | Date | Date d'échéance de la taxation |
| actif | boolean | Indique si la taxation est active dans le système |
| declaration | Declaration | Déclaration associée à cette taxation |

## Structure de la requête TaxationRequestDTO

| Champ | Type | Description |
|-------|------|-------------|
| declarationId | UUID | Identifiant de la déclaration associée |
| montant | Double | Montant de la taxation |
| exercice | String | Année fiscale |
| typeImpot | Enum (TypeImpot) | Type d'impôt concerné |
| exoneration | boolean | Indique si la taxation bénéficie d'une exonération |
| motifExoneration | String | Motif de l'exonération (si applicable) |
| dateEcheance | Date | Date d'échéance de la taxation |

## Règles métier

1. Une taxation est créée à partir d'une déclaration validée.
2. Le montant de la taxation peut être différent du montant déclaré, selon les barèmes et les règles fiscales en vigueur.
3. Une taxation peut être exonérée, avec un motif d'exonération.
4. Une taxation a une date d'échéance, après laquelle des pénalités peuvent s'appliquer.
5. Une taxation passe par plusieurs statuts : EN_COURS → PAYEE (ou EXONEREE, ANNULEE).
6. Seuls les utilisateurs avec les rôles appropriés peuvent créer, modifier ou supprimer des taxations.
