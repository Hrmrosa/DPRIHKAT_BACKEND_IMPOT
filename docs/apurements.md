# Documentation API - Gestion des Apurements

Cette documentation détaille les endpoints disponibles pour la gestion des apurements dans l'API DPRIHKAT.

## Vue d'ensemble

Les apurements représentent les procédures administratives permettant de régler une situation fiscale sans paiement intégral immédiat. Ils sont associés à des taxations et permettent de gérer différentes modalités de règlement comme les remises gracieuses, les échelonnements de paiement ou les transactions.

**Changements récents** :
- Les apurements créés manuellement sont maintenant directement en statut `ACCEPTEE`
- La date de validation et l'agent validateur sont automatiquement renseignés lors de la création
- Le montant apuré est optionnel lors de la création

## Base URL

```
/api/apurements
```

## Endpoints

### 1. Créer un apurement

Crée un nouvel apurement pour une déclaration spécifique.

- **URL**: `/api/apurements/create/{declarationId}?type={typeApurement}`
- **Méthode**: `POST`
- **Rôles autorisés**: `APUREUR`, `RECEVEUR_DES_IMPOTS`, `ADMIN`
- **Paramètres**: 
  - `declarationId` (path): UUID de la déclaration
  - `type` (query): Type d'apurement (REMISE_GRACIEUSE, ECHELONNEMENT, TRANSACTION)
  - `motif` (body): Motif de la demande (optionnel)

#### Payload d'entrée

