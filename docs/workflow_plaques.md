# Workflow de Gestion des Plaques d'Immatriculation

## Vue d'ensemble

Ce document décrit le processus complet d'attribution des plaques d'immatriculation pour les motos et tricycles dans le système DPRIHKAT.

## ⚠️ Important : Distinction Plaque vs Vignette

**PLAQUE** et **VIGNETTE** sont **deux taxations distinctes** :

### 🏷️ Plaque d'Immatriculation
- **Nature :** Taxe d'immatriculation (une seule fois ou lors du renouvellement)
- **Objet :** Obtenir le droit de circuler avec une plaque officielle
- **Fréquence :** Unique (sauf changement, perte, vol)
- **Entité :** `DemandePlaque` → `Plaque`
- **Résultat :** Attribution d'un numéro de plaque physique

### 🎫 Vignette (IRV - Impôt sur les Revenus Véhicules)
- **Nature :** Impôt annuel sur les véhicules
- **Objet :** Taxe de circulation annuelle
- **Fréquence :** Annuelle (renouvellement chaque année)
- **Entité :** `Vignette`
- **Résultat :** Vignette valide pour l'année en cours

**Un véhicule peut avoir :**
- ✅ Une plaque SANS vignette (non conforme pour circuler)
- ✅ Une plaque AVEC vignette (conforme pour circuler)
- ❌ Une vignette SANS plaque (impossible, la plaque est prérequise)

### 🔄 Schéma du Processus Complet

```
┌─────────────────────────────────────────────────────────────────┐
│                    PROCESSUS PLAQUE (Une fois)                   │
└─────────────────────────────────────────────────────────────────┘
   1. Enregistrer véhicule → statut: ENREGISTRE
   2. Créer demande plaque → statut: TAXE
   3. Taxation plaque (TAXE_PLAQUE)
   4. Paiement plaque
   5. Attribution plaque → statut: PLAQUE_ATTRIBUEE
   6. Livraison plaque → statut: ACTIF
                ↓
┌─────────────────────────────────────────────────────────────────┐
│                 PROCESSUS VIGNETTE (Annuel)                      │
└─────────────────────────────────────────────────────────────────┘
   1. Taxation IRV (chaque année)
   2. Paiement IRV
   3. Émission vignette → Véhicule conforme pour circuler
   
   ⟳ Renouvellement chaque année
```

---

## Étapes du Processus

### 1. Enregistrement du Contribuable
**Endpoint:** `POST /api/contribuables` ou utiliser un contribuable existant

**Données requises:**
- Informations d'identification (nom, prénom, NRC, etc.)
- Adresse
- Contact

**Statut:** Contribuable actif dans le système

---

### 2. Enregistrement du Véhicule (Moto/Tricycle)
**Endpoint:** `POST /api/vehicules`

**Données requises:**
```json
{
  "marque": "Honda",
  "modele": "CB125",
  "annee": 2023,
  "numeroChassis": "JH2SC6701NK100001",
  "genre": "MOTO",
  "categorie": "DEUX_ROUES",
  "puissanceFiscale": 5.0,
  "unitePuissance": "CV",
  "proprietaireId": "uuid-du-contribuable"
}
```

**Résultat:**
- Véhicule créé avec `statut = ENREGISTRE`
- `numeroPlaque = null` (pas encore attribué)
- `immatriculation = null` (sera généré après attribution de plaque)

---

### 3. Création de la Demande de Plaque
**Endpoint:** `POST /api/demandes-plaque`

**Données requises:**
```json
{
  "vehiculeId": "uuid-du-vehicule",
  "contribuableId": "uuid-du-contribuable",
  "facturePath": "path/to/facture.pdf"
}
```

**Résultat:**
- Demande créée avec `statut = SOUMISE`
- Statut du véhicule passe à `TAXE`

---

### 4. Taxation de la Plaque
**Endpoint:** `POST /api/taxations`

**Données requises:**
```json
{
  "demandeId": "uuid-de-la-demande",
  "natureImpot": "TAXE_PLAQUE",
  "exercice": "2024"
}
```

**Résultat:**
- Taxation créée avec montant calculé pour la plaque
- Génération d'un avis de taxation pour la plaque
- Lien entre la demande et la taxation

---

### 5. Paiement de la Taxation de Plaque
**Endpoint:** `POST /api/paiements`

**Données requises:**
```json
{
  "taxationId": "uuid-de-la-taxation-plaque",
  "montant": 50000.0,
  "modePaiement": "ESPECES",
  "reference": "PAY-PLAQUE-2024-001"
}
```

**Résultat:**
- Paiement enregistré pour la plaque
- Taxation de plaque marquée comme payée
- Statut de la demande passe à `PAYEE`
- Statut du véhicule passe à `PAYE`
- Le véhicule est maintenant éligible pour l'attribution d'une plaque

