# Données de Graphiques - Module de Rapports

## Vue d'ensemble

Le module de rapports génère automatiquement des données structurées pour les graphiques, prêtes à être utilisées par les bibliothèques de visualisation frontend populaires (Chart.js, Recharts, ApexCharts, D3.js, etc.).

## Structure des Données

### 1. Données Circulaires (Pie/Donut Charts)

Utilisées pour afficher la répartition par type (impôt, mode de paiement, etc.).

```json
{
  "donneesGraphiques": {
    "donneesCirculaires": [
      {
        "label": "IF",
        "valeur": 30000.00,
        "pourcentage": 60.0,
        "couleur": "#3B82F6"
      },
      {
        "label": "IRL",
        "valeur": 15000.00,
        "pourcentage": 30.0,
        "couleur": "#10B981"
      },
      {
        "label": "ICM",
        "valeur": 5000.00,
        "pourcentage": 10.0,
        "couleur": "#F59E0B"
      }
    ]
  }
}
```

**Utilisation avec Chart.js :**
```javascript
const pieData = {
  labels: donneesCirculaires.map(d => d.label),
  datasets: [{
    data: donneesCirculaires.map(d => d.valeur),
    backgroundColor: donneesCirculaires.map(d => d.couleur)
  }]
};
```

### 2. Séries Temporelles (Line/Bar Charts)

Utilisées pour afficher l'évolution dans le temps ou par catégorie.

```json
{
  "donneesGraphiques": {
    "seriesTemporelles": [
      {
        "nom": "IF",
        "labels": ["T_0001", "T_0002", "T_0003"],
        "valeurs": [500.00, 750.00, 600.00],
        "couleur": "#3B82F6"
      },
      {
        "nom": "IRL",
        "labels": ["T_0004", "T_0005"],
        "valeurs": [400.00, 550.00],
        "couleur": "#10B981"
      }
    ]
  }
}
```

**Utilisation avec Chart.js :**
```javascript
const lineData = {
  labels: seriesTemporelles[0].labels,
  datasets: seriesTemporelles.map(serie => ({
    label: serie.nom,
    data: serie.valeurs,
    borderColor: serie.couleur,
    backgroundColor: serie.couleur + '20' // Avec transparence
  }))
};
```

### 3. Évolution Temporelle (Trend Charts)

Affiche l'évolution avec tendance et moyenne.

```json
{
  "donneesGraphiques": {
    "evolutionTemporelle": {
      "periodes": ["2025-01-01", "2025-01-02", "2025-01-03"],
      "montants": [1000.00, 1200.00, 1500.00],
      "quantites": [5, 6, 8],
      "tendance": 50.0,
      "moyenne": 1233.33
    }
  }
}
```

**Utilisation avec Recharts (React) :**
```jsx
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts';

const data = evolutionTemporelle.periodes.map((periode, index) => ({
  periode,
  montant: evolutionTemporelle.montants[index],
  quantite: evolutionTemporelle.quantites[index]
}));

<LineChart data={data}>
  <CartesianGrid strokeDasharray="3 3" />
  <XAxis dataKey="periode" />
  <YAxis />
  <Tooltip />
  <Legend />
  <Line type="monotone" dataKey="montant" stroke="#3B82F6" />
  <Line type="monotone" dataKey="quantite" stroke="#10B981" />
</LineChart>
```

### 4. Top Items (Ranking Charts)

Affiche le classement des meilleurs contributeurs, agents, etc.

```json
{
  "donneesGraphiques": {
    "topItems": [
      {
        "nom": "KABILA Jean",
        "valeur": 15000.00,
        "quantite": 45,
        "pourcentage": 35.5,
        "rang": 1
      },
      {
        "nom": "MUKENDI Pierre",
        "valeur": 12000.00,
        "quantite": 38,
        "pourcentage": 28.4,
        "rang": 2
      }
    ]
  }
}
```

**Utilisation avec Chart.js (Horizontal Bar) :**
```javascript
const barData = {
  labels: topItems.map(item => item.nom),
  datasets: [{
    label: 'Montant',
    data: topItems.map(item => item.valeur),
    backgroundColor: '#3B82F6'
  }]
};

const options = {
  indexAxis: 'y', // Barres horizontales
  scales: {
    x: {
      beginAtZero: true
    }
  }
};
```

### 5. Données Empilées (Stacked Bar Charts)

Pour comparer plusieurs catégories simultanément.

```json
{
  "donneesGraphiques": {
    "donneesEmpilees": [
      {
        "categorie": "Janvier",
        "valeurs": {
          "IF": 5000.00,
          "IRL": 3000.00,
          "ICM": 2000.00
        }
      },
      {
        "categorie": "Février",
        "valeurs": {
          "IF": 6000.00,
          "IRL": 3500.00,
          "ICM": 2500.00
        }
      }
    ]
  }
}
```

