-- Données de test pour les biens des contribuables

-- Propriétés immobilières supplémentaires
INSERT INTO propriete (id, adresse, reference, superficie, usage, valeur_locative, montant_impot, proprietaire_id) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d520', '234 Boulevard Central, Kinshasa', 'PROP-004', 1000.0, 'COMMERCIAL', 10000000.0, 500000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d479'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d521', '567 Avenue Industrielle, Lubumbashi', 'PROP-005', 2000.0, 'INDUSTRIEL', 15000000.0, 750000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d479'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d522', '89 Rue Commerciale, Goma', 'PROP-006', 300.0, 'COMMERCIAL', 3000000.0, 150000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d480'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d523', '12 Avenue Résidentielle, Matadi', 'PROP-007', 200.0, 'RESIDENTIEL', 2000000.0, 100000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d481');

-- Véhicules supplémentaires
INSERT INTO vehicule (id, immatriculation, marque, modele, chassis, annee, type, cylindree, montant_vignette, proprietaire_id, contribuable_id) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d524', 'JKL012', 'Mercedes', 'Actros', 'CHAS004', 2021, 'UTILITAIRE', 6000.0, 200000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 'f47ac10b-58cc-4372-a567-0e02b2c3d479'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d525', 'MNO345', 'Volvo', 'FH', 'CHAS005', 2021, 'UTILITAIRE', 5500.0, 180000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 'f47ac10b-58cc-4372-a567-0e02b2c3d479'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d526', 'PQR678', 'Toyota', 'Hilux', 'CHAS006', 2020, 'UTILITAIRE', 2800.0, 120000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d480', 'f47ac10b-58cc-4372-a567-0e02b2c3d480'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d527', 'STU901', 'Hyundai', 'Tucson', 'CHAS007', 2019, 'PARTICULIER', 2000.0, 90000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d481', 'f47ac10b-58cc-4372-a567-0e02b2c3d481');

-- Vignettes
INSERT INTO vignette (id, numero_vignette, date_emission, date_expiration, montant, statut, vehicule_id) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d528', 'VIG-001-2025', '2025-01-01', '2025-12-31', 150000.0, 'VALIDE', 'f47ac10b-58cc-4372-a567-0e02b2c3d485'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d529', 'VIG-002-2025', '2025-01-01', '2025-12-31', 200000.0, 'VALIDE', 'f47ac10b-58cc-4372-a567-0e02b2c3d524'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d530', 'VIG-003-2025', '2025-01-01', '2025-12-31', 180000.0, 'VALIDE', 'f47ac10b-58cc-4372-a567-0e02b2c3d525'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d531', 'VIG-004-2025', '2025-01-01', '2025-12-31', 100000.0, 'VALIDE', 'f47ac10b-58cc-4372-a567-0e02b2c3d486'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d532', 'VIG-005-2025', '2025-01-01', '2025-12-31', 120000.0, 'VALIDE', 'f47ac10b-58cc-4372-a567-0e02b2c3d526'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d533', 'VIG-006-2025', '2025-01-01', '2025-12-31', 75000.0, 'VALIDE', 'f47ac10b-58cc-4372-a567-0e02b2c3d487'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d534', 'VIG-007-2025', '2025-01-01', '2025-12-31', 90000.0, 'VALIDE', 'f47ac10b-58cc-4372-a567-0e02b2c3d527');

-- Impôts fonciers
INSERT INTO impot_foncier (id, annee, montant_base, montant_du, statut, propriete_id) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d535', 2025, 5000000.0, 250000.0, 'A_PAYER', 'f47ac10b-58cc-4372-a567-0e02b2c3d482'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d536', 2025, 10000000.0, 500000.0, 'A_PAYER', 'f47ac10b-58cc-4372-a567-0e02b2c3d520'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d537', 2025, 15000000.0, 750000.0, 'A_PAYER', 'f47ac10b-58cc-4372-a567-0e02b2c3d521'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d538', 2025, 2000000.0, 100000.0, 'A_PAYER', 'f47ac10b-58cc-4372-a567-0e02b2c3d483'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d539', 2025, 3000000.0, 150000.0, 'A_PAYER', 'f47ac10b-58cc-4372-a567-0e02b2c3d522'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d540', 2025, 1500000.0, 75000.0, 'A_PAYER', 'f47ac10b-58cc-4372-a567-0e02b2c3d484'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d541', 2025, 2000000.0, 100000.0, 'A_PAYER', 'f47ac10b-58cc-4372-a567-0e02b2c3d523');
