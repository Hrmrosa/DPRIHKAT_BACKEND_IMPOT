-- Données de test pour les utilisateurs (mise à jour avec les nouveaux champs)

-- Mise à jour des utilisateurs existants
UPDATE utilisateur SET 
    nom_complet = 'Jean ADMIN',
    sexe = 'M',
    email = 'jean.admin@dgi.cd',
    adresse = '123 Avenue Administration, Kinshasa',
    telephone = '+243111111111'
WHERE id = 'f47ac10b-58cc-4372-a567-0e02b2c3d401';

UPDATE utilisateur SET 
    nom_complet = 'Paul RECEVEUR',
    sexe = 'M',
    email = 'paul.receveur@dgi.cd',
    adresse = '456 Avenue Recettes, Kinshasa',
    telephone = '+243222222222'
WHERE id = 'f47ac10b-58cc-4372-a567-0e02b2c3d402';

UPDATE utilisateur SET 
    nom_complet = 'Marie TAXATEUR',
    sexe = 'F',
    email = 'marie.taxateur@dgi.cd',
    adresse = '789 Avenue Taxation, Kinshasa',
    telephone = '+243333333333'
WHERE id = 'f47ac10b-58cc-4372-a567-0e02b2c3d403';

UPDATE utilisateur SET 
    nom_complet = 'Marc HUISSIER',
    sexe = 'M',
    email = 'marc.huissier@dgi.cd',
    adresse = '321 Avenue Justice, Kinshasa',
    telephone = '+243444444444'
WHERE id = 'f47ac10b-58cc-4372-a567-0e02b2c3d404';

UPDATE utilisateur SET 
    nom_complet = 'Pierre OPJ',
    sexe = 'M',
    email = 'pierre.opj@dgi.cd',
    adresse = '654 Avenue Police, Kinshasa',
    telephone = '+243555555555'
WHERE id = 'f47ac10b-58cc-4372-a567-0e02b2c3d405';

-- Mise à jour des contribuables
UPDATE utilisateur SET 
    nom_complet = 'SARL CONGOLAISE DE COMMERCE',
    email = 'contact@sarl-cc.cd',
    adresse = '123 Avenue du Commerce, Kinshasa',
    telephone = '+243123456789'
WHERE id = 'f47ac10b-58cc-4372-a567-0e02b2c3d406';

UPDATE utilisateur SET 
    nom_complet = 'Établissements KALALA',
    email = 'ets.kalala@gmail.com',
    adresse = '45 Avenue du Marché, Lubumbashi',
    telephone = '+243987654321'
WHERE id = 'f47ac10b-58cc-4372-a567-0e02b2c3d407';

UPDATE utilisateur SET 
    nom_complet = 'Jean MUTOMBO',
    sexe = 'M',
    email = 'jean.mutombo@gmail.com',
    adresse = '78 Rue des Artisans, Kinshasa',
    telephone = '+243555555555'
WHERE id = 'f47ac10b-58cc-4372-a567-0e02b2c3d408';
