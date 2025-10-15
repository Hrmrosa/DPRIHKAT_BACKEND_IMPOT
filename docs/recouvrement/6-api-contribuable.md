# API des Données Contribuable

## 1. Récupération des détails complets

### Endpoint
```http
GET /api/contribuables/{id}/complet
```

### Réponse
```json
{
  "success": true,
  "data": {
    "id": "UUID",
    "nom": "SARL EXEMPLE",
    "matricule": "CONTRIB-001",
    "adressePrincipale": "123 Avenue du Commerce, Kinshasa",
    "telephonePrincipal": "+243123456789",
    "email": "contact@exemple.cd",
    "type": "PERSONNE_MORALE",
    "idNat": "ID-001",
    "nrc": "RCCM-001",
    "numeroIdentificationContribuable": "A001",
    "commercant": true,
    "proprietes": [
      {
        "id": "UUID",
        "adresse": "123 Avenue du Commerce, Kinshasa",
        "reference": "PROP-001",
        "superficie": 500.0,
        "usage": "COMMERCIAL",
        "valeurLocative": 5000000.0,
        "montantImpot": 250000.0
      }
    ],
    "vehicules": [
      {
        "id": "UUID",
        "immatriculation": "ABC123",
        "marque": "Toyota",
        "modele": "Land Cruiser",
        "chassis": "CHAS001",
        "annee": 2020,
        "type": "UTILITAIRE",
        "cylindree": 4500.0,
        "montantVignette": 150000.0
      }
    ],
    "declarations": [
      {
        "id": "UUID",
        "dateDeclaration": "2025-01-01",
        "typeImpot": "IBP",
        "montantDeclare": 1000000.0,
        "statut": "VALIDEE",
        "reference": "DEC-001",
        "exercice": "2024",
        "periode": "ANNUEL",
        "taxateur": {
          "id": "UUID",
          "nom": "Jean TAXATEUR",
          "matricule": "TAX001"
        }
      }
    ],
    "documentsRecouvrement": [
      {
        "id": "UUID",
        "type": "AMR",
        "statut": "NOTIFIE",
        "reference": "AMR-001",
        "montantTotal": 1020000.0,
        "dateEcheance": "2025-01-16"
      }
    ],
    "relances": [
      {
        "id": "UUID",
        "dateRelance": "2025-01-05",
        "type": "COURRIER",
        "contenu": "Rappel de paiement AMR-001",
        "statut": "ENVOYEE",
        "agent": {
          "id": "UUID",
          "nom": "Paul AGENT",
          "matricule": "AG002"
        }
      }
    ]
  }
}
```

## 2. Valeurs possibles

### TypeDocumentRecouvrement
- AMR (Avis de Mise en Recouvrement)
- MED (Mise En Demeure de payer)
- COMMANDEMENT (Commandement de payer)
- CONTRAINTE (Contrainte fiscale)
- ATD (Avis à Tiers Détenteur)
- SAISIE_MOBILIERE (Saisie mobilière)
- SAISIE_IMMOBILIERE (Saisie immobilière)
- FERMETURE_ETABLISSEMENT (Fermeture d'établissement)
- AVIS_NON_LIEU (Avis de non-lieu)
- AVIS_REDRESSEMENT (Avis de redressement)

### StatutDocumentRecouvrement
- GENERE (Généré)
- NOTIFIE (Notifié)
- EN_COURS_EXECUTION (En cours d'exécution)
- EXECUTE (Exécuté)
- NON_EXECUTE (Non exécuté)
- ANNULE (Annulé)

### TypeRedressement
- REDRESSEMENT_SIMPLE (Redressement simple)
- TAXATION_OFFICE (Taxation d'office)
- AVIS_NON_LIEU (Avis de non-lieu)

### OrigineControle
- CONTROLE_SUR_PIECES (Contrôle sur pièces)
- RECHERCHE_RECOUPEMENT (Recherche et recoupement)
- CONTROLE_SUR_PLACE (Contrôle sur place)
- DECLARATION_SPONTANEE (Déclaration spontanée)
