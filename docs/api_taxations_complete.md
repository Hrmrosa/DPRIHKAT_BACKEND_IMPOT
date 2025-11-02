# API Taxations - Documentation Complète

## Vue d'ensemble

L'API Taxations permet de gérer tous les types d'impôts dans le système DPRIHKAT. Elle supporte la création, la consultation, la modification et l'annulation des taxations pour différents types de biens (propriétés immobilières, véhicules, concessions minières).

**URL de base:** `/api/taxations`

**Authentification:** Bearer Token (JWT)

**Date de dernière mise à jour:** 1er Novembre 2025
 
---

## Types d'Impôts Supportés

Le système gère les types d'impôts suivants:

| Code | Nom Complet | Description | Bien Concerné |
|------|-------------|-------------|---------------|
| **IF** | Impôt Foncier | Impôt sur les propriétés bâties | Propriétés immobilières |
| **IRL** | Impôt sur les Revenus Locatifs | Impôt sur les revenus de location | Propriétés immobilières |
| **ICM** | Impôt sur les Concessions Minières | Impôt sur l'exploitation minière | Concessions minières |
| **IRV** | Impôt sur les Revenus Véhicules | Vignette pour véhicules | Véhicules (motos, tricycles) |
| **RL** | Redevance Locative | Redevance pour occupation | Propriétés |
| **PLAQUE** | Taxe de Plaque d'Immatriculation | Taxe pour plaque d'immatriculation | Véhicules |
| **VIGNETTE** | Vignette Véhicule | Vignette annuelle | Véhicules |

---

## Structure de Réponse Enrichie

Toutes les taxations retournent maintenant des informations complètes sur:
- ✅ Le contribuable (nom, adresse, contacts)
- ✅ Le bien concerné (propriété OU véhicule)
- ✅ L'agent taxateur
- ✅ Les informations bancaires
- ✅ La déclaration (si applicable)

### Exemple pour une Taxation de Propriété

```json
{
  "id": "uuid",
  "numeroTaxation": "IF_A1B2C3D4_2025",
  "dateTaxation": "2025-11-01T10:00:00",
  "dateEcheance": "2025-12-01T00:00:00",
  "montant": 150.0,
  "devise": "USD",
  "exercice": "2025",
  "statut": "EN_ATTENTE",
  "typeImpot": "IF",
  "exoneration": false,
  "actif": true,
  "nomBanque": "RAWBANK",
  "numeroCompte": "CD59 0000 0000 0000 0000 0001",
  "intituleCompte": "DPRIHKAT - IMPOTS FONCIERS",
  
  "contribuable": {
    "id": "uuid",
    "nom": "KABONGO Jean",
    "adressePrincipale": "Lubumbashi, Katanga",
    "telephonePrincipal": "+243 XXX XXX XXX",
    "email": "kabongo@example.com"
  },
  
  "propriete": {
    "id": "uuid",
    "type": "BATIMENT",
    "localite": "Lubumbashi",
    "superficie": 500.0,
    "adresse": "Avenue Mobutu, Q. Industriel"
  },
  
  "agent": {
    "id": "uuid",
    "nom": "Agent Taxateur",
    "matricule": "AGT001"
  },
  
  "declaration": {
    "id": "uuid",
    "dateDeclaration": "2025-10-15T08:00:00",
    "statut": "VALIDEE"
  }
}
```

### Exemple pour une Taxation de Plaque/Vignette

