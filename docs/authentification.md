# Documentation API - Authentification et Gestion des Utilisateurs

Cette documentation détaille les endpoints disponibles pour l'authentification et la gestion des utilisateurs dans l'API DPRIHKAT.

## Vue d'ensemble

Le système d'authentification permet aux utilisateurs de se connecter à l'application avec différents rôles (ADMIN, DIRECTEUR, CHEF_DE_BUREAU, TAXATEUR, RECEVEUR_DES_IMPOTS, CONTRIBUABLE, etc.). Chaque rôle a des permissions spécifiques pour accéder aux différentes fonctionnalités de l'application.

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
    "role": "TAXATEUR"
  }
}
```

#### Réponse en cas de première connexion

```json
{
  "success": true,
  "data": {
    "premiereConnexion": true,
    "message": "Vous devez changer votre mot de passe lors de la première connexion"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "INVALID_CREDENTIALS",
    "message": "Identifiants invalides",
    "details": "Le mot de passe est incorrect"
  }
}
```

### 2. Changement de mot de passe

Permet à un utilisateur de changer son mot de passe.

- **URL**: `/api/auth/change-password`
- **Méthode**: `POST`
- **Rôles autorisés**: Tous
- **Corps de la requête**:

```json
{
  "login": "utilisateur123",
  "oldPassword": "ancienMotDePasse",
  "newPassword": "nouveauMotDePasse"
}
```

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "message": "Mot de passe changé avec succès"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "INVALID_CREDENTIALS",
    "message": "Ancien mot de passe incorrect",
    "details": "L'ancien mot de passe est incorrect"
  }
}
```

### 3. Réinitialisation de mot de passe

Permet à un administrateur de réinitialiser le mot de passe d'un utilisateur.

- **URL**: `/api/auth/reset-password`
- **Méthode**: `POST`
- **Rôles autorisés**: `ADMIN`
- **Corps de la requête**:

```json
{
  "userId": "uuid-string"
}
```

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "message": "Mot de passe réinitialisé avec succès",
    "newPassword": "temporaryPassword123"
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
