-- Données de test pour les utilisateurs et agents

-- Utilisateurs
INSERT INTO utilisateur (id, username, password, role, actif) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d401', 'admin', '$2a$10$xVqYxMJw6yZGKbHGX8Jzp.eKxoYXXDxKPUym1H0lJoZcAjJZrQj3W', 'ADMIN', true),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d402', 'receveur', '$2a$10$xVqYxMJw6yZGKbHGX8Jzp.eKxoYXXDxKPUym1H0lJoZcAjJZrQj3W', 'RECEVEUR', true),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d403', 'taxateur', '$2a$10$xVqYxMJw6yZGKbHGX8Jzp.eKxoYXXDxKPUym1H0lJoZcAjJZrQj3W', 'TAXATEUR', true),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d404', 'huissier', '$2a$10$xVqYxMJw6yZGKbHGX8Jzp.eKxoYXXDxKPUym1H0lJoZcAjJZrQj3W', 'HUISSIER', true),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d405', 'opj', '$2a$10$xVqYxMJw6yZGKbHGX8Jzp.eKxoYXXDxKPUym1H0lJoZcAjJZrQj3W', 'OPJ', true),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d406', 'contribuable1', '$2a$10$xVqYxMJw6yZGKbHGX8Jzp.eKxoYXXDxKPUym1H0lJoZcAjJZrQj3W', 'CONTRIBUABLE', true),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d407', 'contribuable2', '$2a$10$xVqYxMJw6yZGKbHGX8Jzp.eKxoYXXDxKPUym1H0lJoZcAjJZrQj3W', 'CONTRIBUABLE', true),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d408', 'contribuable3', '$2a$10$xVqYxMJw6yZGKbHGX8Jzp.eKxoYXXDxKPUym1H0lJoZcAjJZrQj3W', 'CONTRIBUABLE', true);

-- Agents
INSERT INTO agent (id, nom, sexe, matricule, grade, utilisateur_id) VALUES
    ('f47ac10b-58cc-4372-a567-0e02b2c3d409', 'Jean ADMIN', 'M', 'ADM001', 'ADMIN', 'f47ac10b-58cc-4372-a567-0e02b2c3d401'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d410', 'Paul RECEVEUR', 'M', 'REC001', 'RECEVEUR', 'f47ac10b-58cc-4372-a567-0e02b2c3d402'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d411', 'Marie TAXATEUR', 'F', 'TAX001', 'TAXATEUR', 'f47ac10b-58cc-4372-a567-0e02b2c3d403'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d412', 'Marc HUISSIER', 'M', 'HUI001', 'HUISSIER', 'f47ac10b-58cc-4372-a567-0e02b2c3d404'),
    ('f47ac10b-58cc-4372-a567-0e02b2c3d413', 'Pierre OPJ', 'M', 'OPJ001', 'OPJ', 'f47ac10b-58cc-4372-a567-0e02b2c3d405');
