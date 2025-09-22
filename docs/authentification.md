# Documentation API - Authentification et Gestion des Utilisateurs

Cette documentation détaille les endpoints disponibles pour l'authentification et la gestion des utilisateurs dans l'API DPRIHKAT.

## Vue d'ensemble

Le système d'authentification permet aux utilisateurs de se connecter à l'application avec différents rôles (ADMIN, DIRECTEUR, CHEF_DE_BUREAU, TAXATEUR, RECEVEUR_DES_IMPOTS, CONTRIBUABLE, etc.). Chaque rôle a des permissions spécifiques pour accéder aux différentes fonctionnalités de l'application.

## Authentification JWT

### Nouveautés

- **Nettoyage automatique des tokens** : Les tokens JWT sont maintenant automatiquement nettoyés des caractères invalides avant validation
- **Validation renforcée** : Vérification de la structure du token (3 segments séparés par des points)
- **Gestion des erreurs améliorée** : Messages d'erreur plus clairs pour les tokens invalides

### Format du token

Le token JWT doit être inclus dans le header `Authorization` sous la forme :
```
Bearer [token]
```

**Important** :
- Le token ne doit pas contenir d'espaces ou de caractères spéciaux
- Les tokens expirés ou malformés seront rejetés

## Base URL

```
/api/auth
```

## Endpoints d'authentification

### 1. Connexion

Permet à un utilisateur de se connecter à l'application.

- **URL**: `/api/auth/login`
- **Méthode**: `POST`
- **Rôles autorisés**: Tous
- **Corps de la requête**:

```json
{
  "login": "utilisateur123",
  "motDePasse": "motdepasse123"
}
```

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "login": "utilisateur123",
    "role": "TAXATEUR",
    "userId": "123e4567-e89b-12d3-a456-426614174000"
  }
}
```

#### Nouveaux codes d'erreur possibles :
- `INVALID_TOKEN_FORMAT` : Le token contient des caractères invalides
- `MALFORMED_TOKEN` : La structure du token est incorrecte
- `EXPIRED_TOKEN` : Le token a expiré

## Utilisation du token

Toutes les requêtes API (sauf `/api/auth/login`) doivent inclure le token JWT dans le header :
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Important** :
- Ne pas ajouter d'espaces supplémentaires dans le header
- Le token doit être valide et non expiré
- Les tokens sont valables 24 heures par défaut

## Base URL pour la gestion des utilisateurs

```
/api/users
```

## Endpoints de gestion des utilisateurs

### 1. Récupérer tous les utilisateurs

Récupère la liste de tous les utilisateurs enregistrés dans le système.

- **URL**: `/api/users`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`
- **Paramètres**: Aucun

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "utilisateurs": [
      {
        "id": "uuid-string",
        "login": "utilisateur123",
        "role": "TAXATEUR",
        "premiereConnexion": false,
        "bloque": false,
        "agent": {
          "id": "uuid-string",
          "nom": "Nom de l'agent",
          "matricule": "AGT-001"
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
    "code": "USERS_FETCH_ERROR",
    "message": "Erreur lors de la récupération des utilisateurs",
    "details": "Message d'erreur détaillé"
  }
}
```

### 2. Récupérer un utilisateur par son ID

Récupère les détails d'un utilisateur spécifique à partir de son identifiant unique.

- **URL**: `/api/users/{id}`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`
- **Paramètres**:
  - `id` (path): UUID de l'utilisateur à récupérer

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "utilisateur": {
      "id": "uuid-string",
      "login": "utilisateur123",
      "role": "TAXATEUR",
      "premiereConnexion": false,
      "bloque": false,
      "agent": {
        "id": "uuid-string",
        "nom": "Nom de l'agent",
        "matricule": "AGT-001"
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
    "code": "USER_NOT_FOUND",
    "message": "Utilisateur non trouvé",
    "details": "Aucun utilisateur avec cet ID n'existe"
  }
}
```

### 3. Créer un nouvel utilisateur

Crée un nouvel utilisateur dans le système.

- **URL**: `/api/users`
- **Méthode**: `POST`
- **Rôles autorisés**: `ADMIN`
- **Corps de la requête**:

```json
{
  "login": "nouvelUtilisateur",
  "motDePasse": "motdepasse123",
  "role": "TAXATEUR",
  "agentId": "uuid-string"
}
```

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "utilisateur": {
      "id": "uuid-string",
      "login": "nouvelUtilisateur",
      "role": "TAXATEUR",
      "premiereConnexion": true,
      "bloque": false,
      "agent": {
        "id": "uuid-string",
        "nom": "Nom de l'agent",
        "matricule": "AGT-001"
      }
    },
    "message": "Utilisateur créé avec succès"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "USER_CREATE_ERROR",
    "message": "Erreur lors de la création de l'utilisateur",
    "details": "Le login est déjà utilisé"
  }
}
```

### 4. Mettre à jour un utilisateur

Met à jour les informations d'un utilisateur existant.

- **URL**: `/api/users/{id}`
- **Méthode**: `PUT`
- **Rôles autorisés**: `ADMIN`
- **Paramètres**:
  - `id` (path): UUID de l'utilisateur à mettre à jour
- **Corps de la requête**:

```json
{
  "role": "CHEF_DE_BUREAU",
  "bloque": false
}
```

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "utilisateur": {
      "id": "uuid-string",
      "login": "utilisateur123",
      "role": "CHEF_DE_BUREAU",
      "premiereConnexion": false,
      "bloque": false,
      "agent": {
        "id": "uuid-string",
        "nom": "Nom de l'agent",
        "matricule": "AGT-001"
      }
    },
    "message": "Utilisateur mis à jour avec succès"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "USER_UPDATE_ERROR",
    "message": "Erreur lors de la mise à jour de l'utilisateur",
    "details": "Message d'erreur détaillé"
  }
}
```

### 5. Bloquer/Débloquer un utilisateur

Bloque ou débloque un utilisateur existant.

- **URL**: `/api/users/{id}/toggle-block`
- **Méthode**: `PUT`
- **Rôles autorisés**: `ADMIN`
- **Paramètres**:
  - `id` (path): UUID de l'utilisateur à bloquer/débloquer

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "utilisateur": {
      "id": "uuid-string",
      "login": "utilisateur123",
      "role": "TAXATEUR",
      "premiereConnexion": false,
      "bloque": true,
      "agent": {
        "id": "uuid-string",
        "nom": "Nom de l'agent",
        "matricule": "AGT-001"
      }
    },
    "message": "Utilisateur bloqué avec succès"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "USER_BLOCK_ERROR",
    "message": "Erreur lors du blocage/déblocage de l'utilisateur",
    "details": "Message d'erreur détaillé"
  }
}
```