```json
{
  "id": "uuid",
  "numeroTaxation": "PLAQ_41BA110C_DLIUDD_2025",
  "dateTaxation": "2025-11-01T11:21:48",
  "dateEcheance": "2025-12-01T00:00:00",
  "montant": 37.0,
  "devise": "USD",
  "exercice": "2025",
  "statut": "EN_ATTENTE",
  "typeImpot": "PLAQUE",
  "exoneration": false,
  "actif": true,
  "nomBanque": "RAWBANK",
  "numeroCompte": "CD59 0000 0000 0000 0000 0001",
  "intituleCompte": "DPRIHKAT - PLAQUES D'IMMATRICULATION",
  
  "contribuable": {
    "id": "uuid",
    "nom": "KABONGO Jean",
    "adressePrincipale": "Lubumbashi, Katanga",
    "telephonePrincipal": "+243 XXX XXX XXX",
    "email": "kabongo@example.com"
  },
  
  "vehicule": {
    "id": "uuid",
    "marque": "TVS",
    "modele": "TVS 150",
    "numeroChassis": "ABC123XYZ789",
    "immatriculation": "TEMP-A1B2C3D4",
    "genre": "Moto"
  },
  
  "agent": {
    "id": "uuid",
    "nom": "Agent Taxateur",
    "matricule": "AGT001"
  }
}
```

---

## Endpoints Disponibles

### 1. Récupérer Toutes les Taxations (Paginé)

**Endpoint:** `GET /api/taxations`

**Rôles:** TAXATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, ADMIN

**Paramètres de requête:**
- `page` (int, optionnel, défaut: 0) - Numéro de page
- `size` (int, optionnel, défaut: 10) - Nombre d'éléments par page
- `sortBy` (string, optionnel, défaut: "dateTaxation") - Champ de tri
- `sortDir` (string, optionnel, défaut: "desc") - Direction du tri (asc/desc)

**Exemple:**
```bash
curl -X GET "http://localhost:8080/api/taxations?page=0&size=20&sortBy=dateTaxation&sortDir=desc" \
  -H "Authorization: Bearer <token>"
```

**Réponse:**
```json
{
  "success": true,
  "data": {
    "taxations": [...],
    "currentPage": 0,
    "totalItems": 150,
    "totalPages": 8
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-11-01T15:17:08"
  }
}
```

---

### 2. Récupérer les Taxations Actives

**Endpoint:** `GET /api/taxations/actives`

**Rôles:** TAXATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, ADMIN

**Description:** Retourne uniquement les taxations actives (non supprimées logiquement).

**Exemple:**
```bash
curl -X GET "http://localhost:8080/api/taxations/actives" \
  -H "Authorization: Bearer <token>"
```

---

### 3. Récupérer une Taxation par ID

**Endpoint:** `GET /api/taxations/{id}`

**Rôles:** TAXATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, ADMIN

**Exemple:**
```bash
curl -X GET "http://localhost:8080/api/taxations/36f285a6-563d-4657-a315-311a319a8b73" \
  -H "Authorization: Bearer <token>"
```

---

### 4. Filtrer par Type d'Impôt

**Endpoint:** `GET /api/taxations/by-type-impot/{typeImpot}`

**Rôles:** TAXATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, ADMIN

**Types d'impôt disponibles:** IF, IRL, ICM, IRV, RL, PLAQUE, VIGNETTE

**Exemples:**
```bash
# Toutes les taxations de plaques
curl -X GET "http://localhost:8080/api/taxations/by-type-impot/PLAQUE" \
  -H "Authorization: Bearer <token>"

# Toutes les vignettes
curl -X GET "http://localhost:8080/api/taxations/by-type-impot/IRV" \
  -H "Authorization: Bearer <token>"

# Tous les impôts fonciers
curl -X GET "http://localhost:8080/api/taxations/by-type-impot/IF" \
  -H "Authorization: Bearer <token>"

# Impôts sur revenus locatifs
curl -X GET "http://localhost:8080/api/taxations/by-type-impot/IRL" \
  -H "Authorization: Bearer <token>"

# Concessions minières
curl -X GET "http://localhost:8080/api/taxations/by-type-impot/ICM" \
  -H "Authorization: Bearer <token>"
```

---

### 5. Filtrer par Statut

**Endpoint:** `GET /api/taxations/by-statut/{statut}`

**Rôles:** TAXATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, ADMIN

**Statuts disponibles:**
- `EN_ATTENTE` - En attente de paiement
- `PAYEE` - Payée
- `ANNULEE` - Annulée
- `EXONEREE` - Exonérée

