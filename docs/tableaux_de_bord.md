# API des Tableaux de Bord et Rapports

## Tableaux de Bord (Dashboards)

`GET /api/dashboard`

### Description
Récupère les données du tableau de bord selon le rôle de l'utilisateur et une période donnée. Le contenu du tableau de bord est adapté en fonction du rôle de l'utilisateur connecté.

### Paramètres
- `startDate` (optionnel) - Date de début (format ISO8601)
- `endDate` (optionnel) - Date de fin (format ISO8601)

### Rôles autorisés
- ADMIN
- DIRECTEUR
- CHEF_DE_DIVISION
- CHEF_DE_BUREAU
- TAXATEUR
- RECEVEUR_DES_IMPOTS
- APUREUR
- CONTROLLEUR
- VERIFICATEUR
- CONTRIBUABLE

### Données spécifiques par rôle

#### DIRECTEUR / CHEF_DE_DIVISION / ADMIN
Accès à toutes les données du tableau de bord, y compris :
- Statistiques globales (contribuables, véhicules, propriétés)
- Données financières complètes
- Statistiques d'apurements
- Tendances mensuelles
- Alertes et indicateurs

#### RECEVEUR_DES_IMPOTS / APUREUR
Focus sur les données de recouvrement :
- Données financières (taxations, paiements, impayés)
- Statistiques d'apurements détaillées
- Taux de recouvrement et d'apurement
- Relances en cours et effectuées

#### CHEF_DE_BUREAU / TAXATEUR
Focus sur les certificats et déclarations :
- Statistiques des véhicules et propriétés
- Répartition par type d'impôt
- Statistiques des déclarations par statut

#### CONTROLLEUR / VERIFICATEUR
Focus sur les contribuables :
- Statistiques des contribuables (actifs/inactifs)
- Statistiques des véhicules et propriétés
- Répartition par type d'impôt

### Réponse
```json
{
  "success": true,
  "data": {
    "dashboard": {
      "dateReference": "2025-09-22",
      "dateDebut": "2025-09-01",
      "dateFin": "2025-09-21",
      
      "totalContribuables": 1500,
      "totalVehicules": 2500,
      "totalProprietes": 1200,
      "totalDeclarations": 3000,
      
      "montantTotalTaxations": 5000000,
      "montantTotalPaiements": 4500000,
      "montantTotalImpayes": 500000,
      
      "totalApurements": 250,
      "montantTotalApurements": 300000,
      "statsParTypeApurement": {
        "REMISE_GRACIEUSE": 50,
        "ECHELONNEMENT": 100,
        "TRANSACTION": 100
      },
      "montantParTypeApurement": {
        "REMISE_GRACIEUSE": 100000,
        "ECHELONNEMENT": 150000,
        "TRANSACTION": 50000
      },
      
      "statsParTypeImpot": {
        "VIGNETTE_AUTOMOBILE": 3000000,
        "IMPOT_FONCIER": 2000000
      },
      "statsParStatutPaiement": {
        "PAYE": 800,
        "EN_RETARD": 150,
        "EN_ATTENTE": 50
      },
      "statsParStatutDeclaration": {
        "VALIDEE": 2500,
        "EN_ATTENTE": 300,
        "EN_RETARD": 200
      },
      
      "declarationsEnRetard": 200,
      "paiementsEnRetard": 150,
      "relancesEnCours": 75,
      "relancesEffectuees": 120,
      
      "tendanceMensuelleRecettes": {
        "Septembre 2025": 1500000,
        "Août 2025": 1200000,
        "Juillet 2025": 1300000
      },
      "tendanceMensuelleDeclarations": {
        "Septembre 2025": 500,
        "Août 2025": 450,
        "Juillet 2025": 480
      },
      
      "donneesSpecifiques": {
        "tauxRecouvrement": 90.0,
        "tauxApurement": 6.0,
        "contribuablesActifs": 1400,
        "contribuablesInactifs": 100
      }
    }
  },
  "error": null
}

## Rapports Détaillés

`GET /api/reports/{reportType}`

### Description
Génère un rapport détaillé selon le type spécifié et une période donnée.

### Types de rapports disponibles
- TAXATION
- PAIEMENT
- APUREMENT
- VIGNETTE
- CERTIFICAT
- RELANCE

### Paramètres
- `startDate` - Date de début (format ISO8601)
- `endDate` - Date de fin (format ISO8601)
- Filtres supplémentaires selon le type de rapport

### Rôles autorisés
- ADMIN
- DIRECTEUR
- CHEF_DE_DIVISION
- CHEF_DE_BUREAU
- RECEVEUR_DES_IMPOTS
- APUREUR

### Exemple de réponse pour un rapport d'apurement
```json
{
  "success": true,
  "data": {
    "report": {
      "typeReport": "APUREMENT",
      "dateDebut": "2025-09-01",
      "dateFin": "2025-09-21",
      "items": [
        {
          "reference": "APU-2025-00001",
          "date": "2025-09-05",
          "type": "REMISE_GRACIEUSE",
          "contribuableNom": "Jean Dupont",
          "montant": 50000,
          "statut": "ACCEPTEE"
        }
      ],
      "totalItems": 1,
      "totalAmount": 50000,
      "summaryByType": {
        "REMISE_GRACIEUSE": 50000
      }
    },
    "startDate": "2025-09-01",
    "endDate": "2025-09-21"
  },
  "error": null
}

## Tendances et Statistiques

### Tendances Mensuelles

Le tableau de bord inclut maintenant des tendances mensuelles pour :
- Les recettes (montants des paiements par mois)
- Les déclarations (nombre de déclarations par mois)

Ces données permettent de visualiser l'évolution des activités sur une période donnée.

### Statistiques d'Apurements

Les statistiques d'apurements incluent :
- Nombre total d'apurements
- Montant total apuré
- Répartition par type d'apurement (REMISE_GRACIEUSE, ECHELONNEMENT, TRANSACTION)
- Montants par type d'apurement

### Indicateurs de Performance

Pour les rôles administratifs et de recouvrement, des indicateurs de performance sont disponibles :
- Taux de recouvrement (paiements / taxations)
- Taux d'apurement (apurements / taxations)
- Nombre de relances effectuées et en cours

## Utilisation Recommandée

### Filtrage par Date

Pour obtenir des statistiques pertinentes, il est recommandé de spécifier une période :
```
GET /api/dashboard?startDate=2025-09-01&endDate=2025-09-30
```

### Analyse des Tendances

Les tendances mensuelles peuvent être utilisées pour :
- Identifier les périodes de forte activité
- Détecter les anomalies dans les recettes
- Planifier les ressources en fonction des pics d'activité

### Suivi des Apurements

Le suivi des apurements permet de :
- Évaluer l'efficacité des différentes modalités d'apurement
- Identifier les types d'apurements les plus utilisés
- Mesurer l'impact financier des apurements sur les recettes
