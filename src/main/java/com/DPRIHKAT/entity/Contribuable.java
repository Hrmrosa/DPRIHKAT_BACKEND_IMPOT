package com.DPRIHKAT.entity;

/**
 *
 * @author amateur
 */
import com.DPRIHKAT.entity.enums.ModePaiement;
import com.DPRIHKAT.entity.enums.Role;
import com.DPRIHKAT.entity.enums.StatutDeclaration;
import com.DPRIHKAT.entity.enums.StatutPaiement;
import com.DPRIHKAT.entity.enums.TypeContribuable;
import com.DPRIHKAT.entity.enums.TypeImpot;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Contribuable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String nom;

    private String adressePrincipale;

    private String adresseSecondaire;

    private String telephonePrincipal;

    private String telephoneSecondaire;

    private String email;

    private String nationalite;

    @Enumerated(EnumType.STRING)
    private TypeContribuable type;

    private String idNat;

    private String NRC;

    private String sigle;

    private String numeroIdentificationContribuable;

    @OneToMany(mappedBy = "proprietaire")
    private List<Propriete> proprietes = new ArrayList<>();

    @OneToOne(mappedBy = "contribuable")
    private DossierRecouvrement dossierRecouvrement;

    @OneToOne(mappedBy = "contribuable")
    private Utilisateur utilisateur;

    @Transient
    private transient EntityManager entityManager;

    public Contribuable() {
    }

    public Contribuable(String nom, String adressePrincipale, String adresseSecondaire, String telephonePrincipal, String telephoneSecondaire, String email, String nationalite, TypeContribuable type, String idNat, String NRC, String sigle, String numeroIdentificationContribuable) {
        this.nom = nom;
        this.adressePrincipale = adressePrincipale;
        this.adresseSecondaire = adresseSecondaire;
        this.telephonePrincipal = telephonePrincipal;
        this.telephoneSecondaire = telephoneSecondaire;
        this.email = email;
        this.nationalite = nationalite;
        this.type = type;
        this.idNat = idNat;
        this.NRC = NRC;
        this.sigle = sigle;
        this.numeroIdentificationContribuable = numeroIdentificationContribuable;
    }

    public void declarerImpôtEnLigne() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if (month != Calendar.JANUARY || (month == Calendar.JANUARY && day < 2) || month == Calendar.FEBRUARY && day > 1) {
            throw new IllegalStateException("La déclaration en ligne est autorisée uniquement du 2 janvier au 1er février.");
        }
        if (utilisateur == null || !List.of(Role.CONTRIBUABLE).contains(utilisateur.getRole())) {
            throw new IllegalStateException("Seuls les contribuables avec le rôle CONTRIBUTOR peuvent déclarer en ligne.");
        }
        for (Propriete propriete : proprietes) {
            if (propriete == null) continue;
            Declaration declaration = new Declaration();
            declaration.setDate(new java.util.Date());
            declaration.setPropriete(propriete); // Utilise la méthode setPropriete de Declaration
            declaration.setTypeImpot(TypeImpot.IF);
            declaration.setStatut(StatutDeclaration.SOUMISE);
            propriete.calculerImpôt();
            declaration.setMontant(propriete.getMontantImpot());
            soumettreDeclarationEnLigne(declaration);
        }
    }

    public void soumettreDeclarationEnLigne(Declaration declaration) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(declaration.getDate());
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if (month == Calendar.JANUARY && day >= 2 || month == Calendar.FEBRUARY && day == 1) {
            Propriete propriete = declaration.getPropriete();
            if (propriete != null && proprietes.contains(propriete)) {
                if (propriete.getId() == null) {
                    entityManager.persist(propriete);
                }
                declaration.setStatut(StatutDeclaration.SOUMISE);
                entityManager.persist(declaration);
            } else {
                throw new IllegalArgumentException("La déclaration doit être associée à une propriété valide du contribuable.");
            }
        } else {
            throw new IllegalStateException("La déclaration en ligne est autorisée uniquement du 2 janvier au 1er février.");
        }
    }

    public void payerImpôtEnLigne() {
        if (utilisateur == null || !List.of(Role.CONTRIBUABLE).contains(utilisateur.getRole())) {
            throw new IllegalStateException("Seuls les contribuables avec le rôle CONTRIBUTOR peuvent payer en ligne.");
        }
        for (Propriete propriete : proprietes) {
            for (Declaration declaration : propriete.getDeclarations()) {
                if (declaration.getStatut() == StatutDeclaration.VALIDEE && declaration.getPaiement() == null) {
                    Paiement paiement = new Paiement();
                    paiement.setDate(new java.util.Date());
                    paiement.setMontant(declaration.getMontant());
                    paiement.setMode(ModePaiement.BANQUE);
                    paiement.setStatut(StatutPaiement.EN_ATTENTE);
                    paiement.setBordereauBancaire("BORDEREAU-" + UUID.randomUUID().toString());
                    declaration.setPaiement(paiement);
                    entityManager.persist(paiement);
                }
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

    public String getAdressePrincipale() {
        return adressePrincipale;
    }

    public void setAdressePrincipale(String adressePrincipale) {
        this.adressePrincipale = adressePrincipale;
    }

    public String getAdresseSecondaire() {
        return adresseSecondaire;
    }

    public void setAdresseSecondaire(String adresseSecondaire) {
        this.adresseSecondaire = adresseSecondaire;
    }

    public String getTelephonePrincipal() {
        return telephonePrincipal;
    }

    public void setTelephonePrincipal(String telephonePrincipal) {
        this.telephonePrincipal = telephonePrincipal;
    }

    public String getTelephoneSecondaire() {
        return telephoneSecondaire;
    }

    public void setTelephoneSecondaire(String telephoneSecondaire) {
        this.telephoneSecondaire = telephoneSecondaire;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public TypeContribuable getType() {
        return type;
    }

    public void setType(TypeContribuable type) {
        this.type = type;
    }

    public String getIdNat() {
        return idNat;
    }

    public void setIdNat(String idNat) {
        this.idNat = idNat;
    }

    public String getNRC() {
        return NRC;
    }

    public void setNRC(String NRC) {
        this.NRC = NRC;
    }

    public String getSigle() {
        return sigle;
    }

    public void setSigle(String sigle) {
        this.sigle = sigle;
    }

    public String getNumeroIdentificationContribuable() {
        return numeroIdentificationContribuable;
    }

    public void setNumeroIdentificationContribuable(String numeroIdentificationContribuable) {
        this.numeroIdentificationContribuable = numeroIdentificationContribuable;
    }

    public List<Propriete> getProprietes() {
        return proprietes;
    }

    public void setProprietes(List<Propriete> proprietes) {
        this.proprietes = proprietes;
    }

    public DossierRecouvrement getDossierRecouvrement() {
        return dossierRecouvrement;
    }

    public void setDossierRecouvrement(DossierRecouvrement dossierRecouvrement) {
        this.dossierRecouvrement = dossierRecouvrement;
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
}