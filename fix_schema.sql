-- Vérifier si la colonne type_agent existe dans la table agent
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'agent' AND column_name = 'type_agent') THEN
        ALTER TABLE agent ADD COLUMN type_agent VARCHAR(31) DEFAULT 'STANDARD';
    END IF;
END $$;

-- Vérifier si la colonne actif existe dans la table contribuable
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'contribuable' AND column_name = 'actif') THEN
        ALTER TABLE contribuable ADD COLUMN actif BOOLEAN DEFAULT TRUE;
    END IF;
END $$;

-- Vérifier si la colonne agent_id existe dans la table contribuable
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'contribuable' AND column_name = 'agent_id') THEN
        -- Cette colonne est une clé étrangère, donc nous devons être prudents
        -- Nous allons d'abord créer la colonne sans contrainte
        ALTER TABLE contribuable ADD COLUMN agent_id UUID;
        
        -- Ensuite, nous allons mettre à jour les valeurs existantes
        -- Cela suppose que la table agent a au moins une entrée
        UPDATE contribuable SET agent_id = (SELECT id FROM agent LIMIT 1) WHERE agent_id IS NULL;
        
        -- Enfin, nous ajoutons la contrainte de clé étrangère
        ALTER TABLE contribuable ADD CONSTRAINT fk_contribuable_agent FOREIGN KEY (agent_id) REFERENCES agent(id);
    END IF;
END $$;

-- Vérifier si la colonne actif existe dans la table declaration
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'declaration' AND column_name = 'actif') THEN
        ALTER TABLE declaration ADD COLUMN actif BOOLEAN DEFAULT TRUE;
    END IF;
END $$;
