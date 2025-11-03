# Documentation API - Audit et Logs

Cette documentation détaille les endpoints disponibles pour l'audit et les logs système dans l'API DPRIHKAT.

--- 

## Vue d'ensemble

Le système d'audit permet de tracer toutes les actions effectuées dans le système pour assurer la sécurité et la conformité.

### Base URL
```
/api/audit-logs
```

---

## Endpoints

### 1. Rechercher dans les logs d'audit

Recherche des logs d'audit selon différents critères avec pagination.

- **URL**: `/api/audit-logs`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `INFORMATICIEN`
- **Paramètres**:
  - `username` (query, optionnel): Nom d'utilisateur à filtrer
  - `action` (query, optionnel): Type d'action (CREATE, UPDATE, DELETE, etc.)
  - `entityType` (query, optionnel): Type d'entité (Contribuable, Propriete, etc.)
  - `startDate` (query, optionnel): Date de début au format ISO 8601 (ex: 2024-01-01T00:00:00)
  - `endDate` (query, optionnel): Date de fin au format ISO 8601 (ex: 2024-12-31T23:59:59)
  - `page` (query, optionnel): Numéro de page (défaut: 0)
  - `size` (query, optionnel): Taille de page (défaut: 10)
  - `sort` (query, optionnel): Critère de tri (ex: timestamp,desc)

#### Exemples de requêtes

**Tous les logs (paginés)**
```
GET /api/audit-logs?page=0&size=10
```

**Filtrer par utilisateur**
```
GET /api/audit-logs?username=agent1&page=0&size=10
```

**Filtrer par action et entité**
```
GET /api/audit-logs?action=CREATE&entityType=Contribuable&page=0&size=10
```

**Filtrer par période**
```
GET /api/audit-logs?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59&page=0&size=10
```

**Recherche combinée avec tri**
```
GET /api/audit-logs?username=agent1&action=CREATE&entityType=Contribuable&startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59&page=0&size=20&sort=timestamp,desc
```

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "logs": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "username": "agent1",
        "action": "CREATE",
        "entityType": "Contribuable",
        "entityId": "550e8400-e29b-41d4-a716-446655440001",
        "details": "Création du contribuable KABILA JOSEPH LAURENT",
        "timestamp": "2024-10-27T12:00:00",
        "ipAddress": "192.168.1.100"
      },
      {
        "id": "550e8400-e29b-41d4-a716-446655440002",
        "username": "agent1",
        "action": "UPDATE",
        "entityType": "Propriete",
        "entityId": "550e8400-e29b-41d4-a716-446655440010",
        "details": "Mise à jour de la propriété - Avenue de la Liberté",
        "timestamp": "2024-10-27T12:15:00",
        "ipAddress": "192.168.1.100"
      },
      {
        "id": "550e8400-e29b-41d4-a716-446655440003",
        "username": "agent2",
        "action": "DELETE",
        "entityType": "Taxation",
        "entityId": "550e8400-e29b-41d4-a716-446655440020",
        "details": "Suppression de la taxation ID: 550e8400-e29b-41d4-a716-446655440020",
        "timestamp": "2024-10-27T12:30:00",
        "ipAddress": "192.168.1.101"
      }
    ],
    "currentPage": 0,
    "totalItems": 1250,
    "totalPages": 125
  }
}
```

---

## Structure de l'entité AuditLog

| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant unique |
| username | String | Nom d'utilisateur |
| action | String | Type d'action |
| entityType | String | Type d'entité |
| entityId | String | ID de l'entité |
| details | String | Détails de l'action |
| timestamp | DateTime | Date et heure |
| ipAddress | String | Adresse IP |

---

## Actions tracées

### Actions CRUD
- **CREATE**: Création d'une entité
- **READ**: Consultation d'une entité
- **UPDATE**: Modification d'une entité
- **DELETE**: Suppression d'une entité

### Actions métier
- **LOGIN**: Connexion utilisateur
- **LOGOUT**: Déconnexion utilisateur
- **VALIDATE**: Validation (déclaration, paiement, etc.)
- **REJECT**: Rejet
- **APPROVE**: Approbation
- **ASSIGN**: Attribution (plaque, vignette)
- **GENERATE**: Génération (certificat, rapport)
- **EXPORT**: Exportation de données

---

## Entités tracées

Toutes les entités principales du système sont tracées :
- `Contribuable`
- `Propriete`
- `Vehicule`
- `Declaration`
- `Taxation`
- `Paiement`
- `Utilisateur`
- `Plaque`
- `Vignette`
- `Certificat`
- `Apurement`
- `Relance`
- `DossierRecouvrement`

---

## Règles métier

### Traçabilité complète
- Toutes les actions sensibles sont enregistrées
- Impossible de modifier ou supprimer un log
- Conservation permanente des logs

### Informations capturées
- **Qui**: Utilisateur ayant effectué l'action
- **Quoi**: Type d'action et entité concernée
- **Quand**: Date et heure précises
- **Où**: Adresse IP de l'utilisateur
- **Détails**: Description de l'action

### Accès restreint
- Seuls les administrateurs (ADMIN) et informaticiens (INFORMATICIEN) peuvent consulter les logs
- Logs filtrables par utilisateur, action, entité, période
- Pagination automatique pour optimiser les performances
- Tri par timestamp décroissant recommandé pour voir les actions récentes

### Conformité
- Respect des normes de sécurité
- Traçabilité pour audits externes
- Détection des activités suspectes

---

## Cas d'usage

### 1. Audit de sécurité
- Identifier les accès non autorisés
- Détecter les tentatives de fraude
- Analyser les patterns d'utilisation

### 2. Résolution de problèmes
- Retracer l'historique d'une entité
- Identifier la source d'une erreur
- Comprendre les modifications effectuées

### 3. Conformité réglementaire
- Prouver la conformité aux normes
- Fournir des preuves pour audits
- Documenter les processus

### 4. Analyse d'activité
- Statistiques d'utilisation
- Identification des utilisateurs actifs
- Tendances d'utilisation du système

---

## Codes d'erreur

| Code | Description |
|------|-------------|
| AUDIT_LOGS_FETCH_ERROR | Erreur lors de la récupération des logs |
| INVALID_DATE_RANGE | Période de dates invalide |
| UNAUTHORIZED_ACCESS | Accès non autorisé (403 Forbidden) |

---

## Notes techniques

### Format des dates
- Les paramètres `startDate` et `endDate` doivent être au format ISO 8601
- Exemple: `2024-10-27T12:00:00`
- Le fuseau horaire est celui du serveur

### Pagination
- La pagination utilise Spring Data Pageable
- Les pages commencent à 0
- Taille par défaut: 10 éléments
- Tri par défaut: timestamp décroissant

### Performance
- Les requêtes sont optimisées avec des index sur username, action, entityType et timestamp
- Utiliser les filtres pour réduire le volume de données
- Éviter de charger toutes les pages en une seule fois

### Sécurité
- Authentification JWT requise
- Rôles ADMIN ou INFORMATICIEN obligatoires
- Les logs ne peuvent pas être modifiés ou supprimés via l'API
