#!/bin/bash

# Script de configuration rapide Nginx + SSL auto-signé pour DPRIHKAT API
# À exécuter sur le VPS: bash setup-nginx-quick.sh

set -e

echo "🚀 Configuration Nginx + SSL pour DPRIHKAT API"
echo ""

# Vérifier si on est root
if [ "$EUID" -ne 0 ]; then 
    echo "❌ Ce script doit être exécuté en tant que root (sudo)"
    exit 1
fi

# 0. Nettoyer les dépôts problématiques avant l'installation
echo "🧹 Nettoyage des dépôts APT problématiques..."
if [ -f /etc/apt/sources.list.d/adoptopenjdk.list ]; then
    rm -f /etc/apt/sources.list.d/adoptopenjdk.list
fi

# Désactiver temporairement les dépôts externes problématiques
if [ -f /etc/apt/sources.list.d/adoptium.list ]; then
    mv /etc/apt/sources.list.d/adoptium.list /etc/apt/sources.list.d/adoptium.list.backup
fi

# 1. Installer Nginx
echo "📦 Installation de Nginx..."
apt update --allow-insecure-repositories || apt update --allow-unauthenticated || apt update
apt install -y nginx

# 2. Créer le certificat auto-signé
echo "🔐 Création du certificat SSL auto-signé..."
mkdir -p /etc/nginx/ssl
openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
  -keyout /etc/nginx/ssl/dprihkat.key \
  -out /etc/nginx/ssl/dprihkat.crt \
  -subj "/C=CD/ST=Kinshasa/L=Kinshasa/O=DPRIHKAT/CN=45.136.70.97.nip.io"

# 3. Créer la configuration Nginx
echo "📝 Création de la configuration Nginx..."
cat > /etc/nginx/sites-available/dprihkat-api <<'EOF'
# Configuration Nginx pour DPRIHKAT API

server {
    listen 8443 ssl http2;
    server_name 45.136.70.97.nip.io;

    # Certificat auto-signé
    ssl_certificate /etc/nginx/ssl/dprihkat.crt;
    ssl_certificate_key /etc/nginx/ssl/dprihkat.key;

    # Configuration SSL moderne
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    ssl_prefer_server_ciphers on;
    ssl_session_cache shared:SSL:10m;
    ssl_session_timeout 10m;

    # Logs
    access_log /var/log/nginx/dprihkat-api-access.log;
    error_log /var/log/nginx/dprihkat-api-error.log;

    # Proxy vers le backend Spring Boot sur port 8080
    location /api/ {
        proxy_pass http://127.0.0.1:8080/api/;

        # Headers pour le proxy
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header X-Forwarded-Host $host;
        proxy_set_header X-Forwarded-Port $server_port;

        # Timeouts
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;

        # Buffers
        proxy_buffering on;
        proxy_buffer_size 4k;
        proxy_buffers 8 4k;
        proxy_busy_buffers_size 8k;
    }

    # WebSocket support
    location /ws/ {
        proxy_pass http://127.0.0.1:8080/ws/;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}

# Redirection HTTP vers HTTPS
server {
    listen 80;
    server_name 45.136.70.97.nip.io;
    return 301 https://$server_name:8443$request_uri;
}
EOF

# 4. Activer le site
echo "✅ Activation du site..."
ln -sf /etc/nginx/sites-available/dprihkat-api /etc/nginx/sites-enabled/

# 5. Supprimer le site par défaut
rm -f /etc/nginx/sites-enabled/default

# 6. Tester la configuration
echo "🧪 Test de la configuration Nginx..."
nginx -t

# 7. Redémarrer Nginx
echo "🔄 Redémarrage de Nginx..."
systemctl restart nginx
systemctl enable nginx

# 8. Configurer le pare-feu
echo "🔥 Configuration du pare-feu..."
if command -v ufw &> /dev/null; then
    ufw allow 80/tcp
    ufw allow 8443/tcp
    ufw status
fi

# 9. Restaurer les fichiers de dépôts si nécessaire
if [ -f /etc/apt/sources.list.d/adoptium.list.backup ]; then
    mv /etc/apt/sources.list.d/adoptium.list.backup /etc/apt/sources.list.d/adoptium.list
    echo "📦 Dépôts Adoptium restaurés"
fi

# 10. Vérifier que tout fonctionne
echo ""
echo "✅ Configuration terminée!"
echo ""
echo "📊 Vérifications:"
echo ""

# Vérifier Nginx
if systemctl is-active --quiet nginx; then
    echo "✅ Nginx est actif"
else
    echo "❌ Nginx n'est pas actif"
fi

# Vérifier le port 8443
if netstat -tlnp | grep -q ":8443"; then
    echo "✅ Nginx écoute sur le port 8443"
else
    echo "❌ Nginx n'écoute pas sur le port 8443"
fi

# Vérifier le backend
if netstat -tlnp | grep -q ":8080.*java"; then
    echo "✅ Backend écoute sur le port 8080"
else
    echo "⚠️  Backend ne semble pas écouter sur le port 8080"
    echo "   Vérifiez: sudo journalctl -u dpri-impots -n 50"
fi

echo ""
echo "🧪 Test de connexion:"
echo ""

# Test local
if curl -k -s -o /dev/null -w "%{http_code}" https://localhost:8443/api/auth/login | grep -q "40[01]"; then
    echo "✅ Le serveur répond localement"
else
    echo "❌ Le serveur ne répond pas localement"
fi

echo ""
echo "📝 Prochaines étapes:"
echo ""
echo "1. Testez depuis votre machine:"
echo "   curl -k https://45.136.70.97.nip.io:8443/api/auth/login -v"
echo ""
echo "2. Testez depuis le frontend (le certificat auto-signé générera un avertissement)"
echo ""
echo "3. Pour un certificat valide, utilisez Let's Encrypt:"
echo "   sudo apt install certbot python3-certbot-nginx"
echo "   sudo certbot --nginx -d 45.136.70.97.nip.io"
echo ""
echo "📋 Logs utiles:"
echo "   sudo tail -f /var/log/nginx/dprihkat-api-access.log"
echo "   sudo tail -f /var/log/nginx/dprihkat-api-error.log"
echo "   sudo journalctl -u dpri-impots -f"
echo ""