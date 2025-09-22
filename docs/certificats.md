# API de Gestion des Certificats

## Générer un certificat de propriété

`POST /api/certificats/property/{declarationId}`

### Description
Génère un certificat de propriété pour une déclaration donnée.

### Rôles autorisés
- AGENT_CERTIFICAT
- ADMIN

### Paramètres
- `declarationId` (chemin) - UUID de la déclaration

### Réponse
```json
{
  "success": true,
  "data": {
    "certificat": {
      "id": "UUID",
      "numero": "string",
      "dateEmission": "ISO8601",
      "dateExpiration": "ISO8601",
      "type": "string",
      "statut": "string",
      "codeQR": "string",
      "declaration": {
        "id": "UUID",
        "dateDeclaration": "ISO8601",
        "montant": 0
      },
      "propriete": {
        "id": "UUID",
        "adresse": "string"
      },
      "agent": {
        "id": "UUID",
        "nom": "string"
      }
    },
    "message": "Certificat généré avec succès"
  },
  "error": null,
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

### Réponse en cas d'erreur
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "CERTIFICATE_GENERATION_ERROR|INVALID_USER",
    "message": "string",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Générer un certificat de véhicule

`POST /api/certificats/vehicle/{vehiculeId}`

### Description
Génère un certificat pour un véhicule donné.

### Rôles autorisés
- AGENT_CERTIFICAT
- ADMIN

### Paramètres
- `vehiculeId` (chemin) - UUID du véhicule

### Réponse
```json
{
  "success": true,
  "data": {
    "certificat": {
      "id": "UUID",
      "numero": "string",
      "dateEmission": "ISO8601",
      "dateExpiration": "ISO8601",
      "type": "string",
      "statut": "string",
      "codeQR": "string",
      "vehicule": {
        "id": "UUID",
        "marque": "string",
        "modele": "string",
        "immatriculation": "string"
      },
      "agent": {
        "id": "UUID",
        "nom": "string"
      }
    },
    "message": "Certificat généré avec succès"
  },
  "error": null,
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

### Réponse en cas d'erreur
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "CERTIFICATE_GENERATION_ERROR|INVALID_USER",
    "message": "string",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer un certificat par ID

`GET /api/certificats/{id}`

### Description
Récupère les détails d'un certificat spécifique par son ID.

### Rôles autorisés
- AGENT_CERTIFICAT
- TAXATEUR
- RECEVEUR_DES_IMPOTS
- CHEF_DE_BUREAU
- CHEF_DE_DIVISION
- DIRECTEUR
- CONTRIBUABLE
- ADMIN

### Paramètres
- `id` (chemin) - UUID du certificat

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "certificat": {
      "id": "UUID",
      "numero": "string",
      "dateEmission": "ISO8601",
      "dateExpiration": "ISO8601",
      "type": "string",
      "statut": "string",
      "codeQR": "string",
      "declaration": {
        "id": "UUID",
        "dateDeclaration": "ISO8601",
        "montant": 0
      },
      "propriete": {
        "id": "UUID",
        "adresse": "string"
      },
      "vehicule": {
        "id": "UUID",
        "marque": "string",
        "modele": "string",
        "immatriculation": "string"
      },
      "agent": {
        "id": "UUID",
        "nom": "string"
      }
    }
  },
  "error": null,
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

### Réponse en cas d'erreur
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "CERTIFICATE_FETCH_ERROR",
    "message": "Erreur lors de la récupération du certificat",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer les certificats d'une déclaration

`GET /api/certificats/declaration/{declarationId}`

### Description
Récupère la liste des certificats associés à une déclaration.

### Rôles autorisés
- AGENT_CERTIFICAT
- TAXATEUR
- RECEVEUR_DES_IMPOTS
- CHEF_DE_BUREAU
- CHEF_DE_DIVISION
- DIRECTEUR
- CONTRIBUABLE
- ADMIN

