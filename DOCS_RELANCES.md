# Documentation des Relances

## Endpoint GET /api/relances

### Paramètres
- `page` (optionnel, défaut=0) : Numéro de page (commence à 0)
- `size` (optionnel, défaut=10) : Nombre d'éléments par page
- `statut` (optionnel) : Filtre par statut

### Structure de réponse
```json
{
  "data": {
    "totalItems": 100,
    "totalPages": 10,
    "currentPage": 0,
    "relances": [
      {
        "id": "...",
        "taxationId": "...",
        "dateRelance": "...",
        "statut": "..."
        // autres champs
      }
    ]
  },
  "success": true,
  "error": null
}
```

## Endpoint POST /api/relances/create/{taxationId}

### Payload
```json
{
  "typeRelance": "...",
  "commentaire": "..."
}
```

### Réponse
```json
{
  "data": {
    "relance": {
      "id": "...",
      "statut": "ENVOYEE"
      // autres champs
    }
  },
  "success": true,
  "error": null
}
```
