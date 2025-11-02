# Index de la Documentation API DPRIHKAT

Bienvenue dans la documentation complète de l'API DPRIHKAT (Direction Provinciale des Recettes et Impôts du Haut-Katanga).

---

## 📚 Documentation par Fonctionnalité

### 🔐 Authentification et Utilisateurs
- **[Authentification](authentification.md)** - Connexion, JWT, gestion des sessions
- **[Utilisateurs](api_utilisateurs.md)** - Gestion des comptes utilisateurs et agents

### 👥 Gestion des Contribuables
- **[Contribuables](api_contribuables.md)** - CRUD contribuables, recherche, détection doublons
- **[Détails Contribuables](contribuables.md)** - Documentation détaillée complète

### 🏠 Gestion des Biens Immobiliers
- **[Propriétés](api_proprietes.md)** - Gestion des biens immobiliers et impôt foncier (IF)
- **[Propriétés Détaillées](proprietes.md)** - Documentation complète des propriétés

### 🚗 Gestion des Véhicules
- **[Véhicules](api_vehicules.md)** - Gestion des véhicules et IRV
- **[Véhicules Détaillés](vehicules.md)** - Documentation complète des véhicules
- **[Plaques d'Immatriculation](api_plaques.md)** - Gestion des plaques
- **[Workflow Plaques](workflow_plaques.md)** - 🔥 Processus complet d'attribution des plaques
- **[Demandes de Plaques](gestion_plaques.md)** - Processus de demande de plaques
- **[Vignettes](api_vignettes.md)** - Gestion des vignettes automobiles
- **[Vignettes Détaillées](vignettes.md)** - Documentation complète des vignettes

### 📋 Déclarations et Taxation
- **[Déclarations](api_declarations.md)** - Déclarations fiscales en ligne et hors ligne
- **[Déclarations Détaillées](declarations.md)** - Documentation complète
- **[Télé-déclaration](tele_declaration.md)** - Processus de déclaration en ligne
- **[Taxations](api_taxations.md)** - Émission et gestion des taxations
- **[Taxations Détaillées](taxations.md)** - Documentation complète
- **[Taxation Véhicules](gestion_taxation_vehicules.md)** - Taxation spécifique véhicules

### 💰 Paiements et Recouvrement
- **[Paiements](api_paiements.md)** - Enregistrement et validation des paiements
- **[Paiements Détaillés](paiements.md)** - Documentation complète
- **[Recouvrement](api_recouvrement.md)** - Gestion des dossiers de recouvrement
- **[Recouvrement Détaillé](recouvrement.md)** - Processus de recouvrement complet
- **[Documents de Recouvrement](api_documents_recouvrement.md)** - Gestion des actes de recouvrement (AMR, MED, etc.)
- **[Apurements](api_apurements.md)** - Gestion des apurements
- **[Apurements Détaillés](apurements.md)** - Documentation complète
- **[Relances](api_relances.md)** - Système de relances automatiques
- **[Relances Détaillées](relances.md)** - Documentation complète
- **[Poursuites](poursuites.md)** - Gestion des poursuites

### 📊 Dashboard et Statistiques
- **[Dashboard](api_dashboard.md)** - Statistiques et indicateurs clés
- **[Dashboard Unifié](dashboard-unified.md)** - Dashboard consolidé
- **[Dashboard Temps Réel](dashboard-realtime.md)** - Statistiques en temps réel
- **[Statistiques Publiques](dashboard_statistiques_publiques.md)** - API publique sans authentification
- **[Tableaux de Bord](tableaux_de_bord.md)** - Vues et rapports

### 🏢 Structure Administrative
- **[Bureaux](api_bureaux.md)** - Gestion des bureaux
- **[Bureaux Détaillés](bureaux.md)** - Documentation complète
- **[Divisions](api_divisions.md)** - Gestion des divisions
- **[Divisions Détaillées](divisions.md)** - Documentation complète
- **[Agents](README.md)** - Gestion des agents administratifs

### ⚙️ Configuration et Référence
- **[Natures d'Impôt](natures-impot.md)** - Types d'impôts (IF, IRV, ICM)
- **[Taux de Change](api_taux_change.md)** - Gestion des taux de change
- **[Taux de Change Détaillés](gestion_taux_change.md)** - Documentation complète
- **[Données de Référence](referencedata.md)** - Données de base du système
- **[ICM](api_icm.md)** - Impôt sur les Concessions Minières
- **[Concessions Minières](concessions-minieres.md)** - Gestion des concessions
- **[API Concessions Minières](api_concessions_minieres.md)** - Documentation technique complète

### 🔍 Contrôle et Audit
- **[Contrôle Fiscal](api_controle_fiscal.md)** - Détection d'anomalies et rapports
- **[Contrôle Fiscal Détaillé](controle-fiscal.md)** - Processus de contrôle complet
- **[Audit et Logs](api_audit.md)** - Traçabilité et logs système
- **[Audit Détaillé](audit_logging.md)** - Documentation complète
- **[Collecte](api_collecte.md)** - Collecte et enrichissement des données
- **[Collecte Détaillée](collecte.md)** - Documentation complète

### 📜 Certificats
- **[Certificats](api_certificats.md)** - Émission et vérification des certificats
- **[Certificats Détaillés](certificats.md)** - Documentation complète

---

## 🚀 Démarrage Rapide

### Base URL de l'API
```
http://localhost:8080/api
```

### Authentification
Tous les endpoints (sauf les endpoints publics) nécessitent un token JWT dans le header:
```
Authorization: Bearer YOUR_JWT_TOKEN
```

### Format des Réponses
Toutes les réponses suivent le format standardisé:

**Succès:**
```json
{
  "success": true,
  "data": {
    // Données de la réponse
  },
  "timestamp": 1730028220000
}
```

**Erreur:**
```json
{
  "success": false,
  "error": {
    "code": "ERROR_CODE",
    "message": "Message d'erreur",
    "details": "Détails supplémentaires"
  }
}
```

---

## 📖 Guides Techniques

### Corrections et Optimisations
- **[Corrections JSON Deserialization](corrections_json_deserialization.md)** - Résolution des erreurs de désérialisation
- **[Optimisation Taxation](optimisation_taxation.md)** - Optimisations des calculs de taxation
- **[Gestion des Schémas](schema_management.md)** - Gestion des schémas de base de données

---

## 🔑 Rôles et Permissions

### Rôles Disponibles
- **ADMIN**: Administrateur système (accès complet)
- **DIRECTEUR**: Directeur provincial
- **CHEF_DE_DIVISION**: Chef de division
- **CHEF_DE_BUREAU**: Chef de bureau
- **TAXATEUR**: Agent taxateur
- **RECEVEUR_DES_IMPOTS**: Receveur des impôts
- **CONTROLLEUR**: Contrôleur fiscal
- **INFORMATICIEN**: Informaticien système
- **CONTRIBUABLE**: Contribuable (accès limité)

### Matrice des Permissions
Consultez chaque documentation de module pour les permissions spécifiques.

---

## 📝 Conventions

### Formats de Données
- **Dates**: ISO 8601 (YYYY-MM-DDTHH:mm:ss)
- **Montants**: Nombres décimaux (ex: 75000.00)
- **UUIDs**: Format standard UUID v4
- **Téléphones**: Format international (+243...)

### Codes HTTP
- **200**: Succès
- **400**: Erreur de requête
- **401**: Non authentifié
- **403**: Non autorisé
- **404**: Ressource non trouvée
- **500**: Erreur serveur

---

## 🛠️ Environnement de Développement

### Prérequis
- Java 17+
- Spring Boot 3.x
- PostgreSQL 14+
- PostGIS (pour géolocalisation)

### Configuration
Voir `application.properties` pour la configuration complète.

---

## 📞 Support

Pour toute question ou problème:
- Consultez la documentation spécifique du module
- Vérifiez les logs système
- Contactez l'équipe technique DPRIHKAT

---

## 📅 Dernière Mise à Jour

**Date**: 27 Octobre 2025  
**Version**: 1.0.0

---

## 🔗 Liens Rapides

### Modules Principaux
- [Contribuables](api_contribuables.md) | [Propriétés](api_proprietes.md) | [Véhicules](api_vehicules.md)
- [Déclarations](api_declarations.md) | [Taxations](api_taxations.md) | [Paiements](api_paiements.md)
- [Dashboard](api_dashboard.md) | [Utilisateurs](api_utilisateurs.md)

### Processus Métier
- [Télé-déclaration](tele_declaration.md) | [Recouvrement](recouvrement.md) | [Contrôle Fiscal](controle-fiscal.md)

### Administration
- [Bureaux](bureaux.md) | [Divisions](divisions.md) | [Natures d'Impôt](natures-impot.md)
