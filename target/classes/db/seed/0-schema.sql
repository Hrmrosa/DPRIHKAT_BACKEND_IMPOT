-- Création des types enum avec vérification
DO $$ 
BEGIN
    -- Création des types enum s'ils n'existent pas
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'sexe') THEN
        CREATE TYPE sexe AS ENUM ('M', 'F');
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'role') THEN
        CREATE TYPE role AS ENUM ('ADMIN', 'RECEVEUR', 'TAXATEUR', 'OPJ', 'HUISSIER', 'CONTRIBUABLE');
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'type_contribuable') THEN
        CREATE TYPE type_contribuable AS ENUM ('PERSONNE_MORALE', 'PERSONNE_PHYSIQUE');
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'type_impot') THEN
        CREATE TYPE type_impot AS ENUM ('IBP', 'IPR', 'TVA', 'VIGNETTE', 'IMPOT_FONCIER');
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'statut_declaration') THEN
        CREATE TYPE statut_declaration AS ENUM ('EN_ATTENTE', 'VALIDEE', 'REJETEE');
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'mode_paiement') THEN
        CREATE TYPE mode_paiement AS ENUM ('ESPECES', 'CHEQUE', 'VIREMENT', 'MOBILE_MONEY');
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'statut_paiement') THEN
        CREATE TYPE statut_paiement AS ENUM ('EN_ATTENTE', 'VALIDE', 'REJETE');
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'type_document') THEN
        CREATE TYPE type_document AS ENUM ('AMR', 'MED', 'CONTRAINTE', 'COMMANDEMENT', 'ATD', 'SAISIE_MOBILIERE', 'SAISIE_IMMOBILIERE', 'FERMETURE_ETABLISSEMENT');
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'statut_document') THEN
        CREATE TYPE statut_document AS ENUM ('GENERE', 'NOTIFIE', 'EN_COURS_EXECUTION', 'EXECUTE', 'NON_EXECUTE', 'ANNULE');
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'type_relance') THEN
        CREATE TYPE type_relance AS ENUM ('COURRIER', 'EMAIL', 'SMS', 'TELEPHONE');
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'statut_relance') THEN
        CREATE TYPE statut_relance AS ENUM ('PROGRAMMEE', 'ENVOYEE', 'ECHEC');
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'usage_propriete') THEN
        CREATE TYPE usage_propriete AS ENUM ('COMMERCIAL', 'RESIDENTIEL', 'INDUSTRIEL');
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'type_vehicule') THEN
        CREATE TYPE type_vehicule AS ENUM ('PARTICULIER', 'UTILITAIRE', 'POIDS_LOURD');
    END IF;
END $$;

-- Création des tables avec vérification
CREATE TABLE IF NOT EXISTS utilisateur (
    -- [reste du script inchangé...]
