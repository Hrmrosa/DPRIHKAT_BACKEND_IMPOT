# API de Gestion du Recouvrement

## Structure complète d'un dossier de recouvrement
```json
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "dateOuverture": "2025-09-29T00:00:00Z",
  "dateCloture": "2025-12-15T00:00:00Z",
  "statut": "EN_COURS",
  "totalDette": 1500.0,
  "totalRecouvre": 500.0,
  "codeQR": "QR-3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "contribuable": {
    "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "nom": "Dupont",
    "prenom": "Jean",
    "numeroIdentification": "ID123456",
    "adresse": "123 Rue Example",
    "telephone": "+123456789"
  },
  "declarations": [
    {
      "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "dateDeclaration": "2025-06-15T00:00:00Z",
      "montant": 750.0,
      "impot": {
        "type": "TAXE_FONCIERE",
        "taux": 0.15
      },
      "penalites": [
        {
          "montant": 150.0,
          "motif": "RETARD"
        }
      ]
    }
  ],
  "paiements": [
    {
      "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "date": "2025-09-20T00:00:00Z",
      "montant": 500.0,
      "mode": "VIREMENT"
    }
  ],
  "historique": [
    {
      "date": "2025-09-29T00:00:00Z",
      "action": "DOSSIER_OUVERT",
      "agent": "Agent Smith"
    }
  ]
}
```

## Endpoints

### Récupérer un dossier complet
`GET /api/dossiers-recouvrement/{id}`

**Réponse**:
- 200: Dossier complet avec tous les détails (structure ci-dessus)
- 404: Dossier non trouvé

### Rechercher des dossiers
`GET /api/dossiers-recouvrement/search`

**Paramètres**:
- `contribuableId` (optionnel)
- `statut` (optionnel)
- `dateDebut` (optionnel)
- `dateFin` (optionnel)

### Exporter un dossier
`GET /api/dossiers-recouvrement/{id}/export`

**Réponse**:
- 200: PDF du dossier

## Rôles requis
- ADMIN: Toutes opérations
- AGENT_RECOUVREMENT: Opérations spécialisées
- CONTROLLEUR: Consultation seulement

## Codes statut
- EN_COURS: Dossier actif
- CLOTURE: Dossier clos
- SUSPENDU: Dossier en attente

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
