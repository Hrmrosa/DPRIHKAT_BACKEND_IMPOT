-- Migration pour ajouter la relation entre Taxation et DemandePlaque
-- Date: 2025-11-01

-- Ajouter la colonne contribuable_direct_id pour les taxations sans déclaration
ALTER TABLE taxation ADD COLUMN IF NOT EXISTS contribuable_direct_id UUID;
ALTER TABLE taxation ADD CONSTRAINT fk_taxation_contribuable_direct 
    FOREIGN KEY (contribuable_direct_id) REFERENCES contribuable(id);

-- Ajouter la colonne demande_plaque_id pour lier les taxations aux demandes de plaques
ALTER TABLE taxation ADD COLUMN IF NOT EXISTS demande_plaque_id UUID;
ALTER TABLE taxation ADD CONSTRAINT fk_taxation_demande_plaque 
    FOREIGN KEY (demande_plaque_id) REFERENCES demandes_plaque(id);

-- Créer un index pour améliorer les performances des recherches
CREATE INDEX IF NOT EXISTS idx_taxation_demande_plaque ON taxation(demande_plaque_id);
CREATE INDEX IF NOT EXISTS idx_taxation_contribuable_direct ON taxation(contribuable_direct_id);

-- Commentaires
COMMENT ON COLUMN taxation.contribuable_direct_id IS 'Contribuable direct pour les taxations sans déclaration (plaques, vignettes)';
COMMENT ON COLUMN taxation.demande_plaque_id IS 'Demande de plaque associée à cette taxation';
