# DPRIHKAT Backend API Reference - Documentation Complète

Cette documentation présente tous les endpoints REST API avec leurs payloads détaillés et messages de sortie.

**URL de base :** varie selon l'environnement (ex: http://localhost:8080)  
**Authentification :** JWT Bearer Token dans l'en-tête `Authorization: Bearer <token>`  
**Format de réponse :** Enveloppe standardisée via `ResponseUtil`

## Structure de Réponse Standard
Toutes les réponses suivent cette structure :

```json
{
  "success": true/false,
  "data": { /* données de réponse */ } | null,
  "error": {
    "code": "ERROR_CODE",
    "message": "Message d'erreur",
    "details": "Détails supplémentaires"
  } | null,
  "meta": {
    "timestamp": "2024-01-15T10:30:00Z",
    "version": "1.0.0"
  }
}
```

## 🔐 AUTHENTIFICATION

### Connexion
**POST** `/api/auth/login`

**Payload :**
```json
{
  "login": "nom_utilisateur",
  "motDePasse": "mot_de_passe",
  "premiereConnexion": false
}
```

**Réponse de succès :**
```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "login": "nom_utilisateur",
    "role": "ADMIN"
  }
}
```

**Messages d'erreur :**
- `"Utilisateur non trouvé"` - Login incorrect
- `"Mot de passe incorrect"` - Mot de passe invalide
- `"Compte bloqué"` - Utilisateur bloqué
- `"Première connexion requise"` - Changement de mot de passe obligatoire

### Changement de mot de passe
**POST** `/api/auth/change-password`

**Payload :**
```json
{
  "login": "nom_utilisateur",
  "oldPassword": "ancien_mot_de_passe",
  "newPassword": "nouveau_mot_de_passe"
}
```

**Messages de sortie :**
- Succès : `"Mot de passe changé avec succès"`
- Erreur : `"Ancien mot de passe incorrect"`, `"Utilisateur non trouvé"`

---

## 👥 GESTION DES UTILISATEURS
**Base path:** `/api/users` | **Rôles requis:** ADMIN, INFORMATICIEN

### Lister tous les utilisateurs
**GET** `/api/users`

**Paramètres de requête :**
- `page` (int, défaut: 0) - Numéro de page
- `size` (int, défaut: 10) - Taille de page

**Réponse :**
```json
{
  "success": true,
  "data": {
    "utilisateurs": [
      {
        "id": "uuid",
        "login": "admin01",
        "role": "ADMIN",
        "premierConnexion": false,
        "bloque": false,
        "contribuable": null,
        "agent": { "id": "uuid", "nom": "Agent Name" }
      }
    ],
    "totalElements": 25,
    "totalPages": 3
  }
}
```

### Créer un utilisateur
**POST** `/api/users`

**Payload :**
```json
{
  "login": "nouveau_user",
  "motDePasse": "mot_de_passe_temporaire",
  "role": "TAXATEUR",
  "premierConnexion": true,
  "bloque": false
}
```

**Messages de sortie :**
- Succès : `"Utilisateur créé avec succès"`
- Erreur : `"Login déjà existant"`, `"Données invalides"`

### Bloquer/Débloquer un utilisateur
**POST** `/api/users/{id}/block`  
**POST** `/api/users/{id}/unblock`

**Messages de sortie :**
- `"Utilisateur bloqué avec succès"`
- `"Utilisateur débloqué avec succès"`

---

## 📋 GESTION DES DÉCLARATIONS
**Base path:** `/api/declarations`

### Soumettre une déclaration (Contribuable)
**POST** `/api/declarations/soumettre`  
**Rôles:** CONTRIBUABLE

**Payload :**
```json
{
  "date": "2024-01-15T00:00:00Z",
  "montant": 50000.00,
  "typeImpot": "IF",
  "exoneration": false,
  "proprieteId": "propriete_uuid",
  "concessionId": null,
  "location": {
    "type": "Point",
    "coordinates": [-15.123456, 4.654321]
  }
}
```

**Types d'impôts :**
- `IF` - Impôt Foncier
- `IRL` - Impôt sur les Revenus Locatifs
- `ICM` - Impôt sur les Concessions Minières
- `IRV` - Impôt sur les Revenus des Véhicules

