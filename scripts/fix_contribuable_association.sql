-- Script pour corriger l'association entre utilisateurs et contribuables
-- Ce script identifie les utilisateurs avec le rôle CONTRIBUABLE mais qui ont un agent au lieu d'un contribuable associé
-- et corrige cette association

-- 1. Identifier les utilisateurs avec le rôle CONTRIBUABLE
SELECT id, login FROM utilisateur WHERE role = 'CONTRIBUABLE';

-- 2. Vérifier les utilisateurs qui ont un agent_id mais pas de contribuable_id
SELECT u.id, u.login, u.agent_id, u.contribuable_id 
FROM utilisateur u 
WHERE u.role = 'CONTRIBUABLE' AND u.agent_id IS NOT NULL AND u.contribuable_id IS NULL;

-- 3. Correction: Pour chaque utilisateur contribuable, mettre à jour l'association
-- Cette requête suppose que l'agent_id pointe vers un contribuable
UPDATE utilisateur u
SET contribuable_id = u.agent_id, agent_id = NULL
WHERE u.role = 'CONTRIBUABLE' AND u.agent_id IS NOT NULL AND u.contribuable_id IS NULL;

-- 4. Vérifier que la correction a été appliquée
SELECT u.id, u.login, u.agent_id, u.contribuable_id 
FROM utilisateur u 
WHERE u.role = 'CONTRIBUABLE';

-- 5. Vérifier que tous les utilisateurs contribuables ont maintenant un contribuable associé
SELECT COUNT(*) 
FROM utilisateur u 
WHERE u.role = 'CONTRIBUABLE' AND u.contribuable_id IS NULL;
