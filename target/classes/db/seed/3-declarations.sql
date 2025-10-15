-- Données de test pour les déclarations et taxations

-- Déclarations
INSERT INTO declaration (id, date_declaration, type_impot, montant_declare, statut, reference, exercice, periode, contribuable_id, taxateur_id) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d488', '2025-01-01', 'IBP', 1000000.0, 'VALIDEE', 'DEC-001', '2024', 'ANNUEL', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 'f47ac10b-58cc-4372-a567-0e02b2c3d411'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d489', '2025-01-02', 'IPR', 500000.0, 'EN_ATTENTE', 'DEC-002', '2024', 'MENSUEL', 'f47ac10b-58cc-4372-a567-0e02b2c3d480', 'f47ac10b-58cc-4372-a567-0e02b2c3d411'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d490', '2025-01-03', 'TVA', 250000.0, 'VALIDEE', 'DEC-003', '2024', 'MENSUEL', 'f47ac10b-58cc-4372-a567-0e02b2c3d481', 'f47ac10b-58cc-4372-a567-0e02b2c3d411');

-- Taxations
INSERT INTO taxation (id, date_taxation, type_impot, montant_base, montant_impose, montant_penalites, reference, exercice, periode, contribuable_id, taxateur_id) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d491', '2025-01-01', 'IBP', 1000000.0, 1200000.0, 20000.0, 'TAX-001', '2024', 'ANNUEL', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 'f47ac10b-58cc-4372-a567-0e02b2c3d411'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d492', '2025-01-02', 'IPR', 500000.0, 600000.0, 10000.0, 'TAX-002', '2024', 'MENSUEL', 'f47ac10b-58cc-4372-a567-0e02b2c3d480', 'f47ac10b-58cc-4372-a567-0e02b2c3d411'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d493', '2025-01-03', 'TVA', 250000.0, 300000.0, 5000.0, 'TAX-003', '2024', 'MENSUEL', 'f47ac10b-58cc-4372-a567-0e02b2c3d481', 'f47ac10b-58cc-4372-a567-0e02b2c3d411');
