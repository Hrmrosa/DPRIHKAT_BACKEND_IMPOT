-- Enable PostGIS extension on the target database
-- This migration is idempotent and safe to run multiple times
DO $$
BEGIN
    -- Enable PostGIS only if the extension is available on the server
    IF EXISTS (SELECT 1 FROM pg_available_extensions WHERE name = 'postgis') THEN
        EXECUTE 'CREATE EXTENSION IF NOT EXISTS postgis';
        RAISE NOTICE 'PostGIS extension ensured (created if missing).';
    ELSE
        RAISE NOTICE 'PostGIS extension not available on this server; skipping activation.';
    END IF;
END
$$;

-- Optional: enable other PostGIS-related extensions if available
-- DO $$
-- BEGIN
--     IF EXISTS (SELECT 1 FROM pg_available_extensions WHERE name = 'postgis_topology') THEN
--         EXECUTE 'CREATE EXTENSION IF NOT EXISTS postgis_topology';
--     END IF;
--     IF EXISTS (SELECT 1 FROM pg_available_extensions WHERE name = 'postgis_raster') THEN
--         EXECUTE 'CREATE EXTENSION IF NOT EXISTS postgis_raster';
--     END IF;
-- END
-- $$;
