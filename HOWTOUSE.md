# HOWTOUSE (FR) — Guide d’utilisation rapide

Ce guide explique comment installer, configurer, démarrer et utiliser l’API des Impôts du Haut-Katanga, avec un focus sur l’authentification JWT et les principaux endpoints.

---

## 12) Sérialisation JSON (important)
- Pour éviter les récursions infinies et réduire la taille des payloads:
  - `Agent`: n’expose pas `declarations`, `poursuites`, `apurements`, `controlesInitiates`, `controlesValidates`.
  - `Propriete`: cache `declarations`; `proprietaire` est sérialisé comme ID.
  - `ConcessionMinier`: cache `declarations`; `titulaire` est sérialisé comme ID.
- Certaines relations dans `Division`/`Bureau` sont renvoyées sous forme d’IDs.

## 13) Comptes de test (seed)
- admin / Tabc@123 — ROLE: ADMIN (login direct)
- taxateur1 / Tabc@123 — ROLE: TAXATEUR (login direct)
- receveur1 / Tabc@123 — ROLE: RECEVEUR_DES_IMPOTS (login direct)
- contrib1 / Tabc@123 — ROLE: CONTRIBUABLE (première connexion requise)
- contrib2 / Tabc@123 — ROLE: CONTRIBUABLE (login direct) — à utiliser pour tester `/api/proprietes/mine` et `/api/concessions/mine`
- controlleur1 / Controlleur@2025 — ROLE: CONTROLLEUR (login direct) — à utiliser pour tester la collecte mobile et la visualisation des contribuables
- informaticien1 / Informaticien@2025 — ROLE: INFORMATICIEN (login direct) — à utiliser pour tester la collecte mobile

---

## 1) Prérequis
- Java 17
- Maven 3.8+
- PostgreSQL 16 avec extension PostGIS

---

## 2) Installation
1. Cloner le projet
2. Créer une base PostgreSQL (activer PostGIS)
3. Configurer `src/main/resources/application.properties`:
   - Connexion DB: `spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password`
   - JWT: `app.jwtSecret`, `app.jwtExpirationMs`
4. Construire: `mvn clean package`

---

## 3) Démarrage
- Dev: `mvn spring-boot:run`
- Jar: `java -jar target/impots-0.0.1-SNAPSHOT.jar`

Documentation OpenAPI:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

---

## 4) Authentification (JWT)
Endpoints (voir `AuthController`):
- Connexion: `POST /api/auth/login`
  - Body JSON: `{ "login": "string", "motDePasse": "string" }`
  - Cas première connexion: réponse avec `premiereConnexion=true` (pas de token)
- Changer mot de passe: `POST /api/auth/change-password`
  - Body JSON: `{ "login": "string", "oldPassword": "string", "newPassword": "string" }`

En-tête requis ensuite sur endpoints protégés:
- `Authorization: Bearer <JWT_TOKEN>`

Note: le code utilise actuellement un hash SHA-256 (`LetsCrypt`) pour les mots de passe; un `BCryptPasswordEncoder` est défini mais non employé dans le flux principal.

---

## 5) Règles d’accès par rôle (extrait)
D’après `SecurityConfig`:
- Public: `/api/auth/**`
- `POST /api/declarations/soumettre`: `CONTRIBUABLE`
- `POST /api/declarations/manuelle`: `TAXATEUR`, `RECEVEUR_DES_IMPOTS`
- `/api/plaques/**`: `TAXATEUR`, `RECEVEUR_DES_IMPOTS`
- `PUT /api/penalites/ajuster`: `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`
- Autres: authentification requise

Collecte de données (mobile) — uniquement `CONTROLLEUR`, `INFORMATICIEN`:
- `PATCH /api/proprietes/{id}/location` (mise à jour du point géo d’un bien existant)
- `GET /api/proprietes/by-contribuable/{contribuableId}` (lister les biens d’un contribuable)
- `POST /api/collecte/contribuables` (créer un contribuable et ses biens avec géolocalisation)

Rôles disponibles: voir `src/main/java/com/DPRIHKAT/entity/enums/Role.java`.

---

## 6) Exemples cURL (JSON)

### 6.1 Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"login":"john_doe","motDePasse":"secret"}'
```
Réponse (succès): token, type=Bearer, login, role
Réponse (première connexion): `premiereConnexion=true` (pas de token)

### 6.2 Changer le mot de passe
```bash
curl -X POST http://localhost:8080/api/auth/change-password \
  -H "Content-Type: application/json" \
  -d '{"login":"john_doe","oldPassword":"old","newPassword":"new"}'
```

### 6.3 Appel protégé
```bash
curl -H "Authorization: Bearer JWT_TOKEN" \
  "http://localhost:8080/api/declarations?page=0&size=10&sort=date,desc"
```

---

### 6.4 Propriétés — accès direct (staff)
```bash
curl -H "Authorization: Bearer STAFF_JWT" \
  http://localhost:8080/api/proprietes

curl -H "Authorization: Bearer STAFF_JWT" \
  http://localhost:8080/api/proprietes/{id}
