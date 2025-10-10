package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.LogConnexion;
import com.DPRIHKAT.repository.LogConnexionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class LogService {

    private final LogConnexionRepository logConnexionRepository;

    public LogService(LogConnexionRepository logConnexionRepository) {
        this.logConnexionRepository = logConnexionRepository;
    }

    /**
     * Enregistre une connexion utilisateur
     * @param utilisateurId ID de l'utilisateur
     * @param adresseIp Adresse IP de la connexion
     * @param userAgent User-Agent du navigateur
     * @return Le log de connexion créé
     */
    public LogConnexion logConnexion(UUID utilisateurId, String adresseIp, String userAgent) {
        LogConnexion log = new LogConnexion();
        log.setUtilisateurId(utilisateurId);
        log.setDateConnexion(LocalDateTime.now());
        log.setAdresseIp(adresseIp);
        log.setUserAgent(userAgent);
        log.setReussi(true);
        
        return logConnexionRepository.save(log);
    }

    /**
     * Enregistre une tentative de connexion échouée
     * @param login Login utilisé
     * @param adresseIp Adresse IP de la tentative
     * @param userAgent User-Agent du navigateur
     * @param raison Raison de l'échec
     * @return Le log de connexion créé
     */
    public LogConnexion logEchecConnexion(String login, String adresseIp, String userAgent, String raison) {
        LogConnexion log = new LogConnexion();
        log.setLogin(login);
        log.setDateConnexion(LocalDateTime.now());
        log.setAdresseIp(adresseIp);
        log.setUserAgent(userAgent);
        log.setReussi(false);
        log.setRaison(raison);
        
        return logConnexionRepository.save(log);
    }

    /**
     * Récupère les derniers logs de connexion
     * @param limit Nombre maximum de logs à récupérer
     * @return Liste des logs de connexion
     */
    public List<Map<String, Object>> getLastConnectionLogs(int limit) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // Récupérer les derniers logs de connexion
        List<LogConnexion> logs = logConnexionRepository.findTopByOrderByDateConnexionDesc(limit);
        
        // Convertir en liste de maps
        for (LogConnexion log : logs) {
            Map<String, Object> logData = new HashMap<>();
            logData.put("id", log.getId().toString());
            logData.put("utilisateurId", log.getUtilisateurId() != null ? log.getUtilisateurId().toString() : null);
            logData.put("login", log.getLogin());
            logData.put("dateConnexion", log.getDateConnexion().toString());
            logData.put("adresseIp", log.getAdresseIp());
            logData.put("userAgent", log.getUserAgent());
            logData.put("reussi", log.isReussi());
            logData.put("raison", log.getRaison());
            result.add(logData);
        }
        
        return result;
    }

    /**
     * Récupère les logs de connexion pour un utilisateur spécifique
     * @param utilisateurId ID de l'utilisateur
     * @param limit Nombre maximum de logs à récupérer
     * @return Liste des logs de connexion
     */
    public List<Map<String, Object>> getUserConnectionLogs(UUID utilisateurId, int limit) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // Récupérer les logs de connexion de l'utilisateur
        List<LogConnexion> logs = logConnexionRepository.findByUtilisateurIdOrderByDateConnexionDesc(utilisateurId, limit);
        
        // Convertir en liste de maps
        for (LogConnexion log : logs) {
            Map<String, Object> logData = new HashMap<>();
            logData.put("id", log.getId().toString());
            logData.put("dateConnexion", log.getDateConnexion().toString());
            logData.put("adresseIp", log.getAdresseIp());
            logData.put("userAgent", log.getUserAgent());
            logData.put("reussi", log.isReussi());
            result.add(logData);
        }
        
        return result;
    }
}