### Paramètres
- `declarationId` (chemin) - UUID de la déclaration
- `page` (requête, optionnel, défaut: 0) - Numéro de page
- `size` (requête, optionnel, défaut: 10) - Nombre d'éléments par page

### Réponse
```json
{
  "success": true,
  "data": {
    "certificats": [
      {
        "id": "UUID",
        "numero": "string",
        "dateEmission": "ISO8601",
        "dateExpiration": "ISO8601",
        "type": "string",
        "statut": "string",
        "codeQR": "string",
        "declaration": {
          "id": "UUID",
          "dateDeclaration": "ISO8601",
          "montant": 0
        },
        "propriete": {
          "id": "UUID",
          "adresse": "string"
        },
        "agent": {
          "id": "UUID",
          "nom": "string"
        }
      }
    ],
    "currentPage": 0,
    "totalItems": 0,
    "totalPages": 0
  },
  "error": null,
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

### Réponse en cas d'erreur
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "CERTIFICATE_FETCH_ERROR",
    "message": "Erreur lors de la récupération des certificats",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer les certificats d'un véhicule

`GET /api/certificats/vehicle/{vehiculeId}`

### Description
Récupère la liste des certificats associés à un véhicule.

### Rôles autorisés
- AGENT_CERTIFICAT
- TAXATEUR
- RECEVEUR_DES_IMPOTS
- CHEF_DE_BUREAU
- CHEF_DE_DIVISION
- DIRECTEUR
- CONTRIBUABLE
- ADMIN

### Paramètres
- `vehiculeId` (chemin) - UUID du véhicule
- `page` (requête, optionnel, défaut: 0) - Numéro de page
- `size` (requête, optionnel, défaut: 10) - Nombre d'éléments par page

### Réponse
```json
{
  "success": true,
  "data": {
    "certificats": [
      {
        "id": "UUID",
        "numero": "string",
        "dateEmission": "ISO8601",
        "dateExpiration": "ISO8601",
        "type": "string",
        "statut": "string",
        "codeQR": "string",
        "vehicule": {
          "id": "UUID",
          "marque": "string",
          "modele": "string",
          "immatriculation": "string"
        },
        "agent": {
          "id": "UUID",
          "nom": "string"
        }
      }
    ],
    "currentPage": 0,
    "totalItems": 0,
    "totalPages": 0
  },
  "error": null,
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

### Réponse en cas d'erreur
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "CERTIFICATE_FETCH_ERROR",
    "message": "Erreur lors de la récupération des certificats",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer les certificats actifs

`GET /api/certificats/active`

### Description
Récupère la liste des certificats actifs (non expirés).

### Rôles autorisés
- AGENT_CERTIFICAT
- TAXATEUR
- RECEVEUR_DES_IMPOTS
- CHEF_DE_BUREAU
- CHEF_DE_DIVISION
- DIRECTEUR
- ADMIN

### Paramètres
- `page` (requête, optionnel, défaut: 0) - Numéro de page
- `size` (requête, optionnel, défaut: 10) - Nombre d'éléments par page

### Réponse
```json
{
  "success": true,
  "data": {
    "certificats": [
      {
        "id": "UUID",
        "numero": "string",
        "dateEmission": "ISO8601",
        "dateExpiration": "ISO8601",
        "type": "string",
        "statut": "string",
        "codeQR": "string",
        "declaration": {
          "id": "UUID",
          "dateDeclaration": "ISO8601",
          "montant": 0
        },
        "propriete": {
          "id": "UUID",
          "adresse": "string"
        },
        "vehicule": {
          "id": "UUID",
          "marque": "string",
          "modele": "string",
          "immatriculation": "string"
        },
        "agent": {
          "id": "UUID",
          "nom": "string"
        }
      }
    ],
    "currentPage": 0,
    "totalItems": 0,
    "totalPages": 0
  },
  "error": null,
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

### Réponse en cas d'erreur
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "CERTIFICATE_FETCH_ERROR",
    "message": "Erreur lors de la récupération des certificats",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer les certificats expirés

`GET /api/certificats/expired`

### Description
Récupère la liste des certificats expirés.

### Rôles autorisés
- AGENT_CERTIFICAT
- TAXATEUR
- RECEVEUR_DES_IMPOTS
- CHEF_DE_BUREAU
- CHEF_DE_DIVISION
- DIRECTEUR
- ADMIN

### Paramètres
- `page` (requête, optionnel, défaut: 0) - Numéro de page
- `size` (requête, optionnel, défaut: 10) - Nombre d'éléments par page

### Réponse
```json
{
  "success": true,
  "data": {
    "certificats": [
      {
        "id": "UUID",
        "numero": "string",
        "dateEmission": "ISO8601",
        "dateExpiration": "ISO8601",
        "type": "string",
        "statut": "string",
        "codeQR": "string",
        "declaration": {
          "id": "UUID",
          "dateDeclaration": "ISO8601",
          "montant": 0
        },
        "propriete": {
          "id": "UUID",
          "adresse": "string"
        },
        "vehicule": {
          "id": "UUID",
          "marque": "string",
          "modele": "string",
          "immatriculation": "string"
        },
        "agent": {
          "id": "UUID",
          "nom": "string"
        }
      }
    ],
    "currentPage": 0,
    "totalItems": 0,
    "totalPages": 0
  },
  "error": null,
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

### Réponse en cas d'erreur
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "CERTIFICATE_FETCH_ERROR",
    "message": "Erreur lors de la récupération des certificats",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

# API de Certificats Détaillés

Cette section décrit les nouveaux endpoints permettant d'accéder aux certificats avec des informations détaillées sur le contribuable, les propriétés/véhicules, l'agent, le paiement et la taxation.

## Structure du DTO CertificatDetailDTO

Le DTO `CertificatDetailDTO` contient les informations suivantes :

- **Informations de base du certificat** : id, numero, dateEmission, dateExpiration, montant, statut, actif, codeQR, motifAnnulation
- **Informations sur la déclaration** : id, dateDeclaration, statutDeclaration, numeroDeclaration
- **Informations sur le contribuable** : id, nom, adresses, téléphones, email, type, identifiant
- **Informations sur le véhicule (si applicable)** : id, immatriculation, marque, modèle, année, numeroChassis, genre, categorie, puissanceFiscale
- **Informations sur la propriété (si applicable)** : id, type, localite, rangLocalite, superficie, adresse
- **Informations sur l'agent** : id, nom, matricule, bureau
- **Informations sur le paiement** : id, datePaiement, montant, statut, reference, modePaiement
- **Informations sur la taxation** : id, dateTaxation, montant, exercice, statut, typeImpot, numeroTaxation

## Récupérer un certificat détaillé par ID

`GET /api/certificats/details/{id}`

### Description
Récupère un certificat détaillé par son ID avec toutes les informations associées sur le contribuable, les propriétés/véhicules, l'agent, le paiement et la taxation.

### Rôles autorisés
- AGENT_CERTIFICAT
- ADMIN

### Paramètres
- `id` (chemin) - UUID du certificat

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "certificatDetail": {
      "id": "UUID",
      "numero": "CERT-2025-00001",
      "dateEmission": "ISO8601",
      "dateExpiration": "ISO8601",
      "montant": 5000.0,
      "statut": "ACTIF",
      "actif": true,
      "codeQR": "data:image/png;base64,...",
      "motifAnnulation": null,
      "declarationId": "UUID",
      "dateDeclaration": "ISO8601",
      "statutDeclaration": "VALIDEE",
      "numeroDeclaration": "DECL-2025-00001",
      "contribuableId": "UUID",
      "contribuableNom": "Nom du contribuable",
      "contribuableAdressePrincipale": "Adresse principale",
      "contribuableAdresseSecondaire": "Adresse secondaire",
      "contribuableTelephonePrincipal": "+243123456789",
      "contribuableTelephoneSecondaire": "+243987654321",
      "contribuableEmail": "email@example.com",
      "contribuableType": "PERSONNE_PHYSIQUE",
      "contribuableIdentifiant": "CONT-12345",
      "vehiculeId": "UUID",
      "vehiculeImmatriculation": "ABC123",
      "vehiculeMarque": "Toyota",
      "vehiculeModele": "Corolla",
      "vehiculeAnnee": 2022,
      "vehiculeNumeroChassis": "12345678901234567",
      "vehiculeGenre": "VP",
      "vehiculeCategorie": "TOURISME",
      "vehiculePuissanceFiscale": 7.0,
      "agentId": "UUID",
      "agentNom": "Nom de l'agent",
      "agentMatricule": "AGENT-001",
      "agentBureau": "Bureau central",
      "paiementId": "UUID",
      "datePaiement": "ISO8601",
      "montantPaiement": 5000.0,
      "statutPaiement": "COMPLETE",
      "referencePaiement": "PAY-2025-00001",
      "modePaiement": "MOBILE_MONEY",
      "taxationId": "UUID",
      "dateTaxation": "ISO8601",
      "montantTaxation": 5000.0,
      "exercice": "2025",
      "statutTaxation": "PAYEE",
      "typeImpot": "VIGNETTE_AUTOMOBILE",
      "numeroTaxation": "TAX-2025-00001"
    }
  },
  "error": null,
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

### Réponse en cas d'erreur
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "CERTIFICAT_DETAIL_ERROR",
    "message": "Erreur lors de la récupération du certificat détaillé",
    "details": "string"
  },
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer tous les certificats détaillés

