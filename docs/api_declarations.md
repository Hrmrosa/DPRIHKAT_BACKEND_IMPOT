# Documentation API - Gestion des Déclarations

Cette documentation détaille les endpoints disponibles pour la gestion des déclarations fiscales dans l'API DPRIHKAT.

---

## Vue d'ensemble

Les déclarations permettent aux contribuables de déclarer leurs biens (propriétés, véhicules) pour la taxation.

### Base URL
```
/api/declarations
```
 
---

## Endpoints

### 1. Récupérer toutes les déclarations

- **URL**: `/api/declarations`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`, `CONTROLLEUR`, `CHEF_DE_BUREAU`, `TAXATEUR`

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "declarations": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "contribuable": {
          "id": "550e8400-e29b-41d4-a716-446655440001",
          "nom": "KABILA",
          "numeroIdentificationContribuable": "NIF-123456789"
        },
        "dateDeclaration": "2024-01-15T10:30:00",
        "exercice": 2024,
        "enLigne": true,
        "statut": "VALIDEE",
        "proprietes": [
          {
            "id": "550e8400-e29b-41d4-a716-446655440010",
            "adresse": "Avenue de la Liberté",
            "type": "VI"
          }
        ],
        "vehicules": [
          {
            "id": "550e8400-e29b-41d4-a716-446655440020",
            "immatriculation": "CD-123-ABC",
            "marque": "Toyota"
          }
        ]
      }
    ]
  }
}
```

---

### 2. Récupérer une déclaration par ID

- **URL**: `/api/declarations/{id}`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`, `CONTROLLEUR`, `CHEF_DE_BUREAU`, `TAXATEUR`, `CONTRIBUABLE`

---

### 3. Créer une nouvelle déclaration

Permet à un contribuable de créer une déclaration de ses biens.

- **URL**: `/api/declarations`
- **Méthode**: `POST`
- **Rôles autorisés**: `CONTRIBUABLE`, `ADMIN`, `TAXATEUR`

#### Corps de la requête

```json
{
  "contribuableId": "550e8400-e29b-41d4-a716-446655440001",
  "exercice": 2024,
  "enLigne": true,
  "proprietesIds": [
    "550e8400-e29b-41d4-a716-446655440010",
    "550e8400-e29b-41d4-a716-446655440011"
  ],
  "vehiculesIds": [
    "550e8400-e29b-41d4-a716-446655440020"
  ]
}
```

---

### 4. Valider une déclaration

Permet à un agent de valider une déclaration.

- **URL**: `/api/declarations/{id}/valider`
- **Méthode**: `POST`
- **Rôles autorisés**: `ADMIN`, `TAXATEUR`, `CHEF_DE_BUREAU`

---

### 5. Rejeter une déclaration

- **URL**: `/api/declarations/{id}/rejeter`
- **Méthode**: `POST`
- **Rôles autorisés**: `ADMIN`, `TAXATEUR`, `CHEF_DE_BUREAU`

---

## Structure de l'entité Déclaration

| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant unique |
| contribuable | Contribuable | Contribuable déclarant |
| dateDeclaration | DateTime | Date de la déclaration |
| exercice | Integer | Année fiscale |
| enLigne | Boolean | Déclaration en ligne |
| statut | Enum | Statut de la déclaration |
| proprietes | List | Propriétés déclarées |
| vehicules | List | Véhicules déclarés |

---

## Énumérations

### StatutDeclaration
- `EN_ATTENTE`: En attente de validation
- `VALIDEE`: Validée par l'administration
- `REJETEE`: Rejetée

---

## Règles métier

### Période de déclaration en ligne
- Du 2 janvier au 1er février de chaque année
- Hors période: déclaration uniquement à l'administration

### Validation
- Seuls les agents autorisés peuvent valider/rejeter
- Une déclaration validée génère automatiquement des taxations