---

### 6. Attribution de la Plaque
**Endpoint:** `PUT /api/demandes-plaque/{id}/attribuer`

**Données requises:**
```json
{
  "numeroPlaque": "KIN-1234-A"
}
```

**OU sélection d'une plaque en stock:**

**Endpoint:** `PUT /api/demandes-plaque/{id}/attribuer-stock`

**Données requises:**
```json
{
  "plaqueId": "uuid-de-la-plaque-en-stock"
}
```

**Résultat:**
- Une plaque disponible (statut `STOCK`) est attribuée au véhicule
- `numeroPlaque` du véhicule est mis à jour
- Statut de la plaque passe à `ATTRIBUEE`
- Statut de la demande passe à `VALIDEE`
- Statut du véhicule passe à `PLAQUE_ATTRIBUEE`
- Génération du certificat d'immatriculation

---

### 7. Livraison de la Plaque
**Endpoint:** `PUT /api/plaques/{id}/livrer`

**Résultat:**
- Statut de la plaque passe à `LIVREE`
- Statut de la demande passe à `LIVREE`
- Statut du véhicule passe à `ACTIF`
- Le véhicule a maintenant une plaque physique

---

## 🎫 Processus Séparé : Vignette (IRV)

### 1. Taxation Annuelle de la Vignette
**Prérequis :** Le véhicule doit avoir une plaque (`statut >= PLAQUE_ATTRIBUEE`)

**Endpoint:** `POST /api/taxations`

**Données requises:**
```json
{
  "vehiculeId": "uuid-du-vehicule",
  "natureImpot": "IRV",
  "exercice": "2024"
}
```

**Résultat:**
- Taxation IRV créée avec montant calculé selon la puissance fiscale
- Génération d'un avis de taxation pour la vignette

---

### 2. Paiement de la Vignette
**Endpoint:** `POST /api/paiements`

**Données requises:**
```json
{
  "taxationId": "uuid-de-la-taxation-irv",
  "montant": 120000.0,
  "modePaiement": "ESPECES",
  "reference": "PAY-IRV-2024-001"
}
```

**Résultat:**
- Paiement enregistré pour la vignette
- Taxation IRV marquée comme payée

---

### 3. Émission de la Vignette
**Endpoint:** `POST /api/vignettes`

**Données requises:**
```json
{
  "vehiculeId": "uuid-du-vehicule",
  "taxationId": "uuid-de-la-taxation-irv",
  "numero": "VIG-2024-001234",
  "dateExpiration": "2024-12-31"
}
```

**Résultat:**
- Vignette créée avec `statut = ACTIVE`
- Document de vignette généré (PDF avec QR code)
- Le véhicule est maintenant conforme pour circuler

---

## Statuts du Véhicule

| Statut | Description | Actions possibles |
|--------|-------------|-------------------|
| `ENREGISTRE` | Véhicule enregistré, pas encore taxé | Créer taxation |
| `TAXE` | Véhicule taxé, en attente de paiement | Effectuer paiement |
| `PAYE` | Paiement effectué, en attente de plaque | Attribuer plaque |
| `PLAQUE_ATTRIBUEE` | Plaque attribuée, en attente de livraison | Livrer plaque |
| `ACTIF` | Véhicule actif avec plaque livrée | Renouvellement vignette |
| `SUSPENDU` | Véhicule suspendu | Régulariser situation |
| `RADIE` | Véhicule radié du système | Aucune |

---

## Statuts de la Plaque

| Statut | Description |
|--------|-------------|
| `STOCK` | Plaque en stock, disponible |
| `ATTRIBUEE` | Plaque attribuée à un véhicule |
| `LIVREE` | Plaque livrée au propriétaire |

---

## Endpoints Principaux

### 🚗 Véhicules
- `POST /api/vehicules` - Créer un véhicule
- `GET /api/vehicules/{id}` - Récupérer un véhicule
- `GET /api/vehicules?statut=PAYE` - Lister véhicules par statut
- `GET /api/vehicules/sans-plaque` - Lister véhicules sans plaque
- `GET /api/vehicules/contribuable/{id}` - Véhicules d'un contribuable

### 🏷️ Demandes de Plaques
- `POST /api/demandes-plaque` - Créer une demande de plaque
- `GET /api/demandes-plaque/{id}` - Récupérer une demande
- `GET /api/demandes-plaque?statut=SOUMISE` - Lister par statut
- `PUT /api/demandes-plaque/{id}/valider` - Valider une demande
- `PUT /api/demandes-plaque/{id}/rejeter` - Rejeter une demande
- `PUT /api/demandes-plaque/{id}/attribuer` - Attribuer un numéro de plaque
- `PUT /api/demandes-plaque/{id}/attribuer-stock` - Attribuer depuis le stock

