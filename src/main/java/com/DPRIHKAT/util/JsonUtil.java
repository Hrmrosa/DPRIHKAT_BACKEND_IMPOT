package com.DPRIHKAT.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilitaire pour manipuler le JSON
 */
public class JsonUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Convertit un objet en JSON
     * @param object Objet à convertir
     * @return Chaîne JSON
     */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("Erreur lors de la conversion en JSON", e);
            return "{}";
        }
    }
    
    /**
     * Convertit une chaîne JSON en objet
     * @param json Chaîne JSON
     * @param clazz Classe de l'objet
     * @return Objet
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            logger.error("Erreur lors de la conversion depuis JSON", e);
            return null;
        }
    }
}
