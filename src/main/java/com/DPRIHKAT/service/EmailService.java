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
 */
@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * Envoie un email simple
     * @param to adresse email du destinataire
     * @param subject sujet de l'email
     * @param text contenu de l'email
     */
    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            logger.info("Email envoyé avec succès à {}", to);
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'email à {}: {}", to, e.getMessage());
        }
    }
    
    /**
     * Méthode de compatibilité pour ContribuableService
     * @param to adresse email du destinataire
     * @param subject sujet de l'email
     * @param text contenu de l'email
     */
    public void sendEmail(String to, String subject, String text) {
        sendSimpleEmail(to, subject, text);
    }

    /**
     * Envoie un email avec les identifiants de connexion à un contribuable
     * @param email adresse email du contribuable
     * @param nom nom du contribuable
     * @param login identifiant de connexion
     * @param motDePasse mot de passe
     */
    public void envoyerIdentifiantsContribuable(String email, String nom, String login, String motDePasse) {
        String subject = "DPRIHKAT - Vos identifiants de connexion";
        String text = "Bonjour " + nom + ",\n\n" +
                "Votre compte a été créé sur la plateforme DPRIHKAT.\n\n" +
                "Voici vos identifiants de connexion :\n" +
                "Login : " + login + "\n" +
                "Mot de passe : " + motDePasse + "\n\n" +
                "Lors de votre première connexion, vous serez invité à changer votre mot de passe.\n\n" +
                "Cordialement,\n" +
                "L'équipe DPRIHKAT";
        
        sendSimpleEmail(email, subject, text);
    }
}