`GET /api/certificats/details`

### Description
Récupère la liste de tous les certificats détaillés avec les informations associées.

### Rôles autorisés
- AGENT_CERTIFICAT
- ADMIN

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "certificatDetails": [
      {
        "id": "UUID",
        "numero": "CERT-2025-00001",
        "dateEmission": "ISO8601",
        "dateExpiration": "ISO8601",
        "montant": 5000.0,
        "statut": "ACTIF",
        "contribuableNom": "Nom du contribuable",
        "contribuableAdressePrincipale": "Adresse principale",
        "vehiculeImmatriculation": "ABC123",
        "vehiculeMarque": "Toyota",
        "vehiculeModele": "Corolla",
        "agentNom": "Nom de l'agent",
        "montantPaiement": 5000.0,
        "statutPaiement": "COMPLETE"
      }
    ],
    "count": 1
  },
  "error": null,
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer les certificats détaillés d'un contribuable

`GET /api/certificats/details/contribuable/{contribuableId}`

### Description
Récupère les certificats détaillés d'un contribuable spécifique.

### Rôles autorisés
- AGENT_CERTIFICAT
- ADMIN

### Paramètres
- `contribuableId` (chemin) - UUID du contribuable

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "certificatDetails": [
      {
        "id": "UUID",
        "numero": "CERT-2025-00001",
        "dateEmission": "ISO8601",
        "dateExpiration": "ISO8601",
        "montant": 5000.0,
        "statut": "ACTIF",
        "contribuableNom": "Nom du contribuable",
        "contribuableAdressePrincipale": "Adresse principale",
        "vehiculeImmatriculation": "ABC123",
        "vehiculeMarque": "Toyota",
        "vehiculeModele": "Corolla",
        "agentNom": "Nom de l'agent",
        "montantPaiement": 5000.0,
        "statutPaiement": "COMPLETE"
      }
    ],
    "count": 1,
    "contribuableId": "UUID"
  },
  "error": null,
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer les certificats détaillés pour un véhicule

