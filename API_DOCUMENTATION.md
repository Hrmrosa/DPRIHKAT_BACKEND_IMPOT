# Documentation de l'API DPRIHKAT

## Introduction

Cette documentation décrit les routes, les payloads et l'utilisation de l'API DPRIHKAT pour la gestion des impôts fonciers et miniers.

## Table des matières

1. [Authentification](AUTHENTIFICATION.md)
2. [Utilisateurs](UTILISATEURS.md)
3. [Agents](AGENTS.md)
4. [Contribuables](CONTRIBUABLES.md)
5. [Propriétés](PROPRIETES.md)
6. [Déclarations](DECLARATIONS.md)
7. [Taxations](TAXATIONS.md)
8. [Paiements](PAIEMENTS.md)

## Utilisation de l'API

Toutes les routes de l'API sont préfixées par `/api`.

### Format des requêtes

- Toutes les requêtes doivent être au format JSON
- Les dates doivent être au format ISO 8601
- Les UUID doivent être au format standard

### Format des réponses

- Toutes les réponses sont au format JSON
- Les codes HTTP sont utilisés pour indiquer le statut des opérations

### Codes HTTP

- `200` - Succès
- `201` - Création réussie
- `400` - Requête invalide
- `401` - Non authentifié
- `403` - Non autorisé
- `404` - Ressource non trouvée
- `500` - Erreur interne du serveur

## Authentification

L'API utilise un système de jetons JWT pour l'authentification. Après la connexion, un jeton est retourné et doit être inclus dans l'en-tête `Authorization` de toutes les requêtes suivantes.

### En-tête d'authentification

```
Authorization: Bearer <token>
```

## Erreurs

En cas d'erreur, l'API retourne un objet JSON avec les champs suivants :

```json
{
  "timestamp": "date-time",
  "status": "integer",
  "error": "string",
  "message": "string",
  "path": "string"
}
```
