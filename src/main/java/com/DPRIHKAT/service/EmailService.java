package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.DemandePlaque;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service pour l'envoi d'emails
 * 
 * @author amateur
 */
@Service
public class    EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username:noreply@dprihkat.cd}")
    private String fromEmail;
    
    @Value("${app.email.enabled:true}")
    private boolean emailEnabled;
    
    /**
     * Envoie un email
     * 
     * @param to Adresse email du destinataire
     * @param subject Sujet de l'email
     * @param text Contenu de l'email
     */
    public void sendEmail(String to, String subject, String text) {
        if (!emailEnabled) {
            logger.info("Envoi d'email désactivé. Email qui aurait été envoyé : À: {}, Sujet: {}, Contenu: {}", 
                    to, subject, text);
            return;
        }
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            
            logger.info("Email envoyé avec succès à {}", to);
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'email à {}: {}", to, e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Envoie une notification de déclaration validée
     * 
     * @param to Adresse email du destinataire
     * @param declarationId ID de la déclaration
     * @param montant Montant de la déclaration
     */
    public void sendDeclarationValidatedNotification(String to, String declarationId, Double montant) {
        String subject = "Déclaration validée - Notification";
        String text = String.format(
            "Bonjour,\n\n" +
            "Votre déclaration (ID: %s) a été validée avec succès.\n" +
            "Montant de la déclaration: %,.2f FC\n\n" +
            "Veuillez procéder au paiement dans les délais impartis.\n\n" +
            "Cordialement,\n" +
            "Direction des Recettes Provinciales de l'Intérieur de la Province du Haut-Katanga",
            declarationId, montant
        );
        
        sendEmail(to, subject, text);
    }
    
    /**
     * Envoie une notification de validation de demande de plaque
     * 
     * @param demande La demande de plaque validée
     */
    public void envoyerNotificationValidation(DemandePlaque demande) {
        if (demande.getContribuable() == null || demande.getContribuable().getEmail() == null) {
            logger.warn("Impossible d'envoyer la notification de validation : contribuable ou email manquant");
            return;
        }
        
        String to = demande.getContribuable().getEmail();
        String subject = "Demande de plaque validée - Notification";
        String text = String.format(
            "Bonjour %s,\n\n" +
            "Votre demande de plaque d'immatriculation (ID: %s) pour votre véhicule %s %s a été validée avec succès.\n\n" +
            "Une note de taxation a été générée. Veuillez procéder au paiement dans les délais impartis.\n\n" +
            "Cordialement,\n" +
            "Direction des Recettes Provinciales de l'Intérieur de la Province du Haut-Katanga",
            demande.getContribuable().getNom(),
            demande.getId(),
            demande.getVehicule().getMarque(),
            demande.getVehicule().getModele()
        );
        
        sendEmail(to, subject, text);
    }
    
    /**
     * Envoie une notification de rejet de demande de plaque
     * 
     * @param demande La demande de plaque rejetée
     */
    public void envoyerNotificationRejet(DemandePlaque demande) {
        if (demande.getContribuable() == null || demande.getContribuable().getEmail() == null) {
            logger.warn("Impossible d'envoyer la notification de rejet : contribuable ou email manquant");
            return;
        }
        
        String to = demande.getContribuable().getEmail();
        String subject = "Demande de plaque rejetée - Notification";
        String text = String.format(
            "Bonjour %s,\n\n" +
            "Votre demande de plaque d'immatriculation (ID: %s) pour votre véhicule %s %s a été rejetée.\n\n" +
            "Motif du rejet : %s\n\n" +
            "Pour plus d'informations, veuillez contacter nos services.\n\n" +
            "Cordialement,\n" +
            "Direction des Recettes Provinciales de l'Intérieur de la Province du Haut-Katanga",
            demande.getContribuable().getNom(),
            demande.getId(),
            demande.getVehicule().getMarque(),
            demande.getVehicule().getModele(),
            demande.getMotifRejet()
        );
        
        sendEmail(to, subject, text);
    }
    
    /**
     * Envoie une notification de paiement de demande de plaque
     * 
     * @param demande La demande de plaque payée
     */
    public void envoyerNotificationPaiement(DemandePlaque demande) {
        if (demande.getContribuable() == null || demande.getContribuable().getEmail() == null) {
            logger.warn("Impossible d'envoyer la notification de paiement : contribuable ou email manquant");
            return;
        }
        
        String to = demande.getContribuable().getEmail();
        String subject = "Paiement de plaque confirmé - Notification";
        String text = String.format(
            "Bonjour %s,\n\n" +
            "Nous confirmons la réception du paiement pour votre demande de plaque d'immatriculation (ID: %s) " +
            "pour votre véhicule %s %s.\n\n" +
            "Votre plaque est en cours de préparation et vous serez notifié(e) lorsqu'elle sera prête à être livrée.\n\n" +
            "Cordialement,\n" +
            "Direction des Recettes Provinciales de l'Intérieur de la Province du Haut-Katanga",
            demande.getContribuable().getNom(),
            demande.getId(),
            demande.getVehicule().getMarque(),
            demande.getVehicule().getModele()
        );
        
        sendEmail(to, subject, text);
    }
    
    /**
     * Envoie une notification de livraison de plaque
     * 
     * @param demande La demande de plaque livrée
     */
    public void envoyerNotificationLivraison(DemandePlaque demande) {
        if (demande.getContribuable() == null || demande.getContribuable().getEmail() == null) {
            logger.warn("Impossible d'envoyer la notification de livraison : contribuable ou email manquant");
            return;
        }
        
        String to = demande.getContribuable().getEmail();
        String subject = "Plaque d'immatriculation prête - Notification";
        String text = String.format(
            "Bonjour %s,\n\n" +
            "Votre plaque d'immatriculation pour votre véhicule %s %s est prête à être récupérée.\n\n" +
            "Détails de la plaque :\n" +
            "- Numéro de plaque : %s\n" +
            "- Numéro de série : %s\n\n" +
            "Veuillez vous présenter à nos bureaux avec une pièce d'identité pour récupérer votre plaque.\n\n" +
            "Cordialement,\n" +
            "Direction des Recettes Provinciales de l'Intérieur de la Province du Haut-Katanga",
            demande.getContribuable().getNom(),
            demande.getVehicule().getMarque(),
            demande.getVehicule().getModele(),
            demande.getPlaque() != null ? demande.getPlaque().getNumplaque() : "Non disponible",
            demande.getPlaque() != null ? demande.getPlaque().getNumeroSerie() : "Non disponible"
        );
        
        sendEmail(to, subject, text);
    }
    
    /**
     * Envoie une notification d'attribution de plaque
     * 
     * @param demande La demande de plaque avec plaque attribuée
     */
    public void envoyerNotificationAttribution(DemandePlaque demande) {
        if (demande.getContribuable() == null || demande.getContribuable().getEmail() == null) {
            logger.warn("Impossible d'envoyer la notification d'attribution : contribuable ou email manquant");
            return;
        }
        
        String to = demande.getContribuable().getEmail();
        String subject = "Plaque d'immatriculation attribuée - Notification";
        String text = String.format(
            "Bonjour %s,\n\n" +
            "Une plaque d'immatriculation a été attribuée à votre véhicule %s %s.\n\n" +
            "Détails de la plaque :\n" +
            "- Numéro de plaque : %s\n" +
            "- Numéro de série : %s\n\n" +
            "Votre plaque est en cours de préparation. Vous serez notifié(e) lorsqu'elle sera prête à être récupérée.\n\n" +
            "Cordialement,\n" +
            "Direction des Recettes Provinciales de l'Intérieur de la Province du Haut-Katanga",
            demande.getContribuable().getNom(),
            demande.getVehicule().getMarque(),
            demande.getVehicule().getModele(),
            demande.getPlaque() != null ? demande.getPlaque().getNumplaque() : "Non disponible",
            demande.getPlaque() != null ? demande.getPlaque().getNumeroSerie() : "Non disponible"
        );
        
        sendEmail(to, subject, text);
    }
}
