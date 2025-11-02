# Documentation API - Gestion des Vignettes

Cette documentation détaille les endpoints disponibles pour la gestion des vignettes automobiles dans l'API DPRIHKAT.

---

## Vue d'ensemble

Les vignettes sont des taxes annuelles obligatoires pour tous les véhicules circulant en RDC.

### Base URL
```
/api/vignettes
```

---

## Endpoints

### 1. Générer une vignette

Génère une nouvelle vignette pour un véhicule.

- **URL**: `/api/vignettes/generate/{vehiculeId}`
- **Méthode**: `POST`
- **Rôles autorisés**: `TAXATEUR`, `ADMIN`
- **Paramètres**:
  - `vehiculeId` (path): UUID du véhicule
  - `dateExpirationMillis` (form): Date d'expiration en millisecondes
  - `montant` (form, optionnel): Montant de la vignette
  - `puissance` (form, optionnel): Puissance fiscale
  - `document` (form): Fichier PDF de la vignette

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "vignette": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "vehicule": {
        "id": "550e8400-e29b-41d4-a716-446655440001",
        "immatriculation": "CD-123-ABC",
        "marque": "Toyota",
        "modele": "Corolla"
      },
      "numeroVignette": "VIG-2024-001",
      "dateEmission": "2024-01-15T10:00:00",
      "dateExpiration": "2024-12-31",
      "montant": 50000.0,
      "statut": "ACTIVE",
      "document": "vignette_2024_001.pdf"
    },
    "message": "Vignette générée avec succès"
  }
}
```

---

### 2. Récupérer les vignettes d'un véhicule

Récupère l'historique des vignettes d'un véhicule.

- **URL**: `/api/vignettes/vehicle/{vehiculeId}`
- **Méthode**: `GET`
- **Rôles autorisés**: `TAXATEUR`, `RECEVEUR_DES_IMPOTS`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `CONTRIBUABLE`, `ADMIN`
- **Paramètres**:
  - `vehiculeId` (path): UUID du véhicule
  - `page` (query, optionnel): Numéro de page (défaut: 0)
  - `size` (query, optionnel): Taille de page (défaut: 10)

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "vignettes": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "numeroVignette": "VIG-2024-001",
        "dateEmission": "2024-01-15T10:00:00",
        "dateExpiration": "2024-12-31",
        "montant": 50000.0,
        "statut": "ACTIVE"
      },
      {
        "id": "550e8400-e29b-41d4-a716-446655440002",
        "numeroVignette": "VIG-2023-001",
        "dateEmission": "2023-01-15T10:00:00",
        "dateExpiration": "2023-12-31",
        "montant": 45000.0,
        "statut": "EXPIREE"
      }
    ],
    "currentPage": 0,
    "totalItems": 2,
    "totalPages": 1
  }
}
```

---

### 3. Télécharger le document d'une vignette

Télécharge le PDF de la vignette.

- **URL**: `/api/vignettes/{id}/document`
- **Méthode**: `GET`
- **Rôles autorisés**: Tous (authentifiés)
- **Paramètres**:
  - `id` (path): UUID de la vignette

---

### 4. Vérifier la validité d'une vignette

Vérifie si une vignette est valide.

- **URL**: `/api/vignettes/verify/{numeroVignette}`
- **Méthode**: `GET`
- **Rôles autorisés**: Tous
- **Paramètres**:
  - `numeroVignette` (path): Numéro de la vignette

---

## Structure de l'entité Vignette

| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant unique |
| vehicule | Vehicule | Véhicule concerné |
| numeroVignette | String | Numéro unique de vignette |
| dateEmission | DateTime | Date d'émission |
| dateExpiration | Date | Date d'expiration |
| montant | Double | Montant de la vignette |
| statut | Enum | Statut de la vignette |
| document | String | Nom du fichier PDF |
| agent | Utilisateur | Agent émetteur |

---

## Énumérations

### StatutVignette
- `ACTIVE`: Vignette active et valide
- `EXPIREE`: Vignette expirée
- `ANNULEE`: Vignette annulée

---

## Règles métier

### Calcul du montant
- Basé sur la puissance fiscale du véhicule
- Taux définis dans la réglementation
- Peut varier selon le type de véhicule

### Période de validité
- Durée: 1 an à partir de la date d'émission
- Renouvellement obligatoire avant expiration
- Pénalités en cas de retard

### Document
- Format PDF uniquement
- Contient QR code pour vérification
- Doit être conservé dans le véhicule

### Contrôle
- Vérification possible via numéro de vignette
- Contrôle routier par les autorités
- Sanctions en cas d'absence de vignette valide