### 🏷️ Plaques (Stock)
- `POST /api/plaques` - Créer une plaque (stock)
- `GET /api/plaques/disponibles` - Lister plaques disponibles
- `GET /api/plaques/{id}` - Récupérer une plaque
- `PUT /api/plaques/{id}/livrer` - Marquer comme livrée
- `PUT /api/plaques/{id}/liberer` - Libérer une plaque
- `GET /api/plaques/vehicule/{vehiculeId}` - Historique des plaques d'un véhicule

### 🎫 Vignettes (IRV)
- `POST /api/vignettes` - Émettre une vignette
- `GET /api/vignettes/{id}` - Récupérer une vignette
- `GET /api/vignettes/vehicule/{vehiculeId}` - Vignettes d'un véhicule
- `GET /api/vignettes/actives` - Lister vignettes actives
- `PUT /api/vignettes/{id}/desactiver` - Désactiver une vignette
- `GET /api/vignettes/expiration-proche` - Vignettes à renouveler

### 💰 Taxations
- `POST /api/taxations` - Créer une taxation (PLAQUE ou IRV)
- `GET /api/taxations/{id}` - Récupérer une taxation
- `GET /api/taxations/vehicule/{vehiculeId}` - Taxations d'un véhicule
- `GET /api/taxations/demande/{demandeId}` - Taxation d'une demande
- `GET /api/taxations?type=TAXE_PLAQUE` - Filtrer par type

### 💳 Paiements
- `POST /api/paiements` - Enregistrer un paiement
- `GET /api/paiements/{id}` - Récupérer un paiement
- `GET /api/paiements/taxation/{taxationId}` - Paiements d'une taxation
- `GET /api/paiements/contribuable/{id}` - Paiements d'un contribuable

---

## Règles de Gestion

### 🏷️ Règles pour les Plaques

1. **Une demande de plaque nécessite un véhicule enregistré** (`statut = ENREGISTRE`)

2. **Une plaque ne peut être attribuée qu'après paiement complet** de la taxation de plaque

3. **Une plaque ne peut être attribuée qu'à un seul véhicule** à la fois

4. **Un véhicule peut avoir plusieurs plaques dans son historique** (changement, perte, vol)

5. **Seule la dernière plaque active est valide** pour un véhicule

6. **Le numéro de plaque est unique** dans le système

7. **Les plaques peuvent être réutilisées** après radiation d'un véhicule (délai de carence)

8. **Une demande de plaque doit être validée** par un agent avant attribution

### 🎫 Règles pour les Vignettes (IRV)

1. **Une vignette ne peut être émise que pour un véhicule avec plaque** (`statut >= PLAQUE_ATTRIBUEE`)

2. **La vignette est annuelle** et doit être renouvelée chaque année

3. **Le montant de la vignette dépend de la puissance fiscale** du véhicule

4. **Une vignette expirée rend le véhicule non conforme** pour circuler

5. **Un véhicule peut avoir plusieurs vignettes dans son historique** (une par année)

6. **Seule la vignette de l'année en cours est valide**

7. **La vignette doit être payée avant émission**

### ⚖️ Règles Générales

1. **Un véhicule sans plaque ne peut circuler** légalement

2. **Un véhicule avec plaque mais sans vignette valide ne peut circuler** légalement

3. **Les deux taxations (plaque + vignette) sont indépendantes** mais complémentaires

4. **La plaque est un prérequis pour la vignette**, mais pas l'inverse

---

## Cas d'Usage Spéciaux

### Changement de Plaque
1. Libérer l'ancienne plaque (`PUT /api/plaques/{id}/liberer`)
2. Attribuer une nouvelle plaque au véhicule

### Perte ou Vol de Plaque
1. Déclarer la perte (`POST /api/plaques/{id}/declarer-perte`)
2. Créer une nouvelle demande de plaque
3. Payer les frais de remplacement
4. Attribuer une nouvelle plaque

### Transfert de Propriété
1. Mettre à jour le propriétaire du véhicule
2. La plaque reste attachée au véhicule
3. Émettre un nouveau certificat d'immatriculation

### Radiation de Véhicule
1. Changer le statut du véhicule à `RADIE`
2. Libérer la plaque associée
3. La plaque retourne en stock après délai de carence

---

## Exemple de Flux Complet

### 🏷️ Processus Plaque

