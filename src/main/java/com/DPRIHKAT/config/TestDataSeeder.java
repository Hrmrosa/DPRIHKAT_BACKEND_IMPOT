package com.DPRIHKAT.config;

import com.DPRIHKAT.entity.*;
import com.DPRIHKAT.entity.enums.*;
import com.DPRIHKAT.repository.*;
import com.DPRIHKAT.util.LetsCrypt;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@Order(10)
public class TestDataSeeder implements CommandLineRunner {

    private final DivisionRepository divisionRepository;
    private final BureauRepository bureauRepository;
    private final AgentRepository agentRepository;
    private final ContribuableRepository contribuableRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ProprieteRepository proprieteRepository;
    private final ConcessionMinierRepository concessionMinierRepository;
    private final DeclarationRepository declarationRepository;
    private final PaiementRepository paiementRepository;
    private final PenaliteRepository penaliteRepository;
    private final DossierRecouvrementRepository dossierRecouvrementRepository;
    private final RelanceRepository relanceRepository;
    private final PoursuiteRepository poursuiteRepository;
    private final ApurementRepository apurementRepository;
    private final VehiculeRepository vehiculeRepository;
    private final PlaqueRepository plaqueRepository;
    private final CertificatRepository certificatRepository;
    private final VignetteRepository vignetteRepository;
    private final ControleFiscalRepository controleFiscalRepository;
    private final NatureImpotRepository natureImpotRepository;
    private final ProprieteImpotRepository proprieteImpotRepository;
    private final TaxationRepository taxationRepository;

    public TestDataSeeder(
            DivisionRepository divisionRepository,
            BureauRepository bureauRepository,
            AgentRepository agentRepository,
            ContribuableRepository contribuableRepository,
            UtilisateurRepository utilisateurRepository,
            ProprieteRepository proprieteRepository,
            ConcessionMinierRepository concessionMinierRepository,
            DeclarationRepository declarationRepository,
            PaiementRepository paiementRepository,
            PenaliteRepository penaliteRepository,
            DossierRecouvrementRepository dossierRecouvrementRepository,
            RelanceRepository relanceRepository,
            PoursuiteRepository poursuiteRepository,
            ApurementRepository apurementRepository,
            VehiculeRepository vehiculeRepository,
            PlaqueRepository plaqueRepository,
            CertificatRepository certificatRepository,
            VignetteRepository vignetteRepository,
            ControleFiscalRepository controleFiscalRepository,
            NatureImpotRepository natureImpotRepository,
            ProprieteImpotRepository proprieteImpotRepository,
            TaxationRepository taxationRepository) {
        this.divisionRepository = divisionRepository;
        this.bureauRepository = bureauRepository;
        this.agentRepository = agentRepository;
        this.contribuableRepository = contribuableRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.proprieteRepository = proprieteRepository;
        this.concessionMinierRepository = concessionMinierRepository;
        this.declarationRepository = declarationRepository;
        this.paiementRepository = paiementRepository;
        this.penaliteRepository = penaliteRepository;
        this.dossierRecouvrementRepository = dossierRecouvrementRepository;
        this.relanceRepository = relanceRepository;
        this.poursuiteRepository = poursuiteRepository;
        this.apurementRepository = apurementRepository;
        this.vehiculeRepository = vehiculeRepository;
        this.plaqueRepository = plaqueRepository;
        this.certificatRepository = certificatRepository;
        this.vignetteRepository = vignetteRepository;
        this.controleFiscalRepository = controleFiscalRepository;
        this.natureImpotRepository = natureImpotRepository;
        this.proprieteImpotRepository = proprieteImpotRepository;
        this.taxationRepository = taxationRepository;
    }

