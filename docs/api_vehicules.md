# Documentation API - Gestion des Véhicules

Cette documentation détaille les endpoints disponibles pour la gestion des véhicules dans l'API DPRIHKAT.

---

## Vue d'ensemble

Les véhicules représentent les biens mobiliers appartenant aux contribuables et soumis à l'impôt sur les revenus locatifs des véhicules (IRV) et à la vignette.

### Base URL
```
/api/vehicules
```

---

## Endpoints

### 1. Récupérer toutes les marques de véhicules

Récupère la liste de toutes les marques de véhicules disponibles.

- **URL**: `/api/vehicules/marques`
- **Méthode**: `GET`
- **Rôles autorisés**: Tous
- **Paramètres**: Aucun

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "marques": [
      "Toyota",
      "Mercedes-Benz",
      "Nissan",
      "Honda",
      "Ford",
      "Chevrolet"
    ]
  }
}
```

---

### 2. Récupérer les modèles par marque

Récupère la liste des modèles disponibles pour une marque spécifique.

- **URL**: `/api/vehicules/marques/{marque}/modeles`
- **Méthode**: `GET`
- **Rôles autorisés**: Tous
- **Paramètres**:
  - `marque` (path): Nom de la marque

#### Exemple de requête

```
GET /api/vehicules/marques/Toyota/modeles
```

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "marque": "Toyota",
    "modeles": [
      "Corolla",
      "Camry",
      "RAV4",
      "Land Cruiser",
      "Hilux"
    ]
  }
}
```

---

### 3. Récupérer tous les véhicules

Récupère la liste de tous les véhicules enregistrés.

- **URL**: `/api/vehicules`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`, `CONTROLLEUR`, `CHEF_DE_BUREAU`, `TAXATEUR`
- **Paramètres**: Aucun

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "vehicules": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "immatriculation": "CD-123-ABC",
        "marque": "Toyota",
        "modele": "Corolla",
        "annee": 2020,
        "typeVehicule": "VOITURE",
        "puissanceFiscale": 8,
        "actif": true,
        "proprietaire": {
          "id": "550e8400-e29b-41d4-a716-446655440001",
          "nom": "KABILA",
          "numeroIdentificationContribuable": "NIF-123456789"
        }
      }
    ]
  }
}
```

---

### 4. Récupérer un véhicule par ID

Récupère les détails d'un véhicule spécifique.

- **URL**: `/api/vehicules/{id}`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`, `CONTROLLEUR`, `CHEF_DE_BUREAU`, `TAXATEUR`, `CONTRIBUABLE`
- **Paramètres**:
  - `id` (path): UUID du véhicule

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "vehicule": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "immatriculation": "CD-123-ABC",
      "marque": "Toyota",
      "modele": "Corolla",
      "annee": 2020,
      "typeVehicule": "VOITURE",
      "puissanceFiscale": 8,
      "numeroMoteur": "1NZ1234567",
      "numeroChassis": "JTDBT123X12345678",
      "couleur": "Blanc",
      "actif": true,
      "proprietaire": {
        "id": "550e8400-e29b-41d4-a716-446655440001",
        "nom": "KABILA JOSEPH LAURENT",
        "type": "PERSONNE_PHYSIQUE",
        "numeroIdentificationContribuable": "NIF-123456789"
      },
      "plaques": [
        {
          "id": "550e8400-e29b-41d4-a716-446655440010",
          "numero": "CD-123-ABC",
          "disponible": false,
          "dateAttribution": "2023-01-15"
        }
      ]
    }
  }
}
```

---

### 5. Créer un nouveau véhicule

Crée un nouveau véhicule dans le système.

