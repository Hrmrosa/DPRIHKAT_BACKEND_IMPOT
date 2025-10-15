-- Données pour les paiements
INSERT INTO paiement (id, date_paiement, montant, mode_paiement, reference_paiement, statut, contribuable_id, document_recouvrement_id) VALUES
    -- Paiements AMR
    ('f47ac10b-58cc-4372-a567-0e02b2c3d524', '2025-01-15', 2000000.0, 'VIREMENT', 'PAY-001', 'VALIDE', 'f47ac10b-58cc-4372-a567-0e02b2c3d481', 'f47ac10b-58cc-4372-a567-0e02b2c3d511'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d525', '2025-01-16', 1000000.0, 'CHEQUE', 'PAY-002', 'VALIDE', 'f47ac10b-58cc-4372-a567-0e02b2c3d482', 'f47ac10b-58cc-4372-a567-0e02b2c3d512'),
    
    -- Paiements MED
    ('f47ac10b-58cc-4372-a567-0e02b2c3d526', '2025-01-10', 800000.0, 'ESPECES', 'PAY-003', 'VALIDE', 'f47ac10b-58cc-4372-a567-0e02b2c3d483', 'f47ac10b-58cc-4372-a567-0e02b2c3d513'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d527', '2025-01-11', 400000.0, 'MOBILE_MONEY', 'PAY-004', 'VALIDE', 'f47ac10b-58cc-4372-a567-0e02b2c3d484', 'f47ac10b-58cc-4372-a567-0e02b2c3d514'),
    
    -- Paiements Commandement
    ('f47ac10b-58cc-4372-a567-0e02b2c3d528', '2025-01-25', 3000000.0, 'VIREMENT', 'PAY-005', 'VALIDE', 'f47ac10b-58cc-4372-a567-0e02b2c3d481', 'f47ac10b-58cc-4372-a567-0e02b2c3d516');

-- Données pour les quittances
INSERT INTO quittance (id, numero, date_emission, montant_total, paiement_id, agent_id) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d529', 'QUIT-001', '2025-01-15', 2000000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d524', 'f47ac10b-58cc-4372-a567-0e02b2c3d477'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d530', 'QUIT-002', '2025-01-16', 1000000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d525', 'f47ac10b-58cc-4372-a567-0e02b2c3d477'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d531', 'QUIT-003', '2025-01-10', 800000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d526', 'f47ac10b-58cc-4372-a567-0e02b2c3d477'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d532', 'QUIT-004', '2025-01-11', 400000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d527', 'f47ac10b-58cc-4372-a567-0e02b2c3d477'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d533', 'QUIT-005', '2025-01-25', 3000000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d528', 'f47ac10b-58cc-4372-a567-0e02b2c3d477');
