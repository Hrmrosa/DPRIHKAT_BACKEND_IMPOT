-- Données pour les déclarations
INSERT INTO declaration (id, date_declaration, type_impot, montant_declare, statut, reference, exercice, periode, contribuable_id, taxateur_id) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d496', '2025-01-01', 'IBP', 5000000.0, 'VALIDEE', 'DEC-001', '2024', 'ANNUEL', 'f47ac10b-58cc-4372-a567-0e02b2c3d481', 'f47ac10b-58cc-4372-a567-0e02b2c3d478'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d497', '2025-01-02', 'IPR', 2000000.0, 'VALIDEE', 'DEC-002', '2024', 'MENSUEL', 'f47ac10b-58cc-4372-a567-0e02b2c3d482', 'f47ac10b-58cc-4372-a567-0e02b2c3d478'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d498', '2025-01-03', 'TVA', 1500000.0, 'EN_ATTENTE', 'DEC-003', '2024', 'MENSUEL', 'f47ac10b-58cc-4372-a567-0e02b2c3d483', 'f47ac10b-58cc-4372-a567-0e02b2c3d478'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d499', '2025-01-04', 'IPR', 800000.0, 'VALIDEE', 'DEC-004', '2024', 'MENSUEL', 'f47ac10b-58cc-4372-a567-0e02b2c3d484', 'f47ac10b-58cc-4372-a567-0e02b2c3d478'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d500', '2025-01-05', 'IBP', 10000000.0, 'VALIDEE', 'DEC-005', '2024', 'ANNUEL', 'f47ac10b-58cc-4372-a567-0e02b2c3d485', 'f47ac10b-58cc-4372-a567-0e02b2c3d478');

-- Données pour les taxations
INSERT INTO taxation (id, date_taxation, type_impot, montant_base, montant_impose, montant_penalites, reference, exercice, periode, contribuable_id, taxateur_id) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d501', '2025-01-06', 'IBP', 5000000.0, 5500000.0, 50000.0, 'TAX-001', '2024', 'ANNUEL', 'f47ac10b-58cc-4372-a567-0e02b2c3d481', 'f47ac10b-58cc-4372-a567-0e02b2c3d478'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d502', '2025-01-07', 'IPR', 2000000.0, 2200000.0, 20000.0, 'TAX-002', '2024', 'MENSUEL', 'f47ac10b-58cc-4372-a567-0e02b2c3d482', 'f47ac10b-58cc-4372-a567-0e02b2c3d478'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d503', '2025-01-08', 'TVA', 1500000.0, 1650000.0, 15000.0, 'TAX-003', '2024', 'MENSUEL', 'f47ac10b-58cc-4372-a567-0e02b2c3d483', 'f47ac10b-58cc-4372-a567-0e02b2c3d478'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d504', '2025-01-09', 'IPR', 800000.0, 880000.0, 8000.0, 'TAX-004', '2024', 'MENSUEL', 'f47ac10b-58cc-4372-a567-0e02b2c3d484', 'f47ac10b-58cc-4372-a567-0e02b2c3d478'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d505', '2025-01-10', 'IBP', 10000000.0, 11000000.0, 100000.0, 'TAX-005', '2024', 'ANNUEL', 'f47ac10b-58cc-4372-a567-0e02b2c3d485', 'f47ac10b-58cc-4372-a567-0e02b2c3d478');