- **URL**: `/api/vehicules`
- **Méthode**: `POST`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`, `TAXATEUR`

#### Corps de la requête

```json
{
  "immatriculation": "CD-123-ABC",
  "marque": "Toyota",
  "modele": "Corolla",
  "annee": 2020,
  "typeVehicule": "VOITURE",
  "puissanceFiscale": 8,
  "numeroMoteur": "1NZ1234567",
  "numeroChassis": "JTDBT123X12345678",
  "couleur": "Blanc",
  "proprietaireId": "550e8400-e29b-41d4-a716-446655440001"
}
```

#### Champs obligatoires
- `immatriculation`: Numéro d'immatriculation unique
- `marque`: Marque du véhicule
- `modele`: Modèle du véhicule
- `annee`: Année de fabrication
- `typeVehicule`: Type de véhicule
- `puissanceFiscale`: Puissance fiscale (CV)
- `proprietaireId`: UUID du propriétaire (contribuable)

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "vehicule": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "immatriculation": "CD-123-ABC",
      "marque": "Toyota",
      "modele": "Corolla",
      "actif": true
    }
  }
}
```

---

### 6. Mettre à jour un véhicule

Met à jour les informations d'un véhicule existant.

- **URL**: `/api/vehicules/{id}`
- **Méthode**: `PUT`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`, `TAXATEUR`

---

### 7. Récupérer mes véhicules (contribuable)

Permet à un contribuable de récupérer ses propres véhicules.

- **URL**: `/api/vehicules/mine`
- **Méthode**: `GET`
- **Rôles autorisés**: `CONTRIBUABLE`
- **Paramètres**: Aucun (utilise l'authentification)

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "vehicules": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "immatriculation": "CD-123-ABC",
        "marque": "Toyota",
        "modele": "Corolla",
        "annee": 2020,
        "actif": true
      }
    ]
  }
}
```

---

### 8. Supprimer un véhicule

Suppression logique d'un véhicule.

- **URL**: `/api/vehicules/{id}`
- **Méthode**: `DELETE`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`

---

## Structure de l'entité Véhicule

| Champ | Type | Description | Obligatoire |
|-------|------|-------------|-------------|
| id | UUID | Identifiant unique | Auto-généré |
| immatriculation | String | Numéro d'immatriculation | Oui |
| marque | String | Marque du véhicule | Oui |
| modele | String | Modèle du véhicule | Oui |
| annee | Integer | Année de fabrication | Oui |
| typeVehicule | Enum | Type de véhicule | Oui |
| puissanceFiscale | Integer | Puissance fiscale (CV) | Oui |
| numeroMoteur | String | Numéro du moteur | Non |
| numeroChassis | String | Numéro de châssis | Non |
| couleur | String | Couleur du véhicule | Non |
| actif | Boolean | Véhicule actif | Auto |
| proprietaire | Contribuable | Propriétaire | Oui |

---

## Énumérations

### TypeVehicule
- `VOITURE`: Voiture particulière
- `CAMION`: Camion
- `MOTO`: Motocyclette
- `BUS`: Autobus
- `CAMIONNETTE`: Camionnette

---

## Relations

Un véhicule possède :
- **Plaques d'immatriculation**: Historique des plaques attribuées
- **Vignettes**: Historique des vignettes payées
- **Taxations**: Historique des taxations (IRV)

---

## Règles métier

### Immatriculation
- Le numéro d'immatriculation doit être unique
- Format: CD-XXX-ABC (pour la RDC)

### Puissance fiscale
- Utilisée pour le calcul de l'IRV et de la vignette
- Exprimée en chevaux fiscaux (CV)

### Types de véhicules
- Chaque type a des taux d'imposition différents
- Les taux sont définis dans `taux_IRV.json`

---

## Codes d'erreur

| Code | Description |
|------|-------------|
| VEHICULES_MARQUES_ERROR | Erreur récupération marques |
| VEHICULES_MARQUE_NOT_FOUND | Marque non trouvée |
| VEHICULES_MODELES_ERROR | Erreur récupération modèles |
| VEHICULE_NOT_FOUND | Véhicule non trouvé |
| VEHICULE_CREATE_ERROR | Erreur création |
| VEHICULE_UPDATE_ERROR | Erreur mise à jour |
| VEHICULE_DELETE_ERROR | Erreur suppression |