**Messages de sortie :**
- Succès : `"Déclaration soumise avec succès"`
- Erreur : `"Période de soumission fermée (autorisée du 2 janvier au 1er février)"`
- Erreur : `"Géolocalisation obligatoire pour ce type d'impôt"`
- Erreur : `"Propriété non trouvée"`

### Enregistrement manuel (Agent)
**POST** `/api/declarations/manuelle`  
**Rôles:** TAXATEUR, RECEVEUR_DES_IMPOTS

**Payload :** Même structure que la soumission

### Lister les déclarations
**GET** `/api/declarations`  
**Rôles:** TAXATEUR, RECEVEUR_DES_IMPOTS, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, CONTRIBUABLE

**Paramètres :** `page`, `size`, `typeImpot`, `statut`

**Statuts de déclaration :**
- `SOUMISE` - En attente de validation
- `VALIDEE` - Validée par un agent
- `REJETEE` - Rejetée
- `PAYEE` - Payée

---

## 💰 GESTION DES PAIEMENTS
**Base path:** `/api/paiements`

### Traiter un paiement
**POST** `/api/paiements/process/{declarationId}`  
**Rôles:** RECEVEUR_DES_IMPOTS

**Paramètres de requête :**
- `bordereauBancaire` (string, requis) - Numéro de bordereau bancaire

**Exemple :** `POST /api/paiements/process/uuid123?bordereauBancaire=BR2024001234`

**Réponse :**
```json
{
  "success": true,
  "data": {
    "paiement": {
      "id": "uuid",
      "date": "2024-01-15T10:30:00Z",
      "montant": 50000.00,
      "mode": "BANQUE",
      "statut": "VALIDE",
      "bordereauBancaire": "BR2024001234"
    }
  }
}
```

**Messages de sortie :**
- Succès : `"Paiement traité avec succès"`
- Erreur : `"Déclaration non trouvée"`, `"Déclaration déjà payée"`, `"Bordereau bancaire invalide"`

**Statuts de paiement :**
- `EN_ATTENTE` - En attente de validation
- `VALIDE` - Validé
- `REJETE` - Rejeté
- `REMBOURSE` - Remboursé

---

## 🧮 TAXATION ET CALCULS
**Base path:** `/api/taxation`

### Générer note de taxation pour propriété
**POST** `/api/taxation/property/{propertyId}`  
**Rôles:** TAXATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION

**Messages de sortie :**
- Succès : `"Note de taxation générée avec succès"`
- Erreur : `"Propriété non trouvée"`, `"Taxation déjà existante pour cette année"`

### Calculer impôt foncier (IF)
**GET** `/api/taxation/calculate/if/property/{propertyId}`  
**Rôles:** TAXATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, CONTRIBUABLE

**Réponse :**
```json
{
  "success": true,
  "data": {
    "montantCalcule": 75000.00,
    "tauxApplique": 0.15,
    "baseCalcul": 500000.00,
    "propriete": {
      "superficie": 500.0,
      "adresse": "123 Avenue de la Paix"
    }
  }
}
```

### Calculer IRV (Impôt sur Revenus Véhicules)
**GET** `/api/taxation/calculate/irv`  
**Rôles:** TAXATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, CONTRIBUABLE

**Paramètres de requête :**
- `puissanceCV` (double, requis) - Puissance en chevaux
- `poids` (double, requis) - Poids du véhicule

**Exemple :** `GET /api/taxation/calculate/irv?puissanceCV=120&poids=1500`

---

## 🗂️ COLLECTE DE TERRAIN
**Base path:** `/api/collecte`

### Créer contribuable avec biens
**POST** `/api/collecte/contribuables`  
**Rôles:** CONTROLLEUR

