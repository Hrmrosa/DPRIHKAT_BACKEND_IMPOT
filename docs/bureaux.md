# API de Gestion des Bureaux

## Récupérer tous les bureaux

`GET /api/bureaux`

### Description
Récupère la liste paginée de tous les bureaux.

### Rôles autorisés
- ADMIN
- DIRECTEUR
- INFORMATICIEN

### Paramètres de requête
- `page` (optionnel, défaut: 0) - Numéro de page
- `size` (optionnel, défaut: 10) - Nombre d'éléments par page
- `sortBy` (optionnel, défaut: "nom") - Champ de tri
- `sortDir` (optionnel, défaut: "asc") - Direction du tri ("asc" ou "desc")

### Réponse
```json
{
  "success": true,
  "data": {
    "bureaux": [
      {
        "id": "UUID",
        "nom": "string",
        "code": "string",
        "division": {
          "id": "UUID",
          "nom": "string",
          "code": "string"
        },
        "agents": [
          {
            "id": "UUID",
            "nom": "string"
          }
        ]
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
    "code": "BUREAUX_FETCH_ERROR",
    "message": "Erreur lors de la récupération des bureaux",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer un bureau par ID

`GET /api/bureaux/{id}`

### Description
Récupère les détails d'un bureau spécifique par son ID.

### Rôles autorisés
- ADMIN
- DIRECTEUR
- INFORMATICIEN
- CHEF_DE_BUREAU
- CHEF_DE_DIVISION

### Paramètres
- `id` (chemin) - UUID du bureau

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "bureau": {
      "id": "UUID",
      "nom": "string",
      "code": "string",
      "division": {
        "id": "UUID",
        "nom": "string",
        "code": "string"
      },
      "agents": [
        {
          "id": "UUID",
          "nom": "string"
        }
      ]
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
    "code": "BUREAU_NOT_FOUND|BUREAU_FETCH_ERROR",
    "message": "string",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Créer un bureau

`POST /api/bureaux`

### Description
Crée un nouveau bureau.

### Rôles autorisés
- ADMIN
- DIRECTEUR
- INFORMATICIEN

### Corps de la requête
```json
{
  "nom": "string",
  "code": "string",
  "divisionId": "UUID"
}
```

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "bureau": {
      "id": "UUID",
      "nom": "string",
      "code": "string",
      "division": {
        "id": "UUID",
        "nom": "string",
        "code": "string"
      },
      "agents": []
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
    "code": "DIVISION_NOT_FOUND|BUREAU_CREATE_ERROR",
    "message": "string",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Mettre à jour un bureau

`PUT /api/bureaux/{id}`

### Description
Met à jour les informations d'un bureau existant.

### Rôles autorisés
- ADMIN
- DIRECTEUR
- INFORMATICIEN

### Paramètres
- `id` (chemin) - UUID du bureau

### Corps de la requête
```json
{
  "nom": "string",
  "code": "string",
  "divisionId": "UUID"
}
```

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "bureau": {
      "id": "UUID",
      "nom": "string",
      "code": "string",
      "division": {
        "id": "UUID",
        "nom": "string",
        "code": "string"
      },
      "agents": [
        {
          "id": "UUID",
          "nom": "string"
        }
      ]
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
    "code": "BUREAU_NOT_FOUND|DIVISION_NOT_FOUND|BUREAU_UPDATE_ERROR",
    "message": "string",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Supprimer un bureau

`DELETE /api/bureaux/{id}`

### Description
Supprime un bureau existant.

### Rôles autorisés
- ADMIN
- DIRECTEUR
- INFORMATICIEN

### Paramètres
- `id` (chemin) - UUID du bureau

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "message": "Bureau supprimé avec succès"
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
    "code": "BUREAU_DELETE_ERROR",
    "message": "Erreur lors de la suppression du bureau",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```
