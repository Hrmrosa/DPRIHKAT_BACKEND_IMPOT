# Documentation API - Gestion des Paiements

Cette documentation détaille les endpoints disponibles pour la gestion des paiements dans l'API DPRIHKAT.

---

## Vue d'ensemble

Les paiements permettent aux contribuables de régler leurs taxations.

### Base URL
```
/api/paiements
```

--- 

## Endpoints

### 1. Récupérer tous les paiements

- **URL**: `/api/paiements`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`, `CONTROLLEUR`, `RECEVEUR_DES_IMPOTS`

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "paiements": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "contribuable": {
          "id": "550e8400-e29b-41d4-a716-446655440001",
          "nom": "KABILA",
          "numeroIdentificationContribuable": "NIF-123456789"
        },
        "taxation": {
          "id": "550e8400-e29b-41d4-a716-446655440010",
          "natureImpot": {
            "code": "IF",
            "libelle": "Impôt Foncier"
          },
          "montant": 75000.0
        },
        "montant": 75000.0,
        "datePaiement": "2024-03-15T14:30:00",
        "modePaiement": "ESPECES",
        "numeroBordereau": "BOR-2024-001",
        "statut": "VALIDE"
      }
    ]
  }
}
```

---

### 2. Récupérer un paiement par ID

- **URL**: `/api/paiements/{id}`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`, `CONTROLLEUR`, `RECEVEUR_DES_IMPOTS`, `CONTRIBUABLE`

---

### 3. Créer un paiement

Enregistre un nouveau paiement pour une taxation.

- **URL**: `/api/paiements`
- **Méthode**: `POST`
- **Rôles autorisés**: `ADMIN`, `RECEVEUR_DES_IMPOTS`

#### Corps de la requête

```json
{
  "taxationId": "550e8400-e29b-41d4-a716-446655440010",
  "montant": 75000.0,
  "modePaiement": "ESPECES",
  "numeroBordereau": "BOR-2024-001",
  "dateOperation": "2024-03-15"
}
```

#### Champs obligatoires
- `taxationId`: UUID de la taxation à payer
- `montant`: Montant du paiement
- `modePaiement`: Mode de paiement
- `numeroBordereau`: Numéro du bordereau (pour traçabilité)

---

### 4. Récupérer les paiements d'un contribuable

- **URL**: `/api/paiements/contribuable/{contribuableId}`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`, `CONTROLLEUR`, `RECEVEUR_DES_IMPOTS`, `CONTRIBUABLE`

---

### 5. Récupérer mes paiements (contribuable)

- **URL**: `/api/paiements/mine`
- **Méthode**: `GET`
- **Rôles autorisés**: `CONTRIBUABLE`

---

### 6. Valider un paiement

Valide un paiement en attente.

- **URL**: `/api/paiements/{id}/valider`
- **Méthode**: `POST`
- **Rôles autorisés**: `ADMIN`, `RECEVEUR_DES_IMPOTS`, `CHEF_DE_BUREAU`

---

### 7. Annuler un paiement

Annule un paiement.

- **URL**: `/api/paiements/{id}/annuler`
- **Méthode**: `POST`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`

---

## Structure de l'entité Paiement

| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant unique |
| contribuable | Contribuable | Contribuable payeur |
| taxation | Taxation | Taxation payée |
| montant | Double | Montant du paiement |
| datePaiement | DateTime | Date du paiement |
| modePaiement | Enum | Mode de paiement |
| numeroBordereau | String | Numéro de bordereau |
| statut | Enum | Statut du paiement |
| receveur | Utilisateur | Agent receveur |

---

## Énumérations

### ModePaiement
- `ESPECES`: Paiement en espèces
- `CHEQUE`: Paiement par chèque
- `VIREMENT`: Virement bancaire
- `MOBILE_MONEY`: Paiement mobile

### StatutPaiement
- `EN_ATTENTE`: En attente de validation
- `VALIDE`: Validé
- `ANNULE`: Annulé

---

## Règles métier

### Validation
- Un paiement doit être validé par un receveur
- La validation met à jour automatiquement la taxation

### Montant
- Le montant ne peut pas dépasser le solde de la taxation
- Paiements partiels autorisés

### Traçabilité
- Chaque paiement a un numéro de bordereau unique
- L'agent receveur est enregistré automatiquement

### Annulation
- Seuls les administrateurs peuvent annuler un paiement
- L'annulation restaure le solde de la taxation
