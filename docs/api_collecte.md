# Documentation API - Gestion de la Collecte

Cette documentation détaille les endpoints disponibles pour la collecte des données fiscales dans l'API DPRIHKAT.

---

## Vue d'ensemble

La collecte permet de récupérer et d'enrichir les données des propriétés avec leurs déclarations et paiements associés.

### Base URL
```
/api/collectes
```
 
---

## Endpoints

### 1. Récupérer toutes les collectes

Récupère toutes les propriétés avec leurs informations de déclaration et paiement.

- **URL**: `/api/collectes`
- **Méthode**: `GET`
- **Rôles autorisés**: `CONTROLLEUR`, `ADMIN`, `INFORMATICIEN`
- **Paramètres**:
  - `page` (query, optionnel): Numéro de page (défaut: 0)
  - `size` (query, optionnel): Taille de page (défaut: 10)

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "data": {
      "totalItems": 150,
      "totalPages": 15,
      "currentPage": 0,
      "proprietes": [
        {
          "propriete": {
            "id": "550e8400-e29b-41d4-a716-446655440000",
            "adresse": "Avenue de la Liberté, Lubumbashi",
            "type": "VI",
            "superficie": 500.0,
            "montantImpot": 75000.0
          },
          "contribuable": {
            "id": "550e8400-e29b-41d4-a716-446655440001",
            "nom": "KABILA",
            "numeroIdentificationContribuable": "NIF-123456789"
          },
          "declarations": [
            {
              "id": "550e8400-e29b-41d4-a716-446655440010",
              "dateDeclaration": "2024-01-15T10:00:00",
              "exercice": 2024,
              "statut": "VALIDEE"
            }
          ],
          "taxations": [
            {
              "id": "550e8400-e29b-41d4-a716-446655440020",
              "montant": 75000.0,
              "montantPaye": 75000.0,
              "solde": 0.0,
              "statut": "PAYEE"
            }
          ],
          "paiements": [
            {
              "id": "550e8400-e29b-41d4-a716-446655440030",
              "montant": 75000.0,
              "datePaiement": "2024-03-15T14:30:00",
              "modePaiement": "ESPECES"
            }
          ],
          "statistiques": {
            "totalTaxations": 75000.0,
            "totalPaiements": 75000.0,
            "soldeRestant": 0.0,
            "tauxRecouvrement": 100.0
          }
        }
      ]
    }
  }
}
```

---

### 2. Récupérer les collectes d'un contribuable

Récupère les propriétés d'un contribuable spécifique avec leurs informations enrichies.

- **URL**: `/api/collectes/contribuable/{contribuableId}`
- **Méthode**: `GET`
- **Rôles autorisés**: `CONTROLLEUR`, `ADMIN`, `INFORMATICIEN`, `CONTRIBUABLE`
- **Paramètres**:
  - `contribuableId` (path): UUID du contribuable

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "proprietes": [
      {
        "propriete": {
          "id": "550e8400-e29b-41d4-a716-446655440000",
          "adresse": "Avenue de la Liberté",
          "type": "VI",
          "superficie": 500.0
        },
        "declarations": [...],
        "taxations": [...],
        "paiements": [...],
        "statistiques": {
          "totalTaxations": 75000.0,
          "totalPaiements": 75000.0,
          "soldeRestant": 0.0,
          "tauxRecouvrement": 100.0
        }
      }
    ]
  }
}
```

---

## Données enrichies

### Informations incluses

Pour chaque propriété, le système retourne :

1. **Propriété**
   - Informations de base (adresse, type, superficie)
   - Montant de l'impôt calculé
   - Statut de déclaration

2. **Contribuable (Propriétaire)**
   - Informations complètes du contribuable
   - Numéro d'identification fiscale (NIF)
   - Nom, adresse, contact

3. **Déclarations**
   - Historique des déclarations
   - Dates et statuts
   - Exercices fiscaux

4. **Taxations**
   - Taxations émises
   - Montants et soldes
   - Statuts de paiement

5. **Paiements**
   - Historique des paiements
   - Montants et dates
   - Modes de paiement

6. **Statistiques**
   - Total des taxations
   - Total des paiements
   - Solde restant
   - Taux de recouvrement (%)

---

## Règles métier

### Enrichissement des données
- Les données sont enrichies en temps réel
- Calcul automatique des statistiques
- Agrégation des montants par propriété

### Taux de recouvrement
- Formule: `(totalPaiements / totalTaxations) × 100`
- Arrondi à 2 décimales
- 0% si aucune taxation

### Accès contribuable
- Un contribuable ne peut voir que ses propres données
- Vérification automatique de l'identité
- Filtrage par contribuable authentifié

### Pagination
- Recommandé pour les grandes listes
- Améliore les performances
- Limite par défaut: 10 éléments

---

## Cas d'usage

### 1. Contrôle fiscal
Les contrôleurs utilisent cet endpoint pour :
- Vérifier la conformité des déclarations
- Identifier les propriétés non déclarées
- Analyser les taux de recouvrement

### 2. Suivi contribuable
Les contribuables peuvent :
- Consulter leurs propriétés
- Voir l'historique des paiements
- Vérifier leur situation fiscale

### 3. Reporting
Les administrateurs utilisent ces données pour :
- Générer des rapports statistiques
- Analyser les tendances de paiement
- Identifier les zones à risque

---

## Codes d'erreur

| Code | Description |
|------|-------------|
| COLLECTE_FETCH_ERROR | Erreur récupération collectes |
| COLLECTE_CONTRIBUABLE_FETCH_ERROR | Erreur récupération collectes contribuable |
| CONTRIBUABLE_NOT_FOUND | Contribuable non trouvé |
| UNAUTHORIZED_ACCESS | Accès non autorisé |
