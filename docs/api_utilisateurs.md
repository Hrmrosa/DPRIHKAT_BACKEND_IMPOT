# Documentation API - Gestion des Utilisateurs

Cette documentation détaille les endpoints disponibles pour la gestion des utilisateurs dans l'API DPRIHKAT.

---

## Vue d'ensemble

Les utilisateurs sont les agents de l'administration et les contribuables qui accèdent au système.

### Base URL
```
/api/utilisateurs
```
 
---

## Endpoints

### 1. Récupérer tous les utilisateurs

- **URL**: `/api/utilisateurs`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "utilisateurs": [
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
    ]
  }
}
```

---

### 2. Récupérer un utilisateur par ID

- **URL**: `/api/utilisateurs/{id}`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`

---

### 3. Créer un utilisateur

Crée un nouveau compte utilisateur.

- **URL**: `/api/utilisateurs`
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
- `nomComplet`: Nom complet
- `role`: Rôle de l'utilisateur
- `agentId`: UUID de l'agent (si applicable)

---

### 4. Mettre à jour un utilisateur

- **URL**: `/api/utilisateurs/{id}`
- **Méthode**: `PUT`
- **Rôles autorisés**: `ADMIN`, `INFORMATICIEN`

---

### 5. Bloquer/Débloquer un utilisateur

- **URL**: `/api/utilisateurs/{id}/bloquer`
- **Méthode**: `POST`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`

---

### 6. Réinitialiser le mot de passe

- **URL**: `/api/utilisateurs/{id}/reset-password`
- **Méthode**: `POST`
- **Rôles autorisés**: `ADMIN`, `INFORMATICIEN`

---

### 7. Changer mon mot de passe

Permet à un utilisateur de changer son propre mot de passe.

- **URL**: `/api/utilisateurs/change-password`
- **Méthode**: `POST`
- **Rôles autorisés**: Tous (authentifiés)

#### Corps de la requête

```json
{
  "ancienMotDePasse": "OldPassword123!",
  "nouveauMotDePasse": "NewPassword123!"
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
- Les mots de passe sont hashés (BCrypt)
- Les sessions sont gérées par JWT
- Expiration du token: 24 heures
