# API de Contrôle Fiscal

## Récupérer les anomalies

`GET /api/controle-fiscal/anomalies`

### Description
Récupère la liste des déclarations avec anomalies pour le contrôle fiscal.

### Rôles autorisés
- CONTROLLEUR
- CHEF_DE_BUREAU
- CHEF_DE_DIVISION
- DIRECTEUR
- ADMIN

### Paramètres de requête
- `page` (optionnel, défaut: 0) - Numéro de page
- `size` (optionnel, défaut: 10) - Nombre d'éléments par page

### Réponse
```json
{
  "success": true,
  "data": {
    "anomalies": [
      {
        "id": "UUID",
        "dateDeclaration": "ISO8601",
        "montant": 0,
        "statut": "string",
        "typeImpot": "string",
        "exercice": "string",
        "contribuable": {
          "id": "UUID",
          "nom": "string",
          "adressePrincipale": "string"
        },
        "propriete": {
          "id": "UUID",
          "adresse": "string"
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
    "code": "ANOMALY_FETCH_ERROR|INVALID_USER",
    "message": "string",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Générer un rapport fiscal

`GET /api/controle-fiscal/rapport`

### Description
Génère un rapport fiscal pour une période donnée.

### Rôles autorisés
- CONTROLLEUR
- CHEF_DE_BUREAU
- CHEF_DE_DIVISION
- DIRECTEUR
- ADMIN

### Paramètres de requête
- `startDate` (requis) - Date de début (format ISO8601)
- `endDate` (requis) - Date de fin (format ISO8601)

### Réponse
```json
{
  "success": true,
  "data": {
    "rapport": {
      "periode": {
        "debut": "ISO8601",
        "fin": "ISO8601"
      },
      "totalDeclarations": 0,
      "totalMontant": 0,
      "totalAnomalies": 0,
      "tauxConformite": 0,
      "detailsParType": [
        {
          "typeImpot": "string",
          "nombreDeclarations": 0,
          "montantTotal": 0,
          "nombreAnomalies": 0
        }
      ]
    },
    "message": "Rapport fiscal généré avec succès"
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
    "code": "REPORT_GENERATION_ERROR|INVALID_USER",
    "message": "string",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer les top contributeurs

`GET /api/controle-fiscal/top-contributors`

### Description
Récupère la liste des contributeurs qui ont le plus déclaré.

### Rôles autorisés
- CONTROLLEUR
- CHEF_DE_BUREAU
- CHEF_DE_DIVISION
- DIRECTEUR
- ADMIN

### Paramètres de requête
- `limit` (optionnel, défaut: 10) - Nombre de contributeurs à récupérer

### Réponse
```json
{
  "success": true,
  "data": {
    "topContributors": [
      {
        "contribuable": {
          "id": "UUID",
          "nom": "string"
        },
        "totalDeclarations": 0,
        "montantTotal": 0
      }
    ],
    "message": "Top contributeurs récupérés avec succès"
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
    "code": "TOP_CONTRIBUTORS_ERROR|INVALID_USER",
    "message": "string",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer les contributeurs délinquants

`GET /api/controle-fiscal/delinquents`

### Description
Récupère la liste des contributeurs délinquants (déclarations en retard).

### Rôles autorisés
- CONTROLLEUR
- CHEF_DE_BUREAU
- CHEF_DE_DIVISION
- DIRECTEUR
- ADMIN

### Réponse
```json
{
  "success": true,
  "data": {
    "delinquents": [
      {
        "contribuable": {
          "id": "UUID",
          "nom": "string",
          "telephonePrincipal": "string"
        },
        "nombreDeclarationsRetard": 0,
        "montantImpaye": 0
      }
    ],
    "message": "Contributeurs délinquants récupérés avec succès"
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
    "code": "DELINQUENTS_ERROR|INVALID_USER",
    "message": "string",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer les statistiques du tableau de bord

`GET /api/controle-fiscal/dashboard`

### Description
Récupère les statistiques pour le tableau de bord du contrôle fiscal.

### Rôles autorisés
- CONTROLLEUR
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
    "dashboard": {
      "totalDeclarations": 0,
      "declarationsTraitees": 0,
      "declarationsEnAttente": 0,
      "anomaliesDetectees": 0,
      "tauxTraitement": 0,
      "evolutionMensuelle": [
        {
          "mois": "string",
          "nombreDeclarations": 0
        }
      ]
    },
    "message": "Statistiques du tableau de bord récupérées avec succès"
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
    "code": "DASHBOARD_ERROR|INVALID_USER",
    "message": "string",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```
