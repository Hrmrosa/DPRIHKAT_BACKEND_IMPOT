-- Données de test pour les paiements

-- Paiements
INSERT INTO paiement (id, date_paiement, montant, mode_paiement, reference_paiement, statut_paiement, contribuable_id, document_recouvrement_id) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d506', '2025-01-08', 500000.0, 'VIREMENT', 'PAY-001', 'VALIDE', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 'f47ac10b-58cc-4372-a567-0e02b2c3d497'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d507', '2025-01-09', 250000.0, 'CHEQUE', 'PAY-002', 'EN_ATTENTE', 'f47ac10b-58cc-4372-a567-0e02b2c3d480', 'f47ac10b-58cc-4372-a567-0e02b2c3d498'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d508', '2025-01-10', 100000.0, 'ESPECES', 'PAY-003', 'VALIDE', 'f47ac10b-58cc-4372-a567-0e02b2c3d481', 'f47ac10b-58cc-4372-a567-0e02b2c3d499');

-- Quittances
INSERT INTO quittance (id, numero_quittance, date_emission, montant_total, statut, paiement_id) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d509', 'QUIT-001', '2025-01-08', 500000.0, 'EMISE', 'f47ac10b-58cc-4372-a567-0e02b2c3d506'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d510', 'QUIT-002', '2025-01-09', 250000.0, 'EN_ATTENTE', 'f47ac10b-58cc-4372-a567-0e02b2c3d507'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d511', 'QUIT-003', '2025-01-10', 100000.0, 'EMISE', 'f47ac10b-58cc-4372-a567-0e02b2c3d508');

-- Échelonnements
INSERT INTO echelonnement (id, date_demande, nombre_echeances, montant_total, montant_par_echeance, statut, contribuable_id, document_recouvrement_id) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d512', '2025-01-11', 3, 1020000.0, 340000.0, 'APPROUVE', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 'f47ac10b-58cc-4372-a567-0e02b2c3d497'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d513', '2025-01-12', 2, 510000.0, 255000.0, 'EN_ATTENTE', 'f47ac10b-58cc-4372-a567-0e02b2c3d480', 'f47ac10b-58cc-4372-a567-0e02b2c3d498'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d514', '2025-01-13', 4, 255000.0, 63750.0, 'REFUSE', 'f47ac10b-58cc-4372-a567-0e02b2c3d481', 'f47ac10b-58cc-4372-a567-0e02b2c3d499');

-- Échéances
INSERT INTO echeance (id, numero_echeance, date_echeance, montant, statut, echelonnement_id) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d515', 1, '2025-02-11', 340000.0, 'A_PAYER', 'f47ac10b-58cc-4372-a567-0e02b2c3d512'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d516', 2, '2025-03-11', 340000.0, 'A_PAYER', 'f47ac10b-58cc-4372-a567-0e02b2c3d512'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d517', 3, '2025-04-11', 340000.0, 'A_PAYER', 'f47ac10b-58cc-4372-a567-0e02b2c3d512'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d518', 1, '2025-02-12', 255000.0, 'A_PAYER', 'f47ac10b-58cc-4372-a567-0e02b2c3d513'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d519', 2, '2025-03-12', 255000.0, 'A_PAYER', 'f47ac10b-58cc-4372-a567-0e02b2c3d513');
