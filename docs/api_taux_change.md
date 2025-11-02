# Documentation API - Gestion des Taux de Change

Cette documentation détaille les endpoints disponibles pour la gestion des taux de change dans l'API DPRIHKAT.

---

## Vue d'ensemble

Les taux de change permettent de gérer les conversions entre devises (principalement USD et CDF).

### Base URL
```
/api/taux-change
```

---

## Endpoints

### 1. Récupérer tous les taux de change actifs

Récupère uniquement les taux de change actuellement actifs.

- **URL**: `/api/taux-change`
- **Méthode**: `GET`
- **Rôles autorisés**: `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `ADMIN`, `DIRECTEUR`

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "tauxChanges": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "deviseSource": "USD",
        "deviseDestination": "CDF",
        "taux": 2450.0,
        "dateEffective": "2024-10-01",
        "actif": true,
        "agent": {
          "id": "550e8400-e29b-41d4-a716-446655440001",
          "nomComplet": "MUKENDI Jean"
        }
      }
    ]
  }
}
```

---

### 2. Récupérer l'historique complet des taux

Récupère tous les taux de change (actifs et inactifs).

- **URL**: `/api/taux-change/historique`
- **Méthode**: `GET`
- **Rôles autorisés**: `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `ADMIN`, `DIRECTEUR`

---

### 3. Récupérer l'historique par paire de devises

Récupère l'historique des taux pour une paire de devises spécifique.

- **URL**: `/api/taux-change/historique/devises`
- **Méthode**: `GET`
- **Rôles autorisés**: `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `ADMIN`, `DIRECTEUR`
- **Paramètres**:
  - `deviseSource` (query): Devise source (USD, CDF, EUR)
  - `deviseDestination` (query): Devise destination

#### Exemple de requête

```
GET /api/taux-change/historique/devises?deviseSource=USD&deviseDestination=CDF
```

---

### 4. Récupérer le taux actif pour une paire

Récupère le taux de change actuellement actif pour une paire de devises.

- **URL**: `/api/taux-change/actif`
- **Méthode**: `GET`
- **Rôles autorisés**: `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `ADMIN`, `DIRECTEUR`
- **Paramètres**:
  - `deviseSource` (query): Devise source
  - `deviseDestination` (query): Devise destination

---

### 5. Créer un nouveau taux de change

Crée un nouveau taux de change et désactive automatiquement l'ancien.

- **URL**: `/api/taux-change`
- **Méthode**: `POST`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `CHEF_DE_DIVISION`

#### Corps de la requête

```json
{
  "deviseSource": "USD",
  "deviseDestination": "CDF",
  "taux": 2450.0,
  "dateEffective": "2024-10-27"
}
```

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "tauxChange": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "deviseSource": "USD",
      "deviseDestination": "CDF",
      "taux": 2450.0,
      "dateEffective": "2024-10-27",
      "actif": true
    },
    "message": "Taux de change créé avec succès"
  }
}
```

---

### 6. Désactiver un taux de change

Désactive manuellement un taux de change.

- **URL**: `/api/taux-change/{id}/desactiver`
- **Méthode**: `POST`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`

---

### 7. Convertir un montant

Convertit un montant d'une devise à une autre.

- **URL**: `/api/taux-change/convertir`
- **Méthode**: `GET`
- **Rôles autorisés**: Tous (authentifiés)
- **Paramètres**:
  - `montant` (query): Montant à convertir
  - `deviseSource` (query): Devise source
  - `deviseDestination` (query): Devise destination

#### Exemple de requête

```
GET /api/taux-change/convertir?montant=100&deviseSource=USD&deviseDestination=CDF
```

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "montantSource": 100.0,
    "deviseSource": "USD",
    "montantDestination": 245000.0,
    "deviseDestination": "CDF",
    "tauxUtilise": 2450.0,
    "dateConversion": "2024-10-27T12:00:00"
  }
}
```

---

## Structure de l'entité TauxChange

| Champ | Type | Description | Obligatoire |
|-------|------|-------------|-------------|
| id | UUID | Identifiant unique | Auto-généré |
| deviseSource | Enum | Devise source | Oui |
| deviseDestination | Enum | Devise destination | Oui |
| taux | Double | Taux de conversion | Oui |
| dateEffective | Date | Date d'application | Oui |
| actif | Boolean | Taux actif | Auto |
| agent | Utilisateur | Agent créateur | Auto |
| dateCreation | DateTime | Date de création | Auto |

---

## Énumérations

### Devise
- `USD`: Dollar américain
- `CDF`: Franc congolais
- `EUR`: Euro

---

## Règles métier

### Activation/Désactivation automatique
- Lors de la création d'un nouveau taux pour une paire de devises, l'ancien taux est automatiquement désactivé
- Un seul taux peut être actif par paire de devises à un moment donné

### Date effective
- Le taux s'applique à partir de la date effective
- Les conversions utilisent toujours le taux actif à la date de conversion
- L'historique est conservé pour traçabilité

### Conversion
- La conversion utilise le taux actif au moment de la conversion
- Formule: `montantDestination = montantSource × taux`
- Arrondi à 2 décimales pour les montants

### Traçabilité
- Chaque taux est lié à l'agent qui l'a créé
- Date de création enregistrée automatiquement
- Historique complet conservé

---

## Codes d'erreur

| Code | Description |
|------|-------------|
| TAUX_CHANGE_FETCH_ERROR | Erreur récupération taux |
| TAUX_CHANGE_NOT_FOUND | Taux non trouvé |
| TAUX_CHANGE_CREATE_ERROR | Erreur création |
| TAUX_CHANGE_DESACTIVATION_ERROR | Erreur désactivation |
| CONVERSION_ERROR | Erreur conversion |
| INVALID_DEVISE | Devise invalide |
