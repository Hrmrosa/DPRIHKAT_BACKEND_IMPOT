package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Agent;
import com.DPRIHKAT.entity.Bureau;
import com.DPRIHKAT.entity.Contribuable;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.enums.Role;
import com.DPRIHKAT.entity.enums.Sexe;
import com.DPRIHKAT.repository.AgentRepository;
import com.DPRIHKAT.repository.BureauRepository;
import com.DPRIHKAT.repository.ContribuableRepository;
import com.DPRIHKAT.repository.UtilisateurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

/**
 * Service pour gérer les contribuables
 * @author amateur
 */
@Service
public class ContribuableService {

    private static final Logger logger = LoggerFactory.getLogger(ContribuableService.class);

    @Autowired
    private ContribuableRepository contribuableRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private BureauRepository bureauRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    /**
     * Récupère tous les contribuables
     * @return Liste de tous les contribuables
     */
    public List<Contribuable> findAll() {
        return contribuableRepository.findAll();
    }

    /**
     * Récupère un contribuable par son ID
     * @param id L'ID du contribuable
     * @return Le contribuable correspondant, s'il existe
     */
    public Contribuable findById(UUID id) {
        return contribuableRepository.findById(id).orElse(null);
    }

    /**
     * Crée un nouveau contribuable avec un compte utilisateur
     * @param contribuable Le contribuable à créer
     * @return Le contribuable créé
     */
    @Transactional
    public Contribuable createContribuable(Contribuable contribuable) {
        // Sauvegarder le contribuable
        Contribuable savedContribuable = contribuableRepository.save(contribuable);
        
        // Créer un agent pour le contribuable
        Agent agent = createAgentForContribuable(savedContribuable);
        
        // Créer un utilisateur pour le contribuable
        Utilisateur utilisateur = createUtilisateurForContribuable(savedContribuable, agent);
        
        // Envoyer les identifiants par email
        sendCredentialsByEmail(utilisateur, savedContribuable.getEmail());
        
        return savedContribuable;
    }

    /**
     * Crée un agent pour un contribuable
     * @param contribuable Le contribuable
     * @return L'agent créé
     */
    private Agent createAgentForContribuable(Contribuable contribuable) {
        // Récupérer un bureau par défaut pour les contribuables
        Bureau bureau = bureauRepository.findByNom("Bureau des Contribuables")
                .orElseGet(() -> {
                    Bureau newBureau = new Bureau();
                    newBureau.setNom("Bureau des Contribuables");
                    newBureau.setAdresse("Direction Provinciale des Recettes");
                    return bureauRepository.save(newBureau);
                });
        
        // Créer un agent pour le contribuable
        Agent agent = new Agent();
        agent.setNom(contribuable.getNom());
        agent.setSexe(Sexe.MASCULIN); // Par défaut, à modifier si nécessaire
        agent.setMatricule("CONT-" + contribuable.getNumeroIdentificationContribuable());
        agent.setBureau(bureau);
        
        return agentRepository.save(agent);
    }

    /**
     * Crée un utilisateur pour un contribuable
     * @param contribuable Le contribuable
     * @param agent L'agent associé au contribuable
     * @return L'utilisateur créé
     */
    private Utilisateur createUtilisateurForContribuable(Contribuable contribuable, Agent agent) {
        // Générer un nom d'utilisateur et un mot de passe
        String username = generateUsername();
        String password = "Tabc@123"; // Mot de passe par défaut
        
        // Créer l'utilisateur
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setLogin(username);
        utilisateur.setMotDePasse(passwordEncoder.encode(password));
        utilisateur.setRole(Role.CONTRIBUABLE);
        utilisateur.setPremierConnexion(true);
        utilisateur.setBloque(false);
        utilisateur.setContribuable(contribuable);
        utilisateur.setAgent(agent);
        
        // Sauvegarder l'utilisateur
        Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur);
        
        // Mettre à jour les relations
        contribuable.setUtilisateur(savedUtilisateur);
        agent.setUtilisateur(savedUtilisateur);
        
        contribuableRepository.save(contribuable);
        agentRepository.save(agent);
        
        return savedUtilisateur;
    }

    /**
     * Génère un nom d'utilisateur unique pour un contribuable
     * @return Le nom d'utilisateur généré
     */
    private String generateUsername() {
        String prefix = "dpri_c";
        String randomChars = generateRandomChars(4);
        String username = prefix + randomChars;
        
        // Vérifier si le nom d'utilisateur existe déjà
        while (utilisateurRepository.findByLogin(username).isPresent()) {
            randomChars = generateRandomChars(4);
            username = prefix + randomChars;
        }
        
        return username;
    }

    /**
     * Génère une chaîne aléatoire de caractères
     * @param length La longueur de la chaîne
     * @return La chaîne générée
     */
    private String generateRandomChars(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        
        return sb.toString();
    }

    /**
     * Envoie les identifiants par email
     * @param utilisateur L'utilisateur
     * @param email L'adresse email du destinataire
     */
    private void sendCredentialsByEmail(Utilisateur utilisateur, String email) {
        if (email == null || email.isEmpty()) {
            logger.warn("Impossible d'envoyer les identifiants par email : adresse email non spécifiée");
            return;
        }
        
        String subject = "Vos identifiants de connexion DPRIHKAT";
        String message = "Bonjour,\n\n"
                + "Voici vos identifiants de connexion au système DPRIHKAT :\n"
                + "Nom d'utilisateur : " + utilisateur.getLogin() + "\n"
                + "Mot de passe : Tabc@123\n\n"
                + "Veuillez changer votre mot de passe lors de votre première connexion.\n\n"
                + "Cordialement,\n"
                + "L'équipe DPRIHKAT";
        
        try {
            emailService.sendEmail(email, subject, message);
            logger.info("Identifiants envoyés par email à : {}", email);
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi des identifiants par email", e);
        }
    }

    /**
     * Met à jour un contribuable existant
     * @param id L'ID du contribuable
     * @param contribuable Les nouvelles informations du contribuable
     * @return Le contribuable mis à jour, s'il existe
     */
    public Contribuable update(UUID id, Contribuable contribuable) {
        if (contribuableRepository.existsById(id)) {
            contribuable.setId(id);
            return contribuableRepository.save(contribuable);
        }
        return null;
    }

    /**
     * Supprime un contribuable par son ID
     * @param id L'ID du contribuable
     */
    public void deleteById(UUID id) {
        contribuableRepository.deleteById(id);
    }
}
