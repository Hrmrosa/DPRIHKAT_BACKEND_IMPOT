# API de Gestion des Poursuites

## Récupérer toutes les poursuites

`GET /api/poursuites`

### Description
Récupère la liste de toutes les poursuites.

### Rôles autorisés
- ADMIN
- DIRECTEUR
- INFORMATICIEN
- CONTROLLEUR

### Réponse
```json
{
  "success": true,
  "data": {
    "poursuites": [
      {
        "id": "UUID",
        "datePoursuite": "ISO8601",
        "motif": "string",
        "statut": "string",
        "declaration": {
          "id": "UUID"
        },
        "agent": {
          "id": "UUID",
          "nom": "string"
        }
      }
    ]
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
    "code": "POURSUITES_FETCH_ERROR",
    "message": "Erreur lors de la récupération des poursuites",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer une poursuite par ID

`GET /api/poursuites/{id}`

### Description
Récupère les détails d'une poursuite spécifique par son ID.

### Rôles autorisés
- ADMIN
- DIRECTEUR
- INFORMATICIEN
- CONTROLLEUR
- CHEF_DE_BUREAU

### Paramètres
- `id` (chemin) - UUID de la poursuite

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "poursuite": {
      "id": "UUID",
      "datePoursuite": "ISO8601",
      "motif": "string",
      "statut": "string",
      "declaration": {
        "id": "UUID"
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
    "code": "POURSUITE_NOT_FOUND|POURSUITE_FETCH_ERROR",
    "message": "string",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Créer une poursuite

`POST /api/poursuites`

### Description
Crée une nouvelle poursuite.

### Rôles autorisés
- ADMIN
- DIRECTEUR
- INFORMATICIEN
- CONTROLLEUR

### Corps de la requête
```json
{
  "datePoursuite": "ISO8601",
  "motif": "string",
  "statut": "string",
  "declaration": {
    "id": "UUID"
  },
  "agent": {
    "id": "UUID"
  }
}
```

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "poursuite": {
      "id": "UUID",
      "datePoursuite": "ISO8601",
      "motif": "string",
      "statut": "string",
      "declaration": {
        "id": "UUID"
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
    "code": "POURSUITE_CREATE_ERROR",
    "message": "Erreur lors de la création de la poursuite",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Mettre à jour une poursuite

`PUT /api/poursuites/{id}`

### Description
Met à jour les informations d'une poursuite existante.

### Rôles autorisés
- ADMIN
- DIRECTEUR
- INFORMATICIEN
- CONTROLLEUR

### Paramètres
- `id` (chemin) - UUID de la poursuite

### Corps de la requête
```json
{
  "datePoursuite": "ISO8601",
  "motif": "string",
  "statut": "string",
  "declaration": {
    "id": "UUID"
  },
  "agent": {
    "id": "UUID"
  }
}
```

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "poursuite": {
      "id": "UUID",
      "datePoursuite": "ISO8601",
      "motif": "string",
      "statut": "string",
      "declaration": {
        "id": "UUID"
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
    "code": "POURSUITE_UPDATE_ERROR",
    "message": "Erreur lors de la mise à jour de la poursuite",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Supprimer une poursuite

`DELETE /api/poursuites/{id}`

### Description
Supprime une poursuite existante.

### Rôles autorisés
- ADMIN
- DIRECTEUR
- INFORMATICIEN

### Paramètres
- `id` (chemin) - UUID de la poursuite

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "message": "Poursuite supprimée avec succès"
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
    "code": "POURSUITE_DELETE_ERROR",
    "message": "Erreur lors de la suppression de la poursuite",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```
