package com.DPRIHKAT.controller;

import com.DPRIHKAT.dto.JwtResponse;
import com.DPRIHKAT.dto.LoginRequest;
import com.DPRIHKAT.dto.MessageResponse;
import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.DPRIHKAT.security.JwtUtils;
import com.DPRIHKAT.security.UserDetailsImpl;
import com.DPRIHKAT.service.AuditLogService;
import com.DPRIHKAT.util.LetsCrypt;
import com.DPRIHKAT.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UtilisateurRepository utilisateurRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private AuditLogService auditLogService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Check if user exists
            Utilisateur utilisateur = utilisateurRepository.findByLogin(loginRequest.getLogin())
                    .orElse(null);

            if (utilisateur == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("USER_NOT_FOUND", "Utilisateur non trouvé", "Aucun utilisateur avec ce login n'existe"));
            }

            // Check if account is blocked
            if (utilisateur.isBloque()) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("ACCOUNT_BLOCKED", "Compte bloqué", "Votre compte a été bloqué. Contactez l'administrateur."));
            }

            // For first connection, force password change
            if (utilisateur.isPremierConnexion()) {
                return ResponseEntity
                        .ok()
                        .body(ResponseUtil.createSuccessResponse(Map.of(
                                "premiereConnexion", true,
                                "message", "Vous devez changer votre mot de passe lors de la première connexion"
                        )));
            }

            // Authenticate user
            String hashedPassword = LetsCrypt.crypt(loginRequest.getMotDePasse());
            if (!hashedPassword.equals(utilisateur.getMotDePasse())) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_CREDENTIALS", "Identifiants invalides", "Le mot de passe est incorrect"));
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getMotDePasse()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            JwtResponse jwtResponse = new JwtResponse(jwt, userDetails.getUsername(), userDetails.getRole());

            Map<String, Object> response = ResponseUtil.createSuccessResponse(Map.of(
                    "token", jwtResponse.getAccessToken(),
                    "type", jwtResponse.getTokenType(),
                    "login", jwtResponse.getLogin(),
                    "role", jwtResponse.getRole()
            ));

            auditLogService.logLogin();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("AUTH_ERROR", "Erreur d'authentification", e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        try {
            String login = authentication.getName();
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", utilisateur.getId());
            userInfo.put("login", utilisateur.getLogin());
            userInfo.put("role", utilisateur.getRole());
            userInfo.put("premierConnexion", utilisateur.isPremierConnexion());
            userInfo.put("bloque", utilisateur.isBloque());
            userInfo.put("isContribuable", utilisateur.isContribuable());
            
            if (utilisateur.getContribuable() != null) {
                Map<String, Object> contribuableInfo = new HashMap<>();
                contribuableInfo.put("id", utilisateur.getContribuable().getId());
                contribuableInfo.put("nom", utilisateur.getContribuable().getNom());
                contribuableInfo.put("type", utilisateur.getContribuable().getType());
                userInfo.put("contribuable", contribuableInfo);
            } else {
                userInfo.put("contribuable", null);
            }
            
            if (utilisateur.getAgent() != null) {
                Map<String, Object> agentInfo = new HashMap<>();
                agentInfo.put("id", utilisateur.getAgent().getId());
                agentInfo.put("nom", utilisateur.getAgent().getNom());
                userInfo.put("agent", agentInfo);
            } else {
                userInfo.put("agent", null);
            }

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(userInfo));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("USER_INFO_ERROR", "Erreur lors de la récupération des informations de l'utilisateur", e.getMessage()));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody Map<String, String> request) {
        try {
            String login = request.get("login");
            String oldPassword = request.get("oldPassword");
            String newPassword = request.get("newPassword");

            // Check if user exists
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElse(null);

            if (utilisateur == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("USER_NOT_FOUND", "Utilisateur non trouvé", "Aucun utilisateur avec ce login n'existe"));
            }

            // Check old password
            String hashedOldPassword = LetsCrypt.crypt(oldPassword);
            if (!hashedOldPassword.equals(utilisateur.getMotDePasse())) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_CREDENTIALS", "Ancien mot de passe incorrect", "L'ancien mot de passe est incorrect"));
            }

            // Update password
            String hashedNewPassword = LetsCrypt.crypt(newPassword);
            utilisateur.setMotDePasse(hashedNewPassword);
            utilisateur.setPremierConnexion(false);
            utilisateurRepository.save(utilisateur);

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "message", "Mot de passe changé avec succès"
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PASSWORD_CHANGE_ERROR", "Erreur lors du changement de mot de passe", e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody Map<String, String> request) {
        try {
            String userId = request.get("userId");
            
            if (userId == null || userId.isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("INVALID_REQUEST", "Requête invalide", "L'identifiant de l'utilisateur est requis"));
            }
            
            // Trouver l'utilisateur par son ID
            Utilisateur utilisateur = utilisateurRepository.findById(UUID.fromString(userId))
                    .orElse(null);

            if (utilisateur == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("USER_NOT_FOUND", "Utilisateur non trouvé", "Aucun utilisateur avec cet ID n'existe"));
            }

            // Réinitialiser le mot de passe à "Tabc@123"
            String defaultPassword = "Tabc@123";
            String hashedPassword = LetsCrypt.crypt(defaultPassword);
            utilisateur.setMotDePasse(hashedPassword);
            utilisateur.setPremierConnexion(true); // Forcer le changement de mot de passe à la prochaine connexion
            utilisateurRepository.save(utilisateur);

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "message", "Mot de passe réinitialisé avec succès"
            )));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("PASSWORD_RESET_ERROR", "Erreur lors de la réinitialisation du mot de passe", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        auditLogService.logLogout();
        return ResponseEntity.ok(new MessageResponse("User logged out successfully!"));
    }
}