```javascript
// 1. Créer/Récupérer le contribuable
const contribuable = await api.post('/api/contribuables', {
  nom: "KABILA",
  prenom: "Joseph",
  nrc: "123456/78/90",
  // ...
});

// 2. Enregistrer le véhicule
const vehicule = await api.post('/api/vehicules', {
  marque: "Honda",
  modele: "CB125",
  annee: 2023,
  numeroChassis: "JH2SC6701NK100001",
  genre: "MOTO",
  proprietaireId: contribuable.id
});
// vehicule.statut = "ENREGISTRE"
// vehicule.numeroPlaque = null

// 3. Créer une demande de plaque
const demande = await api.post('/api/demandes-plaque', {
  vehiculeId: vehicule.id,
  contribuableId: contribuable.id,
  facturePath: "factures/facture-achat.pdf"
});
// demande.statut = "SOUMISE"
// vehicule.statut = "TAXE"

// 4. Créer la taxation pour la plaque
const taxationPlaque = await api.post('/api/taxations', {
  demandeId: demande.id,
  natureImpot: "TAXE_PLAQUE",
  exercice: "2024"
});
// taxationPlaque.montant = 50000 CDF (exemple)

// 5. Effectuer le paiement de la plaque
const paiementPlaque = await api.post('/api/paiements', {
  taxationId: taxationPlaque.id,
  montant: taxationPlaque.montantTotal,
  modePaiement: "ESPECES",
  reference: "PAY-PLAQUE-2024-001"
});
// demande.statut = "PAYEE"
// vehicule.statut = "PAYE"

// 6. Attribuer une plaque
const result = await api.put(`/api/demandes-plaque/${demande.id}/attribuer`, {
  numeroPlaque: "KIN-1234-A"
});
// vehicule.statut = "PLAQUE_ATTRIBUEE"
// vehicule.numeroPlaque = "KIN-1234-A"
// plaque.statut = "ATTRIBUEE"
// demande.statut = "VALIDEE"

// 7. Livrer la plaque
await api.put(`/api/plaques/${result.plaque.id}/livrer`);
// vehicule.statut = "ACTIF"
// plaque.statut = "LIVREE"
// demande.statut = "LIVREE"
```

### 🎫 Processus Vignette (IRV) - Séparé

```javascript
// Prérequis: Le véhicule doit avoir une plaque (statut >= PLAQUE_ATTRIBUEE)

// 1. Créer la taxation IRV (annuelle)
const taxationIRV = await api.post('/api/taxations', {
  vehiculeId: vehicule.id,
  natureImpot: "IRV",
  exercice: "2024"
});
// taxationIRV.montant = 120000 CDF (selon puissance fiscale)

// 2. Effectuer le paiement de la vignette
const paiementIRV = await api.post('/api/paiements', {
  taxationId: taxationIRV.id,
  montant: taxationIRV.montantTotal,
  modePaiement: "ESPECES",
  reference: "PAY-IRV-2024-001"
});

// 3. Émettre la vignette
const vignette = await api.post('/api/vignettes', {
  vehiculeId: vehicule.id,
  taxationId: taxationIRV.id,
  numero: "VIG-2024-001234",
  dateExpiration: "2024-12-31"
});
// vignette.statut = "ACTIVE"
// Document PDF généré avec QR code

// Le véhicule est maintenant conforme pour circuler (plaque + vignette valide)
```

---

## 📊 Tableau Comparatif : Plaque vs Vignette

| Critère | 🏷️ Plaque | 🎫 Vignette (IRV) |
|---------|-----------|-------------------|
| **Nature** | Taxe d'immatriculation | Impôt annuel de circulation |
| **Fréquence** | Une fois (sauf perte/vol) | Annuelle |
| **Montant** | ~50,000 CDF (fixe) | Variable selon puissance fiscale |
| **Entité** | `DemandePlaque` → `Plaque` | `Vignette` |
| **Taxation** | `TAXE_PLAQUE` | `IRV` |
| **Prérequis** | Véhicule enregistré | Véhicule avec plaque |
| **Résultat** | Numéro de plaque physique | Document annuel avec QR code |
| **Validité** | Permanente | 1 an (renouvellement requis) |
| **Obligatoire pour circuler** | ✅ Oui | ✅ Oui |
| **Peut exister seul** | ✅ Oui | ❌ Non (nécessite plaque) |

### Scénarios possibles :

| Véhicule | Plaque | Vignette | Statut | Peut circuler ? |
|----------|--------|----------|--------|-----------------|
| Enregistré | ❌ | ❌ | `ENREGISTRE` | ❌ Non |
| Taxé | ❌ | ❌ | `TAXE` | ❌ Non |
| Payé | ❌ | ❌ | `PAYE` | ❌ Non |
| Avec plaque | ✅ | ❌ | `ACTIF` | ⚠️ Non (vignette expirée/absente) |
| Avec plaque | ✅ | ✅ | `ACTIF` | ✅ Oui (conforme) |

---

## Notes Techniques

- Les dates sont au format ISO 8601 (`YYYY-MM-DD`)
- Les UUIDs sont au format standard (`xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx`)
- Tous les montants sont en CDF (Francs Congolais)
- Les endpoints nécessitent une authentification JWT
- Les rôles requis varient selon l'opération (voir documentation API)
- **Important :** La plaque et la vignette sont deux taxations distinctes avec des processus séparés
