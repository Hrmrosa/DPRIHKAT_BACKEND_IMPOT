# Documentation API - Gestion des Propriétés

Cette documentation détaille les endpoints disponibles pour la gestion des propriétés dans l'API DPRIHKAT.

## Vue d'ensemble

Les propriétés représentent les biens immobiliers ou fonciers appartenant aux contribuables et qui sont soumis à l'imposition. Elles peuvent être de différents types (terrain, bâtiment, concession minière, etc.) et sont géolocalisées pour faciliter leur identification.

## Base URL

```
/api/proprietes
```

## Endpoints

### 1. Récupérer toutes les propriétés

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

### 2. Récupérer toutes les propriétés (paginées avec tri)

Récupère la liste de toutes les propriétés avec pagination avancée et options de tri.

- **URL**: `/api/proprietes/paginated`
- **Méthode**: `GET`
- **Rôles autorisés**: `TAXATEUR`, `RECEVEUR_DES_IMPOTS`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `ADMIN`, `INFORMATICIEN`
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
    "message": "Erreur lors de la récupération des propriétés paginées",
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

### 6. Créer une nouvelle propriété

Crée une nouvelle propriété dans le système.

- **URL**: `/api/proprietes`
- **Méthode**: `POST`
- **Rôles autorisés**: `TAXATEUR`, `CHEF_DE_BUREAU`, `ADMIN`
- **Corps de la requête**:

```json
{
  "designation": "Résidence principale",
  "adresse": "Avenue 123, Quartier Centre-ville",
  "superficie": 500.0,
  "nombreNiveaux": 2,
  "nombrePieces": 6,
  "dateConstruction": "2010-01-01",
  "latitude": -11.6642,
  "longitude": 27.4661,
  "proprietaireId": "uuid-string"
}
```

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
    "code": "PROPRIETE_CREATE_ERROR",
    "message": "Erreur lors de la création de la propriété",
    "details": "Message d'erreur détaillé"
  }
}
```

### 7. Mettre à jour une propriété

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

### 8. Supprimer une propriété

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
