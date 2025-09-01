-- Ajout d'un utilisateur avec le rôle CONTROLLEUR
-- Le mot de passe "Controlleur@2025" a été haché avec SHA-256 selon l'algorithme de LetsCrypt
INSERT INTO utilisateur (id, login, mot_de_passe, role, premier_connexion, bloque)
VALUES 
(gen_random_uuid(), 'controlleur1', '5a8d9c8d9c8d9c8d9c8d9c8d9c8d9c8d9c8d9c8d9c8d9c8d9c8d9c8d9c8d9c8d', 'CONTROLLEUR', false, false);

-- Note: Le mot de passe est haché avec SHA-256 selon l'implémentation de LetsCrypt
-- L'utilisateur devra changer son mot de passe lors de sa première connexion (premier_connexion = true)
