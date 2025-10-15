# API des Documents de Recouvrement

## 1. Récupération des Documents

### Endpoint
```http
GET /api/documents-recouvrement
```

### Paramètres de requête
```json
{
  "type": "AMR",                    // optionnel
  "statut": "NOTIFIE",             // optionnel
  "contribuableId": "UUID",         // optionnel
  "dossierRecouvrementId": "UUID",  // optionnel
  "dateDebut": "2025-01-01",       // optionnel
  "dateFin": "2025-12-31",         // optionnel
  "page": 0,                       // défaut: 0
  "size": 10,                      // défaut: 10
  "sortBy": "dateGeneration",      // défaut: dateGeneration
  "sortDir": "desc"                // défaut: desc
}
```

### Réponse
```json
{
  "success": true,
  "data": {
    "totalItems": 100,
    "totalPages": 10,
    "currentPage": 0,
    "documents": [
      {
        "id": "UUID",
        "type": "AMR",
        "statut": "NOTIFIE",
        "dateGeneration": "2025-01-01",
        "dateNotification": "2025-01-02",
        "dateEcheance": "2025-01-16",
        "reference": "AMR-001",
        "montantPrincipal": 1000000.0,
        "montantPenalites": 20000.0,
        "montantTotal": 1020000.0,
        "contribuable": {
          "id": "UUID",
          "nom": "SARL EXEMPLE",
          "numeroIdentificationContribuable": "A001"
        },
        "agentGenerateur": {
          "id": "UUID",
          "nom": "Jean DUPONT",
          "matricule": "AG001"
        }
      }
    ]
  }
}
```

## 2. Création d'un AMR

### Endpoint
```http
POST /api/documents-recouvrement/amr
```

### Payload
```json
{
  "dossierRecouvrementId": "UUID",
  "contribuableId": "UUID",
  "agentId": "UUID",
  "montantPrincipal": 1000000.0,
  "montantPenalites": 20000.0,
  "typeRedressement": "REDRESSEMENT_SIMPLE",
  "baseImposable": "Revenus locatifs 2024",
  "declarationId": "UUID"  // optionnel
}
```

### Réponse
```json
{
  "success": true,
  "data": {
    "document": {
      "id": "UUID",
      "type": "AMR",
      "statut": "GENERE",
      "dateGeneration": "2025-01-01",
      "dateEcheance": "2025-01-16",
      "reference": "AMR-001",
      "montantPrincipal": 1000000.0,
      "montantPenalites": 20000.0,
      "montantTotal": 1020000.0,
      "baseImposable": "Revenus locatifs 2024",
      "typeRedressement": "REDRESSEMENT_SIMPLE"
    },
    "message": "Avis de Mise en Recouvrement créé avec succès"
  }
}
```

## 3. Création d'une MED

### Endpoint
```http
POST /api/documents-recouvrement/med
```

### Payload
```json
{
  "dossierRecouvrementId": "UUID",
  "contribuableId": "UUID",
  "agentId": "UUID",
  "montantPrincipal": 500000.0,
  "montantPenalites": 10000.0,
  "paiementInsuffisant": true,
  "montantPaye": 200000.0,
  "declarationId": "UUID"  // optionnel
}
```

### Réponse
```json
{
  "success": true,
  "data": {
    "document": {
      "id": "UUID",
      "type": "MED",
      "statut": "GENERE",
      "dateGeneration": "2025-01-01",
      "dateEcheance": "2025-01-09",
      "reference": "MED-001",
      "montantPrincipal": 500000.0,
      "montantPenalites": 10000.0,
      "montantTotal": 510000.0,
      "paiementInsuffisant": true,
      "montantPaye": 200000.0
    },
    "message": "Mise En Demeure créée avec succès"
  }
}
```
