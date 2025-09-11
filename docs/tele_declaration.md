# Télé-déclaration

## Description

Le système de télé-déclaration permet aux contribuables de déclarer leurs biens en ligne. Le processus inclut l'affichage des biens du contribuable, le choix de l'acte ou de l'impôt en fonction de l'assujettissement du bien, la soumission de la déclaration, la validation par un agent interne, et l'envoi d'une notification par email une fois la déclaration validée.

## Processus de télé-déclaration

1. Le contribuable se connecte à l'application
2. Le système affiche les biens du contribuable
3. Le contribuable choisit l'acte ou l'impôt applicable pour chaque bien
4. Le contribuable soumet la déclaration
5. La déclaration est enregistrée avec le statut "EN_ATTENTE"
6. Un agent interne valide la déclaration
7. Une fois validée, un email est généré et envoyé au contribuable avec un message et les informations de la déclaration

## Endpoints API

### Afficher les biens du contribuable avec les actes/impôts applicables

`GET /api/proprietes/mine/with-tax-types`

#### Description
Retourne la liste des biens du contribuable connecté avec les types d'impôts applicables pour chaque bien.

#### Rôles autorisés
- CONTRIBUABLE
- ADMIN

#### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "proprietes": [
      {
        "propriete": {
          "id": "UUID",
          "type": "string",
          "localite": "string",
          "rangLocalite": 0,
          "superficie": 0,
          "adresse": "string",
          "latitude": 0,
          "longitude": 0,
          "proprietaire": {
            "id": "UUID",
            "nom": "string",
            "adressePrincipale": "string",
            "adresseSecondaire": "string",
            "telephonePrincipal": "string",
            "telephoneSecondaire": "string",
            "email": "string",
            "nationalite": "string",
            "type": "string",
            "idNat": "string",
            "nrc": "string",
            "sigle": "string",
            "numeroIdentificationContribuable": "string"
          }
        },
        "impotsApplicables": [
          {
            "code": "string",
            "libelle": "string",
            "description": "string"
          }
        ]
      }
    ]
  },
  "error": null,
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

### Récupérer les types d'impôts disponibles

`GET /api/declarations/types-impots`

#### Description
Retourne la liste de tous les types d'impôts disponibles dans le système.

#### Rôles autorisés
- CONTRIBUABLE
- ADMIN

#### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "typesImpots": [
      {
        "code": "string",
        "libelle": "string",
        "description": "string"
      }
    ]
  },
  "error": null,
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

### Soumettre une déclaration

`POST /api/declarations/soumettre`

#### Description
Soumet une nouvelle déclaration pour un bien.

#### Rôles autorisés
- CONTRIBUABLE
- ADMIN

#### Corps de la requête
```json
{
  "proprieteId": "UUID",
  "concessionId": "UUID",
  "typeImpot": "string",
  "montant": 0,
  "location": {
    "type": "Point",
    "coordinates": [longitude, latitude]
  }
}
```

#### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "declaration": {
      "id": "UUID",
      "dateDeclaration": "ISO8601",
      "statut": "EN_ATTENTE",
      "source": "EN_LIGNE",
      "propriete": {
        "id": "UUID",
        "type": "string",
        "localite": "string",
        "rangLocalite": 0,
        "superficie": 0,
        "adresse": "string",
        "latitude": 0,
        "longitude": 0
      },
      "concession": {
        "id": "UUID",
        "nombreCarresMinier": 0,
        "superficie": 0,
        "dateAcquisition": "ISO8601",
        "type": "string"
      },
      "contribuable": {
        "id": "UUID",
        "nom": "string",
        "adressePrincipale": "string",
        "adresseSecondaire": "string",
        "telephonePrincipal": "string",
        "telephoneSecondaire": "string",
        "email": "string",
        "nationalite": "string",
        "type": "string",
        "idNat": "string",
        "nrc": "string",
        "sigle": "string",
        "numeroIdentificationContribuable": "string"
      },
      "montant": 0,
      "typeImpot": "string"
    }
  },
  "error": null,
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

### Valider une déclaration

`POST /api/declarations/{id}/valider`

#### Description
Valide une déclaration soumise par un contribuable.

#### Rôles autorisés
- TAXATEUR
- RECEVEUR_DES_IMPOTS
- ADMIN

#### Réponse en cas de succès
```json
{
  "success": true,
  "data": {
    "declaration": {
      "id": "UUID",
      "dateDeclaration": "ISO8601",
      "statut": "VALIDEE",
      "source": "EN_LIGNE",
      "propriete": {
        "id": "UUID",
        "type": "string",
        "localite": "string",
        "rangLocalite": 0,
        "superficie": 0,
        "adresse": "string",
        "latitude": 0,
        "longitude": 0
      },
      "concession": {
        "id": "UUID",
        "nombreCarresMinier": 0,
        "superficie": 0,
        "dateAcquisition": "ISO8601",
        "type": "string"
      },
      "agentValidateur": {
        "id": "UUID",
        "nom": "string",
        "matricule": "string"
      },
      "contribuable": {
        "id": "UUID",
        "nom": "string",
        "adressePrincipale": "string",
        "adresseSecondaire": "string",
        "telephonePrincipal": "string",
        "telephoneSecondaire": "string",
        "email": "string",
        "nationalite": "string",
        "type": "string",
        "idNat": "string",
        "nrc": "string",
        "sigle": "string",
        "numeroIdentificationContribuable": "string"
      },
      "montant": 0,
      "typeImpot": "string"
    },
    "message": "Déclaration validée avec succès"
  },
  "error": null,
  "meta": {
    "timestamp": "ISO8601",
    "version": "1.0.0"
  }
}
```

## Notifications par email

Lorsqu'une déclaration est validée par un agent, un email est automatiquement envoyé au contribuable avec les informations suivantes :

- ID de la déclaration
- Montant de la déclaration
- Message de confirmation

## Configuration

### Dépendances Maven

Pour que le service d'emails fonctionne correctement, assurez-vous que la dépendance suivante est présente dans le fichier `pom.xml` :

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

### Configuration email

Pour activer les notifications par email, ajoutez les propriétés suivantes dans le fichier `application.properties` :

```properties
# Configuration email
spring.mail.host=smtp.example.com
spring.mail.port=587
spring.mail.username=votre-email@example.com
spring.mail.password=votre-mot-de-passe
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
app.email.enabled=true
```

## Période de déclaration

Les déclarations en ligne ne peuvent être soumises qu'entre le 2 janvier et le 1er février de chaque année.
