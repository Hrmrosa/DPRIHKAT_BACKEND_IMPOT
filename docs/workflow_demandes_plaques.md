# Workflow Complet des Demandes de Plaques d'Immatriculation

## Vue d'ensemble

Ce document décrit le workflow complet de gestion des demandes de plaques d'immatriculation dans le système DPRIHKAT, de la soumission initiale jusqu'à la livraison de la plaque.

## Statuts des Demandes

Les demandes de plaques passent par les statuts suivants :

1. **SOUMISE** - La demande a été soumise par le contribuable
2. **VALIDEE** - La demande a été validée par un taxateur
3. **TAXEE** - Les notes de taxation ont été générées
4. **PAYEE** - Le paiement a été effectué
5. **APUREE** - La demande a été apurée et vérifiée
6. **LIVREE** - La plaque a été livrée au contribuable
7. **REJETEE** - La demande a été rejetée
 
## Workflow Détaillé

### 1. Soumission de la Demande

**Endpoint:** `POST /api/demandes-plaque/creer-vehicule-et-demander`

**Rôles autorisés:** CONTRIBUABLE, AGENT_DE_PLAQUES, TAXATEUR, ADMIN

**Paramètres:**
- `marque` (String) - Marque du véhicule
- `modele` (String) - Modèle du véhicule
- `annee` (Integer) - Année du véhicule
- `numeroChassis` (String) - Numéro de châssis
- `genre` (String) - Genre du véhicule
- `categorie` (String, optionnel) - Catégorie du véhicule
- `puissanceFiscale` (Double, optionnel) - Puissance fiscale
- `couleur` (String, optionnel) - Couleur du véhicule
- `nombrePlaces` (Integer, optionnel) - Nombre de places
- `contribuableId` (UUID) - ID du contribuable
- `facture` (MultipartFile) - Facture d'achat du véhicule

**Processus:**
1. Vérification du numéro de châssis (pas de doublon)
2. Création du véhicule avec immatriculation temporaire (TEMP-XXXXXXXX)
3. Création de la demande de plaque
4. **Génération automatique de 2 notes de taxation:**
   - **Note 1: Plaque d'immatriculation - 40 USD**
   - **Note 2: Vignette (moto/tricycle) - 10 USD**
5. Statut de la demande: **TAXEE**
6. Statut du véhicule: **TAXE**

**Réponse:**
```json
{
  "success": true,
  "data": {
    "vehicule": {
      "id": "uuid",
      "marque": "TVS",
      "modele": "TVS 150",
      "immatriculation": "TEMP-A1B2C3D4",
      "statut": "TAXE"
    },
    "demande": {
      "demandeId": "uuid",
      "statut": "TAXEE",
      "dateDemande": "2025-10-31T10:00:00",
      "contribuableNom": "KABONGO Jean",
      "contribuableNRC": "NRC123456",
      "vehiculeMarque": "TVS",
      "vehiculeModele": "TVS 150",
      "notePlaque": {
        "id": "uuid",
        "numeroTaxation": "PLAQ_A1B2C3D4_ABC123_2025",
        "montant": 40.0,
        "devise": "USD",
        "typeImpot": "PLAQUE",
        "dateEcheance": "2025-11-30T00:00:00",
        "nomBanque": "RAWBANK",
        "numeroCompte": "CD59 0000 0000 0000 0000 0001",
        "intituleCompte": "DPRIHKAT - PLAQUES D'IMMATRICULATION",
        "contribuableNom": "KABONGO Jean",
        "contribuableNRC": "NRC123456",
        "vehiculeMarque": "TVS",
        "vehiculeModele": "TVS 150"
      },
      "noteVignette": {
        "id": "uuid",
        "numeroTaxation": "VIG_E5F6G7H8_ABC123_2025",
        "montant": 10.0,
        "devise": "USD",
        "typeImpot": "IRV",
        "dateEcheance": "2025-11-30T00:00:00",
        "nomBanque": "RAWBANK",
        "numeroCompte": "CD59 0000 0000 0000 0000 0002",
        "intituleCompte": "DPRIHKAT - VIGNETTES VEHICULES"
      },
      "message": "Demande de plaque soumise avec succès. Deux notes de taxation ont été générées : Plaque (40 USD) et Vignette (10 USD). Total à payer: 50 USD"
    }
  }
}
```

### 2. Consultation des Demandes

#### 2.1 Toutes les demandes
**Endpoint:** `GET /api/demandes-plaque`

**Rôles:** AGENT_DE_PLAQUES, TAXATEUR, ADMIN

#### 2.2 Demandes par statut
**Endpoint:** `GET /api/demandes-plaque/statut/{statut}`

**Rôles:** AGENT_DE_PLAQUES, TAXATEUR, ADMIN

