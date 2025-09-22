package com.DPRIHKAT.repository;

import com.DPRIHKAT.dto.CertificatDetailDTO;
import com.DPRIHKAT.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Implémentation des méthodes personnalisées du repository CertificatRepository
 */
@Repository
public class CertificatRepositoryCustomImpl implements CertificatRepositoryCustom {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public Optional<CertificatDetailDTO> findCertificatDetailById(UUID id) {
        // Récupérer le certificat
        String jpql = "SELECT c FROM Certificat c WHERE c.id = :id";
        Certificat certificat = null;
        
        try {
            certificat = (Certificat) entityManager.createQuery(jpql)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return Optional.empty();
        }
        
        if (certificat == null) {
            return Optional.empty();
        }
        
        return Optional.of(mapCertificatToDetailDTO(certificat));
    }
    
    @Override
    public List<CertificatDetailDTO> findAllCertificatDetails() {
        // Récupérer tous les certificats
        String jpql = "SELECT c FROM Certificat c";
        List<Certificat> certificats = entityManager.createQuery(jpql, Certificat.class).getResultList();
        
        List<CertificatDetailDTO> dtos = new ArrayList<>();
        for (Certificat certificat : certificats) {
            dtos.add(mapCertificatToDetailDTO(certificat));
        }
        
        return dtos;
    }
    
    @Override
    public List<CertificatDetailDTO> findCertificatDetailsByContribuableId(UUID contribuableId) {
        // Récupérer les certificats liés à un contribuable via les déclarations
        String jpql = "SELECT c FROM Certificat c WHERE c.declaration.contribuable.id = :contribuableId";
        List<Certificat> certificats = entityManager.createQuery(jpql, Certificat.class)
                .setParameter("contribuableId", contribuableId)
                .getResultList();
        
        // Récupérer également les certificats liés à un contribuable via les véhicules
        String jpqlVehicules = "SELECT c FROM Certificat c WHERE c.vehicule.proprietaire.id = :contribuableId";
        List<Certificat> certificatsVehicules = entityManager.createQuery(jpqlVehicules, Certificat.class)
                .setParameter("contribuableId", contribuableId)
                .getResultList();
        
        // Combiner les résultats
        Set<UUID> certificatIds = new HashSet<>();
        List<CertificatDetailDTO> dtos = new ArrayList<>();
        
        for (Certificat certificat : certificats) {
            if (!certificatIds.contains(certificat.getId())) {
                certificatIds.add(certificat.getId());
                dtos.add(mapCertificatToDetailDTO(certificat));
            }
        }
        
        for (Certificat certificat : certificatsVehicules) {
            if (!certificatIds.contains(certificat.getId())) {
                certificatIds.add(certificat.getId());
                dtos.add(mapCertificatToDetailDTO(certificat));
            }
        }
        
        return dtos;
    }
    
    @Override
    public List<CertificatDetailDTO> findCertificatDetailsByVehiculeId(UUID vehiculeId) {
        // Récupérer les certificats liés à un véhicule
        String jpql = "SELECT c FROM Certificat c WHERE c.vehicule.id = :vehiculeId";
        List<Certificat> certificats = entityManager.createQuery(jpql, Certificat.class)
                .setParameter("vehiculeId", vehiculeId)
                .getResultList();
        
        List<CertificatDetailDTO> dtos = new ArrayList<>();
        for (Certificat certificat : certificats) {
            dtos.add(mapCertificatToDetailDTO(certificat));
        }
        
        return dtos;
    }
    
    @Override
    public List<CertificatDetailDTO> findCertificatDetailsByProprieteId(UUID proprieteId) {
        // Récupérer les certificats liés à une propriété via les déclarations
        String jpql = "SELECT c FROM Certificat c WHERE c.declaration.propriete.id = :proprieteId";
        List<Certificat> certificats = entityManager.createQuery(jpql, Certificat.class)
                .setParameter("proprieteId", proprieteId)
                .getResultList();
        
        List<CertificatDetailDTO> dtos = new ArrayList<>();
        for (Certificat certificat : certificats) {
            dtos.add(mapCertificatToDetailDTO(certificat));
        }
        
        return dtos;
    }
    
    @Override
    public List<CertificatDetailDTO> findCertificatDetailsByTaxationId(UUID taxationId) {
        // Récupérer les certificats liés à une taxation via les déclarations
        String jpql = "SELECT c FROM Certificat c JOIN c.declaration.taxations t WHERE t.id = :taxationId";
        List<Certificat> certificats = entityManager.createQuery(jpql, Certificat.class)
                .setParameter("taxationId", taxationId)
                .getResultList();
        
        List<CertificatDetailDTO> dtos = new ArrayList<>();
        for (Certificat certificat : certificats) {
            dtos.add(mapCertificatToDetailDTO(certificat));
        }
        
        return dtos;
    }
    