`GET /api/certificats/details/vehicule/{vehiculeId}`

### Description
Récupère les certificats détaillés pour un véhicule spécifique.

### Rôles autorisés
- AGENT_CERTIFICAT
- ADMIN

### Paramètres
- `vehiculeId` (chemin) - UUID du véhicule

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "certificatDetails": [
      {
        "id": "UUID",
        "numero": "CERT-2025-00001",
        "dateEmission": "ISO8601",
        "dateExpiration": "ISO8601",
        "montant": 5000.0,
        "statut": "ACTIF",
        "contribuableNom": "Nom du contribuable",
        "vehiculeImmatriculation": "ABC123",
        "vehiculeMarque": "Toyota",
        "vehiculeModele": "Corolla",
        "agentNom": "Nom de l'agent",
        "montantPaiement": 5000.0,
        "statutPaiement": "COMPLETE"
      }
    ],
    "count": 1,
    "vehiculeId": "UUID"
  },
  "error": null,
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer les certificats détaillés pour une propriété

`GET /api/certificats/details/propriete/{proprieteId}`

### Description
Récupère les certificats détaillés pour une propriété spécifique.

### Rôles autorisés
- AGENT_CERTIFICAT
- ADMIN

### Paramètres
- `proprieteId` (chemin) - UUID de la propriété

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "certificatDetails": [
      {
        "id": "UUID",
        "numero": "CERT-2025-00001",
        "dateEmission": "ISO8601",
        "dateExpiration": "ISO8601",
        "montant": 5000.0,
        "statut": "ACTIF",
        "contribuableNom": "Nom du contribuable",
        "proprieteType": "IMMEUBLE",
        "proprieteAdresse": "Adresse de la propriété",
        "agentNom": "Nom de l'agent",
        "montantPaiement": 5000.0,
        "statutPaiement": "COMPLETE"
      }
    ],
    "count": 1,
    "proprieteId": "UUID"
  },
  "error": null,
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Récupérer les certificats détaillés liés à une taxation

