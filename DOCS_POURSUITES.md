# Documentation des Poursuites

## Endpoint GET /api/poursuites

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
    "poursuites": [
      {
        "id": "...",
        "taxationId": "...",
        "statut": "...",
        "dateCreation": "..."
        // autres champs
      }
    ]
  },
  "success": true,
  "error": null
}
```

## Endpoint POST /api/poursuites/create/{taxationId}

### Payload complet
```json
{
  "motif": "NON_PAIEMENT",
  "montantReclame": 1500.75,
  "dateEcheance": "2025-12-31",
  "documents": [
    {
      "type": "AVIS_IMPOSITION",
      "reference": "AVIS-2023-456"
    }
  ],
  "canal": "COURRIER|HUISSIER"
}
```

### Champs obligatoires
- `motif` : Raison de la poursuite
- `montantReclame` : Montant total réclamé

### Validation
- Montant doit être ≥ montant dû
- Au moins un document requis
- Date d'échéance future

### Statuts possibles
- `INITIEE`
- `EN_COURS`
- `CLOTUREE`

### Réponse
```json
{
  "data": {
    "poursuite": {
      "id": "...",
      "statut": "INITIEE"
      // autres champs
    }
  },
  "success": true,
  "error": null
}
```
