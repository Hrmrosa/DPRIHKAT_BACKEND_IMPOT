# Documentation API DPRIHKAT_BACKEND_IMPOT

## Vue d'ensemble

Cette documentation détaille les endpoints disponibles dans l'API DPRIHKAT_BACKEND_IMPOT, une application de gestion des impôts pour la Direction Provinciale des Recettes du Haut-Katanga (DPRIHKAT).

L'API permet de gérer les contribuables, les propriétés, les déclarations d'impôts, les taxations, les paiements et les utilisateurs du système.

## Prérequis techniques

- Java 17
- PostgreSQL 17 avec extension PostGIS
- Spring Boot 3.3.2
- Maven

## Structure de la documentation

La documentation est organisée par fonctionnalité, avec un fichier Markdown pour chaque module principal de l'application :

1. [Authentification et gestion des utilisateurs](authentification.md)
2. [Gestion des contribuables](contribuables.md)
3. [Gestion des propriétés](proprietes.md)
4. [Gestion des déclarations](declarations.md)
5. [Gestion des taxations](taxations.md)
6. [Gestion des paiements](paiements.md)
7. [Gestion des données géographiques (GeoData)](geodata.md)
8. [Documentation de collecte](collecte.md)
9. [Données de référence](referencedata.md)
10. [Utilisateurs](utilisateurs.md)
11. [Bureaux](bureaux.md)
12. [Divisions](divisions.md)
13. [Poursuites](poursuites.md)
14. [Relances](relances.md)
15. [Recouvrement](recouvrement.md)
16. [Contrôle Fiscal](controle-fiscal.md)
17. [Vignettes](vignettes.md)
18. [Plaques](plaques.md)
19. [Certificats](certificats.md)
20. [Concessions Minières](concessions-minieres.md)

## Authentification

L'API utilise l'authentification JWT (JSON Web Token). Pour accéder aux endpoints protégés, vous devez inclure un token JWT valide dans l'en-tête `Authorization` de vos requêtes :

```
Authorization: Bearer <votre_token_jwt>
```

Pour obtenir un token JWT, vous devez vous authentifier via l'endpoint `/api/auth/login`. Consultez la [documentation d'authentification](authentification.md) pour plus de détails.

## Rôles et permissions

L'API définit plusieurs rôles avec différentes permissions :

- **ADMIN** : Accès complet à toutes les fonctionnalités
- **DIRECTEUR** : Accès à la plupart des fonctionnalités, avec certaines restrictions administratives
- **CHEF_DE_BUREAU** : Accès aux fonctionnalités de gestion des déclarations, taxations et paiements
- **CHEF_DE_DIVISION** : Accès aux fonctionnalités de consultation et de reporting
- **TAXATEUR** : Accès aux fonctionnalités de gestion des déclarations et taxations
- **RECEVEUR_DES_IMPOTS** : Accès aux fonctionnalités de gestion des paiements
- **CONTRIBUABLE** : Accès limité à ses propres données et déclarations

Chaque endpoint spécifie les rôles autorisés à y accéder.

## Format des réponses

Toutes les réponses de l'API suivent un format standard :

### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    // Données spécifiques à l'endpoint
  }
}
```

### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "ERROR_CODE",
    "message": "Message d'erreur",
    "details": "Détails supplémentaires sur l'erreur"
  }
}
```

## Pagination

Plusieurs endpoints supportent la pagination pour gérer de grandes quantités de données. Les paramètres de pagination standard sont :

- `page` : Numéro de page (commence à 0)
- `size` : Nombre d'éléments par page
- `sortBy` : Champ de tri
- `sortDir` : Direction du tri (asc/desc)

## Géolocalisation

L'API utilise PostGIS pour stocker et manipuler les données géospatiales. Les propriétés peuvent être géolocalisées avec des coordonnées (latitude, longitude), ce qui est obligatoire pour certains types d'impôts.

## Options de déploiement

### Déploiement sur VPS

Pour un déploiement sur VPS, suivez les instructions détaillées dans le fichier [README_VPS_DEPLOY.md](/deploy/vps/README_VPS_DEPLOY.md). Ce guide inclut :

- Installation de Java 17
- Configuration de PostgreSQL 17 avec PostGIS
- Configuration du service systemd
- Gestion des variables d'environnement
- Exécution des migrations Flyway

### Déploiement avec Docker

L'application peut également être déployée à l'aide de Docker. Un Dockerfile est fourni à la racine du projet. Pour plus d'informations sur le déploiement Docker, consultez la documentation Docker officielle.

## Variables d'environnement

L'application nécessite les variables d'environnement suivantes :

- `SPRING_DATASOURCE_URL` : URL de connexion à la base de données PostgreSQL
- `SPRING_DATASOURCE_USERNAME` : Nom d'utilisateur pour la base de données
- `SPRING_DATASOURCE_PASSWORD` : Mot de passe pour la base de données
- `JWT_SECRET` : Clé secrète pour la génération des tokens JWT
- `JWT_EXPIRATION_MS` : Durée de validité des tokens JWT en millisecondes

## Endpoints des Taxations

### Récupérer toutes les taxations (paginées)

`GET /api/taxations`

**Paramètres :**
- `page` (optionnel, défaut: 0) - Numéro de page (commence à 0)
- `size` (optionnel, défaut: 10) - Nombre d'éléments par page
- `sortBy` (optionnel, défaut: "dateTaxation") - Champ de tri
- `sortDir` (optionnel, défaut: "desc") - Direction du tri ("asc" ou "desc")

**Réponse :**
```json
{
  "success": true,
  "data": {
    "taxations": [
      {
        "id": "...",
        "dateTaxation": "...",
        "montant": 0,
        "exercice": "...",
        "statut": "...",
        "typeImpot": "...",
        "exoneration": false,
        "motifExoneration": null,
        "dateEcheance": "...",
        "actif": true,
        "proprieteId": "...",
        "proprieteAdresse": "...",
        "proprieteImpotId": "...",
        "natureImpotCode": "...",
        "natureImpotNom": "...",
        "agentId": "...",
        "agentNom": "...",
        "paiementId": "..."
      }
    ],
    "currentPage": 0,
    "totalItems": 100,
    "totalPages": 10
  },
  "error": null,
  "meta": {
    "timestamp": "2025-09-08T15:52:37",
    "version": "1.0.0"
  }
}
```

### Récupérer les taxations actives (paginées)

`GET /api/taxations/actives`

Mêmes paramètres et structure de réponse que pour `/api/taxations`

## Contribuer à la documentation

Pour contribuer à cette documentation, veuillez soumettre vos modifications via des pull requests sur le dépôt GitHub du projet.
