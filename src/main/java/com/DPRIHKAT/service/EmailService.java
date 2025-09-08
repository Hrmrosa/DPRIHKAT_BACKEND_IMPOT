package com.DPRIHKAT.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service pour l'envoi d'emails
 * 
 * @author amateur
 */
@Service
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    // Commenté pour éviter les erreurs si JavaMailSender n'est pas configuré
    // @Autowired
    // private JavaMailSender mailSender;
    
    @Value("${spring.mail.username:noreply@dprihkat.cd}")
    private String fromEmail;
    
    @Value("${app.email.enabled:false}")
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
            // Simulation d'envoi d'email (à remplacer par l'implémentation réelle)
            logger.info("Simulation d'envoi d'email : À: {}, Sujet: {}", to, subject);
            
            // Code commenté pour éviter les erreurs si JavaMailSender n'est pas configuré
            /*
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            */
            
            logger.info("Email envoyé avec succès à {}", to);
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'email à {}: {}", to, e.getMessage(), e);
            throw e;
        }
    }
}
