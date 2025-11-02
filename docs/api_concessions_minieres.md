# Documentation API - Gestion des Concessions Minières

## Vue d'ensemble

Gestion des concessions minières, des permis d'exploitation et des redevances associées.

### Base URL
```
/api/concessions
```

## Endpoints
 
### 1. Créer une concession
- **URL**: `/api/concessions`
- **Méthode**: `POST`
- **Rôles autorisés**: `ADMIN`, `GESTIONNAIRE_CONCESSIONS`
- **Corps** (JSON):
  ```json
  {
    "numeroPermis": "PM-2024-001",
    "typeConcession": "EXPLOITATION",
    "dateDebut": "2024-01-01",
    "dateFin": "2034-01-01",
    "superficie": 500.5,
    "localisation": "Province du Katanga",
    "minerais": ["CUIVRE", "COBALT"],
    "titulaire": {
      "nom": "SOCIETE MINIERE",
      "adresse": "123 Avenue des Mines",
      "telephone": "+243999999999"
    },
    "tauxRedevance": 2.5
  }
  ```

### 2. Lister les concessions
- **URL**: `/api/concessions`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `GESTIONNAIRE_CONCESSIONS`, `CONTROLEUR`
- **Paramètres** (optionnels):
  - `page` (défaut: 0)
  - `size` (défaut: 10)
  - `statut` (filtre)
  - `type` (filtre)
  - `search` (recherche textuelle)

### 3. Récupérer une concession
- **URL**: `/api/concessions/{id}`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `GESTIONNAIRE_CONCESSIONS`, `CONTROLEUR`

### 4. Mettre à jour une concession
- **URL**: `/api/concessions/{id}`
- **Méthode**: `PUT`
- **Rôles autorisés**: `ADMIN`, `GESTIONNAIRE_CONCESSIONS`

### 5. Supprimer une concession
- **URL**: `/api/concessions/{id}`
- **Méthode**: `DELETE`
- **Rôles autorisés**: `ADMIN`

### 6. Enregistrer un paiement
- **URL**: `/api/concessions/{id}/paiements`
- **Méthode**: `POST`
- **Rôles autorisés**: `ADMIN`, `CAISSIER`
- **Corps** (JSON):
  ```json
  {
    "montant": 25000.0,
    "datePaiement": "2024-10-27",
    "modePaiement": "VIREMENT_BANCAIRE",
    "reference": "VIR-2024-001",
    "periodeDebut": "2024-01-01",
    "periodeFin": "2024-12-31"
  }
  ```

## Modèles de données

### ConcessionMiniere
| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant |
| numeroPermis | String | Numéro du permis |
| typeConcession | Enum | Type (RECHERCHE, EXPLOITATION, PETITE_MINE) |
| statut | Enum | Statut (ACTIVE, INACTIVE, SUSPENDUE) |
| dateDebut | Date | Date de début |
| dateFin | Date | Date de fin |
| superficie | Double | Superficie en hectares |
| localisation | String | Localisation |
| minerais | String[] | Liste des minerais |
| titulaire | Object | Informations du titulaire |
| tauxRedevance | Double | Taux en % |

### PaiementRedevance
| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant |
| datePaiement | Date | Date du paiement |
| montant | Double | Montant payé |
| modePaiement | Enum | Mode de paiement |
| reference | String | Référence |
| periodeDebut | Date | Début période |
| periodeFin | Date | Fin période |
| statut | Enum | Statut du paiement |

## Codes d'erreur

| Code | Description |
|------|-------------|
| CONCESSION_NOT_FOUND | Concession non trouvée |
| CONCESSION_EXISTS | Numéro de permis déjà utilisé |
| INVALID_DATES | Dates invalides |
| PAYMENT_ERROR | Erreur de paiement |
| UNAUTHORIZED | Accès non autorisé |
| INVALID_STATUS | Statut invalide |
| MISSING_FIELDS | Champs manquants |
| INVALID_MINERAL | Type de minerai invalide |
| PAYMENT_EXISTS | Paiement existe déjà pour cette période |
| CONCESSION_INACTIVE | Concession inactive |
| DOCUMENT_ERROR | Erreur document |
| QUOTA_EXCEEDED | Quota dépassé |
| VALIDATION_ERROR | Erreur de validation |
| SERVICE_UNAVAILABLE | Service indisponible |
| INTERNAL_ERROR | Erreur interne |
