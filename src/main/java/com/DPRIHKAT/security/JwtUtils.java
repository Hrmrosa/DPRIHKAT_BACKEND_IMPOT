package com.DPRIHKAT.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    
    // Pattern pour vérifier la structure de base d'un JWT (3 segments séparés par des points)
    private static final Pattern JWT_PATTERN = Pattern.compile("^[A-Za-z0-9\\-_\\.]+\\.[A-Za-z0-9\\-_\\.]+\\.[A-Za-z0-9\\-_\\.]+$");

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getId().toString()) // Utiliser l'UUID au lieu du login
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String getUserIdFromJwtToken(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key()).build()
                    .parseClaimsJws(token).getBody().getSubject();
        } catch (Exception e) {
            logger.error("Error getting user ID from token: {}", e.getMessage());
            throw e;
        }
    }

    public UUID getUserUUIDFromJwtToken(String token) {
        try {
            String id = getUserIdFromJwtToken(token);
            return UUID.fromString(id);
        } catch (Exception e) {
            logger.error("Error converting user ID to UUID: {}", e.getMessage());
            throw e;
        }
    }

    public boolean validateJwtToken(String authToken) {
        if (authToken == null || authToken.isEmpty()) {
            logger.error("JWT token is empty");
            return false;
        }
        
        try {
            // Vérifier si le token a la structure de base d'un JWT (3 segments séparés par des points)
            if (!JWT_PATTERN.matcher(authToken).matches()) {
                logger.error("JWT token structure is invalid");
                return false;
            }
            
            // Vérifier si le token contient des caractères non valides pour base64url
            if (authToken.contains(" ") || authToken.contains("\t") || authToken.contains("\n") || authToken.contains("\r")) {
                logger.error("JWT token contains invalid characters");
                return false;
            }
            
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (DecodingException e) {
            logger.error("JWT token contains invalid base64url characters: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error while validating JWT token: {}", e.getMessage());
        }

        return false;
    }
}
