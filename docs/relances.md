# API de Gestion des Relances

## Récupérer toutes les relances

`GET /api/relances`

### Description
Récupère la liste de toutes les relances.

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
    "relances": [
      {
        "id": "UUID",
        "dateRelance": "ISO8601",
        "type": "string",
        "statut": "string",
        "declaration": {
          "id": "UUID"
        },
        "agent": {
          "id": "UUID",
          "nom": "string"
        },
        "codeQR": "string"
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
    "code": "RELANCES_FETCH_ERROR",
    "message": "Erreur lors de la récupération des relances",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer une relance par ID

`GET /api/relances/{id}`

### Description
Récupère les détails d'une relance spécifique par son ID.

### Rôles autorisés
- ADMIN
- DIRECTEUR
- INFORMATICIEN
- CONTROLLEUR
- CHEF_DE_BUREAU

### Paramètres
- `id` (chemin) - UUID de la relance

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "relance": {
      "id": "UUID",
      "dateRelance": "ISO8601",
      "type": "string",
      "statut": "string",
      "declaration": {
        "id": "UUID"
      },
      "agent": {
        "id": "UUID",
        "nom": "string"
      },
      "codeQR": "string"
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
    "code": "RELANCE_NOT_FOUND|RELANCE_FETCH_ERROR",
    "message": "string",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Créer une relance

`POST /api/relances`

### Description
Crée une nouvelle relance.

### Rôles autorisés
- ADMIN
- DIRECTEUR
- INFORMATICIEN
- CONTROLLEUR

### Corps de la requête
```json
{
  "dateRelance": "ISO8601",
  "type": "string",
  "statut": "string",
  "declaration": {
    "id": "UUID"
  },
  "agent": {
    "id": "UUID"
  },
  "codeQR": "string"
}
```

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "relance": {
      "id": "UUID",
      "dateRelance": "ISO8601",
      "type": "string",
      "statut": "string",
      "declaration": {
        "id": "UUID"
      },
      "agent": {
        "id": "UUID",
        "nom": "string"
      },
      "codeQR": "string"
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
    "code": "RELANCE_CREATE_ERROR",
    "message": "Erreur lors de la création de la relance",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Mettre à jour une relance

`PUT /api/relances/{id}`

### Description
Met à jour les informations d'une relance existante.

### Rôles autorisés
- ADMIN
- DIRECTEUR
- INFORMATICIEN
- CONTROLLEUR

### Paramètres
- `id` (chemin) - UUID de la relance

### Corps de la requête
```json
{
  "dateRelance": "ISO8601",
  "type": "string",
  "statut": "string",
  "declaration": {
    "id": "UUID"
  },
  "agent": {
    "id": "UUID"
  },
  "codeQR": "string"
}
```

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "relance": {
      "id": "UUID",
      "dateRelance": "ISO8601",
      "type": "string",
      "statut": "string",
      "declaration": {
        "id": "UUID"
      },
      "agent": {
        "id": "UUID",
        "nom": "string"
      },
      "codeQR": "string"
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
    "code": "RELANCE_UPDATE_ERROR",
    "message": "Erreur lors de la mise à jour de la relance",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Supprimer une relance

`DELETE /api/relances/{id}`

### Description
Supprime une relance existante.

### Rôles autorisés
- ADMIN
- DIRECTEUR
- INFORMATICIEN

### Paramètres
- `id` (chemin) - UUID de la relance

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "message": "Relance supprimée avec succès"
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
    "code": "RELANCE_DELETE_ERROR",
    "message": "Erreur lors de la suppression de la relance",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```
