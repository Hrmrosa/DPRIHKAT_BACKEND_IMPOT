# Documentation API - Gestion des Véhicules

Cette documentation détaille les endpoints disponibles pour la gestion des véhicules dans l'API DPRIHKAT.

## Vue d'ensemble

L'API des véhicules permet d'accéder aux données des marques et modèles de véhicules stockées dans le fichier `voiture.json`. Ces données sont utilisées pour standardiser les informations sur les véhicules dans le système.

## Base URL

```
/api/vehicules
```

## Endpoints

### 1. Récupérer toutes les marques de véhicules

Récupère la liste de toutes les marques de véhicules disponibles dans le système.

- **URL**: `/api/vehicules/marques`
- **Méthode**: `GET`
- **Rôles autorisés**: Tous les rôles authentifiés
- **Paramètres**: Aucun

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "marques": [
      "ABARTH",
      "ALFA ROMEO",
      "AMG",
      "Aston Martin",
      "AUDI",
      "BENTLEY",
      "BMW",
      "BUGATTI",
      "CADILLAC",
      "CHEVROLET",
      "CHRYSLER",
      "CITROEN",
      "DACIA",
      "DAEWOO",
      "DAIHATSU",
      "DODGE",
      "FERRARI",
      "FIAT",
      "FORD",
      "HONDA",
      "HYUNDAI",
      "INFINITI",
      "JAGUAR",
      "JEEP",
      "KIA",
      "LAMBORGHINI",
      "LANCIA",
      "LAND ROVER",
      "LEXUS",
      "MASERATI",
      "MAZDA",
      "MERCEDES-BENZ",
      "MINI",
      "MITSUBISHI",
      "NISSAN",
      "OPEL",
      "PEUGEOT",
      "PORSCHE",
      "RENAULT",
      "ROLLS-ROYCE",
      "SEAT",
      "SKODA",
      "SMART",
      "SUBARU",
      "SUZUKI",
      "TESLA",
      "TOYOTA",
      "VOLKSWAGEN",
      "VOLVO"
    ]
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-15T10:45:00.000Z"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "VEHICULES_MARQUES_ERROR",
    "message": "Erreur lors de la récupération des marques de véhicules",
    "details": "Message d'erreur détaillé"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-15T10:45:00.000Z"
  }
}
```

### 2. Récupérer les modèles d'une marque

Récupère la liste des modèles disponibles pour une marque de véhicule spécifique.

- **URL**: `/api/vehicules/marques/{marque}/modeles`
- **Méthode**: `GET`
- **Rôles autorisés**: Tous les rôles authentifiés
- **Paramètres**:
  - `marque` (path): Nom de la marque de véhicule (ex: "TOYOTA", "BMW")

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "marque": "TOYOTA",
    "modeles": [
      "4RUNNER",
      "86",
      "ALLEX",
      "ALLION",
      "ALPHARD",
      "ALTEZZA",
      "AQUA",
      "ARISTO",
      "AURIS",
      "AVALON",
      "AVANZA",
      "AVENSIS",
      "AYGO",
      "BB",
      "BELTA",
      "BLADE",
      "BREVIS",
      "C-HR",
      "CALDINA",
      "CAMI",
      "CAMRY",
      "CARINA",
      "CELICA",
      "CENTURY",
      "CHASER",
      "COASTER",
      "COROLLA",
      "COROLLA AXIO",
      "COROLLA FIELDER",
      "COROLLA LEVIN",
      "COROLLA RUMION",
      "COROLLA SPACIO",
      "CORONA",
      "CORSA",
      "CRESSIDA",
      "CRESTA",
      "CROWN",
      "CROWN MAJESTA",
      "DUET",
      "DYNA",
      "ESQUIRE",
      "ESTIMA",
      "FJ CRUISER",
      "FORTUNER",
      "FUNCARGO",
      "GAIA",
      "GRANVIA",
      "HARRIER",
      "HIACE",
      "HIGHLANDER",
      "HILUX",
      "HILUX SURF",
      "IPSUM",
      "IQ",
      "ISIS",
      "IST",
      "KLUGER",
      "LAND CRUISER",
      "LAND CRUISER PRADO",
      "LITE ACE",
      "MARK II",
      "MARK X",
      "MARK X ZIO",
      "MATRIX",
      "MEGA CRUISER",
      "MR-S",
      "MR2",
      "NOAH",
      "OPA",
      "PASSO",
      "PLATZ",
      "PORTE",
      "PREMIO",
      "PRIUS",
      "PROBOX",
      "PROGRES",
      "PRONARD",
      "RACTIS",
      "RAIZE",
      "RAUM",
      "RAV4",
      "REGIUS",
      "RUSH",
      "SAI",
      "SEQUOIA",
      "SIENNA",
      "SIENTA",
      "SOARER",
      "SOLARA",
      "SPADE",
      "SPRINTER",
      "STARLET",
      "SUCCEED",
      "SUPRA",
      "TACOMA",
      "TANK",
      "TERCEL",
      "TOWN ACE",
      "TOYOACE",
      "TUNDRA",
      "URBAN CRUISER",
      "VANGUARD",
      "VELLFIRE",
      "VENZA",
      "VEROSSA",
      "VISTA",
      "VITZ",
      "VOLTZ",
      "VOXY",
      "WISH",
      "YARIS"
    ]
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-15T10:45:00.000Z"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "VEHICULES_MARQUE_NOT_FOUND",
    "message": "Marque non trouvée",
    "details": "La marque spécifiée n'existe pas: INVALID_MARQUE"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-15T10:45:00.000Z"
  }
}
```

### 3. Vérifier l'existence d'un modèle pour une marque

Vérifie si un modèle spécifique existe pour une marque donnée.

- **URL**: `/api/vehicules/marques/{marque}/modeles/{modele}/exists`
- **Méthode**: `GET`
- **Rôles autorisés**: Tous les rôles authentifiés
- **Paramètres**:
  - `marque` (path): Nom de la marque de véhicule
  - `modele` (path): Nom du modèle à vérifier

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "marque": "TOYOTA",
    "modele": "COROLLA",
    "exists": true
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-15T10:45:00.000Z"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "VEHICULES_VERIFICATION_ERROR",
    "message": "Erreur lors de la vérification du modèle",
    "details": "Message d'erreur détaillé"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-15T10:45:00.000Z"
  }
}
```

### 4. Recharger les données des véhicules

Recharge les données des véhicules depuis le fichier voiture.json.

- **URL**: `/api/vehicules/reload`
- **Méthode**: `POST`
- **Rôles autorisés**: Administrateurs
- **Corps de la requête**: Aucun

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "message": "Données des véhicules rechargées avec succès"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-15T10:45:00.000Z"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "VEHICULES_RELOAD_ERROR",
    "message": "Erreur lors du rechargement des données des véhicules",
    "details": "Message d'erreur détaillé"
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-15T10:45:00.000Z"
  }
}
```

## Structure du fichier voiture.json

Le fichier `voiture.json` contient une liste de marques de véhicules et leurs modèles associés. Voici sa structure :

```json
{
  "car_brands": [
    {
      "name": "MARQUE_1",
      "models": [
        "MODELE_1",
        "MODELE_2",
        "MODELE_3"
      ]
    },
    {
      "name": "MARQUE_2",
      "models": [
        "MODELE_1",
        "MODELE_2"
      ]
    }
  ]
}
```

Cette structure permet de maintenir une liste standardisée des marques et modèles de véhicules disponibles dans le système.
