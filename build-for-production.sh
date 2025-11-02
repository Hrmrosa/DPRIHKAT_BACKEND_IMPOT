#!/bin/bash

# Script de build pour la production
# Usage: ./build-for-production.sh

echo "🚀 Build de l'application DPRI Impots pour la production..."
echo ""

# Vérifier que Maven est installé
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven n'est pas installé. Veuillez l'installer d'abord."
    exit 1
fi

# Nettoyer et compiler
echo "📦 Compilation de l'application..."
mvn clean package -DskipTests

# Vérifier si le build a réussi
if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Build réussi!"
    echo ""
    echo "📁 Le fichier JAR est disponible dans: target/impots-0.0.1-SNAPSHOT.jar"
    echo ""
    echo "📤 Pour déployer sur le VPS, exécutez:"
    echo "   scp target/impots-0.0.1-SNAPSHOT.jar user@votre-vps:/home/user/dpri-api/"
    echo ""
    echo "🔄 Puis sur le VPS, redémarrez le service:"
    echo "   sudo systemctl restart dpri-api"
    echo ""
    echo "📖 Consultez DEPLOIEMENT_VPS.md pour plus de détails"
else
    echo ""
    echo "❌ Le build a échoué. Vérifiez les erreurs ci-dessus."
    exit 1
fi
