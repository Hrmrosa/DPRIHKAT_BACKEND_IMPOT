import json
from pathlib import Path

def convert_entry(old_entry):
    properties = old_entry.get('properties', {})
    geometry = old_entry.get('geometry', {})
    
    # Extraire les coordonnées
    longitude, latitude = None, None
    if geometry and 'coordinates' in geometry:
        longitude, latitude = geometry['coordinates'][:2]
    
    # Créer la nouvelle entrée
    new_entry = {
        "contribuable": properties.get("Saisir le nom complet du contribuable", "Inconnu"),
        "biens": {
            "Vi": int(properties.get("Nombre des Vi", 0) or 0),
            "AP": int(properties.get("Nombre des AP", 0) or 0),
            "AT": int(properties.get("Nombre des AT", 0) or 0),
            "batiments": 0,  # À compléter selon vos besoins
            "entrepots": 0,
            "depots": 0,
            "citernes": int(properties.get("Nombre des citernes", 0) or 0),
            "angars": 0,
            "chantiers": 0
        },
        "localisation": {
            "latitude": latitude,
            "longitude": longitude
        },
        "adresse": {
            "commune": properties.get("Choisir la commune de résidence ", "Lubumbashi"),
            "quartier": properties.get("Quartier", ""),
            "avenue": properties.get("Avenue", ""),
            "numero_parcelle": properties.get("Le numéro de la parcelle ", "")
        }
    }
    return new_entry

def main():
    # Chemins des fichiers
    project_root = Path(__file__).parent.parent
    old_path = project_root / "src" / "main" / "resources" / "datas.json"
    new_path = project_root / "src" / "main" / "resources" / "new_datas.json"
    
    # Lire l'ancien fichier
    with open(old_path, 'r', encoding='utf-8') as f:
        data = json.load(f)
    
    # Convertir toutes les entrées
    new_data = []
    if data.get('type') == 'FeatureCollection':
        new_data = [convert_entry(feature) for feature in data.get('features', [])]
    
    # Sauvegarder le nouveau fichier
    with open(new_path, 'w', encoding='utf-8') as f:
        json.dump(new_data, f, indent=2, ensure_ascii=False)
    
    print(f"Conversion terminée. {len(new_data)} contribuables migrés.")

if __name__ == "__main__":
    main()
