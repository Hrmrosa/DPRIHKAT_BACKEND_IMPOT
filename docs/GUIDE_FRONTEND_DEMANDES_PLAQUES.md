# Guide Frontend - Intégration des Demandes de Plaques

## Vue d'ensemble

Ce guide explique comment intégrer le nouveau système de demandes de plaques avec génération automatique des notes de taxation dans votre application frontend.

## 1. Soumission d'une Demande

### Endpoint
```
POST /api/demandes-plaque/creer-vehicule-et-demander
```

### Formulaire à Créer
 
```html
<form enctype="multipart/form-data">
  <!-- Informations du véhicule -->
  <input name="marque" type="text" required placeholder="Marque (ex: TVS)" />
  <input name="modele" type="text" required placeholder="Modèle (ex: TVS 150)" />
  <input name="annee" type="number" required placeholder="Année (ex: 2024)" />
  <input name="numeroChassis" type="text" required placeholder="Numéro de châssis" />
  <input name="genre" type="text" required placeholder="Genre (ex: Moto, Tricycle)" />
  <input name="puissanceFiscale" type="number" step="0.1" placeholder="Puissance fiscale (CV)" />
  <input name="couleur" type="text" placeholder="Couleur" />
  <input name="nombrePlaces" type="number" placeholder="Nombre de places" />
  
  <!-- Contribuable -->
  <input name="contribuableId" type="hidden" value="uuid-du-contribuable" />
  
  <!-- Facture -->
  <input name="facture" type="file" required accept=".pdf,.jpg,.png" />
  
  <button type="submit">Soumettre la demande</button>
</form>
```

### Code JavaScript (Exemple avec Fetch)

