# Documentation API - Gestion des Utilisateurs

Cette documentation détaille les endpoints disponibles pour la gestion des utilisateurs dans l'API DPRIHKAT.

---

## Vue d'ensemble

Les utilisateurs sont les agents de l'administration et les contribuables qui accèdent au système.

### Base URL
```
/api/users
```
 
---

## Endpoints

### 1. Récupérer tous les utilisateurs

Récupère la liste paginée de tous les utilisateurs avec options de tri et recherche.

- **URL**: `/api/users`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `INFORMATICIEN`
- **Paramètres**:
  - `page` (query, optionnel): Numéro de page (défaut: 0)
  - `size` (query, optionnel): Taille de page (défaut: 10)
  - `sortBy` (query, optionnel): Champ de tri (défaut: id)
  - `sortDir` (query, optionnel): Direction du tri - asc/desc (défaut: asc)
  - `search` (query, optionnel): Terme de recherche

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "users": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "login": "agent1",
        "nomComplet": "KABILA Joseph",
        "email": "agent1@dprihkat.cd",
        "telephone": "+243820123456",
        "role": "ADMIN",
        "grade": "Directeur",
        "actif": true,
        "bloque": false,
        "premierConnexion": false
      }
    ],
    "currentPage": 0,
    "totalItems": 150,
    "totalPages": 15
  }
}
```

---

### 2. Récupérer un utilisateur par ID

Récupère les détails d'un utilisateur spécifique.

- **URL**: `/api/users/{id}`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `INFORMATICIEN`
- **Paramètres**:
  - `id` (path): UUID de l'utilisateur

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "user": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "login": "agent1",
      "nomComplet": "KABILA Joseph",
      "email": "agent1@dprihkat.cd",
      "role": "ADMIN",
      "bloque": false
    }
  }
}

---

### 3. Créer un utilisateur

Crée un nouveau compte utilisateur.

- **URL**: `/api/users`
- **Méthode**: `POST`
- **Rôles autorisés**: `ADMIN`, `INFORMATICIEN`

#### Corps de la requête

```json
{
  "login": "agent1",
  "motDePasse": "Password123!",
  "nomComplet": "KABILA Joseph",
  "email": "agent1@dprihkat.cd",
  "telephone": "+243820123456",
  "role": "TAXATEUR",
  "grade": "Agent",
  "sexe": "M",
  "adresse": "Lubumbashi",
  "agentId": "550e8400-e29b-41d4-a716-446655440010"
}
```

#### Champs obligatoires
- `login`: Identifiant de connexion unique
- `motDePasse`: Mot de passe (min 8 caractères)
- `role`: Rôle de l'utilisateur (enum Role)

#### Champs optionnels
- `nomComplet`: Nom complet
- `email`: Adresse email
- `telephone`: Numéro de téléphone
- `grade`: Grade administratif
- `sexe`: Genre (M/F)
- `adresse`: Adresse physique
- `matricule`: Matricule de l'agent
- `contribuable`: Objet Contribuable (pour rôle CONTRIBUABLE)
- `agent`: Objet Agent (pour rôles internes)

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "user": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "login": "agent1",
      "nomComplet": "KABILA Joseph",
      "role": "TAXATEUR",
      "premierConnexion": true
    }
  }
}

---

### 4. Mettre à jour un utilisateur

Met à jour les informations d'un utilisateur existant.

- **URL**: `/api/users/{id}`
- **Méthode**: `PUT`
- **Rôles autorisés**: `ADMIN`, `INFORMATICIEN`
- **Paramètres**:
  - `id` (path): UUID de l'utilisateur

#### Corps de la requête

```json
{
  "login": "agent1_updated",
  "role": "CONTROLLEUR",
  "premierConnexion": false,
  "bloque": false
}
```

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "user": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "login": "agent1_updated",
      "role": "CONTROLLEUR"
    }
  }
}

---

### 5. Supprimer un utilisateur

Supprime un utilisateur du système.

- **URL**: `/api/users/{id}`
- **Méthode**: `DELETE`
- **Rôles autorisés**: `ADMIN`, `INFORMATICIEN`
- **Paramètres**:
  - `id` (path): UUID de l'utilisateur

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "message": "Utilisateur supprimé avec succès"
  }
}
```

---

