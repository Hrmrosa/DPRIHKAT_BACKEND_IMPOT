# API Complète - Demandes de Plaques avec Notes de Taxation

## Vue d'ensemble

Tous les endpoints retournent maintenant les demandes avec **détails complets** incluant :
- ✅ Informations de la demande (statut, dates)
- ✅ Informations du véhicule (marque, modèle, châssis, immatriculation)
- ✅ Informations du contribuable (nom, NRC)
- ✅ **Notes de taxation** (plaque 37 USD + vignette 10 USD)
- ✅ **Plaque assignée** (numéro, statut, date d'attribution) si disponible

## Structure de Réponse Complète

```json
{
  "success": true,
  "data": {
    "demandes": [
      {
        "demandeId": "uuid",
        "statut": "TAXEE",
        "dateDemande": "2025-10-31T10:00:00",
        "dateValidation": null,
        "datePaiement": null,
        "dateLivraison": null,
        
        "vehiculeId": "uuid",
        "vehiculeMarque": "TVS",
        "vehiculeModele": "TVS 150",
        "vehiculeNumeroChassis": "ABC123XYZ789",
        "vehiculeImmatriculation": "TEMP-A1B2C3D4",
        
        "contribuableId": "uuid",
        "contribuableNom": "KABONGO Jean",
        "contribuableNRC": "NRC123456",
        
        "notePlaque": {
          "id": "uuid",
          "numeroTaxation": "PLAQ_A1B2C3D4_ABC123_2025",
          "dateTaxation": "2025-10-31T10:00:00",
          "dateEcheance": "2025-11-30T00:00:00",
          "montant": 37.0,
          "devise": "USD",
          "exercice": "2025",
          "statut": "EN_ATTENTE",
          "typeImpot": "PLAQUE",
          "codeQR": null,
          "nomBanque": "RAWBANK",
          "numeroCompte": "CD59 0000 0000 0000 0000 0001",
          "intituleCompte": "DPRIHKAT - PLAQUES D'IMMATRICULATION",
          "contribuableNom": "KABONGO Jean",
          "contribuableNRC": "NRC123456",
          "contribuableIdNat": "1234567890123",
          "contribuableAdresse": "Lubumbashi, Katanga",
          "contribuableTelephone": "+243 XXX XXX XXX",
          "contribuableEmail": "kabongo@example.com",
          "vehiculeMarque": "TVS",
          "vehiculeModele": "TVS 150",
          "vehiculeAnnee": 2024,
          "vehiculeNumeroChassis": "ABC123XYZ789",
          "vehiculeGenre": "Moto",
          "vehiculeCategorie": "Moto légère",
          "vehiculePuissanceFiscale": 3.0,
          "agentNom": null,
          "agentMatricule": null,
          "bureauNom": null,
          "divisionNom": null
        },
        
        "noteVignette": {
          "id": "uuid",
          "numeroTaxation": "VIG_E5F6G7H8_ABC123_2025",
          "dateTaxation": "2025-10-31T10:00:00",
          "dateEcheance": "2025-11-30T00:00:00",
          "montant": 10.0,
          "devise": "USD",
          "exercice": "2025",
          "statut": "EN_ATTENTE",
          "typeImpot": "IRV",
          "codeQR": null,
          "nomBanque": "RAWBANK",
          "numeroCompte": "CD59 0000 0000 0000 0000 0002",
          "intituleCompte": "DPRIHKAT - VIGNETTES VEHICULES",
          "contribuableNom": "KABONGO Jean",
          "contribuableNRC": "NRC123456",
          "vehiculeMarque": "TVS",
          "vehiculeModele": "TVS 150"
        },
        
        "plaqueId": null,
        "plaqueNumero": null,
        "plaqueStatut": null,
        "plaqueDateAttribution": null,
        
        "message": null
      }
    ],
    "count": 1
  }
}
```

## Endpoints Disponibles

### 1. Créer un Véhicule et Soumettre une Demande

**Endpoint:** `POST /api/demandes-plaque/creer-vehicule-et-demander`

**Rôles:** CONTRIBUABLE, AGENT_DE_PLAQUES, TAXATEUR, ADMIN

**Description:** Crée un véhicule et soumet automatiquement une demande de plaque avec génération des 2 notes de taxation.

**Paramètres (multipart/form-data):**
- `marque` (String, requis)
- `modele` (String, requis)
- `annee` (Integer, requis)
- `numeroChassis` (String, requis)
- `genre` (String, requis)
- `categorie` (String, optionnel)
- `puissanceFiscale` (Double, optionnel)
- `couleur` (String, optionnel)
- `nombrePlaces` (Integer, optionnel)
- `contribuableId` (UUID, requis)
- `facture` (File, requis)

**Réponse:** Demande avec notes de taxation générées

---

### 2. Récupérer Toutes les Demandes (avec détails complets)

**Endpoint:** `GET /api/demandes-plaque`

**Rôles:** AGENT_DE_PLAQUES, TAXATEUR, RECEVEUR_DES_IMPOTS, ADMIN

**Description:** Récupère toutes les demandes avec leurs notes de taxation et plaques assignées.

**Exemple:**
```bash
curl -X GET http://localhost:8080/api/demandes-plaque \
  -H "Authorization: Bearer <token>"
```

**Réponse:**
```json
{
  "success": true,
  "data": {
    "demandes": [...],
    "count": 25
  }
}
```

---

### 3. Récupérer les Demandes par Statut (avec détails complets)

**Endpoint:** `GET /api/demandes-plaque/statut/{statut}`

**Rôles:** AGENT_DE_PLAQUES, TAXATEUR, ADMIN

**Statuts disponibles:**
- `SOUMISE` - Demandes soumises
- `VALIDEE` - Demandes validées
- `TAXEE` - Demandes taxées (notes générées)
- `PAYEE` - Demandes payées
- `APUREE` - Demandes apurées
- `LIVREE` - Plaques livrées
- `REJETEE` - Demandes rejetées

**Exemples:**
```bash
# Toutes les demandes taxées
curl -X GET http://localhost:8080/api/demandes-plaque/statut/TAXEE \
  -H "Authorization: Bearer <token>"

# Toutes les demandes payées
curl -X GET http://localhost:8080/api/demandes-plaque/statut/PAYEE \
  -H "Authorization: Bearer <token>"

# Toutes les demandes livrées
curl -X GET http://localhost:8080/api/demandes-plaque/statut/LIVREE \
  -H "Authorization: Bearer <token>"
```

**Réponse:**
```json
{
  "success": true,
  "data": {
    "demandes": [...],
    "count": 10,
    "statut": "TAXEE"
  }
}
```

---

### 4. Récupérer Mes Demandes (Contribuable)

**Endpoint:** `GET /api/demandes-plaque/mes-demandes`

**Rôles:** CONTRIBUABLE

**Description:** Récupère toutes les demandes du contribuable authentifié avec détails complets.

**Exemple:**
```bash
curl -X GET http://localhost:8080/api/demandes-plaque/mes-demandes \
  -H "Authorization: Bearer <token-contribuable>"
```

**Réponse:**
```json
{
  "success": true,
  "data": {
    "demandes": [...],
    "count": 3
  }
}
```

---

### 5. Récupérer les Demandes d'un Contribuable par Statut

**Endpoint:** `GET /api/demandes-plaque/contribuable/{contribuableId}/statut/{statut}`

**Rôles:** CONTRIBUABLE, AGENT_DE_PLAQUES, TAXATEUR, ADMIN

**Exemple:**
```bash
curl -X GET http://localhost:8080/api/demandes-plaque/contribuable/uuid/statut/PAYEE \
  -H "Authorization: Bearer <token>"
```

---

### 6. Récupérer une Demande Spécifique

**Endpoint:** `GET /api/demandes-plaque/{id}`

**Rôles:** CONTRIBUABLE, AGENT_DE_PLAQUES, TAXATEUR, ADMIN

**Exemple:**
```bash
curl -X GET http://localhost:8080/api/demandes-plaque/uuid \
  -H "Authorization: Bearer <token>"
```

---

### 7. Marquer une Demande comme Payée

**Endpoint:** `POST /api/demandes-plaque/{id}/marquer-paye`

**Rôles:** AGENT_DE_PLAQUES, TAXATEUR, ADMIN

**Description:** Marque une demande comme payée après vérification du paiement.

**Exemple:**
```bash
curl -X POST http://localhost:8080/api/demandes-plaque/uuid/marquer-paye \
  -H "Authorization: Bearer <token>"
```

---

### 8. Attribuer une Plaque

**Endpoint:** `POST /api/demandes-plaque/{id}/attribuer-plaque`

**Rôles:** AGENT_DE_PLAQUES, ADMIN

**Body (JSON):**
```json
{
  "numeroPlaque": "CD-LUB-2024-12345",
  "agentId": "uuid-agent"
}
```

**Exemple:**
```bash
curl -X POST http://localhost:8080/api/demandes-plaque/uuid/attribuer-plaque \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "numeroPlaque": "CD-LUB-2024-12345",
    "agentId": "uuid-agent"
  }'
```

---

### 9. Apurer et Livrer

**Endpoint:** `POST /api/demandes-plaque/{id}/apurer-et-livrer`

**Rôles:** AGENT_DE_PLAQUES, ADMIN

**Body (JSON):**
```json
{
  "numeroPlaque": "CD-LUB-2024-12345"
}
```

---

## Informations sur les Plaques Assignées

Lorsqu'une plaque est assignée à une demande, les champs suivants sont remplis dans la réponse :

```json
{
  "plaqueId": "uuid",
  "plaqueNumero": "CD-LUB-2024-12345",
  "plaqueStatut": "ACTIVE",
  "plaqueDateAttribution": "2025-10-31T15:30:00"
}
```

**Statuts de plaque possibles:**
- `DISPONIBLE` - Plaque disponible
- `ACTIVE` - Plaque active et en circulation
- `SUSPENDUE` - Plaque suspendue
- `PERDUE` - Plaque déclarée perdue
- `VOLEE` - Plaque déclarée volée
- `DESACTIVEE` - Plaque désactivée

## Cas d'Usage Frontend

### 1. Dashboard Agent - Voir toutes les demandes taxées

```javascript
async function chargerDemandesTaxees() {
  const response = await fetch('/api/demandes-plaque/statut/TAXEE', {
    headers: { 'Authorization': `Bearer ${token}` }
  });
  
  const result = await response.json();
  
  result.data.demandes.forEach(demande => {
    console.log(`Demande ${demande.demandeId}`);
    console.log(`Véhicule: ${demande.vehiculeMarque} ${demande.vehiculeModele}`);
    console.log(`Contribuable: ${demande.contribuableNom}`);
    console.log(`Note Plaque: ${demande.notePlaque.numeroTaxation} - ${demande.notePlaque.montant} USD`);
    console.log(`Note Vignette: ${demande.noteVignette.numeroTaxation} - ${demande.noteVignette.montant} USD`);
    console.log(`Total à payer: ${demande.notePlaque.montant + demande.noteVignette.montant} USD`);
    
    if (demande.plaqueNumero) {
      console.log(`Plaque assignée: ${demande.plaqueNumero} (${demande.plaqueStatut})`);
    }
  });
}
```

### 2. Interface Contribuable - Voir mes demandes

```javascript
async function chargerMesDemandes() {
  const response = await fetch('/api/demandes-plaque/mes-demandes', {
    headers: { 'Authorization': `Bearer ${token}` }
  });
  
  const result = await response.json();
  
  return result.data.demandes.map(demande => ({
    id: demande.demandeId,
    vehicule: `${demande.vehiculeMarque} ${demande.vehiculeModele}`,
    statut: demande.statut,
    totalAPayer: demande.notePlaque.montant + demande.noteVignette.montant,
    plaqueAssignee: demande.plaqueNumero || 'En attente',
    notePlaque: demande.notePlaque,
    noteVignette: demande.noteVignette
  }));
}
```

### 3. Afficher les détails d'une demande avec plaque

```jsx
function DemandeDetails({ demande }) {
  return (
    <div className="demande-details">
      <h2>Demande #{demande.demandeId}</h2>
      
      <section className="vehicule">
        <h3>Véhicule</h3>
        <p>Marque: {demande.vehiculeMarque}</p>
        <p>Modèle: {demande.vehiculeModele}</p>
        <p>Châssis: {demande.vehiculeNumeroChassis}</p>
        <p>Immatriculation: {demande.vehiculeImmatriculation}</p>
      </section>
      
      <section className="notes-taxation">
        <h3>Notes de Taxation</h3>
        
        <div className="note">
          <h4>Plaque - {demande.notePlaque.montant} USD</h4>
          <p>N°: {demande.notePlaque.numeroTaxation}</p>
          <p>Échéance: {formatDate(demande.notePlaque.dateEcheance)}</p>
          <button onClick={() => imprimerNote(demande.notePlaque)}>
            Imprimer
          </button>
        </div>
        
        <div className="note">
          <h4>Vignette - {demande.noteVignette.montant} USD</h4>
          <p>N°: {demande.noteVignette.numeroTaxation}</p>
          <p>Échéance: {formatDate(demande.noteVignette.dateEcheance)}</p>
          <button onClick={() => imprimerNote(demande.noteVignette)}>
            Imprimer
          </button>
        </div>
        
        <div className="total">
          <strong>Total: {demande.notePlaque.montant + demande.noteVignette.montant} USD</strong>
        </div>
      </section>
      
      {demande.plaqueNumero && (
        <section className="plaque-assignee">
          <h3>Plaque Assignée</h3>
          <p className="numero-plaque">{demande.plaqueNumero}</p>
          <p>Statut: {demande.plaqueStatut}</p>
          <p>Date d'attribution: {formatDate(demande.plaqueDateAttribution)}</p>
        </section>
      )}
    </div>
  );
}
```

## Filtrage et Recherche

### Exemple: Filtrer les demandes avec plaque assignée

```javascript
async function chargerDemandesAvecPlaque() {
  const response = await fetch('/api/demandes-plaque', {
    headers: { 'Authorization': `Bearer ${token}` }
  });
  
  const result = await response.json();
  
  // Filtrer les demandes qui ont une plaque assignée
  const demandesAvecPlaque = result.data.demandes.filter(
    demande => demande.plaqueNumero !== null
  );
  
  return demandesAvecPlaque;
}
```

### Exemple: Rechercher par numéro de plaque

```javascript
async function rechercherParPlaque(numeroPlaque) {
  const response = await fetch('/api/demandes-plaque', {
    headers: { 'Authorization': `Bearer ${token}` }
  });
  
  const result = await response.json();
  
  return result.data.demandes.find(
    demande => demande.plaqueNumero === numeroPlaque
  );
}
```

## Statistiques

### Exemple: Calculer les statistiques

```javascript
async function calculerStatistiques() {
  const response = await fetch('/api/demandes-plaque', {
    headers: { 'Authorization': `Bearer ${token}` }
  });
  
  const result = await response.json();
  const demandes = result.data.demandes;
  
  return {
    total: demandes.length,
    taxees: demandes.filter(d => d.statut === 'TAXEE').length,
    payees: demandes.filter(d => d.statut === 'PAYEE').length,
    livrees: demandes.filter(d => d.statut === 'LIVREE').length,
    avecPlaque: demandes.filter(d => d.plaqueNumero !== null).length,
    montantTotal: demandes.reduce((sum, d) => 
      sum + d.notePlaque.montant + d.noteVignette.montant, 0
    ),
    montantPaye: demandes
      .filter(d => ['PAYEE', 'APUREE', 'LIVREE'].includes(d.statut))
      .reduce((sum, d) => sum + d.notePlaque.montant + d.noteVignette.montant, 0)
  };
}
```

## Notes Importantes

1. **Toutes les réponses incluent maintenant les détails complets** - Plus besoin d'appels API supplémentaires pour récupérer les notes de taxation ou les plaques.

2. **Les notes de taxation sont toujours présentes** pour les demandes en statut TAXEE et au-delà.

3. **Les informations de plaque** (`plaqueId`, `plaqueNumero`, etc.) sont `null` jusqu'à ce qu'une plaque soit assignée.

4. **Performance optimisée** - Les requêtes sont optimisées pour charger toutes les relations nécessaires en une seule fois.

5. **Pagination** - Pour de grandes quantités de données, envisagez d'ajouter la pagination côté backend.

## Voir les Taxations dans la Liste Générale

Les taxations créées pour les demandes de plaques et vignettes apparaissent automatiquement dans la liste générale des taxations accessible via:

**Endpoint:** `GET /api/taxations`

**Filtres disponibles:**
- Par type d'impôt: `typeImpot=PLAQUE` ou `typeImpot=IRV` (vignette)
- Par statut: `statut=EN_ATTENTE`, `statut=PAYEE`, etc.
- Par contribuable: via l'ID du contribuable

**Exemple pour voir toutes les taxations de plaques:**
```bash
curl -X GET "http://localhost:8080/api/taxations?typeImpot=PLAQUE" \
  -H "Authorization: Bearer <token>"
```

**Exemple pour voir toutes les taxations de vignettes:**
```bash
curl -X GET "http://localhost:8080/api/taxations?typeImpot=IRV" \
  -H "Authorization: Bearer <token>"
```

Les taxations sont créées avec:
- **Type PLAQUE** pour les plaques d'immatriculation (37 USD)
- **Type IRV** pour les vignettes de motos/tricycles (10 USD)
- **Statut EN_ATTENTE** jusqu'au paiement
- **Lien vers la demande** via le champ `demande_plaque_id`

**Structure de la réponse enrichie:**
```json
{
  "id": "uuid",
  "numeroTaxation": "PLAQ_41BA110C_DLIUDD_2025",
  "montant": 37.0,
  "devise": "USD",
  "typeImpot": "PLAQUE",
  "statut": "EN_ATTENTE",
  "contribuable": {
    "id": "uuid", 
    "nom": "KABONGO Jean",
    "adressePrincipale": "Lubumbashi, Katanga",
    "telephonePrincipal": "+243 XXX XXX XXX",
    "email": "kabongo@example.com"
  },
  "vehicule": {
    "id": "uuid",
    "marque": "TVS",
    "modele": "TVS 150",
    "numeroChassis": "ABC123XYZ789",
    "immatriculation": "TEMP-A1B2C3D4",
    "genre": "Moto"
  },
  "agent": {
    "id": "uuid",
    "nom": "Agent Taxateur",
    "matricule": "AGT001"
  }
}
```

## Support

Pour toute question sur l'utilisation de l'API, contactez l'équipe backend DPRIHKAT.
