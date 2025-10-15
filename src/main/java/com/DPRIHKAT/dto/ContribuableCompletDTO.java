package com.DPRIHKAT.dto;

import com.DPRIHKAT.entity.enums.StatutDeclaration;
import com.DPRIHKAT.entity.enums.TypeContribuable;
import com.DPRIHKAT.entity.enums.TypeImpot;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ContribuableCompletDTO {
    private UUID id;
    private String nom;
    private String matricule;
    private String adressePrincipale;
    private String adresseSecondaire;
    private String telephonePrincipal;
    private String telephoneSecondaire;
    private String email;
    private String nationalite;
    private TypeContribuable type;
    private String idNat;
    private String NRC;
    private String sigle;
    private String numeroIdentificationContribuable;
    private boolean actif;
    private boolean commercant;
    private String codeQR;
    
    private List<ProprieteDTO> proprietes;
    private List<VehiculeDTO> vehicules;
    private List<DeclarationDTO> declarations;
    private List<RelanceDTO> relances;
    private List<DocumentRecouvrementDTO> documentsRecouvrement;
    private List<TaxationDTO> taxations;
    
    // Sous-classes DTO
    public static class ProprieteDTO {
        private UUID id;
        private String adresse;
        private String reference;
        private Double superficie;
        private String usage;
        private Double valeurLocative;
        private Double montantImpot;
        
        // Getters et Setters
        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
        public String getAdresse() { return adresse; }
        public void setAdresse(String adresse) { this.adresse = adresse; }
        public String getReference() { return reference; }
        public void setReference(String reference) { this.reference = reference; }
        public Double getSuperficie() { return superficie; }
        public void setSuperficie(Double superficie) { this.superficie = superficie; }
        public String getUsage() { return usage; }
        public void setUsage(String usage) { this.usage = usage; }
        public Double getValeurLocative() { return valeurLocative; }
        public void setValeurLocative(Double valeurLocative) { this.valeurLocative = valeurLocative; }
        public Double getMontantImpot() { return montantImpot; }
        public void setMontantImpot(Double montantImpot) { this.montantImpot = montantImpot; }
    }
    
    public static class VehiculeDTO {
        private UUID id;
        private String immatriculation;
        private String marque;
        private String modele;
        private String chassis;
        private Integer annee;
        private String type;
        private Double cylindree;
        private Double montantVignette;
        
        // Getters et Setters
        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
        public String getImmatriculation() { return immatriculation; }
        public void setImmatriculation(String immatriculation) { this.immatriculation = immatriculation; }
        public String getMarque() { return marque; }
        public void setMarque(String marque) { this.marque = marque; }
        public String getModele() { return modele; }
        public void setModele(String modele) { this.modele = modele; }
        public String getChassis() { return chassis; }
        public void setChassis(String chassis) { this.chassis = chassis; }
        public Integer getAnnee() { return annee; }
        public void setAnnee(Integer annee) { this.annee = annee; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public Double getCylindree() { return cylindree; }
        public void setCylindree(Double cylindree) { this.cylindree = cylindree; }
        public Double getMontantVignette() { return montantVignette; }
        public void setMontantVignette(Double montantVignette) { this.montantVignette = montantVignette; }
    }
    
    public static class DeclarationDTO {
        private UUID id;
        private Date dateDeclaration;
        private TypeImpot typeImpot;
        private Double montantDeclare;
        private StatutDeclaration statut;
        private String reference;
        private String exercice;
        private String periode;
        private AgentDTO taxateur;
        
        // Getters et Setters
        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
        public Date getDateDeclaration() { return dateDeclaration; }
        public void setDateDeclaration(Date dateDeclaration) { this.dateDeclaration = dateDeclaration; }
        public TypeImpot getTypeImpot() { return typeImpot; }
        public void setTypeImpot(TypeImpot typeImpot) { this.typeImpot = typeImpot; }
        public Double getMontantDeclare() { return montantDeclare; }
        public void setMontantDeclare(Double montantDeclare) { this.montantDeclare = montantDeclare; }
        public StatutDeclaration getStatut() { return statut; }
        public void setStatut(StatutDeclaration statut) { this.statut = statut; }
        public String getReference() { return reference; }
        public void setReference(String reference) { this.reference = reference; }
        public String getExercice() { return exercice; }
        public void setExercice(String exercice) { this.exercice = exercice; }
        public String getPeriode() { return periode; }
        public void setPeriode(String periode) { this.periode = periode; }
        public AgentDTO getTaxateur() { return taxateur; }
        public void setTaxateur(AgentDTO taxateur) { this.taxateur = taxateur; }
    }
    
    public static class RelanceDTO {
        private UUID id;
        private Date dateRelance;
        private String type;
        private String contenu;
        private String statut;
        private AgentDTO agent;
        
        // Getters et Setters
        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
        public Date getDateRelance() { return dateRelance; }
        public void setDateRelance(Date dateRelance) { this.dateRelance = dateRelance; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getContenu() { return contenu; }
        public void setContenu(String contenu) { this.contenu = contenu; }
        public String getStatut() { return statut; }
        public void setStatut(String statut) { this.statut = statut; }
        public AgentDTO getAgent() { return agent; }
        public void setAgent(AgentDTO agent) { this.agent = agent; }
    }
    
    public static class DocumentRecouvrementDTO {
        private UUID id;
        private String type;
        private String statut;
        private Date dateGeneration;
        private Date dateNotification;
        private Date dateEcheance;
        private String reference;
        private Double montantPrincipal;
        private Double montantPenalites;
        private Double montantTotal;
        private AgentDTO agentGenerateur;
        
        // Getters et Setters
        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getStatut() { return statut; }
        public void setStatut(String statut) { this.statut = statut; }
        public Date getDateGeneration() { return dateGeneration; }
        public void setDateGeneration(Date dateGeneration) { this.dateGeneration = dateGeneration; }
        public Date getDateNotification() { return dateNotification; }
        public void setDateNotification(Date dateNotification) { this.dateNotification = dateNotification; }
        public Date getDateEcheance() { return dateEcheance; }
        public void setDateEcheance(Date dateEcheance) { this.dateEcheance = dateEcheance; }
        public String getReference() { return reference; }
        public void setReference(String reference) { this.reference = reference; }
        public Double getMontantPrincipal() { return montantPrincipal; }
        public void setMontantPrincipal(Double montantPrincipal) { this.montantPrincipal = montantPrincipal; }
        public Double getMontantPenalites() { return montantPenalites; }
        public void setMontantPenalites(Double montantPenalites) { this.montantPenalites = montantPenalites; }
        public Double getMontantTotal() { return montantTotal; }
        public void setMontantTotal(Double montantTotal) { this.montantTotal = montantTotal; }
        public AgentDTO getAgentGenerateur() { return agentGenerateur; }
        public void setAgentGenerateur(AgentDTO agentGenerateur) { this.agentGenerateur = agentGenerateur; }
    }
    
    public static class TaxationDTO {
        private UUID id;
        private Date dateTaxation;
        private TypeImpot typeImpot;
        private Double montantBase;
        private Double montantImpose;
        private Double montantPenalites;
        private String reference;
        private String exercice;
        private String periode;
        private AgentDTO taxateur;
        
        // Getters et Setters
        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
        public Date getDateTaxation() { return dateTaxation; }
        public void setDateTaxation(Date dateTaxation) { this.dateTaxation = dateTaxation; }
        public TypeImpot getTypeImpot() { return typeImpot; }
        public void setTypeImpot(TypeImpot typeImpot) { this.typeImpot = typeImpot; }
        public Double getMontantBase() { return montantBase; }
        public void setMontantBase(Double montantBase) { this.montantBase = montantBase; }
        public Double getMontantImpose() { return montantImpose; }
        public void setMontantImpose(Double montantImpose) { this.montantImpose = montantImpose; }
        public Double getMontantPenalites() { return montantPenalites; }
        public void setMontantPenalites(Double montantPenalites) { this.montantPenalites = montantPenalites; }
        public String getReference() { return reference; }
        public void setReference(String reference) { this.reference = reference; }
        public String getExercice() { return exercice; }
        public void setExercice(String exercice) { this.exercice = exercice; }
        public String getPeriode() { return periode; }
        public void setPeriode(String periode) { this.periode = periode; }
        public AgentDTO getTaxateur() { return taxateur; }
        public void setTaxateur(AgentDTO taxateur) { this.taxateur = taxateur; }
    }
    
    public static class AgentDTO {
        private UUID id;
        private String nom;
        private String matricule;
        private String grade;
        private String fonction;
        
        // Getters et Setters
        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
        public String getNom() { return nom; }
        public void setNom(String nom) { this.nom = nom; }
        public String getMatricule() { return matricule; }
        public void setMatricule(String matricule) { this.matricule = matricule; }
        public String getGrade() { return grade; }
        public void setGrade(String grade) { this.grade = grade; }
        public String getFonction() { return fonction; }
        public void setFonction(String fonction) { this.fonction = fonction; }
    }
    
    // Getters et Setters pour la classe principale
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }
    public String getAdressePrincipale() { return adressePrincipale; }
    public void setAdressePrincipale(String adressePrincipale) { this.adressePrincipale = adressePrincipale; }
    public String getAdresseSecondaire() { return adresseSecondaire; }
    public void setAdresseSecondaire(String adresseSecondaire) { this.adresseSecondaire = adresseSecondaire; }
    public String getTelephonePrincipal() { return telephonePrincipal; }
    public void setTelephonePrincipal(String telephonePrincipal) { this.telephonePrincipal = telephonePrincipal; }
    public String getTelephoneSecondaire() { return telephoneSecondaire; }
    public void setTelephoneSecondaire(String telephoneSecondaire) { this.telephoneSecondaire = telephoneSecondaire; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNationalite() { return nationalite; }
    public void setNationalite(String nationalite) { this.nationalite = nationalite; }
    public TypeContribuable getType() { return type; }
    public void setType(TypeContribuable type) { this.type = type; }
    public String getIdNat() { return idNat; }
    public void setIdNat(String idNat) { this.idNat = idNat; }
    public String getNRC() { return NRC; }
    public void setNRC(String NRC) { this.NRC = NRC; }
    public String getSigle() { return sigle; }
    public void setSigle(String sigle) { this.sigle = sigle; }
    public String getNumeroIdentificationContribuable() { return numeroIdentificationContribuable; }
    public void setNumeroIdentificationContribuable(String numeroIdentificationContribuable) { this.numeroIdentificationContribuable = numeroIdentificationContribuable; }
    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
    public boolean isCommercant() { return commercant; }
    public void setCommercant(boolean commercant) { this.commercant = commercant; }
    public String getCodeQR() { return codeQR; }
    public void setCodeQR(String codeQR) { this.codeQR = codeQR; }
    public List<ProprieteDTO> getProprietes() { return proprietes; }
    public void setProprietes(List<ProprieteDTO> proprietes) { this.proprietes = proprietes; }
    public List<VehiculeDTO> getVehicules() { return vehicules; }
    public void setVehicules(List<VehiculeDTO> vehicules) { this.vehicules = vehicules; }
    public List<DeclarationDTO> getDeclarations() { return declarations; }
    public void setDeclarations(List<DeclarationDTO> declarations) { this.declarations = declarations; }
    public List<RelanceDTO> getRelances() { return relances; }
    public void setRelances(List<RelanceDTO> relances) { this.relances = relances; }
    public List<DocumentRecouvrementDTO> getDocumentsRecouvrement() { return documentsRecouvrement; }
    public void setDocumentsRecouvrement(List<DocumentRecouvrementDTO> documentsRecouvrement) { this.documentsRecouvrement = documentsRecouvrement; }
    public List<TaxationDTO> getTaxations() { return taxations; }
    public void setTaxations(List<TaxationDTO> taxations) { this.taxations = taxations; }
}