**Payload :**
```json
{
  "nom": "Jean Dupont",
  "adressePrincipale": "123 Avenue de la Paix",
  "adresseSecondaire": "Appartement 4B",
  "telephonePrincipal": "+243123456789",
  "telephoneSecondaire": "+243987654321",
  "email": "jean.dupont@email.com",
  "nationalite": "Congolaise",
  "type": "PERSONNE_PHYSIQUE",
  "idNat": "ID123456789",
  "nrc": "NRC987654321",
  "sigle": "JD",
  "numeroIdentificationContribuable": "NIC001234567",
  "biens": [
    {
      "type": "AP",
      "localite": "Kinshasa",
      "rangLocalite": 1,
      "superficie": 500.0,
      "adresse": "123 Avenue de la Paix",
      "latitude": -4.123456,
      "longitude": 15.654321
    }
  ]
}
```

**Types de biens :**
- `AP` - Appartement
- `VI` - Villa
- `AT` - Atelier
- `CITERNE` - Citerne
- `DEPOT` - Dépôt
- `CH` - Chambre
- `TE` - Terrain

**Messages de sortie :**
- Succès : `"Contribuable et biens créés avec succès"`
- Erreur : `"Données contribuable invalides"`, `"Coordonnées GPS invalides"`

---

## 🏢 GESTION DES CONTRIBUABLES
**Base path:** `/api/contribuables`

### Créer un contribuable
**POST** `/api/contribuables`  
**Rôles:** ADMIN, DIRECTEUR, INFORMATICIEN

**Payload :**
```json
{
  "nom": "Société ABC SARL",
  "adressePrincipale": "123 Avenue de la Paix",
  "adresseSecondaire": "BP 456",
  "telephonePrincipal": "+243123456789",
  "telephoneSecondaire": "+243987654321",
  "email": "contact@abc.cd",
  "nationalite": "Congolaise",
  "type": "PERSONNE_MORALE",
  "idNat": "ID123456789",
  "NRC": "NRC987654321",
  "sigle": "ABC",
  "numeroIdentificationContribuable": "NIC001234567"
}
```

**Types de contribuables :**
- `PERSONNE_PHYSIQUE`
- `PERSONNE_MORALE`

**Messages de sortie :**
- Succès : `"Contribuable créé avec succès"`
- Erreur : `"NIC déjà existant"`, `"Email invalide"`, `"Données obligatoires manquantes"`

## Divisions
Controller: `DivisionController` — Base path: `/api/divisions`

- GET `/` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN
- GET `/{id}` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN, CHEF_DE_BUREAU, CHEF_DE_DIVISION
- POST `/` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN — Body: `Division`
- PUT `/{id}` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN — Body: `Division`
- DELETE `/{id}` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN

## Dossiers de Recouvrement
Controller: `DossierRecouvrementController` — Base path: `/api/dossiers-recouvrement`

- GET `/` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN, CONTROLLEUR
- GET `/{id}` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN, CONTROLLEUR, CHEF_DE_BUREAU
- POST `/` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN, CONTROLLEUR — Body: `DossierRecouvrement`
- PUT `/{id}` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN, CONTROLLEUR — Body: `DossierRecouvrement`
- DELETE `/{id}` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN

## Poursuites
Controller: `PoursuiteController` — Base path: `/api/poursuites`

- GET `/` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN, CONTROLLEUR
- GET `/{id}` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN, CONTROLLEUR, CHEF_DE_BUREAU
- POST `/` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN, CONTROLLEUR — Body: `Poursuite`
- PUT `/{id}` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN, CONTROLLEUR — Body: `Poursuite`
- DELETE `/{id}` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN

## Relances
Controller: `RelanceController` — Base path: `/api/relances`

- GET `/` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN, CONTROLLEUR
- GET `/{id}` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN, CONTROLLEUR, CHEF_DE_BUREAU
- POST `/` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN, CONTROLLEUR — Body: `Relance`
- PUT `/{id}` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN, CONTROLLEUR — Body: `Relance`
- DELETE `/{id}` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN

## Références (Publiques)
Controller: `ReferenceDataController` — Base path: `/api/ref`

- GET `/communes`
- GET `/communes/{commune}/quartiers`
- GET `/communes/{commune}/quartiers/{quartier}/avenues`
- GET `/voitures/marques`
- GET `/voitures/marques/{marque}/models`

## Propriétés
Controller: `ProprieteController` — Base path: `/api/proprietes`