**Exemple:**
```bash
# Toutes les taxations en attente
curl -X GET "http://localhost:8080/api/taxations/by-statut/EN_ATTENTE" \
  -H "Authorization: Bearer <token>"

# Toutes les taxations payées
curl -X GET "http://localhost:8080/api/taxations/by-statut/PAYEE" \
  -H "Authorization: Bearer <token>"
```

---

### 6. Filtrer par Exercice (Année Fiscale)

**Endpoint:** `GET /api/taxations/by-exercice/{exercice}`

**Rôles:** TAXATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, ADMIN

**Exemple:**
```bash
# Toutes les taxations de 2025
curl -X GET "http://localhost:8080/api/taxations/by-exercice/2025" \
  -H "Authorization: Bearer <token>"

# Toutes les taxations de 2024
curl -X GET "http://localhost:8080/api/taxations/by-exercice/2024" \
  -H "Authorization: Bearer <token>"
```

---

### 7. Filtrer par Propriété

**Endpoint:** `GET /api/taxations/by-propriete/{proprieteId}`

**Rôles:** TAXATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, ADMIN

**Description:** Retourne toutes les taxations liées à une propriété spécifique.

**Exemple:**
```bash
curl -X GET "http://localhost:8080/api/taxations/by-propriete/{proprieteId}" \
  -H "Authorization: Bearer <token>"
```

---

### 8. Générer une Taxation pour une Propriété

**Endpoint:** `POST /api/taxations`

**Rôles:** TAXATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, ADMIN

**Description:** Génère une nouvelle taxation pour une propriété déclarée.

**Corps de la requête:**
```json
{
  "proprieteId": "uuid",
  "proprieteImpotId": "uuid",
  "exercice": 2025
}
```

**Exemple:**
```bash
curl -X POST "http://localhost:8080/api/taxations" \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "proprieteId": "550e8400-e29b-41d4-a716-446655440020",
    "proprieteImpotId": "550e8400-e29b-41d4-a716-446655440030",
    "exercice": 2025
  }'
```

**Réponse:**
```json
{
  "success": true,
  "data": {
    "message": "Taxation générée avec succès",
    "taxation": { ... }
  }
}
```

---

### 9. Calculer le Montant de Taxation

**Endpoint:** `GET /api/taxations/calculate/property/{proprieteId}`

**Rôles:** TAXATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, CONTRIBUABLE, ADMIN

**Description:** Calcule le montant de la taxation sans la créer.

**Exemple:**
```bash
curl -X GET "http://localhost:8080/api/taxations/calculate/property/{proprieteId}" \
  -H "Authorization: Bearer <token>"
```

**Réponse:**
```json
{
  "success": true,
  "data": {
    "message": "Calcul de la taxation effectué avec succès",
    "montant": 150.0
  }
}
```

---

### 10. Mettre à Jour le Statut

**Endpoint:** `PUT /api/taxations/{id}/statut/{statut}`

**Rôles:** TAXATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, ADMIN

**Statuts possibles:** EN_ATTENTE, PAYEE, ANNULEE, EXONEREE

**Exemple:**
```bash
# Marquer comme payée
curl -X PUT "http://localhost:8080/api/taxations/{id}/statut/PAYEE" \
  -H "Authorization: Bearer <token>"

# Marquer comme annulée
curl -X PUT "http://localhost:8080/api/taxations/{id}/statut/ANNULEE" \
  -H "Authorization: Bearer <token>"
```

---

### 11. Accorder une Exonération

**Endpoint:** `PUT /api/taxations/{id}/exoneration`

**Rôles:** CHEF_DE_BUREAU, CHEF_DE_DIVISION, ADMIN

**Paramètres de requête:**
- `motif` (string, requis) - Motif de l'exonération

**Exemple:**
```bash
curl -X PUT "http://localhost:8080/api/taxations/{id}/exoneration?motif=Exonération%20sociale" \
  -H "Authorization: Bearer <token>"
```

**Réponse:**
```json
{
  "success": true,
  "data": {
    "message": "Exonération accordée avec succès",
    "taxation": {
      "id": "uuid",
      "exoneration": true,
      "motifExoneration": "Exonération sociale",
      "statut": "EXONEREE"
    }
  }
}
```

