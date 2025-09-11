# Documentation API - Gestion des Paiements

Cette documentation détaille les endpoints disponibles pour la gestion des paiements dans l'API DPRIHKAT.

## Vue d'ensemble

Les paiements représentent les transactions financières effectuées par les contribuables pour régler leurs impôts. Ils sont liés aux taxations et permettent de suivre le statut de règlement des impôts.

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
      "datePaiement": "2025-03-10T14:30:45.123Z",
      "montant": 1200000.00,
      "bordereauBancaire": "BRD-2025-12345",
      "statut": "VALIDE",
      "declaration": {
        "id": "uuid-string",
        "dateDeclaration": "2025-01-15T10:30:45.123Z",
        "montantDeclare": 1000000.00
      },
      "taxation": {
        "id": "uuid-string",
        "dateTaxation": "2025-02-15T10:30:45.123Z",
        "montant": 1200000.00
      }
    },
    "message": "Paiement traité avec succès"
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
    "details": "Message d'erreur détaillé"
  }
}
```

### 2. Récupérer un paiement par ID de déclaration

Récupère les détails d'un paiement pour une déclaration spécifique.

- **URL**: `/api/paiements/declaration/{declarationId}`
- **Méthode**: `GET`
- **Rôles autorisés**: `TAXATEUR`, `RECEVEUR_DES_IMPOTS`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `CONTRIBUABLE`, `ADMIN`
- **Paramètres**:
  - `declarationId` (path): UUID de la déclaration

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "paiement": {
      "id": "uuid-string",
      "datePaiement": "2025-03-10T14:30:45.123Z",
      "montant": 1200000.00,
      "bordereauBancaire": "BRD-2025-12345",
      "statut": "VALIDE",
      "declaration": {
        "id": "uuid-string",
        "dateDeclaration": "2025-01-15T10:30:45.123Z",
        "montantDeclare": 1000000.00
      },
      "taxation": {
        "id": "uuid-string",
        "dateTaxation": "2025-02-15T10:30:45.123Z",
        "montant": 1200000.00
      }
    }
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
  }
}
```

### 3. Récupérer tous les paiements

Récupère la liste de tous les paiements avec pagination et filtrage optionnel par statut.

- **URL**: `/api/paiements`
- **Méthode**: `GET`
- **Rôles autorisés**: `RECEVEUR_DES_IMPOTS`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `ADMIN`
- **Paramètres**:
  - `page` (query, optionnel): Numéro de page (défaut: 0)
  - `size` (query, optionnel): Nombre d'éléments par page (défaut: 10)
  - `statut` (query, optionnel): Statut des paiements à récupérer (EN_ATTENTE, VALIDE, REJETE)

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "paiements": [
      {
        "id": "uuid-string",
        "datePaiement": "2025-03-10T14:30:45.123Z",
        "montant": 1200000.00,
        "bordereauBancaire": "BRD-2025-12345",
        "statut": "VALIDE",
        "declaration": {
          "id": "uuid-string",
          "dateDeclaration": "2025-01-15T10:30:45.123Z",
          "montantDeclare": 1000000.00
        },
        "taxation": {
          "id": "uuid-string",
          "dateTaxation": "2025-02-15T10:30:45.123Z",
          "montant": 1200000.00
        }
      }
    ],
    "currentPage": 0,
    "totalItems": 42,
    "totalPages": 5
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
    "details": "Message d'erreur détaillé"
  }
}
```

## Structure de l'entité Paiement

| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant unique du paiement |
| datePaiement | Date | Date à laquelle le paiement a été effectué |
| montant | Double | Montant du paiement |
| bordereauBancaire | String | Numéro du bordereau bancaire |
| statut | Enum (StatutPaiement) | Statut du paiement (EN_ATTENTE, VALIDE, REJETE) |
| declaration | Declaration | Déclaration associée à ce paiement |
| taxation | Taxation | Taxation associée à ce paiement |

## Règles métier

1. Un paiement est associé à une déclaration et à une taxation.
2. Le montant du paiement doit correspondre au montant de la taxation.
3. Un paiement passe par plusieurs statuts : EN_ATTENTE → VALIDE (ou REJETE).
4. Seuls les utilisateurs avec le rôle RECEVEUR_DES_IMPOTS ou ADMIN peuvent traiter les paiements.
5. Une fois qu'un paiement est validé, le statut de la taxation associée est mis à jour à PAYEE.
6. Le bordereau bancaire est un document qui atteste du paiement effectué par le contribuable.
