# Documentation API - Gestion des Déclarations

Cette documentation détaille les endpoints disponibles pour la gestion des déclarations d'impôts dans l'API DPRIHKAT.

## Vue d'ensemble

Les déclarations représentent les déclarations d'impôts soumises par les contribuables ou enregistrées manuellement par les agents. Elles sont liées à une propriété et à un type d'impôt spécifique.

## Base URL

```
/api/declarations
```

## Endpoints

### 1. Soumettre une déclaration en ligne

Permet à un contribuable de soumettre une déclaration d'impôt en ligne.

- **URL**: `/api/declarations/soumettre`
- **Méthode**: `POST`
- **Rôles autorisés**: `CONTRIBUABLE`, `ADMIN`
- **Restrictions temporelles**: Cette opération n'est autorisée qu'entre le 2 janvier et le 1er février de chaque année.
- **Corps de la requête**:

```json
{
  "proprieteId": "uuid-string",
  "typeImpot": "IF",
  "montantDeclare": 1000000.00,
  "exercice": "2025",
  "description": "Déclaration d'impôt foncier pour ma résidence principale",
  "location": {
    "latitude": -11.6642,
    "longitude": 27.4661
  }
}
```

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "declaration": {
      "id": "uuid-string",
      "dateDeclaration": "2025-01-15T10:30:45.123Z",
      "montantDeclare": 1000000.00,
      "exercice": "2025",
      "statut": "SOUMISE",
      "typeImpot": "IF",
      "description": "Déclaration d'impôt foncier pour ma résidence principale",
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
    "code": "INVALID_PERIOD",
    "message": "Période de déclaration invalide",
    "details": "Les déclarations en ligne ne peuvent être soumises qu'entre le 2 janvier et le 1er février"
  }
}
```

### 2. Enregistrer une déclaration manuelle

Permet à un agent (taxateur ou receveur des impôts) d'enregistrer une déclaration manuelle pour un contribuable.

- **URL**: `/api/declarations/manuelle`
- **Méthode**: `POST`
- **Rôles autorisés**: `TAXATEUR`, `RECEVEUR_DES_IMPOTS`, `ADMIN`
- **Corps de la requête**:

```json
{
  "proprieteId": "uuid-string",
  "typeImpot": "IF",
  "montantDeclare": 1000000.00,
  "exercice": "2025",
  "description": "Déclaration d'impôt foncier enregistrée manuellement",
  "location": {
    "latitude": -11.6642,
    "longitude": 27.4661
  }
}
```

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "declaration": {
      "id": "uuid-string",
      "dateDeclaration": "2025-01-15T10:30:45.123Z",
      "montantDeclare": 1000000.00,
      "exercice": "2025",
      "statut": "SOUMISE",
      "typeImpot": "IF",
      "description": "Déclaration d'impôt foncier enregistrée manuellement",
      "propriete": {
        "id": "uuid-string",
        "designation": "Résidence principale",
        "adresse": "Avenue 123, Quartier Centre-ville"
      },
      "contribuable": {
        "id": "uuid-string",
        "nom": "Nom du contribuable"
      },
      "agentValidateur": {
        "id": "uuid-string",
        "nom": "Nom de l'agent"
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
    "code": "DECLARATION_MANUAL_ERROR",
    "message": "Erreur lors de l'enregistrement de la déclaration manuelle",
    "details": "Message d'erreur détaillé"
  }
}
```

### 3. Récupérer toutes les déclarations (paginées)

Récupère la liste des déclarations avec pagination. Pour les contribuables, seules leurs propres déclarations sont retournées.

