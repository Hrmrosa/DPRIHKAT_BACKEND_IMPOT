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
- **Informations d'apurement associé** (nouveau)

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
      "nomContribuable": "SOCIETE MINIERE XYZ",
      "dateTaxation": "2025-03-01T10:15:30.000Z",
      "devise": "USD",
      "nomTaxateur": "Jean Dupont",
      "typeImpot": "ICM",
      "exerciceFiscal": "2025",
      "taxation": {
        "id": "uuid-string",
        "numeroTaxation": "DPRI-0001-ICMBRF-2025"
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
    "details": "Le montant du paiement ne correspond pas au montant de la taxation"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-03-10T14:30:45.123Z"
  }
}
```

### 2. Récupérer un paiement par déclaration

Permet de récupérer les informations d'un paiement associé à une déclaration.

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
      "nomContribuable": "SOCIETE MINIERE XYZ",
      "dateTaxation": "2025-03-01T10:15:30.000Z",
      "devise": "USD",
      "nomTaxateur": "Jean Dupont",
      "typeImpot": "ICM",
      "exerciceFiscal": "2025",
      "taxation": {
        "id": "uuid-string",
        "numeroTaxation": "DPRI-0001-ICMBRF-2025"
      },
      "apurement": {
        "id": "uuid-string",
        "dateDemande": "2025-03-15T09:30:00.000Z",
        "dateValidation": "2025-03-16T14:45:00.000Z",
        "type": "TOTAL",
        "montantApure": 1200000.00,
        "motif": "Paiement complet de la taxation",
        "statut": "VALIDE",
        "provisoire": false,
        "declarationPayee": true,
        "nomAgent": "Pierre Martin",
        "nomAgentValidateur": "Marie Dubois"
      }
    }
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
    "code": "PAYMENT_RETRIEVAL_ERROR",
    "message": "Erreur lors de la récupération du paiement",
    "details": "Déclaration non trouvée"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-03-12T10:00:00.000Z"
  }
}
```

### 3. Récupérer tous les paiements

Permet de récupérer la liste de tous les paiements.

- **URL**: `/api/paiements`
- **Méthode**: `GET`
- **Rôles autorisés**: `RECEVEUR_DES_IMPOTS`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `ADMIN`

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
        "nomContribuable": "SOCIETE MINIERE XYZ",
        "dateTaxation": "2025-03-01T10:15:30.000Z",
        "devise": "USD",
        "nomTaxateur": "Jean Dupont",
        "typeImpot": "ICM",
        "exerciceFiscal": "2025",
        "taxation": {
          "id": "uuid-string-1",
          "numeroTaxation": "DPRI-0001-ICMBRF-2025"
        },
        "apurement": {
          "id": "uuid-string-1",
          "dateDemande": "2025-03-15T09:30:00.000Z",
          "dateValidation": "2025-03-16T14:45:00.000Z",
          "type": "TOTAL",
          "montantApure": 1200000.00,
          "motif": "Paiement complet de la taxation",
          "statut": "VALIDE",
          "provisoire": false,
          "declarationPayee": true,
          "nomAgent": "Pierre Martin",
          "nomAgentValidateur": "Marie Dubois"
        }
      },
      {
        "id": "uuid-string-2",
        "date": "2025-03-11T09:15:30.456Z",
        "montant": 500000.00,
        "mode": "CHEQUE",
        "statut": "VALIDE",
        "bordereauBancaire": "BRD-2025-12346",
        "actif": true,
        "nomContribuable": "ENTREPRISE ABC",
        "dateTaxation": "2025-03-05T08:30:00.000Z",
        "devise": "USD",
        "nomTaxateur": "Marie Martin",
        "typeImpot": "IRL",
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

### 4. Récupérer les paiements paginés avec filtres

Permet de récupérer les paiements avec pagination et filtres optionnels.

- **URL**: `/api/paiements/paginated`
- **Méthode**: `GET`
- **Rôles autorisés**: `TAXATEUR`, `RECEVEUR_DES_IMPOTS`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `APUREUR`, `CONTRIBUABLE`, `ADMIN`
- **Paramètres**:
  - `statut` (query, optionnel): Statut du paiement (VALIDE, EN_ATTENTE, REJETE)
  - `startDate` (query, optionnel): Date de début (format: yyyy-MM-dd)
  - `endDate` (query, optionnel): Date de fin (format: yyyy-MM-dd)
  - `page` (query, défaut: 0): Numéro de page
  - `size` (query, défaut: 10): Nombre d'éléments par page
  - `sortBy` (query, défaut: "date"): Champ de tri (date, montant, statut, etc.)
  - `sortDirection` (query, défaut: "desc"): Direction du tri (asc, desc)

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
        "nomContribuable": "SOCIETE MINIERE XYZ",
        "dateTaxation": "2025-03-01T10:15:30.000Z",
        "devise": "USD",
        "nomTaxateur": "Jean Dupont",
        "typeImpot": "ICM",
        "exerciceFiscal": "2025",
        "taxation": {
          "id": "uuid-string-1",
          "numeroTaxation": "DPRI-0001-ICMBRF-2025"
        },
        "apurement": {
          "id": "uuid-string-1",
          "dateDemande": "2025-03-15T09:30:00.000Z",
          "dateValidation": "2025-03-16T14:45:00.000Z",
          "type": "TOTAL",
          "montantApure": 1200000.00,
          "motif": "Paiement complet de la taxation",
          "statut": "VALIDE",
          "provisoire": false,
          "declarationPayee": true,
          "nomAgent": "Pierre Martin",
          "nomAgentValidateur": "Marie Dubois"
        }
      }
    ],
    "currentPage": 0,
    "totalItems": 50,
    "totalPages": 5
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
    "message": "Erreur lors de la récupération des paiements paginés",
    "details": "Paramètre de tri invalide"
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
| apurement | ApurementDTO | Informations sur l'apurement associé (si existant) |
| declarationId | UUID | Identifiant de la déclaration associée |

