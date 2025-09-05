package com.DPRIHKAT.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service pour l'envoi d'emails (version de développement qui simule l'envoi)
 * @author amateur
 */
@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    /**
     * Simule l'envoi d'un email simple en le journalisant
     * @param to L'adresse email du destinataire
     * @param subject Le sujet de l'email
     * @param text Le contenu de l'email
     */
    public void sendEmail(String to, String subject, String text) {
        try {
            logger.info("=== SIMULATION D'ENVOI D'EMAIL ===");
            logger.info("À: {}", to);
            logger.info("Sujet: {}", subject);
            logger.info("Contenu: {}", text);
            logger.info("=== FIN DE LA SIMULATION ===");
        } catch (Exception e) {
            logger.error("Erreur lors de la simulation d'envoi d'email à : {}", to, e);
            throw e;
        }
    }
}