**Exemples:**
- `/api/demandes-plaque/statut/TAXEE` - Toutes les demandes taxées
- `/api/demandes-plaque/statut/PAYEE` - Toutes les demandes payées
- `/api/demandes-plaque/statut/APUREE` - Toutes les demandes apurées

**Réponse:**
```json
{
  "success": true,
  "data": {
    "demandes": [...],
    "count": 15,
    "statut": "TAXEE"
  }
}
```

#### 2.3 Demandes d'un contribuable
**Endpoint:** `GET /api/demandes-plaque/contribuable/{contribuableId}`

**Rôles:** CONTRIBUABLE, AGENT_DE_PLAQUES, TAXATEUR, ADMIN

#### 2.4 Demandes d'un contribuable par statut
**Endpoint:** `GET /api/demandes-plaque/contribuable/{contribuableId}/statut/{statut}`

**Rôles:** CONTRIBUABLE, AGENT_DE_PLAQUES, TAXATEUR, ADMIN

**Exemple:**
- `/api/demandes-plaque/contribuable/uuid/statut/PAYEE`

### 3. Impression des Notes de Taxation

Les notes de taxation retournées contiennent toutes les informations nécessaires pour l'impression :

**Informations de la note:**
- Numéro de taxation
- Date de taxation
- Date d'échéance (30 jours)
- Montant et devise
- Type d'impôt (PLAQUE ou IRV)

**Informations du contribuable:**
- Nom complet
- NRC (Numéro de Registre de Commerce)
- ID National
- Adresse
- Téléphone
- Email

**Informations du véhicule:**
- Marque et modèle
- Année
- Numéro de châssis
- Genre et catégorie
- Puissance fiscale

**Informations bancaires:**
- Nom de la banque
- Numéro de compte
- Intitulé du compte

**Informations de l'agent (si disponible):**
- Nom de l'agent taxateur
- Matricule
- Bureau
- Division

### 4. Paiement

**Endpoint:** `POST /api/demandes-plaque/{id}/marquer-paye`

**Rôles:** AGENT_DE_PLAQUES, TAXATEUR, ADMIN

**Processus:**
1. Vérification que la demande est en statut TAXEE
2. Mise à jour du statut: **PAYEE**
3. Enregistrement de la date de paiement
4. Mise à jour du statut du véhicule: **PAYE**
5. Envoi d'un email de notification au contribuable

### 5. Attribution de la Plaque

**Endpoint:** `POST /api/demandes-plaque/{id}/attribuer-plaque`

**Rôles:** AGENT_DE_PLAQUES, ADMIN

**Paramètres:**
- `numeroPlaque` (String) - Numéro de plaque à attribuer
- `agentId` (UUID) - ID de l'agent qui attribue

**Processus:**
1. Vérification que la demande est en statut PAYEE
2. Vérification que le numéro de plaque n'existe pas déjà
3. Création de la plaque
4. Mise à jour de l'immatriculation du véhicule (remplace TEMP-XXXXXXXX)
5. Mise à jour du statut du véhicule: **PLAQUE_ATTRIBUEE**
6. Mise à jour du statut de la demande: **VALIDEE**
7. Envoi d'un email de notification au contribuable

### 6. Apurement et Livraison

**Endpoint:** `POST /api/demandes-plaque/{id}/apurer-et-livrer`

**Rôles:** AGENT_DE_PLAQUES, ADMIN

**Paramètres:**
- `numeroPlaque` (String) - Numéro de plaque

**Processus:**
1. Vérification que la demande est en statut PAYEE
2. Attribution de la plaque au véhicule
3. Génération automatique de la vignette
4. Mise à jour du statut: **APUREE** puis **LIVREE**
5. Enregistrement de la date de livraison
6. Envoi d'un email de notification au contribuable

## Montants des Taxes

### Plaque d'Immatriculation
- **Montant:** 40 USD
- **Type d'impôt:** PLAQUE
- **Compte bancaire:** CD59 0000 0000 0000 0000 0001
- **Intitulé:** DPRIHKAT - PLAQUES D'IMMATRICULATION

### Vignette (Moto/Tricycle)
- **Montant:** 10 USD
- **Type d'impôt:** IRV (Impôt sur Revenu Véhicule)
- **Compte bancaire:** CD59 0000 0000 0000 0000 0002
- **Intitulé:** DPRIHKAT - VIGNETTES VEHICULES

### Total à Payer
**50 USD** (40 USD plaque + 10 USD vignette)

## Notifications Email

Le système envoie automatiquement des emails aux contribuables à chaque étape :

1. **Après soumission** - Confirmation de la demande avec les notes de taxation
2. **Après validation** - Notification de validation
3. **Après rejet** - Notification de rejet avec motif
4. **Après paiement** - Confirmation du paiement
5. **Après attribution** - Notification d'attribution de la plaque
6. **Après livraison** - Notification de livraison

