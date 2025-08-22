# Documentation API — Taxation, Utilisateurs, Vignettes

Cette section complète la documentation de l’API Impôts Haut-Katanga en couvrant trois contrôleurs: `TaxationController`, `UserController`, et `VignetteController`.

Notes générales:
- Réponses JSON conformes à `ResponseUtil` et retournées via `ResponseEntity`. Les réponses de succès contiennent généralement un objet ou liste métier (`declaration`, `user`, `vignettes`, etc.) et éventuellement `message`. Les erreurs contiennent un code (ex. `PROPERTY_NOT_FOUND`) et des messages.
- Authentification JWT obligatoire (voir `AUTHENTIFICATION.md`). Les rôles sont contrôlés via `@PreAuthorize`.

---

## 1) TaxationController
- Base path: `/api/taxation`

Endpoints:
- POST `/property/{propertyId}`
  - Rôles: `TAXATEUR`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`
  - Paramètres:
    - Path: `propertyId` (UUID)
    - Auth: utilisateur authentifié (doit être un agent)
  - Réponse succès: `{ message, declaration }`
  - Erreurs: `INVALID_USER`, `PROPERTY_NOT_FOUND`, `TAXATION_ERROR`

- POST `/concession/{concessionId}`
  - Rôles: `TAXATEUR`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`
  - Paramètres:
    - Path: `concessionId` (UUID)
    - Auth: utilisateur authentifié (doit être un agent)
  - Réponse succès: `{ message, declaration }`
  - Erreurs: `INVALID_USER`, `CONCESSION_NOT_FOUND`, `TAXATION_ERROR`

- GET `/calculate/if/property/{propertyId}`
  - Rôles: `TAXATEUR`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `CONTRIBUABLE`
  - Paramètres:
    - Path: `propertyId` (UUID)
  - Réponse succès: `{ message, montant }`
  - Erreurs: `PROPERTY_NOT_FOUND`, `CALCULATION_ERROR`

- GET `/calculate/icm/concession/{concessionId}`
  - Rôles: `TAXATEUR`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `CONTRIBUABLE`
  - Paramètres:
    - Path: `concessionId` (UUID)
  - Réponse succès: `{ message, montant }`
  - Erreurs: `CONCESSION_NOT_FOUND`, `CALCULATION_ERROR`

- GET `/calculate/irl/property/{propertyId}`
  - Rôles: `TAXATEUR`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `CONTRIBUABLE`
  - Paramètres:
    - Path: `propertyId` (UUID)
  - Réponse succès: `{ message, montant }`
  - Erreurs: `PROPERTY_NOT_FOUND`, `CALCULATION_ERROR`

- GET `/calculate/irv`
  - Rôles: `TAXATEUR`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `CONTRIBUABLE`
  - Paramètres:
    - Query: `puissanceCV` (double), `poids` (double)
  - Réponse succès: `{ message, montant }`
  - Erreurs: `CALCULATION_ERROR`

Exemples cURL:
```bash
# Générer une note de taxation pour une propriété
curl -X POST "{BASE_URL}/api/taxation/property/{propertyId}" \
  -H "Authorization: Bearer {JWT}" -H "Content-Type: application/json"

# Calcul IF d'une propriété
curl "{BASE_URL}/api/taxation/calculate/if/property/{propertyId}" \
  -H "Authorization: Bearer {JWT}"

# Calcul IRV par puissance/poids
curl "{BASE_URL}/api/taxation/calculate/irv?puissanceCV=12&poids=1500" \
  -H "Authorization: Bearer {JWT}"
```

---

## 2) UserController
- Base path: `/api/users`

Endpoints:
- GET ``/``
  - Rôles: `ADMIN` ou `INFORMATICIEN`
  - Paramètres query: `page` (0), `size` (10), `sortBy` (id), `sortDir` (asc|desc), `search` (optionnel)
  - Réponse succès: `{ users, currentPage, totalItems, totalPages }`
  - Erreurs: `USER_FETCH_ERROR`

- GET `/{id}`
  - Rôles: `ADMIN` ou `INFORMATICIEN`
  - Path: `id` (UUID)
  - Réponse succès: `{ user }`
  - Erreurs: `USER_NOT_FOUND`, `USER_FETCH_ERROR`

- POST ``/``
  - Rôles: `ADMIN` ou `INFORMATICIEN`
  - Body: `Utilisateur` (incluant `login`, `motDePasse`, `role`, etc.)
  - Comportement: hash du mot de passe via `LetsCrypt`, `premierConnexion=true`
  - Réponse succès: `{ user }`
  - Erreurs: `USER_EXISTS`, `USER_CREATE_ERROR`

