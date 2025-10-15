-- Données pour les contribuables
INSERT INTO agent (id, nom, sexe, matricule, grade) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d481', 'SARL CONGOLAISE DE COMMERCE', 'M', 'CONTRIB-001', 'CONTRIBUABLE'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d482', 'Établissements KALALA', 'M', 'CONTRIB-002', 'CONTRIBUABLE'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d483', 'Jean MUTOMBO', 'M', 'CONTRIB-003', 'CONTRIBUABLE'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d484', 'Marie LUKUSA', 'F', 'CONTRIB-004', 'CONTRIBUABLE'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d485', 'SOCIETE MINIERE DU KATANGA', 'M', 'CONTRIB-005', 'CONTRIBUABLE');

INSERT INTO contribuable (agent_id, adresse_principale, telephone_principal, email, type, id_nat, nrc, numero_identification_contribuable, commercant) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d481', '123 Avenue du Commerce, Kinshasa', '+243123456789', 'contact@sarl-cc.cd', 'PERSONNE_MORALE', 'ID-001', 'RCCM-001', 'A001', true),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d482', '45 Avenue du Marché, Lubumbashi', '+243987654321', 'ets.kalala@gmail.com', 'PERSONNE_PHYSIQUE', 'ID-002', 'RCCM-002', 'A002', true),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d483', '78 Rue des Artisans, Kinshasa', '+243555555555', 'jean.mutombo@gmail.com', 'PERSONNE_PHYSIQUE', 'ID-003', null, 'A003', false),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d484', '90 Avenue de la Paix, Goma', '+243666666666', 'marie.lukusa@gmail.com', 'PERSONNE_PHYSIQUE', 'ID-004', null, 'A004', false),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d485', '234 Boulevard Minier, Kolwezi', '+243777777777', 'contact@somika.cd', 'PERSONNE_MORALE', 'ID-005', 'RCCM-003', 'A005', true);