---

### 12. Annuler une Taxation

**Endpoint:** `DELETE /api/taxations/{id}/annulation`

**Rôles:** CHEF_DE_DIVISION, ADMIN

**Description:** Annule une taxation en spécifiant un motif. Cette opération est irréversible.

**Corps de la requête:**
```json
{
  "motifAnnulation": "Erreur de taxation"
}
```

**Exemple:**
```bash
curl -X DELETE "http://localhost:8080/api/taxations/{id}/annulation" \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "motifAnnulation": "Erreur de taxation - bien non taxable"
  }'
```

---

### 13. Désactiver une Taxation (Suppression Logique)

**Endpoint:** `DELETE /api/taxations/{id}`

**Rôles:** CHEF_DE_BUREAU, CHEF_DE_DIVISION, ADMIN

**Description:** Désactive une taxation (suppression logique). La taxation reste dans la base mais n'apparaît plus dans les listes actives.

**Exemple:**
```bash
curl -X DELETE "http://localhost:8080/api/taxations/{id}" \
  -H "Authorization: Bearer <token>"
```

---

### 14. Activer une Taxation

**Endpoint:** `PUT /api/taxations/{id}/activate`

**Rôles:** CHEF_DE_BUREAU, CHEF_DE_DIVISION, ADMIN

**Description:** Réactive une taxation précédemment désactivée.

**Exemple:**
```bash
curl -X PUT "http://localhost:8080/api/taxations/{id}/activate" \
  -H "Authorization: Bearer <token>"
```

---

## Cas d'Usage par Type d'Impôt

### 1. Impôt Foncier (IF)

**Workflow:**
```
1. Déclaration de propriété → POST /api/declarations
2. Validation de la déclaration → PUT /api/declarations/{id}/valider
3. Génération de taxation → POST /api/taxations
4. Consultation → GET /api/taxations/by-type-impot/IF
5. Paiement → PUT /api/taxations/{id}/statut/PAYEE
```

**Exemple complet:**
```bash
# 1. Générer une taxation IF
curl -X POST "http://localhost:8080/api/taxations" \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "proprieteId": "uuid",
    "proprieteImpotId": "uuid-IF",
    "exercice": 2025
  }'

# 2. Consulter toutes les taxations IF
curl -X GET "http://localhost:8080/api/taxations/by-type-impot/IF" \
  -H "Authorization: Bearer <token>"

# 3. Consulter les taxations IF en attente
curl -X GET "http://localhost:8080/api/taxations/by-statut/EN_ATTENTE" \
  -H "Authorization: Bearer <token>"
```

---

### 2. Plaques d'Immatriculation (PLAQUE)

**Workflow:**
```
1. Soumission demande → POST /api/demandes-plaque/creer-vehicule-et-demander
2. Création automatique de taxation PLAQUE (37 USD)
3. Consultation → GET /api/taxations/by-type-impot/PLAQUE
4. Paiement → PUT /api/taxations/{id}/statut/PAYEE
5. Attribution plaque → POST /api/demandes-plaque/{id}/attribuer
```

**Exemple complet:**
```bash
# 1. Les taxations de plaques sont créées automatiquement
# lors de la soumission d'une demande de plaque

# 2. Consulter toutes les taxations de plaques
curl -X GET "http://localhost:8080/api/taxations/by-type-impot/PLAQUE" \
  -H "Authorization: Bearer <token>"

# 3. Consulter les plaques en attente de paiement
curl -X GET "http://localhost:8080/api/taxations/by-statut/EN_ATTENTE" \
  -H "Authorization: Bearer <token>"

# 4. Marquer comme payée
curl -X PUT "http://localhost:8080/api/taxations/{id}/statut/PAYEE" \
  -H "Authorization: Bearer <token>"
```

---

### 3. Vignettes (IRV)

**Workflow:**
```
1. Création automatique avec demande de plaque
2. Consultation → GET /api/taxations/by-type-impot/IRV
3. Paiement → PUT /api/taxations/{id}/statut/PAYEE
```

