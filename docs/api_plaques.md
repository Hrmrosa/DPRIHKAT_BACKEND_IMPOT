# API Plaques d'Immatriculation

## Description
API pour la gestion des plaques d'immatriculation des véhicules. Permet de :
- Gérer le cycle de vie des plaques (création, modification, suppression)
- Valider le format des plaques
- Associer/dissocier des plaques aux véhicules
- Importer/exporter des listes de plaques

## Structure de la plaque
```json
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "numplaque": "ABC123",
  "numeroSerie": "SERIE123",
  "disponible": true,
  "dateCreation": "2025-09-23T00:00:00Z",
  "vehicule": {
    "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "marque": "Toyota",
    "modele": "Corolla"
  },
  "statut": "DISPONIBLE"
}
```

## Structure complète de la plaque
```json
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "numplaque": "ABC123",
  "numeroSerie": "SERIE123",
  "disponible": false,
  "dateCreation": "2025-09-25T00:00:00Z",
  "vehicule": {
    "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "marque": "Toyota",
    "modele": "Corolla"
  },
  "statut": "ATTRIBUE"
}
```

## Structure du véhicule
```json
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "marque": "Toyota",
  "modele": "Corolla"
}
```

## Structure complète du véhicule
```json
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "marque": "Toyota",
  "modele": "Corolla",
  "annee": 2020,
  "immatriculation": "ABC123",
  "numeroChassis": "XYZ789",
  "genre": "Particulier",
  "categorie": "VP",
  "puissanceFiscale": 5.0,
  "proprietaire": {
    "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "nom": "Dupont",
    "prenom": "Jean"
  }
}
```

## Endpoints

### Créer une plaque
`POST /api/plaques`

**Paramètres**:
```json
{
  "numplaque": "ABC123",
  "numeroSerie": "SERIE123",
  "disponible": true,
  "statut": "DISPONIBLE"
}
```

**Réponses**:
- 200: Plaque créée
- 400: Erreur de validation
- 403: Non autorisé

---

### Récupérer toutes les plaques
`GET /api/plaques`

**Paramètres query**:
- page (int, défaut: 0)
- size (int, défaut: 10)
- disponible (booléen, optionnel)

**Réponses**:
- 200: Liste paginée des plaques

---

### Récupérer une plaque
`GET /api/plaques/{id}`

**Réponses**:
- 200: Détails de la plaque
- 404: Plaque non trouvée

---

### Récupérer les informations complètes d'une plaque
`GET /api/plaques/{id}/vehicules`

**Exemple de réponse**:
```json
{
  "success": true,
  "data": {
    "vehicule": {
      "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "marque": "Toyota",
      "modele": "Corolla",
      "annee": 2020,
      "immatriculation": "ABC123",
      "numeroChassis": "XYZ789",
      "genre": "Particulier",
      "categorie": "VP",
      "puissanceFiscale": 5.0
    },
    "contribuable": {
      "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "nom": "Dupont",
      "prenom": "Jean",
      "numeroIdentification": "ID123456",
      "adresse": "123 Rue Example",
      "telephone": "+123456789"
    },
    "plaque": "ABC123"
  },
  "meta": {
    "version": "1.0",
    "timestamp": "2025-09-25T10:15:00Z"
  }
}
```

**Réponses**:
- 200: Détails complets du véhicule et du contribuable
- 404: Plaque non trouvée
- 400: Erreur de traitement

---

### Mettre à jour une plaque
`PUT /api/plaques/{id}`

**Paramètres**:
```json
{
  "numplaque": "XYZ789",
  "numeroSerie": "SERIE456",
  "disponible": false,
  "statut": "ATTRIBUE"
}
```

**Réponses**:
- 200: Plaque mise à jour
- 404: Plaque non trouvée

---

### Valider une plaque
`GET /api/plaques/validate/{numplaque}`

**Réponses**:
- 200: `{ "valid": true }`
- 400: Format invalide

---

### Vérifier l'existence d'une plaque
`GET /api/plaques/exists/{numplaque}`

**Réponses**:
- 200: `{ "exists": true }`

---

### Plaques disponibles
`GET /api/plaques/available`

**Réponses**:
- 200: Liste des plaques disponibles

---

### Plaques attribuées
`GET /api/plaques/assigned`

**Réponses**:
- 200: Liste des plaques attribuées

---

### Vérifier le stock
`GET /api/plaques/stock`

**Réponses**:
- 200: `{ "available": true, "message": "Plaques disponibles" }`

---

### Importer des plaques
`POST /api/plaques/import`

**Paramètres**:
- file (fichier CSV/Excel)

**Format attendu**:
```
numplaque,numeroSerie,disponible,statut
ABC123,SERIE1,true,DISPONIBLE
XYZ789,SERIE2,false,ATTRIBUE
```

**Réponses**:
- 200: Statistiques d'import
- 400: Erreur d'import

---

### Exporter des plaques
`GET /api/plaques/export`

**Réponses**:
- 200: Fichier Excel
- 400: Erreur d'export

## Rôles requis
- ADMIN: Toutes opérations
- AGENT_DE_PLAQUES: Opérations spécialisées
- TAXATEUR: Création/assignation
- AUTRES: Consultation seulement

## Codes d'erreur
- PLAQUE_NOT_FOUND: Plaque introuvable
- PLAQUE_VALIDATION_ERROR: Format de plaque invalide
- PLAQUE_ALREADY_EXISTS: La plaque existe déjà
- PLAQUE_IMPORT_ERROR: Erreur lors de l'import
