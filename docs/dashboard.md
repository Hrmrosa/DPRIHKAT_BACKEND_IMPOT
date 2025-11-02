# Documentation du Tableau de Bord DPRIHKAT

## Table des matières

1. [Introduction](#introduction)
2. [Types de Tableaux de Bord](#types-de-tableaux-de-bord)
3. [Fonctionnalités par Rôle](#fonctionnalités-par-rôle)
4. [Architecture du Système](#architecture-du-système)
5. [Structure des Données](#structure-des-données)
6. [API REST](#api-rest)
7. [API WebSocket pour les Mises à Jour en Temps Réel](#api-websocket-pour-les-mises-à-jour-en-temps-réel)
8. [Intégration Frontend](#intégration-frontend)
9. [Sécurité](#sécurité)
10. [Bonnes Pratiques](#bonnes-pratiques)
11. [Fonctionnalités Avancées](#fonctionnalités-avancées)
12. [Exemples d'Utilisation](#exemples-dutilisation)
13. [Conclusion](#conclusion)
 
## Introduction

Le tableau de bord DPRIHKAT est une interface centralisée qui fournit une vue d'ensemble du système d'imposition. Il est conçu pour offrir des statistiques, des graphiques et des informations pertinentes adaptées aux différents rôles d'utilisateurs dans le système. Le dashboard utilise des technologies modernes pour fournir des mises à jour en temps réel, permettant aux utilisateurs de visualiser les changements sans avoir à rafraîchir leur page.

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
- **Mises à jour en temps réel** : Notifications instantanées des changements dans le système

### Directeur

Le directeur a accès à une vue similaire à celle de l'administrateur, mais avec moins de détails :

- **Statistiques générales** : Mêmes statistiques que l'administrateur
- **Graphiques** : Mêmes graphiques que l'administrateur
- **Carte géographique** : Même carte que l'administrateur
- **Listes récentes** : Taxations et paiements récents (nombre limité)
- **Mises à jour en temps réel** : Notifications instantanées des changements pertinents

### Chef de Bureau

*À implémenter selon les besoins spécifiques*

### Taxateur

*À implémenter selon les besoins spécifiques*

### Contribuable

*À implémenter selon les besoins spécifiques*

## Architecture du Système

Le système de tableau de bord DPRIHKAT utilise une architecture moderne qui combine plusieurs technologies pour offrir une expérience utilisateur optimale :

### Vue d'ensemble

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│  Frontend   │◄────┤  Backend    │◄────┤  Base de    │
│  (SPA)      │────►│  (API)      │────►│  Données    │
└─────────────┘     └─────────────┘     └─────────────┘
       ▲                   ▲
       │                   │
       └───────────────────┘
          WebSocket (temps réel)
```

### Composants Principaux

1. **Frontend** : Application web responsive utilisant des frameworks modernes (React, Angular, Vue.js)
2. **Backend** : API REST Spring Boot avec support WebSocket pour les mises à jour en temps réel
3. **Base de Données** : Stockage persistant des données du système

### Flux de Communication

Le système utilise deux approches complémentaires pour la communication entre le frontend et le backend :

1. **API REST** : Pour les requêtes initiales et les mises à jour manuelles
2. **WebSocket** : Pour les mises à jour automatiques en temps réel

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

## API REST

### Endpoints Principaux

#### Récupérer le Tableau de Bord Administrateur

```
GET /api/dashboard/admin
```

#### Récupérer le Tableau de Bord Directeur

```
GET /api/dashboard/directeur
```

#### Récupérer le Tableau de Bord Chef de Bureau

```
GET /api/dashboard/chef-bureau
```

#### Récupérer le Tableau de Bord Taxateur

```
GET /api/dashboard/taxateur
```

#### Récupérer le Tableau de Bord Contribuable

```
GET /api/dashboard/contribuable
```

### Endpoint pour les Données en Temps Réel

```
GET /api/dashboard/realtime-data
```

Cet endpoint renvoie les données les plus récentes du dashboard, adaptées au rôle de l'utilisateur connecté.

#### Exemple de Réponse

```json
{
  "success": true,
  "data": {
    "statistiques": {
      "total_contribuables": 1250,
      "total_proprietes": 3450,
      "total_vehicules": 2100,
      "total_utilisateurs": 75,
      "montant_total_taxations": 1250000.00,
      "montant_total_paiements": 980000.00,
      "taux_recouvrement": 78.40
    },
    "graphiques": {
      "taxations_par_mois": [...],
      "paiements_par_mois": [...],
      "proprietes_par_type": [...],
      "contribuables_par_type": [...]
    },
    "taxations_recentes": [...],
    "paiements_recents": [...]
  },
  "timestamp": 1697183401000
}
```

## API WebSocket pour les Mises à Jour en Temps Réel

### Point de Terminaison WebSocket

```
ws://[host]/ws
```

### Canaux de Communication

Le système utilise plusieurs canaux de communication pour les différents types de mises à jour :

- `/topic/dashboard` : Canal pour les mises à jour générales du dashboard
- `/topic/updates/contribuable` : Canal pour les mises à jour spécifiques aux contribuables
- `/topic/updates/taxation` : Canal pour les mises à jour spécifiques aux taxations
- `/topic/updates/paiement` : Canal pour les mises à jour spécifiques aux paiements
- `/topic/updates/propriete` : Canal pour les mises à jour spécifiques aux propriétés
- `/topic/updates/vehicule` : Canal pour les mises à jour spécifiques aux véhicules

### Format des Messages

#### Message de Mise à Jour Générale

```json
{
  "data": {
    "statistiques": { /* ... */ },
    "graphiques": { /* ... */ },
    "taxations_recentes": [ /* ... */ ],
    "paiements_recents": [ /* ... */ ]
  },
  "timestamp": 1697183401000
}
```

#### Message de Mise à Jour Spécifique

```json
{
  "type": "contribuable",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "nom": "Jean Dupont",
    // ... autres propriétés
  },
  "timestamp": 1697183401000
}
```

### Envoi de Messages au Serveur

Pour demander une mise à jour du dashboard, envoyez un message au point de terminaison `/app/dashboard/refresh` :

```json
{}
```

## Intégration Frontend

Le frontend peut intégrer les données du dashboard de plusieurs façons :

### 1. API REST

Utilisez les endpoints REST pour récupérer les données initiales et les rafraîchir périodiquement :

```javascript
// Exemple avec Axios
axios.get('/api/dashboard/realtime-data')
  .then(response => {
    if (response.data.success) {
      updateDashboard(response.data.data);
    }
  })
  .catch(error => console.error('Erreur lors de la récupération des données:', error));
```

### 2. WebSocket pour les Mises à Jour en Temps Réel

Utilisez les WebSockets pour recevoir des mises à jour en temps réel :

```javascript
// Connexion WebSocket avec SockJS et STOMP
const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
  console.log('Connecté au serveur WebSocket');
  
  // S'abonner au canal des mises à jour générales
  stompClient.subscribe('/topic/dashboard', function(response) {
    const data = JSON.parse(response.body);
    updateDashboard(data.data);
  });
  
  // S'abonner aux mises à jour spécifiques
  stompClient.subscribe('/topic/updates/contribuable', function(response) {
    const data = JSON.parse(response.body);
    updateContribuableSection(data);
  });
  
  // Demander une mise à jour initiale
  stompClient.send("/app/dashboard/refresh", {}, JSON.stringify({}));
});
```

### 3. Gestion des Déconnexions et Reconnexions

```javascript
function connectWebSocket() {
  const socket = new SockJS('/ws');
  stompClient = Stomp.over(socket);
  
  stompClient.connect({}, function(frame) {
    console.log('Connecté au serveur WebSocket');
    // Abonnements...
  }, function(error) {
    console.error('Erreur de connexion WebSocket:', error);
    // Tentative de reconnexion après 5 secondes
    setTimeout(connectWebSocket, 5000);
  });
}

// Détecter les déconnexions réseau
window.addEventListener('offline', function() {
  console.log('Connexion réseau perdue');
});

window.addEventListener('online', function() {
  console.log('Connexion réseau rétablie');
  if (!stompClient || !stompClient.connected) {
    connectWebSocket();
  }
});
```

### 4. Mise à Jour de l'Interface Utilisateur

```javascript
function updateDashboard(data) {
  // Mettre à jour les statistiques
  updateStatistiques(data.statistiques);
  
  // Mettre à jour les graphiques
  updateGraphiques(data.graphiques);
  
  // Mettre à jour les listes de données récentes
  updateRecentData(data);
}

function updateStatistiques(stats) {
  document.getElementById('total-contribuables').textContent = stats.total_contribuables;
  document.getElementById('total-proprietes').textContent = stats.total_proprietes;
  document.getElementById('total-vehicules').textContent = stats.total_vehicules;
  // ...
}

function updateGraphiques(graphiques) {
  // Mise à jour des graphiques avec Chart.js, D3.js, etc.
  // ...
}
```

### 5. Bibliothèques de Visualisation Recommandées

- **Graphiques** : Chart.js, D3.js, Highcharts
- **Cartes** : Leaflet, MapboxGL, OpenLayers
- **Tableaux** : AG Grid, React Table, Material-UI DataGrid
- **Widgets** : Material-UI, Ant Design, Bootstrap

## Sécurité

### Sécurité des API REST

Chaque endpoint est sécurisé avec les rôles appropriés :

- `/api/dashboard/admin` : Accessible uniquement aux utilisateurs avec le rôle `ADMIN`
- `/api/dashboard/directeur` : Accessible uniquement aux utilisateurs avec le rôle `DIRECTEUR`
- `/api/dashboard/chef-bureau` : Accessible uniquement aux utilisateurs avec le rôle `CHEF_DE_BUREAU`
- `/api/dashboard/taxateur` : Accessible uniquement aux utilisateurs avec le rôle `TAXATEUR`
- `/api/dashboard/contribuable` : Accessible uniquement aux utilisateurs avec le rôle `CONTRIBUABLE`
- `/api/dashboard/realtime-data` : Accessible à tous les utilisateurs authentifiés

### Sécurité WebSocket

- Le point de terminaison `/ws` est accessible sans authentification pour faciliter la connexion initiale
- Les messages sont envoyés uniquement aux clients abonnés aux canaux correspondants
- Les données sensibles sont filtrées avant d'être envoyées aux clients
- L'authentification est vérifiée lors de l'abonnement aux canaux

## Bonnes Pratiques

### 1. Optimisation des Performances

- Limiter la taille des messages WebSocket en envoyant uniquement les données modifiées
- Utiliser la compression des messages pour réduire la bande passante
- Mettre en cache les données côté client pour éviter les transferts inutiles
- Mettre en cache les données qui ne changent pas fréquemment côté serveur

### 2. Gestion des Erreurs

- Implémenter une gestion robuste des erreurs de connexion WebSocket
- Mettre en place une stratégie de reconnexion automatique avec backoff exponentiel
- Prévoir un mode dégradé en cas d'indisponibilité des WebSockets (fallback vers polling)
- Journaliser les erreurs côté serveur pour faciliter le débogage

### 3. Pagination et Filtres

- Utiliser la pagination pour les listes longues
- Permettre aux utilisateurs de filtrer les données selon leurs besoins
- Implémenter des filtres côté serveur pour optimiser les requêtes

### 4. Exportation et Rapports

- Offrir des options d'exportation (PDF, Excel) pour les rapports
- Permettre la programmation de rapports automatiques

### 5. Actualisation et Mises à Jour

- Permettre l'actualisation manuelle ou automatique des données
- Indiquer visuellement quand les données ont été mises à jour pour la dernière fois
- Afficher des indicateurs de chargement pendant les mises à jour

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

## Exemples d'Utilisation

### Exemple 1 : Dashboard Administrateur

```javascript
// Connexion WebSocket
connectWebSocket();

// Fonction de mise à jour du dashboard administrateur
function updateAdminDashboard(data) {
  // Mise à jour des statistiques générales
  updateStatistiques(data.statistiques);
  
  // Mise à jour des graphiques
  updateGraphiques(data.graphiques);
  
  // Mise à jour de la carte
  updateCarte(data.carte);
  
  // Mise à jour des listes récentes
  updateUtilisateurs(data.derniers_utilisateurs);
  updateLogsConnexion(data.logs_connexion);
  updateTaxations(data.taxations_recentes);
  updatePaiements(data.paiements_recents);
}
```

### Exemple 2 : Dashboard Contribuable

```javascript
// Connexion WebSocket
connectWebSocket();

// Fonction de mise à jour du dashboard contribuable
function updateContribuableDashboard(data) {
  // Mise à jour des propriétés du contribuable
  updateProprietes(data.proprietes);
  
  // Mise à jour des véhicules du contribuable
  updateVehicules(data.vehicules);
  
  // Mise à jour des déclarations et paiements
  updateDeclarations(data.declarations);
  updatePaiements(data.paiements);
}
```

## Conclusion

Le tableau de bord DPRIHKAT offre une interface moderne et complète pour visualiser et analyser les données du système d'imposition. Grâce à son architecture combinant API REST et WebSockets, il permet une expérience utilisateur fluide avec des mises à jour en temps réel. Les différentes vues adaptées aux rôles des utilisateurs garantissent que chacun a accès aux informations pertinentes pour ses responsabilités.

Les fonctionnalités avancées comme les tableaux de bord personnalisables, les alertes basées sur des seuils, les comparaisons de données, les analyses prédictives et les rapports programmés offrent des outils puissants pour la prise de décision et le suivi des activités.

Cette documentation fournit toutes les informations nécessaires pour comprendre, utiliser et intégrer le tableau de bord DPRIHKAT dans votre environnement de travail.
