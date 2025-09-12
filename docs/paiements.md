# Documentation API - Gestion des Paiements

Cette documentation détaille les endpoints disponibles pour la gestion des paiements dans l'API DPRIHKAT.

## Vue d'ensemble

Les paiements représentent les transactions financières effectuées par les contribuables pour régler leurs impôts. Ils sont liés aux taxations et permettent de suivre le statut de règlement des impôts.

Chaque paiement inclut désormais des informations enrichies :
- Informations de base du paiement (date, montant, bordereau bancaire)
- Nom du contribuable concerné
- Date de la taxation associée
- Montant et devise de la taxation
- Nom de l'agent taxateur
- Type d'impôt concerné
- Exercice fiscal

## Base URL

```
/api/paiements
```

## Endpoints

### 1. Traiter un paiement

Permet d'enregistrer un nouveau paiement pour une déclaration.

- **URL**: `/api/paiements/process/{declarationId}`
- **Méthode**: `POST`
- **Rôles autorisés**: `RECEVEUR_DES_IMPOTS`, `ADMIN`
- **Paramètres**:
  - `declarationId` (path): UUID de la déclaration concernée
  - `bordereauBancaire` (query): Numéro du bordereau bancaire
  - `montant` (query): Montant du paiement

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "paiement": {
      "id": "uuid-string",
      "date": "2025-03-10T14:30:45.123Z",
      "montant": 1200000.00,
      "mode": "VIREMENT",
      "statut": "VALIDE",
      "bordereauBancaire": "BRD-2025-12345",
      "actif": true,
      "nomContribuable": "Société Minière du Katanga",
      "dateTaxation": "2025-02-15T10:30:45.123Z",
      "devise": "USD",
      "nomTaxateur": "Mutombo Jean",
      "typeImpot": "IF",
      "exerciceFiscal": "2025",
      "taxation": {
        "id": "uuid-string",
        "numeroTaxation": "DPRI-0001-IFBRF-2025"
      }
    },
    "message": "Paiement traité avec succès"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-03-10T14:30:45.123Z"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "PAYMENT_PROCESSING_ERROR",
    "message": "Erreur lors du traitement du paiement",
    "details": "La déclaration spécifiée n'existe pas"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-03-10T14:30:45.123Z"
  }
}
```

### 2. Récupérer un paiement par déclaration

Récupère les informations d'un paiement associé à une déclaration spécifique.

- **URL**: `/api/paiements/declaration/{declarationId}`
- **Méthode**: `GET`
- **Rôles autorisés**: `TAXATEUR`, `RECEVEUR_DES_IMPOTS`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `CONTRIBUABLE`, `ADMIN`
- **Paramètres**:
  - `declarationId` (path): UUID de la déclaration concernée

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "paiement": {
      "id": "uuid-string",
      "date": "2025-03-10T14:30:45.123Z",
      "montant": 1200000.00,
      "mode": "VIREMENT",
      "statut": "VALIDE",
      "bordereauBancaire": "BRD-2025-12345",
      "actif": true,
      "nomContribuable": "Société Minière du Katanga",
      "dateTaxation": "2025-02-15T10:30:45.123Z",
      "devise": "USD",
      "nomTaxateur": "Mutombo Jean",
      "typeImpot": "IF",
      "exerciceFiscal": "2025",
      "taxation": {
        "id": "uuid-string",
        "numeroTaxation": "DPRI-0001-IFBRF-2025"
      }
    }
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-03-10T14:35:45.123Z"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "PAYMENT_NOT_FOUND",
    "message": "Paiement non trouvé",
    "details": "Aucun paiement trouvé pour cette déclaration"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-03-10T14:35:45.123Z"
  }
}
```

### 3. Récupérer tous les paiements

Récupère la liste de tous les paiements avec possibilité de filtrer par statut.

- **URL**: `/api/paiements`
- **Méthode**: `GET`
- **Rôles autorisés**: `RECEVEUR_DES_IMPOTS`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `ADMIN`
- **Paramètres**:
  - `page` (optionnel): Numéro de page (commence à 0)
  - `size` (optionnel): Nombre d'éléments par page (par défaut 10)
  - `statut` (optionnel): Filtre par statut (EN_ATTENTE, VALIDE, REJETE)

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "paiements": [
      {
        "id": "uuid-string-1",
        "date": "2025-03-10T14:30:45.123Z",
        "montant": 1200000.00,
        "mode": "VIREMENT",
        "statut": "VALIDE",
        "bordereauBancaire": "BRD-2025-12345",
        "actif": true,
        "nomContribuable": "Société Minière du Katanga",
        "dateTaxation": "2025-02-15T10:30:45.123Z",
        "devise": "USD",
        "nomTaxateur": "Mutombo Jean",
        "typeImpot": "IF",
        "exerciceFiscal": "2025",
        "taxation": {
          "id": "uuid-string-1",
          "numeroTaxation": "DPRI-0001-IFBRF-2025"
        }
      },
      {
        "id": "uuid-string-2",
        "date": "2025-03-11T09:15:30.456Z",
        "montant": 850000.00,
        "mode": "ESPECES",
        "statut": "VALIDE",
        "bordereauBancaire": "BRD-2025-12346",
        "actif": true,
        "nomContribuable": "Entreprise de Construction Lubumbashi",
        "dateTaxation": "2025-02-20T11:45:22.789Z",
        "devise": "USD",
        "nomTaxateur": "Kabila Pierre",
        "typeImpot": "ICM",
        "exerciceFiscal": "2025",
        "taxation": {
          "id": "uuid-string-2",
          "numeroTaxation": "DPRI-0002-ICMBRF-2025"
        }
      }
    ],
    "currentPage": 0,
    "totalItems": 2,
    "totalPages": 1
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-03-12T10:00:00.000Z"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "PAYMENT_FETCH_ERROR",
    "message": "Erreur lors de la récupération des paiements",
    "details": "Erreur de connexion à la base de données"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-03-12T10:00:00.000Z"
  }
}
```

## Structure des données

### Paiement

| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant unique du paiement |
| date | Date | Date du paiement |
| montant | Double | Montant du paiement |
| mode | ModePaiement | Mode de paiement (ESPECES, CHEQUE, VIREMENT) |
| statut | StatutPaiement | Statut du paiement (EN_ATTENTE, VALIDE, REJETE) |
| bordereauBancaire | String | Numéro du bordereau bancaire |
| actif | Boolean | Indique si le paiement est actif |
| nomContribuable | String | Nom du contribuable concerné |
| dateTaxation | Date | Date de la taxation associée |
| devise | Devise | Devise de la taxation (USD, CDF) |
| nomTaxateur | String | Nom de l'agent taxateur |
| typeImpot | TypeImpot | Type d'impôt (IF, IRL, ICM, IRV, RL) |
| exerciceFiscal | String | Exercice fiscal concerné |
| taxation | TaxationDTO | Informations sur la taxation associée |

### TaxationDTO

| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant unique de la taxation |
| numeroTaxation | String | Numéro de taxation au format DPRI-0001-typeimpotcodeBureauTaxateur-annee |

## Règles métier

1. Un paiement est associé à une déclaration et à une taxation.
2. Le montant du paiement doit correspondre au montant de la taxation.
3. Un paiement passe par plusieurs statuts : EN_ATTENTE → VALIDE (ou REJETE).
4. Seul un receveur des impôts ou un administrateur peut traiter un paiement.
5. Les informations enrichies (nom du contribuable, date de taxation, etc.) sont automatiquement récupérées à partir de la taxation associée.