`GET /api/certificats/details/taxation/{taxationId}`

### Description
Récupère les certificats détaillés liés à une taxation spécifique.

### Rôles autorisés
- AGENT_CERTIFICAT
- ADMIN

### Paramètres
- `taxationId` (chemin) - UUID de la taxation

### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "certificatDetails": [
      {
        "id": "UUID",
        "numero": "CERT-2025-00001",
        "dateEmission": "ISO8601",
        "dateExpiration": "ISO8601",
        "montant": 5000.0,
        "statut": "ACTIF",
        "contribuableNom": "Nom du contribuable",
        "vehiculeImmatriculation": "ABC123",
        "agentNom": "Nom de l'agent",
        "montantPaiement": 5000.0,
        "statutPaiement": "COMPLETE",
        "taxationId": "UUID",
        "numeroTaxation": "TAX-2025-00001",
        "typeImpot": "VIGNETTE_AUTOMOBILE"
      }
    ],
    "count": 1,
    "taxationId": "UUID"
  },
  "error": null,
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Utilisation des données détaillées des certificats

Les données détaillées des certificats peuvent être utilisées pour :

1. **Générer des documents de certificat complets** incluant toutes les informations sur le contribuable, le véhicule/propriété, l'agent, le paiement et la taxation
2. **Afficher un tableau de bord de gestion des certificats** avec les informations complètes
3. **Faciliter le suivi des certificats** par contribuable, véhicule, propriété ou taxation
4. **Améliorer la vérification des certificats** en fournissant toutes les informations associées

## Classes et interfaces associées

- `CertificatDetailDTO` : DTO principal contenant toutes les informations détaillées
- `CertificatRepositoryCustom` : Interface définissant les méthodes personnalisées
- `CertificatRepositoryCustomImpl` : Implémentation des méthodes personnalisées
- `CertificatDetailService` : Service pour gérer les certificats détaillés
- `CertificatDetailController` : Contrôleur exposant les endpoints REST
