# API de Gestion des Divisions

## Récupérer toutes les divisions

`GET /api/divisions`

### Description
Récupère la liste paginée de toutes les divisions.

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
    "divisions": [
      {
        "id": "UUID",
        "nom": "string",
        "code": "string",
        "bureaux": [
          {
            "id": "UUID",
            "nom": "string",
            "code": "string"
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
    "code": "DIVISIONS_FETCH_ERROR",
    "message": "Erreur lors de la récupération des divisions",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer une division par ID

`GET /api/divisions/{id}`

### Description
Récupère les détails d'une division spécifique par son ID.

### Rôles autorisés
- ADMIN
- DIRECTEUR
- INFORMATICIEN
- CHEF_DE_BUREAU
- CHEF_DE_DIVISION

### Paramètres
- `id` (chemin) - UUID de la division

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "division": {
      "id": "UUID",
      "nom": "string",
      "code": "string",
      "bureaux": [
        {
          "id": "UUID",
          "nom": "string",
          "code": "string"
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
    "code": "DIVISION_NOT_FOUND|DIVISION_FETCH_ERROR",
    "message": "string",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Créer une division

`POST /api/divisions`

### Description
Crée une nouvelle division.

### Rôles autorisés
- ADMIN
- DIRECTEUR
- INFORMATICIEN

### Corps de la requête
```json
{
  "nom": "string",
  "code": "string"
}
```

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "division": {
      "id": "UUID",
      "nom": "string",
      "code": "string",
      "bureaux": []
    },
    "message": "Division créée avec succès"
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
    "code": "DIVISION_CREATE_ERROR",
    "message": "Erreur lors de la création de la division",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Mettre à jour une division

`PUT /api/divisions/{id}`

### Description
Met à jour les informations d'une division existante.

### Rôles autorisés
- ADMIN
- DIRECTEUR
- INFORMATICIEN

### Paramètres
- `id` (chemin) - UUID de la division

### Corps de la requête
```json
{
  "nom": "string",
  "code": "string"
}
```

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "division": {
      "id": "UUID",
      "nom": "string",
      "code": "string",
      "bureaux": [
        {
          "id": "UUID",
          "nom": "string",
          "code": "string"
        }
      ]
    },
    "message": "Division mise à jour avec succès"
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
    "code": "DIVISION_UPDATE_ERROR",
    "message": "Erreur lors de la mise à jour de la division",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Supprimer une division

`DELETE /api/divisions/{id}`

### Description
Supprime une division existante.

### Rôles autorisés
- ADMIN
- DIRECTEUR
- INFORMATICIEN

### Paramètres
- `id` (chemin) - UUID de la division

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "message": "Division supprimée avec succès"
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
    "code": "DIVISION_DELETE_ERROR",
    "message": "Erreur lors de la suppression de la division",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```
