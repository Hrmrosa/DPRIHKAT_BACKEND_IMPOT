# Agents

## Récupérer tous les agents

**GET** `/api/agents`

Permet de récupérer la liste de tous les agents.

### Réponse

```json
[
  {
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
]
```

## Récupérer un agent par ID

**GET** `/api/agents/{id}`

Permet de récupérer un agent spécifique par son ID.

### Réponse

```json
{
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
```

## Créer un agent

**POST** `/api/agents`

Permet de créer un nouvel agent.

### Payload

```json
{
  "nom": "string",
  "sexe": "M | F",
  "matricule": "string",
  "bureauId": "UUID"
}
```

### Réponse

```json
{
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
```

## Mettre à jour un agent

**PUT** `/api/agents/{id}`

Permet de mettre à jour un agent existant.

### Payload

```json
{
  "nom": "string",
  "sexe": "M | F",
  "matricule": "string",
  "bureauId": "UUID"
}
```

### Réponse

```json
{
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
```

## Supprimer un agent

**DELETE** `/api/agents/{id}`

Permet de supprimer un agent.

### Réponse

```json
{
  "message": "Agent supprimé avec succès"
}
```
