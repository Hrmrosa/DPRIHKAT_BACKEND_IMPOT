# Authentification

## Connexion

**POST** `/api/auth/login`

Permet à un utilisateur de se connecter à l'application.

### Payload

```json
{
  "login": "string",
  "motDePasse": "string"
}
```

### Réponse

```json
{
  "token": "string",
  "utilisateur": {
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
}
```

## Déconnexion

**POST** `/api/auth/logout`

Permet à un utilisateur de se déconnecter de l'application.

### Réponse

```json
{
  "message": "Déconnexion réussie"
}
```
