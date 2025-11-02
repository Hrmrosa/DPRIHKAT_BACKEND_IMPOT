# Documentation API - Gestion des Apurements

Cette documentation détaille les endpoints disponibles pour la gestion des apurements dans l'API DPRIHKAT.

---

## Vue d'ensemble

Les apurements permettent de valider que les contribuables sont à jour de leurs obligations fiscales.

### Base URL
```
/api/apurements
```

---

## Endpoints

### 1. Créer un apurement

Crée un nouvel apurement pour une déclaration.

- **URL**: `/api/apurements/create/{declarationId}`
- **Méthode**: `POST`
- **Rôles autorisés**: `APUREUR`, `RECEVEUR_DES_IMPOTS`, `ADMIN`
- **Paramètres**:
  - `declarationId` (path): UUID de la déclaration
  - `type` (query): Type d'apurement

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "apurement": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "declaration": {
        "id": "550e8400-e29b-41d4-a716-446655440001",
        "contribuable": {
          "nom": "KABILA",
          "numeroIdentificationContribuable": "NIF-123456789"
        },
        "exercice": 2024
      },
      "type": "TOTAL",
      "statut": "EN_ATTENTE",
      "dateCreation": "2024-10-27T12:00:00",
      "agent": {
        "id": "550e8400-e29b-41d4-a716-446655440010",
        "nomComplet": "MUKENDI Jean"
      }
    },
    "message": "Apurement créé avec succès"
  }
}
```

---

### 2. Valider un apurement

Valide un apurement en attente.

- **URL**: `/api/apurements/validate/{apurementId}`
- **Méthode**: `POST`
- **Rôles autorisés**: `APUREUR`, `RECEVEUR_DES_IMPOTS`, `ADMIN`
- **Paramètres**:
  - `apurementId` (path): UUID de l'apurement

---

### 3. Récupérer un apurement par déclaration

Récupère l'apurement associé à une déclaration.

- **URL**: `/api/apurements/declaration/{declarationId}`
- **Méthode**: `GET`
- **Rôles autorisés**: `APUREUR`, `RECEVEUR_DES_IMPOTS`, `TAXATEUR`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `ADMIN`

---

### 4. Récupérer tous les apurements

- **URL**: `/api/apurements`
- **Méthode**: `GET`
- **Rôles autorisés**: `APUREUR`, `RECEVEUR_DES_IMPOTS`, `DIRECTEUR`, `ADMIN`

---

## Structure de l'entité Apurement

| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant unique |
| declaration | Declaration | Déclaration concernée |
| type | Enum | Type d'apurement |
| statut | Enum | Statut de l'apurement |
| dateCreation | DateTime | Date de création |
| dateValidation | DateTime | Date de validation |
| agent | Utilisateur | Agent créateur |
| validateur | Utilisateur | Agent validateur |
| observations | String | Observations |

---

## Énumérations

### TypeApurement
- `TOTAL`: Apurement total (toutes taxes payées)
- `PARTIEL`: Apurement partiel
- `EXONERATION`: Exonération fiscale

### StatutApurement
- `EN_ATTENTE`: En attente de validation
- `VALIDE`: Validé
- `REJETE`: Rejeté

---

## Règles métier

### Conditions de création
- La déclaration doit être validée
- Toutes les taxations doivent être payées (pour apurement total)
- Vérification automatique des paiements

### Validation
- Seuls les apureurs et receveurs peuvent valider
- Vérification de la conformité avant validation
- Génération automatique d'un certificat d'apurement

### Certificat
- Un certificat est généré automatiquement après validation
- Le certificat atteste que le contribuable est en règle
- Valable pour une période déterminée
