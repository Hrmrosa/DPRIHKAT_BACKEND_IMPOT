# Documentation API - Dashboard et Statistiques

Cette documentation détaille les endpoints disponibles pour le dashboard et les statistiques dans l'API DPRIHKAT.

---

## Vue d'ensemble

Le dashboard fournit des statistiques et indicateurs clés sur les activités de la DPRIHKAT.

### Base URL
```
/api/dashboard
```
 
---

## Endpoints

### 1. Statistiques générales (authentifié)

Récupère toutes les statistiques du système (accès restreint).

- **URL**: `/api/dashboard/statistiques`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "total_contribuables": 1250,
    "contribuables_actifs": 1180,
    "contribuables_par_type": [
      {"type": "PERSONNE_PHYSIQUE", "count": 950},
      {"type": "PERSONNE_MORALE", "count": 300}
    ],
    "total_proprietes": 3450,
    "proprietes_declarees": 3200,
    "proprietes_par_type": [
      {"type": "VI", "count": 1500},
      {"type": "AP", "count": 1200},
      {"type": "TE", "count": 750}
    ],
    "total_vehicules": 2100,
    "total_plaques": 2500,
    "plaques_disponibles": 400,
    "plaques_attribuees": 2100,
    "total_taxations": 5550,
    "montant_total_taxations": 12500000.00,
    "montant_total_paiements": 9800000.00,
    "taux_recouvrement": 78.40,
    "declarations_en_attente": 45,
    "declarations_validees": 1205
  },
  "timestamp": 1730028220000
}
```

---

### 2. Statistiques publiques (sans authentification)

Récupère les statistiques publiques accessibles à tous.

- **URL**: `/api/dashboard/public/statistics`
- **Méthode**: `GET`
- **Rôles autorisés**: Aucun (accès public)
- **Paramètres**: Aucun

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "total_contribuables": 1250,
    "contribuables_par_type": [
      {"type": "PERSONNE_PHYSIQUE", "count": 950},
      {"type": "PERSONNE_MORALE", "count": 300}
    ],
    "total_proprietes": 3450,
    "proprietes_par_type": [
      {"type": "VI", "count": 1500},
      {"type": "AP", "count": 1200},
      {"type": "TE", "count": 750}
    ],
    "total_vehicules": 2100,
    "total_plaques": 2500,
    "plaques_disponibles": 400,
    "plaques_attribuees": 2100,
    "montant_total_taxations": 12500000.00,
    "montant_total_paiements": 9800000.00,
    "taux_recouvrement": 78.40
  },
  "timestamp": 1730028220000
}
```

---

### 3. Statistiques par période

Récupère les statistiques pour une période donnée.

- **URL**: `/api/dashboard/statistiques/periode`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`
- **Paramètres**:
  - `dateDebut` (query): Date de début (format: YYYY-MM-DD)
  - `dateFin` (query): Date de fin (format: YYYY-MM-DD)

#### Exemple de requête

```
GET /api/dashboard/statistiques/periode?dateDebut=2024-01-01&dateFin=2024-12-31
```

---

### 4. Statistiques par nature d'impôt

Récupère les statistiques groupées par nature d'impôt.

- **URL**: `/api/dashboard/statistiques/natures-impot`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "statistiques": [
      {
        "natureImpot": {
          "code": "IF",
          "libelle": "Impôt Foncier"
        },
        "nombreTaxations": 3450,
        "montantTotal": 8500000.00,
        "montantPaye": 6800000.00,
        "tauxRecouvrement": 80.00
      },
      {
        "natureImpot": {
          "code": "IRV",
          "libelle": "Impôt sur les Revenus Locatifs des Véhicules"
        },
        "nombreTaxations": 2100,
        "montantTotal": 4000000.00,
        "montantPaye": 3000000.00,
        "tauxRecouvrement": 75.00
      }
    ]
  }
}
```

---

### 5. Statistiques par bureau

Récupère les statistiques groupées par bureau.

- **URL**: `/api/dashboard/statistiques/bureaux`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `CHEF_DE_DIVISION`

---

## Données disponibles

### Contribuables
- Nombre total de contribuables
- Contribuables actifs/inactifs
- Répartition par type (Personne Physique/Morale)
- Répartition par bureau

### Biens
- **Propriétés**: Total, déclarées, par type
- **Véhicules**: Total, par type
- **Plaques**: Total, disponibles, attribuées

### Finances
- Montant total des taxations
- Montant total des paiements
- Taux de recouvrement global
- Statistiques par nature d'impôt

### Déclarations
- Déclarations en attente
- Déclarations validées
- Déclarations rejetées
- Déclarations par période

---

## Règles métier

### Accès public
- Les statistiques publiques ne contiennent aucune donnée sensible
- Données agrégées et anonymisées uniquement
- Pas d'authentification requise

### Accès restreint
- Les statistiques détaillées nécessitent une authentification
- Accès limité selon le rôle de l'utilisateur
- Certaines données sont filtrées par bureau/division

### Calculs
- Le taux de recouvrement = (montant payé / montant taxé) × 100
- Les statistiques sont calculées en temps réel
- Possibilité de mise en cache pour optimisation

---

## Codes d'erreur

| Code | Description |
|------|-------------|
| STATISTICS_ERROR | Erreur récupération statistiques |
| PUBLIC_STATISTICS_ERROR | Erreur statistiques publiques |
| INVALID_DATE_RANGE | Période invalide |
| UNAUTHORIZED_ACCESS | Accès non autorisé |
