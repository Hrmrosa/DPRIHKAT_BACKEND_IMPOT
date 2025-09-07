# Utilisateurs

## Récupérer tous les utilisateurs

**GET** `/api/utilisateurs`

Permet de récupérer la liste de tous les utilisateurs.

### Réponse

```json
[
  {
    "id": "UUID",
    "login": "string",
    "role": "ADMIN | TAXATEUR | RECEVEUR_DES_IMPOTS | CONTRIBUABLE",
    "premierConnexion": "boolean",
    "bloque": "boolean",
    "actif": "boolean",
    "agent": {
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

## Récupérer un utilisateur par ID

**GET** `/api/utilisateurs/{id}`

Permet de récupérer un utilisateur spécifique par son ID.

### Réponse

```json
{
  "id": "UUID",
  "login": "string",
  "role": "ADMIN | TAXATEUR | RECEVEUR_DES_IMPOTS | CONTRIBUABLE",
  "premierConnexion": "boolean",
  "bloque": "boolean",
  "actif": "boolean",
  "agent": {
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

## Créer un utilisateur

**POST** `/api/utilisateurs`

Permet de créer un nouvel utilisateur.

### Payload

```json
{
  "login": "string",
  "motDePasse": "string",
  "role": "ADMIN | TAXATEUR | RECEVEUR_DES_IMPOTS | CONTRIBUABLE",
  "agentId": "UUID"
}
```

### Réponse

```json
{
  "id": "UUID",
  "login": "string",
  "role": "ADMIN | TAXATEUR | RECEVEUR_DES_IMPOTS | CONTRIBUABLE",
  "premierConnexion": "boolean",
  "bloque": "boolean",
  "actif": "boolean",
  "agent": {
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

## Mettre à jour un utilisateur

**PUT** `/api/utilisateurs/{id}`

Permet de mettre à jour un utilisateur existant.

### Payload

```json
{
  "login": "string",
  "role": "ADMIN | TAXATEUR | RECEVEUR_DES_IMPOTS | CONTRIBUABLE",
  "premierConnexion": "boolean",
  "bloque": "boolean",
  "actif": "boolean",
  "agentId": "UUID"
}
```

### Réponse

```json
{
  "id": "UUID",
  "login": "string",
  "role": "ADMIN | TAXATEUR | RECEVEUR_DES_IMPOTS | CONTRIBUABLE",
  "premierConnexion": "boolean",
  "bloque": "boolean",
  "actif": "boolean",
  "agent": {
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

## Supprimer un utilisateur

**DELETE** `/api/utilisateurs/{id}`

Permet de supprimer un utilisateur.

### Réponse

```json
{
  "message": "Utilisateur supprimé avec succès"
}
```