```json
{
  "motif": "Difficultés financières du contribuable",
  "provisoire": true
}
```

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "apurement": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "dateDemande": "2025-09-12T12:30:45.123Z",
      "dateValidation": "2025-09-12T12:30:45.123Z",
      "type": "REMISE_GRACIEUSE",
      "motif": "Difficultés financières du contribuable",
      "statut": "ACCEPTEE",
      "provisoire": true,
      "agentValidateur": {
        "id": "550e8400-e29b-41d4-a716-446655440001",
        "nom": "Kabila"
      },
      "declaration": {
        "id": "550e8400-e29b-41d4-a716-446655440002"
      }
    }
  }
}
```

**Changements** :
- Le statut est maintenant directement `ACCEPTEE` pour les apurements créés manuellement
- La date de validation est automatiquement renseignée
- L'agent validateur est l'utilisateur qui a créé l'apurement

### 2. Récupérer un apurement par ID

- **URL**: `/api/apurements/{id}`
- **Méthode**: `GET`
- **Rôles autorisés**: `APUREUR`, `RECEVEUR_DES_IMPOTS`, `ADMIN`, `CONTRIBUABLE` (seulement pour ses propres apurements)

### 3. Lister les apurements

- **URL**: `/api/apurements`
- **Méthode**: `GET`
- **Paramètres optionnels** :
  - `declarationId` : Filtrer par déclaration
  - `contribuableId` : Filtrer par contribuable
  - `statut` : Filtrer par statut
  - `type` : Filtrer par type
  - `page`, `size` : Pagination

### 4. Valider/Rejeter un apurement

- **URL**: `/api/apurements/{id}/validate`
- **Méthode**: `POST`
- **Rôles autorisés**: `RECEVEUR_DES_IMPOTS`, `ADMIN`
- **Body** :
```json
{
  "valider": true,
  "motif": "Motif de validation ou rejet"
}
```

### 5. Récupérer les apurements par déclaration

Récupère tous les apurements associés à une déclaration.

- **URL**: `/api/apurements/declaration/{declarationId}`
- **Méthode**: `GET`
- **Rôles autorisés**: `APUREUR`, `RECEVEUR_DES_IMPOTS`, `CHEF_DE_BUREAU`, `CHEF_DE_DIVISION`, `DIRECTEUR`, `ADMIN`
- **Paramètres**: 
  - `declarationId` (path parameter): UUID de la déclaration

#### Réponse en cas de succès

```json
{
  "success": true,
  "data": {
    "apurements": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "dateDemande": "2025-09-12T12:30:45.123Z",
        "dateValidation": "2025-09-14T09:15:30.456Z",
        "type": "REMISE_GRACIEUSE",
        "montantApure": 1500.0,
        "motif": "Difficultés financières du contribuable",
        "motifRejet": null,
        "statut": "ACCEPTEE",
        "provisoire": false,
        "actif": true,
        "declarationPayee": true,
        "agent": {
          "id": "550e8400-e29b-41d4-a716-446655440001",
          "nom": "Kabila",
          "postnom": "Jean",
          "prenom": "Pierre"
        },
        "agentValidateur": {
          "id": "550e8400-e29b-41d4-a716-446655440003",
          "nom": "Tshisekedi",
          "postnom": "Marie",
          "prenom": "Claire"
        }
      }
    ]
  },
  "meta": {
    "version": "1.0.0",
    "timestamp": "2025-09-15T08:45:12.123Z"
  }
}
```

## Structure des données

### Apurement

| Champ | Type | Description |
|-------|------|-------------|
| id | UUID | Identifiant unique de l'apurement |
| dateDemande | Date | Date de la demande d'apurement |
| dateValidation | Date | Date de validation de l'apurement (null si non validé) |
| type | TypeApurement | Type d'apurement (REMISE_GRACIEUSE, ECHELONNEMENT, TRANSACTION) |
| montantApure | Double | Montant apuré |
| motif | String | Motif de la demande d'apurement |
| motifRejet | String | Motif du rejet (null si non rejeté) |
| statut | StatutApurement | Statut de l'apurement (PROVISOIRE, ACCEPTEE, REJETEE) |
| provisoire | Boolean | Indique si l'apurement est provisoire |
| actif | Boolean | Indique si l'apurement est actif |
| declarationPayee | Boolean | Indique si la déclaration a été payée |
| agent | Agent | Agent qui a initié la demande d'apurement |
| agentValidateur | Agent | Agent qui a validé ou rejeté l'apurement |
| declaration | Declaration | Déclaration associée à l'apurement |
| taxation | Taxation | Taxation associée à l'apurement |
| paiement | Paiement | Paiement associé à l'apurement (si applicable) |

## Règles métier

1. Un apurement est toujours associé à une déclaration et à une taxation.
2. Un apurement peut être associé à un paiement si celui-ci est effectué.
3. Un apurement passe par plusieurs statuts : PROVISOIRE → ACCEPTEE (ou REJETEE).
4. Seul un apureur ou un receveur des impôts peut créer un apurement.
5. Seul un chef de bureau, un chef de division, un directeur ou un administrateur peut valider ou rejeter un apurement.
6. Un apurement validé peut être définitif ou provisoire.
7. Un apurement validé définitif marque la déclaration comme payée.

## Exemples d'utilisation

### Créer un apurement

```bash
curl -X POST "http://localhost:8080/api/apurements/create/550e8400-e29b-41d4-a716-446655440002?type=REMISE_GRACIEUSE" \
-H "Content-Type: application/json" \
-H "Authorization: Bearer [JWT_TOKEN]" \
-d '{
  "motif": "Difficultés financières du contribuable",
  "provisoire": true
}'
```

### Valider un apurement

```bash
curl -X POST "http://localhost:8080/api/apurements/550e8400-e29b-41d4-a716-446655440000/validate" \
-H "Content-Type: application/json" \
-H "Authorization: Bearer [JWT_TOKEN]" \
-d '{
  "valider": true,
  "motif": "Apurement validé suite à l'examen du dossier"
}'
```

### Rejeter un apurement

```bash
curl -X POST "http://localhost:8080/api/apurements/550e8400-e29b-41d4-a716-446655440000/validate" \
-H "Content-Type: application/json" \
-H "Authorization: Bearer [JWT_TOKEN]" \
-d '{
  "valider": false,
  "motif": "Documents justificatifs insuffisants"
}'
```
