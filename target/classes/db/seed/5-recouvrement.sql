-- Données pour les dossiers de recouvrement
INSERT INTO dossier_recouvrement (id, date_ouverture, statut, total_dette, total_recouvre, code_qr, contribuable_id) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d506', '2025-01-01', 'EN_COURS', 5550000.0, 0.0, 'QR-001', 'f47ac10b-58cc-4372-a567-0e02b2c3d481'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d507', '2025-01-02', 'EN_COURS', 2220000.0, 0.0, 'QR-002', 'f47ac10b-58cc-4372-a567-0e02b2c3d482'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d508', '2025-01-03', 'EN_COURS', 1665000.0, 0.0, 'QR-003', 'f47ac10b-58cc-4372-a567-0e02b2c3d483'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d509', '2025-01-04', 'EN_COURS', 888000.0, 0.0, 'QR-004', 'f47ac10b-58cc-4372-a567-0e02b2c3d484'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d510', '2025-01-05', 'EN_COURS', 11100000.0, 0.0, 'QR-005', 'f47ac10b-58cc-4372-a567-0e02b2c3d485');

-- Données pour les documents de recouvrement
INSERT INTO document_recouvrement (id, type, statut, date_generation, date_echeance, reference, montant_principal, montant_penalites, montant_total, dossier_recouvrement_id, contribuable_id, agent_generateur_id) VALUES
    -- AMR
    ('f47ac10b-58cc-4372-a567-0e02b2c3d511', 'AMR', 'NOTIFIE', '2025-01-01', '2025-01-16', 'AMR-001', 5500000.0, 50000.0, 5550000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d506', 'f47ac10b-58cc-4372-a567-0e02b2c3d481', 'f47ac10b-58cc-4372-a567-0e02b2c3d477'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d512', 'AMR', 'NOTIFIE', '2025-01-02', '2025-01-17', 'AMR-002', 2200000.0, 20000.0, 2220000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d507', 'f47ac10b-58cc-4372-a567-0e02b2c3d482', 'f47ac10b-58cc-4372-a567-0e02b2c3d477'),
    
    -- MED
    ('f47ac10b-58cc-4372-a567-0e02b2c3d513', 'MED', 'NOTIFIE', '2025-01-03', '2025-01-11', 'MED-001', 1650000.0, 15000.0, 1665000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d508', 'f47ac10b-58cc-4372-a567-0e02b2c3d483', 'f47ac10b-58cc-4372-a567-0e02b2c3d477'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d514', 'MED', 'NOTIFIE', '2025-01-04', '2025-01-12', 'MED-002', 880000.0, 8000.0, 888000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d509', 'f47ac10b-58cc-4372-a567-0e02b2c3d484', 'f47ac10b-58cc-4372-a567-0e02b2c3d477'),
    
    -- Contraintes
    ('f47ac10b-58cc-4372-a567-0e02b2c3d515', 'CONTRAINTE', 'GENERE', '2025-01-17', null, 'CONT-001', 5550000.0, 111000.0, 5661000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d506', 'f47ac10b-58cc-4372-a567-0e02b2c3d481', 'f47ac10b-58cc-4372-a567-0e02b2c3d477'),
    
    -- Commandements
    ('f47ac10b-58cc-4372-a567-0e02b2c3d516', 'COMMANDEMENT', 'GENERE', '2025-01-18', '2025-01-26', 'CMD-001', 5661000.0, 169830.0, 5830830.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d506', 'f47ac10b-58cc-4372-a567-0e02b2c3d481', 'f47ac10b-58cc-4372-a567-0e02b2c3d480'),
    
    -- ATD
    ('f47ac10b-58cc-4372-a567-0e02b2c3d517', 'ATD', 'GENERE', '2025-01-27', null, 'ATD-001', 5830830.0, 0.0, 5830830.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d506', 'f47ac10b-58cc-4372-a567-0e02b2c3d481', 'f47ac10b-58cc-4372-a567-0e02b2c3d477');

-- Données pour les fermetures d'établissement
INSERT INTO fermeture_etablissement (id, agent_opj_id, adresse_etablissement, motif_fermeture, montant_amende, document_recouvrement_id) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d518', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', '123 Avenue du Commerce, Kinshasa', 'Non-paiement des impôts', 1000000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d516'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d519', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', '45 Avenue du Marché, Lubumbashi', 'Non-paiement des impôts', 100000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d512');

-- Données pour les relances
INSERT INTO relance (id, date_relance, type, contenu, statut, agent_id, contribuable_id) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d520', '2025-01-05', 'COURRIER', 'Rappel de paiement AMR-001', 'ENVOYEE', 'f47ac10b-58cc-4372-a567-0e02b2c3d477', 'f47ac10b-58cc-4372-a567-0e02b2c3d481'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d521', '2025-01-10', 'EMAIL', 'Rappel de paiement AMR-002', 'ENVOYEE', 'f47ac10b-58cc-4372-a567-0e02b2c3d477', 'f47ac10b-58cc-4372-a567-0e02b2c3d482'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d522', '2025-01-15', 'SMS', 'Rappel de paiement MED-001', 'ENVOYEE', 'f47ac10b-58cc-4372-a567-0e02b2c3d477', 'f47ac10b-58cc-4372-a567-0e02b2c3d483'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d523', '2025-01-20', 'TELEPHONE', 'Rappel de paiement MED-002', 'ENVOYEE', 'f47ac10b-58cc-4372-a567-0e02b2c3d477', 'f47ac10b-58cc-4372-a567-0e02b2c3d484');