**Utilisation avec ApexCharts :**
```javascript
const series = Object.keys(donneesEmpilees[0].valeurs).map(key => ({
  name: key,
  data: donneesEmpilees.map(d => d.valeurs[key])
}));

const options = {
  chart: {
    type: 'bar',
    stacked: true
  },
  xaxis: {
    categories: donneesEmpilees.map(d => d.categorie)
  }
};
```

## Exemples d'Intégration Frontend

### React avec Recharts

```tsx
import React from 'react';
import { PieChart, Pie, Cell, BarChart, Bar, LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts';

interface RapportGraphiquesProps {
  donneesGraphiques: any;
}

const RapportGraphiques: React.FC<RapportGraphiquesProps> = ({ donneesGraphiques }) => {
  // Graphique circulaire
  const renderPieChart = () => (
    <PieChart width={400} height={400}>
      <Pie
        data={donneesGraphiques.donneesCirculaires}
        dataKey="valeur"
        nameKey="label"
        cx="50%"
        cy="50%"
        outerRadius={150}
        label
      >
        {donneesGraphiques.donneesCirculaires.map((entry: any, index: number) => (
          <Cell key={`cell-${index}`} fill={entry.couleur} />
        ))}
      </Pie>
      <Tooltip />
      <Legend />
    </PieChart>
  );

  // Graphique d'évolution
  const renderEvolutionChart = () => {
    const data = donneesGraphiques.evolutionTemporelle.periodes.map((periode: string, index: number) => ({
      periode,
      montant: donneesGraphiques.evolutionTemporelle.montants[index],
      quantite: donneesGraphiques.evolutionTemporelle.quantites[index]
    }));

    return (
      <LineChart width={600} height={300} data={data}>
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="periode" />
        <YAxis yAxisId="left" />
        <YAxis yAxisId="right" orientation="right" />
        <Tooltip />
        <Legend />
        <Line yAxisId="left" type="monotone" dataKey="montant" stroke="#3B82F6" />
        <Line yAxisId="right" type="monotone" dataKey="quantite" stroke="#10B981" />
      </LineChart>
    );
  };

  // Graphique top items
  const renderTopItemsChart = () => (
    <BarChart width={600} height={300} data={donneesGraphiques.topItems} layout="horizontal">
      <CartesianGrid strokeDasharray="3 3" />
      <XAxis type="number" />
      <YAxis dataKey="nom" type="category" width={150} />
      <Tooltip />
      <Legend />
      <Bar dataKey="valeur" fill="#3B82F6" />
    </BarChart>
  );

  return (
    <div className="graphiques-container">
      <div className="graphique">
        <h3>Répartition par Type</h3>
        {donneesGraphiques.donneesCirculaires && renderPieChart()}
      </div>
      
      <div className="graphique">
        <h3>Évolution Temporelle</h3>
        {donneesGraphiques.evolutionTemporelle && renderEvolutionChart()}
      </div>
      
      <div className="graphique">
        <h3>Top 10</h3>
        {donneesGraphiques.topItems && renderTopItemsChart()}
      </div>
    </div>
  );
};

export default RapportGraphiques;
```

### Vue.js avec Chart.js

```vue
<template>
  <div class="graphiques-container">
    <div class="graphique">
      <h3>Répartition par Type</h3>
      <canvas ref="pieChart"></canvas>
    </div>
    
    <div class="graphique">
      <h3>Évolution Temporelle</h3>
      <canvas ref="lineChart"></canvas>
    </div>
    
    <div class="graphique">
      <h3>Top 10</h3>
      <canvas ref="barChart"></canvas>
    </div>
  </div>
</template>

<script>
import { Chart, registerables } from 'chart.js';
Chart.register(...registerables);

export default {
  name: 'RapportGraphiques',
  props: {
    donneesGraphiques: Object
  },
  mounted() {
    this.renderCharts();
  },
  methods: {
    renderCharts() {
      // Pie Chart
      if (this.donneesGraphiques.donneesCirculaires) {
        new Chart(this.$refs.pieChart, {
          type: 'pie',
          data: {
            labels: this.donneesGraphiques.donneesCirculaires.map(d => d.label),
            datasets: [{
              data: this.donneesGraphiques.donneesCirculaires.map(d => d.valeur),
              backgroundColor: this.donneesGraphiques.donneesCirculaires.map(d => d.couleur)
            }]
          }
        });
      }

      // Line Chart
      if (this.donneesGraphiques.evolutionTemporelle) {
        new Chart(this.$refs.lineChart, {
          type: 'line',
          data: {
            labels: this.donneesGraphiques.evolutionTemporelle.periodes,
            datasets: [{
              label: 'Montants',
              data: this.donneesGraphiques.evolutionTemporelle.montants,
              borderColor: '#3B82F6',
              tension: 0.1
            }]
          }
        });
      }

      // Bar Chart
      if (this.donneesGraphiques.topItems) {
        new Chart(this.$refs.barChart, {
          type: 'bar',
          data: {
            labels: this.donneesGraphiques.topItems.map(item => item.nom),
            datasets: [{
              label: 'Montant',
              data: this.donneesGraphiques.topItems.map(item => item.valeur),
              backgroundColor: '#3B82F6'
            }]
          },
          options: {
            indexAxis: 'y'
          }
        });
      }
    }
  }
};
</script>
```

