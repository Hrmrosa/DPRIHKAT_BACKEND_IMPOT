# Documentation des Apurements

## Endpoint GET /api/apurements

### Paramètres
- `page` (optionnel, défaut=0) : Numéro de page (commence à 0)
- `size` (optionnel, défaut=10) : Nombre d'éléments par page
- `statut` (optionnel) : Filtre par statut
- `type` (optionnel) : Filtre par type d'apurement

### Structure de réponse
```json
{
  "data": {
    "totalItems": 100,
    "totalPages": 10,
    "currentPage": 0,
    "apurements": [
      {
        "id": "...",
        "declarationId": "...",
        "statut": "...",
        "type": "..."
        // autres champs
      }
    ]
  },
  "success": true,
  "error": null
}
```

## Endpoint POST /api/apurements/create/{declarationId}

### Payload complet
```json
{
  "type": "TOTALE|PARTIELLE",
  "commentaire": "Détails sur l'apurement",
  "piecesJustificatives": [
    {
      "typeDocument": "FACTURE|QUITTANCE",
      "reference": "REF-123"
    }
  ],
  "montantApure": 1000.50
}
```

### Champs obligatoires
- `type` : Type d'apurement
- `montantApure` : Montant validé

### Validation
- Le montant doit être ≤ montant initial
- Au moins une pièce justificative requise

### Réponse
```json
{
  "data": {
    "apurement": {
      "id": "...",
      "statut": "EN_COURS"
      // autres champs
    }
  },
  "success": true,
  "error": null
}
```
