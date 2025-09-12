# Documentation API - Gestion des Taxations

Cette documentation détaille les endpoints disponibles pour la gestion des taxations dans l'API DPRIHKAT.

## Vue d'ensemble

Les taxations représentent les impôts calculés et appliqués sur les propriétés déclarées par les contribuables. Elles sont générées à partir des déclarations validées et permettent de suivre le processus de recouvrement des impôts.

Chaque taxation possède :
- Un identifiant unique (UUID)
- Un numéro de taxation au format `DPRI-0001-typeimpotcodeBureauTaxateur-annee`
- Un code QR pointant vers l'URL d'impression de la taxation
- Une devise (USD par défaut, avec possibilité de conversion en CDF)
- Des informations bancaires (nom de la banque, numéro de compte, intitulé du compte)

## Base URL

```
/api/taxations
```

## Endpoints

### 1. Récupérer toutes les taxations

Récupère la liste de toutes les taxations enregistrées dans le système.

- **URL**: `/api/taxations`
- **Méthode**: `GET`
- **Rôles autorisés**: `TAXATEUR`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `ADMIN`
- **Paramètres**: 
  - `page` (optionnel): Numéro de page (commence à 0)
  - `size` (optionnel): Nombre d'éléments par page (par défaut 10)
  - `sortBy` (optionnel): Champ de tri (par défaut dateTaxation)
  - `sortDir` (optionnel): Direction du tri (asc/desc, par défaut desc)

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "totalItems": 38,
    "taxations": [
      {
        "id": "1ce73eff-cac9-4ed5-9670-ee40240a0335",
        "dateTaxation": "2025-09-10T05:36:47.378+00:00",
        "montant": 6400.203021963628,
        "exercice": "2025",
        "statut": "VALIDEE",
        "typeImpot": "IF",
        "exoneration": false,
        "motifExoneration": null,
        "dateEcheance": null,
        "actif": true,
        "codeQR": "http://localhost:3000/print/taxation/1ce73eff-cac9-4ed5-9670-ee40240a0335",
        "numeroTaxation": "DPRI-0001-IFBRF-2025",
        "devise": "USD",
        "nomBanque": "Banque Centrale du Congo",
        "numeroCompte": "00123456789",
        "intituleCompte": "DGRK - Recettes Fiscales Foncières",
        "nomAgent": "Mutombo Jean",
        "declaration": {
          "id": "uuid-string",
          "dateDeclaration": "2025-01-15T10:30:45.123Z",
          "statut": "VALIDEE"
        },
        "propriete": {
          "id": "7e9501cb-3205-4599-bbc0-f0a887aab4a1",
          "type": "VILLA",
          "localite": "Kenya",
          "superficie": 500.0,
          "adresse": "Av. Kivu 44, Kenya"
        },
        "contribuable": {
          "id": "uuid-string",
          "nom": "Nom du contribuable",
          "adressePrincipale": "Adresse principale du contribuable",
          "telephonePrincipal": "+243123456789",
          "email": "contribuable@example.com"
        }
      }
    ],
    "totalPages": 4,
    "currentPage": 0
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-12T07:55:47.410243835"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "TAXATION_FETCH_ERROR",
    "message": "Erreur lors de la récupération des taxations",
    "details": "Message d'erreur détaillé"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-12T07:55:47.410243835"
  }
}
```

### 2. Récupérer toutes les taxations actives

Récupère la liste de toutes les taxations actives dans le système.

- **URL**: `/api/taxations/actives`
- **Méthode**: `GET`
- **Rôles autorisés**: `TAXATEUR`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `ADMIN`
- **Paramètres**: Aucun

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "taxations": [
      {
        "id": "1ce73eff-cac9-4ed5-9670-ee40240a0335",
        "dateTaxation": "2025-09-10T05:36:47.378+00:00",
        "montant": 6400.203021963628,
        "exercice": "2025",
        "statut": "VALIDEE",
        "typeImpot": "IF",
        "exoneration": false,
        "motifExoneration": null,
        "dateEcheance": null,
        "actif": true,
        "codeQR": "http://localhost:3000/print/taxation/1ce73eff-cac9-4ed5-9670-ee40240a0335",
        "numeroTaxation": "DPRI-0001-IFBRF-2025",
        "devise": "USD",
        "nomBanque": "Banque Centrale du Congo",
        "numeroCompte": "00123456789",
        "intituleCompte": "DGRK - Recettes Fiscales Foncières",
        "nomAgent": "Mutombo Jean",
        "declaration": {
          "id": "uuid-string",
          "dateDeclaration": "2025-01-15T10:30:45.123Z",
          "statut": "VALIDEE"
        },
        "propriete": {
          "id": "7e9501cb-3205-4599-bbc0-f0a887aab4a1",
          "type": "VILLA",
          "localite": "Kenya",
          "superficie": 500.0,
          "adresse": "Av. Kivu 44, Kenya"
        },
        "contribuable": {
          "id": "uuid-string",
          "nom": "Nom du contribuable",
          "adressePrincipale": "Adresse principale du contribuable",
          "telephonePrincipal": "+243123456789",
          "email": "contribuable@example.com"
        }
      }
    ]
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-12T07:55:47.410243835"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "TAXATION_FETCH_ERROR",
    "message": "Erreur lors de la récupération des taxations actives",
    "details": "Message d'erreur détaillé"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-12T07:55:47.410243835"
  }
}
```