### TaxationDTO

| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant unique de la taxation |
| numeroTaxation | String | Numéro de taxation au format DPRI-0001-typeimpotcodeBureauTaxateur-annee |

### ApurementDTO

| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant unique de l'apurement |
| dateDemande | Date | Date de la demande d'apurement |
| dateValidation | Date | Date de validation de l'apurement |
| type | TypeApurement | Type d'apurement (TOTAL, PARTIEL) |
| montantApure | Double | Montant apuré |
| motif | String | Motif de l'apurement |
| motifRejet | String | Motif de rejet (si applicable) |
| statut | StatutApurement | Statut de l'apurement (EN_ATTENTE, VALIDE, REJETE) |
| provisoire | Boolean | Indique si l'apurement est provisoire |
| declarationPayee | Boolean | Indique si la déclaration a été payée |
| agentId | UUID | Identifiant de l'agent qui a initié l'apurement |
| nomAgent | String | Nom de l'agent qui a initié l'apurement |
| agentValidateurId | UUID | Identifiant de l'agent validateur |
| nomAgentValidateur | String | Nom de l'agent validateur |

## Règles métier

1. Un paiement est associé à une déclaration et à une taxation.
2. Le montant du paiement doit correspondre au montant de la taxation.
3. Un paiement passe par plusieurs statuts : EN_ATTENTE → VALIDE (ou REJETE).
4. Seul un receveur des impôts ou un administrateur peut traiter un paiement.
5. Les informations enrichies (nom du contribuable, date de taxation, etc.) sont automatiquement récupérées à partir de la taxation associée.
6. Un paiement peut être associé à un apurement qui indique comment le paiement a été traité.

## Exemples d'utilisation

### Récupérer les paiements paginés avec filtres

```bash
curl -X GET "http://localhost:8080/api/paiements/paginated?statut=VALIDE&startDate=2025-01-01&endDate=2025-09-21&page=0&size=20&sortBy=montant&sortDirection=desc" \
-H "Authorization: Bearer [JWT_TOKEN]"
```

### Récupérer la deuxième page des paiements

```bash
curl -X GET "http://localhost:8080/api/paiements/paginated?page=1&size=10" \
-H "Authorization: Bearer [JWT_TOKEN]"
