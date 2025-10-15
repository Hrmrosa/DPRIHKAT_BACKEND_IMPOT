# API des Saisies

## 1. Création d'une Saisie Mobilière

### Endpoint
```http
POST /api/documents-recouvrement/saisie-mobiliere
```

### Payload
```json
{
  "dossierRecouvrementId": "UUID",
  "contribuableId": "UUID",
  "agentId": "UUID",
  "huissierId": "UUID",
  "montantPrincipal": 1000000.0,
  "montantPenalites": 20000.0,
  "descriptionBiensSaisis": "1 véhicule Toyota Land Cruiser, 2 ordinateurs...",
  "valeurEstimeeBiens": 2000000.0,
  "typeBiensMobiliers": "VEHICULES_ET_MATERIEL",
  "lieuStockage": "Parking sécurisé DGI",
  "gardien": "Jean GARDIEN"
}
```

### Réponse
```json
{
  "success": true,
  "data": {
    "document": {
      "id": "UUID",
      "type": "SAISIE_MOBILIERE",
      "statut": "GENERE",
      "dateGeneration": "2025-01-01",
      "reference": "SM-001",
      "montantPrincipal": 1000000.0,
      "montantPenalites": 20000.0,
      "montantTotal": 1020000.0,
      "fraisSaisie": 51000.0,
      "descriptionBiensSaisis": "1 véhicule Toyota Land Cruiser, 2 ordinateurs...",
      "valeurEstimeeBiens": 2000000.0,
      "typeBiensMobiliers": "VEHICULES_ET_MATERIEL",
      "lieuStockage": "Parking sécurisé DGI",
      "gardien": "Jean GARDIEN"
    },
    "message": "Saisie mobilière créée avec succès"
  }
}
```

## 2. Création d'une Saisie Immobilière

### Endpoint
```http
POST /api/documents-recouvrement/saisie-immobiliere
```

### Payload
```json
{
  "dossierRecouvrementId": "UUID",
  "contribuableId": "UUID",
  "agentId": "UUID",
  "huissierId": "UUID",
  "montantPrincipal": 1000000.0,
  "montantPenalites": 20000.0,
  "descriptionBiensSaisis": "Immeuble R+2 usage commercial",
  "valeurEstimeeBiens": 50000000.0,
  "referencesCadastrales": "CAD-001-2025",
  "adresseBien": "123 Avenue du Commerce, Kinshasa",
  "superficie": 500.0,
  "titresPropriete": "Vol 123 Folio 45"
}
```

### Réponse
```json
{
  "success": true,
  "data": {
    "document": {
      "id": "UUID",
      "type": "SAISIE_IMMOBILIERE",
      "statut": "GENERE",
      "dateGeneration": "2025-01-01",
      "reference": "SI-001",
      "montantPrincipal": 1000000.0,
      "montantPenalites": 20000.0,
      "montantTotal": 1020000.0,
      "fraisSaisie": 51000.0,
      "descriptionBiensSaisis": "Immeuble R+2 usage commercial",
      "valeurEstimeeBiens": 50000000.0,
      "referencesCadastrales": "CAD-001-2025",
      "adresseBien": "123 Avenue du Commerce, Kinshasa",
      "superficie": 500.0,
      "titresPropriete": "Vol 123 Folio 45"
    },
    "message": "Saisie immobilière créée avec succès"
  }
}
```