```javascript
async function soumettreDemandePlaque(formData) {
  try {
    const response = await fetch('/api/demandes-plaque/creer-vehicule-et-demander', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`
      },
      body: formData // FormData avec tous les champs
    });
    
    const result = await response.json();
    
    if (result.success) {
      const demande = result.data.demande;
      
      // Afficher les notes de taxation
      afficherNotesDetaxation(demande.notePlaque, demande.noteVignette);
      
      // Afficher le message de succès
      alert(demande.message);
      
      // Rediriger vers la page d'impression
      window.location.href = `/imprimer-notes/${demande.demandeId}`;
    } else {
      alert('Erreur: ' + result.message);
    }
  } catch (error) {
    console.error('Erreur:', error);
    alert('Erreur lors de la soumission de la demande');
  }
}
```

### Réponse Attendue

```json
{
  "success": true,
  "data": {
    "vehicule": { ... },
    "demande": {
      "demandeId": "uuid",
      "statut": "TAXEE",
      "notePlaque": {
        "numeroTaxation": "PLAQ_...",
        "montant": 40.0,
        "devise": "USD",
        ...
      },
      "noteVignette": {
        "numeroTaxation": "VIG_...",
        "montant": 10.0,
        "devise": "USD",
        ...
      },
      "message": "Demande soumise avec succès..."
    }
  }
}
```

## 2. Affichage des Notes de Taxation

### Composant React (Exemple)

```jsx
function NotesDetaxation({ notePlaque, noteVignette }) {
  const total = notePlaque.montant + noteVignette.montant;
  
  return (
    <div className="notes-taxation">
      <h2>Notes de Taxation Générées</h2>
      
      {/* Note Plaque */}
      <div className="note-card">
        <h3>Plaque d'Immatriculation</h3>
        <div className="note-details">
          <p><strong>Numéro:</strong> {notePlaque.numeroTaxation}</p>
          <p><strong>Montant:</strong> {notePlaque.montant} {notePlaque.devise}</p>
          <p><strong>Date d'échéance:</strong> {formatDate(notePlaque.dateEcheance)}</p>
          <p><strong>Banque:</strong> {notePlaque.nomBanque}</p>
          <p><strong>Compte:</strong> {notePlaque.numeroCompte}</p>
          <p><strong>Intitulé:</strong> {notePlaque.intituleCompte}</p>
        </div>
        <button onClick={() => imprimerNote(notePlaque)}>
          Imprimer Note Plaque
        </button>
      </div>
      
      {/* Note Vignette */}
      <div className="note-card">
        <h3>Vignette</h3>
        <div className="note-details">
          <p><strong>Numéro:</strong> {noteVignette.numeroTaxation}</p>
          <p><strong>Montant:</strong> {noteVignette.montant} {noteVignette.devise}</p>
          <p><strong>Date d'échéance:</strong> {formatDate(noteVignette.dateEcheance)}</p>
          <p><strong>Banque:</strong> {noteVignette.nomBanque}</p>
          <p><strong>Compte:</strong> {noteVignette.numeroCompte}</p>
          <p><strong>Intitulé:</strong> {noteVignette.intituleCompte}</p>
        </div>
        <button onClick={() => imprimerNote(noteVignette)}>
          Imprimer Note Vignette
        </button>
      </div>
      
      {/* Total */}
      <div className="total-section">
        <h3>Total à Payer</h3>
        <p className="total-amount">{total} USD</p>
      </div>
    </div>
  );
}
```

## 3. Template d'Impression

### HTML pour Impression

```html
<!DOCTYPE html>
<html>
<head>
  <title>Note de Taxation - {{numeroTaxation}}</title>
  <style>
    @media print {
      body { margin: 0; }
      .no-print { display: none; }
    }
    
    .note-taxation {
      width: 21cm;
      min-height: 29.7cm;
      padding: 2cm;
      margin: 0 auto;
      background: white;
      font-family: Arial, sans-serif;
    }
    
    .header {
      text-align: center;
      border-bottom: 2px solid #000;
      padding-bottom: 20px;
      margin-bottom: 30px;
    }
    
    .logo {
      max-width: 150px;
    }
    
    .title {
      font-size: 24px;
      font-weight: bold;
      margin: 20px 0;
    }
    
    .section {
      margin: 20px 0;
    }
    
    .section-title {
      font-weight: bold;
      font-size: 16px;
      border-bottom: 1px solid #ccc;
      padding-bottom: 5px;
      margin-bottom: 10px;
    }
    
    .info-row {
      display: flex;
      justify-content: space-between;
      padding: 5px 0;
    }
    
    .label {
      font-weight: bold;
    }
    
    .montant {
      font-size: 28px;
      font-weight: bold;
      color: #2c3e50;
      text-align: center;
      margin: 30px 0;
      padding: 20px;
      border: 3px solid #2c3e50;
    }
    
    .footer {
      margin-top: 50px;
      text-align: center;
      font-size: 12px;
      color: #666;
    }
  </style>
