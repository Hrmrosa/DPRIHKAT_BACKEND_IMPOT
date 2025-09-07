# Paiements

## Récupérer tous les paiements

**GET** `/api/paiements`

Permet de récupérer la liste de tous les paiements.

### Réponse

```json
[
  {
    "id": "UUID",
    "date": "date-time",
    "montant": "double",
    "mode": "ESPECE | VIREMENT_BANCAIRE | CHEQUE | MOBILE_MONEY | CARTE_BANCAIRE",
    "statut": "EN_ATTENTE | VALIDE | REJETE",
    "bordereauBancaire": "string",
    "actif": "boolean",
    "taxation": {
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
  }
]
```

## Récupérer un paiement par ID

**GET** `/api/paiements/{id}`

Permet de récupérer un paiement spécifique par son ID.

### Réponse

```json
{
  "id": "UUID",
  "date": "date-time",
  "montant": "double",
  "mode": "ESPECE | VIREMENT_BANCAIRE | CHEQUE | MOBILE_MONEY | CARTE_BANCAIRE",
  "statut": "EN_ATTENTE | VALIDE | REJETE",
  "bordereauBancaire": "string",
  "actif": "boolean",
  "taxation": {
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
}
```

## Créer un paiement

**POST** `/api/paiements`

Permet de créer un nouveau paiement.

### Payload

```json
{
  "date": "date-time",
  "montant": "double",
  "mode": "ESPECE | VIREMENT_BANCAIRE | CHEQUE | MOBILE_MONEY | CARTE_BANCAIRE",
  "statut": "EN_ATTENTE | VALIDE | REJETE",
  "bordereauBancaire": "string",
  "taxationId": "UUID"
}
```

### Réponse

```json
{
  "id": "UUID",
  "date": "date-time",
  "montant": "double",
  "mode": "ESPECE | VIREMENT_BANCAIRE | CHEQUE | MOBILE_MONEY | CARTE_BANCAIRE",
  "statut": "EN_ATTENTE | VALIDE | REJETE",
  "bordereauBancaire": "string",
  "actif": "boolean",
  "taxation": {
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
}
```

## Mettre à jour un paiement

**PUT** `/api/paiements/{id}`

Permet de mettre à jour un paiement existant.

### Payload

```json
{
  "date": "date-time",
  "montant": "double",
  "mode": "ESPECE | VIREMENT_BANCAIRE | CHEQUE | MOBILE_MONEY | CARTE_BANCAIRE",
  "statut": "EN_ATTENTE | VALIDE | REJETE",
  "bordereauBancaire": "string",
  "taxationId": "UUID"
}
```

### Réponse

```json
{
  "id": "UUID",
  "date": "date-time",
  "montant": "double",
  "mode": "ESPECE | VIREMENT_BANCAIRE | CHEQUE | MOBILE_MONEY | CARTE_BANCAIRE",
  "statut": "EN_ATTENTE | VALIDE | REJETE",
  "bordereauBancaire": "string",
  "actif": "boolean",
  "taxation": {
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
}
```

## Supprimer un paiement

**DELETE** `/api/paiements/{id}`

Permet de supprimer un paiement.

### Réponse

```json
{
  "message": "Paiement supprimé avec succès"
}
```
