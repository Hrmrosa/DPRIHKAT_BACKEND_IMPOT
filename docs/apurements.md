# Documentation API - Gestion des Apurements

Cette documentation détaille les endpoints disponibles pour la gestion des apurements dans l'API DPRIHKAT.

## Vue d'ensemble

Les apurements représentent les procédures administratives permettant de régler une situation fiscale sans paiement intégral immédiat. Ils sont associés à des taxations et permettent de gérer différentes modalités de règlement comme les remises gracieuses, les échelonnements de paiement ou les transactions.

Chaque apurement possède :
- Un identifiant unique (UUID)
- Une date de demande
- Une date de validation (si applicable)
- Un type (REMISE_GRACIEUSE, ECHELONNEMENT, TRANSACTION)
- Un montant apuré
- Un motif de demande
- Un motif de rejet (si applicable)
- Un statut (ACCEPTEE, REJETEE, PROVISOIRE, DEFINITIF)
- Un indicateur provisoire/définitif
- Un indicateur d'activité

## Base URL

```
/api/apurements
```

## Endpoints

### 1. Créer un apurement

Crée un nouvel apurement pour une déclaration spécifique.

- **URL**: `/api/apurements/create/{declarationId}`
- **Méthode**: `POST`
- **Rôles autorisés**: `APUREUR`, `RECEVEUR_DES_IMPOTS`, `ADMIN`
- **Paramètres**: 
  - `declarationId` (path parameter): UUID de la déclaration
  - `type` (query parameter): Type d'apurement (REMISE_GRACIEUSE, ECHELONNEMENT, TRANSACTION)

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "apurement": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "dateDemande": "2025-09-12T12:30:45.123Z",
      "dateValidation": null,
      "type": "REMISE_GRACIEUSE",
      "montantApure": 1500.0,
      "motif": "Difficultés financières du contribuable",
      "motifRejet": null,
      "statut": "PROVISOIRE",
      "provisoire": true,
      "actif": true,
      "declarationPayee": false,
      "agent": {
        "id": "550e8400-e29b-41d4-a716-446655440001",
        "nom": "Kabila",
        "postnom": "Jean",
        "prenom": "Pierre"
      },
      "declaration": {
        "id": "550e8400-e29b-41d4-a716-446655440002",
        "dateDeclaration": "2025-09-10T10:30:45.123Z",
        "statut": "VALIDEE"
      }
    },
    "message": "Apurement créé avec succès"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-12T12:30:45.123Z"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "APUREMENT_CREATION_ERROR",
    "message": "Erreur lors de la création de l'apurement",
    "details": "La déclaration spécifiée n'existe pas"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-12T12:30:45.123Z"
  }
}
```

### 2. Valider un apurement

Valide un apurement existant.

- **URL**: `/api/apurements/validate/{apurementId}`
- **Méthode**: `POST`
- **Rôles autorisés**: `APUREUR`, `RECEVEUR_DES_IMPOTS`, `ADMIN`
- **Paramètres**: 
  - `apurementId` (path parameter): UUID de l'apurement à valider

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "apurement": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "dateDemande": "2025-09-12T12:30:45.123Z",
      "dateValidation": "2025-09-12T14:45:30.789Z",
      "type": "REMISE_GRACIEUSE",
      "montantApure": 1500.0,
      "motif": "Difficultés financières du contribuable",
      "motifRejet": null,
      "statut": "ACCEPTEE",
      "provisoire": false,
      "actif": true,
      "declarationPayee": true,
      "agent": {
        "id": "550e8400-e29b-41d4-a716-446655440001",
        "nom": "Kabila",
        "postnom": "Jean",
        "prenom": "Pierre"
      },
      "declaration": {
        "id": "550e8400-e29b-41d4-a716-446655440002",
        "dateDeclaration": "2025-09-10T10:30:45.123Z",
        "statut": "VALIDEE"
      }
    },
    "message": "Apurement validé avec succès"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-12T14:45:30.789Z"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "APUREMENT_VALIDATION_ERROR",
    "message": "Erreur lors de la validation de l'apurement",
    "details": "L'apurement spécifié n'existe pas"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-12T14:45:30.789Z"
  }
}
```

### 3. Récupérer un apurement par déclaration

Récupère l'apurement associé à une déclaration spécifique.

