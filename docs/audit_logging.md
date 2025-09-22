# Journalisation des Actions Utilisateurs (Audit Logging)

## Fonctionnalité

Le système enregistre automatiquement toutes les actions importantes des utilisateurs dans une table dédiée (`audit_log`). Cela permet de :

- Suivre l'activité des utilisateurs
- Détecter les comportements anormaux
- Respecter les exigences de conformité

## Actions Journalisées

1. **Authentification** :
   - Connexion (LOGIN)
   - Déconnexion (LOGOUT)

2. **Opérations CRUD** (automatiquement interceptées) :
   - Création (CREATE)
   - Lecture (READ)
   - Mise à jour (UPDATE)
   - Suppression (DELETE)

3. **Actions spécifiques** (journalisées manuellement)

## Structure des Logs

Chaque entrée contient :
- Utilisateur concerné
- Type d'action
- Entité affectée
- Date/heure
- Adresse IP
- Détails supplémentaires

## Gestion des Dates

**Nouveauté** : Le système gère maintenant automatiquement les dates limites pour éviter les erreurs :

- Si aucune date de début n'est spécifiée, la date par défaut est le 1er janvier 1970
- Si aucune date de fin n'est spécifiée, la date par défaut est la date actuelle + 10 ans
- Les dates invalides ou hors plage sont automatiquement corrigées

## API de Consultation

Endpoint : `GET /api/audit-logs`

**Paramètres** :
- `username` : Filtrer par utilisateur (optionnel)
- `action` : Filtrer par type d'action (optionnel)
- `entityType` : Filtrer par type d'entité (optionnel)
- `startDate` : Date de début au format ISO 8601 (optionnel)
  - Exemple : `2025-09-01T00:00:00`
  - Si non spécifié, utilise une date par défaut raisonnable
- `endDate` : Date de fin au format ISO 8601 (optionnel)
  - Si non spécifié, utilise une date future raisonnable
- `page` : Numéro de page (0-based, optionnel, défaut=0)
- `size` : Nombre d'éléments par page (optionnel, défaut=20)

**Paramètres de pagination** :
- `page` : Numéro de page (0-based, optionnel, défaut=0)
- `size` : Nombre d'éléments par page (optionnel, défaut=20)

**Permissions** :
- Seuls les utilisateurs avec rôle `DIRECTEUR` peuvent accéder aux logs

**Nouveautés** :
- Gestion robuste des paramètres optionnels
- Meilleure compatibilité avec PostgreSQL
- Performances optimisées pour les grandes quantités de données

**Exemple complet** :
```bash
curl -X GET "http://localhost:8080/api/audit-logs?action=CREATE&entityType=APUREMENT&startDate=2025-09-01T00:00:00&endDate=2025-09-30T23:59:59&page=0&size=10" \
-H "Authorization: Bearer [JWT_TOKEN]"
```

**Exemple de requête avec pagination** :
```bash
curl -X GET "http://localhost:8080/api/audit-logs?page=1&size=10" \
-H "Authorization: Bearer [JWT_TOKEN]"
```

**Exemple de réponse** :
```json
{
  "success": true,
  "data": {
    "logs": [
      {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "username": "admin",
        "action": "CREATE",
        "entityType": "APUREMENT",
        "timestamp": "2025-09-15T14:30:22"
      }
    ],
    "currentPage": 0,
    "totalItems": 42,
    "totalPages": 3
  }
}
```

**Best Practices** :
- Pour les grandes collections, limiter à 50 éléments par page maximum
- Toujours vérifier `totalPages` pour une navigation correcte
- Utiliser `currentPage` et `totalItems` pour afficher la pagination côté client

## Rétention des Données

- Les logs sont conservés pendant 90 jours
- Une tâche planifiée purge automatiquement les anciens logs chaque nuit
