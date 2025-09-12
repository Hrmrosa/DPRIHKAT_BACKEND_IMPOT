package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Agent;
import com.DPRIHKAT.entity.TauxChange;
import com.DPRIHKAT.entity.enums.Devise;
import com.DPRIHKAT.repository.AgentRepository;
import com.DPRIHKAT.repository.TauxChangeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service pour gérer les taux de change entre devises
 * @author amateur
 */
@Service
public class TauxChangeService {

    private static final Logger logger = LoggerFactory.getLogger(TauxChangeService.class);

    @Autowired
    private TauxChangeRepository tauxChangeRepository;
    
    @Autowired
    private AgentRepository agentRepository;
    
    /**
     * Récupère tous les taux de change actifs
     * @return Liste de tous les taux de change actifs
     */
    public List<TauxChange> getAllActiveTauxChange() {
        return tauxChangeRepository.findByActifTrue();
    }
    
    /**
     * Récupère le taux de change actif le plus récent pour une paire de devises
     * @param deviseSource La devise source
     * @param deviseDestination La devise destination
     * @return Le taux de change actif le plus récent, si disponible
     */
    public Optional<TauxChange> getLatestTauxChange(Devise deviseSource, Devise deviseDestination) {
        return tauxChangeRepository.findLatestActiveTauxChange(deviseSource, deviseDestination);
    }
    
    /**
     * Récupère le taux de change actif à une date donnée pour une paire de devises
     * @param deviseSource La devise source
     * @param deviseDestination La devise destination
     * @param date La date
     * @return Le taux de change actif à la date donnée, si disponible
     */
    public Optional<TauxChange> getTauxChangeAtDate(Devise deviseSource, Devise deviseDestination, Date date) {
        return tauxChangeRepository.findTauxChangeAtDate(deviseSource, deviseDestination, date);
    }
    
    /**
     * Crée un nouveau taux de change
     * @param taux Le taux de change
     * @param deviseSource La devise source
     * @param deviseDestination La devise destination
     * @param agentId L'ID de l'agent qui définit ce taux de change
     * @return Le taux de change créé
     */
    public TauxChange createTauxChange(Double taux, Devise deviseSource, Devise deviseDestination, UUID agentId) throws Exception {
        // Vérifier que l'agent existe
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new Exception("Agent non trouvé avec l'ID: " + agentId));
        
        // Créer le taux de change
        TauxChange tauxChange = new TauxChange();
        tauxChange.setTaux(taux);
        tauxChange.setDeviseSource(deviseSource);
        tauxChange.setDeviseDestination(deviseDestination);
        tauxChange.setDateEffective(new Date());
        tauxChange.setAgent(agent);
        tauxChange.setActif(true);
        
        return tauxChangeRepository.save(tauxChange);
    }
    
    /**
     * Convertit un montant d'une devise à une autre
     * @param montant Le montant à convertir
     * @param deviseSource La devise source
     * @param deviseDestination La devise destination
     * @return Le montant converti
     */
    public Double convertirMontant(Double montant, Devise deviseSource, Devise deviseDestination) throws Exception {
        // Si les devises sont identiques, pas besoin de conversion
        if (deviseSource == deviseDestination) {
            return montant;
        }
        
        // Récupérer le taux de change le plus récent
        TauxChange tauxChange = getLatestTauxChange(deviseSource, deviseDestination)
                .orElseThrow(() -> new Exception("Aucun taux de change trouvé pour la conversion de " + deviseSource + " vers " + deviseDestination));
        
        // Convertir le montant
        return montant * tauxChange.getTaux();
    }
    
    /**
     * Désactive un taux de change
     * @param id L'ID du taux de change à désactiver
     */
    public void desactiverTauxChange(UUID id) throws Exception {
        TauxChange tauxChange = tauxChangeRepository.findById(id)
                .orElseThrow(() -> new Exception("Taux de change non trouvé avec l'ID: " + id));
        
        tauxChange.setActif(false);
        tauxChangeRepository.save(tauxChange);
    }
}
