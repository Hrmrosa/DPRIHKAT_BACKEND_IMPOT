-- Modifier les colonnes pour les rendre nullables dans un premier temps
ALTER TABLE agent ALTER COLUMN type_agent DROP NOT NULL;
ALTER TABLE contribuable ALTER COLUMN actif DROP NOT NULL;
ALTER TABLE contribuable ALTER COLUMN agent_id DROP NOT NULL;
ALTER TABLE declaration ALTER COLUMN actif DROP NOT NULL;

-- Ajouter des contraintes CHECK pour s'assurer que les nouvelles entrées respectent les règles
ALTER TABLE agent ADD CONSTRAINT chk_agent_type_agent CHECK (type_agent IS NOT NULL);
ALTER TABLE contribuable ADD CONSTRAINT chk_contribuable_actif CHECK (actif IS NOT NULL);
ALTER TABLE contribuable ADD CONSTRAINT chk_contribuable_agent_id CHECK (agent_id IS NOT NULL);
ALTER TABLE declaration ADD CONSTRAINT chk_declaration_actif CHECK (actif IS NOT NULL);

-- Créer des triggers pour définir des valeurs par défaut lors de l'insertion
CREATE OR REPLACE FUNCTION set_default_type_agent()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.type_agent IS NULL THEN
        NEW.type_agent = 'STANDARD';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_set_default_type_agent
BEFORE INSERT ON agent
FOR EACH ROW
EXECUTE FUNCTION set_default_type_agent();

CREATE OR REPLACE FUNCTION set_default_actif_contribuable()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.actif IS NULL THEN
        NEW.actif = TRUE;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_set_default_actif_contribuable
BEFORE INSERT ON contribuable
FOR EACH ROW
EXECUTE FUNCTION set_default_actif_contribuable();

CREATE OR REPLACE FUNCTION set_default_actif_declaration()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.actif IS NULL THEN
        NEW.actif = TRUE;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_set_default_actif_declaration
BEFORE INSERT ON declaration
FOR EACH ROW
EXECUTE FUNCTION set_default_actif_declaration();
