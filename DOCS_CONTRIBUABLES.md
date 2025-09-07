# Contribuables

## Récupérer tous les contribuables

**GET** `/api/contribuables`

Permet de récupérer la liste de tous les contribuables.

### Réponse

```json
[
  {
    "id": "UUID",
    "adressePrincipale": "string",
    "adresseSecondaire": "string",
    "telephonePrincipal": "string",
    "telephoneSecondaire": "string",
    "email": "string",
    "nationalite": "string",
    "type": "PERSONNE_PHYSIQUE | PERSONNE_MORALE",
    "idNat": "string",
    "NRC": "string",
    "sigle": "string",
    "numeroIdentificationContribuable": "string",
    "nom": "string",
    "sexe": "M | F",
    "matricule": "string",
    "bureau": {
      "id": "UUID",
      "nom": "string",
      "code": "string",
      "division": {
        "id": "UUID",
        "nom": "string",
        "code": "string"
      }
    }
  }
]
```

## Récupérer un contribuable par ID

**GET** `/api/contribuables/{id}`

Permet de récupérer un contribuable spécifique par son ID.

### Réponse

```json
{
  "id": "UUID",
  "adressePrincipale": "string",
  "adresseSecondaire": "string",
  "telephonePrincipal": "string",
  "telephoneSecondaire": "string",
  "email": "string",
  "nationalite": "string",
  "type": "PERSONNE_PHYSIQUE | PERSONNE_MORALE",
  "idNat": "string",
  "NRC": "string",
  "sigle": "string",
  "numeroIdentificationContribuable": "string",
  "nom": "string",
  "sexe": "M | F",
  "matricule": "string",
  "bureau": {
    "id": "UUID",
    "nom": "string",
    "code": "string",
    "division": {
      "id": "UUID",
      "nom": "string",
      "code": "string"
    }
  }
}
```

## Créer un contribuable

**POST** `/api/contribuables`

Permet de créer un nouveau contribuable.

### Payload

```json
{
  "adressePrincipale": "string",
  "adresseSecondaire": "string",
  "telephonePrincipal": "string",
  "telephoneSecondaire": "string",
  "email": "string",
  "nationalite": "string",
  "type": "PERSONNE_PHYSIQUE | PERSONNE_MORALE",
  "idNat": "string",
  "NRC": "string",
  "sigle": "string",
  "numeroIdentificationContribuable": "string",
  "nom": "string",
  "sexe": "M | F",
  "matricule": "string",
  "bureauId": "UUID"
}
```

### Réponse

```json
{
  "id": "UUID",
  "adressePrincipale": "string",
  "adresseSecondaire": "string",
  "telephonePrincipal": "string",
  "telephoneSecondaire": "string",
  "email": "string",
  "nationalite": "string",
  "type": "PERSONNE_PHYSIQUE | PERSONNE_MORALE",
  "idNat": "string",
  "NRC": "string",
  "sigle": "string",
  "numeroIdentificationContribuable": "string",
  "nom": "string",
  "sexe": "M | F",
  "matricule": "string",
  "bureau": {
    "id": "UUID",
    "nom": "string",
    "code": "string",
    "division": {
      "id": "UUID",
      "nom": "string",
      "code": "string"
    }
  }
}
```

## Mettre à jour un contribuable

**PUT** `/api/contribuables/{id}`

Permet de mettre à jour un contribuable existant.

### Payload

```json
{
  "adressePrincipale": "string",
  "adresseSecondaire": "string",
  "telephonePrincipal": "string",
  "telephoneSecondaire": "string",
  "email": "string",
  "nationalite": "string",
  "type": "PERSONNE_PHYSIQUE | PERSONNE_MORALE",
  "idNat": "string",
  "NRC": "string",
  "sigle": "string",
  "numeroIdentificationContribuable": "string",
  "nom": "string",
  "sexe": "M | F",
  "matricule": "string",
  "bureauId": "UUID"
}
```

### Réponse

```json
{
  "id": "UUID",
  "adressePrincipale": "string",
  "adresseSecondaire": "string",
  "telephonePrincipal": "string",
  "telephoneSecondaire": "string",
  "email": "string",
  "nationalite": "string",
  "type": "PERSONNE_PHYSIQUE | PERSONNE_MORALE",
  "idNat": "string",
  "NRC": "string",
  "sigle": "string",
  "numeroIdentificationContribuable": "string",
  "nom": "string",
  "sexe": "M | F",
  "matricule": "string",
  "bureau": {
    "id": "UUID",
    "nom": "string",
    "code": "string",
    "division": {
      "id": "UUID",
      "nom": "string",
      "code": "string"
    }
  }
}
```

## Supprimer un contribuable

**DELETE** `/api/contribuables/{id}`

Permet de supprimer un contribuable.

### Réponse

```json
{
  "message": "Contribuable supprimé avec succès"
}
```
