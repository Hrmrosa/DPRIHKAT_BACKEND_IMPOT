-- Requête pour récupérer les détails d'un contribuable avec ses propriétés et véhicules
WITH contribuable_data AS (
    SELECT 
        c.id, 
        c.nom, 
        c.type, 
        c.adresse_principale, 
        c.telephone_principal, 
        c.email
    FROM 
        contribuable c
    WHERE 
        c.id = ?
),
proprietes_data AS (
    SELECT 
        p.id, 
        p.adresse, 
        p.superficie, 
        p.montant_impot
    FROM 
        propriete p
    WHERE 
        p.proprietaire_id = ?
),
vehicules_data AS (
    SELECT 
        v.id, 
        v.immatriculation, 
        v.marque, 
        v.modele, 
        v.annee
    FROM 
        vehicules v
    WHERE 
        v.proprietaire_id = ? OR v.contribuable_id = ?
)
SELECT 
    json_build_object(
        'contribuable', (SELECT row_to_json(c) FROM contribuable_data c),
        'proprietes', (SELECT json_agg(p) FROM proprietes_data p),
        'vehicules', (SELECT json_agg(v) FROM vehicules_data v)
    ) AS details;
