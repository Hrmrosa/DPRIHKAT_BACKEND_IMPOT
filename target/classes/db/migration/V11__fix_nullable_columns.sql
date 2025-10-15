-- Mettre à jour la colonne type_agent dans la table agent
UPDATE agent SET type_agent = 'STANDARD' WHERE type_agent IS NULL;

-- Mettre à jour la colonne actif dans la table contribuable
UPDATE contribuable SET actif = true WHERE actif IS NULL;

-- Mettre à jour la colonne agent_id dans la table contribuable
-- Utiliser le premier agent disponible
UPDATE contribuable SET agent_id = (SELECT id FROM agent LIMIT 1) WHERE agent_id IS NULL;

-- Mettre à jour la colonne actif dans la table declaration
UPDATE declaration SET actif = true WHERE actif IS NULL;

-- Ajouter les contraintes NOT NULL après avoir mis à jour les données
ALTER TABLE agent ALTER COLUMN type_agent SET NOT NULL;
ALTER TABLE contribuable ALTER COLUMN actif SET NOT NULL;
ALTER TABLE contribuable ALTER COLUMN agent_id SET NOT NULL;
ALTER TABLE declaration ALTER COLUMN actif SET NOT NULL;