    /**
     * Méthode utilitaire pour mapper un Certificat vers un CertificatDetailDTO
     * @param certificat Le certificat à mapper
     * @return Le DTO avec toutes les informations détaillées
     */
    private CertificatDetailDTO mapCertificatToDetailDTO(Certificat certificat) {
        CertificatDetailDTO dto = new CertificatDetailDTO();
        
        // Informations de base du certificat
        dto.setId(certificat.getId());
        dto.setNumero(certificat.getNumero());
        dto.setDateEmission(certificat.getDateEmission());
        dto.setDateExpiration(certificat.getDateExpiration());
        dto.setMontant(certificat.getMontant());
        dto.setStatut(certificat.getStatut());
        dto.setActif(certificat.isActif());
        dto.setCodeQR(certificat.getCodeQR());
        dto.setMotifAnnulation(certificat.getMotifAnnulation());
        
        // Informations sur la déclaration
        Declaration declaration = certificat.getDeclaration();
        if (declaration != null) {
            dto.setDeclarationId(declaration.getId());
            dto.setDateDeclaration(declaration.getDateDeclaration());
            dto.setStatutDeclaration(declaration.getStatut());
            // La déclaration n'a pas de méthode getNumero(), on utilise l'ID comme numéro
            dto.setNumeroDeclaration("DECL-" + declaration.getId().toString().substring(0, 8));
            
            // Informations sur le contribuable via la déclaration
            Contribuable contribuable = declaration.getContribuable();
            if (contribuable != null) {
                setContribuableInfo(dto, contribuable);
            }
            
            // Informations sur la propriété via la déclaration
            Propriete propriete = declaration.getPropriete();
            if (propriete != null) {
                dto.setProprieteId(propriete.getId());
                dto.setProprieteType(propriete.getType());
                dto.setProprieteLocalite(propriete.getLocalite());
                dto.setProprieteRangLocalite(propriete.getRangLocalite());
                dto.setProprieteSuperficie(propriete.getSuperficie());
                dto.setProprieteAdresse(propriete.getAdresse());
            }
            
            // Informations sur la taxation via la déclaration
            // La déclaration n'a pas de méthode getTaxation(), mais une liste de taxations
            if (declaration.getTaxations() != null && !declaration.getTaxations().isEmpty()) {
                Taxation taxation = declaration.getTaxations().get(0); // On prend la première taxation
                setTaxationInfo(dto, taxation);
                
                // Informations sur le paiement via la taxation
                Paiement paiement = taxation.getPaiement();
                if (paiement != null) {
                    setPaiementInfo(dto, paiement);
                }
            }
        }
        
        // Informations sur le véhicule
        Vehicule vehicule = certificat.getVehicule();
        if (vehicule != null) {
            dto.setVehiculeId(vehicule.getId());
            dto.setVehiculeImmatriculation(vehicule.getImmatriculation());
            dto.setVehiculeMarque(vehicule.getMarque());
            dto.setVehiculeModele(vehicule.getModele());
            dto.setVehiculeAnnee(vehicule.getAnnee());
            dto.setVehiculeNumeroChassis(vehicule.getNumeroChassis());
            dto.setVehiculeGenre(vehicule.getGenre());
            dto.setVehiculeCategorie(vehicule.getCategorie());
            dto.setVehiculePuissanceFiscale(vehicule.getPuissanceFiscale());
            
            // Informations sur le contribuable via le véhicule
            Contribuable proprietaire = vehicule.getProprietaire();
            if (proprietaire != null && dto.getContribuableId() == null) {
                setContribuableInfo(dto, proprietaire);
            }
        }
        
        // Informations sur l'agent
        Agent agent = certificat.getAgent();
        if (agent != null) {
            dto.setAgentId(agent.getId());
            dto.setAgentNom(agent.getNom());
            dto.setAgentMatricule(agent.getMatricule());
            // Récupérer le bureau de l'agent si disponible
            if (agent.getBureau() != null) {
                dto.setAgentBureau(agent.getBureau().getNom());
            }
        }
        
        return dto;
    }
    
    /**
     * Méthode utilitaire pour définir les informations du contribuable dans le DTO
     * @param dto Le DTO à mettre à jour
     * @param contribuable Le contribuable source
     */
    private void setContribuableInfo(CertificatDetailDTO dto, Contribuable contribuable) {
        dto.setContribuableId(contribuable.getId());
        dto.setContribuableNom(contribuable.getNom());
        dto.setContribuableAdressePrincipale(contribuable.getAdressePrincipale());
        dto.setContribuableAdresseSecondaire(contribuable.getAdresseSecondaire());
        dto.setContribuableTelephonePrincipal(contribuable.getTelephonePrincipal());
        dto.setContribuableTelephoneSecondaire(contribuable.getTelephoneSecondaire());
        dto.setContribuableEmail(contribuable.getEmail());
        dto.setContribuableType(contribuable.getType());
        dto.setContribuableIdentifiant(contribuable.getNumeroIdentificationContribuable());
    }
    
    /**
     * Méthode utilitaire pour définir les informations de taxation dans le DTO
     * @param dto Le DTO à mettre à jour
     * @param taxation La taxation source
     */
    private void setTaxationInfo(CertificatDetailDTO dto, Taxation taxation) {
        dto.setTaxationId(taxation.getId());
        dto.setDateTaxation(taxation.getDateTaxation());
        dto.setMontantTaxation(taxation.getMontant());
        dto.setExercice(taxation.getExercice());
        dto.setStatutTaxation(taxation.getStatut());
        dto.setTypeImpot(taxation.getTypeImpot());
        dto.setNumeroTaxation(taxation.getNumeroTaxation());
    }
    
    /**
     * Méthode utilitaire pour définir les informations de paiement dans le DTO
     * @param dto Le DTO à mettre à jour
     * @param paiement Le paiement source
     */
    private void setPaiementInfo(CertificatDetailDTO dto, Paiement paiement) {
        dto.setPaiementId(paiement.getId());
        dto.setDatePaiement(paiement.getDate()); // Utiliser date au lieu de getDatePaiement()
        dto.setMontantPaiement(paiement.getMontant());
        dto.setStatutPaiement(paiement.getStatut());
        dto.setReferencePaiement(paiement.getBordereauBancaire()); // Utiliser bordereauBancaire au lieu de getReference()
        if (paiement.getMode() != null) {
            dto.setModePaiement(paiement.getMode().toString()); // Utiliser mode au lieu de getModePaiement()
        }
    }
}
