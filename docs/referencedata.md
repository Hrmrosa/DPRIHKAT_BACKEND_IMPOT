# API des Données de Référence

## Données géographiques

### Liste des communes

`GET /api/ref/communes`

#### Description
Récupère la liste de toutes les communes disponibles.

#### Réponse
```json
{
  "success": true,
  "data": {
    "communes": [
      "string"
    ]
  },
  "error": null,
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

### Liste des quartiers d'une commune

`GET /api/ref/communes/{commune}/quartiers`

#### Description
Récupère la liste des quartiers d'une commune spécifique.

#### Paramètres
- `commune` (chemin) - Nom de la commune

#### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "commune": "string",
    "quartiers": [
      "string"
    ]
  },
  "error": null,
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

#### Réponse en cas d'erreur
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "COMMUNE_NOT_FOUND",
    "message": "Commune introuvable",
    "details": "Aucune commune: [nom_commune]"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

### Liste des avenues d'un quartier

`GET /api/ref/communes/{commune}/quartiers/{quartier}/avenues`

#### Description
Récupère la liste des avenues d'un quartier spécifique dans une commune.

#### Paramètres
- `commune` (chemin) - Nom de la commune
- `quartier` (chemin) - Nom du quartier

#### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "commune": "string",
    "quartier": "string",
    "avenues": [
      "string"
    ]
  },
  "error": null,
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

#### Réponse en cas d'erreur
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "QUARTIER_NOT_FOUND",
    "message": "Quartier introuvable",
    "details": "Aucun quartier: [nom_quartier] dans la commune: [nom_commune]"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Données des véhicules

### Liste des marques de voitures

`GET /api/ref/voitures/marques`

#### Description
Récupère la liste de toutes les marques de voitures disponibles.

#### Réponse
```json
{
  "success": true,
  "data": {
    "marques": [
      "string"
    ]
  },
  "error": null,
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

### Liste des modèles d'une marque

`GET /api/ref/voitures/marques/{marque}/models`

#### Description
Récupère la liste des modèles d'une marque de voiture spécifique.

#### Paramètres
- `marque` (chemin) - Nom de la marque

#### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "marque": "string",
    "models": [
      "string"
    ]
  },
  "error": null,
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

#### Réponse en cas d'erreur
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "MARQUE_NOT_FOUND",
    "message": "Marque introuvable",
    "details": "Aucune marque: [nom_marque]"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```
