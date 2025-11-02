# Changelog - Système de Demandes de Plaques avec Taxation Automatique

## Date: 31 Octobre 2025

## Résumé des Changements

Le système de demandes de plaques d'immatriculation a été amélioré pour automatiser la génération des notes de taxation et faciliter la consultation des demandes par statut.

## Nouvelles Fonctionnalités

### 1. Génération Automatique des Notes de Taxation

Lors de la soumission d'une demande de plaque, **2 notes de taxation sont automatiquement créées** :

#### Note 1: Plaque d'Immatriculation
- **Montant:** 40 USD
- **Type:** PLAQUE
- **Compte:** CD59 0000 0000 0000 0000 0001
- **Intitulé:** DPRIHKAT - PLAQUES D'IMMATRICULATION
- **Échéance:** 30 jours

#### Note 2: Vignette (Moto/Tricycle)
- **Montant:** 10 USD
- **Type:** IRV (Impôt sur Revenu Véhicule)
- **Compte:** CD59 0000 0000 0000 0000 0002
- **Intitulé:** DPRIHKAT - VIGNETTES VEHICULES
- **Échéance:** 30 jours

**Total à payer: 50 USD**

### 2. Nouveaux DTOs pour l'Impression

#### NoteTaxationDTO
Contient toutes les informations nécessaires pour imprimer une note de taxation :
- Informations de la taxation (numéro, montant, dates, etc.)
- Informations du contribuable (nom, NRC, adresse, contacts)
- Informations du véhicule (marque, modèle, châssis, etc.)
- Informations bancaires (banque, compte, intitulé)
- Informations de l'agent taxateur (nom, matricule, bureau, division)

#### DemandePlaqueResponseDTO
Retourne une demande complète avec ses 2 notes de taxation prêtes pour l'impression.

### 3. Nouveaux Endpoints de Consultation

#### Consulter les demandes par statut
```
GET /api/demandes-plaque/statut/{statut}
```
Rôles: AGENT_DE_PLAQUES, TAXATEUR, ADMIN

Statuts disponibles:
- `SOUMISE` - Demandes soumises
- `VALIDEE` - Demandes validées
- `TAXEE` - Demandes taxées (avec notes générées)
- `PAYEE` - Demandes payées
- `APUREE` - Demandes apurées
- `LIVREE` - Plaques livrées
- `REJETEE` - Demandes rejetées

#### Consulter les demandes d'un contribuable par statut
```
GET /api/demandes-plaque/contribuable/{contribuableId}/statut/{statut}
```
Rôles: CONTRIBUABLE, AGENT_DE_PLAQUES, TAXATEUR, ADMIN

### 4. Workflow Amélioré

**Avant:**
1. Soumission → Statut SOUMISE
2. Validation manuelle → Génération des notes → Statut TAXEE
3. Paiement → Statut PAYEE
4. Attribution → Livraison

**Maintenant:**
1. Soumission → **Génération automatique des 2 notes** → Statut **TAXEE** directement
2. Paiement → Statut PAYEE
3. Attribution → Livraison

**Avantage:** Le contribuable reçoit immédiatement les notes de taxation à imprimer et peut procéder au paiement sans attendre une validation manuelle.

## Fichiers Modifiés

### Services
- **DemandePlaqueService.java**
  - Méthode `soumettreDemande()` modifiée pour retourner `DemandePlaqueResponseDTO`
  - Ajout de `creerNoteTaxationPlaque()` - Crée la note de 40 USD
  - Ajout de `creerNoteTaxationVignette()` - Crée la note de 10 USD
  - Ajout de `convertirTaxationEnDTO()` - Convertit les taxations en DTOs pour impression
  - Injection de `TaxationRepository`

### Controllers
- **DemandePlaqueController.java**
  - Endpoint `creerVehiculeEtDemanderPlaque` mis à jour pour utiliser le nouveau DTO
  - Ajout de `getDemandesByStatut()` - Consulter par statut
  - Ajout de `getDemandesByContribuableAndStatut()` - Consulter par contribuable et statut

### DTOs (Nouveaux fichiers)
- **NoteTaxationDTO.java** - DTO pour les données d'impression des notes
- **DemandePlaqueResponseDTO.java** - DTO de réponse avec les notes de taxation

### Documentation
- **workflow_demandes_plaques.md** - Documentation complète du workflow
- **CHANGELOG_DEMANDES_PLAQUES.md** - Ce fichier

## Exemple de Réponse API

