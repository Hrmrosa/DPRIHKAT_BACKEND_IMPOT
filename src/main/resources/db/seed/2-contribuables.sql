-- Données de test pour les contribuables et leurs biens

-- Contribuables
INSERT INTO agent (id, nom, sexe, matricule, grade, utilisateur_id) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d479', 'SARL CONGOLAISE DE COMMERCE', 'M', 'CONTRIB-001', 'CONTRIBUABLE', 'f47ac10b-58cc-4372-a567-0e02b2c3d406'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d480', 'Établissements KALALA', 'M', 'CONTRIB-002', 'CONTRIBUABLE', 'f47ac10b-58cc-4372-a567-0e02b2c3d407'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d481', 'Jean MUTOMBO', 'M', 'CONTRIB-003', 'CONTRIBUABLE', 'f47ac10b-58cc-4372-a567-0e02b2c3d408');

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
