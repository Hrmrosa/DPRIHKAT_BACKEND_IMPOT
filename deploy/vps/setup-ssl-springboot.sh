#!/bin/bash

# Script de configuration SSL pour Spring Boot (sans Nginx)
# À exécuter sur le VPS: bash setup-ssl-springboot.sh

set -e

echo "🔐 Configuration SSL pour Spring Boot/Tomcat"
echo ""

# Vérifier si on est root
if [ "$EUID" -ne 0 ]; then 
    echo "❌ Ce script doit être exécuté en tant que root (sudo)"
    exit 1
fi

DOMAIN="45.136.70.97.nip.io"
CERT_DIR="/etc/dprihkat-ssl"
KEYSTORE_PATH="${CERT_DIR}/keystore.p12"
KEYSTORE_PASSWORD="dprihkat2025SecurePassword"

# 1. Créer le répertoire pour les certificats
echo "📁 Création du répertoire pour les certificats..."
mkdir -p ${CERT_DIR}

# 2. Générer un certificat auto-signé
echo "🔐 Génération du certificat SSL auto-signé..."
keytool -genkeypair \
  -alias dprihkat \
  -keyalg RSA \
  -keysize 2048 \
  -storetype PKCS12 \
  -keystore ${KEYSTORE_PATH} \
  -validity 365 \
  -storepass ${KEYSTORE_PASSWORD} \
  -keypass ${KEYSTORE_PASSWORD} \
  -dname "CN=${DOMAIN}, OU=DPRIHKAT, O=DPRIHKAT, L=Kinshasa, ST=Kinshasa, C=CD" \
  -ext "SAN=DNS:${DOMAIN},IP:45.136.70.97"

# 3. Donner les permissions appropriées
echo "🔒 Configuration des permissions..."
chown dpri:dpri ${KEYSTORE_PATH}
chmod 600 ${KEYSTORE_PATH}

# 4. Créer/Mettre à jour le fichier de configuration
echo "📝 Mise à jour de la configuration Spring Boot..."

ENV_FILE="/etc/dpri-impots/env"

# Sauvegarder l'ancien fichier
if [ -f "${ENV_FILE}" ]; then
    cp ${ENV_FILE} ${ENV_FILE}.backup
    echo "💾 Sauvegarde créée: ${ENV_FILE}.backup"
fi

# Supprimer les anciennes configurations SSL si elles existent
sed -i '/^SERVER_SSL_/d' ${ENV_FILE} 2>/dev/null || true
sed -i '/^SERVER_PORT=/d' ${ENV_FILE} 2>/dev/null || true

# Ajouter les nouvelles configurations SSL
cat >> ${ENV_FILE} <<EOF

# Configuration SSL
SERVER_PORT=8443
SERVER_SSL_ENABLED=true
SERVER_SSL_KEY_STORE=${KEYSTORE_PATH}
SERVER_SSL_KEY_STORE_PASSWORD=${KEYSTORE_PASSWORD}
SERVER_SSL_KEY_STORE_TYPE=PKCS12
SERVER_SSL_KEY_ALIAS=dprihkat
EOF

echo "✅ Configuration ajoutée à ${ENV_FILE}"

# 5. Configurer le pare-feu
echo "🔥 Configuration du pare-feu..."
if command -v ufw &> /dev/null; then
    ufw allow 8443/tcp
    echo "✅ Port 8443 ouvert dans UFW"
fi

# 6. Redémarrer le service
echo "🔄 Redémarrage du service dpri-impots..."
systemctl restart dpri-impots

# Attendre que le service démarre
echo "⏳ Attente du démarrage du service (30 secondes)..."
sleep 30

# 7. Vérifier le statut
echo ""
echo "📊 Vérifications:"
echo ""

# Vérifier le service
if systemctl is-active --quiet dpri-impots; then
    echo "✅ Service dpri-impots est actif"
else
    echo "❌ Service dpri-impots n'est pas actif"
    echo "   Voir les logs: sudo journalctl -u dpri-impots -n 50"
fi

# Vérifier le port
sleep 5
if netstat -tlnp | grep -q ":8443.*java"; then
    echo "✅ Java écoute sur le port 8443"
else
    echo "⚠️  Java ne semble pas écouter sur le port 8443"
    echo "   Vérifiez les logs: sudo journalctl -u dpri-impots -n 50"
fi

# Test de connexion
echo ""
echo "🧪 Test de connexion locale..."
if curl -k -s -o /dev/null -w "%{http_code}" https://localhost:8443/api/auth/login | grep -q "40[01]"; then
    echo "✅ Le serveur répond localement sur HTTPS"
else
    echo "⚠️  Le serveur ne répond pas encore"
    echo "   Attendez quelques secondes et testez manuellement:"
    echo "   curl -k https://localhost:8443/api/auth/login -v"
fi

echo ""
echo "✅ Configuration SSL terminée!"
echo ""
echo "📝 Informations importantes:"
echo "   - Keystore: ${KEYSTORE_PATH}"
echo "   - Password: ${KEYSTORE_PASSWORD}"
echo "   - Port: 8443"
echo "   - Type: PKCS12"
echo ""
echo "🧪 Tests à effectuer:"
echo ""
echo "1. Test local sur le VPS:"
echo "   curl -k https://localhost:8443/api/auth/login -v"
echo ""
echo "2. Test depuis votre machine:"
echo "   curl -k https://45.136.70.97.nip.io:8443/api/auth/login -v"
echo ""
echo "3. Test CORS:"
echo "   curl -X OPTIONS https://45.136.70.97.nip.io:8443/api/auth/login \\"
echo "     -H \"Origin: https://dpri-impot-frontend.vercel.app\" \\"
echo "     -H \"Access-Control-Request-Method: POST\" \\"
echo "     -k -v"
echo ""
echo "📋 Logs utiles:"
echo "   sudo journalctl -u dpri-impots -f"
echo "   sudo journalctl -u dpri-impots -n 100 --no-pager"
echo ""
echo "⚠️  Note: Le certificat est auto-signé. Pour un certificat valide,"
echo "   utilisez Let's Encrypt (voir documentation)."
echo ""