- GET `/` — Roles: TAXATEUR, RECEVEUR_DES_IMPOTS, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, ADMIN, INFORMATICIEN
- GET `/{id}` — Roles: TAXATEUR, RECEVEUR_DES_IMPOTS, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, ADMIN, INFORMATICIEN
- GET `/mine` — Roles: CONTRIBUABLE
- PATCH `/{id}/location` — Roles: CONTROLLEUR — Body: JSON map (latitude, longitude, etc.)
- GET `/by-contribuable/{contribuableId}` — Roles: CONTROLLEUR

## Concessions Minières
Controller: `ConcessionMinierController` — Base path: `/api/concessions`

- GET `/` — Roles: TAXATEUR, RECEVEUR_DES_IMPOTS, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, ADMIN, INFORMATICIEN
- GET `/{id}` — Roles: TAXATEUR, RECEVEUR_DES_IMPOTS, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, ADMIN, INFORMATICIEN
- GET `/mine` — Roles: CONTRIBUABLE

## Déclarations
Controller: `DeclarationController` — Base path: `/api/declarations`

- POST `/soumettre` — Roles: CONTRIBUABLE — Body: `DeclarationRequest`
- POST `/manuelle` — Roles: TAXATEUR, RECEVEUR_DES_IMPOTS — Body: `DeclarationRequest`
- GET `/` — Roles: TAXATEUR, RECEVEUR_DES_IMPOTS, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, CONTRIBUABLE — Query: `page`, `size`, (others per implementation)
- GET `/{id}` — Roles: TAXATEUR, RECEVEUR_DES_IMPOTS, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, CONTRIBUABLE
- GET `/type/{type}` — Roles: TAXATEUR, RECEVEUR_DES_IMPOTS, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR — Query: `page`, `size`
- GET `/statut/{statut}` — Roles: TAXATEUR, RECEVEUR_DES_IMPOTS, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR — Query: `page`, `size`

## Apurements
Controller: `ApurementController` — Base path: `/api/apurements`

- POST `/create/{declarationId}` — Roles: APUREUR, RECEVEUR_DES_IMPOTS — Query: `type` (TypeApurement)
- POST `/validate/{apurementId}` — Roles: APUREUR, RECEVEUR_DES_IMPOTS
- GET `/declaration/{declarationId}` — Roles: APUREUR, RECEVEUR_DES_IMPOTS, TAXATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR
- GET `/` — Roles: APUREUR, RECEVEUR_DES_IMPOTS, TAXATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR — Query: `page`, `size`

## Paiements
Controller: `PaiementController` — Base path: `/api/paiements`

- POST `/process/{declarationId}` — Roles: RECEVEUR_DES_IMPOTS — Query: `bordereauBancaire` (string)
- GET `/declaration/{declarationId}` — Roles: TAXATEUR, RECEVEUR_DES_IMPOTS, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, CONTRIBUABLE
- GET `/` — Roles: RECEVEUR_DES_IMPOTS, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR — Query: `page`, `size`

## Pénalités
Controller: `PenaliteController` — Base path: `/api/penalites`

- POST `/calculer/{declarationId}` — Roles: TAXATEUR, RECEVEUR_DES_IMPOTS
- POST `/ajuster/{penaltyId}` — Roles: CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR — Query: `newAmount` (double)
- GET `/declaration/{declarationId}` — Roles: TAXATEUR, RECEVEUR_DES_IMPOTS, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, CONTRIBUABLE — Query: `page`, `size`
- GET `/contribuable` — Roles: TAXATEUR, RECEVEUR_DES_IMPOTS, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, CONTRIBUABLE — Query: `page`, `size`

## Taxation
Controller: `TaxationController` — Base path: `/api/taxation`

- POST `/property/{propertyId}` — Roles: TAXATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION
- POST `/concession/{concessionId}` — Roles: TAXATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION
- GET `/calculate/if/property/{propertyId}` — Roles: TAXATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, CONTRIBUABLE
- GET `/calculate/icm/concession/{concessionId}` — Roles: TAXATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, CONTRIBUABLE
- GET `/calculate/irl/property/{propertyId}` — Roles: TAXATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, CONTRIBUABLE
- GET `/calculate/irv` — Roles: TAXATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, CONTRIBUABLE — Query: `puissanceCV`, `poids`

