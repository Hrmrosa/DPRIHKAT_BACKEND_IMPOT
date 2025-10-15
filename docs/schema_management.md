# Gestion du Schéma de Base de Données

Ce document décrit les bonnes pratiques pour gérer le schéma de base de données dans le projet DPRIHKAT_BACKEND_IMPOT.

## Problèmes courants

1. **Colonnes NOT NULL sans valeur par défaut** : Lorsqu'on ajoute une contrainte NOT NULL à une colonne existante qui contient des valeurs NULL.
2. **Modifications de schéma conflictuelles** : Hibernate tente de modifier le schéma alors que Flyway est configuré pour gérer les migrations.

## Solutions mises en place

### 1. Migrations Flyway

Nous utilisons Flyway pour gérer les migrations de base de données. Toutes les modifications de schéma doivent être effectuées via des scripts de migration Flyway.

Exemples de scripts de migration :
- `V11__fix_nullable_columns.sql` : Met à jour les colonnes NULL existantes avec des valeurs par défaut.
- `V12__modify_column_constraints.sql` : Modifie les contraintes sur les colonnes pour une meilleure flexibilité.

### 2. Configuration Hibernate

Nous avons modifié la configuration Hibernate pour éviter les conflits avec Flyway :

```properties
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.schema_validation.enabled=false
```

- `validate` : Hibernate vérifie que le schéma correspond aux entités, mais ne tente pas de le modifier.
- `schema_validation.enabled=false` : Désactive la validation stricte du schéma au démarrage.

### 3. Valeurs par défaut dans les entités

Toutes les entités doivent définir des valeurs par défaut pour les champs obligatoires :

```java
private boolean actif = true; // Valeur par défaut
```

### 4. Triggers et contraintes PostgreSQL

Nous avons ajouté des triggers et des contraintes CHECK dans PostgreSQL pour garantir l'intégrité des données :

- Triggers pour définir des valeurs par défaut lors de l'insertion
- Contraintes CHECK pour s'assurer que les nouvelles entrées respectent les règles

### Bonnes pratiques

1. **Toujours utiliser Flyway** pour les modifications de schéma
2. **Définir des valeurs par défaut** dans les entités Java
3. **Tester les migrations** dans un environnement de développement avant de les appliquer en production
4. **Documenter les changements** de schéma importants

## Commandes utiles

### Réinitialiser la base de données

```bash
# Supprimer la base de données
dropdb dpri_impots

# Créer une nouvelle base de données
createdb dpri_impots

# Ajouter l'extension PostGIS
psql -d dpri_impots -c "CREATE EXTENSION postgis;"
```

### Vérifier l'état des migrations Flyway

```bash
./mvnw flyway:info
```

### Réparer l'historique des migrations Flyway

```bash
./mvnw flyway:repair
```
