# Authentification JWT — Guide d'utilisation (FR)

Ce document décrit en détail les endpoints d'authentification, le flux de connexion et de changement de mot de passe, la génération/validation de JWT, ainsi que les règles d'accès par rôle pour l'API des Impôts du Haut-Katanga.

Références code:
- Contrôleur: `src/main/java/com/DPRIHKAT/controller/AuthController.java`
- Sécurité: `src/main/java/com/DPRIHKAT/config/SecurityConfig.java`
- JWT Utils: `src/main/java/com/DPRIHKAT/security/JwtUtils.java`
- Filtre JWT: `src/main/java/com/DPRIHKAT/security/JwtAuthFilter.java`
- DTOs: `src/main/java/com/DPRIHKAT/dto/LoginRequest.java`, `src/main/java/com/DPRIHKAT/dto/JwtResponse.java`
- Rôles: `src/main/java/com/DPRIHKAT/entity/enums/Role.java`
- Propriétés: `src/main/resources/application.properties` (ou `target/classes/application.properties` à l'exécution)

---

## 1) Endpoints d'authentification

### 1.1 Connexion
- Méthode/URL: `POST /api/auth/login`
- Corps (JSON):
```json
{
  "login": "string",
  "motDePasse": "string"
}
```
- Réponses possibles:
  - Succès (token émis):
    ```json
    {
      "success": true,
      "data": {
        "token": "JWT_TOKEN",
        "type": "Bearer",
        "login": "john_doe",
        "role": "ADMIN"
      },
      "error": null,
      "meta": { "timestamp": "...", "version": "1.0.0" }
    }
    ```
  - Première connexion (pas de token):
    ```json
    {
      "success": true,
      "data": {
        "premiereConnexion": true,
        "message": "Vous devez changer votre mot de passe lors de la première connexion"
      },
      "error": null,
      "meta": { "timestamp": "...", "version": "1.0.0" }
    }
    ```
  - Erreurs fréquentes:
    - Utilisateur inconnu: code `USER_NOT_FOUND`
    - Compte bloqué: code `ACCOUNT_BLOCKED`
    - Mot de passe incorrect: code `INVALID_CREDENTIALS`
    - Erreur générique: code `AUTH_ERROR`

- Exemple cURL:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"login":"john_doe","motDePasse":"secret"}'
```

### 1.2 Changement de mot de passe
- Méthode/URL: `POST /api/auth/change-password`
- Corps (JSON):
```json
{
  "login": "string",
  "oldPassword": "string",
  "newPassword": "string"
}
```
- Réponses:
```json
{
  "success": true,
  "data": { "message": "Mot de passe changé avec succès" },
  "error": null,
  "meta": { "timestamp": "...", "version": "1.0.0" }
}
```
- Erreurs possibles:
  - Utilisateur inconnu: `USER_NOT_FOUND`
  - Ancien mot de passe incorrect: `INVALID_CREDENTIALS`
  - Erreur générique: `PASSWORD_CHANGE_ERROR`

- Exemple cURL:
```bash
curl -X POST http://localhost:8080/api/auth/change-password \
  -H "Content-Type: application/json" \
  -d '{"login":"john_doe","oldPassword":"old","newPassword":"new"}'
