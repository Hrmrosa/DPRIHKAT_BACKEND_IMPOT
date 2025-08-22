package com.DPRIHKAT.entity;

/**
 *
 * @author amateur
 */

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.DPRIHKAT.entity.enums.MotifPenalite;
import com.DPRIHKAT.entity.enums.Role;
import com.DPRIHKAT.entity.enums.Sexe;
import com.DPRIHKAT.entity.enums.StatutDeclaration;
import com.DPRIHKAT.entity.enums.TypeImpot;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String nom;

    @Enumerated(EnumType.STRING)
    private Sexe sexe;

    private String matricule;

    @ManyToOne
    @JoinColumn(name = "bureau_id")
    private Bureau bureau;

    @OneToMany(mappedBy = "agentValidateur")
    @JsonIgnore
    private List<Declaration> declarations = new ArrayList<>();

    @OneToMany(mappedBy = "agentInitiateur")
    @JsonIgnore
    private List<Poursuite> poursuites = new ArrayList<>();

    @OneToMany(mappedBy = "agentValidateur")
    @JsonIgnore
    private List<Apurement> apurements = new ArrayList<>();

    @OneToOne(mappedBy = "agent")
    private Utilisateur utilisateur;

    @Transient
    private transient EntityManager entityManager;

    @OneToMany(mappedBy = "agentInitiateur")
    @JsonIgnore
    private List<ControleFiscal> controlesInitiates = new ArrayList<>();

    @OneToMany(mappedBy = "agentValidateur")
    @JsonIgnore
    private List<ControleFiscal> controlesValidates = new ArrayList<>();

    public Agent() {
    }

    public Agent(String nom, Sexe sexe, String matricule, Bureau bureau) {
        this.nom = nom;
        this.sexe = sexe;
        this.matricule = matricule;
        this.bureau = bureau;
    }

    public void validerDeclaration() {
        if (utilisateur == null || !List.of(Role.TAXATEUR, Role.VERIFICATEUR, Role.CHEF_DE_BUREAU).contains(utilisateur.getRole())) {
            throw new IllegalStateException("Seuls les rôles TAXATEUR, VERIFICATEUR ou CHEF_DE_BUREAU peuvent valider des déclarations.");
        }
        for (Declaration declaration : declarations) {
            if (declaration.getStatut() == StatutDeclaration.SOUMISE) {
                if (List.of(TypeImpot.IF, TypeImpot.IRL, TypeImpot.ICM).contains(declaration.getTypeImpot()) && declaration.getPropriete().getLocation() == null) {
                    throw new IllegalStateException("La géolocalisation est obligatoire pour " + declaration.getTypeImpot());
                }
                declaration.setStatut(StatutDeclaration.VALIDEE);
                entityManager.merge(declaration);
            }
        }
    }

    public void enregistrerDeclarationManuelle() {
        if (utilisateur == null || !List.of(Role.TAXATEUR, Role.RECEVEUR_DES_IMPOTS).contains(utilisateur.getRole())) {
            throw new IllegalStateException("Seuls les rôles TAXATEUR ou RECEVEUR_DES_IMPOTS peuvent enregistrer des déclarations manuelles.");
        }
        Declaration declaration = new Declaration();
        declaration.setDate(new java.util.Date());
        declaration.setStatut(StatutDeclaration.SOUMISE);
        declaration.setAgentValidateur(this);
        Propriete propriete = new Propriete(); // À remplacer par une propriété réelle
        declaration.setPropriete(propriete); // Utilise la méthode setPropriete de Declaration
        declarations.add(declaration);
        entityManager.persist(declaration);
    }

    public void initierPoursuite() {
        if (utilisateur == null || !List.of(Role.CONTROLLEUR, Role.CHEF_DE_BUREAU, Role.CHEF_DE_DIVISION).contains(utilisateur.getRole())) {
            throw new IllegalStateException("Seuls les rôles CONTROLLER, CHEF_DE_BUREAU ou CHEF_DE_DIVISION peuvent initier des poursuites.");
        }
        Poursuite poursuite = new Poursuite();
        poursuite.setAgentInitiateur(this);
        poursuite.setDateLancement(new java.util.Date());
        poursuites.add(poursuite);
        entityManager.persist(poursuite);
    }

    public void genererNoteTaxation(Propriete propriete, String anneeFiscale) {
        if (utilisateur == null || !List.of(Role.TAXATEUR, Role.CHEF_DE_BUREAU).contains(utilisateur.getRole())) {
            throw new IllegalStateException("Seuls les rôles TAXATEUR ou CHEF_DE_BUREAU peuvent générer des notes de taxation.");
        }
        boolean taxationExistante = propriete.getDeclarations().stream()
                .anyMatch(d -> d.getDate().toString().contains(anneeFiscale) && d.getTypeImpot() == TypeImpot.IF);
        if (taxationExistante) {
            throw new IllegalStateException("Une taxation existe déjà pour ce bien et cette année fiscale.");
        }
        if (propriete.getId() == null) {
            entityManager.persist(propriete);
        }
        propriete.calculerImpôt();
        Declaration declaration = new Declaration();
        declaration.setDate(new java.util.Date());
        declaration.setMontant(propriete.getMontantImpot());
        declaration.setTypeImpot(TypeImpot.IF);
        declaration.setPropriete(propriete); // Utilise la méthode setPropriete de Declaration
        declaration.setAgentValidateur(this);
        declaration.setStatut(StatutDeclaration.SOUMISE);
        declarations.add(declaration);
        entityManager.persist(declaration);
    }

    public void ajusterPenalite(Penalite penalite, Double nouveauMontant) {
        if (utilisateur == null || !List.of(Role.CHEF_DE_BUREAU, Role.CHEF_DE_DIVISION, Role.DIRECTEUR).contains(utilisateur.getRole())) {
            throw new IllegalStateException("Seuls les rôles CHEF_DE_BUREAU, CHEF_DE_DIVISION ou DIRECTEUR peuvent ajuster les pénalités.");
        }
        penalite.setMontant(nouveauMontant);
        entityManager.merge(penalite);
    }

    public void emettreVignette() {
        if (utilisateur == null || !List.of(Role.RECEVEUR_DES_IMPOTS, Role.APUREUR).contains(utilisateur.getRole())) {
            throw new IllegalStateException("Seuls les rôles RECEVEUR_DES_IMPOTS ou APUREUR peuvent émettre des vignettes.");
        }
        if (!verifierStock("VIGNETTE")) {
            throw new IllegalStateException("Stock de vignettes indisponible.");
        }
        String qrCode = "VIGNETTE-" + UUID.randomUUID().toString();
        System.out.println("Vignette émise avec QR code : " + qrCode);
    }

    public void emettreCertificat() {
        if (utilisateur == null || !List.of(Role.RECEVEUR_DES_IMPOTS, Role.APUREUR).contains(utilisateur.getRole())) {
            throw new IllegalStateException("Seuls les rôles RECEVEUR_DES_IMPOTS ou APUREUR peuvent émettre des certificats.");
        }
        if (!verifierStock("CERTIFICAT")) {
            throw new IllegalStateException("Stock de certificats indisponible.");
        }
        String qrCode = "CERTIFICAT-" + UUID.randomUUID().toString();
        System.out.println("Certificat émis avec QR code : " + qrCode);
    }

    public void emettrePlaque(Plaque plaque) {
        if (utilisateur == null || !List.of(Role.RECEVEUR_DES_IMPOTS, Role.APUREUR).contains(utilisateur.getRole())) {
            throw new IllegalStateException("Seuls les rôles RECEVEUR_DES_IMPOTS ou APUREUR peuvent émettre des plaques.");
        }
        if (!verifierStock("PLAQUE") || !plaque.isDisponible()) {
            throw new IllegalStateException("Plaque non disponible ou stock insuffisant.");
        }
        plaque.setDisponible(false);
        plaque.setNumplaque("PLAQUE-" + UUID.randomUUID().toString());
        System.out.println("Plaque émise avec numéro : " + plaque.getNumplaque());
        entityManager.merge(plaque);
    }

    public boolean verifierStock(String typeElement) {
        return true; // Simuler stock disponible
    }

    public void controlerFiscal() {
        if (utilisateur == null || !List.of(Role.CONTROLLEUR, Role.VERIFICATEUR).contains(utilisateur.getRole())) {
            throw new IllegalStateException("Seuls les rôles CONTROLLER ou VERIFICATEUR peuvent effectuer un contrôle fiscal.");
        }
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        for (Declaration declaration : declarations) {
            if (declaration.getStatut() == StatutDeclaration.SOUMISE && cal.getTime().after(new java.util.Date(year - 1900, Calendar.FEBRUARY, 1))) {
                Penalite penalite = new Penalite();
                penalite.setMotif(MotifPenalite.RETARD);
                penalite.setDateApplication(new java.util.Date());
                penalite.appliquerPenalite();
                declaration.getPenalites().add(penalite);
                entityManager.persist(penalite);
            }
        }
    }

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public Bureau getBureau() {
        return bureau;
    }

    public void setBureau(Bureau bureau) {
        this.bureau = bureau;
    }

    public List<Declaration> getDeclarations() {
        return declarations;
    }

    public void setDeclarations(List<Declaration> declarations) {
        this.declarations = declarations;
    }

    public List<Poursuite> getPoursuites() {
        return poursuites;
    }

    public void setPoursuites(List<Poursuite> poursuites) {
        this.poursuites = poursuites;
    }

    public List<Apurement> getApurements() {
        return apurements;
    }

    public void setApurements(List<Apurement> apurements) {
        this.apurements = apurements;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    @JsonIgnore
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @JsonIgnore
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<ControleFiscal> getControlesInitiates() {
        return controlesInitiates;
    }

    public void setControlesInitiates(List<ControleFiscal> controlesInitiates) {
        this.controlesInitiates = controlesInitiates;
    }

    public List<ControleFiscal> getControlesValidates() {
        return controlesValidates;
    }

    public void setControlesValidates(List<ControleFiscal> controlesValidates) {
        this.controlesValidates = controlesValidates;
    }
}