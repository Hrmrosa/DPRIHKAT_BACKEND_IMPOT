# API de Gestion des Concessions Minières

## Récupérer toutes les concessions minières

`GET /api/concessions`

### Description
Récupère la liste de toutes les concessions minières.

### Rôles autorisés
- TAXATEUR
- RECEVEUR_DES_IMPOTS
- CHEF_DE_BUREAU
- CHEF_DE_DIVISION
- DIRECTEUR
- ADMIN
- INFORMATICIEN

### Réponse
```json
{
  "success": true,
  "data": {
    "concessions": [
      {
        "id": "UUID",
        "nom": "string",
        "description": "string",
        "superficie": 0,
        "localisation": "string",
        "dateDebut": "ISO8601",
        "dateFin": "ISO8601",
        "statut": "string",
        "titulaire": {
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
    "code": "CONCESSIONS_FETCH_ERROR",
    "message": "Erreur lors de la récupération des concessions",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer une concession minière par ID

`GET /api/concessions/{id}`

### Description
Récupère les détails d'une concession minière spécifique par son ID.

### Rôles autorisés
- TAXATEUR
- RECEVEUR_DES_IMPOTS
- CHEF_DE_BUREAU
- CHEF_DE_DIVISION
- DIRECTEUR
- ADMIN
- INFORMATICIEN

### Paramètres
- `id` (chemin) - UUID de la concession minière

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "concession": {
      "id": "UUID",
      "nom": "string",
      "description": "string",
      "superficie": 0,
      "localisation": "string",
      "dateDebut": "ISO8601",
      "dateFin": "ISO8601",
      "statut": "string",
      "titulaire": {
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
    "code": "CONCESSION_NOT_FOUND|CONCESSION_FETCH_ERROR",
    "message": "string",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer mes concessions minières

`GET /api/concessions/mine`

### Description
Récupère la liste des concessions minières appartenant à l'utilisateur connecté.

### Rôles autorisés
- CONTRIBUABLE
- ADMIN

### Réponse
```json
{
  "success": true,
  "data": {
    "concessions": [
      {
        "id": "UUID",
        "nom": "string",
        "description": "string",
        "superficie": 0,
        "localisation": "string",
        "dateDebut": "ISO8601",
        "dateFin": "ISO8601",
        "statut": "string",
        "titulaire": {
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
    "code": "INVALID_USER|CONCESSIONS_MINE_ERROR",
    "message": "string",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```