```

> Note: La documentation antérieure mentionnait `/api/auth/changer-mot-de-passe`. Le code actuel expose `/api/auth/change-password`.

---

## 2) Structure et fonctionnement du JWT

### 2.1 Contenu du token
Le token JWT contient les informations suivantes:
- **Subject**: UUID de l'utilisateur (identifiant unique)
- **Issued At**: Date d'émission du token
- **Expiration**: Date d'expiration du token (configurable via `jwt.expirationMs`)
- **Signature**: Signature HMAC-SHA256 avec la clé secrète

> **Sécurité améliorée**: Le token ne contient plus directement le login ni le rôle de l'utilisateur. Ces informations sont récupérées depuis la base de données à chaque requête authentifiée, ce qui renforce la sécurité et permet de révoquer des droits sans avoir à invalider les tokens existants.

### 2.2 Configuration
La configuration JWT se fait via les propriétés suivantes:
- `jwt.secret`: Clé secrète pour signer les tokens (OBLIGATOIRE)
- `jwt.expirationMs`: Durée de validité en millisecondes (défaut: 86400000 = 24h)

Ces propriétés peuvent être définies dans `application.properties` ou via des variables d'environnement:
```properties
# Dans application.properties
jwt.secret=votre_clé_secrète_très_longue_et_complexe
jwt.expirationMs=86400000
```

```bash
# Variables d'environnement
export JWT_SECRET=votre_clé_secrète_très_longue_et_complexe
export JWT_EXPIRATION_MS=86400000
```

### 2.3 Flux d'authentification
1. L'utilisateur envoie ses identifiants (login/mot de passe)
2. Le serveur vérifie les identifiants et génère un token JWT contenant l'UUID de l'utilisateur
3. Le client stocke ce token et l'inclut dans l'en-tête `Authorization: Bearer <token>` pour les requêtes suivantes
4. Pour chaque requête authentifiée:
   - Le serveur extrait l'UUID du token
   - Il récupère l'utilisateur complet depuis la base de données
   - Il vérifie les autorisations basées sur le rôle actuel de l'utilisateur

---

## 3) Rôles et autorisations

Le système utilise les rôles suivants avec leurs permissions associées:

### 3.1 Rôles administratifs
- **ADMIN**: Accès complet à toutes les fonctionnalités
- **INFORMATICIEN**: Gestion des utilisateurs, configuration système, participation à la collecte de données terrain
- **CONTROLLEUR**: Visualisation des contribuables et collecte de données terrain

### 3.2 Rôles de direction
- **DIRECTEUR**: Visualisation de toutes les données et statistiques
- **CHEF_DE_DIVISION**: Gestion des bureaux et agents de sa division
- **CHEF_DE_BUREAU**: Gestion des agents de son bureau

### 3.3 Rôles opérationnels
- **TAXATEUR**: Gestion des contribuables, propriétés et taxation
- **VERIFICATEUR**: Contrôles fiscaux et vérifications
- **APUREUR**: Validation des paiements
- **RECEVEUR_DES_IMPOTS**: Gestion des recouvrements

### 3.4 Rôle externe
- **CONTRIBUABLE**: Accès limité à ses propres données et déclarations

### 3.5 Matrice d'accès par endpoint

| Endpoint                | ADMIN | INFORMATICIEN | DIRECTEUR | CHEF_DIV | CHEF_BUR | TAXATEUR | VERIF | CONTROLLEUR | APUREUR | RECEVEUR | CONTRIB |
|-------------------------|-------|---------------|-----------|----------|----------|----------|-------|-------------|---------|----------|---------|
| /api/users/**           | ✓     | ✓             |           |          |          |          |       |             |         |          |         |
| /api/contribuables/**   | ✓     | ✓             | ✓         | ✓        | ✓        | ✓        | ✓     | ✓           |         |          | (self)  |
| /api/proprietes/**      | ✓     | ✓             | ✓         | ✓        | ✓        | ✓        | ✓     |             |         |          | (self)  |
| /api/declarations/**    | ✓     | ✓             | ✓         | ✓        | ✓        | ✓        | ✓     |             | ✓       | ✓        | (self)  |
| /api/paiements/**       | ✓     | ✓             | ✓         | ✓        | ✓        |          |       |             | ✓       | ✓        | (self)  |
| /api/collecte/**        | ✓     | ✓             |           |          |          |          |       | ✓           |         |          |         |
| /api/controles/**       | ✓     | ✓             | ✓         | ✓        | ✓        |          | ✓     |             |         |          |         |
| /api/poursuites/**      | ✓     | ✓             | ✓         |          |          |          |       | ✓           |         | ✓        |         |
| /api/relances/**        | ✓     | ✓             | ✓         |          |          |          |       | ✓           |         | ✓        |         |

> Note: (self) indique que l'utilisateur n'a accès qu'à ses propres données

---

## 4) Sécurité JWT

### 4.1 Structure du Token

Le token JWT est généré avec une structure sécurisée:

- **Payload**: Contient uniquement l'identifiant unique (UUID) de l'utilisateur
- **Signature**: Utilise l'algorithme HMAC-SHA256 avec une clé secrète
- **Expiration**: Configurée via la propriété `jwt.expirationMs` (par défaut: 86400000 ms = 24h)

> **Note de sécurité**: Le token ne contient pas directement le login ou le rôle de l'utilisateur. Ces informations sont récupérées depuis la base de données lors de la validation du token, ce qui renforce la sécurité et garantit que les informations sont toujours à jour.

### 4.2 Flux d'authentification

1. L'utilisateur envoie ses identifiants (login/mot de passe)
2. Le serveur vérifie les identifiants et génère un token JWT contenant uniquement l'UUID de l'utilisateur
3. Le client stocke ce token et l'inclut dans l'en-tête `Authorization: Bearer <token>` pour les requêtes suivantes
4. Pour chaque requête authentifiée:
   - Le serveur extrait l'UUID du token
   - Il récupère l'utilisateur complet depuis la base de données
   - Il vérifie les autorisations basées sur le rôle actuel de l'utilisateur

### 4.3 Configuration

La configuration JWT se trouve dans `application.properties`:

```properties
# JWT Properties
jwt.secret=votreCléSecrète
jwt.expirationMs=86400000
```

Pour des raisons de sécurité, il est recommandé de définir ces valeurs via des variables d'environnement en production:

```properties
jwt.secret=${JWT_SECRET}
jwt.expirationMs=${JWT_EXPIRATION_MS:86400000}
```

### 4.4 Nouvelle structure du token JWT

La nouvelle structure du token JWT a été améliorée pour renforcer la sécurité :

- **Header**: Contient le type de token (JWT) et l'algorithme de signature (HS256)
- **Payload**: Contient **uniquement** l'identifiant unique (UUID) de l'utilisateur comme sujet du token
  - Ne contient plus directement le login ni le rôle de l'utilisateur
- **Signature**: Utilise l'algorithme HMAC-SHA256 avec une clé secrète pour garantir l'intégrité et l'authenticité du token

Voici un exemple simplifié du payload du token JWT avec la nouvelle structure :

```json
{
  "sub": "550e8400-e29b-41d4-a716-446655440000", // UUID de l'utilisateur
  "iat": 1516239022,                             // Date d'émission
  "exp": 1516325422                              // Date d'expiration
}
```

Cette nouvelle approche présente plusieurs avantages :
1. **Sécurité renforcée** : Les informations sensibles comme le login et le rôle ne sont pas exposées dans le token
2. **Données toujours à jour** : Les informations de l'utilisateur sont récupérées depuis la base de données à chaque requête
3. **Révocation facilitée** : Si un utilisateur est bloqué ou si son rôle change, les modifications sont prises en compte immédiatement

> **Note importante** : Bien que le token ne contienne plus le login et le rôle, la réponse d'authentification envoyée au client contient toujours ces informations pour maintenir la compatibilité avec le frontend existant.

---

## 5) Exemples complémentaires

- Requête authentifiée avec pagination:
```bash
curl -H "Authorization: Bearer JWT_TOKEN" \
  "http://localhost:8080/api/declarations?page=0&size=10&sort=date,desc"
