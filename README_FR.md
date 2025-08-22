# API de Taxes de Haut-Katanga

Il s'agit d'une application Spring Boot qui fournit une API REST pour gérer diverses taxes provinciales à Haut-Katanga, République Démocratique du Congo. L'API prend en charge la télé-déclaration, la taxation par les agents, la géolocalisation (PostGIS), la gestion des pénalités, les paiements, l'émission d'articles imprimés, le contrôle fiscal et la gestion des utilisateurs/rôles conformément aux lois et réglementations fiscales locales.

## Fonctionnalités

- **Gestion des Taxes** :
  - Impôt Foncier (IF)
  - Impôt sur les Revenus Locatifs (IRL)
  - Retenue sur les Loyers (RL)
  - Impôt Réel sur les Véhicules (IRV)
  - Impôt sur les Concessions Minières (ICM)

- **Télé-déclaration** : Les contributeurs peuvent déclarer leurs taxes en ligne
- **Taxation par les agents** : Les agents fiscaux peuvent créer des évaluations fiscales
- **Géolocalisation** : Intégration PostGIS pour le suivi de l'emplacement des propriétés
- **Gestion des pénalités** : Calcul et application automatisés des pénalités
- **Procédures de recouvrement** : Processus de recouvrement de la dette en plusieurs étapes
- **Traitement des paiements** : Suivi et rapprochement des paiements
- **Articles imprimés** : Intégration avec l'API de stock externe pour les vignettes, certificats et plaques
- **Contrôle fiscal** : Fonctionnalités de vérification et d'audit
- **Gestion des utilisateurs et des rôles** : Système RBAC avec rôles prédéfinis

## Technologies

- Java 17
- Spring Boot 3.1.0
- Spring Security avec JWT
- Spring Data JPA
- Hibernate 6.2.2
- PostgreSQL avec PostGIS
- Maven 3.9.4
- JUnit 5 pour les tests

## Configuration requise

- Java 17 JDK
- Maven 3.9.4
- PostgreSQL 12+ avec extension PostGIS
- Accès à l'API de stock externe (simulée)

## Installation

1. Clonez le dépôt
2. Installez PostgreSQL et PostGIS
3. Créez une base de données nommée `dpri_impots`
4. Configurez les identifiants de la base de données dans `src/main/resources/application.properties`
5. Exécutez `mvn clean install` pour construire le projet
6. Exécutez `mvn spring-boot:run` pour démarrer l'application

## Structure du projet

```
src/
├── main/
│   ├── java/
│   │   └── com/dpri/impots/
│   │       ├── common/          # Classes utilitaires et base
│   │       ├── config/          # Configuration Spring
│   │       ├── controller/      # Contrôleurs REST
│   │       ├── dto/             # Objets de transfert de données
│   │       ├── entity/          # Entités JPA
│   │       ├── exception/       # Gestion des exceptions
│   │       ├── repository/      # Interfaces de dépôt Spring Data
│   │       ├── security/        # Configuration de sécurité
│   │       ├── service/         # Services métier
│   │       └── DpriImpotsApplication.java # Classe principale
│   └── resources/
│       ├── application.properties # Configuration de l'application
│       └── data.sql              # Données initiales (le cas échéant)
└── test/                        # Tests unitaires et d'intégration
```

## Entités principales

- **Contributeur** : Personne ou entité assujettie à l'impôt
- **Utilisateur** : Compte d'utilisateur avec rôles RBAC
- **Bien** : Classe de base pour les propriétés et véhicules
- **Propriété** : Propriété immobilière avec géolocalisation
- **Véhicule** : Véhicule avec numéro de châssis
- **Taxe** : Classe de base pour tous les types de taxes
- **IF** : Impôt Foncier
- **IRL** : Impôt sur les Revenus Locatifs
- **RL** : Retenue sur les Loyers
- **IRV** : Impôt Réel sur les Véhicules
- **ICM** : Impôt sur les Concessions Minières
- **Pénalité** : Pénalités appliquées aux déclarations en retard
- **Procédure de recouvrement** : Processus de recouvrement de la dette
- **Paiement** : Enregistrement des paiements
- **Article imprimé** : Vignettes, certificats et plaques
- **Tableau de bord** : Configurations de tableau de bord personnalisées
- **Rapport** : Génération de rapports
- **Journal d'audit** : Suivi des actions de l'utilisateur

## Rôles et autorisations

