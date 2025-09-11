# Documentation API - Gestion des Contribuables

Cette documentation détaille les endpoints disponibles pour la gestion des contribuables dans l'API DPRIHKAT.

## Vue d'ensemble

Les contribuables sont des entités qui représentent les personnes physiques ou morales assujetties à l'impôt. Ils sont également considérés comme des agents dans le système.

## Base URL

```
/api/contribuables
```

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
        "id": "uuid-string",
        "nom": "Nom du contribuable",
        "sexe": "M",
        "matricule": "CONT-001",
        "bureau": {
          "id": "uuid-string",
          "nom": "Bureau des Contribuables",
          "code": "BC"
        },
        "adressePrincipale": "Avenue 123, Quartier Centre-ville",
        "telephonePrincipal": "+243820123456",
        "email": "contribuable@example.com",
        "nationalite": "RDC",
        "type": "PERSONNE_PHYSIQUE",
        "idNat": "IDNAT-123456",
        "numeroIdentificationContribuable": "NIF-123456789",
        "codeQR": "string",
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

### 2. Récupérer un contribuable par son ID

Récupère les détails d'un contribuable spécifique à partir de son identifiant unique.

- **URL**: `/api/contribuables/{id}`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`, `CONTROLLEUR`, `CHEF_DE_BUREAU`, `CONTRIBUABLE`
- **Paramètres**:
  - `id` (path): UUID du contribuable à récupérer

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "contribuable": {
      "id": "uuid-string",
      "nom": "Nom du contribuable",
      "sexe": "M",
      "matricule": "CONT-001",
      "bureau": {
        "id": "uuid-string",
        "nom": "Bureau des Contribuables",
        "code": "BC"
      },
      "adressePrincipale": "Avenue 123, Quartier Centre-ville",
      "telephonePrincipal": "+243820123456",
      "email": "contribuable@example.com",
      "nationalite": "RDC",
      "type": "PERSONNE_PHYSIQUE",
      "idNat": "IDNAT-123456",
      "numeroIdentificationContribuable": "NIF-123456789",
      "codeQR": "string",
      "actif": true
    }
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "CONTRIBUABLE_NOT_FOUND",
    "message": "Contribuable non trouvé",
    "details": "Aucun contribuable trouvé avec l'ID fourni"
  }
}
```

### 3. Créer un nouveau contribuable

Crée un nouveau contribuable dans le système.

- **URL**: `/api/contribuables`
- **Méthode**: `POST`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`
- **Corps de la requête**:

```json
{
  "nom": "Nom du contribuable",
  "sexe": "M",
  "matricule": "CONT-001",
  "bureau": {
    "id": "uuid-string"
  },
  "adressePrincipale": "Avenue 123, Quartier Centre-ville",
  "adresseSecondaire": "Zone Industrielle 1, Lubumbashi",
  "telephonePrincipal": "+243820123456",
  "telephoneSecondaire": "+243815123456",
  "email": "contribuable@example.com",
  "nationalite": "RDC",
  "type": "PERSONNE_PHYSIQUE",
  "idNat": "IDNAT-123456",
  "NRC": "RCCM-CD/LUB/2020-B-1234",
  "sigle": "ABC01",
  "numeroIdentificationContribuable": "NIF-123456789",
  "codeQR": "string"
}
```

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "contribuable": {
      "id": "uuid-string",
      "nom": "Nom du contribuable",
      "sexe": "M",
      "matricule": "CONT-001",
      "bureau": {
        "id": "uuid-string",
        "nom": "Bureau des Contribuables",
        "code": "BC"
      },
      "adressePrincipale": "Avenue 123, Quartier Centre-ville",
      "telephonePrincipal": "+243820123456",
      "email": "contribuable@example.com",
      "nationalite": "RDC",
      "type": "PERSONNE_PHYSIQUE",
      "idNat": "IDNAT-123456",
      "numeroIdentificationContribuable": "NIF-123456789",
      "codeQR": "string",
      "actif": true
    }
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "CONTRIBUABLE_CREATE_ERROR",
    "message": "Erreur lors de la création du contribuable",
    "details": "Message d'erreur détaillé"
  }
}
```

