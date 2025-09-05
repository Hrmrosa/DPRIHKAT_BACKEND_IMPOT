# API de Gestion des Impôts du Haut-Katanga (DPRIHKAT)

Cette application Spring Boot fournit une API REST pour la gestion des différents impôts provinciaux dans le Haut-Katanga, République Démocratique du Congo. L'API prend en charge la télé-déclaration, la taxation par les agents, la géolocalisation (PostGIS), la gestion des pénalités, les paiements, l'émission de documents imprimés, le contrôle fiscal et la gestion des utilisateurs/rôles conformément aux lois et réglementations fiscales locales.

## Fonctionnalités

- **Gestion des impôts** :
  - Impôt Foncier (IF)
  - Impôt sur les Revenus Locatifs (IRL)
  - Retenue sur les Loyers (RL)
  - Impôt Réel sur les Véhicules (IRV)
  - Impôt sur les Concessions Minières (ICM)

- **Distinction entre déclaration et taxation** :
  - **Déclaration** : Ajout d'un bien à un contribuable (par un agent ou par le contribuable lui-même)
  - **Taxation** : Application d'un impôt à une propriété pour un exercice fiscal donné

- **Télé-déclaration** : Les contribuables peuvent déclarer leurs impôts en ligne entre le 2 janvier et le 1er février
- **Taxation par les agents** : Les agents fiscaux peuvent créer des avis d'imposition
- **Géolocalisation** : Intégration de PostGIS pour le suivi de l'emplacement des propriétés (obligatoire pour IF, IRL, RL, ICM, IRV)
- **Gestion des pénalités** : Calcul automatique des pénalités (2% par mois pour les paiements tardifs, 25% pour la non-déclaration)
- **Procédures de recouvrement** : Processus de recouvrement de créances en plusieurs étapes
- **Traitement des paiements** : Suivi des paiements et rapprochement avec les bordereaux bancaires
- **Articles imprimés** : Intégration avec l'API de stock externe pour les vignettes, certificats et plaques
- **Contrôle fiscal** : Mécanismes de surveillance et de contrôle
- **Gestion des utilisateurs/rôles** : Contrôle d'accès basé sur les rôles avec hiérarchie complète des rôles
- **Journalisation des audits** : Historique complet de toutes les actions dans le système
- **Rapports basés sur les rôles** : Rapports personnalisés pour chaque rôle d'utilisateur
- **Tableaux de bord basés sur les rôles** : Tableaux de bord personnalisés pour chaque rôle d'utilisateur
- **Authentification JWT** : Authentification sécurisée avec prise en charge des tokens de rafraîchissement
- **Gestion automatique des utilisateurs contribuables** : Création automatique de comptes utilisateurs pour les contribuables avec génération de nom d'utilisateur et mot de passe
- **Envoi d'emails** : Envoi automatique d'emails aux contribuables avec leurs identifiants

## Rôles des utilisateurs

Le système implémente un contrôle d'accès complet basé sur les rôles avec les rôles suivants :

- **Taxateur** : Gère les contribuables, les propriétés et la taxation
- **Chef de bureau** : Voit toutes les informations sur les contribuables et la taxation dans son bureau
- **Chef de division** : Voit tous les mouvements dans sa division
- **Directeur** : Voit tous les mouvements dans l'organisation
- **Chef de cellules** : Voit la taxation et les contribuables dans son bureau assigné
- **Vérificateur** : Voit les contribuables et la taxation qui ont été traités
- **Controlleur** : Voit les contribuables et les notes problématiques (mal déclarées, déclarations manquantes, notes en retard...)
- **Apureur** : Voit la taxation et valide les paiements dans le système
- **Receveur des impôts** : Voit les notes en retard, les paiements et les dossiers en procédure de recouvrement
- **Admin** : Accès administratif complet
- **Informaticien** : Gère les utilisateurs et la configuration du système, peut participer à la collecte de données sur le terrain
- **Contribuable** : Soumet des déclarations d'impôts en ligne

