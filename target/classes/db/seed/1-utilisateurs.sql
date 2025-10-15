-- Données de base pour les utilisateurs et agents
INSERT INTO utilisateur (id, username, password, role, actif) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d470', 'admin', '$2a$10$xVqYxMJw6TYwZeoGKsZ1H.4Y5UJAXKXhOD.pIKcrhcEfKyQ3dMKMG', 'ADMIN', true),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d471', 'receveur1', '$2a$10$xVqYxMJw6TYwZeoGKsZ1H.4Y5UJAXKXhOD.pIKcrhcEfKyQ3dMKMG', 'RECEVEUR', true),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d472', 'taxateur1', '$2a$10$xVqYxMJw6TYwZeoGKsZ1H.4Y5UJAXKXhOD.pIKcrhcEfKyQ3dMKMG', 'TAXATEUR', true),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d473', 'opj1', '$2a$10$xVqYxMJw6TYwZeoGKsZ1H.4Y5UJAXKXhOD.pIKcrhcEfKyQ3dMKMG', 'OPJ', true),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d474', 'huissier1', '$2a$10$xVqYxMJw6TYwZeoGKsZ1H.4Y5UJAXKXhOD.pIKcrhcEfKyQ3dMKMG', 'HUISSIER', true),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d475', 'contribuable1', '$2a$10$xVqYxMJw6TYwZeoGKsZ1H.4Y5UJAXKXhOD.pIKcrhcEfKyQ3dMKMG', 'CONTRIBUABLE', true);

INSERT INTO agent (id, nom, sexe, matricule, grade) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d476', 'Jean ADMIN', 'M', 'ADM001', 'ADMIN'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d477', 'Paul RECEVEUR', 'M', 'REC001', 'RECEVEUR'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d478', 'Marie TAXATEUR', 'F', 'TAX001', 'TAXATEUR'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d479', 'Pierre OPJ', 'M', 'OPJ001', 'OPJ'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d480', 'Marc HUISSIER', 'M', 'HUI001', 'HUISSIER');
