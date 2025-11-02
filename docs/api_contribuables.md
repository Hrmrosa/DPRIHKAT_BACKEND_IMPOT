# Documentation API - Gestion des Contribuables

Cette documentation détaille les endpoints disponibles pour la gestion des contribuables dans l'API DPRIHKAT.

---

## Vue d'ensemble

Les contribuables sont des entités qui représentent les personnes physiques ou morales assujetties à l'impôt.

### Base URL
```
/api/contribuables
```

---

## Endpoints

### 1. Récupérer tous les contribuables

Récupère la liste de tous les contribuables enregistrés dans le système.

- **URL**: `/api/contribuables`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`, `CONTROLLEUR`, `CHEF_DE_BUREAU`
- **Paramètres**: Aucun

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "contribuables": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "nom": "KABILA",
        "postnom": "JOSEPH",
        "prenom": "LAURENT",
        "sexe": "M",
        "matricule": "CONT-001",
        "bureau": {
          "id": "550e8400-e29b-41d4-a716-446655440001",
          "nom": "Bureau des Contribuables",
          "code": "BC"
        },
        "adressePrincipale": "Avenue 123, Quartier Centre-ville",
        "telephonePrincipal": "+243820123456",
        "email": "contribuable@example.com",
        "type": "PERSONNE_PHYSIQUE",
        "numeroIdentificationContribuable": "NIF-123456789",
        "actif": true
      }
    ]
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "CONTRIBUABLES_FETCH_ERROR",
    "message": "Erreur lors de la récupération des contribuables",
    "details": "Message d'erreur détaillé"
  }
}
```

---

### 2. Récupérer un contribuable par son ID

Récupère les détails d'un contribuable spécifique incluant ses véhicules et concessions.

- **URL**: `/api/contribuables/{id}`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`, `CONTROLLEUR`, `CHEF_DE_BUREAU`, `CONTRIBUABLE`
- **Paramètres**:
  - `id` (path): UUID du contribuable

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "contribuable": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "nom": "KABILA",
      "postnom": "JOSEPH",
      "prenom": "LAURENT",
      "sexe": "M",
      "matricule": "CONT-001",
      "bureau": {
        "id": "550e8400-e29b-41d4-a716-446655440001",
        "nom": "Bureau des Contribuables",
        "code": "BC"
      },
      "adressePrincipale": "Avenue 123, Quartier Centre-ville",
      "telephonePrincipal": "+243820123456",
      "email": "contribuable@example.com",
      "type": "PERSONNE_PHYSIQUE",
      "numeroIdentificationContribuable": "NIF-123456789",
      "actif": true,
      "vehicules": [
        {
          "id": "550e8400-e29b-41d4-a716-446655440010",
          "marque": "Toyota",
          "modele": "Corolla",
          "immatriculation": "CD-123-ABC"
        }
      ],
      "concessions": [
        {
          "id": "550e8400-e29b-41d4-a716-446655440020",
          "numero": "CONC-2023-001",
          "localisation": "Lubumbashi"
        }
      ]
    }
  }
}
```

---

### 3. Récupérer les détails complets d'un contribuable

Retourne tous les détails incluant biens immobiliers et véhicules.

- **URL**: `/api/contribuables/{id}/details`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`, `CONTROLLEUR`

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "nom": "KABILA",
    "proprietes": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440030",
        "adresse": "Avenue de la Liberté",
        "type": "VI",
        "superficie": 500.0,
        "montantImpot": 75000.0
      }
    ],
    "vehicules": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440010",
        "immatriculation": "CD-123-ABC",
        "marque": "Toyota",
        "modele": "Corolla"
      }
    ]
  }
}
```

---

### 4. Créer un nouveau contribuable

Crée un nouveau contribuable. Détecte automatiquement les doublons.

- **URL**: `/api/contribuables`
- **Méthode**: `POST`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`

#### Corps de la requête

```json
{
  "nom": "KABILA",
  "postnom": "JOSEPH",
  "prenom": "LAURENT",
  "sexe": "M",
  "bureau": {
    "id": "550e8400-e29b-41d4-a716-446655440001"
  },
  "adressePrincipale": "Avenue 123",
  "telephonePrincipal": "+243820123456",
  "email": "contribuable@example.com",
  "type": "PERSONNE_PHYSIQUE",
  "idNat": "IDNAT-123456",
  "numeroIdentificationContribuable": "NIF-123456789"
}
```

#### Réponse succès (nouveau)

```json
{
  "success": true,
  "data": {
    "contribuable": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "nom": "KABILA",
      "actif": true
    }
  }
}
```

#### Réponse (doublon détecté)

```json
{
  "success": true,
  "data": {
    "code": "CONTRIBUABLE_ALREADY_EXISTS",
    "message": "Un contribuable avec des informations similaires existe déjà",
    "isExisting": true,
    "contribuable": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "nom": "KABILA"
    }
  }
}
```

---

### 5. Mettre à jour un contribuable

- **URL**: `/api/contribuables/{id}`
- **Méthode**: `PUT`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`, `CONTRIBUABLE`

---

### 6. Désactiver un contribuable

Suppression logique du contribuable.

- **URL**: `/api/contribuables/{id}`
- **Méthode**: `DELETE`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`

#### Réponse succès

```json
{
  "success": true,
  "data": {
    "message": "Contribuable désactivé avec succès",
    "contribuable": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "actif": false
    }
  }
}
```

---

### 7. Rechercher un contribuable

- **URL**: `/api/contribuables/recherche`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`, `CONTROLLEUR`, `CHEF_DE_BUREAU`
- **Paramètres**:
  - `critere`: `telephone`, `email`, `nom`, `numero`
  - `valeur`: Valeur à rechercher

#### Exemple

```
GET /api/contribuables/recherche?critere=telephone&valeur=+243820123456
```

---

## Structure Contribuable

| Champ | Type | Obligatoire |
|-------|------|-------------|
| nom | String | Oui |
| sexe | Enum | Oui |
| telephonePrincipal | String | Oui |
| type | Enum | Oui |
| bureau | Bureau | Oui |
| idNat | String | Conditionnel* |
| NRC | String | Conditionnel** |

\* Obligatoire pour PERSONNE_PHYSIQUE  
\** Obligatoire pour PERSONNE_MORALE

---

## Règles métier

### Détection doublons
Vérification par ordre de priorité:
1. Téléphone principal
2. Email
3. Numéro identification
4. Nom exact

### Types
- **PERSONNE_PHYSIQUE**: Individus
- **PERSONNE_MORALE**: Entreprises

### Déclarations
- En ligne: 2 janvier au 1er février uniquement
- Hors ligne: Toute l'année à l'administration