## Stack technique

- **Backend** : Spring Boot 3.3.2 avec Java 17
- **Base de données** : PostgreSQL avec extension PostGIS
- **ORM** : Hibernate Spatial pour les données de géolocalisation
- **Sécurité** : Spring Security avec authentification JWT
- **Documentation API** : Springdoc OpenAPI
- **Outil de build** : Maven
- **Email** : Spring Mail pour l'envoi d'emails

## Structure du projet

```
src/main/java/com/DPRIHKAT/
├── config/           # Classes de configuration
├── controller/       # Contrôleurs REST
├── dto/              # Objets de transfert de données
├── entity/           # Entités JPA
│   └── enums/        # Énumérations
├── repository/       # Repositories Spring Data JPA
├── service/          # Services métier
└── util/             # Classes utilitaires
```

## Endpoints API

### Endpoints d'authentification

- `POST /api/auth/login` - Authentifier un utilisateur avec login et mot de passe
- `POST /api/auth/changer-mot-de-passe` - Changer le mot de passe lors de la première connexion

### Endpoints de déclaration

- `POST /api/declarations/soumettre` - Soumettre une déclaration d'impôt en ligne (Contribuable uniquement)
- `POST /api/declarations/manuelle` - Enregistrer une déclaration manuelle (Taxateur, Receveur des impôts)
- `GET /api/declarations` - Lister toutes les déclarations avec pagination

### Endpoints de taxation

- `POST /api/taxations` - Générer une taxation
- `GET /api/taxations` - Lister toutes les taxations
- `GET /api/taxations/actives` - Lister toutes les taxations actives
- `GET /api/taxations/{id}` - Récupérer une taxation par son ID
- `GET /api/taxations/by-propriete/{proprieteId}` - Récupérer les taxations pour une propriété
- `GET /api/taxations/by-exercice/{exercice}` - Récupérer les taxations pour un exercice
- `GET /api/taxations/by-type-impot/{typeImpot}` - Récupérer les taxations pour un type d'impôt
- `GET /api/taxations/by-statut/{statut}` - Récupérer les taxations par statut
- `GET /api/taxations/calculate/property/{proprieteId}` - Calculer le montant de la taxation pour une propriété
- `PUT /api/taxations/{id}/statut/{statut}` - Mettre à jour le statut d'une taxation
- `PUT /api/taxations/{id}/exoneration` - Accorder une exonération
- `DELETE /api/taxations/{id}` - Désactiver une taxation
- `PUT /api/taxations/{id}/activate` - Activer une taxation

### Endpoints de gestion des plaques

- `POST /api/plaques` - Créer une nouvelle plaque
- `POST /api/plaques/assigner` - Assigner une plaque à une propriété
- `GET /api/plaques` - Lister toutes les plaques avec pagination
- `GET /api/plaques/disponibles` - Lister les plaques disponibles avec pagination

### Endpoints de gestion des pénalités

- `POST /api/penalites/calculer` - Calculer automatiquement les pénalités pour une déclaration tardive
- `PUT /api/penalites/ajuster` - Ajuster manuellement les pénalités (Chef de bureau, Chef de division, Directeur)

### Endpoints de gestion des utilisateurs

- `POST /api/users` - Créer un nouvel utilisateur
- `POST /api/users/bloquer` - Bloquer un compte utilisateur
- `GET /api/users` - Lister tous les utilisateurs avec pagination

### Endpoints de gestion des contribuables

- `POST /api/contribuables` - Créer un nouveau contribuable avec un compte utilisateur
- `GET /api/contribuables` - Lister tous les contribuables
- `GET /api/contribuables/{id}` - Récupérer un contribuable par son ID

### Endpoints de natures d'impôt et liens propriété-impôt