- **ROLE_CONTRIBUTOR** : Accès limité aux fonctionnalités de déclaration
- **ROLE_AGENT** : Accès aux fonctionnalités de taxation et de vérification
- **ROLE_SUPERVISOR** : Accès étendu aux fonctionnalités de supervision
- **ROLE_ADMIN** : Accès complet à toutes les fonctionnalités
- **ROLE_SUPER_ADMIN** : Accès à la gestion des utilisateurs et des rôles

## Journal d'audit

L'application enregistre toutes les actions importantes dans un journal d'audit comprenant :

- ID utilisateur et rôle
- Action effectuée
- Type et ID d'entité
- Horodatage
- Informations détaillées sur l'action

## Contribution

Les contributions sont les bienvenues ! Veuillez créer une branche pour chaque fonctionnalité ou correction de bogue, puis soumettre une demande de fusion.

## Endpoints clés ajoutés (accès direct et filtrage par propriétaire)
- Propriétés (staff):
  - `GET /api/proprietes`
  - `GET /api/proprietes/{id}`
  - Sérialisation: `proprietaire` renvoyé comme ID, `declarations` masquée.
- Concessions minières (staff):
  - `GET /api/concessions`
  - `GET /api/concessions/{id}`
  - Sérialisation: `titulaire` renvoyé comme ID, `declarations` masquée.
- Mes biens (contribuable connecté):
  - `GET /api/proprietes/mine`
  - `GET /api/concessions/mine`

## Sérialisation JSON et prévention des cycles
- `Agent`: n’expose pas `declarations`, `poursuites`, `apurements`, `controlesInitiates`, `controlesValidates`.
- `Propriete`: `proprietaire` en ID, `declarations` masquée.
- `ConcessionMinier`: `titulaire` en ID, `declarations` masquée.
- Certaines relations `Division`/`Bureau` sont sérialisées uniquement par IDs pour réduire la taille.

## Comptes de test (seeder)
- admin / Tabc@123 — ADMIN (connexion directe)
- taxateur1 / Tabc@123 — TAXATEUR (connexion directe)
- receveur1 / Tabc@123 — RECEVEUR_DES_IMPOTS (connexion directe)
- contrib1 / Tabc@123 — CONTRIBUABLE (première connexion requise)
- contrib2 / Tabc@123 — CONTRIBUABLE (connexion directe) — à utiliser pour tester `/api/proprietes/mine` et `/api/concessions/mine`

## Exemples cURL
```bash
# Propriétés (staff)
curl -H "Authorization: Bearer STAFF_JWT" http://localhost:8080/api/proprietes
curl -H "Authorization: Bearer STAFF_JWT" http://localhost:8080/api/proprietes/{id}

# Concessions (staff)
curl -H "Authorization: Bearer STAFF_JWT" http://localhost:8080/api/concessions
curl -H "Authorization: Bearer STAFF_JWT" http://localhost:8080/api/concessions/{id}

# Mes biens (contribuable)
curl -H "Authorization: Bearer CONTRIB_JWT" http://localhost:8080/api/proprietes/mine
curl -H "Authorization: Bearer CONTRIB_JWT" http://localhost:8080/api/concessions/mine
```

## Référentiels JSON (communes et véhicules)

Endpoints de lecture chargés depuis `src/main/resources/communes.json` et `src/main/resources/voiture.json` (recherche insensible à la casse, sans base de données):

- Communes
  - `GET /api/ref/communes` — liste des communes
  - `GET /api/ref/communes/{commune}/quartiers` — quartiers d’une commune
  - `GET /api/ref/communes/{commune}/quartiers/{quartier}/avenues` — avenues d’un quartier
- Véhicules
  - `GET /api/ref/voitures/marques` — liste des marques
  - `GET /api/ref/voitures/marques/{marque}/models` — modèles pour une marque

Exemples cURL:
```bash
curl http://localhost:8080/api/ref/communes
curl http://localhost:8080/api/ref/communes/kamalondo/quartiers
curl http://localhost:8080/api/ref/communes/kamalondo/quartiers/kitumaini/avenues

curl http://localhost:8080/api/ref/voitures/marques
curl http://localhost:8080/api/ref/voitures/marques/BMW/models
```

Réponse type:
```json
{
  "success": true,
  "data": { "quartiers": ["Kitumaini", "Njanja"] },
  "error": null,
  "meta": { "timestamp": "...", "version": "1.0.0" }
}
```

## Licence

Ce projet est sous licence selon les termes définis par le ministère des Finances de la République Démocratique du Congo.