## Vignettes
Controller: `VignetteController` — Base path: `/api/vignettes`

- POST `/generate/{vehiculeId}` — Roles: TAXATEUR — Query: `dateExpirationMillis` (long)
- GET `/{id}` — Roles: TAXATEUR, RECEVEUR_DES_IMPOTS, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, CONTRIBUABLE
- GET `/vehicle/{vehiculeId}` — Roles: TAXATEUR, RECEVEUR_DES_IMPOTS, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, CONTRIBUABLE — Query: `page`, `size`
- GET `/active` — Roles: TAXATEUR, RECEVEUR_DES_IMPOTS, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR — Query: `page`, `size`
- GET `/expired` — Roles: TAXATEUR, RECEVEUR_DES_IMPOTS, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR — Query: `page`, `size`

## Plaques
Controller: `PlaqueController` — Base path: `/api/plaques`

- POST `/assign/{vehiculeId}` — Roles: AGENT_DE_PLAQUES
- GET `/{id}` — Roles: AGENT_DE_PLAQUES, TAXATEUR, RECEVEUR_DES_IMPOTS, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR
- GET `/` — Roles: AGENT_DE_PLAQUES, TAXATEUR, RECEVEUR_DES_IMPOTS, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR — Query: `page`, `size`
- GET `/vehicule/{vehiculeId}` — Roles: AGENT_DE_PLAQUES, TAXATEUR, RECEVEUR_DES_IMPOTS, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, CONTRIBUABLE — Query: `page`, `size`
- GET `/stock` — Roles: AGENT_DE_PLAQUES, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR

## Certificats
Controller: `CertificatController` — Base path: `/api/certificats`

- POST `/property/{declarationId}` — Roles: AGENT_CERTIFICAT
- POST `/vehicle/{vehiculeId}` — Roles: AGENT_CERTIFICAT
- GET `/{id}` — Roles: AGENT_CERTIFICAT, TAXATEUR, RECEVEUR_DES_IMPOTS, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, CONTRIBUABLE
- GET `/declaration/{declarationId}` — Roles: AGENT_CERTIFICAT, TAXATEUR, RECEVEUR_DES_IMPOTS, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, CONTRIBUABLE — Query: `page`, `size`
- GET `/vehicle/{vehiculeId}` — Roles: AGENT_CERTIFICAT, TAXATEUR, RECEVEUR_DES_IMPOTS, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, CONTRIBUABLE — Query: `page`, `size`
- GET `/active` — Roles: AGENT_CERTIFICAT, TAXATEUR, RECEVEUR_DES_IMPOTS, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR — Query: `page`, `size`
- GET `/expired` — Roles: AGENT_CERTIFICAT, TAXATEUR, RECEVEUR_DES_IMPOTS, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR — Query: `page`, `size`

## Collecte (Saisie Terrain)
Controller: `CollecteController` — Base path: `/api/collecte`

- POST `/contribuables` — Roles: CONTROLLEUR — Body: `CollecteContribuableRequest`

## Contrôle Fiscal
Controller: `ControleFiscalController` — Base path: `/api/controle-fiscal`

- GET `/anomalies` — Roles: CONTROLLEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR — Query: `page`, `size` (others per implementation)
- GET `/rapport` — Roles: CONTROLLEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR — Query: `startDate`, `endDate` (ISO date)
- GET `/top-contributors` — Roles: CONTROLLEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR — Query: `limit`
- GET `/delinquents` — Roles: CONTROLLEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR
- GET `/dashboard` — Roles: CONTROLLEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, ADMIN, INFORMATICIEN

---

## Notes
- Most list endpoints support pagination via `page` and `size` query params.
- Request/response schemas (DTOs and entities) can be found in the corresponding `model`, `dto`, `request`, or `entity` packages. Additions welcome to include JSON examples per endpoint.
- Error responses include `error.code`, `error.message`, and optional `error.details`.