- `GET /api/natures-impot` - Lister toutes les natures d'impôt
- `GET /api/natures-impot/actives` - Lister toutes les natures d'impôt actives
- `GET /api/natures-impot/{id}` - Récupérer une nature d'impôt par son ID
- `POST /api/natures-impot` - Créer une nouvelle nature d'impôt
- `PUT /api/natures-impot/{id}` - Mettre à jour une nature d'impôt
- `DELETE /api/natures-impot/{id}` - Supprimer une nature d'impôt
- `GET /api/proprietes-impots` - Lister tous les liens propriété-impôt
- `GET /api/proprietes-impots/{id}` - Récupérer un lien propriété-impôt par son ID
- `POST /api/proprietes-impots` - Créer un nouveau lien propriété-impôt
- `PUT /api/proprietes-impots/{id}` - Mettre à jour un lien propriété-impôt
- `DELETE /api/proprietes-impots/{id}` - Supprimer un lien propriété-impôt

## Sécurité

L'application implémente un contrôle d'accès basé sur les rôles :

- **Contribuable** peut uniquement soumettre des déclarations en ligne
- **Taxateur** et **Receveur des impôts** peuvent enregistrer des déclarations manuelles
- **Chef de bureau**, **Chef de division** et **Directeur** peuvent ajuster les pénalités
- **Admin** et **Directeur** peuvent bloquer des comptes utilisateur

## Règles fiscales

### Impôt Foncier (IF)

Les taux sont chargés depuis `taux_if.json` :
- Villas : 0,50-2,00 USD/m²
- Appartements : 30-150 USD
- Commercial : 5-25 USD
- Domestique : 5-20 USD (selon le rang et le type de contribuable)

### Impôt sur les Concessions Minières (ICM)

Les taux sont chargés depuis `taux_icm.json` :
- Recherche : 0,03-0,12 USD/ha
- Exploitation : 0,03-0,12 USD/ha

### Impôt sur les Revenus Locatifs (IRL)

- Taux total : 22% (20% d'avance, 2% de solde dû avant le 1er février de l'année suivante)

### Retenue sur les Loyers (RL)

- Mêmes taux que l'IRL

### Impôt Réel sur les Véhicules (IRV)

- Basé sur la cylindrée/tonnage (ex : 54 USD + 19 USD TSCR pour <2,5T pour les particuliers)

## Règles de pénalité

- **Paiements tardifs** : 2% par mois
- **Non-déclaration** : 25% de pénalité

## Configuration de la base de données

L'application utilise PostgreSQL avec l'extension PostGIS pour les données de géolocalisation. La configuration se trouve dans `application.properties`.

## Fichiers de taux d'imposition JSON

Les taux d'imposition sont chargés à partir de fichiers JSON dans `src/main/resources` :
- `taux_if.json` - Taux d'imposition IF
- `taux_icm.json` - Taux d'imposition ICM

## Endpoints JSON de référence

Endpoints en lecture seule chargés à partir de `src/main/resources/communes.json` et `src/main/resources/voiture.json` (recherches insensibles à la casse ; pas de base de données) :

- Communes
  - `GET /api/ref/communes` — liste des communes
  - `GET /api/ref/communes/{commune}/quartiers` — liste des quartiers d'une commune
  - `GET /api/ref/communes/{commune}/quartiers/{quartier}/avenues` — liste des avenues d'un quartier
- Véhicules
  - `GET /api/ref/voitures/marques` — liste des marques
  - `GET /api/ref/voitures/marques/{marque}/models` — liste des modèles pour une marque

Exemples :
```bash
curl http://localhost:8080/api/ref/communes
curl http://localhost:8080/api/ref/communes/kamalondo/quartiers
curl http://localhost:8080/api/ref/communes/kamalondo/quartiers/kitumaini/avenues

curl http://localhost:8080/api/ref/voitures/marques
curl http://localhost:8080/api/ref/voitures/marques/BMW/models
```

## Format de réponse

Toutes les réponses API suivent un format standardisé :

```json
{
  "success": true,
  "data": {...},
  "error": null,
  "meta": {
    "timestamp": "2025-08-20T07:52:37.123456",
    "version": "1.0.0"
  }
}
```