- **URL**: `/api/declarations`
- **Méthode**: `GET`
- **Rôles autorisés**: `TAXATEUR`, `RECEVEUR_DES_IMPOTS`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `CONTRIBUABLE`, `ADMIN`
- **Paramètres**:
  - `page` (query, optionnel): Numéro de page (défaut: 0)
  - `size` (query, optionnel): Nombre d'éléments par page (défaut: 10)
  - `sortBy` (query, optionnel): Champ de tri (défaut: "date")
  - `sortDir` (query, optionnel): Direction du tri (asc/desc, défaut: "desc")

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "declarations": [
      {
        "id": "uuid-string",
        "dateDeclaration": "2025-01-15T10:30:45.123Z",
        "montantDeclare": 1000000.00,
        "exercice": "2025",
        "statut": "SOUMISE",
        "typeImpot": "IF",
        "description": "Déclaration d'impôt foncier",
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
    ],
    "currentPage": 0,
    "totalItems": 42,
    "totalPages": 5
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "DECLARATION_FETCH_ERROR",
    "message": "Erreur lors de la récupération des déclarations",
    "details": "Message d'erreur détaillé"
  }
}
```

### 4. Récupérer une déclaration par son ID

Récupère les détails d'une déclaration spécifique à partir de son identifiant unique.

- **URL**: `/api/declarations/{id}`
- **Méthode**: `GET`
- **Rôles autorisés**: `TAXATEUR`, `RECEVEUR_DES_IMPOTS`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `CONTRIBUABLE`, `ADMIN`
- **Paramètres**:
  - `id` (path): UUID de la déclaration à récupérer

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "declaration": {
      "id": "uuid-string",
      "dateDeclaration": "2025-01-15T10:30:45.123Z",
      "montantDeclare": 1000000.00,
      "exercice": "2025",
      "statut": "SOUMISE",
      "typeImpot": "IF",
      "description": "Déclaration d'impôt foncier",
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
    "code": "DECLARATION_NOT_FOUND",
    "message": "Déclaration non trouvée",
    "details": "Aucune déclaration avec cet ID n'existe"
  }
}
```

### 5. Récupérer les déclarations par statut

Récupère la liste des déclarations filtrées par statut avec pagination.

- **URL**: `/api/declarations/statut/{statut}`
- **Méthode**: `GET`
- **Rôles autorisés**: `TAXATEUR`, `RECEVEUR_DES_IMPOTS`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `ADMIN`
- **Paramètres**:
  - `statut` (path): Statut des déclarations à récupérer (SOUMISE, VALIDEE, REJETEE, TAXEE, PAYEE)
  - `page` (query, optionnel): Numéro de page (défaut: 0)
  - `size` (query, optionnel): Nombre d'éléments par page (défaut: 10)
  - `sortBy` (query, optionnel): Champ de tri (défaut: "date")
  - `sortDir` (query, optionnel): Direction du tri (asc/desc, défaut: "desc")

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "declarations": [
      {
        "id": "uuid-string",
        "dateDeclaration": "2025-01-15T10:30:45.123Z",
        "montantDeclare": 1000000.00,
        "exercice": "2025",
        "statut": "SOUMISE",
        "typeImpot": "IF",
        "description": "Déclaration d'impôt foncier",
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
    ],
    "currentPage": 0,
    "totalItems": 15,
    "totalPages": 2
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "DECLARATION_FETCH_ERROR",
    "message": "Erreur lors de la récupération des déclarations par statut",
    "details": "Message d'erreur détaillé"
  }
}
```

## Structure de l'entité Declaration

| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant unique de la déclaration |
| dateDeclaration | Date | Date de soumission de la déclaration |
| montantDeclare | Double | Montant déclaré par le contribuable |
| exercice | String | Année fiscale concernée par la déclaration |
| statut | Enum (StatutDeclaration) | Statut de la déclaration (SOUMISE, VALIDEE, REJETEE, TAXEE, PAYEE) |
| typeImpot | Enum (TypeImpot) | Type d'impôt concerné (IF, IRL, ICM, etc.) |
| description | String | Description de la déclaration |
| propriete | Propriete | Propriété concernée par la déclaration |
| contribuable | Contribuable | Contribuable ayant soumis la déclaration |
| agentValidateur | Agent | Agent ayant validé ou enregistré la déclaration |

## Structure de la requête DeclarationRequest

| Champ | Type | Description |
|-------|------|-------------|
| proprieteId | UUID | Identifiant de la propriété concernée |
| typeImpot | Enum (TypeImpot) | Type d'impôt concerné |
| montantDeclare | Double | Montant déclaré |
| exercice | String | Année fiscale |
| description | String | Description de la déclaration |
| location | Object | Coordonnées géographiques (obligatoire pour certains types d'impôts) |
| location.latitude | Double | Latitude de la propriété |
| location.longitude | Double | Longitude de la propriété |

## Règles métier

1. Les déclarations en ligne ne peuvent être soumises qu'entre le 2 janvier et le 1er février de chaque année.
2. La géolocalisation est obligatoire pour les types d'impôts suivants : IF, IRL, RL, ICM, IRV.
3. Les contribuables ne peuvent voir que leurs propres déclarations.
4. Seuls les agents avec les rôles appropriés peuvent enregistrer des déclarations manuelles.
5. Une déclaration passe par plusieurs statuts : SOUMISE → VALIDEE → TAXEE → PAYEE (ou REJETEE).
