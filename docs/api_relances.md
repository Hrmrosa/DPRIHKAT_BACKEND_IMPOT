# Documentation API - Gestion des Relances

Cette documentation détaille les endpoints disponibles pour la gestion des relances dans l'API DPRIHKAT.

---

## Vue d'ensemble

Les relances permettent de notifier les contribuables de leurs impayés et de les inciter au paiement.

### Base URL
```
/api/relances
```

---

## Endpoints
 
### 1. Récupérer toutes les relances

- **URL**: `/api/relances`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`, `CONTROLLEUR`

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "relances": [
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
          "montant": 75000.0,
          "solde": 75000.0
        },
        "numeroRelance": "REL-2024-001",
        "typeRelance": "PREMIERE_RELANCE",
        "dateRelance": "2024-03-01T10:00:00",
        "dateEcheance": "2024-03-15",
        "statut": "ENVOYEE",
        "modeEnvoi": "EMAIL"
      }
    ]
  }
}
```

---

### 2. Récupérer une relance par ID

- **URL**: `/api/relances/{id}`
- **Méthode**: `GET`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`, `CONTROLLEUR`, `CHEF_DE_BUREAU`

---

### 3. Créer une relance

Crée une nouvelle relance pour un contribuable.

- **URL**: `/api/relances`
- **Méthode**: `POST`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`, `CONTROLLEUR`

#### Corps de la requête

```json
{
  "contribuableId": "550e8400-e29b-41d4-a716-446655440001",
  "taxationId": "550e8400-e29b-41d4-a716-446655440010",
  "typeRelance": "PREMIERE_RELANCE",
  "dateEcheance": "2024-03-15",
  "modeEnvoi": "EMAIL",
  "message": "Veuillez procéder au paiement de votre impôt foncier."
}
```

---

### 4. Mettre à jour une relance

- **URL**: `/api/relances/{id}`
- **Méthode**: `PUT`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`, `CONTROLLEUR`

---

### 5. Supprimer une relance

- **URL**: `/api/relances/{id}`
- **Méthode**: `DELETE`
- **Rôles autorisés**: `ADMIN`, `DIRECTEUR`, `INFORMATICIEN`

---

## Structure de l'entité Relance

| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant unique |
| contribuable | Contribuable | Contribuable concerné |
| taxation | Taxation | Taxation impayée |
| numeroRelance | String | Numéro unique de relance |
| typeRelance | Enum | Type de relance |
| dateRelance | DateTime | Date d'envoi |
| dateEcheance | Date | Date limite de paiement |
| statut | Enum | Statut de la relance |
| modeEnvoi | Enum | Mode d'envoi |
| message | String | Message personnalisé |

---

## Énumérations

### TypeRelance
- `PREMIERE_RELANCE`: Première relance (amiable)
- `DEUXIEME_RELANCE`: Deuxième relance
- `TROISIEME_RELANCE`: Troisième relance
- `MISE_EN_DEMEURE`: Mise en demeure (dernière relance)

### StatutRelance
- `PROGRAMMEE`: Programmée
- `ENVOYEE`: Envoyée
- `RECUE`: Reçue par le contribuable
- `PAYEE`: Paiement effectué suite à la relance
- `EXPIREE`: Échéance dépassée sans paiement

### ModeEnvoi
- `EMAIL`: Par email
- `SMS`: Par SMS
- `COURRIER`: Par courrier postal
- `NOTIFICATION`: Notification dans l'application

---

## Règles métier

### Calendrier des relances
- **Première relance**: 15 jours après la date limite
- **Deuxième relance**: 30 jours après la première
- **Troisième relance**: 15 jours après la deuxième
- **Mise en demeure**: 15 jours après la troisième

### Génération automatique
- Les relances peuvent être générées automatiquement par le système
- Basées sur les taxations impayées
- Envoi programmé selon le calendrier

### Suivi
- Chaque relance est tracée dans l'historique
- Le statut est mis à jour automatiquement
- Notification à l'agent en cas de paiement
