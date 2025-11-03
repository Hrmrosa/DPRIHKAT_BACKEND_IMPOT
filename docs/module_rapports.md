# Module de Rapports Analytiques - DPRIHKAT

## Vue d'ensemble

Le module de rapports analytiques permet de générer des rapports détaillés sur les différentes activités du système DPRIHKAT. Il offre une vision complète des taxations, paiements, relances, actes de recouvrement et collecte de données.

## Types de rapports disponibles

### 1. Rapport sur les Taxations (`TAXATION`)
Fournit des informations détaillées sur toutes les taxations effectuées pendant une période donnée.

**Données incluses :**
- Numéro de taxation
- Date de taxation
- Montant et devise
- Statut de la taxation
- Type d'impôt
- Exercice fiscal
- Informations sur l'agent taxateur
- Informations sur le contribuable
- Statut d'exonération

**Statistiques calculées :**
- Nombre total de taxations
- Montant total taxé
- Répartition par type d'impôt
- Répartition des montants par type

### 2. Rapport sur les Paiements (`PAIEMENT`)
Analyse tous les paiements reçus pendant la période spécifiée.

**Données incluses :**
- Date du paiement
- Montant
- Statut du paiement
- Mode de paiement (espèces, chèque, virement, etc.)
- Bordereau bancaire
- Informations bancaires
- Informations sur le contribuable
- Numéro de taxation associé

**Statistiques calculées :**
- Nombre total de paiements
- Montant total encaissé
- Répartition par mode de paiement

### 3. Rapport sur les Relances (`RELANCE`)
Suivi des relances envoyées aux contribuables.

**Données incluses :**
- Date d'envoi
- Type de relance
- Statut de la relance
- Contenu de la relance
- Informations sur le contribuable
- Dossier de recouvrement associé

**Statistiques calculées :**
- Nombre total de relances
- Répartition par type de relance
- Taux de réponse

### 4. Rapport sur les Actes de Recouvrement (`RECOUVREMENT`)
Détails sur tous les actes de recouvrement générés.

**Données incluses :**
- Type de document (AMR, MED, Commandement, etc.)
- Statut du document
- Date de génération
- Date de notification
- Référence du document
- Montants (principal, pénalités, total)
- Informations sur le contribuable
- Agent générateur et notificateur

**Statistiques calculées :**
- Nombre total d'actes
- Montant total en recouvrement
- Répartition par type d'acte

### 5. Rapport sur la Collecte de Données (`COLLECTE`)
Suivi de toutes les collectes de données effectuées.

**Données incluses :**
- Date de collecte
- Type de collecte (déclaration, taxation, etc.)
- Agent collecteur
- Contribuable concerné
- Type d'impôt
- Montant
- Statut

**Statistiques calculées :**
- Nombre total de collectes
- Répartition par type
- Performance par agent

### 6. Rapport Global (`GLOBAL`)
Combine tous les types de rapports en une vue d'ensemble complète.

## Périodes de rapport

Le système supporte plusieurs périodes prédéfinies :

- **JOUR** : Rapport journalier (jour en cours)
- **SEMAINE** : Rapport hebdomadaire (du lundi au dimanche)
- **MOIS** : Rapport mensuel (du 1er au dernier jour du mois)
- **TRIMESTRE** : Rapport trimestriel (3 mois)
- **SEMESTRE** : Rapport semestriel (6 mois)
- **ANNEE** : Rapport annuel (année civile)
- **PERSONNALISEE** : Période personnalisée avec dates de début et fin

## Endpoints API

### 1. Génération de rapport générique

```http
POST /api/rapports/generer
```

**Corps de la requête :**
```json
{
  "typeRapport": "TAXATION",
  "periode": "MOIS",
  "dateDebut": "2025-01-01",
  "dateFin": "2025-01-31",
  "agentId": "uuid-de-l-agent",
  "bureauId": "uuid-du-bureau",
  "divisionId": "uuid-de-la-division"
}
```