### 3. Récupérer une taxation par ID

Récupère les détails d'une taxation spécifique par son identifiant.

- **URL**: `/api/taxations/{id}`
- **Méthode**: `GET`
- **Rôles autorisés**: `TAXATEUR`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `ADMIN`
- **Paramètres**: 
  - `id` (path parameter): UUID de la taxation

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "taxation": {
      "id": "1ce73eff-cac9-4ed5-9670-ee40240a0335",
      "dateTaxation": "2025-09-10T05:36:47.378+00:00",
      "montant": 6400.203021963628,
      "exercice": "2025",
      "statut": "VALIDEE",
      "typeImpot": "IF",
      "exoneration": false,
      "motifExoneration": null,
      "dateEcheance": null,
      "actif": true,
      "codeQR": "http://localhost:3000/print/taxation/1ce73eff-cac9-4ed5-9670-ee40240a0335",
      "numeroTaxation": "DPRI-0001-IFBRF-2025",
      "devise": "USD",
      "nomBanque": "Banque Centrale du Congo",
      "numeroCompte": "00123456789",
      "intituleCompte": "DGRK - Recettes Fiscales Foncières",
      "nomAgent": "Mutombo Jean",
      "declaration": {
        "id": "uuid-string",
        "dateDeclaration": "2025-01-15T10:30:45.123Z",
        "statut": "VALIDEE"
      },
      "propriete": {
        "id": "7e9501cb-3205-4599-bbc0-f0a887aab4a1",
        "type": "VILLA",
        "localite": "Kenya",
        "superficie": 500.0,
        "adresse": "Av. Kivu 44, Kenya"
      },
      "contribuable": {
        "id": "uuid-string",
        "nom": "Nom du contribuable",
        "adressePrincipale": "Adresse principale du contribuable",
        "telephonePrincipal": "+243123456789",
        "email": "contribuable@example.com"
      }
    }
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-12T07:55:47.410243835"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "TAXATION_NOT_FOUND",
    "message": "Taxation non trouvée",
    "details": "Aucune taxation avec l'ID fourni"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-12T07:55:47.410243835"
  }
}
```

### 4. Récupérer les taxations par contribuable

Récupère la liste des taxations pour un contribuable spécifique.

- **URL**: `/api/taxations/contribuable/{contribuableId}`
- **Méthode**: `GET`
- **Rôles autorisés**: `TAXATEUR`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `ADMIN`, `CONTRIBUABLE`
- **Paramètres**: 
  - `contribuableId` (path parameter): UUID du contribuable

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "taxations": [
      {
        "id": "1ce73eff-cac9-4ed5-9670-ee40240a0335",
        "dateTaxation": "2025-09-10T05:36:47.378+00:00",
        "montant": 6400.203021963628,
        "exercice": "2025",
        "statut": "VALIDEE",
        "typeImpot": "IF",
        "exoneration": false,
        "motifExoneration": null,
        "dateEcheance": null,
        "actif": true,
        "codeQR": "http://localhost:3000/print/taxation/1ce73eff-cac9-4ed5-9670-ee40240a0335",
        "numeroTaxation": "DPRI-0001-IFBRF-2025",
        "devise": "USD",
        "nomBanque": "Banque Centrale du Congo",
        "numeroCompte": "00123456789",
        "intituleCompte": "DGRK - Recettes Fiscales Foncières",
        "nomAgent": "Mutombo Jean",
        "declaration": {
          "id": "uuid-string",
          "dateDeclaration": "2025-01-15T10:30:45.123Z",
          "statut": "VALIDEE"
        },
        "propriete": {
          "id": "7e9501cb-3205-4599-bbc0-f0a887aab4a1",
          "type": "VILLA",
          "localite": "Kenya",
          "superficie": 500.0,
          "adresse": "Av. Kivu 44, Kenya"
        },
        "contribuable": {
          "id": "uuid-string",
          "nom": "Nom du contribuable",
          "adressePrincipale": "Adresse principale du contribuable",
          "telephonePrincipal": "+243123456789",
          "email": "contribuable@example.com"
        }
      }
    ]
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-12T07:55:47.410243835"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "TAXATION_FETCH_ERROR",
    "message": "Erreur lors de la récupération des taxations du contribuable",
    "details": "Message d'erreur détaillé"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-12T07:55:47.410243835"
  }
}
```

### 9. Récupérer les taxations par statut

Récupère la liste des taxations avec un statut spécifique.