### JavaScript Vanilla avec Chart.js

```javascript
// Fonction pour créer un graphique circulaire
function creerGraphiqueCirculaire(canvasId, donneesCirculaires) {
  const ctx = document.getElementById(canvasId).getContext('2d');
  
  new Chart(ctx, {
    type: 'pie',
    data: {
      labels: donneesCirculaires.map(d => d.label),
      datasets: [{
        data: donneesCirculaires.map(d => d.valeur),
        backgroundColor: donneesCirculaires.map(d => d.couleur)
      }]
    },
    options: {
      responsive: true,
      plugins: {
        legend: {
          position: 'bottom'
        },
        tooltip: {
          callbacks: {
            label: function(context) {
              const item = donneesCirculaires[context.dataIndex];
              return `${item.label}: ${item.valeur.toFixed(2)} (${item.pourcentage.toFixed(1)}%)`;
            }
          }
        }
      }
    }
  });
}

// Fonction pour créer un graphique d'évolution
function creerGraphiqueEvolution(canvasId, evolutionTemporelle) {
  const ctx = document.getElementById(canvasId).getContext('2d');
  
  new Chart(ctx, {
    type: 'line',
    data: {
      labels: evolutionTemporelle.periodes,
      datasets: [{
        label: 'Montants',
        data: evolutionTemporelle.montants,
        borderColor: '#3B82F6',
        backgroundColor: '#3B82F620',
        tension: 0.4
      }]
    },
    options: {
      responsive: true,
      plugins: {
        title: {
          display: true,
          text: `Tendance: ${evolutionTemporelle.tendance.toFixed(1)}% | Moyenne: ${evolutionTemporelle.moyenne.toFixed(2)}`
        }
      },
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  });
}

// Utilisation
fetch('/api/rapports/taxations?periode=MOIS')
  .then(response => response.json())
  .then(rapport => {
    const graphiques = rapport.donneesGraphiques;
    
    if (graphiques.donneesCirculaires) {
      creerGraphiqueCirculaire('pieChart', graphiques.donneesCirculaires);
    }
    
    if (graphiques.evolutionTemporelle) {
      creerGraphiqueEvolution('lineChart', graphiques.evolutionTemporelle);
    }
  });
```

## Palette de Couleurs

Les couleurs utilisées suivent une palette cohérente :

- **Bleu** (#3B82F6) - Couleur principale
- **Vert** (#10B981) - Succès, positif
- **Orange** (#F59E0B) - Attention, neutre
- **Rouge** (#EF4444) - Alerte, négatif
- **Violet** (#8B5CF6) - Secondaire
- **Rose** (#EC4899) - Accentuation

## Indicateurs Visuels

### Tendance

La tendance est exprimée en pourcentage :
- **Positif** (> 0) : Croissance ↗️
- **Négatif** (< 0) : Décroissance ↘️
- **Stable** (≈ 0) : Stabilité →

```javascript
function afficherTendance(tendance) {
  if (tendance > 5) {
    return { icon: '↗️', color: '#10B981', text: 'Hausse' };
  } else if (tendance < -5) {
    return { icon: '↘️', color: '#EF4444', text: 'Baisse' };
  } else {
    return { icon: '→', color: '#F59E0B', text: 'Stable' };
  }
}
```

## Bonnes Pratiques

1. **Responsive Design** : Utilisez des graphiques adaptatifs
2. **Accessibilité** : Ajoutez des labels et descriptions
3. **Performance** : Limitez le nombre de points affichés (max 100)
4. **Interactivité** : Ajoutez des tooltips informatifs
5. **Export** : Permettez l'export en image/PDF

## Bibliothèques Recommandées

### Chart.js
- Simple et léger
- Excellente documentation
- Idéal pour les graphiques standards

### Recharts
- Parfait pour React
- Composants déclaratifs
- Hautement personnalisable

### ApexCharts
- Graphiques modernes et animés
- Nombreux types de graphiques
- Fonctionnalités avancées

### D3.js
- Maximum de flexibilité
- Visualisations personnalisées
- Courbe d'apprentissage plus élevée

## Support

Pour toute question sur l'intégration des graphiques, consultez la documentation de votre bibliothèque de visualisation ou contactez l'équipe de développement.
