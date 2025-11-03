-- Données pour les propriétés
INSERT INTO propriete (id, adresse, reference, superficie, usage, valeur_locative, montant_impot, proprietaire_id) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d486', '123 Avenue du Commerce, Kinshasa', 'PROP-001', 500.0, 'COMMERCIAL', 5000000.0, 250000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d481'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d487', '45 Avenue du Marché, Lubumbashi', 'PROP-002', 200.0, 'COMMERCIAL', 2000000.0, 100000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d482'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d488', '78 Rue des Artisans, Kinshasa', 'PROP-003', 150.0, 'RESIDENTIEL', 1500000.0, 75000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d483'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d489', '90 Avenue de la Paix, Goma', 'PROP-004', 120.0, 'RESIDENTIEL', 1200000.0, 60000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d484'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d490', '234 Boulevard Minier, Kolwezi', 'PROP-005', 1000.0, 'INDUSTRIEL', 10000000.0, 500000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d485');

-- Données pour les véhicules
INSERT INTO vehicule (id, immatriculation, marque, modele, chassis, annee, type, cylindree, montant_vignette, proprietaire_id, contribuable_id) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d491', 'ABC123', 'Toyota', 'Land Cruiser', 'CHAS001', 2020, 'UTILITAIRE', 4500.0, 150000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d481', 'f47ac10b-58cc-4372-a567-0e02b2c3d481'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d492', 'DEF456', 'Mitsubishi', 'L200', 'CHAS002', 2019, 'UTILITAIRE', 2500.0, 100000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d482', 'f47ac10b-58cc-4372-a567-0e02b2c3d482'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d493', 'GHI789', 'Honda', 'Civic', 'CHAS003', 2018, 'PARTICULIER', 1800.0, 75000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d483', 'f47ac10b-58cc-4372-a567-0e02b2c3d483'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d494', 'JKL012', 'Hyundai', 'Tucson', 'CHAS004', 2021, 'PARTICULIER', 2000.0, 80000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d484', 'f47ac10b-58cc-4372-a567-0e02b2c3d484'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d495', 'MNO345', 'Volvo', 'FH16', 'CHAS005', 2020, 'POIDS_LOURD', 16000.0, 300000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d485', 'f47ac10b-58cc-4372-a567-0e02b2c3d485');