```
Réponse: `proprietaire` est renvoyé comme ID. La collection `declarations` est cachée.

### 6.5 Concessions minières — accès direct (staff)
```bash
curl -H "Authorization: Bearer STAFF_JWT" \
  http://localhost:8080/api/concessions

curl -H "Authorization: Bearer STAFF_JWT" \
  http://localhost:8080/api/concessions/{id}
```
Réponse: `titulaire` est renvoyé comme ID. La collection `declarations` est cachée.

### 6.6 Mes biens (Contribuable)
```bash
curl -H "Authorization: Bearer CONTRIBUABLE_JWT" \
  http://localhost:8080/api/proprietes/mine

curl -H "Authorization: Bearer CONTRIBUABLE_JWT" \
  http://localhost:8080/api/concessions/mine
```
Renvoie uniquement les biens du contribuable authentifié.

---

### 6.7 Collecte mobile (CONTROLLEUR, INFORMATICIEN)

Mettre à jour la localisation d’un bien existant:
```bash
curl -X PATCH \
  -H "Authorization: Bearer CONTROLLEUR_JWT" \
  -H "Content-Type: application/json" \
  -d '{"latitude": -11.659, "longitude": 27.481}' \
  http://localhost:8080/api/proprietes/{proprieteId}/location
```

Lister les biens d’un contribuable par ID:
```bash
curl -H "Authorization: Bearer CONTROLLEUR_JWT" \
  http://localhost:8080/api/proprietes/by-contribuable/{contribuableId}
```

Créer un nouveau contribuable avec ses biens et points géo:
```bash
curl -X POST \
  -H "Authorization: Bearer CONTROLLEUR_JWT" \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "KABILA K.",
    "adressePrincipale": "Av. Lubumbashi",
    "telephonePrincipal": "+243999000111",
    "email": "kk@example.com",
    "type": "PERSONNE_PHYSIQUE",
    "biens": [
      {
        "type": "VI",
        "localite": "Golf",
        "rangLocalite": 2,
        "superficie": 350.0,
        "adresse": "Parcelle 12",
        "latitude": -11.66,
        "longitude": 27.48
      }
    ]
  }' \
  http://localhost:8080/api/collecte/contribuables
```

### 6.8 Référentiels JSON (communes et véhicules)

Endpoints lecture seule chargés depuis `src/main/resources/communes.json` et `src/main/resources/voiture.json` (recherche insensible à la casse):

Communes:
- `GET /api/ref/communes` — liste des communes
- `GET /api/ref/communes/{commune}/quartiers` — quartiers d’une commune
- `GET /api/ref/communes/{commune}/quartiers/{quartier}/avenues` — avenues d’un quartier

Voitures:
- `GET /api/ref/voitures/marques` — liste des marques
- `GET /api/ref/voitures/marques/{marque}/models` — modèles pour une marque

Exemples cURL:
```bash
# Communes → Quartiers → Avenues
curl http://localhost:8080/api/ref/communes
curl http://localhost:8080/api/ref/communes/kamalondo/quartiers
curl http://localhost:8080/api/ref/communes/kamalondo/quartiers/kitumaini/avenues

# Voitures → Marques → Modèles
curl http://localhost:8080/api/ref/voitures/marques
curl http://localhost:8080/api/ref/voitures/marques/BMW/models
```

Réponse type:
```json
{
  "success": true,
  "data": { "quartiers": ["Kitumaini", "Njanja"] },
  "error": null,
  "meta": { "timestamp": "...", "version": "1.0.0" }
}
```

Note: ces endpoints ne modifient pas la base; les données sont chargées et mises en cache en mémoire.

## 7) Pagination
Paramètres standard Spring Data:
- `page` (0-based)
- `size`
- `sort` (ex: `date,desc`)

---

## 8) Bonnes pratiques
- Toujours envoyer `Content-Type: application/json` pour les endpoints d’authentification.
- Stocker le JWT côté client de manière sécurisée (ex: mémoire volatile).
- Régénérer un token après changement de mot de passe.
- Surveiller la valeur `app.jwtExpirationMs` pour l’expiration.

---

## 9) Dépannage
- 401/403: vérifier l’en-tête `Authorization` et l’expiration du token.
- `USER_NOT_FOUND`/`ACCOUNT_BLOCKED`: vérifier l’état du compte.
- `INVALID_CREDENTIALS`: vérifier mot de passe et cohérence du hash.
- Base de données: valider URL/identifiants et extension PostGIS.

---

## 10) Points d’attention (technique)
- Endpoint de changement de mot de passe dans le code: `/api/auth/change-password`.
  - Si vous préférez `/api/auth/changer-mot-de-passe`, il faut renommer dans le contrôleur et mettre à jour la doc.
- `app.jwtRefreshExpirationMs` est présent mais aucun endpoint de refresh n’est implémenté.
- `JwtUtils.generateJwtToken` devrait idéalement utiliser `authentication.getName()` pour récupérer le login.

---

## 11) Checklist de première mise en service
- [ ] Base PostgreSQL + PostGIS opérationnelle
- [ ] `application.properties` configuré (DB + JWT)
- [ ] Rôles et utilisateurs initiaux créés
- [ ] Tests de login, première connexion, changement de mot de passe
- [ ] Tests d’accès par rôle (au moins 1 endpoint par rôle clé)
