a# API des Mesures de Poursuite

## 1. Création d'une Contrainte Fiscale

### Endpoint
```http
POST /api/documents-recouvrement/contrainte
```

### Payload
```json
{
  "dossierRecouvrementId": "UUID",
  "contribuableId": "UUID",
  "agentId": "UUID",
  "receveurId": "UUID",
  "montantPrincipal": 1000000.0,
  "montantPenalites": 20000.0
}
```

### Réponse
```json
{
  "success": true,
  "data": {
    "document": {
      "id": "UUID",
      "type": "CONTRAINTE",
      "statut": "GENERE",
      "dateGeneration": "2025-01-01",
      "reference": "CONT-001",
      "montantPrincipal": 1000000.0,
      "montantPenalites": 20000.0,
      "montantTotal": 1020000.0,
      "receveur": {
        "id": "UUID",
        "nom": "Paul RECEVEUR",
        "matricule": "REC001"
      }
    },
    "message": "Contrainte fiscale créée avec succès"
  }
}
```

## 2. Création d'un Commandement de Payer

### Endpoint
```http
POST /api/documents-recouvrement/commandement
```

### Payload
```json
{
  "dossierRecouvrementId": "UUID",
  "contribuableId": "UUID",
  "agentId": "UUID",
  "huissierId": "UUID",
  "montantPrincipal": 1000000.0,
  "montantPenalites": 20000.0
}
```

### Réponse
```json
{
  "success": true,
  "data": {
    "document": {
      "id": "UUID",
      "type": "COMMANDEMENT",
      "statut": "GENERE",
      "dateGeneration": "2025-01-01",
      "dateEcheance": "2025-01-09",
      "reference": "CMD-001",
      "montantPrincipal": 1000000.0,
      "montantPenalites": 20000.0,
      "montantTotal": 1020000.0,
      "fraisCommandement": 30600.0,
      "huissier": {
        "id": "UUID",
        "nom": "Marc HUISSIER",
        "matricule": "HUI001"
      }
    },
    "message": "Commandement de payer créé avec succès"
  }
}
```

## 3. Création d'un ATD

### Endpoint
```http
POST /api/documents-recouvrement/atd
```

### Payload
```json
{
  "dossierRecouvrementId": "UUID",
  "contribuableId": "UUID",
  "agentId": "UUID",
  "montantPrincipal": 1000000.0,
  "montantPenalites": 20000.0,
  "nomTiersDetenteur": "Banque XYZ",
  "adresseTiersDetenteur": "123 Avenue Lumumba, Kinshasa",
  "qualiteTiersDetenteur": "Banque"
}
```

### Réponse
```json
{
  "success": true,
  "data": {
    "document": {
      "id": "UUID",
      "type": "ATD",
      "statut": "GENERE",
      "dateGeneration": "2025-01-01",
      "dateEcheance": "2025-01-09",
      "reference": "ATD-001",
      "montantPrincipal": 1000000.0,
      "montantPenalites": 20000.0,
      "montantTotal": 1020000.0,
      "nomTiersDetenteur": "Banque XYZ",
      "adresseTiersDetenteur": "123 Avenue Lumumba, Kinshasa",
      "qualiteTiersDetenteur": "Banque"
    },
    "message": "Avis à Tiers Détenteur créé avec succès"
  }
}
```