**Réponse :**
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
  "taxations": [
    {
      "id": "uuid",
      "numeroTaxation": "T_0001_IF_BUR001_2025",
      "dateTaxation": "2025-01-10T00:00:00Z",
      "montant": 500.00,
      "devise": "USD",
      "statut": "PAYEE",
      "typeImpot": "IF",
      "exercice": "2025",
      "nomAgent": "KABILA Jean",
      "matriculeAgent": "MAT001",
      "nomContribuable": "MUKENDI Pierre",
      "numeroContribuable": "CONT001",
      "exoneration": false
    }
  ]
}
```

### 2. Rapport sur les taxations

```http
GET /api/rapports/taxations?periode=MOIS&agentId=uuid
```

**Paramètres :**
- `periode` (optionnel) : JOUR, SEMAINE, MOIS, TRIMESTRE, SEMESTRE, ANNEE (défaut: MOIS)
- `dateDebut` (optionnel) : Date de début au format yyyy-MM-dd
- `dateFin` (optionnel) : Date de fin au format yyyy-MM-dd
- `agentId` (optionnel) : UUID de l'agent pour filtrer

### 3. Rapport sur les paiements

```http
GET /api/rapports/paiements?periode=SEMAINE
```

**Paramètres :** Identiques au rapport taxations

### 4. Rapport sur les relances

```http
GET /api/rapports/relances?periode=MOIS
```

**Paramètres :**
- `periode` (optionnel) : Période du rapport
- `dateDebut` (optionnel) : Date de début
- `dateFin` (optionnel) : Date de fin

### 5. Rapport sur les recouvrements

```http
GET /api/rapports/recouvrements?periode=TRIMESTRE&agentId=uuid
```

**Paramètres :** Identiques au rapport taxations

### 6. Rapport sur les collectes

```http
GET /api/rapports/collectes?periode=MOIS&agentId=uuid
```

**Paramètres :** Identiques au rapport taxations

### 7. Rapport global

```http
GET /api/rapports/global?periode=ANNEE
```

**Paramètres :** Identiques au rapport taxations

### 8. Rapport personnalisé

```http
GET /api/rapports/personnalise?typeRapport=TAXATION&dateDebut=2025-01-01&dateFin=2025-01-31
```

**Paramètres obligatoires :**
- `typeRapport` : Type de rapport (TAXATION, PAIEMENT, etc.)
- `dateDebut` : Date de début au format yyyy-MM-dd
- `dateFin` : Date de fin au format yyyy-MM-dd

**Paramètres optionnels :**
- `agentId` : UUID de l'agent

## Permissions requises

Les endpoints de rapports nécessitent les rôles suivants :

- **Rapports taxations** : TAXATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, ADMIN
- **Rapports paiements** : RECEVEUR_DES_IMPOTS, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, ADMIN
- **Rapports relances** : CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, ADMIN
- **Rapports recouvrements** : CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, ADMIN
- **Rapports collectes** : TAXATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, ADMIN
- **Rapport global** : CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, ADMIN
- **Génération générique** : CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, ADMIN

## Exemples d'utilisation

### Exemple 1 : Rapport mensuel des taxations d'un agent

```bash
curl -X GET "http://localhost:8080/api/rapports/taxations?periode=MOIS&agentId=123e4567-e89b-12d3-a456-426614174000" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Exemple 2 : Rapport hebdomadaire des paiements

```bash
curl -X GET "http://localhost:8080/api/rapports/paiements?periode=SEMAINE" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Exemple 3 : Rapport personnalisé sur une période spécifique

```bash
curl -X GET "http://localhost:8080/api/rapports/personnalise?typeRapport=TAXATION&dateDebut=2025-01-01&dateFin=2025-01-31" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Exemple 4 : Rapport global annuel

```bash
curl -X GET "http://localhost:8080/api/rapports/global?periode=ANNEE" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Exemple 5 : Génération de rapport via POST

```bash
curl -X POST "http://localhost:8080/api/rapports/generer" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "typeRapport": "PAIEMENT",
    "periode": "TRIMESTRE",
    "agentId": "123e4567-e89b-12d3-a456-426614174000"
  }'
```

## Cas d'usage

### 1. Suivi de performance d'un agent taxateur

Un chef de bureau souhaite évaluer la performance d'un agent taxateur sur le mois écoulé :

```http
GET /api/rapports/taxations?periode=MOIS&agentId=agent-uuid
```

Le rapport retournera :
- Nombre de taxations effectuées
- Montant total taxé
- Répartition par type d'impôt
- Liste détaillée de toutes les taxations

### 2. Analyse des recouvrements trimestriels

Le directeur souhaite analyser l'efficacité des actions de recouvrement :

```http
GET /api/rapports/recouvrements?periode=TRIMESTRE
```

Le rapport montrera :
- Nombre d'actes générés par type
- Montants en recouvrement
- Agents les plus actifs
- Taux de notification

### 3. Rapport annuel pour la direction

Pour une présentation à la direction, un rapport global annuel :

```http
GET /api/rapports/global?periode=ANNEE
```

Ce rapport consolidé inclut :
- Toutes les taxations de l'année
- Tous les paiements reçus
- Toutes les relances envoyées
- Tous les actes de recouvrement
- Statistiques globales et comparatives

### 4. Audit d'une période spécifique

Pour un audit sur une période précise :

```http
GET /api/rapports/personnalise?typeRapport=GLOBAL&dateDebut=2024-06-01&dateFin=2024-12-31
```

## Bonnes pratiques

1. **Utiliser les périodes prédéfinies** quand possible pour une cohérence dans les rapports
2. **Filtrer par agent** pour des rapports de performance individuels
3. **Utiliser le rapport GLOBAL** pour des vues d'ensemble
4. **Générer des rapports réguliers** pour suivre les tendances
5. **Exporter les données** pour des analyses approfondies dans Excel ou autres outils

## Évolutions futures

- Export PDF/Excel des rapports
- Rapports programmés automatiques
- Graphiques et visualisations
- Comparaisons inter-périodes
- Alertes sur anomalies
- Rapports par bureau/division
- Intégration avec le module de dashboard

## Support

Pour toute question ou problème concernant le module de rapports, contactez l'équipe de développement DPRIHKAT.
