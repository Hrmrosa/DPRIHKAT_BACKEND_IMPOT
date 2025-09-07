# Taxations

## Récupérer toutes les taxations

**GET** `/api/taxations`

Permet de récupérer la liste de toutes les taxations.

### Réponse

```json
[
  {
    "id": "UUID",
    "dateTaxation": "date-time",
    "montant": "double",
    "exercice": "string",
    "statut": "SOUMISE | VALIDEE | PAYEE",
    "typeImpot": "IF | ICM | IRL | IRV | RL",
    "exoneration": "boolean",
    "actif": "boolean",
    "declaration": {
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
    },
    "agentTaxateur": {
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
    }
  }
]
```

## Récupérer une taxation par ID

**GET** `/api/taxations/{id}`

Permet de récupérer une taxation spécifique par son ID.

### Réponse

```json
{
  "id": "UUID",
  "dateTaxation": "date-time",
  "montant": "double",
  "exercice": "string",
  "statut": "SOUMISE | VALIDEE | PAYEE",
  "typeImpot": "IF | ICM | IRL | IRV | RL",
  "exoneration": "boolean",
  "actif": "boolean",
  "declaration": {
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
  },
  "agentTaxateur": {
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
  }
}
```

## Créer une taxation

**POST** `/api/taxations`

Permet de créer une nouvelle taxation.

### Payload

```json
{
  "dateTaxation": "date-time",
  "montant": "double",
  "exercice": "string",
  "statut": "SOUMISE | VALIDEE | PAYEE",
  "typeImpot": "IF | ICM | IRL | IRV | RL",
  "exoneration": "boolean",
  "declarationId": "UUID",
  "agentTaxateurId": "UUID",
  "agentValidateurId": "UUID"
}
```

### Réponse

```json
{
  "id": "UUID",
  "dateTaxation": "date-time",
  "montant": "double",
  "exercice": "string",
  "statut": "SOUMISE | VALIDEE | PAYEE",
  "typeImpot": "IF | ICM | IRL | IRV | RL",
  "exoneration": "boolean",
  "actif": "boolean",
  "declaration": {
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
  },
  "agentTaxateur": {
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
  }
}
```

## Mettre à jour une taxation

**PUT** `/api/taxations/{id}`

Permet de mettre à jour une taxation existante.

### Payload

```json
{
  "dateTaxation": "date-time",
  "montant": "double",
  "exercice": "string",
  "statut": "SOUMISE | VALIDEE | PAYEE",
  "typeImpot": "IF | ICM | IRL | IRV | RL",
  "exoneration": "boolean",
  "declarationId": "UUID",
  "agentTaxateurId": "UUID",
  "agentValidateurId": "UUID"
}
```

### Réponse

```json
{
  "id": "UUID",
  "dateTaxation": "date-time",
  "montant": "double",
  "exercice": "string",
  "statut": "SOUMISE | VALIDEE | PAYEE",
  "typeImpot": "IF | ICM | IRL | IRV | RL",
  "exoneration": "boolean",
  "actif": "boolean",
  "declaration": {
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
  },
  "agentTaxateur": {
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
  }
}
```

## Supprimer une taxation

**DELETE** `/api/taxations/{id}`

Permet de supprimer une taxation.

### Réponse

```json
{
  "message": "Taxation supprimée avec succès"
}
```