```

- Erreur identifiants (exemple):
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "INVALID_CREDENTIALS",
    "message": "Identifiants invalides",
    "details": "Le mot de passe est incorrect"
  },
  "meta": { "timestamp": "...", "version": "1.0.0" }
}
```

---

## 6) Notes techniques et recommandations

- Hash de mot de passe actuel: SHA-256 via `LetsCrypt` (fichier `util/LetsCrypt.java`).
  - `SecurityConfig` déclare un `BCryptPasswordEncoder` mais la vérification principale est effectuée manuellement dans `AuthController` avec SHA-256.
  - Recommandation: migrer vers BCrypt de bout en bout et retirer la double vérification,
    ou documenter et assumer l’usage de SHA-256 de manière cohérente (provider dédié).
- Refresh token: la propriété `app.jwtRefreshExpirationMs` est définie, mais aucun endpoint de rafraîchissement n’est implémenté.
  - Recommandation: soit implémenter `/api/auth/refresh`, soit supprimer la propriété et la doc associée.

---

## 7) Résolution de problèmes

- 401/403 sur endpoint protégé: vérifier l’en-tête `Authorization: Bearer <JWT>` et l’expiration du token.
- `USER_NOT_FOUND` ou `ACCOUNT_BLOCKED`: vérifier l’état du compte dans la base.
- `INVALID_CREDENTIALS` en login/changement: vérifier le mot de passe et/ou la cohérence du hash.
- Décalage de fuseau horaires/horloge système: l’expiration du token peut être affectée.
