# Documentation API - Gestion des Taux de Change

Cette documentation détaille les endpoints disponibles pour la gestion des taux de change dans l'API DPRIHKAT.

## Vue d'ensemble

Le système DPRIHKAT permet de gérer les taux de change entre les devises USD (Dollar américain) et CDF (Franc congolais). Ces taux sont utilisés pour convertir les montants des taxations entre les différentes devises.

Par défaut, toutes les taxations sont enregistrées en USD, mais peuvent être converties en CDF selon le taux de change en vigueur.

## Base URL

```
/api/taux-change
```

## Endpoints

### 1. Récupérer tous les taux de change actifs

Récupère la liste de tous les taux de change actifs dans le système.

- **URL**: `/api/taux-change`
- **Méthode**: `GET`
- **Rôles autorisés**: `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `ADMIN`, `DIRECTEUR`
- **Paramètres**: Aucun

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "tauxChanges": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "dateEffective": "2025-09-10T05:36:47.378+00:00",
        "taux": 2500.0,
        "deviseSource": "USD",
        "deviseDestination": "CDF",
        "actif": true,
        "agent": {
          "id": "uuid-string",
          "nom": "Nom de l'agent",
          "prenom": "Prénom de l'agent",
          "fonction": "DIRECTEUR"
        }
      }
    ]
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-12T07:55:47.410243835"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "TAUX_CHANGE_FETCH_ERROR",
    "message": "Erreur lors de la récupération des taux de change",
    "details": "Message d'erreur détaillé"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-12T07:55:47.410243835"
  }
}
```

### 2. Récupérer le taux de change actuel

Récupère le taux de change actif le plus récent pour une paire de devises.

- **URL**: `/api/taux-change/actuel`
- **Méthode**: `GET`
- **Rôles autorisés**: `TAXATEUR`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `ADMIN`, `DIRECTEUR`
- **Paramètres**: 
  - `deviseSource` (query parameter): Devise source (USD ou CDF)
  - `deviseDestination` (query parameter): Devise destination (USD ou CDF)

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "tauxChange": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "dateEffective": "2025-09-10T05:36:47.378+00:00",
      "taux": 2500.0,
      "deviseSource": "USD",
      "deviseDestination": "CDF",
      "actif": true,
      "agent": {
        "id": "uuid-string",
        "nom": "Nom de l'agent",
        "prenom": "Prénom de l'agent",
        "fonction": "DIRECTEUR"
      }
    }
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-12T07:55:47.410243835"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "TAUX_CHANGE_NOT_FOUND",
    "message": "Aucun taux de change trouvé",
    "details": "Aucun taux de change actif trouvé pour la conversion de USD vers CDF"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-12T07:55:47.410243835"
  }
}
```

### 3. Créer un nouveau taux de change

Crée un nouveau taux de change entre deux devises.

- **URL**: `/api/taux-change`
- **Méthode**: `POST`
- **Rôles autorisés**: `CHEF_DE_DIVISION`, `ADMIN`, `DIRECTEUR`
- **Corps de la requête**:

```json
{
  "taux": 2500.0,
  "deviseSource": "USD",
  "deviseDestination": "CDF"
}
```

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "message": "Taux de change créé avec succès",
    "tauxChange": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "dateEffective": "2025-09-12T07:55:47.410243835",
      "taux": 2500.0,
      "deviseSource": "USD",
      "deviseDestination": "CDF",
      "actif": true,
      "agent": {
        "id": "uuid-string",
        "nom": "Nom de l'agent",
        "prenom": "Prénom de l'agent",
        "fonction": "DIRECTEUR"
      }
    }
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-12T07:55:47.410243835"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "TAUX_CHANGE_CREATION_ERROR",
    "message": "Erreur lors de la création du taux de change",
    "details": "Message d'erreur détaillé"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-12T07:55:47.410243835"
  }
}
```

### 4. Désactiver un taux de change

Désactive un taux de change existant.

- **URL**: `/api/taux-change/{id}`
- **Méthode**: `DELETE`
- **Rôles autorisés**: `CHEF_DE_DIVISION`, `ADMIN`, `DIRECTEUR`
- **Paramètres**: 
  - `id` (path parameter): UUID du taux de change

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "message": "Taux de change désactivé avec succès"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-12T07:55:47.410243835"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "TAUX_CHANGE_DEACTIVATION_ERROR",
    "message": "Erreur lors de la désactivation du taux de change",
    "details": "Message d'erreur détaillé"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-12T07:55:47.410243835"
  }
}
```

### 5. Convertir un montant

Convertit un montant d'une devise à une autre selon le taux de change actuel.

- **URL**: `/api/taux-change/convertir`
- **Méthode**: `GET`
- **Rôles autorisés**: `TAXATEUR`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `ADMIN`, `DIRECTEUR`, `CONTRIBUABLE`
- **Paramètres**: 
  - `montant` (query parameter): Montant à convertir
  - `deviseSource` (query parameter): Devise source (USD ou CDF)
  - `deviseDestination` (query parameter): Devise destination (USD ou CDF)

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "montantOriginal": 100.0,
    "deviseSource": "USD",
    "montantConverti": 250000.0,
    "deviseDestination": "CDF",
    "tauxApplique": 2500.0,
    "dateConversion": "2025-09-12T07:55:47.410243835"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-12T07:55:47.410243835"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "CONVERSION_ERROR",
    "message": "Erreur lors de la conversion du montant",
    "details": "Aucun taux de change actif trouvé pour la conversion de USD vers CDF"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-12T07:55:47.410243835"
  }
}
```

## Structure des données

### TauxChange

| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant unique du taux de change |
| dateEffective | Date | Date à laquelle le taux de change devient effectif |
| taux | Double | Valeur du taux de change |
| deviseSource | Enum | Devise source (USD ou CDF) |
| deviseDestination | Enum | Devise destination (USD ou CDF) |
| actif | Boolean | Indique si le taux de change est actif |
| agent | Object | Agent qui a défini ce taux de change |

### Agent

| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant unique de l'agent |
| nom | String | Nom de l'agent |
| prenom | String | Prénom de l'agent |
| fonction | Enum | Fonction de l'agent |
