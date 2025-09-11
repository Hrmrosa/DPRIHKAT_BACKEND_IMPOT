# API de Gestion du Recouvrement

## Récupérer tous les dossiers de recouvrement

`GET /api/dossiers-recouvrement`

### Description
Récupère la liste de tous les dossiers de recouvrement.

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
    "dossiers": [
      {
        "id": "UUID",
        "dateOuverture": "ISO8601",
        "dateCloture": "ISO8601",
        "statut": "string",
        "totalDu": 0,
        "totalRecouvre": 0,
        "codeQR": "string",
        "contribuable": {
          "id": "UUID",
          "nom": "string"
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
    "code": "DOSSIERS_FETCH_ERROR",
    "message": "Erreur lors de la récupération des dossiers de recouvrement",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer un dossier de recouvrement par ID

`GET /api/dossiers-recouvrement/{id}`

### Description
Récupère les détails d'un dossier de recouvrement spécifique par son ID.

### Rôles autorisés
- ADMIN
- DIRECTEUR
- INFORMATICIEN
- CONTROLLEUR
- CHEF_DE_BUREAU

### Paramètres
- `id` (chemin) - UUID du dossier de recouvrement

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "dossier": {
      "id": "UUID",
      "dateOuverture": "ISO8601",
      "dateCloture": "ISO8601",
      "statut": "string",
      "totalDu": 0,
      "totalRecouvre": 0,
      "codeQR": "string",
      "contribuable": {
        "id": "UUID",
        "nom": "string"
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
    "code": "DOSSIER_NOT_FOUND|DOSSIER_FETCH_ERROR",
    "message": "string",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Créer un dossier de recouvrement

`POST /api/dossiers-recouvrement`

### Description
Crée un nouveau dossier de recouvrement.

### Rôles autorisés
- ADMIN
- DIRECTEUR
- INFORMATICIEN
- CONTROLLEUR

### Corps de la requête
```json
{
  "dateOuverture": "ISO8601",
  "dateCloture": "ISO8601",
  "statut": "string",
  "totalDu": 0,
  "totalRecouvre": 0,
  "codeQR": "string",
  "contribuable": {
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
    "dossier": {
      "id": "UUID",
      "dateOuverture": "ISO8601",
      "dateCloture": "ISO8601",
      "statut": "string",
      "totalDu": 0,
      "totalRecouvre": 0,
      "codeQR": "string",
      "contribuable": {
        "id": "UUID",
        "nom": "string"
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
    "code": "DOSSIER_CREATE_ERROR",
    "message": "Erreur lors de la création du dossier",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Mettre à jour un dossier de recouvrement

`PUT /api/dossiers-recouvrement/{id}`

### Description
Met à jour les informations d'un dossier de recouvrement existant.

### Rôles autorisés
- ADMIN
- DIRECTEUR
- INFORMATICIEN
- CONTROLLEUR

### Paramètres
- `id` (chemin) - UUID du dossier de recouvrement

### Corps de la requête
```json
{
  "dateOuverture": "ISO8601",
  "dateCloture": "ISO8601",
  "statut": "string",
  "totalDu": 0,
  "totalRecouvre": 0,
  "codeQR": "string",
  "contribuable": {
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
    "dossier": {
      "id": "UUID",
      "dateOuverture": "ISO8601",
      "dateCloture": "ISO8601",
      "statut": "string",
      "totalDu": 0,
      "totalRecouvre": 0,
      "codeQR": "string",
      "contribuable": {
        "id": "UUID",
        "nom": "string"
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
    "code": "DOSSIER_UPDATE_ERROR",
    "message": "Erreur lors de la mise à jour du dossier",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Supprimer un dossier de recouvrement

`DELETE /api/dossiers-recouvrement/{id}`

### Description
Supprime un dossier de recouvrement existant.

### Rôles autorisés
- ADMIN
- DIRECTEUR
- INFORMATICIEN

### Paramètres
- `id` (chemin) - UUID du dossier de recouvrement

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "message": "Dossier supprimé avec succès"
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
    "code": "DOSSIER_DELETE_ERROR",
    "message": "Erreur lors de la suppression du dossier",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```