    @Override
    public void run(String... args) {
        // Eviter les doublons si déjà seedé
        if (utilisateurRepository.count() > 0) {
            System.out.println("TestDataSeeder: données déjà présentes, on saute le seeding.");
            return;
        }
        System.out.println("TestDataSeeder: démarrage du seeding de données de test...");

        // 1) Organisation: Division + Bureaux
        Division div = new Division("Division Urbaine", "DIV-001");
        divisionRepository.save(div);

        Bureau bur1 = new Bureau("Bureau Katuba", "BU-001", div);
        Bureau bur2 = new Bureau("Bureau Kenya", "BU-002", div);
        bureauRepository.saveAll(List.of(bur1, bur2));

        // 2) Agents
        Agent taxateur = new Agent("KAPENDA Jean", Sexe.M, "AGT-1001", bur1);
        Agent chefBureau = new Agent("MULENGA Marie", Sexe.F, "AGT-2001", bur1);
        Agent receveur = new Agent("ILUNGA Paul", Sexe.M, "AGT-3001", bur2);
        agentRepository.saveAll(List.of(taxateur, chefBureau, receveur));

        // 3) Contribuables
        Contribuable c1 = new Contribuable(
                "Société Minière KATANGA SA",
                "Av. Industrielle 12",
                "Zone Minière 5",
                "+243820000001",
                "+243990000001",
                "contact@katangamining.cd",
                "RDC",
                TypeContribuable.PERSONNE_MORALE,
                "IDNAT-012345",
                "RCCM-CD/LUA/2020-B-1234",
                "KATMIN",
                "NIF-100200300"
        );
        Contribuable c2 = new Contribuable(
                "KASONGO Beya",
                "Av. Kasapa 45",
                null,
                "+243820000002",
                null,
                "beya.kasongo@example.com",
                "RDC",
                TypeContribuable.PERSONNE_PHYSIQUE,
                "IDNAT-067890",
                null,
                null,
                "NIF-200300400"
        );
        Contribuable c3 = new Contribuable(
                "MUTAMBA Daniel",
                "Av. Kampemba 10",
                null,
                "+243820000003",
                null,
                "daniel.mutamba@example.com",
                "RDC",
                TypeContribuable.PERSONNE_PHYSIQUE,
                "IDNAT-078901",
                null,
                null,
                "NIF-300400500"
        );
        contribuableRepository.saveAll(List.of(c1, c2, c3));

        // 3.1) Créer des agents pour les contribuables
        Agent agentC1 = new Agent("Société Minière KATANGA SA", Sexe.M, "CONT-" + c1.getNumeroIdentificationContribuable(), bur1);
        Agent agentC2 = new Agent("KASONGO Beya", Sexe.M, "CONT-" + c2.getNumeroIdentificationContribuable(), bur1);
        Agent agentC3 = new Agent("MUTAMBA Daniel", Sexe.M, "CONT-" + c3.getNumeroIdentificationContribuable(), bur1);
        agentRepository.saveAll(List.of(agentC1, agentC2, agentC3));
        
        // 3.2) Associer les agents aux contribuables
        c1.setAgent(agentC1);
        c2.setAgent(agentC2);
        c3.setAgent(agentC3);
        contribuableRepository.saveAll(List.of(c1, c2, c3));

        // 4) Utilisateurs (mot de passe haché avec LetsCrypt; ex: Tabc@123)
        String pwdInit = LetsCrypt.crypt("Tabc@123");
        Utilisateur admin = new Utilisateur("admin", pwdInit, Role.ADMIN);
        Utilisateur uTaxateur = new Utilisateur("taxateur1", pwdInit, Role.TAXATEUR);
        Utilisateur uReceveur = new Utilisateur("receveur1", pwdInit, Role.RECEVEUR_DES_IMPOTS);
        Utilisateur uContribuable = new Utilisateur("contrib1", pwdInit, Role.CONTRIBUABLE);
        Utilisateur uContribuable2 = new Utilisateur("contrib2", pwdInit, Role.CONTRIBUABLE);
        Utilisateur uContribuable3 = new Utilisateur("dpri_ctest", pwdInit, Role.CONTRIBUABLE);

        // Lier utilisateurs aux acteurs
        uTaxateur.setAgent(taxateur);
        taxateur.setUtilisateur(uTaxateur);

        uReceveur.setAgent(receveur);
        receveur.setUtilisateur(uReceveur);

        uContribuable.setContribuable(c2);
        uContribuable.setAgent(agentC2);
        c2.setUtilisateur(uContribuable);
        agentC2.setUtilisateur(uContribuable);

        uContribuable2.setContribuable(c3);
        uContribuable2.setAgent(agentC3);
        c3.setUtilisateur(uContribuable2);
        agentC3.setUtilisateur(uContribuable2);

        uContribuable3.setContribuable(c1);
        uContribuable3.setAgent(agentC1);
        c1.setUtilisateur(uContribuable3);
        agentC1.setUtilisateur(uContribuable3);

        // Mettre certains comptes hors première connexion pour permettre login direct
        admin.setPremierConnexion(false);
        uTaxateur.setPremierConnexion(false);
        uReceveur.setPremierConnexion(false);
        // Laisser le contribuable contrib1 en première connexion pour tester le flux
        // Mettre contrib2 en login direct pour tester les endpoints /mine
        uContribuable2.setPremierConnexion(false);
        uContribuable3.setPremierConnexion(false);

        utilisateurRepository.saveAll(List.of(admin, uTaxateur, uReceveur, uContribuable, uContribuable2, uContribuable3));
        agentRepository.saveAll(List.of(taxateur, receveur, agentC1, agentC2, agentC3)); // sync côté inverse
        contribuableRepository.saveAll(List.of(c1, c2, c3));

        // 5) Propriétés (IF)
        GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
        Propriete p1 = new Propriete();
        p1.setType(TypePropriete.VI);
        p1.setLocalite("Katuba");
        p1.setRangLocalite(2);
        p1.setSuperficie(350.0);
        p1.setAdresse("Parcelle 123, Q. Katuba");
        p1.setProprietaire(c2);
        p1.setMontantImpot(350.0 * 1.25); // Exemple statique pour le test
        p1.setLocation(gf.createPoint(new Coordinate(11.65, -7.45)));

        Propriete p2 = new Propriete();
        p2.setType(TypePropriete.AP);
        p2.setLocalite("Kenya");
        p2.setRangLocalite(1);
        p2.setSuperficie(0.0);
        p2.setAdresse("Immeuble 7, Av. Université");
        p2.setProprietaire(c2);
        p2.setMontantImpot(150.0); // Exemple
        p2.setLocation(gf.createPoint(new Coordinate(11.67, -7.43)));

        Propriete p3 = new Propriete();
        p3.setType(TypePropriete.AP);
        p3.setLocalite("Katuba");
        p3.setRangLocalite(3);
        p3.setSuperficie(120.0);
        p3.setAdresse("Parcelle 88, Q. Katuba");
        p3.setProprietaire(c3);
        p3.setMontantImpot(120.0 * 1.10);
        p3.setLocation(gf.createPoint(new Coordinate(11.63, -7.47)));

        proprieteRepository.saveAll(List.of(p1, p2, p3));

        // 6) Concession minière (ICM)
        ConcessionMinier cm1 = new ConcessionMinier();
        cm1.setNombreCarresMinier(12.0);
        cm1.setDateAcquisition(new Date());
        cm1.setType(TypeConcession.EXPLOITATION);
        cm1.setAnnexe("annexe1");
        cm1.setTitulaire(c1);
        cm1.setMontantImpot(12.0 * 84.955 * 2.0); // valeur test
        concessionMinierRepository.save(cm1);

        // Concession pour contrib2 (c3) pour tester /api/concessions/mine
        ConcessionMinier cm2 = new ConcessionMinier();
        cm2.setNombreCarresMinier(5.0);
        cm2.setDateAcquisition(new Date());
        cm2.setType(TypeConcession.RECHERCHE);
        cm2.setAnnexe("annexe2");
        cm2.setTitulaire(c3);
        cm2.setMontantImpot(5.0 * 84.955 * 1.5);
        concessionMinierRepository.save(cm2);

        // 7) Déclarations
        Declaration dIF = new Declaration();
        dIF.setDate(new Date());
        dIF.setMontant(p1.getMontantImpot());
        dIF.setStatut(StatutDeclaration.SOUMISE);
        dIF.setTypeImpot(TypeImpot.IF);
        dIF.setExoneration(false);
        dIF.setPropriete(p1);
        dIF.setAgentValidateur(taxateur);

        Declaration dICM = new Declaration();
        dICM.setDate(new Date());
        dICM.setMontant(cm1.getMontantImpot());
        dICM.setStatut(StatutDeclaration.VALIDEE);
        dICM.setTypeImpot(TypeImpot.ICM);
        dICM.setExoneration(false);
        dICM.setConcession(cm1);
        dICM.setAgentValidateur(chefBureau);

        declarationRepository.saveAll(List.of(dIF, dICM));

        // 8) Paiement pour une déclaration validée
        Paiement paiement = new Paiement(new Date(), dICM.getMontant(), ModePaiement.BANQUE, StatutPaiement.VALIDE, "BORD-2025-0001");
        paiementRepository.save(paiement);
        dICM.setPaiement(paiement);
        declarationRepository.save(dICM);

        // 9) Pénalité liée à la déclaration IF (ex: retard)
        Penalite pen = new Penalite(MotifPenalite.RETARD, 25_000.0, new Date());
        pen.setDeclaration(dIF);
        penaliteRepository.save(pen);

        // 10) Dossier de recouvrement pour le contribuable c2
        DossierRecouvrement dossier = new DossierRecouvrement(500_000.0, 50_000.0, new Date(), null);
        dossier.setContribuable(c2);
        dossierRecouvrementRepository.save(dossier);

        // 11) Relance et poursuite
        Relance rel = new Relance(new Date(), TypeRelance.EMAIL, StatutRelance.ENVOYEE, "Veuillez régulariser votre situation.");
        rel.setDossierRecouvrement(dossier);
        relanceRepository.save(rel);

        Poursuite prs = new Poursuite();
        prs.setType(TypePoursuite.SAISIE_IMMOBILIERE);
        prs.setDateLancement(new Date());
        prs.setStatut(StatutPoursuite.EN_COURS);
        prs.setMontantRecouvre(0.0);
        prs.setAgentInitiateur(chefBureau);
        prs.setDossierRecouvrement(dossier);
        poursuiteRepository.save(prs);

        // 12) Apurement (provisoire)
        Apurement ap = new Apurement(new Date(), TypeApurement.TRANSACTION, 100_000.0, "Plan de paiement", StatutApurement.ACCEPTEE, true);
        ap.setAgent(receveur);
        ap.setAgentValidateur(chefBureau);
        ap.setDeclaration(dIF);
        ap.setDossierRecouvrement(dossier);
        apurementRepository.save(ap);

        // 13) Véhicule + plaque + certificat + vignette
        Vehicule vh = new Vehicule("TOYOTA", "Hilux", 2022, "HVX-456", "CHS-987654321");
        vh.setProprietaire(c2);
        vehiculeRepository.save(vh);

        Plaque plq = new Plaque("SER-0001", null, true);
        plq.setVehicule(vh);
        plq.setDisponible(false);
        plq.setNumplaque("PLAQUE-TEST-0001");
        plaqueRepository.save(plq);

        Certificat cert = new Certificat("CERT-0001", new Date(System.currentTimeMillis() + 31536000000L), 250.0, dICM, vh, receveur);
        certificatRepository.save(cert);

        Vignette vgt = new Vignette("VG-0001", new Date(System.currentTimeMillis() + 31536000000L), 120.0, vh, receveur);
        vignetteRepository.save(vgt);

        // 14) Contrôle fiscal
        ControleFiscal ctrl = new ControleFiscal(TypeControle.VERIFICATION_PAIEMENT, dIF, taxateur);
        ctrl.setObservations("Vérification des bases IF");
        ctrl.setAgentValidateur(chefBureau);
        controleFiscalRepository.save(ctrl);
        
        // 15) Natures d'impôt
        NatureImpot ni1 = new NatureImpot();
        ni1.setCode(TypeImpot.IF);
        ni1.setNom("Impôt Foncier");
        ni1.setDescription("Impôt sur les propriétés foncières");
        ni1.setActif(true);
        
        NatureImpot ni2 = new NatureImpot();
        ni2.setCode(TypeImpot.IRL);
        ni2.setNom("Impôt sur Revenu Locatif");
        ni2.setDescription("Impôt sur les revenus locatifs des propriétés");
        ni2.setActif(true);
        
        NatureImpot ni3 = new NatureImpot();
        ni3.setCode(TypeImpot.ICM);
        ni3.setNom("Impôt sur Concessions Minières");
        ni3.setDescription("Impôt sur les concessions minières");
        ni3.setActif(true);
        
        natureImpotRepository.saveAll(List.of(ni1, ni2, ni3));
        
        // 16) Liens propriété-impôt
        ProprieteImpot pi1 = new ProprieteImpot();
        pi1.setPropriete(p1);
        pi1.setNatureImpot(ni1);
        pi1.setTauxImposition(1.25);
        pi1.setActif(true);
        
        ProprieteImpot pi2 = new ProprieteImpot();
        pi2.setPropriete(p2);
        pi2.setNatureImpot(ni1);
        pi2.setTauxImposition(1.0);
        pi2.setActif(true);
        
        ProprieteImpot pi3 = new ProprieteImpot();
        pi3.setPropriete(p1);
        pi3.setNatureImpot(ni2);
        pi3.setTauxImposition(2.0);
        pi3.setActif(true);
        
        proprieteImpotRepository.saveAll(List.of(pi1, pi2, pi3));
        
        // 17) Taxations
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        
        // Taxation pour p1 avec IF
        Taxation t1 = new Taxation();
        t1.setDateTaxation(new Date());
        t1.setMontant(p1.getMontantImpot());
        t1.setExercice(currentYear);
        t1.setStatut(StatutTaxation.VALIDEE);
        t1.setTypeImpot(TypeImpot.IF);
        t1.setExoneration(false);
        t1.setPropriete(p1);
        t1.setProprieteImpot(pi1);
        t1.setAgentTaxateur(taxateur);
        cal.set(currentYear, Calendar.DECEMBER, 31);
        t1.setDateEcheance(cal.getTime());
        
        // Taxation pour p2 avec IF
        Taxation t2 = new Taxation();
        t2.setDateTaxation(new Date());
        t2.setMontant(p2.getMontantImpot());
        t2.setExercice(currentYear);
        t2.setStatut(StatutTaxation.EN_ATTENTE);
        t2.setTypeImpot(TypeImpot.IF);
        t2.setExoneration(false);
        t2.setPropriete(p2);
        t2.setProprieteImpot(pi2);
        t2.setAgentTaxateur(taxateur);
        cal.set(currentYear, Calendar.DECEMBER, 31);
        t2.setDateEcheance(cal.getTime());
        
        // Taxation pour p1 avec IRL
        Taxation t3 = new Taxation();
        t3.setDateTaxation(new Date());
        t3.setMontant(p1.getSuperficie() * 2.0); // Exemple de calcul pour IRL
        t3.setExercice(currentYear);
        t3.setStatut(StatutTaxation.VALIDEE);
        t3.setTypeImpot(TypeImpot.IRL);
        t3.setExoneration(false);
        t3.setPropriete(p1);
        t3.setProprieteImpot(pi3);
        t3.setAgentTaxateur(taxateur);
        cal.set(currentYear, Calendar.DECEMBER, 31);
        t3.setDateEcheance(cal.getTime());
        
        // Taxation payée
        Taxation t4 = new Taxation();
        t4.setDateTaxation(new Date());
        t4.setMontant(p3.getMontantImpot());
        t4.setExercice(currentYear - 1); // Année précédente
        t4.setStatut(StatutTaxation.PAYEE);
        t4.setTypeImpot(TypeImpot.IF);
        t4.setExoneration(false);
        t4.setPropriete(p3);
        t4.setAgentTaxateur(taxateur);
        t4.setPaiement(paiement);
        cal.set(currentYear - 1, Calendar.DECEMBER, 31);
        t4.setDateEcheance(cal.getTime());
        
        // Taxation exonérée
        Taxation t5 = new Taxation();
        t5.setDateTaxation(new Date());
        t5.setMontant(cm1.getMontantImpot());
        t5.setExercice(currentYear);
        t5.setStatut(StatutTaxation.EXONEREE);
        t5.setTypeImpot(TypeImpot.ICM);
        t5.setExoneration(true);
        t5.setMotifExoneration("Exonération accordée pour promotion de l'investissement minier");
        t5.setAgentTaxateur(chefBureau);
        cal.set(currentYear, Calendar.DECEMBER, 31);
        t5.setDateEcheance(cal.getTime());
        
        taxationRepository.saveAll(List.of(t1, t2, t3, t4, t5));

        System.out.println("TestDataSeeder: seeding terminé.");
    }
}
