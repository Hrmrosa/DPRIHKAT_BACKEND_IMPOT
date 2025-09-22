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

# API de Relances Détaillées

Cette section décrit les nouveaux endpoints permettant d'accéder aux relances avec des informations détaillées sur le contribuable, ses biens et les impôts assignés.

## Structure du DTO RelanceDetailDTO

Le DTO `RelanceDetailDTO` contient les informations suivantes :

- **Informations de base de la relance** : id, dateEnvoi, type, statut, contenu, codeQR
- **Informations du contribuable** : id, nom, adresses, téléphones, email, nationalité, type, identifiant
- **Informations sur le dossier de recouvrement** : id, totalDu, totalRecouvre, dateOuverture
- **Informations sur les biens du contribuable** : liste des biens avec leurs caractéristiques
- **Informations sur les impôts assignés aux biens** : liste des impôts avec leurs montants et échéances

## Récupérer une relance détaillée par ID

`GET /api/relances/details/{id}`

### Description
Récupère une relance détaillée par son ID avec toutes les informations associées sur le contribuable, ses biens et les impôts.

### Rôles autorisés
- ADMIN
- AGENT_RECOUVREMENT

### Paramètres
- `id` (chemin) - UUID de la relance

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "relanceDetail": {
      "id": "UUID",
      "dateEnvoi": "ISO8601",
      "type": "PREMIERE_RELANCE",
      "statut": "ENVOYEE",
      "contenu": "Contenu de la relance",
      "codeQR": "data:image/png;base64,...",
      "contribuableId": "UUID",
      "contribuableNom": "Nom du contribuable",
      "contribuableAdressePrincipale": "Adresse principale",
      "contribuableAdresseSecondaire": "Adresse secondaire",
      "contribuableTelephonePrincipal": "+243123456789",
      "contribuableTelephoneSecondaire": "+243987654321",
      "contribuableEmail": "email@example.com",
      "contribuableNationalite": "Congolaise",
      "contribuableType": "PERSONNE_PHYSIQUE",
      "contribuableIdentifiant": "CONT-12345",
      "dossierRecouvrementId": "UUID",
      "totalDu": 5000.0,
      "totalRecouvre": 1000.0,
      "dateOuverture": "ISO8601",
      "biens": [
        {
          "id": "UUID",
          "type": "IMMEUBLE",
          "localite": "Gombe",
          "rangLocalite": 1,
          "superficie": 500.0,
          "adresse": "Avenue de la Libération, Kinshasa",
          "montantImpot": 3000.0,
          "impots": [
            {
              "id": "UUID",
              "libelle": "Impôt foncier",
              "typeImpot": "IMPOT_FONCIER",
              "montant": 3000.0,
              "tauxImposition": 10.0,
              "exercice": "2025",
              "dateEcheance": "ISO8601",
              "taxationId": "UUID"
            }
          ]
        },
        {
          "id": "UUID",
          "type": "TERRAIN",
          "localite": "Limete",
          "rangLocalite": 2,
          "superficie": 1000.0,
          "adresse": "Avenue du Commerce, Kinshasa",
          "montantImpot": 2000.0,
          "impots": [
            {
              "id": "UUID",
              "libelle": "Impôt sur le revenu locatif",
              "typeImpot": "IMPOT_REVENU_LOCATIF",
              "montant": 2000.0,
              "tauxImposition": 15.0,
              "exercice": "2025",
              "dateEcheance": "ISO8601",
              "taxationId": "UUID"
            }
          ]
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
    "code": "RELANCE_DETAIL_ERROR",
    "message": "Erreur lors de la récupération de la relance détaillée",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer toutes les relances détaillées

`GET /api/relances/details`

### Description
Récupère la liste de toutes les relances détaillées avec les informations associées.

### Rôles autorisés
- ADMIN
- AGENT_RECOUVREMENT

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "relanceDetails": [
      {
        "id": "UUID",
        "dateEnvoi": "ISO8601",
        "type": "PREMIERE_RELANCE",
        "statut": "ENVOYEE",
        "contribuableNom": "Nom du contribuable",
        "contribuableAdressePrincipale": "Adresse principale",
        "totalDu": 5000.0,
        "totalRecouvre": 1000.0,
        "biens": [
          {
            "id": "UUID",
            "type": "IMMEUBLE",
            "adresse": "Avenue de la Libération, Kinshasa",
            "montantImpot": 3000.0,
            "impots": [
              {
                "libelle": "Impôt foncier",
                "montant": 3000.0,
                "exercice": "2025"
              }
            ]
          }
        ]
      }
    ],
    "count": 1
  },
  "error": null,
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer les relances détaillées d'un contribuable

`GET /api/relances/details/contribuable/{contribuableId}`

### Description
Récupère les relances détaillées d'un contribuable spécifique.

### Rôles autorisés
- ADMIN
- AGENT_RECOUVREMENT

### Paramètres
- `contribuableId` (chemin) - UUID du contribuable

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "relanceDetails": [
      {
        "id": "UUID",
        "dateEnvoi": "ISO8601",
        "type": "PREMIERE_RELANCE",
        "statut": "ENVOYEE",
        "contribuableNom": "Nom du contribuable",
        "contribuableAdressePrincipale": "Adresse principale",
        "totalDu": 5000.0,
        "totalRecouvre": 1000.0,
        "biens": [
          {
            "id": "UUID",
            "type": "IMMEUBLE",
            "adresse": "Avenue de la Libération, Kinshasa",
            "montantImpot": 3000.0,
            "impots": [
              {
                "libelle": "Impôt foncier",
                "montant": 3000.0,
                "exercice": "2025"
              }
            ]
          }
        ]
      }
    ],
    "count": 1,
    "contribuableId": "UUID"
  },
  "error": null,
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Utilisation des données détaillées des relances

Les données détaillées des relances peuvent être utilisées pour :

1. **Générer des documents de relance personnalisés** incluant toutes les informations sur les biens et impôts du contribuable
2. **Afficher un tableau de bord de recouvrement** avec les montants dus et recouvrés par contribuable
3. **Faciliter le suivi des échéances** des impôts par bien
4. **Améliorer le taux de recouvrement** en fournissant des informations précises aux contribuables

## Classes et interfaces associées

- `RelanceDetailDTO` : DTO principal contenant toutes les informations détaillées
- `RelanceDetailDTO.BienDTO` : Sous-classe pour les informations sur les biens
- `RelanceDetailDTO.ImpotDTO` : Sous-classe pour les informations sur les impôts
- `RelanceRepositoryCustom` : Interface définissant les méthodes personnalisées
- `RelanceRepositoryCustomImpl` : Implémentation des méthodes personnalisées
- `RelanceDetailService` : Service pour gérer les relances détaillées
- `RelanceDetailController` : Contrôleur exposant les endpoints REST
