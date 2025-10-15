# Documentation du Module de Recouvrement

## Introduction

Le module de recouvrement gère l'ensemble du processus de recouvrement des impôts et taxes, depuis l'émission des avis de mise en recouvrement jusqu'aux mesures de poursuite. Il implémente les différentes étapes du circuit de recouvrement conformément à la législation fiscale.

## Types de Documents de Recouvrement

### 1. Avis de Mise en Recouvrement (AMR)

L'AMR est émis suite à un contrôle fiscal ayant abouti à un redressement ou une taxation d'office. Il contient :
- L'identification du contribuable
- La nature de l'impôt ou des droits dus
- La base imposable
- Le montant en principal
- Le montant des pénalités
- Le délai de paiement (15 jours)

### 2. Mise En Demeure (MED)

La MED est é mise en cas de déclaration sans paiement ou avec paiement insuffisant. Elle contient :
- L'identification du contribuable
- Le montant dû
- Le délai de paiement (8 jours)

### 3. Contrainte Fiscale

La contrainte est décernée par le Receveur des Impôts lorsque l'AMR ou la MED n'a pas été exécuté dans le délai imparti.

### 4. Commandement de Payer

Le commandement est signifié au contribuable par un huissier du Trésor, lui enjoignant de payer dans un délai de 8 jours, sous peine d'exécution des mesures de poursuite.

### 5. Avis à Tiers Détenteur (ATD)

L'ATD est adressé aux tiers détenteurs (banques, locataires, débiteurs, etc.) pour qu'ils versent directement au Trésor les sommes qu'ils doivent au contribuable défaillant.

### 6. Saisies

#### 6.1 Saisie Mobilière

Saisie des biens mobiliers du contribuable défaillant, avec possibilité de vente aux enchères.

#### 6.2 Saisie Immobilière

Saisie des biens immobiliers du contribuable défaillant, avec possibilité de vente aux enchères.

### 7. Fermeture d'Établissement

Mesure de fermeture provisoire des établissements par apposition de scellés, effectuée par un agent OPJ.

## Circuit du Recouvrement

### Phase 1 : Émission des documents initiaux

1. **Contrôle fiscal** : Émission d'un AMR (délai de paiement : 15 jours)
2. **Déclaration sans paiement** : Émission d'une MED (délai de paiement : 8 jours)

### Phase 2 : Non-exécution des documents initiaux

1. **Non-exécution de l'AMR** : Contrainte décernée par le Receveur et signification du Commandement par l'Huissier
2. **Non-exécution de la MED** : Contrainte décernée par le Receveur et signification du Commandement par l'Huissier

### Phase 3 : Non-exécution du Commandement

1. **Mesures de poursuite** :
   - ATD
   - Saisie mobilière et immobilière
   - Fermeture des établissements

## Pénalités de Recouvrement

- Retard de paiement : 2% par mois de retard
- Frais de poursuite :
  - Commandement : 3% du montant dû
  - Saisie : 5% du montant dû
  - Vente : 3% du montant dû

## Amendes pour Fermeture d'Établissement

Les amendes pour fermeture d'établissement sont fixées selon le type de contribuable :

1. **Personnes Morales** : 1.000.000 FC
2. **Personnes Physiques** :
   - Commerçants : 100.000 FC
   - Non-commerçants : 50.000 FC
3. **Autres cas** : 100.000 FC (montant par défaut)

## Données du Contribuable

Le système maintient des informations détaillées sur chaque contribuable, incluant :

### 1. Informations de Base
- Identité (nom, matricule, etc.)
- Adresses et contacts
- Type de contribuable (personne morale/physique)
- Statut de commerçant
- Documents d'identification (IdNat, NRC, etc.)

### 2. Biens
- **Propriétés Immobilières**
  - Adresse
  - Références cadastrales
  - Superficie
  - Usage
  - Valeur locative
  - Montant de l'impôt

- **Véhicules**
  - Immatriculation
  - Marque et modèle
  - Numéro de chassis
  - Type de véhicule
  - Cylindrée
  - Montant de la vignette

### 3. Déclarations et Taxations
- **Déclarations**
  - Type d'impôt
  - Période et exercice
  - Montants déclarés
  - Statut
  - Taxateur assigné

- **Taxations**
  - Base imposable
  - Montants imposés
  - Pénalités
  - Agent taxateur

### 4. Documents de Recouvrement
- Historique des documents émis
- Statuts et échéances
- Montants (principal, pénalités, total)
- Agents responsables

### 5. Relances
- Type de relance
- Date d'envoi
- Contenu
- Statut
- Agent émetteur

## API du Module de Recouvrement

[Documentation existante des API...]

### Nouvelles fonctionnalités

#### 1. Gestion des amendes pour fermeture d'établissement

La gestion des amendes pour fermeture d'établissement est maintenant possible via l'API.

#### 2. Gestion des biens du contribuable

La gestion des biens du contribuable est maintenant possible via l'API.

#### 3. Gestion des déclarations et des taxations

La gestion des déclarations et des taxations est maintenant possible via l'API.

#### 4. Gestion des documents de recouvrement

La gestion des documents de recouvrement est maintenant possible via l'API.

#### 5. Gestion des relations

La gestion des relations est maintenant possible via l'API.
