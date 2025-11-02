# Documentation API - Gestion des Divisions

Cette documentation détaille les endpoints disponibles pour la gestion des divisions dans l'API DPRIHKAT.

---

## Vue d'ensemble

Les divisions sont les entités administratives de premier niveau de la DPRIHKAT.

### Base URL
```
/api/divisions
```

---

## Endpoints

### 1. Récupérer toutes les divisions (paginé)

- **URL**: `/api/divisions`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`
- **Paramètres**:
  - `page`, `size`, `sortBy`, `sortDir` (mêmes que bureaux)

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "divisions": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "code": "DF",
        "nom": "Division Fiscale",
        "bureaux": [
          {
            "id": "550e8400-e29b-41d4-a716-446655440001",
            "code": "BC01",
            "nom": "Bureau Central"
          }
        ],
        "actif": true
      }
    ],
    "currentPage": 0,
    "totalItems": 5,
    "totalPages": 1
  }
}
```

---

### 2. Récupérer une division par ID

- **URL**: `/api/divisions/{id}`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`

---

### 3. Créer une division

- **URL**: `/api/divisions`
- **Méthode**: `POST`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`

#### Corps de la requête

```json
{
  "code": "DF",
  "nom": "Division Fiscale"
}
```

---

### 4. Mettre à jour une division

- **URL**: `/api/divisions/{id}`
- **Méthode**: `PUT`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`

---

### 5. Supprimer une division

- **URL**: `/api/divisions/{id}`
- **Méthode**: `DELETE`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`

---

## Structure de l'entité Division

| Champ | Type | Description | Obligatoire |
|-------|------|-------------|-------------|
| id | UUID | Identifiant unique | Auto-généré |
| code | String | Code unique de la division | Oui |
| nom | String | Nom de la division | Oui |
| bureaux | List | Liste des bureaux | Auto |
| actif | Boolean | Division active | Auto |

---

## Règles métier

### Hiérarchie
- Une division peut avoir plusieurs bureaux
- Un chef de division supervise tous les bureaux
- Les statistiques sont agrégées au niveau division

### Code unique
- Le code doit être unique
- Format: 2-3 caractères majuscules
- Exemples: DF (Division Fiscale), DR (Division Recouvrement)
