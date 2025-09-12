# Optimisation du chargement des taxations

## Problème rencontré

Le système a rencontré une erreur PostgreSQL lors du chargement des taxations :

```
ERROR: target lists can have at most 1664 entries
```

Cette erreur se produit lorsque Hibernate génère une requête SQL avec trop de colonnes. Dans notre cas, l'entité `Taxation` possède de nombreuses relations avec d'autres entités, et le chargement par défaut (EAGER) de toutes ces relations génère une requête SQL avec plus de 1664 colonnes, dépassant ainsi la limite de PostgreSQL.

## Solutions mises en œuvre

### 1. Modification de l'entité Taxation pour utiliser le chargement paresseux (LAZY)

Nous avons modifié toutes les relations dans l'entité `Taxation` pour utiliser le chargement paresseux (FetchType.LAZY) au lieu du chargement avide (EAGER) par défaut :

```java
// Avant
@ManyToOne
@JoinColumn(name = "declaration_id")
@JsonIdentityReference(alwaysAsId = true)
private Declaration declaration;

// Après
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "declaration_id")
@JsonIdentityReference(alwaysAsId = true)
private Declaration declaration;
```

Cette modification a été appliquée à toutes les relations de l'entité `Taxation` :
- declaration
- natureImpot
- agent
- paiement
- apurements
- proprieteImpot
- contribuable
- propriete

### 2. Création d'une méthode personnalisée dans le repository

Nous avons ajouté une méthode personnalisée dans le repository `TaxationRepository` pour charger une taxation par ID sans effectuer de jointures excessives :

```java
/**
 * Trouve une taxation par son ID sans charger les relations profondes
 * Cette méthode utilise une requête JPQL personnalisée pour éviter les jointures excessives
 * @param id l'ID de la taxation
 * @return la taxation correspondante
 */
@Query("SELECT t FROM Taxation t WHERE t.id = :id")
Taxation findByIdWithoutJoins(@Param("id") UUID id);
```

### 3. Optimisation du service TaxationService

Nous avons modifié la méthode `getTaxationById` dans le service `TaxationService` pour utiliser la nouvelle méthode du repository :

```java
/**
 * Récupère une taxation par son ID
 * @param id L'ID de la taxation
 * @return La taxation correspondante, si elle existe
 */
public Optional<Taxation> getTaxationById(UUID id) {
    try {
        Taxation taxation = taxationRepository.findByIdWithoutJoins(id);
        return Optional.ofNullable(taxation);
    } catch (Exception e) {
        logger.error("Erreur lors de la récupération de la taxation avec l'ID: {}", id, e);
        return Optional.empty();
    }
}
```

### 4. Utilisation de DTOs dans le contrôleur

Le contrôleur `TaxationController` utilise déjà des DTOs pour les réponses, ce qui permet de limiter les données renvoyées au client et d'éviter les problèmes de sérialisation des entités avec des relations circulaires.

## Résultats

Ces modifications ont permis de résoudre l'erreur de limite de colonnes PostgreSQL en :
1. Réduisant le nombre de jointures effectuées lors du chargement des taxations
2. Chargeant les relations uniquement lorsqu'elles sont nécessaires (lazy loading)
3. Utilisant des DTOs pour la sérialisation des données

## Recommandations pour l'avenir

1. **Utiliser le chargement paresseux par défaut** : Pour les entités avec de nombreuses relations, privilégier le chargement paresseux (FetchType.LAZY) par défaut.
2. **Créer des requêtes personnalisées** : Pour les opérations de lecture, créer des requêtes JPQL ou SQL personnalisées qui ne chargent que les données nécessaires.
3. **Utiliser des projections ou des DTOs** : Pour les réponses API, utiliser des projections ou des DTOs qui ne contiennent que les champs nécessaires.
4. **Éviter les relations bidirectionnelles inutiles** : Limiter les relations bidirectionnelles aux cas où elles sont réellement nécessaires.
5. **Surveiller les performances** : Utiliser des outils comme Hibernate Statistics ou Spring Boot Actuator pour surveiller les performances des requêtes et détecter les problèmes potentiels.
