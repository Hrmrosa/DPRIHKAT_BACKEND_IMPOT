-- Données de test pour le module de recouvrement

-- Contribuables
INSERT INTO agent (id, nom, sexe, matricule, grade) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d479', 'SARL CONGOLAISE DE COMMERCE', 'M', 'CONTRIB-001', 'CONTRIBUABLE'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d480', 'Établissements KALALA', 'M', 'CONTRIB-002', 'CONTRIBUABLE'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d481', 'Jean MUTOMBO', 'M', 'CONTRIB-003', 'CONTRIBUABLE');

INSERT INTO contribuable (agent_id, adresse_principale, telephone_principal, email, type, id_nat, nrc, numero_identification_contribuable, commercant) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d479', '123 Avenue du Commerce, Kinshasa', '+243123456789', 'contact@sarl-cc.cd', 'PERSONNE_MORALE', 'ID-001', 'RCCM-001', 'A001', true),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d480', '45 Avenue du Marché, Lubumbashi', '+243987654321', 'ets.kalala@gmail.com', 'PERSONNE_PHYSIQUE', 'ID-002', 'RCCM-002', 'A002', true),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d481', '78 Rue des Artisans, Kinshasa', '+243555555555', 'jean.mutombo@gmail.com', 'PERSONNE_PHYSIQUE', 'ID-003', null, 'A003', false);

-- Propriétés
INSERT INTO propriete (id, adresse, reference, superficie, usage, valeur_locative, montant_impot, proprietaire_id) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d482', '123 Avenue du Commerce, Kinshasa', 'PROP-001', 500.0, 'COMMERCIAL', 5000000.0, 250000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d479'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d483', '45 Avenue du Marché, Lubumbashi', 'PROP-002', 200.0, 'COMMERCIAL', 2000000.0, 100000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d480'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d484', '78 Rue des Artisans, Kinshasa', 'PROP-003', 150.0, 'RESIDENTIEL', 1500000.0, 75000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d481');

-- Véhicules
INSERT INTO vehicule (id, immatriculation, marque, modele, chassis, annee, type, cylindree, montant_vignette, proprietaire_id, contribuable_id) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d485', 'ABC123', 'Toyota', 'Land Cruiser', 'CHAS001', 2020, 'UTILITAIRE', 4500.0, 150000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 'f47ac10b-58cc-4372-a567-0e02b2c3d479'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d486', 'DEF456', 'Mitsubishi', 'L200', 'CHAS002', 2019, 'UTILITAIRE', 2500.0, 100000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d480', 'f47ac10b-58cc-4372-a567-0e02b2c3d480'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d487', 'GHI789', 'Honda', 'Civic', 'CHAS003', 2018, 'PARTICULIER', 1800.0, 75000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d481', 'f47ac10b-58cc-4372-a567-0e02b2c3d481');

-- Dossiers de recouvrement
INSERT INTO dossier_recouvrement (id, date_ouverture, statut, total_dette, total_recouvre, code_qr, contribuable_id) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d488', '2025-01-01', 'EN_COURS', 1000000.0, 0.0, 'QR-001', 'f47ac10b-58cc-4372-a567-0e02b2c3d479'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d489', '2025-01-02', 'EN_COURS', 500000.0, 0.0, 'QR-002', 'f47ac10b-58cc-4372-a567-0e02b2c3d480'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d490', '2025-01-03', 'EN_COURS', 250000.0, 0.0, 'QR-003', 'f47ac10b-58cc-4372-a567-0e02b2c3d481');

-- Documents de recouvrement
INSERT INTO document_recouvrement (id, type, statut, date_generation, date_echeance, reference, montant_principal, montant_penalites, montant_total, dossier_recouvrement_id, contribuable_id, agent_generateur_id) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d491', 'AMR', 'GENERE', '2025-01-01', '2025-01-16', 'AMR-001', 1000000.0, 20000.0, 1020000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d488', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 'f47ac10b-58cc-4372-a567-0e02b2c3d479'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d492', 'MED', 'GENERE', '2025-01-02', '2025-01-10', 'MED-001', 500000.0, 10000.0, 510000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d489', 'f47ac10b-58cc-4372-a567-0e02b2c3d480', 'f47ac10b-58cc-4372-a567-0e02b2c3d480'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d493', 'COMMANDEMENT', 'GENERE', '2025-01-03', '2025-01-11', 'CMD-001', 250000.0, 5000.0, 255000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d490', 'f47ac10b-58cc-4372-a567-0e02b2c3d481', 'f47ac10b-58cc-4372-a567-0e02b2c3d481');

-- Fermetures d'établissement
INSERT INTO fermeture_etablissement (id, agent_opj_id, adresse_etablissement, motif_fermeture, montant_amende) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d491', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', '123 Avenue du Commerce, Kinshasa', 'Non-paiement des impôts', 1000000.0),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d492', 'f47ac10b-58cc-4372-a567-0e02b2c3d480', '45 Avenue du Marché, Lubumbashi', 'Non-paiement des impôts', 100000.0),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d493', 'f47ac10b-58cc-4372-a567-0e02b2c3d481', '78 Rue des Artisans, Kinshasa', 'Non-paiement des impôts', 50000.0);
