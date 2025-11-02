# Documentation API - Gestion des Documents de Recouvrement

Cette documentation détaille les endpoints disponibles pour la gestion des documents de recouvrement dans l'API DPRIHKAT.

---

## Vue d'ensemble

Les documents de recouvrement sont des actes administratifs émis dans le cadre de la procédure de recouvrement des impôts. Ils incluent notamment les Avis de Mise en Recouvrement (AMR), les Mises En Demeure (MED), les Contraintes Fiscales, les Commandements de Payer et les Avis à Tiers Détenteur (ATD).

### Base URL
```
/api/documents-recouvrement
```

## Endpoints

### 1. Lister les documents de recouvrement
**GET /**

Récupère une liste paginée des documents de recouvrement avec filtres optionnels.

**Paramètres de requête :**
- `type` (optionnel) : Type de document (AMR, MED, CONTRAINTE, COMMANDEMENT, ATD)
- `statut` (optionnel) : Statut du document (BROUILLON, VALIDE, ANNULE, CLOTURE)
- `contribuableId` (optionnel) : ID du contribuable
- `dossierRecouvrementId` (optionnel) : ID du dossier de recouvrement
- `dateDebut` (optionnel) : Date de début pour le filtre
- `dateFin` (optionnel) : Date de fin pour le filtre
- `page` (défaut: 0) : Numéro de la page
- `size` (défaut: 10) : Nombre d'éléments par page
- `sortBy` (défaut: "dateGeneration") : Champ de tri
- `sortDir` (défaut: "desc") : Direction du tri (asc/desc)

**Rôles autorisés :** ADMIN, DIRECTEUR, INFORMATICIEN, CONTROLEUR, RECEVEUR_DES_IMPOTS

### 2. Récupérer un document par son ID
**GET /{id}**

Récupère les détails d'un document de recouvrement spécifique.

**Paramètres de chemin :**
- `id` : ID du document

**Rôles autorisés :** ADMIN, DIRECTEUR, INFORMATICIEN, CONTROLEUR, RECEVEUR_DES_IMPOTS

### 3. Récupérer les documents par type
**GET /type/{type}**

Récupère tous les documents d'un type spécifique.

**Paramètres de chemin :**
- `type` : Type de document (AMR, MED, CONTRAINTE, COMMANDEMENT, ATD)

**Rôles autorisés :** ADMIN, DIRECTEUR, INFORMATICIEN, CONTROLEUR, RECEVEUR_DES_IMPOTS

### 4. Récupérer les documents par statut
**GET /statut/{statut}**

Récupère tous les documents ayant un statut spécifique.

**Paramètres de chemin :**
- `statut` : Statut du document (BROUILLON, VALIDE, ANNULE, CLOTURE)

**Rôles autorisés :** ADMIN, DIRECTEUR, INFORMATICIEN, CONTROLEUR, RECEVEUR_DES_IMPOTS

### 5. Récupérer les documents d'un contribuable
**GET /contribuable/{contribuableId}**

Récupère tous les documents liés à un contribuable spécifique.

**Paramètres de chemin :**
- `contribuableId` : ID du contribuable

**Rôles autorisés :** ADMIN, DIRECTEUR, INFORMATICIEN, CONTROLEUR, RECEVEUR_DES_IMPOTS

### 6. Récupérer les documents d'un dossier
**GET /dossier/{dossierRecouvrementId}**

Récupère tous les documents liés à un dossier de recouvrement spécifique.

**Paramètres de chemin :**
- `dossierRecouvrementId` : ID du dossier de recouvrement

**Rôles autorisés :** ADMIN, DIRECTEUR, INFORMATICIEN, CONTROLEUR, RECEVEUR_DES_IMPOTS

### 7. Récupérer les documents en retard
**GET /en-retard/{type}**

Récupère les documents dont la date d'échéance est dépassée.

**Paramètres de chemin :**
- `type` : Type de document (AMR, MED, CONTRAINTE, COMMANDEMENT, ATD)

**Rôles autorisés :** ADMIN, DIRECTEUR, INFORMATICIEN, CONTROLEUR, RECEVEUR_DES_IMPOTS

## Types de documents

### 1. Avis de Mise en Recouvrement (AMR)
**POST /amr**

Crée un nouvel Avis de Mise en Recouvrement.

**Corps de la requête :**
```json
{
  "dossierRecouvrementId": "uuid",
  "contribuableId": "uuid",
  "agentId": "uuid",
  "montantPrincipal": 1000000.0,
  "montantPenalites": 50000.0,
  "dateEmission": "2023-01-01",
  "dateEcheance": "2023-02-01",
  "reference": "AMR-2023-001"
}
```

**Rôles autorisés :** ADMIN, DIRECTEUR, INFORMATICIEN, RECEVEUR_DES_IMPOTS

### 2. Mise En Demeure (MED)
**POST /med**

Crée une nouvelle Mise En Demeure.

**Rôles autorisés :** ADMIN, DIRECTEUR, INFORMATICIEN, RECEVEUR_DES_IMPOTS

### 3. Contrainte Fiscale
**POST /contrainte**

Crée une nouvelle Contrainte Fiscale.

**Rôles autorisés :** ADMIN, DIRECTEUR, INFORMATICIEN, RECEVEUR_DES_IMPOTS

### 4. Commandement de Payer
**POST /commandement**

Crée un nouveau Commandement de Payer.

**Rôles autorisés :** ADMIN, DIRECTEUR, INFORMATICIEN, RECEVEUR_DES_IMPOTS

### 5. Avis à Tiers Détenteur (ATD)
**POST /atd**

Crée un nouvel Avis à Tiers Détenteur.

**Rôles autorisés :** ADMIN, DIRECTEUR, INFORMATICIEN, RECEVEUR_DES_IMPOTS

## Gestion des statuts

### Mettre à jour le statut d'un document
**PUT /{id}/statut**

Met à jour le statut d'un document de recouvrement.

**Paramètres de chemin :**
- `id` : ID du document

**Corps de la requête :**
```json
{
  "statut": "VALIDE",
  "commentaire": "Document validé par l'agent"
}
```

**Rôles autorisés :** ADMIN, DIRECTEUR, INFORMATICIEN, RECEVEUR_DES_IMPOTS

## Codes d'erreur

| Code | Description |
|------|-------------|
| DOCUMENT_NOT_FOUND | Le document demandé n'existe pas |
| DOCUMENTS_FETCH_ERROR | Erreur lors de la récupération des documents |
| DOCUMENT_CREATION_ERROR | Erreur lors de la création du document |
| DOCUMENT_UPDATE_ERROR | Erreur lors de la mise à jour du document |
| INVALID_DOCUMENT_TYPE | Type de document non valide |
| INVALID_DOCUMENT_STATUS | Statut de document non valide |
| MISSING_REQUIRED_FIELDS | Champs obligatoires manquants |
| UNAUTHORIZED_ACTION | Action non autorisée pour ce rôle |

## Exemple de réponse

```json
{
  "success": true,
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "type": "AMR",
    "statut": "VALIDE",
    "reference": "AMR-2023-001",
    "dateGeneration": "2023-01-01T00:00:00Z",
    "dateEcheance": "2023-02-01T00:00:00Z",
    "montantPrincipal": 1000000.0,
    "montantPenalites": 50000.0,
    "contribuable": {
      "id": "123e4567-e89b-12d3-a456-426614174001",
      "nom": "DUPONT",
      "prenom": "Jean"
    },
    "dossierRecouvrement": {
      "id": "123e4567-e89b-12d3-a456-426614174002",
      "reference": "DOS-2023-001"
    }
  }
}
```
