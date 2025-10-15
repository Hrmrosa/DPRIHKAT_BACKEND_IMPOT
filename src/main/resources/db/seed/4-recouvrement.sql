-- Données de test pour le recouvrement

-- Dossiers de recouvrement
INSERT INTO dossier_recouvrement (id, date_ouverture, statut, total_dette, total_recouvre, code_qr, contribuable_id) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d494', '2025-01-01', 'EN_COURS', 1000000.0, 0.0, 'QR-001', 'f47ac10b-58cc-4372-a567-0e02b2c3d479'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d495', '2025-01-02', 'EN_COURS', 500000.0, 0.0, 'QR-002', 'f47ac10b-58cc-4372-a567-0e02b2c3d480'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d496', '2025-01-03', 'EN_COURS', 250000.0, 0.0, 'QR-003', 'f47ac10b-58cc-4372-a567-0e02b2c3d481');

-- Documents de recouvrement
INSERT INTO document_recouvrement (id, type, statut, date_generation, date_echeance, reference, montant_principal, montant_penalites, montant_total, dossier_recouvrement_id, contribuable_id, agent_generateur_id) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d497', 'AMR', 'GENERE', '2025-01-01', '2025-01-16', 'AMR-001', 1000000.0, 20000.0, 1020000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d494', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 'f47ac10b-58cc-4372-a567-0e02b2c3d410'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d498', 'MED', 'GENERE', '2025-01-02', '2025-01-10', 'MED-001', 500000.0, 10000.0, 510000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d495', 'f47ac10b-58cc-4372-a567-0e02b2c3d480', 'f47ac10b-58cc-4372-a567-0e02b2c3d410'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d499', 'COMMANDEMENT', 'GENERE', '2025-01-03', '2025-01-11', 'CMD-001', 250000.0, 5000.0, 255000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d496', 'f47ac10b-58cc-4372-a567-0e02b2c3d481', 'f47ac10b-58cc-4372-a567-0e02b2c3d410');

-- Fermetures d'établissement
INSERT INTO fermeture_etablissement (id, agent_opj_id, adresse_etablissement, motif_fermeture, montant_amende, document_recouvrement_id) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d500', 'f47ac10b-58cc-4372-a567-0e02b2c3d413', '123 Avenue du Commerce, Kinshasa', 'Non-paiement des impôts', 1000000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d497'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d501', 'f47ac10b-58cc-4372-a567-0e02b2c3d413', '45 Avenue du Marché, Lubumbashi', 'Non-paiement des impôts', 100000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d498'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d502', 'f47ac10b-58cc-4372-a567-0e02b2c3d413', '78 Rue des Artisans, Kinshasa', 'Non-paiement des impôts', 50000.0, 'f47ac10b-58cc-4372-a567-0e02b2c3d499');

-- Relances
INSERT INTO relance (id, date_relance, type, contenu, statut, agent_id, contribuable_id, document_recouvrement_id) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d503', '2025-01-05', 'COURRIER', 'Rappel de paiement AMR-001', 'ENVOYEE', 'f47ac10b-58cc-4372-a567-0e02b2c3d410', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 'f47ac10b-58cc-4372-a567-0e02b2c3d497'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d504', '2025-01-06', 'EMAIL', 'Rappel de paiement MED-001', 'ENVOYEE', 'f47ac10b-58cc-4372-a567-0e02b2c3d410', 'f47ac10b-58cc-4372-a567-0e02b2c3d480', 'f47ac10b-58cc-4372-a567-0e02b2c3d498'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d505', '2025-01-07', 'SMS', 'Rappel de paiement CMD-001', 'ENVOYEE', 'f47ac10b-58cc-4372-a567-0e02b2c3d410', 'f47ac10b-58cc-4372-a567-0e02b2c3d481', 'f47ac10b-58cc-4372-a567-0e02b2c3d499');
