# Documentation API - Gestion des Propriétés

Cette documentation détaille les endpoints disponibles pour la gestion des propriétés dans l'API DPRIHKAT.

## Vue d'ensemble

Les propriétés représentent les biens immobiliers ou fonciers appartenant aux contribuables et qui sont soumis à l'imposition. Elles peuvent être de différents types (terrain, bâtiment, concession minière, etc.) et sont géolocalisées pour faciliter leur identification.

## Base URL

```
/api/proprietes
```

## Endpoints

### 1. Créer une nouvelle propriété

Crée une nouvelle propriété avec les natures d'impôt associées.

- **URL**: `/api/proprietes`
- **Méthode**: `POST`
- **Rôles autorisés**: `TAXATEUR`, `ADMIN`, `DIRECTEUR`
- **Corps de la requête**:

```json
{
  "type": "VI",
  "localite": "Lubumbashi",
  "rangLocalite": 1,
  "superficie": 500.0,
  "adresse": "Avenue 123, Quartier Centre-ville",
  "latitude": -11.6642,
  "longitude": 27.4661,
  "proprietaireId": "uuid-string-du-contribuable",
  "naturesImpotIds": [
    "uuid-string-nature-impot-1",
    "uuid-string-nature-impot-2"
  ]
}
```

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "propriete": {
      "id": "uuid-string",
      "type": "VI",
      "localite": "Lubumbashi",
      "rangLocalite": 1,
      "superficie": 500.0,
      "adresse": "Avenue 123, Quartier Centre-ville",
      "actif": true,
      "declare": false,
      "declarationEnLigne": false,
      "naturesImpot": [
        {
          "id": "uuid-string-nature-impot-1",
          "code": "IF",
          "nom": "Impôt Foncier",
          "description": "Impôt sur les propriétés immobilières",
          "actif": true
        },
        {
          "id": "uuid-string-nature-impot-2",
          "code": "IRL",
          "nom": "Impôt sur les Revenus Locatifs",
          "description": "Impôt sur les revenus générés par la location de biens immobiliers",
          "actif": true
        }
      ],
      "proprietaire": {
        "id": "uuid-string-du-contribuable",
        "nom": "Nom du contribuable",
        "type": "PERSONNE_PHYSIQUE",
        "idNat": "IDNAT-123456",
        "numeroImpot": "A1234567B"
      }
    },
    "message": "Propriété créée avec succès"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-14T12:05:00.000Z"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "INVALID_DATA",
    "message": "Données invalides",
    "details": "Contribuable non trouvé avec ID: uuid-string-du-contribuable"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-14T12:05:00.000Z"
  }
}
```

### 2. Récupérer toutes les propriétés

Récupère la liste de toutes les propriétés enregistrées dans le système avec pagination simple.

- **URL**: `/api/proprietes`
- **Méthode**: `GET`
- **Rôles autorisés**: `TAXATEUR`, `RECEVEUR_DES_IMPOTS`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `ADMIN`, `INFORMATICIEN`
- **Paramètres**:
  - `page` (query, optionnel): Numéro de page (défaut: 0)
  - `size` (query, optionnel): Nombre d'éléments par page (défaut: 10)

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "data": {
      "totalItems": 42,
      "totalPages": 5,
      "currentPage": 0,
      "proprietes": [
        {
          "id": "uuid-string",
          "designation": "Résidence principale",
          "adresse": "Avenue 123, Quartier Centre-ville",
          "superficie": 500.0,
          "nombreNiveaux": 2,
          "nombrePieces": 6,
          "dateConstruction": "2010-01-01",
          "location": {
            "type": "Point",
            "coordinates": [-11.6642, 27.4661]
          },
          "proprietaire": {
            "id": "uuid-string",
            "nom": "Nom du contribuable"
          }
        }
      ]
    }
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "PROPRIETES_FETCH_ERROR",
    "message": "Erreur lors de la récupération des propriétés",
    "details": "Message d'erreur détaillé"
  }
}
```

### 3. Récupérer une propriété par son ID

Récupère les détails d'une propriété spécifique à partir de son identifiant unique.

