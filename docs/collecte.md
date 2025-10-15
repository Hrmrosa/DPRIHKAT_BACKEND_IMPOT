# API de Collecte

## Fonctionnalités Avancées

### Récupération des Collectes avec Déclarations et Paiements

`GET /api/collectes`

#### Description
Récupère toutes les propriétés avec leurs déclarations et paiements associés en une seule requête.

#### Paramètres
- `page` (optionnel) : Numéro de page (commence à 0, par défaut 0)
- `size` (optionnel) : Nombre d'éléments par page (par défaut 10)

#### Rôles autorisés
- CONTROLLEUR
- ADMIN
- INFORMATICIEN

#### Réponse
```json
{
  "success": true,
  "data": {
    "totalItems": 120,
    "totalPages": 12,
    "currentPage": 0,
    "proprietes": [
      {
        "propriete": {
          "id": "UUID",
          "type": "VI",
          "localite": "Kinshasa",
          "rangLocalite": 1,
          "superficie": 500.0,
          "adresse": "123 Avenue Lumumba",
          "proprietaire": {
            "id": "UUID",
            "nom": "Entreprise XYZ"
          }
        },
        "aDeclare": true,
        "paiementComplet": false,
        "paiementPartiel": true,
        "declarations": [
          {
            "id": "UUID",
            "date": "2025-01-15",
            "statut": "VALIDEE",
            "paiement": {
              "id": "UUID",
              "statut": "EN_ATTENTE",
              "montant": 1500.0
            }
          }
        ]
      }
    ]
  }
}
```

### Récupération des Collectes par Contribuable

`GET /api/collectes/contribuable/{contribuableId}`

#### Description
Récupère toutes les propriétés d'un contribuable spécifique avec leurs déclarations et paiements associés.

#### Paramètres
- `contribuableId` : UUID du contribuable

#### Rôles autorisés
- CONTROLLEUR
- ADMIN
- INFORMATICIEN
- CONTRIBUABLE

#### Réponse
```json
{
  "success": true,
  "proprietes": [
    {
      "propriete": {
        "id": "UUID",
        "type": "VI",
        "localite": "Kinshasa",
        "rangLocalite": 1,
        "superficie": 500.0,
        "adresse": "123 Avenue Lumumba"
      },
      "aDeclare": true,
      "paiementComplet": false,
      "paiementPartiel": true,
      "declarations": [
        {
          "id": "UUID",
          "date": "2025-01-15",
          "statut": "VALIDEE",
          "paiement": {
            "id": "UUID",
            "statut": "EN_ATTENTE",
            "montant": 1500.0
          }
        }
      ]
    }
  ]
}
```

### Suivi des Déclarations et Paiements (Ancienne méthode)

`GET /api/collecte/contribuables/{id}/etat-declarations`

#### Description
Récupère l'état des déclarations et paiements pour un contribuable donné.

#### Paramètres
- `id` : UUID du contribuable
- `annee` (optionnel) : Année de référence (par défaut année en cours)

#### Réponse
```json
{
  "aDeclare": true,
  "paiementComplet": false,
  "paiementPartiel": true,
  "declarations": [
    {
      "id": "UUID",
      "date": "2025-01-15",
      "statut": "VALIDEE",
      "paiement": {
        "id": "UUID",
        "statut": "EN_ATTENTE",
        "montant": 1500.0
      }
    }
  ]
}
```

## Création de contribuable avec biens

`POST /api/collecte/contribuables`

### Description
Crée un nouveau contribuable avec ses biens associés.

### Rôles autorisés
- CONTROLLEUR
- ADMIN
- INFORMATICIEN

### Corps de la requête
```json
{
  "nom": "string",
  "adressePrincipale": "string",
  "adresseSecondaire": "string",
  "telephonePrincipal": "string",
  "telephoneSecondaire": "string",
  "email": "string",
  "nationalite": "string",
  "type": "string",
  "idNat": "string",
  "nrc": "string",
  "sigle": "string",
  "numeroIdentificationContribuable": "string",
  "biens": [
    {
      "type": "string",
      "localite": "string",
      "rangLocalite": 0,
      "superficie": 0,
      "adresse": "string",
      "latitude": 0,
      "longitude": 0
    }
  ]
}
```

## Valeurs possibles

### StatutDeclaration
- SOUMISE
- VALIDEE
- REJETEE

### StatutPaiement
- EN_ATTENTE
- VALIDE
- ANNULE
- COMPLETE
- PARTIEL

### TypeContribuable
- PERSONNE_PHYSIQUE
- PERSONNE_MORALE

### TypePropriete
- AP
- VI
- AT
- CITERNE
- DEPOT
- CH
- TE