- **URL**: `/api/taxations/statut/{statut}`
- **Méthode**: `GET`
- **Rôles autorisés**: `TAXATEUR`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `ADMIN`
- **Paramètres**: 
  - `statut` (path parameter): Statut de la taxation (EN_COURS, PAYEE, EXONEREE, ANNULEE)
  - `page` (optionnel): Numéro de page (commence à 0)
  - `size` (optionnel): Nombre d'éléments par page (par défaut 10)
  - `sortBy` (optionnel): Champ de tri (par défaut dateTaxation)
  - `sortDir` (optionnel): Direction du tri (asc/desc, par défaut desc)

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "totalItems": 5,
    "taxations": [
      {
        "id": "1ce73eff-cac9-4ed5-9670-ee40240a0335",
        "dateTaxation": "2025-09-10T05:36:47.378+00:00",
        "montant": 6400.203021963628,
        "exercice": "2025",
        "statut": "VALIDEE",
        "typeImpot": "IF",
        "exoneration": false,
        "motifExoneration": null,
        "dateEcheance": null,
        "actif": true,
        "codeQR": "http://localhost:3000/print/taxation/1ce73eff-cac9-4ed5-9670-ee40240a0335",
        "numeroTaxation": "DPRI-0001-IFBRF-2025",
        "devise": "USD",
        "nomBanque": "Banque Centrale du Congo",
        "numeroCompte": "00123456789",
        "intituleCompte": "DGRK - Recettes Fiscales Foncières",
        "motifAnnulation": null,
        "nomAgent": "Mutombo Jean",
        "declaration": {
          "id": "uuid-string",
          "dateDeclaration": "2025-01-15T10:30:45.123Z",
          "statut": "VALIDEE"
        }
      }
    ],
    "totalPages": 1,
    "currentPage": 0
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-12T07:55:47.410243835"
  }
}
```

### 10. Annuler une taxation

Permet à un chef de division ou à un administrateur d'annuler une taxation en spécifiant un motif d'annulation.

- **URL**: `/api/taxations/{id}`
- **Méthode**: `DELETE`
- **Rôles autorisés**: `CHEF_DE_DIVISION`, `ADMIN`
- **Paramètres**: 
  - `id` (path parameter): UUID de la taxation à annuler

#### Corps de la requête

```json
{
  "motifAnnulation": "Erreur de calcul du montant"
}
```

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "message": "Taxation annulée avec succès",
    "taxation": {
      "id": "1ce73eff-cac9-4ed5-9670-ee40240a0335",
      "dateTaxation": "2025-09-10T05:36:47.378+00:00",
      "montant": 6400.203021963628,
      "exercice": "2025",
      "statut": "ANNULEE",
      "typeImpot": "IF",
      "exoneration": false,
      "motifExoneration": null,
      "dateEcheance": null,
      "actif": false,
      "codeQR": "http://localhost:3000/print/taxation/1ce73eff-cac9-4ed5-9670-ee40240a0335",
      "numeroTaxation": "DPRI-0001-IFBRF-2025",
      "devise": "USD",
      "nomBanque": "Banque Centrale du Congo",
      "numeroCompte": "00123456789",
      "intituleCompte": "DGRK - Recettes Fiscales Foncières",
      "motifAnnulation": "Erreur de calcul du montant",
      "nomAgent": "Mutombo Jean"
    }
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-12T07:55:47.410243835"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "TAXATION_ANNULATION_ERROR",
    "message": "Erreur lors de l'annulation de la taxation",
    "details": "Cette taxation est déjà annulée"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-12T07:55:47.410243835"
  }
}
```

## Structure des données

### Taxation

| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant unique de la taxation |
| dateTaxation | Date | Date de la taxation |
| montant | Double | Montant de la taxation |
| exercice | String | Exercice fiscal |
| statut | StatutTaxation | Statut de la taxation (EN_COURS, PAYEE, EXONEREE, ANNULLEE) |
| typeImpot | TypeImpot | Type d'impôt (IF, IRL, ICM, etc.) |
| exoneration | Boolean | Indique si la taxation est exonérée |
| motifExoneration | String | Motif de l'exonération |
| dateEcheance | Date | Date d'échéance de la taxation |
| actif | Boolean | Indique si la taxation est active |
| codeQR | String | Code QR unique pour l'identification |
| numeroTaxation | String | Numéro de taxation |
| devise | String | Devise de la taxation (USD par défaut) |
| nomBanque | String | Nom de la banque |
| numeroCompte | String | Numéro de compte bancaire |
| intituleCompte | String | Intitulé du compte bancaire |
| nomAgent | String | Nom de l'agent qui a effectué la taxation |
| declaration | Declaration | Déclaration associée à la taxation |
| propriete | Propriete | Propriété associée à la taxation |
| contribuable | Contribuable | Contribuable associé à la taxation |
| motifAnnulation | String | Motif d'annulation de la taxation |

### Declaration

| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant unique de la déclaration |
| dateDeclaration | Date | Date de la déclaration |
| statut | String | Statut de la déclaration |

### Propriete

| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant unique de la propriété |
| type | String | Type de la propriété |
| localite | String | Localité de la propriété |
| superficie | Double | Superficie de la propriété |
| adresse | String | Adresse de la propriété |

### Contribuable

| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant unique du contribuable |
| nom | String | Nom du contribuable |
| adressePrincipale | String | Adresse principale du contribuable |
| telephonePrincipal | String | Téléphone principal du contribuable |
| email | String | Email du contribuable |
