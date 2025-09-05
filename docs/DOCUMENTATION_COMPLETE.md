# Documentation Complète du Système DPRIHKAT

## Table des matières

1. [Introduction](#1-introduction)
2. [Architecture du système](#2-architecture-du-système)
3. [Entités et modèle de données](#3-entités-et-modèle-de-données)
4. [API REST](#4-api-rest)
5. [Authentification et sécurité](#5-authentification-et-sécurité)
6. [Gestion des utilisateurs et des rôles](#6-gestion-des-utilisateurs-et-des-rôles)
7. [Gestion des contribuables](#7-gestion-des-contribuables)
8. [Gestion des propriétés](#8-gestion-des-propriétés)
9. [Déclarations et taxations](#9-déclarations-et-taxations)
10. [Paiements et apurements](#10-paiements-et-apurements)
11. [Pénalités et recouvrements](#11-pénalités-et-recouvrements)
12. [Géolocalisation](#12-géolocalisation)
13. [Données de référence](#13-données-de-référence)
14. [Déploiement](#14-déploiement)
15. [Tests et données de test](#15-tests-et-données-de-test)
16. [Conformité légale](#16-conformité-légale)
17. [Exemples de payloads](#17-exemples-de-payloads)

## 1. Introduction

Le système DPRIHKAT (Direction Provinciale des Recettes du Haut-Katanga) est une application de gestion des impôts provinciaux pour la province du Haut-Katanga en République Démocratique du Congo. Cette application permet la gestion complète du cycle de vie des impôts, depuis la déclaration jusqu'au paiement et au recouvrement.

### 1.1 Objectifs du système

- Faciliter la déclaration des impôts par les contribuables
- Automatiser le processus de taxation par les agents
- Gérer les paiements et les apurements
- Suivre les procédures de recouvrement
- Fournir des rapports et des tableaux de bord pour les différents rôles
- Assurer la conformité avec les lois fiscales en vigueur

### 1.2 Types d'impôts gérés

- **Impôt Foncier (IF)** : Impôt sur les propriétés foncières
- **Impôt sur les Revenus Locatifs (IRL)** : Impôt sur les revenus locatifs des propriétés
- **Retenue sur les Loyers (RL)** : Retenue à la source sur les loyers
- **Impôt Réel sur les Véhicules (IRV)** : Impôt sur les véhicules
- **Impôt sur les Concessions Minières (ICM)** : Impôt sur les concessions minières

## 2. Architecture du système

### 2.1 Stack technique

- **Backend** : Spring Boot 3.3.2 avec Java 17
- **Base de données** : PostgreSQL avec extension PostGIS
- **ORM** : Hibernate Spatial pour les données de géolocalisation
- **Sécurité** : Spring Security avec authentification JWT
- **Documentation API** : Springdoc OpenAPI
- **Outil de build** : Maven

### 2.2 Structure du projet

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

### 2.3 Flux de données

1. **Déclaration** : Le contribuable ou l'agent déclare un bien
2. **Taxation** : L'agent taxateur génère une note de taxation
3. **Validation** : Le chef de bureau valide la taxation
4. **Paiement** : Le contribuable effectue le paiement
5. **Apurement** : L'agent apureur valide le paiement
6. **Recouvrement** (si nécessaire) : Procédure de recouvrement en cas de non-paiement

## 3. Entités et modèle de données

### 3.1 Principales entités

#### 3.1.1 Utilisateur et Agent

- **Utilisateur** : Représente un utilisateur du système avec un login, mot de passe et rôle
- **Agent** : Représente un agent de l'administration fiscale
- **Contribuable** : Représente un contribuable (personne physique ou morale)

#### 3.1.2 Organisation

- **Division** : Division administrative
- **Bureau** : Bureau fiscal rattaché à une division

#### 3.1.3 Propriétés

- **Propriete** : Bien immobilier soumis à l'impôt foncier
- **ConcessionMinier** : Concession minière soumise à l'ICM
- **Vehicule** : Véhicule soumis à l'IRV

#### 3.1.4 Taxation

- **Declaration** : Déclaration d'un bien par un contribuable ou un agent
- **Taxation** : Application d'un impôt à une propriété pour un exercice fiscal
- **NatureImpot** : Type d'impôt applicable
- **ProprieteImpot** : Lien entre une propriété et une nature d'impôt

#### 3.1.5 Paiement et recouvrement

- **Paiement** : Paiement d'une taxation
- **Apurement** : Validation d'un paiement
- **Penalite** : Pénalité appliquée en cas de retard ou de non-déclaration
- **DossierRecouvrement** : Dossier de recouvrement pour les impayés
- **Relance** : Relance envoyée au contribuable
- **Poursuite** : Procédure de poursuite en cas de non-paiement

### 3.2 Relations entre entités

- Un **Utilisateur** peut être lié à un **Agent** ou un **Contribuable**
- Un **Agent** appartient à un **Bureau**
- Un **Bureau** appartient à une **Division**
- Un **Contribuable** possède plusieurs **Propriete**, **ConcessionMinier** ou **Vehicule**
- Une **Propriete** peut avoir plusieurs **ProprieteImpot** qui la lient à différentes **NatureImpot**
- Une **Declaration** est liée à une **Propriete** ou une **ConcessionMinier**
- Une **Taxation** est liée à une **Propriete** et un **ProprieteImpot**
- Une **Taxation** peut avoir un **Paiement** et un **Apurement**
- Un **DossierRecouvrement** contient des **Relance** et des **Poursuite**

### 3.3 Énumérations

- **Role** : ADMIN, TAXATEUR, CHEF_DE_BUREAU, CHEF_DE_DIVISION, DIRECTEUR, VERIFICATEUR, CONTROLLEUR, APUREUR, RECEVEUR_DES_IMPOTS, INFORMATICIEN, CONTRIBUABLE
- **TypeImpot** : IF, IRL, RL, IRV, ICM
- **StatutDeclaration** : SOUMISE, VALIDEE, REJETEE
- **StatutTaxation** : EN_ATTENTE, VALIDEE, PAYEE, PAYEE_PARTIELLEMENT, ANNULEE, EXONEREE, APUREE
- **StatutPaiement** : EN_ATTENTE, VALIDE, REJETE
- **ModePaiement** : ESPECE, BANQUE, MOBILE
- **TypeContribuable** : PERSONNE_PHYSIQUE, PERSONNE_MORALE
- **TypePropriete** : VI (Villa), AP (Appartement), CO (Commercial), DO (Domestique)
- **TypeConcession** : RECHERCHE, EXPLOITATION
- **MotifPenalite** : RETARD, NON_DECLARATION
- **TypeRelance** : COURRIER, EMAIL, TELEPHONE
- **StatutRelance** : ENVOYEE, RECUE, SANS_REPONSE
- **TypePoursuite** : MISE_EN_DEMEURE, SAISIE_IMMOBILIERE, SAISIE_MOBILIERE
- **StatutPoursuite** : EN_COURS, TERMINEE, ANNULEE
- **TypeApurement** : PAIEMENT_INTEGRAL, TRANSACTION, EXONERATION
- **StatutApurement** : EN_ATTENTE, ACCEPTEE, REJETEE
- **Sexe** : M, F

## 4. API REST

### 4.1 Format de réponse standard

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

En cas d'erreur :

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "ERROR_CODE",
    "message": "Message d'erreur",
    "details": "Détails de l'erreur"
  },
  "meta": {
    "timestamp": "2025-08-20T07:52:37.123456",
    "version": "1.0.0"
  }
}
```

### 4.2 Pagination

Tous les endpoints de liste supportent la pagination avec les paramètres standard de Spring Data JPA :
- `page` - Numéro de page (à partir de 0)
- `size` - Taille de la page
- `sort` - Champ et direction de tri (ex: `date,desc`)

Exemple : `GET /api/declarations?page=0&size=10&sort=date,desc`

### 4.3 Endpoints principaux

#### 4.3.1 Authentification

- `POST /api/auth/login` - Authentifier un utilisateur
- `POST /api/auth/changer-mot-de-passe` - Changer le mot de passe lors de la première connexion
- `POST /api/auth/refresh-token` - Rafraîchir le token JWT

#### 4.3.2 Utilisateurs

- `POST /api/users` - Créer un nouvel utilisateur
- `GET /api/users` - Lister tous les utilisateurs
- `GET /api/users/{id}` - Récupérer un utilisateur par son ID
- `PUT /api/users/{id}` - Mettre à jour un utilisateur
- `POST /api/users/bloquer` - Bloquer un compte utilisateur

#### 4.3.3 Contribuables

- `POST /api/contribuables` - Créer un nouveau contribuable
- `GET /api/contribuables` - Lister tous les contribuables
- `GET /api/contribuables/{id}` - Récupérer un contribuable par son ID
- `PUT /api/contribuables/{id}` - Mettre à jour un contribuable
- `GET /api/contribuables/mine` - Récupérer les informations du contribuable connecté

#### 4.3.4 Propriétés

- `POST /api/proprietes` - Créer une nouvelle propriété
- `GET /api/proprietes` - Lister toutes les propriétés
- `GET /api/proprietes/{id}` - Récupérer une propriété par son ID
- `PUT /api/proprietes/{id}` - Mettre à jour une propriété
- `GET /api/proprietes/mine` - Récupérer les propriétés du contribuable connecté

#### 4.3.5 Déclarations

- `POST /api/declarations/soumettre` - Soumettre une déclaration en ligne
- `POST /api/declarations/manuelle` - Enregistrer une déclaration manuelle
- `GET /api/declarations` - Lister toutes les déclarations
- `GET /api/declarations/{id}` - Récupérer une déclaration par son ID
- `PUT /api/declarations/{id}/valider` - Valider une déclaration
- `PUT /api/declarations/{id}/rejeter` - Rejeter une déclaration

#### 4.3.6 Taxations

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

#### 4.3.7 Paiements

- `POST /api/paiements` - Enregistrer un paiement
- `GET /api/paiements` - Lister tous les paiements
- `GET /api/paiements/{id}` - Récupérer un paiement par son ID
- `PUT /api/paiements/{id}/valider` - Valider un paiement
- `PUT /api/paiements/{id}/rejeter` - Rejeter un paiement

#### 4.3.8 Apurements

- `POST /api/apurements` - Créer un apurement
- `GET /api/apurements` - Lister tous les apurements
- `GET /api/apurements/{id}` - Récupérer un apurement par son ID
- `PUT /api/apurements/{id}/accepter` - Accepter un apurement
- `PUT /api/apurements/{id}/rejeter` - Rejeter un apurement

#### 4.3.9 Pénalités

- `POST /api/penalites/calculer` - Calculer automatiquement les pénalités
- `PUT /api/penalites/ajuster` - Ajuster manuellement les pénalités

#### 4.3.10 Recouvrements

- `POST /api/recouvrements` - Créer un dossier de recouvrement
- `GET /api/recouvrements` - Lister tous les dossiers de recouvrement
- `GET /api/recouvrements/{id}` - Récupérer un dossier de recouvrement par son ID
- `POST /api/recouvrements/{id}/relances` - Créer une relance
- `POST /api/recouvrements/{id}/poursuites` - Créer une poursuite

#### 4.3.11 Natures d'impôt et liens propriété-impôt

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

#### 4.3.12 Données de référence

- `GET /api/ref/communes` - Lister les communes
- `GET /api/ref/communes/{commune}/quartiers` - Lister les quartiers d'une commune
- `GET /api/ref/communes/{commune}/quartiers/{quartier}/avenues` - Lister les avenues d'un quartier
- `GET /api/ref/voitures/marques` - Lister les marques de voitures
- `GET /api/ref/voitures/marques/{marque}/models` - Lister les modèles d'une marque

## 5. Authentification et sécurité

### 5.1 Authentification JWT

Le système utilise JSON Web Tokens (JWT) pour l'authentification :

1. L'utilisateur s'authentifie avec son login et mot de passe
2. Le serveur génère un token JWT avec une durée de validité limitée
3. Le client inclut ce token dans l'en-tête `Authorization` de chaque requête
4. Le serveur vérifie la validité du token pour chaque requête

### 5.2 Refresh token

Pour éviter que l'utilisateur doive se reconnecter fréquemment :

1. Lors de l'authentification, le serveur génère également un refresh token avec une durée de validité plus longue
2. Lorsque le token JWT expire, le client peut utiliser le refresh token pour obtenir un nouveau token JWT
3. Si le refresh token est également expiré, l'utilisateur doit se reconnecter

### 5.3 Première connexion

Lors de la première connexion, l'utilisateur est obligé de changer son mot de passe :

1. L'utilisateur s'authentifie avec le mot de passe initial
2. Le système détecte qu'il s'agit d'une première connexion
3. L'utilisateur est redirigé vers la page de changement de mot de passe
4. Une fois le mot de passe changé, l'utilisateur peut accéder au système

### 5.4 Contrôle d'accès basé sur les rôles

Le système implémente un contrôle d'accès basé sur les rôles (RBAC) :

- Chaque endpoint est protégé par des annotations `@PreAuthorize`
- Seuls les utilisateurs avec les rôles appropriés peuvent accéder aux endpoints
- Les rôles sont hiérarchiques (ex: ADMIN > CHEF_DE_DIVISION > CHEF_DE_BUREAU)

## 6. Gestion des utilisateurs et des rôles

### 6.1 Rôles du système

- **ADMIN** : Accès administratif complet
- **TAXATEUR** : Gère les contribuables, les propriétés et la taxation
- **CHEF_DE_BUREAU** : Voit toutes les informations sur les contribuables et la taxation dans son bureau
- **CHEF_DE_DIVISION** : Voit tous les mouvements dans sa division
- **DIRECTEUR** : Voit tous les mouvements dans l'organisation
- **VERIFICATEUR** : Voit les contribuables et la taxation qui ont été traités
- **CONTROLLEUR** : Voit les contribuables et les notes problématiques
- **APUREUR** : Voit la taxation et valide les paiements
- **RECEVEUR_DES_IMPOTS** : Voit les notes en retard, les paiements et les dossiers en recouvrement
- **INFORMATICIEN** : Gère les utilisateurs et la configuration du système
- **CONTRIBUABLE** : Soumet des déclarations en ligne

### 6.2 Création d'utilisateurs

- Les utilisateurs avec le rôle ADMIN peuvent créer d'autres utilisateurs
- Pour les agents, le nom d'utilisateur est créé manuellement par l'administrateur
- Pour les contribuables, le nom d'utilisateur est généré automatiquement au format `dpri_c` + 4 caractères aléatoires
- Le mot de passe initial est `Tabc@123` pour tous les utilisateurs
- Lors de la création d'un contribuable, un email est envoyé avec les identifiants générés

### 6.3 Blocage de comptes

- Les utilisateurs avec les rôles ADMIN ou DIRECTEUR peuvent bloquer des comptes utilisateur
- Un compte bloqué ne peut plus se connecter au système
- Le blocage est réversible (le compte peut être débloqué)

## 7. Gestion des contribuables

### 7.1 Types de contribuables

- **Personne physique** : Individu
- **Personne morale** : Entreprise, association, etc.

### 7.2 Informations du contribuable

- **Nom** : Nom du contribuable
- **Adresse principale** : Adresse principale du contribuable
- **Adresse secondaire** : Adresse secondaire (optionnelle)
- **Téléphone principal** : Numéro de téléphone principal
- **Téléphone secondaire** : Numéro de téléphone secondaire (optionnel)
- **Email** : Adresse email
- **Nationalité** : Nationalité du contribuable
- **Type** : Type de contribuable (PERSONNE_PHYSIQUE ou PERSONNE_MORALE)
- **ID National** : Numéro d'identification nationale
- **NRC** : Numéro de registre de commerce (pour les personnes morales)
- **Sigle** : Sigle de l'entreprise (pour les personnes morales)
- **Numéro d'identification contribuable** : Numéro d'identification fiscale

### 7.3 Relation avec les agents

Chaque contribuable est associé à un agent qui est créé automatiquement lors de la création du contribuable. Cet agent est lié au contribuable via la relation `Contribuable.agent`.

## 8. Gestion des propriétés

### 8.1 Types de propriétés

- **VI (Villa)** : Maison individuelle
- **AP (Appartement)** : Appartement dans un immeuble
- **CO (Commercial)** : Local commercial
- **DO (Domestique)** : Propriété domestique

### 8.2 Informations de la propriété

- **Type** : Type de propriété (VI, AP, CO, DO)
- **Localité** : Localité où se trouve la propriété
- **Rang localité** : Rang de la localité (1, 2, 3, etc.)
- **Superficie** : Superficie en m²
- **Adresse** : Adresse de la propriété
- **Propriétaire** : Contribuable propriétaire
- **Montant impôt** : Montant de l'impôt calculé
- **Location** : Coordonnées géographiques (Point PostGIS)

### 8.3 Concessions minières

- **Nombre de carrés miniers** : Nombre de carrés miniers
- **Date d'acquisition** : Date d'acquisition de la concession
- **Type** : Type de concession (RECHERCHE ou EXPLOITATION)
- **Annexe** : Annexe de la concession
- **Titulaire** : Contribuable titulaire
- **Montant impôt** : Montant de l'impôt calculé

### 8.4 Véhicules

- **Marque** : Marque du véhicule
- **Modèle** : Modèle du véhicule
- **Année** : Année de fabrication
- **Plaque** : Numéro de plaque
- **Châssis** : Numéro de châssis
- **Propriétaire** : Contribuable propriétaire

## 9. Déclarations et taxations

### 9.1 Distinction entre déclaration et taxation

- **Déclaration** : Ajout d'un bien à un contribuable (par un agent ou par le contribuable lui-même)
- **Taxation** : Application d'un impôt à une propriété pour un exercice fiscal donné

### 9.2 Déclaration

#### 9.2.1 Informations de la déclaration

- **Date** : Date de la déclaration
- **Montant** : Montant de l'impôt
- **Statut** : Statut de la déclaration (SOUMISE, VALIDEE, REJETEE)
- **Type d'impôt** : Type d'impôt (IF, IRL, RL, IRV, ICM)
- **Exonération** : Indique si la déclaration est exonérée
- **Propriété** : Propriété concernée (pour IF, IRL, RL)
- **Concession** : Concession concernée (pour ICM)
- **Agent validateur** : Agent qui a validé la déclaration

#### 9.2.2 Processus de déclaration

1. **Déclaration en ligne** (contribuable) :
   - Le contribuable se connecte au système
   - Il sélectionne une propriété
   - Il soumet une déclaration
   - La déclaration est en statut SOUMISE

2. **Déclaration manuelle** (agent) :
   - L'agent se connecte au système
   - Il sélectionne un contribuable
   - Il sélectionne une propriété
   - Il soumet une déclaration
   - La déclaration est en statut SOUMISE

3. **Validation** (agent) :
   - L'agent vérifie la déclaration
   - Il valide la déclaration
   - La déclaration passe en statut VALIDEE

### 9.3 Taxation

#### 9.3.1 Informations de la taxation

- **Date de taxation** : Date de la taxation
- **Montant** : Montant de la taxation
- **Exercice** : Année fiscale
- **Statut** : Statut de la taxation (EN_ATTENTE, VALIDEE, PAYEE, PAYEE_PARTIELLEMENT, ANNULEE, EXONEREE, APUREE)
- **Type d'impôt** : Type d'impôt (IF, IRL, RL, IRV, ICM)
- **Exonération** : Indique si la taxation est exonérée
- **Motif exonération** : Motif de l'exonération (si applicable)
- **Date d'échéance** : Date limite de paiement
- **Propriété** : Propriété concernée
- **Propriété-impôt** : Lien entre la propriété et la nature d'impôt
- **Agent taxateur** : Agent qui a généré la taxation
- **Paiement** : Paiement associé (si applicable)
- **Apurement** : Apurement associé (si applicable)
- **Actif** : Indique si la taxation est active

#### 9.3.2 Processus de taxation

1. **Génération** :
   - L'agent taxateur génère une taxation pour une propriété
   - La taxation est en statut EN_ATTENTE
   - Le montant est calculé en fonction du lien propriété-impôt ou des taux JSON

2. **Validation** :
   - Le chef de bureau valide la taxation
   - La taxation passe en statut VALIDEE

3. **Paiement** :
   - Le contribuable effectue le paiement
   - La taxation passe en statut PAYEE ou PAYEE_PARTIELLEMENT

4. **Apurement** :
   - L'agent apureur valide le paiement
   - La taxation passe en statut APUREE

5. **Exonération** (si applicable) :
   - Le chef de bureau ou le chef de division accorde une exonération
   - La taxation passe en statut EXONEREE

### 9.4 Natures d'impôt et liens propriété-impôt

#### 9.4.1 Nature d'impôt

- **Code** : Code de la nature d'impôt (IF, IRL, RL, IRV, ICM)
- **Nom** : Nom de la nature d'impôt
- **Description** : Description de la nature d'impôt
- **Actif** : Indique si la nature d'impôt est active

#### 9.4.2 Lien propriété-impôt

- **Propriété** : Propriété concernée
- **Nature d'impôt** : Nature d'impôt applicable
- **Taux d'imposition** : Taux d'imposition spécifique
- **Actif** : Indique si le lien est actif

### 9.5 Calcul des impôts

#### 9.5.1 Impôt Foncier (IF)

Les taux sont chargés depuis `taux_if.json` :
- Villas : 0.50-2.00 USD/m²
- Appartements : 30-150 USD
- Commercial : 5-25 USD
- Domestique : 5-20 USD (selon le rang et le type de contribuable)

#### 9.5.2 Impôt sur les Concessions Minières (ICM)

Les taux sont chargés depuis `taux_icm.json` :
- Recherche : 0.03-0.12 USD/ha
- Exploitation : 0.03-0.12 USD/ha

#### 9.5.3 Impôt sur les Revenus Locatifs (IRL)

- Taux total : 22% (20% d'avance, 2% de solde dû avant le 1er février de l'année suivante)

#### 9.5.4 Retenue sur les Loyers (RL)

- Mêmes taux que l'IRL

#### 9.5.5 Impôt Réel sur les Véhicules (IRV)

- Basé sur la cylindrée/tonnage (ex: 54 USD + 19 USD TSCR pour <2.5T pour les particuliers)

## 10. Paiements et apurements

### 10.1 Paiement

#### 10.1.1 Informations du paiement

- **Date** : Date du paiement
- **Montant** : Montant du paiement
- **Mode** : Mode de paiement (ESPECE, BANQUE, MOBILE)
- **Statut** : Statut du paiement (EN_ATTENTE, VALIDE, REJETE)
- **Bordereau bancaire** : Numéro du bordereau bancaire (pour les paiements par banque)

#### 10.1.2 Processus de paiement

1. **Enregistrement** :
   - Le contribuable effectue un paiement
   - Le paiement est enregistré dans le système
   - Le paiement est en statut EN_ATTENTE

2. **Validation** :
   - L'agent apureur vérifie le paiement
   - Il valide le paiement
   - Le paiement passe en statut VALIDE

3. **Rejet** (si applicable) :
   - L'agent apureur rejette le paiement
   - Le paiement passe en statut REJETE

### 10.2 Apurement

#### 10.2.1 Informations de l'apurement

- **Date** : Date de l'apurement
- **Type** : Type d'apurement (PAIEMENT_INTEGRAL, TRANSACTION, EXONERATION)
- **Montant** : Montant de l'apurement
- **Motif** : Motif de l'apurement
- **Statut** : Statut de l'apurement (EN_ATTENTE, ACCEPTEE, REJETEE)
- **Provisoire** : Indique si l'apurement est provisoire
- **Agent** : Agent qui a créé l'apurement
- **Agent validateur** : Agent qui a validé l'apurement
- **Déclaration** : Déclaration concernée
- **Dossier de recouvrement** : Dossier de recouvrement concerné (si applicable)

#### 10.2.2 Processus d'apurement

1. **Création** :
   - L'agent apureur crée un apurement
   - L'apurement est en statut EN_ATTENTE

2. **Validation** :
   - Le chef de bureau valide l'apurement
   - L'apurement passe en statut ACCEPTEE

3. **Rejet** (si applicable) :
   - Le chef de bureau rejette l'apurement
   - L'apurement passe en statut REJETEE

## 11. Pénalités et recouvrements

### 11.1 Pénalités

#### 11.1.1 Informations de la pénalité

- **Motif** : Motif de la pénalité (RETARD, NON_DECLARATION)
- **Montant** : Montant de la pénalité
- **Date** : Date de la pénalité
- **Déclaration** : Déclaration concernée

#### 11.1.2 Règles de pénalité

- **Paiements en retard** : 2% par mois
- **Non-déclaration** : 25% de pénalité

### 11.2 Dossier de recouvrement

#### 11.2.1 Informations du dossier de recouvrement

- **Montant principal** : Montant principal dû
- **Montant pénalités** : Montant des pénalités
- **Date ouverture** : Date d'ouverture du dossier
- **Date clôture** : Date de clôture du dossier (si applicable)
- **Contribuable** : Contribuable concerné

### 11.3 Relance

#### 11.3.1 Informations de la relance

- **Date** : Date de la relance
- **Type** : Type de relance (COURRIER, EMAIL, TELEPHONE)
- **Statut** : Statut de la relance (ENVOYEE, RECUE, SANS_REPONSE)
- **Message** : Message de la relance
- **Dossier de recouvrement** : Dossier de recouvrement concerné

### 11.4 Poursuite

#### 11.4.1 Informations de la poursuite

- **Type** : Type de poursuite (MISE_EN_DEMEURE, SAISIE_IMMOBILIERE, SAISIE_MOBILIERE)
- **Date lancement** : Date de lancement de la poursuite
- **Statut** : Statut de la poursuite (EN_COURS, TERMINEE, ANNULEE)
- **Montant recouvré** : Montant recouvré
- **Agent initiateur** : Agent qui a initié la poursuite
- **Dossier de recouvrement** : Dossier de recouvrement concerné

## 12. Géolocalisation

### 12.1 PostGIS

Le système utilise l'extension PostGIS de PostgreSQL pour stocker et manipuler les données géographiques.

### 12.2 Coordonnées géographiques

Les propriétés sont géolocalisées avec des coordonnées géographiques (latitude, longitude) stockées dans un champ `location` de type `Point`.

### 12.3 Visualisation

Les coordonnées peuvent être utilisées pour visualiser les propriétés sur une carte.

## 13. Données de référence

### 13.1 Communes, quartiers et avenues

Les données de référence pour les communes, quartiers et avenues sont chargées depuis `communes.json` :

- `GET /api/ref/communes` - Liste des communes
- `GET /api/ref/communes/{commune}/quartiers` - Liste des quartiers d'une commune
- `GET /api/ref/communes/{commune}/quartiers/{quartier}/avenues` - Liste des avenues d'un quartier

### 13.2 Marques et modèles de voitures

Les données de référence pour les marques et modèles de voitures sont chargées depuis `voiture.json` :

- `GET /api/ref/voitures/marques` - Liste des marques de voitures
- `GET /api/ref/voitures/marques/{marque}/models` - Liste des modèles d'une marque

### 13.3 Taux d'imposition

Les taux d'imposition sont chargés depuis des fichiers JSON :

- `taux_if.json` - Taux pour l'Impôt Foncier
- `taux_icm.json` - Taux pour l'Impôt sur les Concessions Minières

## 14. Déploiement

### 14.1 Prérequis

- Java 17
- Maven 3.8+
- PostgreSQL 16 avec extension PostGIS

### 14.2 Configuration

La configuration de l'application se fait dans le fichier `application.properties` :

```properties
# Configuration de la base de données
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/dpri_impots}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:postgres}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:maman es}
spring.datasource.driver-class-name=org.postgresql.Driver

# Configuration JPA
spring.jpa.database-platform=org.hibernate.spatial.dialect.postgis.PostgisPG95Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true

# Configuration du serveur
server.port=${PORT:8080}

# Configuration JWT
app.jwtSecret=${JWT_SECRET:=======================DPRI=Impots=Secret===========================}
app.jwtExpirationMs=86400000
app.jwtRefreshExpirationMs=864000000

# Logging
logging.level.com.dpri.impots=DEBUG

# Flyway
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true

# Configuration email
spring.mail.host=${SPRING_MAIL_HOST:smtp.gmail.com}
spring.mail.port=${SPRING_MAIL_PORT:587}
spring.mail.username=${SPRING_MAIL_USERNAME:your-email@gmail.com}
spring.mail.password=${SPRING_MAIL_PASSWORD:your-password}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000
```

### 14.3 Déploiement sur un VPS

#### 14.3.1 Installation des prérequis

```bash
# Mise à jour du système
sudo apt update
sudo apt upgrade -y

# Installation de Java 17
sudo apt install openjdk-17-jdk -y

# Installation de PostgreSQL 16 et PostGIS
sudo sh -c 'echo "deb http://apt.postgresql.org/pub/repos/apt $(lsb_release -cs)-pgdg main" > /etc/apt/sources.list.d/pgdg.list'
wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo apt-key add -
sudo apt update
sudo apt install postgresql-16 postgresql-16-postgis-3 -y

# Configuration de PostgreSQL
sudo -u postgres psql -c "CREATE DATABASE dpri_impots;"
sudo -u postgres psql -c "CREATE USER dpri_user WITH ENCRYPTED PASSWORD 'your_password';"
sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE dpri_impots TO dpri_user;"
sudo -u postgres psql -d dpri_impots -c "CREATE EXTENSION postgis;"
```

#### 14.3.2 Configuration du service systemd

Créer un fichier `/etc/systemd/system/dprihkat.service` :

```
[Unit]
Description=DPRIHKAT Backend API
After=network.target

[Service]
User=dprihkat
Group=dprihkat
WorkingDirectory=/opt/dprihkat
ExecStart=/usr/bin/java -jar /opt/dprihkat/impots-0.0.1-SNAPSHOT.jar
EnvironmentFile=/opt/dprihkat/env
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

#### 14.3.3 Fichier d'environnement

Créer un fichier `/opt/dprihkat/env` :

```
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/dpri_impots
SPRING_DATASOURCE_USERNAME=dpri_user
SPRING_DATASOURCE_PASSWORD=your_password
JWT_SECRET=your_secret_key
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=your-email@gmail.com
SPRING_MAIL_PASSWORD=your-email-password
```

#### 14.3.4 Déploiement de l'application

```bash
# Créer l'utilisateur dprihkat
sudo useradd -m -d /opt/dprihkat -s /bin/bash dprihkat

# Copier le JAR dans le répertoire de déploiement
sudo cp impots-0.0.1-SNAPSHOT.jar /opt/dprihkat/

# Définir les permissions
sudo chown -R dprihkat:dprihkat /opt/dprihkat
sudo chmod 600 /opt/dprihkat/env

# Activer et démarrer le service
sudo systemctl enable dprihkat
sudo systemctl start dprihkat
```

### 14.4 Déploiement avec Docker

#### 14.4.1 Dockerfile

```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/impots-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### 14.4.2 Déploiement sur Railway

1. Créer un projet sur Railway
2. Ajouter un service PostgreSQL
3. Ajouter un service à partir du Dockerfile
4. Configurer les variables d'environnement :
   - `SPRING_DATASOURCE_URL` : Utiliser la variable `DATABASE_URL` de Railway
   - `SPRING_DATASOURCE_USERNAME` : Utiliser la variable `PGUSER` de Railway
   - `SPRING_DATASOURCE_PASSWORD` : Utiliser la variable `PGPASSWORD` de Railway
   - `JWT_SECRET` : Définir une valeur secrète
   - `SPRING_MAIL_HOST`, `SPRING_MAIL_PORT`, `SPRING_MAIL_USERNAME`, `SPRING_MAIL_PASSWORD` : Configurer selon votre service de messagerie

## 15. Tests et données de test

### 15.1 Données de test

Le système inclut un chargeur de données de test (`TestDataSeeder`) qui crée des données initiales pour le développement et les tests :

- Divisions et bureaux
- Agents et contribuables
- Propriétés, concessions minières et véhicules
- Déclarations et taxations
- Paiements et apurements
- Pénalités et recouvrements
- Natures d'impôt et liens propriété-impôt

### 15.2 Utilisateurs de test

- **admin** / **Tabc@123** : Administrateur
- **taxateur1** / **Tabc@123** : Agent taxateur
- **receveur1** / **Tabc@123** : Agent receveur
- **contrib1** / **Tabc@123** : Contribuable (première connexion)
- **contrib2** / **Tabc@123** : Contribuable (connexion directe)
- **dpri_ctest** / **Tabc@123** : Contribuable (format de nom d'utilisateur généré)

### 15.3 Tests unitaires et d'intégration

Le système inclut des tests unitaires et d'intégration pour valider le bon fonctionnement des fonctionnalités.

## 16. Conformité légale

Le système est conforme aux cadres légaux suivants :

- Constitution de la RDC (18 février 2006, révisée le 20 janvier 2011)
- Loi n° 11/002 (20 janvier 2011)
- Ordonnance-loi n° 69-006 (10 février 1969)
- Loi n° 04/2003 (13 mars 2003)
- Loi n° 83/004 (23 février 1983)
- Arrêté ministériel n° 004/ICAB/MINFIN/HKAT-KATANGA/2018 (10 août 2018)

## 17. Exemples de payloads

Cette section présente des exemples de payloads pour les principales fonctionnalités du système.

### 17.1 Authentification

#### 17.1.1 Login

**Endpoint**: `POST /api/auth/login`

**Payload**:
```json
{
  "login": "taxateur1",
  "motDePasse": "Tabc@123"
}
```

**Réponse**:
```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "username": "taxateur1",
    "role": "TAXATEUR",
    "premierConnexion": false
  },
  "error": null,
  "meta": {
    "timestamp": "2025-09-05T21:56:00.123456",
    "version": "1.0.0"
  }
}
```

#### 17.1.2 Changement de mot de passe

**Endpoint**: `POST /api/auth/changer-mot-de-passe`

**Payload**:
```json
{
  "ancienMotDePasse": "Tabc@123",
  "nouveauMotDePasse": "NouveauMdp@456",
  "confirmationMotDePasse": "NouveauMdp@456"
}
```

**Réponse**:
```json
{
  "success": true,
  "data": {
    "message": "Mot de passe changé avec succès"
  },
  "error": null,
  "meta": {
    "timestamp": "2025-09-05T21:56:00.123456",
    "version": "1.0.0"
  }
}
```

### 17.2 Gestion des contribuables

#### 17.2.1 Création d'un contribuable

**Endpoint**: `POST /api/contribuables`

**Payload**:
```json
{
  "nom": "Jean Dupont",
  "adressePrincipale": "123 Avenue Principale, Kinshasa",
  "adresseSecondaire": "456 Rue Secondaire, Kinshasa",
  "telephonePrincipal": "+243123456789",
  "telephoneSecondaire": "+243987654321",
  "email": "jean.dupont@example.com",
  "nationalite": "Congolaise",
  "type": "PERSONNE_PHYSIQUE",
  "idNat": "ID123456",
  "NRC": "NRC789012",
  "sigle": "JD",
  "numeroIdentificationContribuable": "NIC345678"
}
```

**Réponse**:
```json
{
  "success": true,
  "data": {
    "message": "Contribuable créé avec succès. Les identifiants ont été envoyés par email.",
    "contribuable": {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "nom": "Jean Dupont",
      "adressePrincipale": "123 Avenue Principale, Kinshasa",
      "adresseSecondaire": "456 Rue Secondaire, Kinshasa",
      "telephonePrincipal": "+243123456789",
      "telephoneSecondaire": "+243987654321",
      "email": "jean.dupont@example.com",
      "nationalite": "Congolaise",
      "type": "PERSONNE_PHYSIQUE",
      "idNat": "ID123456",
      "NRC": "NRC789012",
      "sigle": "JD",
      "numeroIdentificationContribuable": "NIC345678",
      "actif": true
    }
  },
  "error": null,
  "meta": {
    "timestamp": "2025-09-05T21:56:00.123456",
    "version": "1.0.0"
  }
}
```

### 17.3 Gestion des propriétés

#### 17.3.1 Création d'une propriété

**Endpoint**: `POST /api/proprietes`

**Payload**:
```json
{
  "type": "VI",
  "localite": "Katuba",
  "rangLocalite": 2,
  "superficie": 350.0,
  "adresse": "Parcelle 123, Q. Katuba",
  "proprietaireId": "123e4567-e89b-12d3-a456-426614174000",
  "location": {
    "type": "Point",
    "coordinates": [11.65, -7.45]
  }
}
```

**Réponse**:
```json
{
  "success": true,
  "data": {
    "propriete": {
      "id": "123e4567-e89b-12d3-a456-426614174001",
      "type": "VI",
      "localite": "Katuba",
      "rangLocalite": 2,
      "superficie": 350.0,
      "adresse": "Parcelle 123, Q. Katuba",
      "proprietaireId": "123e4567-e89b-12d3-a456-426614174000",
      "proprietaireNom": "Jean Dupont",
      "montantImpot": 437.5,
      "location": {
        "type": "Point",
        "coordinates": [11.65, -7.45]
      }
    }
  },
  "error": null,
  "meta": {
    "timestamp": "2025-09-05T21:56:00.123456",
    "version": "1.0.0"
  }
}
```

#### 17.3.2 Création d'une concession minière

**Endpoint**: `POST /api/concessions`

**Payload**:
```json
{
  "nombreCarresMinier": 12.0,
  "dateAcquisition": "2025-01-15",
  "type": "EXPLOITATION",
  "annexe": "annexe1",
  "titulaireId": "123e4567-e89b-12d3-a456-426614174000"
}
```

**Réponse**:
```json
{
  "success": true,
  "data": {
    "concession": {
      "id": "123e4567-e89b-12d3-a456-426614174002",
      "nombreCarresMinier": 12.0,
      "dateAcquisition": "2025-01-15",
      "type": "EXPLOITATION",
      "annexe": "annexe1",
      "titulaireId": "123e4567-e89b-12d3-a456-426614174000",
      "titulaireNom": "Jean Dupont",
      "montantImpot": 2038.92
    }
  },
  "error": null,
  "meta": {
    "timestamp": "2025-09-05T21:56:00.123456",
    "version": "1.0.0"
  }
}
```

### 17.4 Déclarations

#### 17.4.1 Soumission d'une déclaration en ligne

**Endpoint**: `POST /api/declarations/soumettre`

**Payload**:
```json
{
  "proprieteId": "123e4567-e89b-12d3-a456-426614174001",
  "typeImpot": "IF"
}
```

**Réponse**:
```json
{
  "success": true,
  "data": {
    "message": "Déclaration soumise avec succès",
    "declaration": {
      "id": "123e4567-e89b-12d3-a456-426614174003",
      "date": "2025-09-05T21:56:00.123456",
      "montant": 437.5,
      "statut": "SOUMISE",
      "typeImpot": "IF",
      "exoneration": false,
      "proprieteId": "123e4567-e89b-12d3-a456-426614174001",
      "proprieteAdresse": "Parcelle 123, Q. Katuba"
    }
  },
  "error": null,
  "meta": {
    "timestamp": "2025-09-05T21:56:00.123456",
    "version": "1.0.0"
  }
}
```

#### 17.4.2 Enregistrement d'une déclaration manuelle

**Endpoint**: `POST /api/declarations/manuelle`

**Payload**:
```json
{
  "proprieteId": "123e4567-e89b-12d3-a456-426614174001",
  "typeImpot": "IF",
  "montant": 437.5
}
```

**Réponse**:
```json
{
  "success": true,
  "data": {
    "message": "Déclaration enregistrée avec succès",
    "declaration": {
      "id": "123e4567-e89b-12d3-a456-426614174004",
      "date": "2025-09-05T21:56:00.123456",
      "montant": 437.5,
      "statut": "SOUMISE",
      "typeImpot": "IF",
      "exoneration": false,
      "proprieteId": "123e4567-e89b-12d3-a456-426614174001",
      "proprieteAdresse": "Parcelle 123, Q. Katuba",
      "agentValidateurId": "123e4567-e89b-12d3-a456-426614174005",
      "agentValidateurNom": "KAPENDA Jean"
    }
  },
  "error": null,
  "meta": {
    "timestamp": "2025-09-05T21:56:00.123456",
    "version": "1.0.0"
  }
}
```

### 17.5 Taxations

#### 17.5.1 Génération d'une taxation

**Endpoint**: `POST /api/taxations`

**Payload**:
```json
{
  "proprieteId": "123e4567-e89b-12d3-a456-426614174001",
  "proprieteImpotId": "123e4567-e89b-12d3-a456-426614174006",
  "exercice": 2025
}
```

**Réponse**:
```json
{
  "success": true,
  "data": {
    "message": "Taxation générée avec succès",
    "taxation": {
      "id": "123e4567-e89b-12d3-a456-426614174007",
      "dateTaxation": "2025-09-05T21:56:00.123456",
      "montant": 437.5,
      "exercice": 2025,
      "statut": "EN_ATTENTE",
      "typeImpot": "IF",
      "exoneration": false,
      "motifExoneration": null,
      "dateEcheance": "2025-12-31T23:59:59",
      "actif": true,
      "proprieteId": "123e4567-e89b-12d3-a456-426614174001",
      "proprieteAdresse": "Parcelle 123, Q. Katuba",
      "proprieteImpotId": "123e4567-e89b-12d3-a456-426614174006",
      "natureImpotCode": "IF",
      "natureImpotNom": "Impôt Foncier",
      "agentTaxateurId": "123e4567-e89b-12d3-a456-426614174005",
      "agentTaxateurNom": "KAPENDA Jean",
      "paiementId": null,
      "apurementId": null
    }
  },
  "error": null,
  "meta": {
    "timestamp": "2025-09-05T21:56:00.123456",
    "version": "1.0.0"
  }
}
```

#### 17.5.2 Calcul du montant de la taxation pour une propriété

**Endpoint**: `GET /api/taxations/calculate/property/{proprieteId}`

**Réponse**:
```json
{
  "success": true,
  "data": {
    "message": "Calcul de la taxation effectué avec succès",
    "montant": 437.5
  },
  "error": null,
  "meta": {
    "timestamp": "2025-09-05T21:56:00.123456",
    "version": "1.0.0"
  }
}
```

#### 17.5.3 Mise à jour du statut d'une taxation

**Endpoint**: `PUT /api/taxations/{id}/statut/{statut}`

**Réponse**:
```json
{
  "success": true,
  "data": {
    "message": "Statut de la taxation mis à jour avec succès",
    "taxation": {
      "id": "123e4567-e89b-12d3-a456-426614174007",
      "dateTaxation": "2025-09-05T21:56:00.123456",
      "montant": 437.5,
      "exercice": 2025,
      "statut": "VALIDEE",
      "typeImpot": "IF",
      "exoneration": false,
      "motifExoneration": null,
      "dateEcheance": "2025-12-31T23:59:59",
      "actif": true,
      "proprieteId": "123e4567-e89b-12d3-a456-426614174001",
      "proprieteAdresse": "Parcelle 123, Q. Katuba",
      "proprieteImpotId": "123e4567-e89b-12d3-a456-426614174006",
      "natureImpotCode": "IF",
      "natureImpotNom": "Impôt Foncier",
      "agentTaxateurId": "123e4567-e89b-12d3-a456-426614174005",
      "agentTaxateurNom": "KAPENDA Jean",
      "paiementId": null,
      "apurementId": null
    }
  },
  "error": null,
  "meta": {
    "timestamp": "2025-09-05T21:56:00.123456",
    "version": "1.0.0"
  }
}
```

#### 17.5.4 Accord d'une exonération

**Endpoint**: `PUT /api/taxations/{id}/exoneration?motif=Motif de l'exonération`

**Réponse**:
```json
{
  "success": true,
  "data": {
    "message": "Exonération accordée avec succès",
    "taxation": {
      "id": "123e4567-e89b-12d3-a456-426614174007",
      "dateTaxation": "2025-09-05T21:56:00.123456",
      "montant": 437.5,
      "exercice": 2025,
      "statut": "EXONEREE",
      "typeImpot": "IF",
      "exoneration": true,
      "motifExoneration": "Motif de l'exonération",
      "dateEcheance": "2025-12-31T23:59:59",
      "actif": true,
      "proprieteId": "123e4567-e89b-12d3-a456-426614174001",
      "proprieteAdresse": "Parcelle 123, Q. Katuba",
      "proprieteImpotId": "123e4567-e89b-12d3-a456-426614174006",
      "natureImpotCode": "IF",
      "natureImpotNom": "Impôt Foncier",
      "agentTaxateurId": "123e4567-e89b-12d3-a456-426614174005",
      "agentTaxateurNom": "KAPENDA Jean",
      "paiementId": null,
      "apurementId": null
    }
  },
  "error": null,
  "meta": {
    "timestamp": "2025-09-05T21:56:00.123456",
    "version": "1.0.0"
  }
}
```

### 17.6 Paiements

#### 17.6.1 Enregistrement d'un paiement

**Endpoint**: `POST /api/paiements`

**Payload**:
```json
{
  "taxationId": "123e4567-e89b-12d3-a456-426614174007",
  "montant": 437.5,
  "mode": "BANQUE",
  "bordereauBancaire": "BORD-2025-0001"
}
```

**Réponse**:
```json
{
  "success": true,
  "data": {
    "message": "Paiement enregistré avec succès",
    "paiement": {
      "id": "123e4567-e89b-12d3-a456-426614174008",
      "date": "2025-09-05T21:56:00.123456",
      "montant": 437.5,
      "mode": "BANQUE",
      "statut": "EN_ATTENTE",
      "bordereauBancaire": "BORD-2025-0001",
      "taxationId": "123e4567-e89b-12d3-a456-426614174007"
    }
  },
  "error": null,
  "meta": {
    "timestamp": "2025-09-05T21:56:00.123456",
    "version": "1.0.0"
  }
}
```

### 17.7 Apurements

#### 17.7.1 Création d'un apurement

**Endpoint**: `POST /api/apurements`

**Payload**:
```json
{
  "type": "PAIEMENT_INTEGRAL",
  "montant": 437.5,
  "motif": "Paiement intégral de la taxation",
  "provisoire": false,
  "taxationId": "123e4567-e89b-12d3-a456-426614174007",
  "paiementId": "123e4567-e89b-12d3-a456-426614174008"
}
```

**Réponse**:
```json
{
  "success": true,
  "data": {
    "message": "Apurement créé avec succès",
    "apurement": {
      "id": "123e4567-e89b-12d3-a456-426614174009",
      "date": "2025-09-05T21:56:00.123456",
      "type": "PAIEMENT_INTEGRAL",
      "montant": 437.5,
      "motif": "Paiement intégral de la taxation",
      "statut": "EN_ATTENTE",
      "provisoire": false,
      "agentId": "123e4567-e89b-12d3-a456-426614174005",
      "agentNom": "KAPENDA Jean",
      "taxationId": "123e4567-e89b-12d3-a456-426614174007",
      "paiementId": "123e4567-e89b-12d3-a456-426614174008"
    }
  },
  "error": null,
  "meta": {
    "timestamp": "2025-09-05T21:56:00.123456",
    "version": "1.0.0"
  }
}
```

### 17.8 Natures d'impôt et liens propriété-impôt

#### 17.8.1 Création d'une nature d'impôt

**Endpoint**: `POST /api/natures-impot`

**Payload**:
```json
{
  "code": "IF",
  "nom": "Impôt Foncier",
  "description": "Impôt sur les propriétés foncières"
}
```

**Réponse**:
```json
{
  "success": true,
  "data": {
    "natureImpot": {
      "id": "123e4567-e89b-12d3-a456-426614174010",
      "code": "IF",
      "nom": "Impôt Foncier",
      "description": "Impôt sur les propriétés foncières",
      "actif": true
    }
  },
  "error": null,
  "meta": {
    "timestamp": "2025-09-05T21:56:00.123456",
    "version": "1.0.0"
  }
}
```

#### 17.8.2 Création d'un lien propriété-impôt

**Endpoint**: `POST /api/proprietes-impots`

**Payload**:
```json
{
  "proprieteId": "123e4567-e89b-12d3-a456-426614174001",
  "natureImpotId": "123e4567-e89b-12d3-a456-426614174010",
  "tauxImposition": 1.25
}
```

**Réponse**:
```json
{
  "success": true,
  "data": {
    "proprieteImpot": {
      "id": "123e4567-e89b-12d3-a456-426614174006",
      "proprieteId": "123e4567-e89b-12d3-a456-426614174001",
      "proprieteAdresse": "Parcelle 123, Q. Katuba",
      "natureImpotId": "123e4567-e89b-12d3-a456-426614174010",
      "natureImpotCode": "IF",
      "natureImpotNom": "Impôt Foncier",
      "tauxImposition": 1.25,
      "actif": true
    }
  },
  "error": null,
  "meta": {
    "timestamp": "2025-09-05T21:56:00.123456",
    "version": "1.0.0"
  }
}
```

### 17.9 Utilisateurs

#### 17.9.1 Création d'un utilisateur

**Endpoint**: `POST /api/users`

**Payload**:
```json
{
  "login": "taxateur2",
  "motDePasse": "Tabc@123",
  "role": "TAXATEUR",
  "agentId": "123e4567-e89b-12d3-a456-426614174011"
}
```

**Réponse**:
```json
{
  "success": true,
  "data": {
    "message": "Utilisateur créé avec succès",
    "utilisateur": {
      "id": "123e4567-e89b-12d3-a456-426614174012",
      "login": "taxateur2",
      "role": "TAXATEUR",
      "premierConnexion": true,
      "bloque": false,
      "agentId": "123e4567-e89b-12d3-a456-426614174011",
      "agentNom": "MUTUMBO Pierre"
    }
  },
  "error": null,
  "meta": {
    "timestamp": "2025-09-05T21:56:00.123456",
    "version": "1.0.0"
  }
}
```
