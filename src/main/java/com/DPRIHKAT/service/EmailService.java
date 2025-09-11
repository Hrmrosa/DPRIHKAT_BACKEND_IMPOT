package com.DPRIHKAT.service;

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
public class EmailService {
    
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
}
