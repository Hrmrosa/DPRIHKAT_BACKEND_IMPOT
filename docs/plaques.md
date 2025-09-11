# API de Gestion des Plaques

## Assigner une plaque à un véhicule

`POST /api/plaques/assign/{vehiculeId}`

### Description
Assigne une plaque disponible à un véhicule. Un document PDF ou image doit être joint lors de l'assignation.
Les documents sont stockés dans le répertoire `/pieces` sur le serveur.

### Rôles autorisés
- AGENT_DE_PLAQUES
- ADMIN

### Paramètres
- `vehiculeId` (chemin) - UUID du véhicule

### Réponse
```json
{
  "success": true,
  "data": {
    "plaque": {
      "id": "UUID",
      "numero": "string",
      "dateAssignation": "ISO8601",
      "statut": "string",
      "codeQR": "string",
      "document": "string",
      "vehicule": {
        "id": "UUID",
        "marque": "string",
        "modele": "string",
        "immatriculation": "string"
      },
      "agent": {
        "id": "UUID",
        "nom": "string"
      }
    },
    "message": "Plaque assignée avec succès"
  },
  "error": null,
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

### Réponse en cas d'erreur
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "PLAQUE_ASSIGNMENT_ERROR|INVALID_USER",
    "message": "string",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer une plaque par ID

`GET /api/plaques/{id}`

### Description
Récupère les détails d'une plaque spécifique par son ID.
Les documents sont stockés dans le répertoire `/pieces` sur le serveur.

### Rôles autorisés
- AGENT_DE_PLAQUES
- TAXATEUR
- RECEVEUR_DES_IMPOTS
- CHEF_DE_BUREAU
- CHEF_DE_DIVISION
- DIRECTEUR
- ADMIN

### Paramètres
- `id` (chemin) - UUID de la plaque

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "plaque": {
      "id": "UUID",
      "numero": "string",
      "dateAssignation": "ISO8601",
      "statut": "string",
      "codeQR": "string",
      "document": "string",
      "vehicule": {
        "id": "UUID",
        "marque": "string",
        "modele": "string",
        "immatriculation": "string"
      },
      "agent": {
        "id": "UUID",
        "nom": "string"
      }
    }
  },
  "error": null,
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

### Réponse en cas d'erreur
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "PLAQUE_FETCH_ERROR",
    "message": "Erreur lors de la récupération de la plaque",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer toutes les plaques

`GET /api/plaques`

### Description
Récupère la liste de toutes les plaques avec option de filtrage par disponibilité.
Les documents sont stockés dans le répertoire `/pieces` sur le serveur.

### Rôles autorisés
- AGENT_DE_PLAQUES
- TAXATEUR
- RECEVEUR_DES_IMPOTS
- CHEF_DE_BUREAU
- CHEF_DE_DIVISION
- DIRECTEUR
- ADMIN

### Paramètres de requête
- `page` (optionnel, défaut: 0) - Numéro de page
- `size` (optionnel, défaut: 10) - Nombre d'éléments par page
- `disponible` (optionnel) - Filtrer par disponibilité (true pour les plaques disponibles, false pour les plaques assignées)

### Réponse
```json
{
  "success": true,
  "data": {
    "plaques": [
      {
        "id": "UUID",
        "numero": "string",
        "dateAssignation": "ISO8601",
        "statut": "string",
        "codeQR": "string",
        "document": "string",
        "vehicule": {
          "id": "UUID",
          "marque": "string",
          "modele": "string",
          "immatriculation": "string"
        },
        "agent": {
          "id": "UUID",
          "nom": "string"
        }
      }
    ],
    "currentPage": 0,
    "totalItems": 0,
    "totalPages": 0
  },
  "error": null,
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

### Réponse en cas d'erreur
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "PLAQUE_FETCH_ERROR",
    "message": "Erreur lors de la récupération des plaques",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer les plaques d'un véhicule

`GET /api/plaques/vehicule/{vehiculeId}`

### Description
Récupère la liste des plaques associées à un véhicule.
Les documents sont stockés dans le répertoire `/pieces` sur le serveur.

### Rôles autorisés
- AGENT_DE_PLAQUES
- TAXATEUR
- RECEVEUR_DES_IMPOTS
- CHEF_DE_BUREAU
- CHEF_DE_DIVISION
- DIRECTEUR
- CONTRIBUABLE
- ADMIN

### Paramètres
- `vehiculeId` (chemin) - UUID du véhicule
- `page` (requête, optionnel, défaut: 0) - Numéro de page
- `size` (requête, optionnel, défaut: 10) - Nombre d'éléments par page

### Réponse
```json
{
  "success": true,
  "data": {
    "plaques": [
      {
        "id": "UUID",
        "numero": "string",
        "dateAssignation": "ISO8601",
        "statut": "string",
        "codeQR": "string",
        "document": "string",
        "vehicule": {
          "id": "UUID",
          "marque": "string",
          "modele": "string",
          "immatriculation": "string"
        },
        "agent": {
          "id": "UUID",
          "nom": "string"
        }
      }
    ],
    "currentPage": 0,
    "totalItems": 0,
    "totalPages": 0
  },
  "error": null,
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

### Réponse en cas d'erreur
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "PLAQUE_FETCH_ERROR",
    "message": "Erreur lors de la récupération des plaques",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Vérifier le stock de plaques

`GET /api/plaques/stock`

### Description
Vérifie la disponibilité des plaques en stock.
Les documents sont stockés dans le répertoire `/pieces` sur le serveur.

### Rôles autorisés
- AGENT_DE_PLAQUES
- CHEF_DE_BUREAU
- CHEF_DE_DIVISION
- DIRECTEUR
- ADMIN

### Réponse
```json
{
  "success": true,
  "data": {
    "available": true,
    "message": "Plaques disponibles en stock"
  },
  "error": null,
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

### Réponse en cas d'erreur
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "STOCK_CHECK_ERROR",
    "message": "Erreur lors de la vérification du stock",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```
