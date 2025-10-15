package com.DPRIHKAT.aspect;

import com.DPRIHKAT.entity.*;
import com.DPRIHKAT.service.DashboardNotificationService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Aspect pour capturer les événements de modification des entités
 * et déclencher les mises à jour du dashboard en temps réel
 */
@Aspect
@Component
public class DashboardUpdateAspect {

    private static final Logger logger = LoggerFactory.getLogger(DashboardUpdateAspect.class);
    
    private final DashboardNotificationService notificationService;
    
    @Autowired
    public DashboardUpdateAspect(DashboardNotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    /**
     * Capture les événements de création/mise à jour de contribuables
     */
    @AfterReturning(
            pointcut = "execution(* com.DPRIHKAT.repository.ContribuableRepository.save*(..)) || " +
                      "execution(* com.DPRIHKAT.repository.ContribuableRepository.saveAll(..)) || " +
                      "execution(* com.DPRIHKAT.service.ContribuableService.*(..)) && !execution(* com.DPRIHKAT.service.ContribuableService.get*(..))",
            returning = "result")
    public void afterContribuableUpdate(JoinPoint joinPoint, Object result) {
        logger.info("Contribuable créé ou mis à jour, notification du dashboard");
        if (result instanceof Contribuable) {
            notificationService.notifySpecificUpdate("contribuable", result);
        } else {
            notificationService.notifyDashboardUpdate();
        }
    }
    
    /**
     * Capture les événements de création/mise à jour de propriétés
     */
    @AfterReturning(
            pointcut = "execution(* com.DPRIHKAT.repository.ProprieteRepository.save*(..)) || " +
                      "execution(* com.DPRIHKAT.repository.ProprieteRepository.saveAll(..)) || " +
                      "execution(* com.DPRIHKAT.service.ProprieteService.*(..)) && !execution(* com.DPRIHKAT.service.ProprieteService.get*(..))",
            returning = "result")
    public void afterProprieteUpdate(JoinPoint joinPoint, Object result) {
        logger.info("Propriété créée ou mise à jour, notification du dashboard");
        if (result instanceof Propriete) {
            notificationService.notifySpecificUpdate("propriete", result);
        } else {
            notificationService.notifyDashboardUpdate();
        }
    }
    
    /**
     * Capture les événements de création/mise à jour de véhicules
     */
    @AfterReturning(
            pointcut = "execution(* com.DPRIHKAT.repository.VehiculeRepository.save*(..)) || " +
                      "execution(* com.DPRIHKAT.repository.VehiculeRepository.saveAll(..)) || " +
                      "execution(* com.DPRIHKAT.service.VehiculeService.*(..)) && !execution(* com.DPRIHKAT.service.VehiculeService.get*(..))",
            returning = "result")
    public void afterVehiculeUpdate(JoinPoint joinPoint, Object result) {
        logger.info("Véhicule créé ou mis à jour, notification du dashboard");
        if (result instanceof Vehicule) {
            notificationService.notifySpecificUpdate("vehicule", result);
        } else {
            notificationService.notifyDashboardUpdate();
        }
    }
    
    /**
     * Capture les événements de création/mise à jour de taxations
     */
    @AfterReturning(
            pointcut = "execution(* com.DPRIHKAT.repository.TaxationRepository.save*(..)) || " +
                      "execution(* com.DPRIHKAT.repository.TaxationRepository.saveAll(..)) || " +
                      "execution(* com.DPRIHKAT.service.TaxationService.*(..)) && !execution(* com.DPRIHKAT.service.TaxationService.get*(..))",
            returning = "result")
    public void afterTaxationUpdate(JoinPoint joinPoint, Object result) {
        logger.info("Taxation créée ou mise à jour, notification du dashboard");
        if (result instanceof Taxation) {
            notificationService.notifySpecificUpdate("taxation", result);
        } else {
            notificationService.notifyDashboardUpdate();
        }
    }
    
    /**
     * Capture les événements de création/mise à jour de paiements
     */
    @AfterReturning(
            pointcut = "execution(* com.DPRIHKAT.repository.PaiementRepository.save*(..)) || " +
                      "execution(* com.DPRIHKAT.repository.PaiementRepository.saveAll(..)) || " +
                      "execution(* com.DPRIHKAT.service.PaiementService.*(..)) && !execution(* com.DPRIHKAT.service.PaiementService.get*(..))",
            returning = "result")
    public void afterPaiementUpdate(JoinPoint joinPoint, Object result) {
        logger.info("Paiement créé ou mis à jour, notification du dashboard");
        if (result instanceof Paiement) {
            notificationService.notifySpecificUpdate("paiement", result);
        } else {
            notificationService.notifyDashboardUpdate();
        }
    }
    
    /**
     * Capture les événements de création/mise à jour de déclarations
     */
    @AfterReturning(
            pointcut = "execution(* com.DPRIHKAT.repository.DeclarationRepository.save*(..)) || " +
                      "execution(* com.DPRIHKAT.repository.DeclarationRepository.saveAll(..)) || " +
                      "execution(* com.DPRIHKAT.service.DeclarationService.*(..)) && !execution(* com.DPRIHKAT.service.DeclarationService.get*(..))",
            returning = "result")
    public void afterDeclarationUpdate(JoinPoint joinPoint, Object result) {
        logger.info("Déclaration créée ou mise à jour, notification du dashboard");
        if (result instanceof Declaration) {
            notificationService.notifySpecificUpdate("declaration", result);
        } else {
            notificationService.notifyDashboardUpdate();
        }
    }
}
