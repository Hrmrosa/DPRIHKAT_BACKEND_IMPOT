package com.DPRIHKAT.repository;

import com.DPRIHKAT.entity.Apurement;
import com.DPRIHKAT.entity.enums.StatutApurement;
import com.DPRIHKAT.entity.enums.TypeApurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public interface ApurementRepository extends JpaRepository<Apurement, UUID> {
    @Query("SELECT a FROM Apurement a WHERE a.taxation.declaration.id = :declarationId")
    Apurement findByDeclarationId(@Param("declarationId") UUID declarationId);
    
    /**
     * Trouve les apurements par ID de déclaration
     * @param declarationId L'ID de la déclaration
     * @return Liste des apurements correspondants
     */
    List<Apurement> findByDeclaration_Id(UUID declarationId);
    
    /**
     * Trouve l'apurement associé à un paiement
     * @param paiementId L'ID du paiement
     * @return L'apurement correspondant
     */
    Apurement findByPaiement_Id(UUID paiementId);
    
    List<Apurement> findByStatut(StatutApurement statut);
    List<Apurement> findByType(TypeApurement type);
    List<Apurement> findByAgent_Id(UUID agentId);
    
    /**
     * Compte le nombre d'apurements par type entre deux dates
     * @param dateDebut Date de début
     * @param dateFin Date de fin
     * @return Map avec le type d'apurement comme clé et le nombre comme valeur
     */
    @Query("SELECT a.type as type, COUNT(a) as count FROM Apurement a " +
           "WHERE a.dateDemande BETWEEN :dateDebut AND :dateFin " +
           "GROUP BY a.type")
    List<Map<String, Object>> countByTypeBetweenDates(
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin);
    
    /**
     * Calcule la somme des montants apurés par type entre deux dates
     * @param dateDebut Date de début
     * @param dateFin Date de fin
     * @return Map avec le type d'apurement comme clé et le montant comme valeur
     */
    @Query("SELECT a.type as type, SUM(a.montantApure) as montant FROM Apurement a " +
           "WHERE a.dateDemande BETWEEN :dateDebut AND :dateFin " +
           "GROUP BY a.type")
    List<Map<String, Object>> sumMontantByTypeBetweenDates(
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin);
    
    /**
     * Calcule la somme totale des montants apurés entre deux dates
     * @param dateDebut Date de début
     * @param dateFin Date de fin
     * @return Somme totale des montants apurés
     */
    @Query("SELECT SUM(a.montantApure) FROM Apurement a " +
           "WHERE a.dateDemande BETWEEN :dateDebut AND :dateFin")
    BigDecimal sumMontantBetweenDates(
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin);
    
    /**
     * Compte le nombre d'apurements par statut entre deux dates
     * @param dateDebut Date de début
     * @param dateFin Date de fin
     * @return Map avec le statut d'apurement comme clé et le nombre comme valeur
     */
    @Query("SELECT a.statut as statut, COUNT(a) as count FROM Apurement a " +
           "WHERE a.dateDemande BETWEEN :dateDebut AND :dateFin " +
           "GROUP BY a.statut")
    List<Map<String, Object>> countByStatutBetweenDates(
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin);
    
    /**
     * Compte le nombre d'apurements par mois entre deux dates
     * @param dateDebut Date de début
     * @param dateFin Date de fin
     * @return Map avec le mois comme clé et le nombre comme valeur
     */
    @Query("SELECT FUNCTION('YEAR', a.dateDemande) as annee, " +
           "FUNCTION('MONTH', a.dateDemande) as mois, " +
           "COUNT(a) as count FROM Apurement a " +
           "WHERE a.dateDemande BETWEEN :dateDebut AND :dateFin " +
           "GROUP BY FUNCTION('YEAR', a.dateDemande), FUNCTION('MONTH', a.dateDemande) " +
           "ORDER BY annee, mois")
    List<Map<String, Object>> countByMonthBetweenDates(
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin);
}
