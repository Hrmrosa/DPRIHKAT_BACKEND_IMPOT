# Exemples d'Utilisation du Module de Rapports

## Table des Matières
1. [Exemples cURL](#exemples-curl)
2. [Exemples JavaScript/TypeScript](#exemples-javascripttypescript)
3. [Exemples Python](#exemples-python)
4. [Exemples Java](#exemples-java)
5. [Scénarios Réels](#scénarios-réels)

## Exemples cURL

### 1. Rapport Mensuel des Taxations

```bash
curl -X GET "http://localhost:8080/api/rapports/taxations?periode=MOIS" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -H "Accept: application/json"
```

**Réponse attendue :**
```json
{
  "typeRapport": "TAXATION",
  "periode": "MOIS",
  "dateDebut": "2025-11-01T00:00:00Z",
  "dateFin": "2025-11-30T23:59:59Z",
  "dateGeneration": "2025-11-03T10:15:00Z",
  "statistiquesGlobales": {
    "nombreTaxations": 45,
    "montantTotalTaxations": 15000.00,
    "repartitionParType": {
      "IF": 25,
      "IRL": 15,
      "ICM": 5
    }
  },
  "taxations": [...]
}
```

### 2. Rapport des Paiements d'un Agent Spécifique

```bash
curl -X GET "http://localhost:8080/api/rapports/paiements?periode=SEMAINE&agentId=123e4567-e89b-12d3-a456-426614174000" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Accept: application/json"
```

### 3. Rapport Global Annuel

```bash
curl -X GET "http://localhost:8080/api/rapports/global?periode=ANNEE" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Accept: application/json"
```

### 4. Rapport Personnalisé avec Dates Spécifiques

```bash
curl -X GET "http://localhost:8080/api/rapports/personnalise?typeRapport=TAXATION&dateDebut=2025-01-01&dateFin=2025-03-31" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Accept: application/json"
```

### 5. Génération via POST

```bash
curl -X POST "http://localhost:8080/api/rapports/generer" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "typeRapport": "PAIEMENT",
    "periode": "TRIMESTRE",
    "agentId": "123e4567-e89b-12d3-a456-426614174000"
  }'
```

## Exemples JavaScript/TypeScript

### 1. Service de Rapport (React/Vue.js)

```typescript
// rapportService.ts
import axios from 'axios';

interface RapportRequest {
  typeRapport: 'TAXATION' | 'PAIEMENT' | 'RELANCE' | 'COLLECTE' | 'RECOUVREMENT' | 'GLOBAL';
  periode: 'JOUR' | 'SEMAINE' | 'MOIS' | 'TRIMESTRE' | 'SEMESTRE' | 'ANNEE' | 'PERSONNALISEE';
  dateDebut?: string;
  dateFin?: string;
  agentId?: string;
}

interface StatistiquesGlobales {
  nombreTaxations?: number;
  montantTotalTaxations?: number;
  nombrePaiements?: number;
  montantTotalPaiements?: number;
  nombreRelances?: number;
  nombreActesRecouvrement?: number;
  montantTotalRecouvrement?: number;
  nombreCollectes?: number;
  repartitionParType?: Record<string, number>;
  repartitionMontantsParType?: Record<string, number>;
}

interface RapportResponse {
  typeRapport: string;
  periode: string;
  dateDebut: string;
  dateFin: string;
  dateGeneration: string;
  agentNom?: string;
  agentMatricule?: string;
  statistiquesGlobales: StatistiquesGlobales;
  taxations?: any[];
  paiements?: any[];
  relances?: any[];
  recouvrements?: any[];
  collectes?: any[];
}

class RapportService {
  private baseURL = 'http://localhost:8080/api/rapports';
  private token: string;

  constructor(token: string) {
    this.token = token;
  }

  private getHeaders() {
    return {
      'Authorization': `Bearer ${this.token}`,
      'Content-Type': 'application/json'
    };
  }

  async genererRapport(request: RapportRequest): Promise<RapportResponse> {
    const response = await axios.post(
      `${this.baseURL}/generer`,
      request,
      { headers: this.getHeaders() }
    );
    return response.data;
  }

  async getRapportTaxations(
    periode: string = 'MOIS',
    agentId?: string
  ): Promise<RapportResponse> {
    const params = new URLSearchParams({ periode });
    if (agentId) params.append('agentId', agentId);

    const response = await axios.get(
      `${this.baseURL}/taxations?${params}`,
      { headers: this.getHeaders() }
    );
    return response.data;
  }

  async getRapportPaiements(
    periode: string = 'MOIS',
    agentId?: string
  ): Promise<RapportResponse> {
    const params = new URLSearchParams({ periode });
    if (agentId) params.append('agentId', agentId);

    const response = await axios.get(
      `${this.baseURL}/paiements?${params}`,
      { headers: this.getHeaders() }
    );
    return response.data;
  }

  async getRapportGlobal(periode: string = 'MOIS'): Promise<RapportResponse> {
    const response = await axios.get(
      `${this.baseURL}/global?periode=${periode}`,
      { headers: this.getHeaders() }
    );
    return response.data;
  }

  async getRapportPersonnalise(
    typeRapport: string,
    dateDebut: string,
    dateFin: string,
    agentId?: string
  ): Promise<RapportResponse> {
    const params = new URLSearchParams({
      typeRapport,
      dateDebut,
      dateFin
    });
    if (agentId) params.append('agentId', agentId);

    const response = await axios.get(
      `${this.baseURL}/personnalise?${params}`,
      { headers: this.getHeaders() }
    );
    return response.data;
  }
}

export default RapportService;
```

### 2. Composant React

```tsx
// RapportDashboard.tsx
import React, { useState, useEffect } from 'react';
import RapportService from './rapportService';

const RapportDashboard: React.FC = () => {
  const [rapport, setRapport] = useState<any>(null);
  const [loading, setLoading] = useState(false);
  const [periode, setPeriode] = useState('MOIS');
  const [typeRapport, setTypeRapport] = useState('TAXATION');

  const rapportService = new RapportService(localStorage.getItem('token') || '');

  const chargerRapport = async () => {
    setLoading(true);
    try {
      const data = await rapportService.genererRapport({
        typeRapport: typeRapport as any,
        periode: periode as any
      });
      setRapport(data);
    } catch (error) {
      console.error('Erreur lors du chargement du rapport:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    chargerRapport();
  }, [periode, typeRapport]);

  if (loading) return <div>Chargement...</div>;
  if (!rapport) return <div>Aucun rapport disponible</div>;

  return (
    <div className="rapport-dashboard">
      <h1>Rapport {rapport.typeRapport}</h1>
      
      <div className="filtres">
        <select value={typeRapport} onChange={(e) => setTypeRapport(e.target.value)}>
          <option value="TAXATION">Taxations</option>
          <option value="PAIEMENT">Paiements</option>
          <option value="RELANCE">Relances</option>
          <option value="RECOUVREMENT">Recouvrements</option>
          <option value="COLLECTE">Collectes</option>
          <option value="GLOBAL">Global</option>
        </select>

        <select value={periode} onChange={(e) => setPeriode(e.target.value)}>
          <option value="JOUR">Aujourd'hui</option>
          <option value="SEMAINE">Cette semaine</option>
          <option value="MOIS">Ce mois</option>
          <option value="TRIMESTRE">Ce trimestre</option>
          <option value="ANNEE">Cette année</option>
        </select>
      </div>

      <div className="statistiques">
        <h2>Statistiques Globales</h2>
        {rapport.statistiquesGlobales && (
          <div className="stats-grid">
            {rapport.statistiquesGlobales.nombreTaxations && (
              <div className="stat-card">
                <h3>Taxations</h3>
                <p className="nombre">{rapport.statistiquesGlobales.nombreTaxations}</p>
                <p className="montant">${rapport.statistiquesGlobales.montantTotalTaxations?.toFixed(2)}</p>
              </div>
            )}
            {rapport.statistiquesGlobales.nombrePaiements && (
              <div className="stat-card">
                <h3>Paiements</h3>
                <p className="nombre">{rapport.statistiquesGlobales.nombrePaiements}</p>
                <p className="montant">${rapport.statistiquesGlobales.montantTotalPaiements?.toFixed(2)}</p>
              </div>
            )}
          </div>
        )}
      </div>

      <div className="details">
        <h2>Détails</h2>
        {rapport.taxations && rapport.taxations.length > 0 && (
          <table>
            <thead>
              <tr>
                <th>Numéro</th>
                <th>Date</th>
                <th>Montant</th>
                <th>Type</th>
                <th>Statut</th>
              </tr>
            </thead>
            <tbody>
              {rapport.taxations.map((t: any) => (
                <tr key={t.id}>
                  <td>{t.numeroTaxation}</td>
                  <td>{new Date(t.dateTaxation).toLocaleDateString()}</td>
                  <td>${t.montant?.toFixed(2)}</td>
                  <td>{t.typeImpot}</td>
                  <td>{t.statut}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
};

export default RapportDashboard;
```

## Exemples Python

### 1. Client Python

```python
# rapport_client.py
import requests
from typing import Optional, Dict, Any
from datetime import datetime

class RapportClient:
    def __init__(self, base_url: str, token: str):
        self.base_url = base_url
        self.token = token
        self.headers = {
            'Authorization': f'Bearer {token}',
            'Content-Type': 'application/json'
        }
    
    def generer_rapport(
        self,
        type_rapport: str,
        periode: str = 'MOIS',
        date_debut: Optional[str] = None,
        date_fin: Optional[str] = None,
        agent_id: Optional[str] = None
    ) -> Dict[str, Any]:
        """Génère un rapport selon les critères spécifiés"""
        data = {
            'typeRapport': type_rapport,
            'periode': periode
        }
        
        if date_debut:
            data['dateDebut'] = date_debut
        if date_fin:
            data['dateFin'] = date_fin
        if agent_id:
            data['agentId'] = agent_id
        
        response = requests.post(
            f'{self.base_url}/rapports/generer',
            json=data,
            headers=self.headers
        )
        response.raise_for_status()
        return response.json()
    
    def get_rapport_taxations(
        self,
        periode: str = 'MOIS',
        agent_id: Optional[str] = None
    ) -> Dict[str, Any]:
        """Récupère le rapport des taxations"""
        params = {'periode': periode}
        if agent_id:
            params['agentId'] = agent_id
        
        response = requests.get(
            f'{self.base_url}/rapports/taxations',
            params=params,
            headers=self.headers
        )
        response.raise_for_status()
        return response.json()
    
    def get_rapport_global(self, periode: str = 'MOIS') -> Dict[str, Any]:
        """Récupère le rapport global"""
        response = requests.get(
            f'{self.base_url}/rapports/global',
            params={'periode': periode},
            headers=self.headers
        )
        response.raise_for_status()
        return response.json()
    
    def exporter_csv(self, rapport: Dict[str, Any], filename: str):
        """Exporte un rapport en CSV"""
        import csv
        
        if 'taxations' in rapport and rapport['taxations']:
            with open(filename, 'w', newline='', encoding='utf-8') as f:
                writer = csv.DictWriter(f, fieldnames=rapport['taxations'][0].keys())
                writer.writeheader()
                writer.writerows(rapport['taxations'])

# Utilisation
if __name__ == '__main__':
    client = RapportClient(
        base_url='http://localhost:8080/api',
        token='YOUR_JWT_TOKEN'
    )
    
    # Rapport mensuel des taxations
    rapport = client.get_rapport_taxations(periode='MOIS')
    print(f"Nombre de taxations: {rapport['statistiquesGlobales']['nombreTaxations']}")
    print(f"Montant total: {rapport['statistiquesGlobales']['montantTotalTaxations']}")
    
    # Export CSV
    client.exporter_csv(rapport, 'rapport_taxations.csv')
```

## Exemples Java

### 1. Client Java avec RestTemplate

```java
// RapportClient.java
package com.example.client;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

public class RapportClient {
    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String token;

    public RapportClient(String baseUrl, String token) {
        this.restTemplate = new RestTemplate();
        this.baseUrl = baseUrl;
        this.token = token;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public Map<String, Object> genererRapport(
            String typeRapport,
            String periode,
            String dateDebut,
            String dateFin,
            String agentId) {
        
        Map<String, Object> request = new HashMap<>();
        request.put("typeRapport", typeRapport);
        request.put("periode", periode);
        if (dateDebut != null) request.put("dateDebut", dateDebut);
        if (dateFin != null) request.put("dateFin", dateFin);
        if (agentId != null) request.put("agentId", agentId);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, getHeaders());

        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl + "/rapports/generer",
                HttpMethod.POST,
                entity,
                Map.class
        );

        return response.getBody();
    }

    public Map<String, Object> getRapportTaxations(String periode, String agentId) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(baseUrl + "/rapports/taxations")
                .queryParam("periode", periode);
        
        if (agentId != null) {
            builder.queryParam("agentId", agentId);
        }

        HttpEntity<?> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<Map> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                Map.class
        );

        return response.getBody();
    }

    public static void main(String[] args) {
        RapportClient client = new RapportClient(
                "http://localhost:8080/api",
                "YOUR_JWT_TOKEN"
        );

        // Rapport mensuel
        Map<String, Object> rapport = client.getRapportTaxations("MOIS", null);
        System.out.println("Rapport généré: " + rapport);
    }
}
```

## Scénarios Réels

### Scénario 1 : Évaluation Mensuelle d'un Agent Taxateur

**Contexte :** Un chef de bureau souhaite évaluer la performance d'un agent taxateur pour le mois écoulé.

```bash
# Récupérer le rapport
curl -X GET "http://localhost:8080/api/rapports/taxations?periode=MOIS&agentId=agent-123" \
  -H "Authorization: Bearer TOKEN"

# Analyser les résultats
# - Nombre de taxations effectuées
# - Montant total taxé
# - Répartition par type d'impôt
# - Comparaison avec les objectifs
```

### Scénario 2 : Rapport Hebdomadaire des Encaissements

**Contexte :** Le receveur des impôts doit produire un rapport hebdomadaire des paiements reçus.

```bash
curl -X GET "http://localhost:8080/api/rapports/paiements?periode=SEMAINE" \
  -H "Authorization: Bearer TOKEN"
```

### Scénario 3 : Audit Trimestriel

**Contexte :** La direction demande un audit complet des activités du trimestre.

```bash
curl -X GET "http://localhost:8080/api/rapports/global?periode=TRIMESTRE" \
  -H "Authorization: Bearer TOKEN"
```

### Scénario 4 : Analyse des Actions de Recouvrement

**Contexte :** Évaluer l'efficacité des actions de recouvrement sur le semestre.

```bash
curl -X GET "http://localhost:8080/api/rapports/recouvrements?periode=SEMESTRE" \
  -H "Authorization: Bearer TOKEN"
```

### Scénario 5 : Rapport Annuel pour la Direction

**Contexte :** Préparer le rapport annuel pour la présentation à la direction générale.

```bash
curl -X GET "http://localhost:8080/api/rapports/global?periode=ANNEE" \
  -H "Authorization: Bearer TOKEN" > rapport_annuel_2025.json
```

## Conseils d'Utilisation

1. **Utilisez les périodes prédéfinies** pour une cohérence dans vos rapports
2. **Filtrez par agent** pour des évaluations individuelles
3. **Exportez les données** pour des analyses approfondies
4. **Automatisez les rapports récurrents** avec des scripts
5. **Combinez plusieurs rapports** pour une vue d'ensemble complète

## Dépannage

### Problème : Rapport vide
**Solution :** Vérifiez que la période contient des données. Essayez une période plus large.

### Problème : Erreur 401
**Solution :** Vérifiez que votre token JWT est valide et non expiré.

### Problème : Erreur 403
**Solution :** Vérifiez que votre rôle a les permissions nécessaires pour ce type de rapport.

### Problème : Timeout
**Solution :** Pour les périodes très larges (année complète), utilisez des filtres supplémentaires ou divisez en plusieurs requêtes.
