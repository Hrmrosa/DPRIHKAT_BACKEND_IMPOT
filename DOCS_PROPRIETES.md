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

## Processus de Collecte

### Workflow complet
1. Création du contribuable
2. Ajout des propriétés
3. Validation par le contrôleur

### Exemple complet
```json
{
  "nom": "Dupont",
  "adressePrincipale": "123 Rue Principale",
  "telephonePrincipal": "+123456789",
  "email": "dupont@example.com",
  "type": "PARTICULIER",
  "biens": [
    {
      "adresse": "123 Rue Principale",
      "surface": 150,
      "latitude": 12.345678,
      "longitude": -12.345678
    }
  ]
}
```

### Statuts possibles
- `BROUILLON`
- `VALIDEE`
- `REJETEE`

### Erreurs spécifiques
- `COLLECTE_INVALID_ADDRESS` : Adresse géolocalisable manquante
- `COLLECTE_MISSING_PROPERTIES` : Aucun bien fourni

## CollecteController

### Endpoints

1. **POST /api/collecte/contribuables**
   - Crée un nouveau contribuable avec ses biens
   - Nécessite le rôle CONTROLLEUR, ADMIN ou INFORMATICIEN
   - Payload : Voir section précédente

2. **GET /api/collecte/collectes**
   - Liste paginée des collectes
   - Paramètres : page, size
   - Réponse paginée standard

### Workflow de validation
1. Vérification des données requises
2. Création du contribuable
3. Enregistrement des propriétés
4. Géolocalisation des adresses

### Exceptions gérées
- `INVALID_REQUEST` : Requête vide
- `NO_PROPERTIES` : Aucun bien fourni
- `INVALID_COORDINATES` : Coordonnées GPS invalides

### Exemple complet
```java
// Exemple d'implémentation côté client
const response = await fetch('/api/collecte/contribuables', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer token'
  },
  body: JSON.stringify(payload)
});
```

## Endpoint POST /api/collecte/contribuables

Crée un nouveau contribuable avec ses biens

### Payload
```json
{
  "nom": "...",
  "adressePrincipale": "...",
  "telephonePrincipal": "...",
  "email": "...",
  "type": "...",
  "biens": [
    {
      "adresse": "...",
      "surface": 0,
      "latitude": 0,
      "longitude": 0
    }
  ]
}
```

### Réponse
```json
{
  "data": {
    "contribuable": {
      "id": "...",
      "nom": "..."
      // autres champs
    },
    "proprietes": [
      {
        "id": "...",
        "adresse": "..."
        // autres champs
      }
    ]
  },
  "success": true,
  "error": null
}
```

### Erreurs
- 400 : Données invalides
- 403 : Non autorisé