### 6. Bloquer un utilisateur

Bloque l'accès d'un utilisateur au système.

- **URL**: `/api/users/{id}/block`
- **Méthode**: `POST`
- **Rôles autorisés**: `ADMIN`, `INFORMATICIEN`
- **Paramètres**:
  - `id` (path): UUID de l'utilisateur

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "user": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "login": "agent1",
      "bloque": true
    }
  }
}

---

### 7. Débloquer un utilisateur

Débloque l'accès d'un utilisateur au système.

- **URL**: `/api/users/{id}/unblock`
- **Méthode**: `POST`
- **Rôles autorisés**: `ADMIN`, `INFORMATICIEN`
- **Paramètres**:
  - `id` (path): UUID de l'utilisateur

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "user": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "login": "agent1",
      "bloque": false
    }
  }
}

---

### 8. Récupérer les utilisateurs par rôle

Récupère tous les utilisateurs ayant un rôle spécifique.

- **URL**: `/api/users/role/{role}`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `INFORMATICIEN`
- **Paramètres**:
  - `role` (path): Rôle à filtrer (enum Role)
  - `page` (query, optionnel): Numéro de page (défaut: 0)
  - `size` (query, optionnel): Taille de page (défaut: 10)
  - `sortBy` (query, optionnel): Champ de tri (défaut: id)
  - `sortDir` (query, optionnel): Direction du tri - asc/desc (défaut: asc)

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "users": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "login": "agent1",
        "nomComplet": "KABILA Joseph",
        "role": "TAXATEUR"
      }
    ],
    "currentPage": 0,
    "totalItems": 25,
    "totalPages": 3
  }
}
```

---

## Structure de l'entité Utilisateur

| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant unique |
| login | String | Identifiant de connexion |
| motDePasse | String | Mot de passe (hashé) |
| nomComplet | String | Nom complet |
| email | String | Adresse email |
| telephone | String | Numéro de téléphone |
| role | Enum | Rôle de l'utilisateur |
| grade | String | Grade administratif |
| sexe | Enum | Genre (M/F) |
| adresse | String | Adresse |
| actif | Boolean | Compte actif |
| bloque | Boolean | Compte bloqué |
| premierConnexion | Boolean | Première connexion |
| agent | Agent | Agent associé |
| contribuable | Contribuable | Contribuable associé |

---

## Énumérations

### Role
- `ADMIN`: Administrateur système
- `DIRECTEUR`: Directeur
- `CHEF_DE_DIVISION`: Chef de division
- `CHEF_DE_BUREAU`: Chef de bureau
- `TAXATEUR`: Agent taxateur
- `RECEVEUR_DES_IMPOTS`: Receveur des impôts
- `CONTROLLEUR`: Contrôleur fiscal
- `INFORMATICIEN`: Informaticien
- `CONTRIBUABLE`: Contribuable

---

## Règles métier

### Création de compte
- Le login doit être unique
- Le mot de passe doit respecter les critères de sécurité
- Un utilisateur peut être un agent ou un contribuable

### Première connexion
- À la première connexion, l'utilisateur doit changer son mot de passe
- Le flag `premierConnexion` passe à `false` après changement

### Blocage
- Un compte bloqué ne peut pas se connecter
- Seuls les administrateurs peuvent bloquer/débloquer

### Sécurité
- Les mots de passe sont hashés avec l'algorithme LetsCrypt
- Les sessions sont gérées par JWT
- Expiration du token: 24 heures
- Le préfixe `ROLE_` n'est pas nécessaire dans les vérifications Spring Security

---

## Codes d'erreur

| Code | Description |
|------|-------------|
| USER_FETCH_ERROR | Erreur lors de la récupération des utilisateurs |
| USER_NOT_FOUND | Utilisateur non trouvé |
| USER_EXISTS | Login déjà utilisé |
| USER_CREATE_ERROR | Erreur lors de la création de l'utilisateur |
| USER_UPDATE_ERROR | Erreur lors de la mise à jour de l'utilisateur |
| USER_DELETE_ERROR | Erreur lors de la suppression de l'utilisateur |
| USER_BLOCK_ERROR | Erreur lors du blocage de l'utilisateur |
| USER_UNBLOCK_ERROR | Erreur lors du déblocage de l'utilisateur |
