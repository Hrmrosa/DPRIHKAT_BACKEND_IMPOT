# Gestion des Plaques d'Immatriculation

## 1. Vue d'Ensemble

Le système de gestion des plaques permet :
- L'attribution de nouvelles plaques
- La gestion des demandes de plaques
- La génération automatique de vignettes
- L'intégration avec le système de taxation

## 2. Modèle de Données

### 2.1 Entités Principales

#### Plaque
```java
class Plaque {
    UUID id;
    String numero;
    StatutPlaque statut; // STOCK, ATTRIBUEE, LIVREE
    Vehicule vehicule;
    Vignette vignette;
}
```

#### DemandePlaque
```java
class DemandePlaque {
    UUID id;
    StatutDemande statut; // SOUMISE, VALIDEE, PAYEE, LIVREE
    Date dateDemande;
    String facturePath;
    Contribuable demandeur;
    Vehicule vehicule;
    Taxation taxation;
    Plaque plaque;
}
```

## 3. Workflows

### 3.1 Nouvelle Demande (Contribuable)
1. Soumission du formulaire avec pièces jointes
2. Vérification par l'administration
3. Validation et création de la taxation
4. Paiement
5. Attribution de la plaque

### 3.2 Création Directe (Admin)
1. Vérification des droits
2. Création immédiate de la plaque
3. Génération de la vignette

## 4. API Principale

### 4.1 Créer une Demande
```
POST /api/demandes-plaque/soumettre
Content-Type: multipart/form-data
Authorization: Bearer {token}

vehiculeId=123e4567-e89b-12d3-a456-426614174000&facture=@facture.pdf
```

### 4.2 Valider une Demande (Admin)
```
POST /api/demandes-plaque/{id}/valider
Authorization: Bearer {token}
```

### 4.3 Attribuer une Plaque
```
POST /api/plaques/assign/{vehiculeId}
Content-Type: multipart/form-data
Authorization: Bearer {token}

document=@justificatif.pdf
```

## 5. Règles Métier

### 5.1 Validation des Demandes
- Un véhicule ne peut avoir qu'une plaque active
- La facture doit être au format PDF
- Seul l'admin peut valider les demandes

### 5.2 Génération des Vignettes
- Durée de validité : 1 an
- Format : PDF avec QR code
- Inclut les informations du véhicule et du propriétaire

## 6. Exemples

### Réponse de Succès
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174001",
  "statut": "VALIDE",
  "plaque": {
    "numero": "AB123CD",
    "dateCreation": "2025-09-15T19:00:00Z"
  },
  "vignette": {
    "numero": "VIG-5A6B7C8D",
    "expiration": "2026-09-15"
  }
}
```

## 7. Gestion des Erreurs

| Code | Message | Cause Possible |
|------|---------|----------------|
| 400 | Données invalides | Champs manquants ou incorrects |
| 403 | Accès refusé | Droits insuffisants |
| 404 | Non trouvé | Ressource inexistante |
| 409 | Conflit | Plaque déjà attribuée |
