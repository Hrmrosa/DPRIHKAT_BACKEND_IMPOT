# Documentation du Tableau de Bord DPRIHKAT

## Introduction

Le tableau de bord DPRIHKAT est une interface centralisée qui fournit une vue d'ensemble du système d'imposition. Il est conçu pour offrir des statistiques, des graphiques et des informations pertinentes adaptées aux différents rôles d'utilisateurs dans le système.

## Types de Tableaux de Bord

Le système propose différents tableaux de bord selon le rôle de l'utilisateur connecté :

1. **Tableau de bord Administrateur** : Vue complète du système
2. **Tableau de bord Directeur** : Vue d'ensemble avec statistiques principales
3. **Tableau de bord Chef de Bureau** : Vue spécifique aux activités de bureau
4. **Tableau de bord Taxateur** : Vue centrée sur les activités de taxation
5. **Tableau de bord Contribuable** : Vue personnalisée pour les contribuables

## Fonctionnalités par Rôle

### Administrateur

L'administrateur a accès à toutes les fonctionnalités du tableau de bord :

- **Statistiques générales** : Nombre total de contribuables, propriétés, véhicules, utilisateurs
- **Montants financiers** : Total des taxations, total des paiements, taux de recouvrement
- **Graphiques** : Taxations et paiements par mois, répartition des propriétés et contribuables par type
- **Carte géographique** : Visualisation des propriétés sur une carte
- **Listes récentes** : Derniers utilisateurs, logs de connexion, taxations et paiements

### Directeur

Le directeur a accès à une vue similaire à celle de l'administrateur, mais avec moins de détails :

- **Statistiques générales** : Mêmes statistiques que l'administrateur
- **Graphiques** : Mêmes graphiques que l'administrateur
- **Carte géographique** : Même carte que l'administrateur
- **Listes récentes** : Taxations et paiements récents (nombre limité)

### Chef de Bureau

*À implémenter selon les besoins spécifiques*

### Taxateur

*À implémenter selon les besoins spécifiques*

### Contribuable

*À implémenter selon les besoins spécifiques*

## Structure des Données

### Statistiques Générales

```json
{
  "total_contribuables": 1250,
  "total_proprietes": 3450,
  "total_vehicules": 2100,
  "total_utilisateurs": 75,
  "montant_total_taxations": 1250000.00,
  "montant_total_paiements": 980000.00,
  "taux_recouvrement": 78.40
}
```

### Graphiques

#### Taxations par Mois

```json
[
  {
    "mois": "2025-01",
    "montant": 120000.00
  },
  {
    "mois": "2025-02",
    "montant": 135000.00
  },
  // ... autres mois
]
```

#### Paiements par Mois

```json
[
  {
    "mois": "2025-01",
    "montant": 95000.00
  },
  {
    "mois": "2025-02",
    "montant": 110000.00
  },
  // ... autres mois
]
```

#### Répartition des Propriétés par Type

```json
[
  {
    "type": "MAISON",
    "count": 1500
  },
  {
    "type": "APPARTEMENT",
    "count": 1200
  },
  // ... autres types
]
```

#### Répartition des Contribuables par Type

```json
[
  {
    "type": "PERSONNE_PHYSIQUE",
    "count": 950
  },
  {
    "type": "PERSONNE_MORALE",
    "count": 300
  },
  // ... autres types
]
```

### Carte Géographique

La carte utilise le format GeoJSON pour représenter les propriétés :

```json
{
  "type": "FeatureCollection",
  "features": [
    {
      "type": "Feature",
      "geometry": {
        "type": "Point",
        "coordinates": [15.3125, -4.3217]
      },
      "properties": {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "type": "MAISON",
        "adresse": "123 Avenue Principale",
        "superficie": 250.0,
        "montantImpot": 1500.0,
        "proprietaire": "Jean Dupont"
      }
    },
    // ... autres propriétés
  ]
}
```

### Listes Récentes

#### Derniers Utilisateurs

```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "nomComplet": "Marie Dubois",
    "login": "mdubois",
    "role": "TAXATEUR",
    "actif": true
  },
  // ... autres utilisateurs
]
```

#### Logs de Connexion

