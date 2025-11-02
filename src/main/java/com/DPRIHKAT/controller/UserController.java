package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.Utilisateur;
import com.DPRIHKAT.entity.enums.Role;
import com.DPRIHKAT.repository.UtilisateurRepository;
import com.DPRIHKAT.util.LetsCrypt;
import com.DPRIHKAT.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UtilisateurRepository utilisateurRepository;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INFORMATICIEN')")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Utilisateur> utilisateurPage;
            if (search != null && !search.isEmpty()) {
                utilisateurPage = utilisateurRepository.findBySearchTerm(search, pageable);
            } else {
                utilisateurPage = utilisateurRepository.findAll(pageable);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("users", utilisateurPage.getContent());
            response.put("currentPage", utilisateurPage.getNumber());
            response.put("totalItems", utilisateurPage.getTotalElements());
            response.put("totalPages", utilisateurPage.getTotalPages());

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(response));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("USER_FETCH_ERROR", "Erreur lors de la récupération des utilisateurs", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INFORMATICIEN')")
    public ResponseEntity<?> getUserById(@PathVariable UUID id) {
        try {
            Utilisateur utilisateur = utilisateurRepository.findById(id)
                    .orElse(null);

            if (utilisateur == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("USER_NOT_FOUND", "Utilisateur non trouvé", "Aucun utilisateur avec cet ID n'existe"));
            }

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("user", utilisateur)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("USER_FETCH_ERROR", "Erreur lors de la récupération de l'utilisateur", e.getMessage()));
        }
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INFORMATICIEN')")
    public ResponseEntity<?> createUser(@Valid @RequestBody Utilisateur utilisateur) {
        try {
            if (utilisateurRepository.existsByLogin(utilisateur.getLogin())) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("USER_EXISTS", "Login déjà utilisé", "Un utilisateur avec ce login existe déjà"));
            }

            // Hash password
            String hashedPassword = LetsCrypt.crypt(utilisateur.getMotDePasse());
            utilisateur.setMotDePasse(hashedPassword);
            utilisateur.setPremierConnexion(true);

            Utilisateur savedUser = utilisateurRepository.save(utilisateur);

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("user", savedUser)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("USER_CREATE_ERROR", "Erreur lors de la création de l'utilisateur", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INFORMATICIEN')")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @Valid @RequestBody Utilisateur utilisateurDetails) {
        try {
            Utilisateur utilisateur = utilisateurRepository.findById(id)
                    .orElse(null);

            if (utilisateur == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("USER_NOT_FOUND", "Utilisateur non trouvé", "Aucun utilisateur avec cet ID n'existe"));
            }

            // Update user details
            utilisateur.setLogin(utilisateurDetails.getLogin());
            utilisateur.setRole(utilisateurDetails.getRole());
            utilisateur.setPremierConnexion(utilisateurDetails.isPremierConnexion());
            utilisateur.setBloque(utilisateurDetails.isBloque());
            utilisateur.setContribuable(utilisateurDetails.getContribuable());
            utilisateur.setAgent(utilisateurDetails.getAgent());

            Utilisateur updatedUser = utilisateurRepository.save(utilisateur);

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("user", updatedUser)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("USER_UPDATE_ERROR", "Erreur lors de la mise à jour de l'utilisateur", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INFORMATICIEN')")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        try {
            Utilisateur utilisateur = utilisateurRepository.findById(id)
                    .orElse(null);

            if (utilisateur == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("USER_NOT_FOUND", "Utilisateur non trouvé", "Aucun utilisateur avec cet ID n'existe"));
            }

            utilisateurRepository.deleteById(id);

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("message", "Utilisateur supprimé avec succès")));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("USER_DELETE_ERROR", "Erreur lors de la suppression de l'utilisateur", e.getMessage()));
        }
    }

    @PostMapping("/{id}/block")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INFORMATICIEN')")
    public ResponseEntity<?> blockUser(@PathVariable UUID id) {
        try {
            Utilisateur utilisateur = utilisateurRepository.findById(id)
                    .orElse(null);

            if (utilisateur == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("USER_NOT_FOUND", "Utilisateur non trouvé", "Aucun utilisateur avec cet ID n'existe"));
            }

            utilisateur.setBloque(true);
            Utilisateur updatedUser = utilisateurRepository.save(utilisateur);

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("user", updatedUser)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("USER_BLOCK_ERROR", "Erreur lors du blocage de l'utilisateur", e.getMessage()));
        }
    }

    @PostMapping("/{id}/unblock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INFORMATICIEN')")
    public ResponseEntity<?> unblockUser(@PathVariable UUID id) {
        try {
            Utilisateur utilisateur = utilisateurRepository.findById(id)
                    .orElse(null);

            if (utilisateur == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseUtil.createErrorResponse("USER_NOT_FOUND", "Utilisateur non trouvé", "Aucun utilisateur avec cet ID n'existe"));
            }

            utilisateur.setBloque(false);
            Utilisateur updatedUser = utilisateurRepository.save(utilisateur);

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of("user", updatedUser)));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("USER_UNBLOCK_ERROR", "Erreur lors du déblocage de l'utilisateur", e.getMessage()));
        }
    }

    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INFORMATICIEN')")
    public ResponseEntity<?> getUsersByRole(
            @PathVariable Role role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Utilisateur> utilisateurPage = utilisateurRepository.findByRole(role, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("users", utilisateurPage.getContent());
            response.put("currentPage", utilisateurPage.getNumber());
            response.put("totalItems", utilisateurPage.getTotalElements());
            response.put("totalPages", utilisateurPage.getTotalPages());

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(response));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtil.createErrorResponse("USER_FETCH_ERROR", "Erreur lors de la récupération des utilisateurs par rôle", e.getMessage()));
        }
    }
}