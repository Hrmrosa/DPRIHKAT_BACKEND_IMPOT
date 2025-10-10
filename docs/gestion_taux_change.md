# Gestion des Taux de Change

## Endpoints

### 1. Créer un taux de change
**POST /api/taux-change**

**Payload d'entrée**:
```json
{
  "taux": 2500.0,
  "deviseSource": "USD", 
  "deviseDestination": "CDF"
}
```

**Réponse réussie**:
```json
{
  "success": true,
  "data": {
    "message": "Taux créé avec succès",
    "tauxChange": {
      "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "dateEffective": "2025-10-07T10:09:01+02:00",
      "taux": 2500.0,
      "deviseSource": "USD",
      "deviseDestination": "CDF",
      "actif": true
    }
  }
}
```

### 2. Obtenir les taux actifs  
**GET /api/taux-change**

**Réponse**:
```json
{
  "success": true,
  "data": {
    "tauxChanges": [
      {
        "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
        "dateEffective": "2025-10-07T10:09:01+02:00",
        "taux": 2500.0,
        "deviseSource": "USD",
        "deviseDestination": "CDF",
        "actif": true
      }
    ]
  }
}
```

### 3. Historique complet
**GET /api/taux-change/historique**

**Réponse**:
```json
{
  "success": true,
  "data": {
    "tauxChanges": [
      {
        "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
        "dateEffective": "2025-10-07T10:09:01+02:00",
        "taux": 2500.0,
        "deviseSource": "USD",
        "deviseDestination": "CDF",
        "actif": true
      },
      {
        "id": "3fa85f64-5717-4562-b3fc-2c963f66afa7",
        "dateEffective": "2025-10-06T10:09:01+02:00",
        "taux": 2450.0,
        "deviseSource": "USD",
        "deviseDestination": "CDF",
        "actif": false
      }
    ]
  }
}
```

### 4. Conversion de montant
**GET /api/taux-change/convertir?montant=100&deviseSource=USD&deviseDestination=CDF**

**Réponse**:
```json
{
  "success": true,
  "data": {
    "montantOriginal": 100,
    "deviseSource": "USD",
    "montantConverti": 250000,
    "deviseDestination": "CDF",
    "tauxApplique": 2500.0
  }
}
```
