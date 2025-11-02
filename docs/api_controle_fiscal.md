# Documentation API - Contrôle Fiscal

Cette documentation détaille les endpoints disponibles pour le contrôle fiscal dans l'API DPRIHKAT.

---

## Vue d'ensemble

Le contrôle fiscal permet de détecter les anomalies et de générer des rapports sur la situation fiscale.

### Base URL
```
/api/controle-fiscal
```

---

## Endpoints

### 1. Récupérer les anomalies

Récupère la liste des anomalies fiscales détectées.

- **URL**: `/api/controle-fiscal/anomalies`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`, `CONTROLLEUR`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`
- **Paramètres**:
  - `page` (query, optionnel): Numéro de page (défaut: 0)
  - `size` (query, optionnel): Taille de page (défaut: 10)

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "anomalies": [
      {
        "type": "PROPRIETE_NON_DECLAREE",
        "severite": "HAUTE",
        "contribuable": {
          "id": "550e8400-e29b-41d4-a716-446655440001",
          "nom": "KABILA",
          "numeroIdentificationContribuable": "NIF-123456789"
        },
        "description": "Propriété détectée mais non déclarée",
        "details": {
          "proprieteId": "550e8400-e29b-41d4-a716-446655440010",
          "adresse": "Avenue de la Liberté",
          "type": "VI"
        },
        "dateDetection": "2024-10-27T12:00:00",
        "statut": "NON_TRAITEE"
      },
      {
        "type": "RETARD_PAIEMENT",
        "severite": "MOYENNE",
        "contribuable": {
          "id": "550e8400-e29b-41d4-a716-446655440002",
          "nom": "TSHISEKEDI"
        },
        "description": "Paiement en retard de plus de 90 jours",
        "details": {
          "taxationId": "550e8400-e29b-41d4-a716-446655440020",
          "montantDu": 75000.0,
          "joursRetard": 95
        },
        "dateDetection": "2024-10-27T12:00:00",
        "statut": "EN_COURS"
      }
    ],
    "currentPage": 0,
    "totalItems": 45,
    "totalPages": 5
  }
}
```

---

### 2. Générer un rapport fiscal

Génère un rapport fiscal pour une période donnée.

- **URL**: `/api/controle-fiscal/rapport`
- **Méthode**: `GET`
- **Rôles autorisés**: `CONTROLLEUR`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `ADMIN`
- **Paramètres**:
  - `startDate` (query): Date de début (format: YYYY-MM-DD)
  - `endDate` (query): Date de fin (format: YYYY-MM-DD)

#### Exemple de requête

```
GET /api/controle-fiscal/rapport?startDate=2024-01-01&endDate=2024-12-31
```

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "rapport": {
      "periode": {
        "debut": "2024-01-01",
        "fin": "2024-12-31"
      },
      "statistiques": {
        "nombreContribuables": 1250,
        "nombreDeclarations": 3450,
        "nombreAnomalies": 45,
        "tauxConformite": 96.4
      },
      "anomaliesParType": [
        {
          "type": "PROPRIETE_NON_DECLAREE",
          "nombre": 15,
          "pourcentage": 33.3
        },
        {
          "type": "RETARD_PAIEMENT",
          "nombre": 20,
          "pourcentage": 44.4
        },
        {
          "type": "DECLARATION_INCOMPLETE",
          "nombre": 10,
          "pourcentage": 22.2
        }
      ],
      "montantsRecuperes": {
        "total": 2500000.0,
        "parType": [
          {
            "natureImpot": "IF",
            "montant": 1500000.0
          },
          {
            "natureImpot": "IRV",
            "montant": 1000000.0
          }
        ]
      },
      "recommandations": [
        "Intensifier les contrôles sur les propriétés non déclarées",
        "Renforcer les relances pour les retards de paiement"
      ]
    },
    "message": "Rapport fiscal généré avec succès"
  }
}
```

---

### 3. Vérifier la conformité d'un contribuable

Vérifie si un contribuable est en conformité fiscale.

- **URL**: `/api/controle-fiscal/verifier/{contribuableId}`
- **Méthode**: `GET`
- **Rôles autorisés**: `CONTROLLEUR`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `ADMIN`

---

## Types d'anomalies détectées

### 1. Propriétés non déclarées
- Détection automatique des propriétés non déclarées
- Comparaison avec les données cadastrales
- Sévérité: HAUTE

### 2. Retards de paiement
- Taxations impayées au-delà de la date limite
- Calcul automatique des jours de retard
- Sévérité: MOYENNE à HAUTE (selon durée)

### 3. Déclarations incomplètes
- Déclarations avec informations manquantes
- Vérification de la cohérence des données
- Sévérité: MOYENNE

### 4. Incohérences de données
- Différences entre déclarations et réalité
- Valeurs aberrantes
- Sévérité: MOYENNE

### 5. Véhicules non déclarés
- Véhicules immatriculés mais non déclarés
- Croisement avec base de données véhicules
- Sévérité: HAUTE

---

## Énumérations

### TypeAnomalie
- `PROPRIETE_NON_DECLAREE`
- `VEHICULE_NON_DECLARE`
- `RETARD_PAIEMENT`
- `DECLARATION_INCOMPLETE`
- `INCOHERENCE_DONNEES`
- `DOUBLE_DECLARATION`

### SeveriteAnomalie
- `BASSE`: Anomalie mineure
- `MOYENNE`: Nécessite attention
- `HAUTE`: Nécessite action immédiate
- `CRITIQUE`: Fraude potentielle

### StatutAnomalie
- `NON_TRAITEE`: Détectée mais non traitée
- `EN_COURS`: En cours de traitement
- `RESOLUE`: Résolue
- `CLASSEE`: Classée sans suite

---

## Règles métier

### Détection automatique
- Les anomalies sont détectées automatiquement par le système
- Exécution quotidienne des contrôles
- Notification aux agents concernés

### Priorisation
- Les anomalies sont triées par sévérité
- Les anomalies critiques sont traitées en priorité
- Suivi de l'état de traitement

### Rapports
- Génération de rapports périodiques
- Statistiques et tendances
- Recommandations automatiques

### Actions correctives
- Notification au contribuable
- Relances automatiques
- Ouverture de dossier de contrôle si nécessaire

---

## Codes d'erreur

| Code | Description |
|------|-------------|
| ANOMALIES_FETCH_ERROR | Erreur récupération anomalies |
| RAPPORT_GENERATION_ERROR | Erreur génération rapport |
| INVALID_DATE_RANGE | Période invalide |
| CONTRIBUABLE_NOT_FOUND | Contribuable non trouvé |
