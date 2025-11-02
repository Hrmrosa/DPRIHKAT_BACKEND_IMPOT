# Documentation API - Gestion des Propriétés

Cette documentation détaille les endpoints disponibles pour la gestion des propriétés (biens immobiliers) dans l'API DPRIHKAT.

---

## Vue d'ensemble

Les propriétés représentent les biens immobiliers appartenant aux contribuables et soumis à l'impôt foncier (IF).

### Base URL
```
/api/proprietes
```

---

## Endpoints

### 1. Récupérer toutes les propriétés (paginé)

Récupère la liste paginée de toutes les propriétés enregistrées.
 
- **URL**: `/api/proprietes`
- **Méthode**: `GET`
- **Rôles autorisés**: `TAXATEUR`, `RECEVEUR_DES_IMPOTS`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `ADMIN`, `INFORMATICIEN`
- **Paramètres**:
  - `page` (query, optionnel): Numéro de page (défaut: 0)
  - `size` (query, optionnel): Taille de page (défaut: 10)

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "data": {
      "totalItems": 150,
      "totalPages": 15,
      "currentPage": 0,
      "proprietes": [
        {
          "id": "550e8400-e29b-41d4-a716-446655440000",
          "adresse": "Avenue de la Liberté, Lubumbashi",
          "type": "VI",
          "superficie": 500.0,
          "valeurLocative": 1500000.0,
          "montantImpot": 75000.0,
          "rangLocalite": 1,
          "declare": true,
          "declarationEnLigne": false,
          "actif": true,
          "proprietaire": {
            "id": "550e8400-e29b-41d4-a716-446655440001",
            "nom": "KABILA",
            "numeroIdentificationContribuable": "NIF-123456789"
          }
        }
      ]
    }
  }
}
```

---

### 2. Récupérer toutes les propriétés avec tri (paginé)

Récupère la liste paginée avec options de tri.

- **URL**: `/api/proprietes/paginated`
- **Méthode**: `GET`
- **Rôles autorisés**: `TAXATEUR`, `RECEVEUR_DES_IMPOTS`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `ADMIN`, `INFORMATICIEN`
- **Paramètres**:
  - `page` (query, optionnel): Numéro de page (défaut: 0)
  - `size` (query, optionnel): Taille de page (défaut: 10)
  - `sortBy` (query, optionnel): Champ de tri (défaut: id)
  - `sortDir` (query, optionnel): Direction du tri (asc/desc, défaut: asc)

#### Exemple de requête

```
GET /api/proprietes/paginated?page=0&size=20&sortBy=adresse&sortDir=asc
```

---

### 3. Récupérer une propriété par ID

Récupère les détails d'une propriété spécifique.

- **URL**: `/api/proprietes/{id}`
- **Méthode**: `GET`
- **Rôles autorisés**: `TAXATEUR`, `RECEVEUR_DES_IMPOTS`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `ADMIN`, `INFORMATICIEN`
- **Paramètres**:
  - `id` (path): UUID de la propriété

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "propriete": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "adresse": "Avenue de la Liberté, Lubumbashi",
      "type": "VI",
      "superficie": 500.0,
      "valeurLocative": 1500000.0,
      "montantImpot": 75000.0,
      "rangLocalite": 1,
      "declare": true,
      "declarationEnLigne": false,
      "actif": true,
      "proprietaire": {
        "id": "550e8400-e29b-41d4-a716-446655440001",
        "nom": "KABILA JOSEPH LAURENT",
        "type": "PERSONNE_PHYSIQUE"
      },
      "naturesImpot": [
        {
          "id": "550e8400-e29b-41d4-a716-446655440010",
          "code": "IF",
          "libelle": "Impôt Foncier"
        }
      ]
    }
  }
}
```

---

### 4. Récupérer mes propriétés (contribuable)

Permet à un contribuable de récupérer ses propres propriétés.

- **URL**: `/api/proprietes/mine`
- **Méthode**: `GET`
- **Rôles autorisés**: `CONTRIBUABLE`
- **Paramètres**: Aucun (utilise l'authentification)

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "proprietes": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "adresse": "Avenue de la Liberté",
        "type": "VI",
        "superficie": 500.0,
        "montantImpot": 75000.0,
        "declare": true,
        "actif": true
      }
    ]
  }
}
```

---

### 5. Récupérer mes propriétés paginées (contribuable)

Version paginée pour les contribuables.

- **URL**: `/api/proprietes/mine/paginated`
- **Méthode**: `GET`
- **Rôles autorisés**: `CONTRIBUABLE`
- **Paramètres**:
  - `page`, `size`, `sortBy`, `sortDir` (mêmes que endpoint 2)

---

### 6. Créer une nouvelle propriété

Crée une nouvelle propriété dans le système.

- **URL**: `/api/proprietes`
- **Méthode**: `POST`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`, `TAXATEUR`