- **URL**: `/api/proprietes/{id}`
- **Méthode**: `GET`
- **Rôles autorisés**: `TAXATEUR`, `RECEVEUR_DES_IMPOTS`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `ADMIN`, `INFORMATICIEN`
- **Paramètres**:
  - `id` (path): UUID de la propriété à récupérer

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "propriete": {
      "id": "uuid-string",
      "designation": "Résidence principale",
      "adresse": "Avenue 123, Quartier Centre-ville",
      "superficie": 500.0,
      "nombreNiveaux": 2,
      "nombrePieces": 6,
      "dateConstruction": "2010-01-01",
      "location": {
        "type": "Point",
        "coordinates": [-11.6642, 27.4661]
      },
      "proprietaire": {
        "id": "uuid-string",
        "nom": "Nom du contribuable"
      }
    }
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "PROPRIETE_NOT_FOUND",
    "message": "Propriété non trouvée",
    "details": "Aucune propriété avec l'ID fourni"
  }
}
```

### 4. Récupérer mes propriétés

Permet à un contribuable de récupérer la liste de ses propres propriétés.

- **URL**: `/api/proprietes/mine`
- **Méthode**: `GET`
- **Rôles autorisés**: `CONTRIBUABLE`
- **Paramètres**: Aucun

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "proprietes": [
      {
        "id": "uuid-string",
        "designation": "Résidence principale",
        "adresse": "Avenue 123, Quartier Centre-ville",
        "superficie": 500.0,
        "nombreNiveaux": 2,
        "nombrePieces": 6,
        "dateConstruction": "2010-01-01",
        "location": {
          "type": "Point",
          "coordinates": [-11.6642, 27.4661]
        }
      }
    ]
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "INVALID_USER",
    "message": "Utilisateur non valide",
    "details": "Seuls les contribuables peuvent voir leurs propriétés"
  }
}
```

### 5. Récupérer mes propriétés (paginées)

Permet à un contribuable de récupérer la liste de ses propres propriétés avec pagination et tri.

- **URL**: `/api/proprietes/mine/paginated`
- **Méthode**: `GET`
- **Rôles autorisés**: `CONTRIBUABLE`
- **Paramètres**:
  - `page` (query, optionnel): Numéro de page (défaut: 0)
  - `size` (query, optionnel): Nombre d'éléments par page (défaut: 10)
  - `sortBy` (query, optionnel): Champ de tri (défaut: "id")
  - `sortDir` (query, optionnel): Direction du tri (asc/desc, défaut: "asc")

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "data": {
      "totalItems": 3,
      "totalPages": 1,
      "currentPage": 0,
      "proprietes": [
        {
          "id": "uuid-string",
          "designation": "Résidence principale",
          "adresse": "Avenue 123, Quartier Centre-ville",
          "superficie": 500.0,
          "nombreNiveaux": 2,
          "nombrePieces": 6,
          "dateConstruction": "2010-01-01",
          "location": {
            "type": "Point",
            "coordinates": [-11.6642, 27.4661]
          }
        }
      ]
    }
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "INVALID_USER",
    "message": "Utilisateur non valide",
    "details": "Seuls les contribuables peuvent voir leurs propriétés"
  }
}
```

### 6. Récupérer les propriétés d'un contribuable (paginé)

Récupère la liste paginée des propriétés appartenant à un contribuable spécifique.

- **URL**: `/api/proprietes/contribuable/{contribuableId}`
- **Méthode**: `GET`
- **Rôles autorisés**: `TAXATEUR`, `RECEVEUR_DES_IMPOTS`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `ADMIN`, `INFORMATICIEN`, `CONTRIBUABLE`
- **Paramètres**:
  - `contribuableId` (path): UUID du contribuable
  - `page` (query, optionnel): Numéro de page (défaut: 0)
  - `size` (query, optionnel): Nombre d'éléments par page (défaut: 10)
  - `sortBy` (query, optionnel): Champ de tri (défaut: id)
  - `sortDir` (query, optionnel): Direction du tri (asc/desc, défaut: "asc")

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "data": {
      "totalItems": 5,
      "totalPages": 1,
      "currentPage": 0,
      "proprietes": [
        {
          "id": "uuid-string",
          "type": "VI",
          "localite": "Lubumbashi",
          "rangLocalite": 1,
          "superficie": 500.0,
          "adresse": "Avenue 123, Quartier Centre-ville",
          "actif": true,
          "declare": true,
          "declarationEnLigne": false,
          "proprietaire": {
            "id": "uuid-string",
            "nom": "Société Minière du Katanga",
            "type": "PERSONNE_MORALE",
            "rccm": "CD/LSH/RCCM/22-B-01234",
            "idNat": "AB123456",
            "numeroImpot": "A1234567B",
            "adressePrincipale": "Avenue Mobutu 123, Lubumbashi",
            "telephonePrincipal": "+243123456789",
            "email": "contact@smk.cd"
          },
          "montantImpot": 1500.0
        },
        {
          "id": "uuid-string-2",
          "type": "AP",
          "localite": "Lubumbashi",
          "rangLocalite": 2,
          "superficie": 120.0,
          "adresse": "Avenue Lumumba 45, Quartier Golf",
          "actif": true,
          "declare": true,
          "declarationEnLigne": true,
          "proprietaire": {
            "id": "uuid-string",
            "nom": "Société Minière du Katanga",
            "type": "PERSONNE_MORALE",
            "rccm": "CD/LSH/RCCM/22-B-01234",
            "idNat": "AB123456",
            "numeroImpot": "A1234567B",
            "adressePrincipale": "Avenue Mobutu 123, Lubumbashi",
            "telephonePrincipal": "+243123456789",
            "email": "contact@smk.cd"
          },
          "montantImpot": 800.0
        }
      ]
    }
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-13T18:10:00.000Z"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "PROPRIETES_FETCH_ERROR",
    "message": "Erreur lors de la récupération des propriétés du contribuable",
    "details": "Détails de l'erreur"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-13T18:10:00.000Z"
  }
}
```