- PUT `/{id}`
  - Rôles: `ADMIN` ou `INFORMATICIEN`
  - Path: `id` (UUID)
  - Body: `Utilisateur` (champs mis à jour: `login`, `role`, `premierConnexion`, `bloque`, `contribuable`, `agent`)
  - Réponse succès: `{ user }`
  - Erreurs: `USER_NOT_FOUND`, `USER_UPDATE_ERROR`

- DELETE `/{id}`
  - Rôles: `ADMIN` ou `INFORMATICIEN`
  - Path: `id` (UUID)
  - Réponse succès: `{ message }`
  - Erreurs: `USER_NOT_FOUND`, `USER_DELETE_ERROR`

- POST `/{id}/block`
  - Rôles: `ADMIN` ou `INFORMATICIEN`
  - Path: `id` (UUID)
  - Réponse succès: `{ user }`
  - Erreurs: `USER_NOT_FOUND`, `USER_BLOCK_ERROR`

- POST `/{id}/unblock`
  - Rôles: `ADMIN` ou `INFORMATICIEN`
  - Path: `id` (UUID)
  - Réponse succès: `{ user }`
  - Erreurs: `USER_NOT_FOUND`, `USER_UNBLOCK_ERROR`

- GET `/role/{role}`
  - Rôles: `ADMIN` ou `INFORMATICIEN`
  - Path: `role` (enum `Role`)
  - Query: `page` (0), `size` (10), `sortBy` (id), `sortDir` (asc|desc)
  - Réponse succès: `{ users, currentPage, totalItems, totalPages }`
  - Erreurs: `USER_FETCH_ERROR`

Exemples cURL:
```bash
# Liste paginée (recherche optionnelle)
curl "{BASE_URL}/api/users?page=0&size=10&sortBy=login&sortDir=asc&search=doe" \
  -H "Authorization: Bearer {JWT}"

# Création d'utilisateur
curl -X POST "{BASE_URL}/api/users" -H "Authorization: Bearer {JWT}" \
  -H "Content-Type: application/json" \
  -d '{
    "login": "jdoe",
    "motDePasse": "P@ssword!",
    "role": "INFORMATICIEN"
  }'
```

---

## 3) VignetteController
- Base path: `/api/vignettes`

Endpoints:
- POST `/generate/{vehiculeId}`
  - Rôles: `TAXATEUR`
  - Paramètres:
    - Path: `vehiculeId` (UUID)
    - Query: `dateExpirationMillis` (long), `montant` (Double, optionnel), `puissance` (Double, optionnel)
    - Auth: utilisateur authentifié (doit être un agent)
  - Réponse succès: `{ vignette, message }`
  - Erreurs: `INVALID_USER`, `VIGNETTE_GENERATION_ERROR`

- GET `/{id}`
  - Rôles: `TAXATEUR`, `RECEVEUR_DES_IMPOTS`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `CONTRIBUABLE`
  - Path: `id` (UUID)
  - Réponse succès: `{ vignette }`
  - Erreurs: `VIGNETTE_NOT_FOUND`, `VIGNETTE_FETCH_ERROR`

- GET `/vehicle/{vehiculeId}`
  - Rôles: `TAXATEUR`, `RECEVEUR_DES_IMPOTS`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `CONTRIBUABLE`
  - Paramètres:
    - Path: `vehiculeId` (UUID)
    - Query: `page` (0), `size` (10) — utilisés pour le métadonnées de pagination retour; la liste renvoyée provient du service
  - Réponse succès: `{ vignettes, currentPage, totalItems, totalPages }`
  - Erreurs: `VIGNETTE_FETCH_ERROR`

- GET `/active`
  - Rôles: `TAXATEUR`, `RECEVEUR_DES_IMPOTS`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`
  - Query: `page` (0), `size` (10)
  - Réponse succès: `{ vignettes, currentPage, totalItems, totalPages }`
  - Erreurs: `VIGNETTE_FETCH_ERROR`

- GET `/expired`
  - Rôles: `TAXATEUR`, `RECEVEUR_DES_IMPOTS`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`
  - Query: `page` (0), `size` (10)
  - Réponse succès: `{ vignettes, currentPage, totalItems, totalPages }`
  - Erreurs: `VIGNETTE_FETCH_ERROR`

Exemples cURL:
```bash
# Générer une vignette véhicule
curl -X POST "{BASE_URL}/api/vignettes/generate/{vehiculeId}?dateExpirationMillis=1735689600000&montant=0&puissance=12" \
  -H "Authorization: Bearer {JWT}"

# Lister vignettes d'un véhicule
curl "{BASE_URL}/api/vignettes/vehicle/{vehiculeId}?page=0&size=10" \
  -H "Authorization: Bearer {JWT}"
```

---

Référez-vous à `README_FR.md` et `AUTHENTIFICATION.md` pour les conventions générales, la sécurité et les exemples d’authentification.