#### Corps de la requête

```json
{
  "adresse": "Avenue de la Liberté, Lubumbashi",
  "type": "VI",
  "superficie": 500.0,
  "valeurLocative": 1500000.0,
  "rangLocalite": 1,
  "proprietaireId": "550e8400-e29b-41d4-a716-446655440001",
  "latitude": -11.6645,
  "longitude": 27.4794
}
```

#### Champs obligatoires
- `adresse`: Adresse complète de la propriété
- `type`: Type de propriété (VI, AP, CH, TE, DEPOT)
- `superficie`: Superficie en m²
- `proprietaireId`: UUID du propriétaire (contribuable)
- `rangLocalite`: Rang de la localité (1, 2, 3)

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "propriete": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "adresse": "Avenue de la Liberté, Lubumbashi",
      "type": "VI",
      "superficie": 500.0,
      "montantImpot": 75000.0,
      "actif": true,
      "declare": false
    }
  }
}
```

---

### 7. Mettre à jour une propriété

Met à jour les informations d'une propriété existante.

- **URL**: `/api/proprietes/{id}`
- **Méthode**: `PUT`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`, `TAXATEUR`

---

### 8. Déclarer une propriété

Permet de déclarer une propriété (en ligne ou hors ligne).

- **URL**: `/api/proprietes/{id}/declarer`
- **Méthode**: `POST`
- **Rôles autorisés**: `CONTRIBUABLE`, `ADMIN`, `TAXATEUR`
- **Paramètres**:
  - `enLigne` (query): true pour déclaration en ligne, false pour déclaration à l'administration

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "message": "Propriété déclarée avec succès",
    "propriete": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "declare": true,
      "declarationEnLigne": true
    }
  }
}
```

---

### 9. Supprimer une propriété

Suppression logique d'une propriété.

- **URL**: `/api/proprietes/{id}`
- **Méthode**: `DELETE`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`

---

## Structure de l'entité Propriété

| Champ | Type | Description | Obligatoire |
|-------|------|-------------|-------------|
| id | UUID | Identifiant unique | Auto-généré |
| adresse | String | Adresse complète | Oui |
| type | Enum | Type de propriété | Oui |
| superficie | Double | Superficie en m² | Oui |
| valeurLocative | Double | Valeur locative annuelle | Non |
| montantImpot | Double | Montant de l'impôt calculé | Auto-calculé |
| rangLocalite | Integer | Rang de la localité (1-3) | Oui |
| declare | Boolean | Propriété déclarée | Auto |
| declarationEnLigne | Boolean | Déclaration en ligne | Auto |
| actif | Boolean | Propriété active | Auto |
| proprietaire | Contribuable | Propriétaire | Oui |
| localisation | Point | Coordonnées GPS | Non |

---

## Énumérations

### TypePropriete
- `VI`: Villa
- `AP`: Appartement
- `CH`: Commerce/Hôtel
- `TE`: Terrain
- `DEPOT`: Dépôt/Entrepôt

### RangLocalite
- `1`: Rang 1 (centre-ville, zones premium)
- `2`: Rang 2 (zones intermédiaires)
- `3`: Rang 3 (zones périphériques)

---

## Règles métier

### Calcul de l'impôt
- L'impôt est calculé automatiquement selon le type de propriété, le rang de localité et le type de contribuable
- Les taux sont définis dans le fichier `taux_if.json`
- Pour les villas: `montantImpot = superficie × taux`
- Pour les autres types: `montantImpot = taux fixe`

### Déclaration
- Les propriétés doivent être déclarées pour être taxées
- Déclaration en ligne: du 2 janvier au 1er février
- Déclaration hors ligne: toute l'année à l'administration

### Géolocalisation
- Les coordonnées GPS (latitude/longitude) sont optionnelles
- Utilisées pour la cartographie et l'analyse spatiale

---

## Codes d'erreur

| Code | Description |
|------|-------------|
| PROPRIETES_FETCH_ERROR | Erreur récupération propriétés |
| PROPRIETE_NOT_FOUND | Propriété non trouvée |
| PROPRIETE_CREATE_ERROR | Erreur création |
| PROPRIETE_UPDATE_ERROR | Erreur mise à jour |
| PROPRIETE_DELETE_ERROR | Erreur suppression |
| INVALID_USER | Utilisateur non valide |