### 7. Récupérer toutes les propriétés d'un contribuable (sans pagination)

Récupère la liste complète des propriétés appartenant à un contribuable spécifique sans pagination.

- **URL**: `/api/proprietes/contribuable/{contribuableId}/all`
- **Méthode**: `GET`
- **Rôles autorisés**: `TAXATEUR`, `RECEVEUR_DES_IMPOTS`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `ADMIN`, `INFORMATICIEN`, `CONTRIBUABLE`
- **Paramètres**:
  - `contribuableId` (path): UUID du contribuable

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "proprietes": [
      {
        "id": "uuid-string",
        "type": "VI",
        "localite": "Lubumbashi",
        "rangLocalite": 1,
        "superficie": 500.0,
        "adresse": "Avenue 123, Quartier Centre-ville",
        "actif": true,
        "declare": true,
        "declarationEnLigne": false,
        "proprietaire": {
          "id": "uuid-string",
          "nom": "Société Minière du Katanga",
          "type": "PERSONNE_MORALE",
          "rccm": "CD/LSH/RCCM/22-B-01234",
          "idNat": "AB123456",
          "numeroImpot": "A1234567B",
          "adressePrincipale": "Avenue Mobutu 123, Lubumbashi",
          "telephonePrincipal": "+243123456789",
          "email": "contact@smk.cd"
        },
        "montantImpot": 1500.0
      },
      {
        "id": "uuid-string-2",
        "type": "AP",
        "localite": "Lubumbashi",
        "rangLocalite": 2,
        "superficie": 120.0,
        "adresse": "Avenue Lumumba 45, Quartier Golf",
        "actif": true,
        "declare": true,
        "declarationEnLigne": true,
        "proprietaire": {
          "id": "uuid-string",
          "nom": "Société Minière du Katanga",
          "type": "PERSONNE_MORALE",
          "rccm": "CD/LSH/RCCM/22-B-01234",
          "idNat": "AB123456",
          "numeroImpot": "A1234567B",
          "adressePrincipale": "Avenue Mobutu 123, Lubumbashi",
          "telephonePrincipal": "+243123456789",
          "email": "contact@smk.cd"
        },
        "montantImpot": 800.0
      }
    ]
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-13T18:10:00.000Z"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "PROPRIETES_FETCH_ERROR",
    "message": "Erreur lors de la récupération de toutes les propriétés du contribuable",
    "details": "Détails de l'erreur"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-13T18:10:00.000Z"
  }
}
```

### 8. Mettre à jour une propriété

Met à jour les informations d'une propriété existante.

- **URL**: `/api/proprietes/{id}`
- **Méthode**: `PUT`
- **Rôles autorisés**: `TAXATEUR`, `CHEF_DE_BUREAU`, `ADMIN`
- **Paramètres**:
  - `id` (path): UUID de la propriété à mettre à jour
- **Corps de la requête**:

```json
{
  "designation": "Résidence secondaire",
  "adresse": "Avenue 456, Quartier Golf",
  "superficie": 600.0,
  "nombreNiveaux": 3,
  "nombrePieces": 8,
  "latitude": -11.6580,
  "longitude": 27.4790
}
```

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "propriete": {
      "id": "uuid-string",
      "designation": "Résidence secondaire",
      "adresse": "Avenue 456, Quartier Golf",
      "superficie": 600.0,
      "nombreNiveaux": 3,
      "nombrePieces": 8,
      "dateConstruction": "2010-01-01",
      "location": {
        "type": "Point",
        "coordinates": [-11.6580, 27.4790]
      },
      "proprietaire": {
        "id": "uuid-string",
        "nom": "Nom du contribuable"
      }
    }
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "PROPRIETE_UPDATE_ERROR",
    "message": "Erreur lors de la mise à jour de la propriété",
    "details": "Message d'erreur détaillé"
  }
}
```