</head>
<body>
  <div class="note-taxation">
    <!-- En-tête -->
    <div class="header">
      <img src="/logo-dprihkat.png" alt="DPRIHKAT" class="logo" />
      <h1>Direction des Recettes Provinciales de l'Intérieur</h1>
      <h2>Province du Haut-Katanga</h2>
      <div class="title">NOTE DE TAXATION</div>
      <p>{{typeImpot}} - {{numeroTaxation}}</p>
    </div>
    
    <!-- Informations du Contribuable -->
    <div class="section">
      <div class="section-title">CONTRIBUABLE</div>
      <div class="info-row">
        <span class="label">Nom:</span>
        <span>{{contribuableNom}}</span>
      </div>
      <div class="info-row">
        <span class="label">NRC:</span>
        <span>{{contribuableNRC}}</span>
      </div>
      <div class="info-row">
        <span class="label">ID National:</span>
        <span>{{contribuableIdNat}}</span>
      </div>
      <div class="info-row">
        <span class="label">Adresse:</span>
        <span>{{contribuableAdresse}}</span>
      </div>
      <div class="info-row">
        <span class="label">Téléphone:</span>
        <span>{{contribuableTelephone}}</span>
      </div>
    </div>
    
    <!-- Informations du Véhicule -->
    <div class="section">
      <div class="section-title">VÉHICULE</div>
      <div class="info-row">
        <span class="label">Marque:</span>
        <span>{{vehiculeMarque}}</span>
      </div>
      <div class="info-row">
        <span class="label">Modèle:</span>
        <span>{{vehiculeModele}}</span>
      </div>
      <div class="info-row">
        <span class="label">Année:</span>
        <span>{{vehiculeAnnee}}</span>
      </div>
      <div class="info-row">
        <span class="label">Numéro de châssis:</span>
        <span>{{vehiculeNumeroChassis}}</span>
      </div>
      <div class="info-row">
        <span class="label">Genre:</span>
        <span>{{vehiculeGenre}}</span>
      </div>
      <div class="info-row">
        <span class="label">Puissance fiscale:</span>
        <span>{{vehiculePuissanceFiscale}} CV</span>
      </div>
    </div>
    
    <!-- Montant -->
    <div class="montant">
      MONTANT À PAYER: {{montant}} {{devise}}
    </div>
    
    <!-- Informations de Paiement -->
    <div class="section">
      <div class="section-title">INFORMATIONS DE PAIEMENT</div>
      <div class="info-row">
        <span class="label">Banque:</span>
        <span>{{nomBanque}}</span>
      </div>
      <div class="info-row">
        <span class="label">Numéro de compte:</span>
        <span>{{numeroCompte}}</span>
      </div>
      <div class="info-row">
        <span class="label">Intitulé du compte:</span>
        <span>{{intituleCompte}}</span>
      </div>
      <div class="info-row">
        <span class="label">Date d'échéance:</span>
        <span>{{dateEcheance}}</span>
      </div>
    </div>
    
    <!-- QR Code (si disponible) -->
    {{#if codeQR}}
    <div class="section">
      <div class="section-title">CODE QR</div>
      <img src="{{codeQR}}" alt="QR Code" style="width: 150px; height: 150px;" />
    </div>
    {{/if}}
    
    <!-- Pied de page -->
    <div class="footer">
      <p>Cette note de taxation est valable jusqu'au {{dateEcheance}}</p>
      <p>Pour toute information, contactez-nous au +243 XXX XXX XXX ou support@dprihkat.cd</p>
      <p>Date d'émission: {{dateTaxation}}</p>
    </div>
    
    <!-- Bouton d'impression -->
    <div class="no-print" style="text-align: center; margin-top: 30px;">
      <button onclick="window.print()" style="padding: 10px 30px; font-size: 16px;">
        Imprimer
      </button>
      <button onclick="window.close()" style="padding: 10px 30px; font-size: 16px; margin-left: 10px;">
        Fermer
      </button>
    </div>
  </div>
</body>
</html>
```

### Fonction d'Impression JavaScript

```javascript
function imprimerNote(note) {
  // Créer une nouvelle fenêtre
  const printWindow = window.open('', '_blank');
  
  // Générer le HTML avec les données de la note
  const html = genererHTMLNote(note);
  
  // Écrire le HTML dans la fenêtre
  printWindow.document.write(html);
  printWindow.document.close();
  
  // Attendre le chargement puis imprimer
  printWindow.onload = function() {
    printWindow.print();
  };
}

function genererHTMLNote(note) {
  // Remplacer les placeholders par les vraies valeurs
  let html = templateHTML; // Le template ci-dessus
  
  Object.keys(note).forEach(key => {
    const placeholder = `{{${key}}}`;
    const value = note[key] || '';
    html = html.replace(new RegExp(placeholder, 'g'), value);
  });
  
  return html;
}
```

## 4. Consultation des Demandes par Statut

### Endpoint
```
GET /api/demandes-plaque/statut/{statut}
```

### Composant de Filtrage

```jsx
function DemandesParStatut() {
  const [statut, setStatut] = useState('TAXEE');
  const [demandes, setDemandes] = useState([]);
  const [loading, setLoading] = useState(false);
  
  const statuts = [
    { value: 'SOUMISE', label: 'Soumises' },
    { value: 'VALIDEE', label: 'Validées' },
    { value: 'TAXEE', label: 'Taxées' },
    { value: 'PAYEE', label: 'Payées' },
    { value: 'APUREE', label: 'Apurées' },
    { value: 'LIVREE', label: 'Livrées' },
    { value: 'REJETEE', label: 'Rejetées' }
  ];
  
  useEffect(() => {
    chargerDemandes();
  }, [statut]);
  
  async function chargerDemandes() {
    setLoading(true);
    try {
      const response = await fetch(`/api/demandes-plaque/statut/${statut}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      
      const result = await response.json();
      if (result.success) {
        setDemandes(result.data.demandes);
      }
    } catch (error) {
      console.error('Erreur:', error);
    } finally {
      setLoading(false);
    }
  }
  
  return (
    <div>
      <h2>Demandes de Plaques</h2>
      
      {/* Filtres */}
      <div className="filters">
        <label>Filtrer par statut:</label>
        <select value={statut} onChange={(e) => setStatut(e.target.value)}>
          {statuts.map(s => (
            <option key={s.value} value={s.value}>{s.label}</option>
          ))}
        </select>
      </div>
      
      {/* Liste des demandes */}
      {loading ? (
        <p>Chargement...</p>
      ) : (
        <div className="demandes-list">
          <p>{demandes.length} demande(s) trouvée(s)</p>
          {demandes.map(demande => (
            <DemandeCard key={demande.id} demande={demande} />
          ))}
        </div>
      )}
    </div>
  );
}
```

## 5. Styles CSS Recommandés

```css
/* Notes de taxation */
.notes-taxation {
  max-width: 1200px;
  margin: 20px auto;
  padding: 20px;
}

.note-card {
  background: white;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 20px;
  margin: 20px 0;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.note-card h3 {
  color: #2c3e50;
  border-bottom: 2px solid #3498db;
  padding-bottom: 10px;
  margin-bottom: 15px;
}

.note-details p {
  margin: 10px 0;
  display: flex;
  justify-content: space-between;
}

.note-details strong {
  color: #555;
}

.note-card button {
  background: #3498db;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
  margin-top: 15px;
}

.note-card button:hover {
  background: #2980b9;
}

.total-section {
  background: #2c3e50;
  color: white;
  padding: 20px;
  border-radius: 8px;
  text-align: center;
  margin-top: 30px;
}

.total-amount {
  font-size: 36px;
  font-weight: bold;
  margin: 10px 0;
}

/* Filtres */
.filters {
  background: #f8f9fa;
  padding: 15px;
  border-radius: 5px;
  margin-bottom: 20px;
}

.filters label {
  margin-right: 10px;
  font-weight: bold;
}

.filters select {
  padding: 8px 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

/* Liste des demandes */
.demandes-list {
  margin-top: 20px;
}

.demande-card {
  background: white;
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 15px;
  margin: 10px 0;
  cursor: pointer;
  transition: all 0.3s;
}

.demande-card:hover {
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
  transform: translateY(-2px);
}

/* Badges de statut */
.badge {
  display: inline-block;
  padding: 5px 10px;
  border-radius: 3px;
  font-size: 12px;
  font-weight: bold;
}

.badge-taxee { background: #f39c12; color: white; }
.badge-payee { background: #27ae60; color: white; }
.badge-livree { background: #2ecc71; color: white; }
.badge-rejetee { background: #e74c3c; color: white; }
```

## 6. Gestion des Erreurs

```javascript
async function gererErreurs(response) {
  if (!response.ok) {
    const result = await response.json();
    
    switch (result.code) {
      case 'VEHICULE_CHASSIS_EXISTS':
        alert('Ce numéro de châssis existe déjà dans le système');
        break;
      case 'INVALID_STATUT':
        alert('Statut invalide. Statuts valides: SOUMISE, VALIDEE, TAXEE, PAYEE, APUREE, LIVREE, REJETEE');
        break;
      default:
        alert('Erreur: ' + result.message);
    }
    
    throw new Error(result.message);
  }
  
  return response.json();
}
```

## 7. Checklist d'Intégration

- [ ] Formulaire de soumission créé
- [ ] Affichage des 2 notes de taxation
- [ ] Template d'impression des notes
- [ ] Fonction d'impression fonctionnelle
- [ ] Filtrage par statut implémenté
- [ ] Styles CSS appliqués
- [ ] Gestion des erreurs
- [ ] Tests avec données réelles
- [ ] Responsive design vérifié
- [ ] Accessibilité vérifiée

## Support

Pour toute question sur l'intégration frontend, contactez l'équipe backend DPRIHKAT.
