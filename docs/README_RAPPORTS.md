# Module de Rapports - Guide de Démarrage Rapide

## Introduction

Le module de rapports du système DPRIHKAT permet de générer des rapports analytiques détaillés sur les activités fiscales. Ce guide vous aidera à démarrer rapidement avec le module.

## Composants du Module

### 1. Enums
- **`TypeRapport`** : Types de rapports disponibles (TAXATION, PAIEMENT, RELANCE, COLLECTE, RECOUVREMENT, GLOBAL)
- **`PeriodeRapport`** : Périodes prédéfinies (JOUR, SEMAINE, MOIS, TRIMESTRE, SEMESTRE, ANNEE, PERSONNALISEE)

### 2. DTOs
- **`RapportRequestDTO`** : Requête pour générer un rapport
- **`RapportResponseDTO`** : Réponse contenant le rapport complet avec statistiques
- **`RapportTaxationDTO`** : Détails d'une taxation dans le rapport
- **`RapportPaiementDTO`** : Détails d'un paiement dans le rapport
- **`RapportRelanceDTO`** : Détails d'une relance dans le rapport
- **`RapportRecouvrementDTO`** : Détails d'un acte de recouvrement
- **`RapportCollecteDTO`** : Détails d'une collecte de données

### 3. Service
- **`RapportAnalytiqueService`** : Service principal pour la génération de rapports

### 4. Contrôleur
- **`RapportAnalytiqueController`** : Endpoints REST pour accéder aux rapports

## Démarrage Rapide

### Exemple 1 : Rapport mensuel des taxations

```bash
curl -X GET "http://localhost:8080/api/rapports/taxations?periode=MOIS" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Exemple 2 : Rapport des paiements d'un agent

```bash
curl -X GET "http://localhost:8080/api/rapports/paiements?periode=SEMAINE&agentId=agent-uuid" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Exemple 3 : Rapport global annuel

```bash
curl -X GET "http://localhost:8080/api/rapports/global?periode=ANNEE" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Exemple 4 : Rapport personnalisé

```bash
curl -X POST "http://localhost:8080/api/rapports/generer" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "typeRapport": "TAXATION",
    "periode": "PERSONNALISEE",
    "dateDebut": "2025-01-01",
    "dateFin": "2025-01-31",
    "agentId": "uuid-agent"
  }'
```

## Structure de la Réponse

```json
{
  "typeRapport": "TAXATION",
  "periode": "MOIS",
  "dateDebut": "2025-01-01T00:00:00Z",
  "dateFin": "2025-01-31T23:59:59Z",
  "dateGeneration": "2025-01-15T10:30:00Z",
  "agentNom": "KABILA Jean",
  "agentMatricule": "MAT001",
  "statistiquesGlobales": {
    "nombreTaxations": 150,
    "montantTotalTaxations": 50000.00,
    "nombrePaiements": 120,
    "montantTotalPaiements": 45000.00,
    "nombreRelances": 30,
    "nombreActesRecouvrement": 15,
    "montantTotalRecouvrement": 10000.00,
    "nombreCollectes": 200,
    "repartitionParType": {
      "IF": 80,
      "IRL": 50,
      "ICM": 20
    },
    "repartitionMontantsParType": {
      "IF": 30000.00,
      "IRL": 15000.00,
      "ICM": 5000.00
    }
  },
  "taxations": [...],
  "paiements": [...],
  "relances": [...],
  "recouvrements": [...],
  "collectes": [...]
}
```

## Permissions

| Endpoint | Rôles Autorisés |
|----------|----------------|
| `/taxations` | TAXATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, ADMIN |
| `/paiements` | RECEVEUR_DES_IMPOTS, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, ADMIN |
| `/relances` | CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, ADMIN |
| `/recouvrements` | CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, ADMIN |
| `/collectes` | TAXATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, ADMIN |
| `/global` | CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, ADMIN |

## Filtres Disponibles

### Par Période
- `JOUR` : Données du jour en cours
- `SEMAINE` : Données de la semaine en cours (lundi-dimanche)
- `MOIS` : Données du mois en cours
- `TRIMESTRE` : Données du trimestre en cours
- `SEMESTRE` : Données du semestre en cours
- `ANNEE` : Données de l'année en cours
- `PERSONNALISEE` : Période personnalisée avec dateDebut et dateFin

### Par Agent
Ajoutez le paramètre `agentId` pour filtrer les données par agent spécifique.

### Par Bureau/Division
Ajoutez `bureauId` ou `divisionId` pour filtrer par entité administrative (à implémenter).

## Cas d'Usage Courants

### 1. Évaluation de Performance d'un Agent
```http
GET /api/rapports/taxations?periode=MOIS&agentId=agent-uuid
```

### 2. Suivi des Encaissements
```http
GET /api/rapports/paiements?periode=SEMAINE
```

### 3. Analyse des Actions de Recouvrement
```http
GET /api/rapports/recouvrements?periode=TRIMESTRE
```

### 4. Rapport de Direction
```http
GET /api/rapports/global?periode=ANNEE
```

### 5. Audit sur Période Spécifique
```http
GET /api/rapports/personnalise?typeRapport=GLOBAL&dateDebut=2024-01-01&dateFin=2024-12-31
```

## Intégration Frontend

### React/Vue.js Example

```javascript
async function genererRapport(typeRapport, periode, agentId = null) {
  const params = new URLSearchParams({
    periode: periode
  });
  
  if (agentId) {
    params.append('agentId', agentId);
  }
  
  const response = await fetch(
    `http://localhost:8080/api/rapports/${typeRapport}?${params}`,
    {
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    }
  );
  
  return await response.json();
}

// Utilisation
const rapport = await genererRapport('taxations', 'MOIS', 'agent-uuid');
console.log(rapport.statistiquesGlobales);
```

## Dépannage

### Erreur 401 - Non autorisé
Vérifiez que votre token JWT est valide et que vous avez les permissions nécessaires.

### Erreur 400 - Requête invalide
Pour une période PERSONNALISEE, assurez-vous de fournir dateDebut et dateFin.

### Données vides
Vérifiez que la période sélectionnée contient des données. Essayez une période plus large.

## Prochaines Fonctionnalités

- Export PDF/Excel
- Graphiques et visualisations
- Rapports programmés automatiques
- Comparaisons inter-périodes
- Alertes sur anomalies

## Documentation Complète

Pour plus de détails, consultez [module_rapports.md](./module_rapports.md)

## Support

Pour toute question, contactez l'équipe de développement DPRIHKAT.