```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440002",
    "utilisateurId": "550e8400-e29b-41d4-a716-446655440001",
    "dateConnexion": "2025-10-09T15:30:45",
    "adresseIp": "192.168.1.100",
    "userAgent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) ...",
    "reussi": true
  },
  // ... autres logs
]
```

#### Taxations Récentes

```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440003",
    "montant": 2500.0,
    "dateTaxation": "2025-10-08T10:15:30",
    "contribuable": "Jean Dupont"
  },
  // ... autres taxations
]
```

#### Paiements Récents

```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440004",
    "montant": 2500.0,
    "datePaiement": "2025-10-09T09:30:15",
    "reference": "PAY-2025-10-09-001",
    "contribuable": "Jean Dupont"
  },
  // ... autres paiements
]
```

## Endpoints API

### Récupérer le Tableau de Bord Administrateur

```
GET /api/dashboard/admin
```

### Récupérer le Tableau de Bord Directeur

```
GET /api/dashboard/directeur
```

### Récupérer le Tableau de Bord Chef de Bureau

```
GET /api/dashboard/chef-bureau
```

### Récupérer le Tableau de Bord Taxateur

```
GET /api/dashboard/taxateur
```

### Récupérer le Tableau de Bord Contribuable

```
GET /api/dashboard/contribuable
```

## Sécurité

Chaque endpoint est sécurisé avec les rôles appropriés :

- `/api/dashboard/admin` : Accessible uniquement aux utilisateurs avec le rôle `ADMIN`
- `/api/dashboard/directeur` : Accessible uniquement aux utilisateurs avec le rôle `DIRECTEUR`
- `/api/dashboard/chef-bureau` : Accessible uniquement aux utilisateurs avec le rôle `CHEF_DE_BUREAU`
- `/api/dashboard/taxateur` : Accessible uniquement aux utilisateurs avec le rôle `TAXATEUR`
- `/api/dashboard/contribuable` : Accessible uniquement aux utilisateurs avec le rôle `CONTRIBUABLE`

## Intégration Frontend

Le frontend peut intégrer ces données dans différents composants visuels :

- **Statistiques générales** : Cartes ou widgets
- **Graphiques** : Graphiques à barres ou à lignes pour les données temporelles
- **Carte** : Carte interactive avec marqueurs pour les propriétés
- **Listes** : Tableaux paginés pour les données récentes

## Bonnes Pratiques

1. **Mise en cache** : Mettre en cache les données qui ne changent pas fréquemment
2. **Pagination** : Utiliser la pagination pour les listes longues
3. **Filtres** : Permettre aux utilisateurs de filtrer les données selon leurs besoins
4. **Exportation** : Offrir des options d'exportation (PDF, Excel) pour les rapports
5. **Actualisation** : Permettre l'actualisation manuelle ou automatique des données

## Fonctionnalités Avancées

### Tableaux de Bord Personnalisables

Les utilisateurs peuvent personnaliser leur tableau de bord selon leurs préférences :

- **Activation/désactivation de widgets** : Choisir quels widgets afficher
- **Réorganisation des widgets** : Modifier l'ordre d'affichage des widgets
- **Personnalisation des tailles** : Ajuster la taille des widgets
- **Thèmes visuels** : Choisir parmi différents thèmes (clair, sombre, contraste élevé)
- **Filtres personnalisés** : Définir des filtres par défaut pour les données

#### Configuration du Tableau de Bord

```json
{
  "utilisateur_id": "550e8400-e29b-41d4-a716-446655440001",
  "widgets_actifs": {
    "statistiques": true,
    "taxations_mois": true,
    "paiements_mois": false,
    "proprietes_type": true,
    "carte": true
  },
  "ordre_widgets": ["statistiques", "proprietes_type", "carte", "taxations_mois"],
  "theme": "dark",
  "filtres": {
    "periode": "6_mois",
    "region": "Centre"
  }
}
```

#### Endpoints API pour la Personnalisation

```
GET /api/dashboard/config
POST /api/dashboard/config
PUT /api/dashboard/config
DELETE /api/dashboard/config
```

### Alertes Basées sur des Seuils

