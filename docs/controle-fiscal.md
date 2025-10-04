# API de Contrôle Fiscal

## Récupérer les contribuables insolvables

`GET /api/controle-fiscal/insolvables`

### Description
Récupère la liste paginée des contribuables avec au moins 2 déclarations impayées depuis plus de 3 mois.

### Rôles autorisés
- ADMIN
- DIRECTEUR
- INFORMATICIEN
- CONTROLLEUR

### Paramètres
- `page` (optionnel, défaut=0) - Numéro de page (0-based)
- `size` (optionnel, défaut=10) - Taille de la page

### Réponse
```json
{
  "success": true,
  "data": {
    "insolvables": [
      {
        "id": "UUID",
        "nomComplet": "string",
        "nombreDeclarationsImpayees": 2,
        "totalDette": 0,
        "dateDerniereDeclaration": "ISO8601",
        "contribuable": {
          "id": "UUID",
          "nom": "string",
          "adressePrincipale": "string",
          "telephone": "string",
          "email": "string"
        },
        "biens": [
          {
            "id": "UUID",
            "type": "IMMEUBLE/VEHICULE",
            "adresse": "string",
            "valeur": 0,
            "declarations": [
              {
                "id": "UUID",
                "date": "ISO8601",
                "montant": 0,
                "impot": {
                  "type": "string",
                  "taux": 0
                },
                "paiement": {
                  "date": "ISO8601",
                  "montant": 0,
                  "mode": "string"
                },
                "penalites": [
                  {
                    "montant": 0,
                    "motif": "string",
                    "date": "ISO8601"
                  }
                ]
              }
            ]
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

### Exemple
```bash
curl -X GET "http://localhost:8080/api/controle-fiscal/insolvables?page=0&size=5"
```

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
    "totalItems": 48,
    "totalPages": 5,
    "currentPage": 0,
    "anomalies": [
      {
        "id": "94ed98dc-f74b-43ac-940c-dbb5d4e78367",
        "dateDeclaration": "2025-06-30T07:42:47.355+00:00",
        "statut": "VALIDEE",
        "contribuableDetails": {
          "id": "6a73b298-94bc-46bd-b887-e33dd7de07ba",
          "nomComplet": "Mutombo Jean",
          "adresse": "123 Avenue de la République",
          "telephone": "+243810000000",
          "email": "jean.mutombo@example.com",
          "typeContribuable": "PARTICULIER"
        },
        "proprieteDetails": {
          "id": "45f00dfb-f6c5-4663-8a01-51c7ffc29f1d",
          "adresse": "456 Boulevard du 30 Juin",
          "superficie": 150,
          "valeurLocative": 5000,
          "typePropriete": "APPARTEMENT"
        },
        "taxations": [
          {
            "id": "50cfca51-2a8e-41b1-865a-45569141d08e",
            "montant": 86.61,
            "typeImpot": "IF"
          }
        ]
      }
    ]
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
