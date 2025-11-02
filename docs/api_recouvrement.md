# Documentation API - Gestion du Recouvrement

Cette documentation détaille les endpoints disponibles pour la gestion du recouvrement des impôts dans l'API DPRIHKAT.

---

## Vue d'ensemble

Le recouvrement permet de gérer les dossiers de recouvrement pour les contribuables en situation d'impayés.

### Base URL
```
/api/dossiers-recouvrement
```

---

## Endpoints

### 1. Rechercher des dossiers de recouvrement
 
Recherche des dossiers selon différents critères.

- **URL**: `/api/dossiers-recouvrement/search`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `AGENT_RECOUVREMENT`, `CONTROLLEUR`
- **Paramètres**:
  - `contribuableId` (query, optionnel): UUID du contribuable
  - `statut` (query, optionnel): Statut du dossier
  - `dateDebut` (query, optionnel): Date de début (format: YYYY-MM-DD)
  - `dateFin` (query, optionnel): Date de fin (format: YYYY-MM-DD)

#### Exemple de requête

```
GET /api/dossiers-recouvrement/search?statut=EN_COURS&dateDebut=2024-01-01&dateFin=2024-12-31
```

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "dossiers": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "contribuable": {
          "id": "550e8400-e29b-41d4-a716-446655440001",
          "nom": "KABILA",
          "numeroIdentificationContribuable": "NIF-123456789"
        },
        "numeroDossier": "REC-2024-001",
        "montantDu": 150000.0,
        "montantRecouvre": 50000.0,
        "solde": 100000.0,
        "statut": "EN_COURS",
        "dateOuverture": "2024-01-15T10:00:00",
        "taxations": [
          {
            "id": "550e8400-e29b-41d4-a716-446655440010",
            "natureImpot": {
              "code": "IF",
              "libelle": "Impôt Foncier"
            },
            "montant": 75000.0,
            "solde": 50000.0
          }
        ]
      }
    ]
  }
}
```

---

### 2. Exporter un dossier de recouvrement

Exporte un dossier de recouvrement au format PDF.

- **URL**: `/api/dossiers-recouvrement/{id}/export`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `AGENT_RECOUVREMENT`
- **Paramètres**:
  - `id` (path): UUID du dossier

---

## Structure de l'entité DossierRecouvrement

| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant unique |
| contribuable | Contribuable | Contribuable concerné |
| numeroDossier | String | Numéro unique du dossier |
| montantDu | Double | Montant total dû |
| montantRecouvre | Double | Montant déjà recouvré |
| solde | Double | Solde restant |
| statut | Enum | Statut du dossier |
| dateOuverture | DateTime | Date d'ouverture |
| dateCloture | DateTime | Date de clôture |
| taxations | List | Taxations concernées |

---

## Énumérations

### StatutDossierRecouvrement
- `OUVERT`: Dossier ouvert
- `EN_COURS`: En cours de traitement
- `SUSPENDU`: Temporairement suspendu
- `CLOTURE`: Clôturé (recouvrement complet)
- `ANNULE`: Annulé

---

## Règles métier

### Ouverture de dossier
- Un dossier est ouvert automatiquement après plusieurs relances infructueuses
- Seuil par défaut: 3 relances sans réponse
- Délai minimum: 90 jours après la date limite de paiement

### Calcul du solde
- `solde = montantDu - montantRecouvre`
- Mis à jour automatiquement à chaque paiement

### Clôture
- Un dossier est clôturé quand le solde = 0
- Peut être clôturé manuellement par un administrateur
