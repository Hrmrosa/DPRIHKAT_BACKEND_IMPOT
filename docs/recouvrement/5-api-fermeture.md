# API de Fermeture d'Établissement

## 1. Création d'une Fermeture d'Établissement

### Endpoint
```http
POST /api/documents-recouvrement/fermeture
```

### Payload
```json
{
  "dossierRecouvrementId": "UUID",
  "contribuableId": "UUID",
  "agentId": "UUID",
  "agentOPJId": "UUID",
  "montantPrincipal": 1000000.0,
  "montantPenalites": 20000.0,
  "adresseEtablissement": "123 Avenue du Commerce, Kinshasa",
  "motifFermeture": "Non-paiement des impôts"
}
```

### Réponse
```json
{
  "success": true,
  "data": {
    "document": {
      "id": "UUID",
      "type": "FERMETURE_ETABLISSEMENT",
      "statut": "GENERE",
      "dateGeneration": "2025-01-01",
      "reference": "FE-001",
      "montantPrincipal": 1000000.0,
      "montantPenalites": 20000.0,
      "montantTotal": 1020000.0,
      "montantAmende": 1000000.0,
      "adresseEtablissement": "123 Avenue du Commerce, Kinshasa",
      "motifFermeture": "Non-paiement des impôts",
      "agentOPJ": {
        "id": "UUID",
        "nom": "Pierre OPJ",
        "matricule": "OPJ001"
      }
    },
    "message": "Fermeture d'établissement créée avec succès"
  }
}
```

## 2. Mise à jour du statut d'un document

### Endpoint
```http
PATCH /api/documents-recouvrement/{id}/statut
```

### Payload
```json
{
  "statut": "NOTIFIE"
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
      "statut": "NOTIFIE",
      "dateGeneration": "2025-01-01",
      "dateNotification": "2025-01-02",
      "dateEcheance": "2025-01-16",
      "reference": "AMR-001"
    },
    "message": "Statut du document mis à jour avec succès"
  }
}
```