**Exemple complet:**
```bash
# 1. Consulter toutes les vignettes
curl -X GET "http://localhost:8080/api/taxations/by-type-impot/IRV" \
  -H "Authorization: Bearer <token>"

# 2. Vignettes en attente de paiement
curl -X GET "http://localhost:8080/api/taxations/by-statut/EN_ATTENTE" \
  -H "Authorization: Bearer <token>"

# 3. Marquer comme payée
curl -X PUT "http://localhost:8080/api/taxations/{id}/statut/PAYEE" \
  -H "Authorization: Bearer <token>"
```

---

### 4. Impôt sur Revenus Locatifs (IRL)

**Workflow:**
```
1. Déclaration de propriété locative
2. Génération de taxation IRL
3. Paiement
```

---

### 5. Concessions Minières (ICM)

**Workflow:**
```
1. Déclaration de concession
2. Génération de taxation ICM
3. Paiement
```

---

## Codes d'Erreur

| Code | Message | Description |
|------|---------|-------------|
| `TAXATION_FETCH_ERROR` | Erreur lors de la récupération | Erreur générale de récupération |
| `TAXATION_GENERATION_ERROR` | Erreur lors de la génération | Erreur lors de la création |
| `TAXATION_CALCULATION_ERROR` | Erreur lors du calcul | Erreur de calcul du montant |
| `TAXATION_UPDATE_ERROR` | Erreur lors de la mise à jour | Erreur de mise à jour |
| `TAXATION_EXEMPTION_ERROR` | Erreur lors de l'exonération | Erreur d'exonération |
| `TAXATION_ANNULATION_ERROR` | Erreur lors de l'annulation | Erreur d'annulation |
| `TAXATION_DEACTIVATION_ERROR` | Erreur lors de la désactivation | Erreur de désactivation |
| `TAXATION_ACTIVATION_ERROR` | Erreur lors de l'activation | Erreur d'activation |
| `INVALID_USER` | Utilisateur non valide | Utilisateur non autorisé |
| `MOTIF_ANNULATION_REQUIRED` | Motif requis | Motif d'annulation obligatoire |

---

## Notes Importantes

### 1. Données Enrichies

Toutes les taxations retournent maintenant les informations complètes:
- ✅ **Contribuable:** nom, adresse, téléphone, email
- ✅ **Bien:** propriété (type, superficie, adresse) OU véhicule (marque, modèle, châssis)
- ✅ **Agent:** nom, matricule
- ✅ **Déclaration:** date, statut (si applicable)

### 2. Champs Optionnels

- `vehicule` sera `null` pour les taxations de propriétés
- `propriete` sera `null` pour les taxations de plaques/vignettes
- `agent` sera `null` si aucun agent n'est assigné
- `declaration` sera `null` pour les taxations sans déclaration (plaques/vignettes)

### 3. Contribuable Direct

Pour les taxations sans déclaration (plaques, vignettes), le contribuable est stocké directement via `contribuableDirect`.

### 4. Pagination

L'endpoint principal `/api/taxations` supporte la pagination pour de meilleures performances avec de grandes quantités de données.

### 5. Filtrage Multiple

Vous pouvez combiner les filtres côté frontend après avoir récupéré les données. Par exemple:
- Récupérer toutes les taxations de 2025
- Filtrer côté frontend par type PLAQUE et statut EN_ATTENTE

### 6. Montants Fixes

- **Plaque d'immatriculation:** 37 USD
- **Vignette moto/tricycle:** 10 USD
- **Autres impôts:** calculés selon les barèmes configurés

### 7. Workflow Complet de Taxation

```
Déclaration → Validation → Génération Taxation → Paiement → Apurement
```

### 8. Sécurité

- Tous les endpoints nécessitent une authentification JWT
- Les rôles sont vérifiés pour chaque opération
- Les opérations sensibles (annulation, exonération) nécessitent des rôles élevés

---

## Support

Pour toute question sur l'utilisation de l'API Taxations, contactez l'équipe backend DPRIHKAT.

**Email:** support@dprihkat.cd  
**Documentation:** https://docs.dprihkat.cd
