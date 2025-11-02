-- Création des utilisateurs avec des UUID fixes pour la cohérence
INSERT INTO utilisateur (id, username, password, role, actif, email, telephone) VALUES
    ('e09230eb-30bf-47cc-afa4-972772121770', 'admin', '$2a$10$xVqYxMJw6TYwZeoGKsZ1H.4Y5UJAXKXhOD.pIKcrhcEfKyQ3dMKMG', 'ADMIN', true, 'admin@dpri.cd', '+243123456789'),
    ('e09230eb-30bf-47cc-afa4-972772121771', 'receveur1', '$2a$10$xVqYxMJw6TYwZeoGKsZ1H.4Y5UJAXKXhOD.pIKcrhcEfKyQ3dMKMG', 'RECEVEUR', true, 'receveur1@dpri.cd', '+243123456790'),
    ('e09230eb-30bf-47cc-afa4-972772121772', 'taxateur1', '$2a$10$xVqYxMJw6TYwZeoGKsZ1H.4Y5UJAXKXhOD.pIKcrhcEfKyQ3dMKMG', 'TAXATEUR', true, 'taxateur1@dpri.cd', '+243123456791'),
    ('e09230eb-30bf-47cc-afa4-972772121773', 'opj1', '$2a$10$xVqYxMJw6TYwZeoGKsZ1H.4Y5UJAXKXhOD.pIKcrhcEfKyQ3dMKMG', 'OPJ', true, 'opj1@dpri.cd', '+243123456792'),
    ('e09230eb-30bf-47cc-afa4-972772121774', 'huissier1', '$2a$10$xVqYxMJw6TYwZeoGKsZ1H.4Y5UJAXKXhOD.pIKcrhcEfKyQ3dMKMG', 'HUISSIER', true, 'huissier1@dpri.cd', '+243123456793'),
    ('e09230eb-30bf-47cc-afa4-972772121775', 'contribuable1', '$2a$10$xVqYxMJw6TYwZeoGKsZ1H.4Y5UJAXKXhOD.pIKcrhcEfKyQ3dMKMG', 'CONTRIBUABLE', true, 'contribuable1@dpri.cd', '+243123456794');

-- Création des agents correspondants
INSERT INTO agent (id, nom, sexe, matricule, grade, utilisateur_id) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d476', 'Jean ADMIN', 'M', 'ADM001', 'ADMIN', 'e09230eb-30bf-47cc-afa4-972772121770'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d477', 'Paul RECEVEUR', 'M', 'REC001', 'RECEVEUR', 'e09230eb-30bf-47cc-afa4-972772121771'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d478', 'Marie TAXATEUR', 'F', 'TAX001', 'TAXATEUR', 'e09230eb-30bf-47cc-afa4-972772121772'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d479', 'Pierre OPJ', 'M', 'OPJ001', 'OPJ', 'e09230eb-30bf-47cc-afa4-972772121773'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d480', 'Marc HUISSIER', 'M', 'HUI001', 'HUISSIER', 'e09230eb-30bf-47cc-afa4-972772121774');
