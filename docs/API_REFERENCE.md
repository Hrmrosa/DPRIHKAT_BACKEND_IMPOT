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
- `"Identifiants invalides"` - Login ou mot de passe incorrect
- `"Compte bloqué"` - Le compte utilisateur est bloqué
- `"Première connexion requise"` - L'utilisateur doit changer son mot de passe

### Changer mot de passe (première connexion)
**POST** `/api/auth/change-password`

**Payload :**
```json
{
  "login": "nom_utilisateur",
  "ancienMotDePasse": "mot_de_passe_temporaire",
  "nouveauMotDePasse": "nouveau_mot_de_passe"
}
```

**Messages de sortie :**
- Succès : `"Mot de passe changé avec succès"`
- Erreur : `"Ancien mot de passe incorrect"`, `"Nouveau mot de passe invalide"`

### Rafraîchir token
**POST** `/api/auth/refresh`

**Payload :** Aucun (utilise le token existant dans l'en-tête)

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

---

## 👤 UTILISATEURS
**Base path:** `/api/users`

### Lister les utilisateurs
**GET** `/api/users`  
**Rôles:** ADMIN, INFORMATICIEN, CONTROLLEUR

**Paramètres de requête :**
- `page` (défaut: 0) - Numéro de page
- `size` (défaut: 10) - Taille de page
- `sort` (défaut: "id,asc") - Champ et direction de tri

**Réponse :**
```json
{
  "success": true,
  "data": {
    "users": [
      {
        "id": "uuid",
        "login": "username",
        "role": "ROLE_NAME",
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
**Rôles:** ADMIN, INFORMATICIEN, CONTROLLEUR

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
**Rôles:** ADMIN, INFORMATICIEN, CONTROLLEUR

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
  "proprieteId": "uuid",
  "exercice": 2024,
  "commentaire": "Déclaration impôt foncier 2024"
}
```

**Messages de sortie :**
- Succès : `"Déclaration soumise avec succès"`
- Erreur : `"Propriété non trouvée"`, `"Montant invalide"`, `"Période de déclaration fermée"`

### Valider une déclaration (Agent)
**POST** `/api/declarations/{id}/valider`  
**Rôles:** TAXATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION

**Payload :**
```json
{
  "commentaire": "Déclaration validée après vérification",
  "montantCorrige": 55000.00
}
```

**Messages de sortie :**
- Succès : `"Déclaration validée avec succès"`
- Erreur : `"Déclaration non trouvée"`, `"Déclaration déjà validée"`

### Rejeter une déclaration (Agent)
**POST** `/api/declarations/{id}/rejeter`  
**Rôles:** TAXATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION

**Payload :**
```json
{
  "motifRejet": "Montant sous-évalué",
  "commentaire": "Veuillez recalculer selon la formule officielle"
}
```

**Messages de sortie :**
- Succès : `"Déclaration rejetée avec succès"`
- Erreur : `"Déclaration non trouvée"`, `"Déclaration déjà traitée"`

---

## 💰 GESTION DES PAIEMENTS
**Base path:** `/api/paiements`

### Enregistrer un paiement
**POST** `/api/paiements`  
**Rôles:** APUREUR, RECEVEUR_DES_IMPOTS, ADMIN, CONTROLLEUR

**Payload :**
```json
{
  "declarationId": "uuid",
  "date": "2024-01-15T10:30:00Z",
  "montant": 50000.00,
  "mode": "BANQUE",
  "bordereauBancaire": "BR2024001234",
  "commentaire": "Paiement complet"
}
```

**Messages de sortie :**
- Succès : `"Paiement enregistré avec succès"`
- Erreur : `"Déclaration non trouvée"`, `"Montant invalide"`

### Traiter un paiement
**POST** `/api/paiements/process/{declarationId}`  
**Rôles:** APUREUR, RECEVEUR_DES_IMPOTS, ADMIN, CONTROLLEUR

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
    "montantBase": 45000.00,
    "montantPenalites": 0.00,
    "montantTotal": 45000.00,
    "details": {
      "superficie": 500,
      "tauxZone": 3.5,
      "coefficientType": 1.2
    }
  }
}
```

---

## 📱 COLLECTE MOBILE
**Base path:** `/api/collecte`

### Créer contribuable avec biens
**POST** `/api/collecte/contribuables`  
**Rôles:** CONTROLLEUR, ADMIN, INFORMATICIEN

**Payload :**
```json
{
  "contribuable": {
    "nom": "Société XYZ",
    "adresse": "456 Avenue du Commerce",
    "telephone": "+243123456789",
    "email": "contact@xyz.cd",
    "type": "PERSONNE_MORALE"
  },
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
**Rôles:** ADMIN, DIRECTEUR, INFORMATICIEN, CONTROLLEUR

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

### Lister tous les contribuables
**GET** `/api/contribuables`  
**Rôles:** ADMIN, DIRECTEUR, INFORMATICIEN, CONTROLLEUR, CHEF_DE_BUREAU

**Paramètres de requête :**
- `page` (défaut: 0) - Numéro de page
- `size` (défaut: 10) - Taille de page
- `sort` (défaut: "id,asc") - Champ et direction de tri

**Réponse :**
```json
{
  "success": true,
  "data": {
    "contribuables": [
      {
        "id": "uuid",
        "nom": "Société ABC SARL",
        "adressePrincipale": "123 Avenue de la Paix",
        "email": "contact@abc.cd",
        "type": "PERSONNE_MORALE"
      }
    ],
    "totalElements": 25,
    "totalPages": 3
  }
}
```

---

## 🏠 GESTION DES PROPRIÉTÉS
**Base path:** `/api/proprietes`

### Créer une propriété
**POST** `/api/proprietes`  
**Rôles:** ADMIN, DIRECTEUR, INFORMATICIEN, TAXATEUR, CONTROLLEUR

**Payload :**
```json
{
  "designation": "Immeuble commercial",
  "adresse": "123 Avenue du Commerce",
  "superficie": 1500.0,
  "latitude": -4.123456,
  "longitude": 15.654321,
  "contribuableId": "uuid",
  "typeProprieteBatie": "IMMEUBLE_COMMERCIAL",
  "nombreNiveaux": 5,
  "nombrePieces": 20,
  "materiaux": "DURABLE"
}
```

### Lister les propriétés d'un contribuable
**GET** `/api/proprietes/contribuable/{contribuableId}`  
**Rôles:** ADMIN, DIRECTEUR, INFORMATICIEN, TAXATEUR, CHEF_DE_BUREAU, CONTRIBUABLE, CONTROLLEUR

**Réponse :**
```json
{
  "success": true,
  "data": {
    "proprietes": [
      {
        "id": "uuid",
        "designation": "Immeuble commercial",
        "adresse": "123 Avenue du Commerce",
        "superficie": 1500.0,
        "latitude": -4.123456,
        "longitude": 15.654321,
        "typeProprieteBatie": "IMMEUBLE_COMMERCIAL"
      }
    ]
  }
}
```

---

## 🔍 CONTRÔLE FISCAL
**Base path:** `/api/controles`

### Initier un contrôle fiscal
**POST** `/api/controles`  
**Rôles:** VERIFICATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, CONTROLLEUR

**Payload :**
```json
{
  "contribuableId": "uuid",
  "dateDebut": "2024-01-15T00:00:00Z",
  "dateFin": "2024-01-30T00:00:00Z",
  "motif": "Vérification périodique",
  "agentId": "uuid"
}
```

### Valider un contrôle fiscal
**POST** `/api/controles/{id}/valider`  
**Rôles:** CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, CONTROLLEUR

**Payload :**
```json
{
  "rapport": "Contrôle effectué avec succès. Aucune irrégularité détectée.",
  "conclusion": "CONFORME"
}
```

**Conclusions possibles :**
- `CONFORME` - Aucune irrégularité
- `NON_CONFORME` - Irrégularités détectées
- `PARTIELLEMENT_CONFORME` - Quelques irrégularités mineures

---

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

- GET `/types-contribuables` — Public
- GET `/types-proprietes` — Public
- GET `/types-materiaux` — Public
- GET `/types-impots` — Public
- GET `/taux-impots` — Public

## Calcul IRV
Controller: `TaxationController` — Base path: `/api/taxation`

- GET `/calculate/irv` — Roles: TAXATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, CONTRIBUABLE — Query: `puissanceCV`, `poids`

## Vignettes
Controller: `VignetteController` — Base path: `/api/vignettes`

- GET `/` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN, TAXATEUR, CHEF_DE_BUREAU
- GET `/{id}` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN, TAXATEUR, CHEF_DE_BUREAU
- POST `/` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN, TAXATEUR — Body: `Vignette`
- PUT `/{id}` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN, TAXATEUR — Body: `Vignette`
- DELETE `/{id}` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN

## Plaques
Controller: `PlaqueController` — Base path: `/api/plaques`

- GET `/` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN, TAXATEUR, CHEF_DE_BUREAU
- GET `/{id}` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN, TAXATEUR, CHEF_DE_BUREAU
- POST `/` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN, TAXATEUR — Body: `Plaque`
- PUT `/{id}` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN, TAXATEUR — Body: `Plaque`
- DELETE `/{id}` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN

## Certificats
Controller: `CertificatController` — Base path: `/api/certificats`

- GET `/` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN, TAXATEUR, CHEF_DE_BUREAU
- GET `/{id}` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN, TAXATEUR, CHEF_DE_BUREAU
- POST `/` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN, TAXATEUR — Body: `Certificat`
- PUT `/{id}` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN, TAXATEUR — Body: `Certificat`
- DELETE `/{id}` — Roles: ADMIN, DIRECTEUR, INFORMATICIEN