### 6. Supprimer un utilisateur

Supprime un utilisateur du système.

- **URL**: `/api/users/{id}`
- **Méthode**: `DELETE`
- **Rôles autorisés**: `ADMIN`
- **Paramètres**:
  - `id` (path): UUID de l'utilisateur à supprimer

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "message": "Utilisateur supprimé avec succès"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "USER_DELETE_ERROR",
    "message": "Erreur lors de la suppression de l'utilisateur",
    "details": "Message d'erreur détaillé"
  }
}
```

## Structure de l'entité Utilisateur

| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant unique de l'utilisateur |
| login | String | Nom d'utilisateur pour la connexion |
| motDePasse | String | Mot de passe crypté |
| role | Enum (Role) | Rôle de l'utilisateur (ADMIN, DIRECTEUR, CHEF_DE_BUREAU, TAXATEUR, RECEVEUR_DES_IMPOTS, CONTRIBUABLE, etc.) |
| premiereConnexion | boolean | Indique si c'est la première connexion de l'utilisateur |
| bloque | boolean | Indique si le compte utilisateur est bloqué |
| agent | Agent | Agent associé à cet utilisateur |
| contribuable | Contribuable | Contribuable associé à cet utilisateur (si applicable) |

## Règles métier

1. Chaque utilisateur doit être associé à un agent ou à un contribuable.
2. Les utilisateurs doivent changer leur mot de passe lors de leur première connexion.
3. Seuls les administrateurs peuvent créer, modifier ou supprimer des utilisateurs.
4. Les mots de passe sont stockés de manière cryptée dans la base de données.
5. Un utilisateur peut être bloqué par un administrateur en cas de besoin.
6. Les tokens JWT sont utilisés pour l'authentification et ont une durée de validité limitée.
