DO $$ 
DECLARE
    type_name text;
    type_names text[] := ARRAY[
        'sexe', 'role', 'type_contribuable', 'type_impot', 
        'statut_declaration', 'mode_paiement', 'statut_paiement',
        'type_document', 'statut_document', 'type_relance',
        'statut_relance', 'usage_propriete', 'type_vehicule'
    ];
BEGIN
    FOREACH type_name IN ARRAY type_names LOOP
        IF EXISTS (SELECT 1 FROM pg_type WHERE typname = type_name) THEN
            EXECUTE format('DROP TYPE IF EXISTS %I CASCADE', type_name);
        END IF;
    END LOOP;
END $$;
