# Déclarations

## Récupérer toutes les déclarations

**GET** `/api/declarations`

Permet de récupérer la liste de toutes les déclarations.

### Réponse

```json
[
  {
    "id": "UUID",
    "dateDeclaration": "date-time",
    "statut": "SOUMISE | VALIDEE | REJETEE",
    "source": "EN_LIGNE | ADMINISTRATION",
    "actif": "boolean",
    "propriete": {
      "id": "UUID",
      "type": "VI | AP | TE | MA | BI | CI | IN | CO | PA",
      "localite": "string",
      "rangLocalite": "integer",
      "superficie": "double",
      "adresse": "string",
      "montantImpot": "double",
      "location": {
        "type": "Point",
        "coordinates": [
          "longitude",
          "latitude"
        ]
      },
      "actif": "boolean",
      "declarationEnLigne": "boolean",
      "declare": "boolean",
      "proprietaire": {
        "id": "UUID",
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
    },
    "concession": {
      "id": "UUID",
      "nombreCarresMinier": "double",
      "dateAcquisition": "date-time",
      "type": "EXPLOITATION | RECHERCHE",
      "annexe": "string",
      "montantImpot": "double",
      "actif": "boolean",
      "titulaire": {
        "id": "UUID",
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
    },
    "agentValidateur": {
      "id": "UUID",
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
    },
    "contribuable": {
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
  }
]
```

## Récupérer une déclaration par ID

**GET** `/api/declarations/{id}`

Permet de récupérer une déclaration spécifique par son ID.

### Réponse

```json
{
  "id": "UUID",
  "dateDeclaration": "date-time",
  "statut": "SOUMISE | VALIDEE | REJETEE",
  "source": "EN_LIGNE | ADMINISTRATION",
  "actif": "boolean",
  "propriete": {
    "id": "UUID",
    "type": "VI | AP | TE | MA | BI | CI | IN | CO | PA",
    "localite": "string",
    "rangLocalite": "integer",
    "superficie": "double",
    "adresse": "string",
    "montantImpot": "double",
    "location": {
      "type": "Point",
      "coordinates": [
        "longitude",
        "latitude"
      ]
    },
    "actif": "boolean",
    "declarationEnLigne": "boolean",
    "declare": "boolean",
    "proprietaire": {
      "id": "UUID",
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
  },
  "concession": {
    "id": "UUID",
    "nombreCarresMinier": "double",
    "dateAcquisition": "date-time",
    "type": "EXPLOITATION | RECHERCHE",
    "annexe": "string",
    "montantImpot": "double",
    "actif": "boolean",
    "titulaire": {
      "id": "UUID",
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
  },
  "agentValidateur": {
    "id": "UUID",
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
  },
  "contribuable": {
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
}
```

## Créer une déclaration

**POST** `/api/declarations`

Permet de créer une nouvelle déclaration.

### Payload

```json
{
  "dateDeclaration": "date-time",
  "statut": "SOUMISE | VALIDEE | REJETEE",
  "source": "EN_LIGNE | ADMINISTRATION",
  "proprieteId": "UUID",
  "concessionId": "UUID",
  "contribuableId": "UUID",
  "agentValidateurId": "UUID"
}
```

### Réponse

```json
{
  "id": "UUID",
  "dateDeclaration": "date-time",
  "statut": "SOUMISE | VALIDEE | REJETEE",
  "source": "EN_LIGNE | ADMINISTRATION",
  "actif": "boolean",
  "propriete": {
    "id": "UUID",
    "type": "VI | AP | TE | MA | BI | CI | IN | CO | PA",
    "localite": "string",
    "rangLocalite": "integer",
    "superficie": "double",
    "adresse": "string",
    "montantImpot": "double",
    "location": {
      "type": "Point",
      "coordinates": [
        "longitude",
        "latitude"
      ]
    },
    "actif": "boolean",
    "declarationEnLigne": "boolean",
    "declare": "boolean",
    "proprietaire": {
      "id": "UUID",
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
  },
  "concession": {
    "id": "UUID",
    "nombreCarresMinier": "double",
    "dateAcquisition": "date-time",
    "type": "EXPLOITATION | RECHERCHE",
    "annexe": "string",
    "montantImpot": "double",
    "actif": "boolean",
    "titulaire": {
      "id": "UUID",
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
  },
  "agentValidateur": {
    "id": "UUID",
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
  },
  "contribuable": {
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
}
```

## Mettre à jour une déclaration

**PUT** `/api/declarations/{id}`

Permet de mettre à jour une déclaration existante.

### Payload

```json
{
  "dateDeclaration": "date-time",
  "statut": "SOUMISE | VALIDEE | REJETEE",
  "source": "EN_LIGNE | ADMINISTRATION",
  "proprieteId": "UUID",
  "concessionId": "UUID",
  "contribuableId": "UUID",
  "agentValidateurId": "UUID"
}
```

### Réponse

```json
{
  "id": "UUID",
  "dateDeclaration": "date-time",
  "statut": "SOUMISE | VALIDEE | REJETEE",
  "source": "EN_LIGNE | ADMINISTRATION",
  "actif": "boolean",
  "propriete": {
    "id": "UUID",
    "type": "VI | AP | TE | MA | BI | CI | IN | CO | PA",
    "localite": "string",
    "rangLocalite": "integer",
    "superficie": "double",
    "adresse": "string",
    "montantImpot": "double",
    "location": {
      "type": "Point",
      "coordinates": [
        "longitude",
        "latitude"
      ]
    },
    "actif": "boolean",
    "declarationEnLigne": "boolean",
    "declare": "boolean",
    "proprietaire": {
      "id": "UUID",
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
  },
  "concession": {
    "id": "UUID",
    "nombreCarresMinier": "double",
    "dateAcquisition": "date-time",
    "type": "EXPLOITATION | RECHERCHE",
    "annexe": "string",
    "montantImpot": "double",
    "actif": "boolean",
    "titulaire": {
      "id": "UUID",
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
  },
  "agentValidateur": {
    "id": "UUID",
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
  },
  "contribuable": {
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
}
```

## Supprimer une déclaration

**DELETE** `/api/declarations/{id}`

Permet de supprimer une déclaration.

### Réponse

```json
{
  "message": "Déclaration supprimée avec succès"
}
```