### 9. Supprimer une propriété

Supprime une propriété du système.

- **URL**: `/api/proprietes/{id}`
- **Méthode**: `DELETE`
- **Rôles autorisés**: `CHEF_DE_BUREAU`, `ADMIN`
- **Paramètres**:
  - `id` (path): UUID de la propriété à supprimer

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "message": "Propriété supprimée avec succès"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "PROPRIETE_DELETE_ERROR",
    "message": "Erreur lors de la suppression de la propriété",
    "details": "Message d'erreur détaillé"
  }
}
```

### 10. Récupérer les propriétés avec types d'impôts

Récupère les propriétés du contribuable connecté avec les types d'impôts applicables pour chacune.

- **URL**: `/api/proprietes/mine/with-tax-types`
- **Méthode**: `GET`
- **Rôles autorisés**: `CONTRIBUABLE`
- **Paramètres**: Aucun

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "proprietes": [
      {
        "id": "uuid-string",
        "designation": "Résidence principale",
        "adresse": "Avenue 123, Quartier Centre-ville",
        "superficie": 500.0,
        "nombreNiveaux": 2,
        "nombrePieces": 6,
        "dateConstruction": "2010-01-01",
        "location": {
          "type": "Point",
          "coordinates": [-11.6642, 27.4661]
        },
        "typesImpot": [
          {
            "id": "uuid-string-type-impot-1",
            "code": "IF",
            "nom": "Impôt Foncier",
            "description": "Impôt sur les propriétés immobilières",
            "actif": true
          },
          {
            "id": "uuid-string-type-impot-2",
            "code": "IRL",
            "nom": "Impôt sur les Revenus Locatifs",
            "description": "Impôt sur les revenus générés par la location de biens immobiliers",
            "actif": true
          }
        ]
      }
    ]
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "INVALID_USER",
    "message": "Utilisateur non valide",
    "details": "Seuls les contribuables peuvent voir leurs propriétés"
  }
}
```

## Structure de l'entité Propriete

| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant unique de la propriété |
| designation | String | Nom ou désignation de la propriété |
| adresse | String | Adresse physique de la propriété |
| superficie | Double | Superficie de la propriété en mètres carrés |
| nombreNiveaux | Integer | Nombre d'étages (pour les bâtiments) |
| nombrePieces | Integer | Nombre de pièces (pour les bâtiments) |
| dateConstruction | Date | Date de construction (pour les bâtiments) |
| location | Point | Coordonnées géographiques de la propriété (latitude, longitude) |
| proprietaire | Contribuable | Contribuable propriétaire du bien |

## Règles métier

1. Chaque propriété doit être associée à un contribuable.
2. Les coordonnées géographiques sont obligatoires pour certains types d'impôts (IF, IRL, ICM).
3. Les contribuables ne peuvent voir que leurs propres propriétés.
4. Seuls les utilisateurs avec les rôles appropriés peuvent créer, modifier ou supprimer des propriétés.
5. Une propriété peut faire l'objet de plusieurs déclarations et taxations pour différents types d'impôts.
