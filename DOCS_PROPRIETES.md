# Propriétés

## Récupérer toutes les propriétés

**GET** `/api/proprietes`

Permet de récupérer la liste de toutes les propriétés.

### Réponse

```json
[
  {
    "id": "UUID",
    "type": "VI | AP | TE | MA | BI | CI | IN | CO | PA",
    "localite": "string",
    "rangLocalite": "integer",
    "superficie": "double",
    "adresse": "string",
    "montantImpot": "double",
    "location": {
      "type": "Point",
      "coordinates": [
        "longitude",
        "latitude"
      ]
    },
    "actif": "boolean",
    "declarationEnLigne": "boolean",
    "declare": "boolean",
    "proprietaire": {
      "id": "UUID",
      "nom": "string",
      "sexe": "M | F",
      "matricule": "string",
      "bureau": {
        "id": "UUID",
        "nom": "string",
        "code": "string",
        "division": {
          "id": "UUID",
          "nom": "string",
          "code": "string"
        }
      }
    }
  }
]
```

## Récupérer une propriété par ID

**GET** `/api/proprietes/{id}`

Permet de récupérer une propriété spécifique par son ID.

### Réponse

```json
{
  "id": "UUID",
  "type": "VI | AP | TE | MA | BI | CI | IN | CO | PA",
  "localite": "string",
  "rangLocalite": "integer",
  "superficie": "double",
  "adresse": "string",
  "montantImpot": "double",
  "location": {
    "type": "Point",
    "coordinates": [
      "longitude",
      "latitude"
    ]
  },
  "actif": "boolean",
  "declarationEnLigne": "boolean",
  "declare": "boolean",
  "proprietaire": {
    "id": "UUID",
    "nom": "string",
    "sexe": "M | F",
    "matricule": "string",
    "bureau": {
      "id": "UUID",
      "nom": "string",
      "code": "string",
      "division": {
        "id": "UUID",
        "nom": "string",
        "code": "string"
      }
    }
  }
}
```

## Créer une propriété

**POST** `/api/proprietes`

Permet de créer une nouvelle propriété.

### Payload

```json
{
  "type": "VI | AP | TE | MA | BI | CI | IN | CO | PA",
  "localite": "string",
  "rangLocalite": "integer",
  "superficie": "double",
  "adresse": "string",
  "montantImpot": "double",
  "longitude": "double",
  "latitude": "double",
  "proprietaireId": "UUID"
}
```

### Réponse

```json
{
  "id": "UUID",
  "type": "VI | AP | TE | MA | BI | CI | IN | CO | PA",
  "localite": "string",
  "rangLocalite": "integer",
  "superficie": "double",
  "adresse": "string",
  "montantImpot": "double",
  "location": {
    "type": "Point",
    "coordinates": [
      "longitude",
      "latitude"
    ]
  },
  "actif": "boolean",
  "declarationEnLigne": "boolean",
  "declare": "boolean",
  "proprietaire": {
    "id": "UUID",
    "nom": "string",
    "sexe": "M | F",
    "matricule": "string",
    "bureau": {
      "id": "UUID",
      "nom": "string",
      "code": "string",
      "division": {
        "id": "UUID",
        "nom": "string",
        "code": "string"
      }
    }
  }
}
```

## Mettre à jour une propriété

**PUT** `/api/proprietes/{id}`

Permet de mettre à jour une propriété existante.

### Payload

```json
{
  "type": "VI | AP | TE | MA | BI | CI | IN | CO | PA",
  "localite": "string",
  "rangLocalite": "integer",
  "superficie": "double",
  "adresse": "string",
  "montantImpot": "double",
  "longitude": "double",
  "latitude": "double",
  "proprietaireId": "UUID"
}
```

### Réponse

```json
{
  "id": "UUID",
  "type": "VI | AP | TE | MA | BI | CI | IN | CO | PA",
  "localite": "string",
  "rangLocalite": "integer",
  "superficie": "double",
  "adresse": "string",
  "montantImpot": "double",
  "location": {
    "type": "Point",
    "coordinates": [
      "longitude",
      "latitude"
    ]
  },
  "actif": "boolean",
  "declarationEnLigne": "boolean",
  "declare": "boolean",
  "proprietaire": {
    "id": "UUID",
    "nom": "string",
    "sexe": "M | F",
    "matricule": "string",
    "bureau": {
      "id": "UUID",
      "nom": "string",
      "code": "string",
      "division": {
        "id": "UUID",
        "nom": "string",
        "code": "string"
      }
    }
  }
}
```

## Supprimer une propriété

**DELETE** `/api/proprietes/{id}`

Permet de supprimer une propriété.

### Réponse

```json
{
  "message": "Propriété supprimée avec succès"
}
```
