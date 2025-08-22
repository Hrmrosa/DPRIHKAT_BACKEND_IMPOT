-- Enable PostGIS extension on the target database
-- This migration is idempotent and safe to run multiple times
CREATE EXTENSION IF NOT EXISTS postgis;

-- Optional extensions if your app needs them
-- CREATE EXTENSION IF NOT EXISTS postgis_topology;
-- CREATE EXTENSION IF NOT EXISTS postgis_raster;