### 4. Mettre à jour un contribuable

Met à jour les informations d'un contribuable existant.

- **URL**: `/api/contribuables/{id}`
- **Méthode**: `PUT`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`, `CONTRIBUABLE`
- **Paramètres**:
  - `id` (path): UUID du contribuable à mettre à jour
- **Corps de la requête**:

```json
{
  "nom": "Nouveau nom du contribuable",
  "adressePrincipale": "Nouvelle adresse principale",
  "telephonePrincipal": "+243820654321",
  "email": "nouveau.email@example.com",
  "codeQR": "string"
}
```

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "contribuable": {
      "id": "uuid-string",
      "nom": "Nouveau nom du contribuable",
      "sexe": "M",
      "matricule": "CONT-001",
      "bureau": {
        "id": "uuid-string",
        "nom": "Bureau des Contribuables",
        "code": "BC"
      },
      "adressePrincipale": "Nouvelle adresse principale",
      "telephonePrincipal": "+243820654321",
      "email": "nouveau.email@example.com",
      "nationalite": "RDC",
      "type": "PERSONNE_PHYSIQUE",
      "idNat": "IDNAT-123456",
      "numeroIdentificationContribuable": "NIF-123456789",
      "codeQR": "string",
      "actif": true
    }
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "CONTRIBUABLE_UPDATE_ERROR",
    "message": "Erreur lors de la mise à jour du contribuable",
    "details": "Message d'erreur détaillé"
  }
}
```

### 5. Supprimer un contribuable

Supprime un contribuable du système (suppression logique).

- **URL**: `/api/contribuables/{id}`
- **Méthode**: `DELETE`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`
- **Paramètres**:
  - `id` (path): UUID du contribuable à supprimer

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "message": "Contribuable supprimé avec succès"
  }
}
```

#### Réponse en cas d'erreur

```json
{
  "success": false,
  "error": {
    "code": "CONTRIBUABLE_DELETE_ERROR",
    "message": "Erreur lors de la suppression du contribuable",
    "details": "Message d'erreur détaillé"
  }
}
```

## Structure de l'entité Contribuable

| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant unique du contribuable |
| nom | String | Nom complet du contribuable |
| sexe | Enum (Sexe) | Genre du contribuable (M/F) |
| matricule | String | Matricule unique du contribuable |
| bureau | Bureau | Bureau auquel est rattaché le contribuable |
| adressePrincipale | String | Adresse principale du contribuable |
| adresseSecondaire | String | Adresse secondaire du contribuable (optionnel) |
| telephonePrincipal | String | Numéro de téléphone principal |
| telephoneSecondaire | String | Numéro de téléphone secondaire (optionnel) |
| email | String | Adresse email du contribuable |
| nationalite | String | Nationalité du contribuable |
| type | Enum (TypeContribuable) | Type de contribuable (PERSONNE_PHYSIQUE/PERSONNE_MORALE) |
| idNat | String | Numéro d'identification nationale |
| NRC | String | Numéro de registre de commerce (pour les personnes morales) |
| sigle | String | Sigle ou acronyme (pour les personnes morales) |
| numeroIdentificationContribuable | String | Numéro d'identification fiscale (NIF) |
| codeQR | String | Code QR du contribuable |
| actif | boolean | Indique si le contribuable est actif dans le système |

## Règles métier

1. Un contribuable est également considéré comme un agent dans le système.
2. Un contribuable peut être une personne physique ou une personne morale.
3. Chaque contribuable doit avoir un numéro d'identification fiscale (NIF) unique.
4. Les contribuables peuvent déclarer des impôts en ligne uniquement du 2 janvier au 1er février.