## Gestion des Erreurs

### Erreurs Courantes

1. **Numéro de châssis existant**
   - Code: `VEHICULE_CHASSIS_EXISTS`
   - Message: "Un véhicule avec ce numéro de châssis existe déjà"

2. **Véhicule n'appartient pas au contribuable**
   - Message: "Ce véhicule n'appartient pas à ce contribuable"

3. **Statut invalide**
   - Code: `INVALID_STATUT`
   - Message: "Les statuts valides sont: SOUMISE, VALIDEE, TAXEE, PAYEE, APUREE, LIVREE, REJETEE"

4. **Transition de statut invalide**
   - Exemple: "Cette demande ne peut pas être payée car elle n'est pas en statut TAXEE"

## Exemples d'Utilisation

### Exemple 1: Créer un véhicule et soumettre une demande

```bash
curl -X POST http://localhost:8080/api/demandes-plaque/creer-vehicule-et-demander \
  -H "Authorization: Bearer <token>" \
  -F "marque=TVS" \
  -F "modele=TVS 150" \
  -F "annee=2024" \
  -F "numeroChassis=ABC123XYZ789" \
  -F "genre=Moto" \
  -F "puissanceFiscale=3.0" \
  -F "contribuableId=uuid-contribuable" \
  -F "facture=@facture.pdf"
```

### Exemple 2: Consulter les demandes taxées

```bash
curl -X GET http://localhost:8080/api/demandes-plaque/statut/TAXEE \
  -H "Authorization: Bearer <token>"
```

### Exemple 3: Consulter les demandes d'un contribuable

```bash
curl -X GET http://localhost:8080/api/demandes-plaque/contribuable/{uuid}/statut/PAYEE \
  -H "Authorization: Bearer <token>"
```

### Exemple 4: Marquer une demande comme payée

```bash
curl -X POST http://localhost:8080/api/demandes-plaque/{uuid}/marquer-paye \
  -H "Authorization: Bearer <token>"
```

### Exemple 5: Attribuer une plaque

```bash
curl -X POST http://localhost:8080/api/demandes-plaque/{uuid}/attribuer-plaque \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "numeroPlaque": "CD-LUB-2024-12345",
    "agentId": "uuid-agent"
  }'
```

## Diagramme de Flux

```
[Contribuable soumet demande]
           ↓
[Véhicule créé avec TEMP-XXXXXXXX]
           ↓
[2 Notes de taxation générées]
  - Plaque: 40 USD
  - Vignette: 10 USD
           ↓
[Statut: TAXEE]
           ↓
[Contribuable paie 50 USD]
           ↓
[Statut: PAYEE]
           ↓
[Agent attribue plaque]
           ↓
[Immatriculation mise à jour]
           ↓
[Statut: VALIDEE]
           ↓
[Apurement et livraison]
           ↓
[Vignette générée automatiquement]
           ↓
[Statut: LIVREE]
```

## Sécurité et Autorisations

### Rôles et Permissions

| Action | CONTRIBUABLE | AGENT_DE_PLAQUES | TAXATEUR | ADMIN |
|--------|--------------|------------------|----------|-------|
| Soumettre demande | ✓ | ✓ | ✓ | ✓ |
| Consulter ses demandes | ✓ | ✓ | ✓ | ✓ |
| Consulter toutes demandes | ✗ | ✓ | ✓ | ✓ |
| Valider demande | ✗ | ✗ | ✓ | ✓ |
| Rejeter demande | ✗ | ✗ | ✓ | ✓ |
| Marquer comme payé | ✗ | ✓ | ✓ | ✓ |
| Attribuer plaque | ✗ | ✓ | ✗ | ✓ |
| Apurer et livrer | ✗ | ✓ | ✗ | ✓ |

## Notes Importantes

1. **Immatriculation temporaire:** Lors de la création du véhicule, une immatriculation temporaire au format `TEMP-XXXXXXXX` est générée. Elle sera remplacée par le vrai numéro de plaque lors de l'attribution.

2. **Génération automatique des notes:** Les 2 notes de taxation (plaque + vignette) sont créées automatiquement lors de la soumission de la demande. Le contribuable reçoit immédiatement les informations nécessaires pour le paiement.

3. **Échéance de paiement:** Les notes de taxation ont une échéance de 30 jours à partir de la date de création.

4. **Conversion de devises:** Le système supporte la gestion des taux de change USD/CDF. Les montants peuvent être affichés dans les deux devises.

5. **Vignette automatique:** Lors de l'apurement et de la livraison, une vignette est automatiquement générée pour le véhicule.

## Support et Contact

Pour toute question ou problème concernant les demandes de plaques, veuillez contacter :
- Email: support@dprihkat.cd
- Téléphone: +243 XXX XXX XXX
