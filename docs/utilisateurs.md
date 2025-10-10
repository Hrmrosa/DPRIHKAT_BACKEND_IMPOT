# API de Gestion des Utilisateurs

## Champs de l'utilisateur

### Champs obligatoires
- `nomComplet` (string) : Nom complet de l'utilisateur
- `sexe` (enum) : Sexe de l'utilisateur (M ou F)
- `grade` (string) : Grade de l'utilisateur
- `matricule` (string) : Matricule unique de l'utilisateur
- `login` (string) : Identifiant de connexion
- `motDePasse` (string) : Mot de passe (non renvoyé dans les réponses)
- `role` (enum) : Rôle de l'utilisateur (voir énumération ci-dessous)

### Champs optionnels
- `email` (string) : Adresse email de l'utilisateur
- `adresse` (string) : Adresse physique
- `telephone` (string) : Numéro de téléphone

## Énumérations

### Rôles (Role)
- `ADMIN` : Administrateur système
- `DIRECTEUR` : Directeur
- `CHEF_DE_BUREAU` : Chef de bureau
- `TAXATEUR` : Agent taxateur
- `APUREUR` : Agent d'apurement
- `CONTRIBUABLE` : Utilisateur contribuable

### Sexe (Sexe)
- `M` : Masculin
- `F` : Féminin

## Récupérer tous les utilisateurs

`GET /api/users`

### Description
Récupère la liste paginée de tous les utilisateurs avec option de recherche.

### Rôles autorisés
- ADMIN
- INFORMATICIEN

### Paramètres de requête
- `page` (optionnel, défaut: 0) - Numéro de page
- `size` (optionnel, défaut: 10) - Nombre d'éléments par page
- `sortBy` (optionnel, défaut: "id") - Champ de tri
- `sortDir` (optionnel, défaut: "asc") - Direction du tri ("asc" ou "desc")
- `search` (optionnel) - Terme de recherche

### Réponse
```json
{
  "success": true,
  "data": {
    "users": [
      {
        "id": "UUID",
        "login": "string",
        "role": "string",
        "premierConnexion": true,
        "bloque": true,
        "contribuable": {
          "id": "UUID"
        },
        "agent": {
          "id": "UUID"
        }
      }
    ],
    "currentPage": 0,
    "totalItems": 0,
    "totalPages": 0
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
    "code": "USER_FETCH_ERROR",
    "message": "Erreur lors de la récupération des utilisateurs",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer un utilisateur par ID

`GET /api/users/{id}`

### Description
Récupère les détails d'un utilisateur spécifique par son ID.

### Rôles autorisés
- ADMIN
- INFORMATICIEN

### Paramètres
- `id` (chemin) - UUID de l'utilisateur

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "user": {
      "id": "UUID",
      "login": "string",
      "role": "string",
      "premierConnexion": true,
      "bloque": true,
      "contribuable": {
        "id": "UUID"
      },
      "agent": {
        "id": "UUID"
      }
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
    "code": "USER_NOT_FOUND|USER_FETCH_ERROR",
    "message": "string",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Créer un utilisateur

`POST /api/users`

### Description
Crée un nouvel utilisateur.

### Rôles autorisés
- ADMIN
- INFORMATICIEN

### Corps de la requête
```json
{
  "login": "string",
  "motDePasse": "string",
  "role": "string",
  "premierConnexion": true,
  "bloque": true,
  "contribuable": {
    "id": "UUID"
  },
  "agent": {
    "id": "UUID"
  }
}
```

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "user": {
      "id": "UUID",
      "login": "string",
      "role": "string",
      "premierConnexion": true,
      "bloque": true,
      "contribuable": {
        "id": "UUID"
      },
      "agent": {
        "id": "UUID"
      }
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
    "code": "USER_EXISTS|USER_CREATE_ERROR",
    "message": "string",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Mettre à jour un utilisateur

`PUT /api/users/{id}`

### Description
Met à jour les informations d'un utilisateur existant.

### Rôles autorisés
- ADMIN
- INFORMATICIEN

### Paramètres
- `id` (chemin) - UUID de l'utilisateur

### Corps de la requête
```json
{
  "login": "string",
  "role": "string",
  "premierConnexion": true,
  "bloque": true,
  "contribuable": {
    "id": "UUID"
  },
  "agent": {
    "id": "UUID"
  }
}
```

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "user": {
      "id": "UUID",
      "login": "string",
      "role": "string",
      "premierConnexion": true,
      "bloque": true,
      "contribuable": {
        "id": "UUID"
      },
      "agent": {
        "id": "UUID"
      }
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
    "code": "USER_NOT_FOUND|USER_UPDATE_ERROR",
    "message": "string",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Supprimer un utilisateur

`DELETE /api/users/{id}`

### Description
Supprime un utilisateur existant.

### Rôles autorisés
- ADMIN
- INFORMATICIEN

### Paramètres
- `id` (chemin) - UUID de l'utilisateur

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "message": "Utilisateur supprimé avec succès"
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
    "code": "USER_NOT_FOUND|USER_DELETE_ERROR",
    "message": "string",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Bloquer un utilisateur

`POST /api/users/{id}/block`

### Description
Bloque un utilisateur existant.

### Rôles autorisés
- ADMIN
- INFORMATICIEN

### Paramètres
- `id` (chemin) - UUID de l'utilisateur

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "user": {
      "id": "UUID",
      "login": "string",
      "role": "string",
      "premierConnexion": true,
      "bloque": true,
      "contribuable": {
        "id": "UUID"
      },
      "agent": {
        "id": "UUID"
      }
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
    "code": "USER_NOT_FOUND|USER_BLOCK_ERROR",
    "message": "string",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Débloquer un utilisateur

`POST /api/users/{id}/unblock`

### Description
Débloque un utilisateur existant.

### Rôles autorisés
- ADMIN
- INFORMATICIEN

### Paramètres
- `id` (chemin) - UUID de l'utilisateur

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "user": {
      "id": "UUID",
      "login": "string",
      "role": "string",
      "premierConnexion": true,
      "bloque": false,
      "contribuable": {
        "id": "UUID"
      },
      "agent": {
        "id": "UUID"
      }
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
    "code": "USER_NOT_FOUND|USER_UNBLOCK_ERROR",
    "message": "string",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer les utilisateurs par rôle

`GET /api/users/role/{role}`

### Description
Récupère la liste paginée des utilisateurs filtrés par rôle.

### Rôles autorisés
- ADMIN
- INFORMATICIEN

### Paramètres
- `role` (chemin) - Rôle des utilisateurs à récupérer

### Paramètres de requête
- `page` (optionnel, défaut: 0) - Numéro de page
- `size` (optionnel, défaut: 10) - Nombre d'éléments par page
- `sortBy` (optionnel, défaut: "id") - Champ de tri
- `sortDir` (optionnel, défaut: "asc") - Direction du tri ("asc" ou "desc")

### Réponse
```json
{
  "success": true,
  "data": {
    "users": [
      {
        "id": "UUID",
        "login": "string",
        "role": "string",
        "premierConnexion": true,
        "bloque": true,
        "contribuable": {
          "id": "UUID"
        },
        "agent": {
          "id": "UUID"
        }
      }
    ],
    "currentPage": 0,
    "totalItems": 0,
    "totalPages": 0
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
    "code": "USER_FETCH_ERROR",
    "message": "Erreur lors de la récupération des utilisateurs par rôle",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```
