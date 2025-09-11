# API de Collecte

## Création de contribuable avec biens

`POST /api/collecte/contribuables`

### Description
Crée un nouveau contribuable avec ses biens associés.

### Rôles autorisés
- CONTROLLEUR
- ADMIN
- INFORMATICIEN

### Corps de la requête
```json
{
  "nom": "string",
  "adressePrincipale": "string",
  "adresseSecondaire": "string",
  "telephonePrincipal": "string",
  "telephoneSecondaire": "string",
  "email": "string",
  "nationalite": "string",
  "type": "string",
  "idNat": "string",
  "nrc": "string",
  "sigle": "string",
  "numeroIdentificationContribuable": "string",
  "biens": [
    {
      "type": "string",
      "localite": "string",
      "rangLocalite": 0,
      "superficie": 0,
      "adresse": "string",
      "latitude": 0,
      "longitude": 0
    }
  ]
}
```

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "contribuable": {
      "id": "UUID",
      "nom": "string",
      "adressePrincipale": "string",
      "adresseSecondaire": "string",
      "telephonePrincipal": "string",
      "telephoneSecondaire": "string",
      "email": "string",
      "nationalite": "string",
      "type": "string",
      "idNat": "string",
      "nrc": "string",
      "sigle": "string",
      "numeroIdentificationContribuable": "string"
    },
    "proprietes": [
      {
        "id": "UUID",
        "type": "string",
        "localite": "string",
        "rangLocalite": 0,
        "superficie": 0,
        "adresse": "string",
        "latitude": 0,
        "longitude": 0,
        "proprietaire": {
          "id": "UUID",
          "nom": "string",
          "adressePrincipale": "string",
          "adresseSecondaire": "string",
          "telephonePrincipal": "string",
          "telephoneSecondaire": "string",
          "email": "string",
          "nationalite": "string",
          "type": "string",
          "idNat": "string",
          "nrc": "string",
          "sigle": "string",
          "numeroIdentificationContribuable": "string"
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
    "code": "INVALID_REQUEST|NO_PROPERTIES|INVALID_TYPE_CONTRIBUABLE|INVALID_TYPE_PROPRIETE|COLLECTE_CREATE_ERROR",
    "message": "string",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}

## Récupération de toutes les collectes

`GET /api/collecte/collectes`

### Description
Récupère la liste paginée de toutes les collectes (propriétés).

### Rôles autorisés
- CONTROLLEUR
- ADMIN
- INFORMATICIEN

### Paramètres de requête
- `page` (optionnel, défaut: 0) - Numéro de page
- `size` (optionnel, défaut: 10) - Nombre d'éléments par page

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "totalItems": 0,
    "totalPages": 0,
    "currentPage": 0,
    "proprietes": [
      {
        "id": "UUID",
        "type": "string",
        "localite": "string",
        "rangLocalite": 0,
        "superficie": 0,
        "adresse": "string",
        "latitude": 0,
        "longitude": 0,
        "proprietaire": {
          "id": "UUID",
          "nom": "string",
          "adressePrincipale": "string",
          "adresseSecondaire": "string",
          "telephonePrincipal": "string",
          "telephoneSecondaire": "string",
          "email": "string",
          "nationalite": "string",
          "type": "string",
          "idNat": "string",
          "nrc": "string",
          "sigle": "string",
          "numeroIdentificationContribuable": "string"
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
    "code": "COLLECTE_FETCH_ERROR",
    "message": "Erreur lors de la récupération des collectes",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}

## Valeurs possibles

### TypeContribuable
- PERSONNE_PHYSIQUE
- PERSONNE_MORALE

### TypePropriete
- AP
- VI
- AT
- CITERNE
- DEPOT
- CH
- TE
