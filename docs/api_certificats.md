# Documentation API - Gestion des Certificats

Cette documentation détaille les endpoints disponibles pour la gestion des certificats fiscaux dans l'API DPRIHKAT.

---

## Vue d'ensemble

Les certificats fiscaux attestent que le contribuable est en règle avec ses obligations fiscales.
 
### Base URL
```
/api/certificats
```

---

## Endpoints

### 1. Émettre un certificat

Émet un nouveau certificat fiscal pour un contribuable.

- **URL**: `/api/certificats/emettre`
- **Méthode**: `POST`
- **Rôles autorisés**: `RECEVEUR_DES_IMPOTS`, `APUREUR`
- **Paramètres**: Aucun (utilise l'authentification)

#### Réponse en cas de succès

```json
{
  "success": true,
  "message": "Certificat émis avec succès",
  "data": {
    "numeroCertificat": "CERT-2024-001",
    "contribuable": {
      "id": "550e8400-e29b-41d4-a716-446655440001",
      "nom": "KABILA JOSEPH LAURENT",
      "numeroIdentificationContribuable": "NIF-123456789"
    },
    "dateEmission": "2024-10-27T12:00:00",
    "dateExpiration": "2025-10-27",
    "statut": "VALIDE",
    "agent": {
      "id": "550e8400-e29b-41d4-a716-446655440010",
      "nomComplet": "MUKENDI Jean",
      "grade": "Receveur des Impôts"
    }
  }
}
```

---

### 2. Vérifier un certificat

Vérifie la validité d'un certificat fiscal.

- **URL**: `/api/certificats/verifier/{numeroCertificat}`
- **Méthode**: `GET`
- **Rôles autorisés**: Tous (public)
- **Paramètres**:
  - `numeroCertificat` (path): Numéro du certificat

#### Réponse en cas de succès

```json
{
  "success": true,
  "message": "Vérification effectuée",
  "data": {
    "valide": true,
    "certificat": {
      "numeroCertificat": "CERT-2024-001",
      "contribuable": {
        "nom": "KABILA JOSEPH LAURENT",
        "numeroIdentificationContribuable": "NIF-123456789"
      },
      "dateEmission": "2024-10-27T12:00:00",
      "dateExpiration": "2025-10-27",
      "statut": "VALIDE"
    }
  }
}
```

#### Réponse si certificat invalide

```json
{
  "success": true,
  "message": "Vérification effectuée",
  "data": {
    "valide": false,
    "raison": "Certificat expiré ou inexistant"
  }
}
```

---

## Structure de l'entité Certificat

| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant unique |
| numeroCertificat | String | Numéro unique du certificat |
| contribuable | Contribuable | Contribuable concerné |
| dateEmission | DateTime | Date d'émission |
| dateExpiration | Date | Date d'expiration |
| statut | Enum | Statut du certificat |
| agent | Utilisateur | Agent émetteur |
| motif | String | Motif de l'émission |

---

## Énumérations

### StatutCertificat
- `VALIDE`: Certificat valide
- `EXPIRE`: Certificat expiré
- `REVOQUE`: Certificat révoqué
- `ANNULE`: Certificat annulé

---

## Règles métier

### Conditions d'émission
- Le contribuable doit être à jour de tous ses paiements
- Aucune taxation en cours avec solde impayé
- Aucun dossier de recouvrement ouvert
- Vérification automatique avant émission

### Période de validité
- Durée: 1 an à partir de la date d'émission
- Renouvellement possible avant expiration
- Vérification de la situation fiscale à chaque renouvellement

### Agents autorisés
- **Receveur des Impôts**: Peut émettre tous types de certificats
- **Apureur**: Peut émettre des certificats d'apurement

### Révocation
- Un certificat peut être révoqué si la situation fiscale change
- Notification automatique au contribuable
- Mise à jour du statut dans le système

### Vérification publique
- Accessible sans authentification
- Permet aux tiers de vérifier la validité
- Retourne uniquement les informations publiques