Le système permet de configurer des alertes basées sur des seuils définis :

- **Types d'alertes** : Taxations, paiements, taux de recouvrement, etc.
- **Conditions** : Supérieur à, inférieur à, égal à, etc.
- **Niveaux d'alerte** : Information, avertissement, critique
- **Notifications** : Email, application, SMS
- **Fréquence de vérification** : Périodicité des vérifications

#### Configuration d'Alerte

```json
{
  "nom": "Alerte taux de recouvrement faible",
  "type": "TAUX_RECOUVREMENT",
  "metrique": "taux_recouvrement",
  "operateur": "INFERIEUR",
  "valeur_seuil": 70.0,
  "niveau": "WARNING",
  "notification_email": true,
  "notification_application": true,
  "frequence_verification": 60
}
```

#### Endpoints API pour les Alertes

```
GET /api/alertes
POST /api/alertes
PUT /api/alertes/{id}
DELETE /api/alertes/{id}
```

### Comparaisons de Données

Le système permet de comparer des données entre différentes périodes :

- **Types de comparaisons** : Taxations, paiements, contribuables, propriétés, etc.
- **Périodes** : Année précédente, trimestre précédent, mois précédent, etc.
- **Visualisations** : Graphiques côte à côte, tableaux comparatifs, variations en pourcentage
- **Exportation** : Rapports de comparaison au format PDF ou Excel

#### Configuration de Comparaison

```json
{
  "nom": "Comparaison taxations annuelles",
  "type": "TAXATIONS",
  "periode_reference": "ANNEE_PRECEDENTE",
  "periode_comparaison": "ANNEE_EN_COURS",
  "filtres": {
    "region": "Centre",
    "type_propriete": "MAISON"
  }
}
```

#### Endpoints API pour les Comparaisons

```
GET /api/comparaisons
POST /api/comparaisons
PUT /api/comparaisons/{id}
DELETE /api/comparaisons/{id}
GET /api/comparaisons/{id}/executer
```

### Analyses Prédictives

Le système intègre des modèles prédictifs pour anticiper les tendances :

- **Types d'analyses** : Prévision des taxations, prévision des paiements, détection d'anomalies, etc.
- **Périodes de prédiction** : 1 mois, 3 mois, 6 mois, 1 an, etc.
- **Visualisations** : Graphiques de tendances, intervalles de confiance
- **Précision** : Indicateurs de fiabilité des prédictions

#### Configuration d'Analyse Prédictive

```json
{
  "nom": "Prévision taxations 6 mois",
  "type": "PREVISION_TAXATIONS",
  "periode_prediction": "MOIS_6",
  "parametres": {
    "algorithme": "ARIMA",
    "donnees_historiques": "24_MOIS"
  }
}
```

#### Endpoints API pour les Analyses Prédictives

```
GET /api/analyses-predictives
POST /api/analyses-predictives
PUT /api/analyses-predictives/{id}
DELETE /api/analyses-predictives/{id}
GET /api/analyses-predictives/{id}/executer
```

### Rapports Programmés

Le système permet de programmer l'envoi automatique de rapports par email :

- **Types de rapports** : Tableau de bord, taxations, paiements, etc.
- **Fréquences** : Quotidien, hebdomadaire, mensuel, trimestriel, etc.
- **Formats** : PDF, Excel, CSV, HTML
- **Destinataires** : Liste d'emails
- **Personnalisation** : Titre, message, contenu du rapport

#### Configuration de Rapport Programmé

```json
{
  "nom": "Rapport mensuel taxations",
  "type": "taxations",
  "frequence": "MENSUEL",
  "jour_mois": 1,
  "heure": 8,
  "minute": 0,
  "format": "PDF",
  "destinataires": "directeur@example.com, finance@example.com",
  "objet_email": "Rapport mensuel des taxations",
  "corps_email": "Veuillez trouver ci-joint le rapport mensuel des taxations."
}
```

#### Endpoints API pour les Rapports Programmés

```
GET /api/rapports-programmes
POST /api/rapports-programmes
PUT /api/rapports-programmes/{id}
DELETE /api/rapports-programmes/{id}
GET /api/rapports-programmes/{id}/executer
```