```json
{
  "success": true,
  "data": {
    "vehicule": {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "marque": "TVS",
      "modele": "TVS 150",
      "immatriculation": "TEMP-A1B2C3D4",
      "statut": "TAXE"
    },
    "demande": {
      "demandeId": "223e4567-e89b-12d3-a456-426614174001",
      "statut": "TAXEE",
      "dateDemande": "2025-10-31T10:00:00",
      "contribuableNom": "KABONGO Jean",
      "notePlaque": {
        "numeroTaxation": "PLAQ_A1B2C3D4_ABC123_2025",
        "montant": 40.0,
        "devise": "USD",
        "typeImpot": "PLAQUE",
        "dateEcheance": "2025-11-30",
        "nomBanque": "RAWBANK",
        "numeroCompte": "CD59 0000 0000 0000 0000 0001",
        "intituleCompte": "DPRIHKAT - PLAQUES D'IMMATRICULATION",
        "contribuableNom": "KABONGO Jean",
        "contribuableNRC": "NRC123456",
        "vehiculeMarque": "TVS",
        "vehiculeModele": "TVS 150",
        "vehiculeNumeroChassis": "ABC123XYZ789"
      },
      "noteVignette": {
        "numeroTaxation": "VIG_E5F6G7H8_ABC123_2025",
        "montant": 10.0,
        "devise": "USD",
        "typeImpot": "IRV",
        "dateEcheance": "2025-11-30",
        "nomBanque": "RAWBANK",
        "numeroCompte": "CD59 0000 0000 0000 0000 0002",
        "intituleCompte": "DPRIHKAT - VIGNETTES VEHICULES"
      },
      "message": "Demande de plaque soumise avec succès. Deux notes de taxation ont été générées : Plaque (40 USD) et Vignette (10 USD). Total à payer: 50 USD"
    }
  }
}
```

## Impact sur le Frontend

### Changements Nécessaires

1. **Affichage des Notes de Taxation**
   - Après soumission, afficher les 2 notes avec tous leurs détails
   - Bouton "Imprimer Note Plaque" (40 USD)
   - Bouton "Imprimer Note Vignette" (10 USD)
   - Afficher le total: 50 USD

2. **Consultation par Statut**
   - Ajouter des filtres par statut dans l'interface de consultation
   - Exemples: "Voir les demandes taxées", "Voir les demandes payées", etc.

3. **Template d'Impression**
   - Créer un template pour imprimer les notes de taxation
   - Inclure toutes les informations du DTO (contribuable, véhicule, banque, etc.)
   - Ajouter un QR code si disponible

## Migration des Données Existantes

**Aucune migration nécessaire** - Les demandes existantes continuent de fonctionner normalement. Seules les nouvelles demandes bénéficient de la génération automatique des notes.

## Tests Recommandés

1. **Test de soumission**
   - Créer un véhicule et soumettre une demande
   - Vérifier que 2 notes sont créées
   - Vérifier les montants (40 USD + 10 USD)

2. **Test de consultation par statut**
   - Consulter `/api/demandes-plaque/statut/TAXEE`
   - Vérifier que les demandes retournées ont bien le statut TAXEE

3. **Test d'impression**
   - Récupérer les DTOs des notes
   - Vérifier que toutes les informations sont présentes
   - Tester l'impression

## Configuration

### Informations Bancaires

Les informations bancaires sont actuellement codées en dur dans le service. Pour les modifier :

**Fichier:** `DemandePlaqueService.java`

**Plaque (ligne 561-563):**
```java
taxation.setNomBanque("RAWBANK");
taxation.setNumeroCompte("CD59 0000 0000 0000 0000 0001");
taxation.setIntituleCompte("DPRIHKAT - PLAQUES D'IMMATRICULATION");
```

**Vignette (ligne 595-597):**
```java
taxation.setNomBanque("RAWBANK");
taxation.setNumeroCompte("CD59 0000 0000 0000 0000 0002");
taxation.setIntituleCompte("DPRIHKAT - VIGNETTES VEHICULES");
```

### Montants

**Plaque (ligne 541):**
```java
taxation.setMontant(40.0); // Montant fixe pour la plaque
```

**Vignette (ligne 575):**
```java
taxation.setMontant(10.0); // Montant fixe pour la vignette moto/tricycle
```

## Prochaines Améliorations Possibles

1. **Configuration externe des montants** - Permettre de configurer les montants via un fichier de configuration ou une interface admin

2. **Montants variables selon le type de véhicule** - Différencier les montants pour motos, tricycles, voitures, etc.

3. **Génération de QR codes** - Ajouter des QR codes sur les notes pour faciliter le paiement mobile

4. **Intégration paiement mobile** - Permettre le paiement direct via mobile money

5. **Statistiques** - Dashboard avec statistiques sur les demandes par statut
 
## Support

Pour toute question ou problème, contacter l'équipe de développement DPRIHKAT.

---

**Version:** 2.0  
**Date:** 31 Octobre 2025  
**Auteur:** Équipe DPRIHKAT