Réponses d'erreur :

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "ERROR_CODE",
    "message": "Message d'erreur",
    "details": "Informations détaillées sur l'erreur"
  },
  "meta": {
    "timestamp": "2025-08-20T07:52:37.123456",
    "version": "1.0.0"
  }
}
```

## Pagination

Tous les endpoints de liste prennent en charge la pagination à l'aide des paramètres standard Spring Data JPA :
- `page` - Numéro de page (à partir de 0)
- `size` - Taille de la page
- `sort` - Champ et direction de tri (ex : `date,desc`)

Exemple : `GET /api/declarations?page=0&size=10&sort=date,desc`

## Exemple d'utilisation

### Authentifier un utilisateur

```bash
curl -X POST "http://localhost:8080/api/auth/login" \
  -d "login=john_doe" \
  -d "motDePasse=mot_de_passe_securise"
```

### Soumettre une déclaration d'impôt

```bash
curl -X POST "http://localhost:8080/api/declarations/soumettre" \
  -H "Authorization: Bearer JWT_TOKEN" \
  -d "proprieteId=123e4567-e89b-12d3-a456-426614174000" \
  -d "typeImpot=IF"
```

### Lister les déclarations

```bash
curl -X GET "http://localhost:8080/api/declarations?page=0&size=10&sort=date,desc" \
  -H "Authorization: Bearer JWT_TOKEN"
```

### Générer une taxation

```bash
curl -X POST "http://localhost:8080/api/taxations" \
  -H "Authorization: Bearer JWT_TOKEN" \
  -d "proprieteId=123e4567-e89b-12d3-a456-426614174000" \
  -d "proprieteImpotId=123e4567-e89b-12d3-a456-426614174001" \
  -d "exercice=2025"
```

## Conformité légale

Cette application est conforme aux cadres juridiques suivants :
- Constitution de la RDC (18 février 2006, révisée le 20 janvier 2011)
- Loi n° 11/002 (20 janvier 2011)
- Ordonnance-loi n° 69-006 (10 février 1969)
- Loi n° 04/2003 (13 mars 2003)
- Loi n° 83/004 (23 février 1983)
- Arrêté ministériel n° 004/ICAB/MINFIN/HKAT-KATANGA/2018 (10 août 2018)

## Développement

### Prérequis

- Java 17
- Maven 3.8+
- PostgreSQL 16 avec extension PostGIS

### Configuration

1. Créer une base de données PostgreSQL avec l'extension PostGIS
2. Mettre à jour `application.properties` avec vos identifiants de base de données
3. Exécuter l'application : `mvn spring-boot:run`

### Compilation

```bash
mvn clean package
```

### Exécution

```bash
mvn spring-boot:run
```

Ou :

```bash
java -jar target/impots-0.0.1-SNAPSHOT.jar
```

## Tests

Exécuter les tests unitaires et d'intégration :

```bash
mvn test
```

## Données de test

Le système inclut un chargeur de données de test (`TestDataSeeder`) qui crée des données initiales pour le développement et les tests :

- Divisions et bureaux
- Agents et contribuables
- Propriétés, concessions minières et véhicules
- Déclarations et taxations
- Paiements et apurements
- Pénalités et recouvrements
- Natures d'impôt et liens propriété-impôt

### Utilisateurs de test

- **admin** / **Tabc@123** : Administrateur
- **taxateur1** / **Tabc@123** : Agent taxateur
- **receveur1** / **Tabc@123** : Agent receveur
- **contrib1** / **Tabc@123** : Contribuable (première connexion)
- **contrib2** / **Tabc@123** : Contribuable (connexion directe)
- **dpri_ctest** / **Tabc@123** : Contribuable (format de nom d'utilisateur généré)

## Documentation

La documentation complète de l'API est disponible à :
- `http://localhost:8080/swagger-ui.html`
- `http://localhost:8080/v3/api-docs`

Une documentation détaillée du système est disponible dans le fichier `docs/DOCUMENTATION_COMPLETE.md`.
