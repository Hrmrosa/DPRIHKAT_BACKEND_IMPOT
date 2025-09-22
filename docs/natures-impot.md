# Documentation API - Gestion des Natures d'Impôt

Cette documentation détaille les endpoints disponibles pour la gestion des natures d'impôt dans l'API DPRIHKAT.

## Vue d'ensemble

Les natures d'impôt représentent les différents types d'impôts qui peuvent être appliqués aux propriétés des contribuables. Elles sont utilisées notamment lors de la création d'une propriété pour spécifier quels impôts s'appliquent à cette propriété.

## Base URL

```
/api/natures-impot
```

## Endpoints

### 1. Récupérer toutes les natures d'impôt

Récupère la liste de toutes les natures d'impôt actives dans le système.

- **URL**: `/api/natures-impot`
- **Méthode**: `GET`
- **Rôles autorisés**: Tous les rôles authentifiés
- **Paramètres**: Aucun

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "naturesImpot": [
      {
        "id": "uuid-string-1",
        "code": "IF",
        "nom": "Impôt Foncier",
        "description": "Impôt sur les propriétés immobilières",
        "actif": true
      },
      {
        "id": "uuid-string-2",
        "code": "IRL",
        "nom": "Impôt sur les Revenus Locatifs",
        "description": "Impôt sur les revenus générés par la location de biens immobiliers",
        "actif": true
      },
      {
        "id": "uuid-string-3",
        "code": "RL",
        "nom": "Redevance Locative",
        "description": "Redevance sur les biens loués",
        "actif": true
      }
    ]
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-14T12:30:00.000Z"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "NATURES_IMPOT_FETCH_ERROR",
    "message": "Erreur lors de la récupération des natures d'impôt",
    "details": "Message d'erreur détaillé"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-14T12:30:00.000Z"
  }
}
```

### 2. Récupérer une nature d'impôt par ID

Récupère une nature d'impôt spécifique par son identifiant unique.

- **URL**: `/api/natures-impot/{id}`
- **Méthode**: `GET`
- **Rôles autorisés**: Tous les rôles authentifiés
- **Paramètres**:
  - `id` (path): UUID de la nature d'impôt à récupérer

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "natureImpot": {
      "id": "uuid-string-1",
      "code": "IF",
      "nom": "Impôt Foncier",
      "description": "Impôt sur les propriétés immobilières",
      "actif": true
    }
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-14T12:30:00.000Z"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "NATURE_IMPOT_NOT_FOUND",
    "message": "Nature d'impôt non trouvée",
    "details": "Aucune nature d'impôt trouvée avec l'ID fourni"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-14T12:30:00.000Z"
  }
}
```

### 3. Récupérer une nature d'impôt par code

Récupère une nature d'impôt spécifique par son code.

- **URL**: `/api/natures-impot/code/{code}`
- **Méthode**: `GET`
- **Rôles autorisés**: Tous les rôles authentifiés
- **Paramètres**:
  - `code` (path): Code de la nature d'impôt à récupérer (ex: "IF", "IRL")

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "natureImpot": {
      "id": "uuid-string-1",
      "code": "IF",
      "nom": "Impôt Foncier",
      "description": "Impôt sur les propriétés immobilières",
      "actif": true
    }
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-14T12:30:00.000Z"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "NATURE_IMPOT_NOT_FOUND",
    "message": "Nature d'impôt non trouvée",
    "details": "Aucune nature d'impôt trouvée avec le code fourni"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-14T12:30:00.000Z"
  }
}
```

## Utilisation avec la création de propriété

Lors de la création d'une propriété, vous devez spécifier les natures d'impôt qui s'appliquent à cette propriété. Pour cela :

1. Récupérez d'abord la liste des natures d'impôt disponibles via l'endpoint `/api/natures-impot`
2. Sélectionnez les natures d'impôt appropriées pour la propriété
3. Incluez les IDs des natures d'impôt sélectionnées dans le champ `naturesImpotIds` lors de la création de la propriété

Exemple de création d'une propriété avec des natures d'impôt :

```json
POST /api/proprietes
{
  "type": "VI",
  "localite": "Lubumbashi",
  "rangLocalite": 1,
  "superficie": 500.0,
  "adresse": "Avenue 123, Quartier Centre-ville",
  "latitude": -11.6642,
  "longitude": 27.4661,
  "proprietaireId": "uuid-string-du-contribuable",
  "naturesImpotIds": [
    "uuid-string-1",  // ID de l'Impôt Foncier (IF)
    "uuid-string-2"   // ID de l'Impôt sur les Revenus Locatifs (IRL)
  ]
}
```

Cette approche permet d'associer directement les impôts appropriés à une propriété lors de sa création, simplifiant ainsi le processus de gestion fiscale.
