# Authentification JWT — Guide d’utilisation (FR)

Ce document décrit en détail les endpoints d’authentification, le flux de connexion et de changement de mot de passe, la génération/validation de JWT, ainsi que les règles d’accès par rôle pour l’API des Impôts du Haut-Katanga.

Références code:
- Contrôleur: `src/main/java/com/DPRIHKAT/controller/AuthController.java`
- Sécurité: `src/main/java/com/DPRIHKAT/config/SecurityConfig.java`
- JWT Utils: `src/main/java/com/DPRIHKAT/security/JwtUtils.java`
- Filtre JWT: `src/main/java/com/DPRIHKAT/security/JwtAuthFilter.java`
- DTOs: `src/main/java/com/DPRIHKAT/dto/LoginRequest.java`, `src/main/java/com/DPRIHKAT/dto/JwtResponse.java`
- Rôles: `src/main/java/com/DPRIHKAT/entity/enums/Role.java`
- Propriétés: `src/main/resources/application.properties` (ou `target/classes/application.properties` à l’exécution)

---

## 1) Endpoints d’authentification

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

## 2) JWT: génération, validation et usage

- Génération: `JwtUtils.generateJwtToken(Authentication)`
  - Algorithme: HS256
  - Subject: le login de l’utilisateur
  - Paramètres: `app.jwtSecret`, `app.jwtExpirationMs`
- Validation: `JwtUtils.validateJwtToken(token)`
- Filtrage des requêtes: `JwtAuthFilter`
  - Extrait le token depuis l’en-tête `Authorization: Bearer <JWT>`
  - Valide le token et charge l’utilisateur dans le contexte Spring Security

### En-têtes requis pour les endpoints protégés
- `Authorization: Bearer <JWT_TOKEN>`

### Durée de vie
- Le jeton expire après `app.jwtExpirationMs` millisecondes.

### Propriétés (exemple)
```
app.jwtSecret=...votre clef secrète...
app.jwtExpirationMs=86400000
app.jwtRefreshExpirationMs=864000000 # présent mais pas utilisé (pas d’endpoint de refresh)
```

---

## 3) Contrôle d’accès par rôle (extrait)

Source: `SecurityConfig`
- Accès public: `/api/auth/**`
- `POST /api/declarations/soumettre`: `CONTRIBUABLE`
- `POST /api/declarations/manuelle`: `TAXATEUR`, `RECEVEUR_DES_IMPOTS`
- `/api/plaques/**`: `TAXATEUR`, `RECEVEUR_DES_IMPOTS`
- `PUT /api/penalites/ajuster`: `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`
- Autres endpoints: authentification requise

Enum des rôles: voir `Role.java`.

---

## 4) Flux de première connexion

1) Appel `POST /api/auth/login` avec identifiants initiaux
2) Réponse:
   - `premiereConnexion=true` (pas de token) + message
3) L’application cliente doit afficher le formulaire de changement de mot de passe
4) Appel `POST /api/auth/change-password` avec `login`, `oldPassword`, `newPassword`
5) En cas de succès, l’utilisateur peut relancer `login` et recevra un JWT

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
