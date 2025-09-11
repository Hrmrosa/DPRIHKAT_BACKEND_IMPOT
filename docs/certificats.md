# API de Gestion des Certificats

## Générer un certificat de propriété

`POST /api/certificats/property/{declarationId}`

### Description
Génère un certificat de propriété pour une déclaration donnée.

### Rôles autorisés
- AGENT_CERTIFICAT
- ADMIN

### Paramètres
- `declarationId` (chemin) - UUID de la déclaration

### Réponse
```json
{
  "success": true,
  "data": {
    "certificat": {
      "id": "UUID",
      "numero": "string",
      "dateEmission": "ISO8601",
      "dateExpiration": "ISO8601",
      "type": "string",
      "statut": "string",
      "codeQR": "string",
      "declaration": {
        "id": "UUID",
        "dateDeclaration": "ISO8601",
        "montant": 0
      },
      "propriete": {
        "id": "UUID",
        "adresse": "string"
      },
      "agent": {
        "id": "UUID",
        "nom": "string"
      }
    },
    "message": "Certificat généré avec succès"
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
    "code": "CERTIFICATE_GENERATION_ERROR|INVALID_USER",
    "message": "string",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Générer un certificat de véhicule

`POST /api/certificats/vehicle/{vehiculeId}`

### Description
Génère un certificat pour un véhicule donné.

### Rôles autorisés
- AGENT_CERTIFICAT
- ADMIN

### Paramètres
- `vehiculeId` (chemin) - UUID du véhicule

### Réponse
```json
{
  "success": true,
  "data": {
    "certificat": {
      "id": "UUID",
      "numero": "string",
      "dateEmission": "ISO8601",
      "dateExpiration": "ISO8601",
      "type": "string",
      "statut": "string",
      "codeQR": "string",
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
    "message": "Certificat généré avec succès"
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
    "code": "CERTIFICATE_GENERATION_ERROR|INVALID_USER",
    "message": "string",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer un certificat par ID

`GET /api/certificats/{id}`

### Description
Récupère les détails d'un certificat spécifique par son ID.

### Rôles autorisés
- AGENT_CERTIFICAT
- TAXATEUR
- RECEVEUR_DES_IMPOTS
- CHEF_DE_BUREAU
- CHEF_DE_DIVISION
- DIRECTEUR
- CONTRIBUABLE
- ADMIN

### Paramètres
- `id` (chemin) - UUID du certificat

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "certificat": {
      "id": "UUID",
      "numero": "string",
      "dateEmission": "ISO8601",
      "dateExpiration": "ISO8601",
      "type": "string",
      "statut": "string",
      "codeQR": "string",
      "declaration": {
        "id": "UUID",
        "dateDeclaration": "ISO8601",
        "montant": 0
      },
      "propriete": {
        "id": "UUID",
        "adresse": "string"
      },
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
    "code": "CERTIFICATE_FETCH_ERROR",
    "message": "Erreur lors de la récupération du certificat",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer les certificats d'une déclaration

`GET /api/certificats/declaration/{declarationId}`

### Description
Récupère la liste des certificats associés à une déclaration.

### Rôles autorisés
- AGENT_CERTIFICAT
- TAXATEUR
- RECEVEUR_DES_IMPOTS
- CHEF_DE_BUREAU
- CHEF_DE_DIVISION
- DIRECTEUR
- CONTRIBUABLE
- ADMIN

### Paramètres
- `declarationId` (chemin) - UUID de la déclaration
- `page` (requête, optionnel, défaut: 0) - Numéro de page
- `size` (requête, optionnel, défaut: 10) - Nombre d'éléments par page

### Réponse
```json
{
  "success": true,
  "data": {
    "certificats": [
      {
        "id": "UUID",
        "numero": "string",
        "dateEmission": "ISO8601",
        "dateExpiration": "ISO8601",
        "type": "string",
        "statut": "string",
        "codeQR": "string",
        "declaration": {
          "id": "UUID",
          "dateDeclaration": "ISO8601",
          "montant": 0
        },
        "propriete": {
          "id": "UUID",
          "adresse": "string"
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
    "code": "CERTIFICATE_FETCH_ERROR",
    "message": "Erreur lors de la récupération des certificats",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer les certificats d'un véhicule

`GET /api/certificats/vehicle/{vehiculeId}`

### Description
Récupère la liste des certificats associés à un véhicule.

### Rôles autorisés
- AGENT_CERTIFICAT
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
    "certificats": [
      {
        "id": "UUID",
        "numero": "string",
        "dateEmission": "ISO8601",
        "dateExpiration": "ISO8601",
        "type": "string",
        "statut": "string",
        "codeQR": "string",
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
    "code": "CERTIFICATE_FETCH_ERROR",
    "message": "Erreur lors de la récupération des certificats",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer les certificats actifs

`GET /api/certificats/active`

### Description
Récupère la liste des certificats actifs (non expirés).

### Rôles autorisés
- AGENT_CERTIFICAT
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
    "certificats": [
      {
        "id": "UUID",
        "numero": "string",
        "dateEmission": "ISO8601",
        "dateExpiration": "ISO8601",
        "type": "string",
        "statut": "string",
        "codeQR": "string",
        "declaration": {
          "id": "UUID",
          "dateDeclaration": "ISO8601",
          "montant": 0
        },
        "propriete": {
          "id": "UUID",
          "adresse": "string"
        },
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
    "code": "CERTIFICATE_FETCH_ERROR",
    "message": "Erreur lors de la récupération des certificats",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer les certificats expirés

`GET /api/certificats/expired`

### Description
Récupère la liste des certificats expirés.

### Rôles autorisés
- AGENT_CERTIFICAT
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
    "certificats": [
      {
        "id": "UUID",
        "numero": "string",
        "dateEmission": "ISO8601",
        "dateExpiration": "ISO8601",
        "type": "string",
        "statut": "string",
        "codeQR": "string",
        "declaration": {
          "id": "UUID",
          "dateDeclaration": "ISO8601",
          "montant": 0
        },
        "propriete": {
          "id": "UUID",
          "adresse": "string"
        },
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
    "code": "CERTIFICATE_FETCH_ERROR",
    "message": "Erreur lors de la récupération des certificats",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```