- **URL**: `/api/apurements/declaration/{declarationId}`
- **Méthode**: `GET`
- **Rôles autorisés**: `APUREUR`, `RECEVEUR_DES_IMPOTS`, `TAXATEUR`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `ADMIN`
- **Paramètres**: 
  - `declarationId` (path parameter): UUID de la déclaration

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "apurement": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "dateDemande": "2025-09-12T12:30:45.123Z",
      "dateValidation": "2025-09-12T14:45:30.789Z",
      "type": "REMISE_GRACIEUSE",
      "montantApure": 1500.0,
      "motif": "Difficultés financières du contribuable",
      "motifRejet": null,
      "statut": "ACCEPTEE",
      "provisoire": false,
      "actif": true,
      "declarationPayee": true,
      "agent": {
        "id": "550e8400-e29b-41d4-a716-446655440001",
        "nom": "Kabila",
        "postnom": "Jean",
        "prenom": "Pierre"
      },
      "declaration": {
        "id": "550e8400-e29b-41d4-a716-446655440002",
        "dateDeclaration": "2025-09-10T10:30:45.123Z",
        "statut": "VALIDEE"
      }
    }
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-12T15:00:00.000Z"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "APUREMENT_NOT_FOUND",
    "message": "Apurement non trouvé",
    "details": "Aucun apurement trouvé pour cette déclaration"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-12T15:00:00.000Z"
  }
}
```

### 4. Récupérer tous les apurements

Récupère la liste de tous les apurements, avec possibilité de filtrer par statut ou type.

- **URL**: `/api/apurements`
- **Méthode**: `GET`
- **Rôles autorisés**: `APUREUR`, `RECEVEUR_DES_IMPOTS`, `TAXATEUR`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `ADMIN`
- **Paramètres**: 
  - `page` (optionnel): Numéro de page (commence à 0)
  - `size` (optionnel): Nombre d'éléments par page (par défaut 10)
  - `statut` (optionnel): Filtre par statut (ACCEPTEE, REJETEE, PROVISOIRE, DEFINITIF)
  - `type` (optionnel): Filtre par type (REMISE_GRACIEUSE, ECHELONNEMENT, TRANSACTION)

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "apurements": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "dateDemande": "2025-09-12T12:30:45.123Z",
        "dateValidation": "2025-09-12T14:45:30.789Z",
        "type": "REMISE_GRACIEUSE",
        "montantApure": 1500.0,
        "motif": "Difficultés financières du contribuable",
        "motifRejet": null,
        "statut": "ACCEPTEE",
        "provisoire": false,
        "actif": true,
        "declarationPayee": true,
        "agent": {
          "id": "550e8400-e29b-41d4-a716-446655440001",
          "nom": "Kabila",
          "postnom": "Jean",
          "prenom": "Pierre"
        },
        "declaration": {
          "id": "550e8400-e29b-41d4-a716-446655440002",
          "dateDeclaration": "2025-09-10T10:30:45.123Z",
          "statut": "VALIDEE"
        }
      }
    ],
    "currentPage": 0,
    "totalItems": 1,
    "totalPages": 1
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-12T15:15:00.000Z"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "APUREMENT_FETCH_ERROR",
    "message": "Erreur lors de la récupération des apurements",
    "details": "Erreur de connexion à la base de données"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-12T15:15:00.000Z"
  }
}
```

## Structure des données

### Apurement

| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant unique de l'apurement |
| dateDemande | Date | Date de la demande d'apurement |
| dateValidation | Date | Date de validation de l'apurement (null si non validé) |
| type | TypeApurement | Type d'apurement (REMISE_GRACIEUSE, ECHELONNEMENT, TRANSACTION) |
| montantApure | Double | Montant apuré |
| motif | String | Motif de la demande d'apurement |
| motifRejet | String | Motif de rejet (null si non rejeté) |
| statut | StatutApurement | Statut de l'apurement (ACCEPTEE, REJETEE, PROVISOIRE, DEFINITIF) |
| provisoire | Boolean | Indique si l'apurement est provisoire ou définitif |
| actif | Boolean | Indique si l'apurement est actif |
| declarationPayee | Boolean | Indique si la déclaration associée a été payée |
| agent | Agent | Agent qui a créé l'apurement |
| declaration | Declaration | Déclaration associée à l'apurement |

### Agent

| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant unique de l'agent |
| nom | String | Nom de l'agent |
| postnom | String | Postnom de l'agent |
| prenom | String | Prénom de l'agent |

### Declaration

| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant unique de la déclaration |
| dateDeclaration | Date | Date de la déclaration |
| statut | String | Statut de la déclaration |
