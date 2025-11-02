# Documentation API - Gestion des Bureaux

Cette documentation détaille les endpoints disponibles pour la gestion des bureaux dans l'API DPRIHKAT.

---

## Vue d'ensemble
 
Les bureaux sont des entités administratives rattachées aux divisions de la DPRIHKAT.

### Base URL
```
/api/bureaux
```

---

## Endpoints

### 1. Récupérer tous les bureaux (paginé)

- **URL**: `/api/bureaux`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`
- **Paramètres**:
  - `page` (query, optionnel): Numéro de page (défaut: 0)
  - `size` (query, optionnel): Taille de page (défaut: 10)
  - `sortBy` (query, optionnel): Champ de tri (défaut: nom)
  - `sortDir` (query, optionnel): Direction du tri (asc/desc, défaut: asc)

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "bureaux": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "code": "BC01",
        "nom": "Bureau Central",
        "division": {
          "id": "550e8400-e29b-41d4-a716-446655440001",
          "code": "DF",
          "nom": "Division Fiscale"
        },
        "adresse": "Avenue Mobutu, Lubumbashi",
        "telephone": "+243820123456",
        "email": "bureau.central@dprihkat.cd",
        "actif": true
      }
    ],
    "currentPage": 0,
    "totalItems": 15,
    "totalPages": 2
  }
}
```

---

### 2. Récupérer un bureau par ID

- **URL**: `/api/bureaux/{id}`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`

---

### 3. Créer un bureau

- **URL**: `/api/bureaux`
- **Méthode**: `POST`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`

#### Corps de la requête

```json
{
  "code": "BC01",
  "nom": "Bureau Central",
  "divisionId": "550e8400-e29b-41d4-a716-446655440001",
  "adresse": "Avenue Mobutu, Lubumbashi",
  "telephone": "+243820123456",
  "email": "bureau.central@dprihkat.cd"
}
```

---

### 4. Mettre à jour un bureau

- **URL**: `/api/bureaux/{id}`
- **Méthode**: `PUT`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`

---

### 5. Supprimer un bureau

- **URL**: `/api/bureaux/{id}`
- **Méthode**: `DELETE`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`

---

## Structure de l'entité Bureau

| Champ | Type | Description | Obligatoire |
|-------|------|-------------|-------------|
| id | UUID | Identifiant unique | Auto-généré |
| code | String | Code unique du bureau | Oui |
| nom | String | Nom du bureau | Oui |
| division | Division | Division de rattachement | Oui |
| adresse | String | Adresse physique | Non |
| telephone | String | Numéro de téléphone | Non |
| email | String | Adresse email | Non |
| actif | Boolean | Bureau actif | Auto |

---

## Règles métier

### Code unique
- Le code doit être unique dans le système
- Format recommandé: 2-4 caractères majuscules + chiffres
- Exemples: BC01, BF02, BL03

### Rattachement
- Chaque bureau doit être rattaché à une division
- Un bureau ne peut appartenir qu'à une seule division
- La division doit exister avant la création du bureau

### Agents
- Un bureau peut avoir plusieurs agents
- Un chef de bureau est désigné parmi les agents
- Les agents héritent des permissions du bureau
