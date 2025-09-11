# API de Gestion des Vignettes

## Générer une vignette

`POST /api/vignettes/generate/{vehiculeId}`

### Description
Génère une nouvelle vignette pour un véhicule. Un document PDF ou image doit être joint lors de la génération.
Les documents sont stockés dans le répertoire `/pieces` sur le serveur.

### Rôles autorisés
- AGENT_VIGNETTE
- ADMIN

### Paramètres
- `vehiculeId` (chemin) - UUID du véhicule
- `dateExpirationMillis` (requête) - Timestamp de la date d'expiration en millisecondes
- `montant` (requête, optionnel) - Montant de la vignette
- `puissance` (requête, optionnel) - Puissance du véhicule
- `document` (requête, obligatoire) - Document associé à la vignette

### Réponse
```json
{
  "success": true,
  "data": {
    "vignette": {
      "id": "UUID",
      "numero": "string",
      "dateEmission": "ISO8601",
      "dateExpiration": "ISO8601",
      "montant": 0,
      "statut": "string",
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
    "message": "Vignette générée avec succès"
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
    "code": "VIGNETTE_GENERATION_ERROR|INVALID_USER",
    "message": "string",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer une vignette par ID

`GET /api/vignettes/{id}`

### Description
Récupère les détails d'une vignette spécifique par son ID.
Les documents sont stockés dans le répertoire `/pieces` sur le serveur.

### Rôles autorisés
- AGENT_VIGNETTE
- TAXATEUR
- RECEVEUR_DES_IMPOTS
- CHEF_DE_BUREAU
- CHEF_DE_DIVISION
- DIRECTEUR
- CONTRIBUABLE
- ADMIN

### Paramètres
- `id` (chemin) - UUID de la vignette

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "vignette": {
      "id": "UUID",
      "numero": "string",
      "dateEmission": "ISO8601",
      "dateExpiration": "ISO8601",
      "montant": 0,
      "statut": "string",
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
    "code": "VIGNETTE_FETCH_ERROR",
    "message": "Erreur lors de la récupération de la vignette",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer les vignettes d'un véhicule

`GET /api/vignettes/vehicule/{vehiculeId}`

### Description
Récupère la liste des vignettes associées à un véhicule.
Les documents sont stockés dans le répertoire `/pieces` sur le serveur.

### Rôles autorisés
- AGENT_VIGNETTE
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
    "vignettes": [
      {
        "id": "UUID",
        "numero": "string",
        "dateEmission": "ISO8601",
        "dateExpiration": "ISO8601",
        "montant": 0,
        "statut": "string",
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
    "code": "VIGNETTE_FETCH_ERROR",
    "message": "Erreur lors de la récupération des vignettes",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer les vignettes actives

`GET /api/vignettes/active`

### Description
Récupère la liste paginée des vignettes actives.
Les documents sont stockés dans le répertoire `/pieces` sur le serveur.

### Rôles autorisés
- AGENT_VIGNETTE
- TAXATEUR
- RECEVEUR_DES_IMPOTS
- CHEF_DE_BUREAU
- CHEF_DE_DIVISION
- DIRECTEUR
- ADMIN

### Paramètres
- `page` (requête, optionnel, défaut: 0) - Numéro de page
- `size` (requête, optionnel, défaut: 10) - Nombre d'éléments par page

### Réponse
```json
{
  "success": true,
  "data": {
    "vignettes": [
      {
        "id": "UUID",
        "numero": "string",
        "dateEmission": "ISO8601",
        "dateExpiration": "ISO8601",
        "montant": 0,
        "statut": "string",
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
    "code": "VIGNETTE_FETCH_ERROR",
    "message": "Erreur lors de la récupération des vignettes",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer les vignettes expirées

`GET /api/vignettes/expired`

### Description
Récupère la liste paginée des vignettes expirées.
Les documents sont stockés dans le répertoire `/pieces` sur le serveur.

### Rôles autorisés
- AGENT_VIGNETTE
- TAXATEUR
- RECEVEUR_DES_IMPOTS
- CHEF_DE_BUREAU
- CHEF_DE_DIVISION
- DIRECTEUR
- ADMIN

### Paramètres
- `page` (requête, optionnel, défaut: 0) - Numéro de page
- `size` (requête, optionnel, défaut: 10) - Nombre d'éléments par page

### Réponse
```json
{
  "success": true,
  "data": {
    "vignettes": [
      {
        "id": "UUID",
        "numero": "string",
        "dateEmission": "ISO8601",
        "dateExpiration": "ISO8601",
        "montant": 0,
        "statut": "string",
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
    "code": "VIGNETTE_FETCH_ERROR",
    "message": "Erreur lors de la récupération des vignettes",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```
