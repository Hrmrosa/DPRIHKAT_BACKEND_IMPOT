package com.DPRIHKAT.controller;

import com.DPRIHKAT.dto.BienDTO;
import com.DPRIHKAT.dto.CollecteContribuableRequest;
import com.DPRIHKAT.entity.Contribuable;
import com.DPRIHKAT.entity.Propriete;
import com.DPRIHKAT.entity.enums.TypeContribuable;
import com.DPRIHKAT.entity.enums.TypePropriete;
import com.DPRIHKAT.repository.ContribuableRepository;
import com.DPRIHKAT.repository.ProprieteRepository;
import com.DPRIHKAT.util.ResponseUtil;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/collecte")
public class CollecteController {

    @Autowired
    private ContribuableRepository contribuableRepository;

    @Autowired
    private ProprieteRepository proprieteRepository;

    @PostMapping("/contribuables")
    @PreAuthorize("hasAnyRole('CONTROLLEUR','ADMIN','INFORMATICIEN')")
    public ResponseEntity<?> creerContribuableAvecBiens(@RequestBody CollecteContribuableRequest request) {
        try {
            if (request == null) {
                return ResponseEntity.badRequest().body(ResponseUtil.createErrorResponse(
                        "INVALID_REQUEST", "Requête invalide", "Le corps de la requête est vide"));
            }
            if (request.getBiens() == null || request.getBiens().isEmpty()) {
                return ResponseEntity.badRequest().body(ResponseUtil.createErrorResponse(
                        "NO_PROPERTIES", "Aucun bien fourni", "Fournissez au moins un bien pour le contribuable"));
            }

            // Construire le contribuable
            Contribuable c = new Contribuable();
            c.setNom(request.getNom());
            c.setAdressePrincipale(request.getAdressePrincipale());
            c.setAdresseSecondaire(request.getAdresseSecondaire());
            c.setTelephonePrincipal(request.getTelephonePrincipal());
            c.setTelephoneSecondaire(request.getTelephoneSecondaire());
            c.setEmail(request.getEmail());
            c.setNationalite(request.getNationalite());
            if (request.getType() != null) {
                try {
                    c.setType(TypeContribuable.valueOf(request.getType().toUpperCase()));
                } catch (IllegalArgumentException ex) {
                    return ResponseEntity.badRequest().body(ResponseUtil.createErrorResponse(
                            "INVALID_TYPE_CONTRIBUABLE", "Type de contribuable invalide", "Valeur fournie: " + request.getType()));
                }
            }
            c.setIdNat(request.getIdNat());
            c.setNRC(request.getNrc());
            c.setSigle(request.getSigle());
            c.setNumeroIdentificationContribuable(request.getNumeroIdentificationContribuable());

            Contribuable savedContribuable = contribuableRepository.save(c);

            // Construire les propriétés
            List<Propriete> toSave = new ArrayList<>();
            GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
            for (BienDTO b : request.getBiens()) {
                Propriete p = new Propriete();
                try {
                    if (b.getType() != null) {
                        p.setType(TypePropriete.valueOf(b.getType().toUpperCase()));
                    }
                } catch (IllegalArgumentException ex) {
                    return ResponseEntity.badRequest().body(ResponseUtil.createErrorResponse(
                            "INVALID_TYPE_PROPRIETE", "Type de bien invalide", "Valeur fournie: " + b.getType()));
                }
                p.setLocalite(b.getLocalite());
                p.setRangLocalite(b.getRangLocalite());
                p.setSuperficie(b.getSuperficie());
                p.setAdresse(b.getAdresse());
                if (b.getLatitude() != null && b.getLongitude() != null) {
                    Point point = geometryFactory.createPoint(new Coordinate(b.getLongitude(), b.getLatitude()));
                    p.setLocation(point);
                }
                p.setProprietaire(savedContribuable);
                toSave.add(p);
            }

            List<Propriete> savedProprietes = proprieteRepository.saveAll(toSave);

            return ResponseEntity.ok(ResponseUtil.createSuccessResponse(Map.of(
                    "contribuable", savedContribuable,
                    "proprietes", savedProprietes
            )));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtil.createErrorResponse(
                    "COLLECTE_CREATE_ERROR", "Erreur lors de la création du contribuable et des biens", e.getMessage()));
        }
    }
}
