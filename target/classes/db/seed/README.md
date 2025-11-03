# Données de Test pour l'Application DPRI-HKAT

Ce répertoire contient les scripts SQL pour initialiser la base de données avec des données de test.

## Structure des fichiers

1. `0-schema.sql` : Création des tables et types enum
2. `1-utilisateurs.sql` : Données des utilisateurs et agents
3. `2-contribuables.sql` : Données des contribuables
4. `3-biens.sql` : Données des propriétés et véhicules
5. `4-declarations.sql` : Données des déclarations et taxations
6. `5-recouvrement.sql` : Données du recouvrement (dossiers, documents, fermetures, relances)
7. `6-paiements.sql` : Données des paiements et quittances

## Ordre d'exécution

Les scripts doivent être exécutés dans l'ordre numérique pour respecter les dépendances entre les tables.

## Données de test

### Utilisateurs
- 1 administrateur
- 1 receveur
- 1 taxateur
- 1 OPJ
- 1 huissier
- Plusieurs contribuables

### Contribuables
- 2 personnes morales
- 3 personnes physiques (dont 1 commerçant)

### Biens
- 5 propriétés (commerciales, résidentielles, industrielles)
- 5 véhicules (particuliers, utilitaires, poids lourds)

### Déclarations et Taxations
- 5 déclarations (IBP, IPR, TVA)
- 5 taxations correspondantes

### Recouvrement
- 5 dossiers de recouvrement
- Documents variés (AMR, MED, Contraintes, etc.)
- 2 fermetures d'établissement
- 4 relances de différents types

### Paiements
- 5 paiements avec différents modes
- 5 quittances correspondantes

## Identifiants par défaut

### Administrateur
- Login : admin
- Mot de passe : password

### Receveur
- Login : receveur1
- Mot de passe : password

### Taxateur
- Login : taxateur1
- Mot de passe : password

### OPJ
- Login : opj1
- Mot de passe : password

### Huissier
- Login : huissier1
- Mot de passe : password

### Contribuable
- Login : contribuable1
- Mot de passe : password

Note : Tous les mots de passe sont hashés avec BCrypt dans la base de données.
