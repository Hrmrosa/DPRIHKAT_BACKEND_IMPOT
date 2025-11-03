#!/bin/bash

# Script pour corriger le fichier systemd et activer SSL
# À exécuter sur le VPS: bash fix-systemd-ssl.sh

set -e

echo "🔧 Correction du fichier systemd pour SSL"
echo ""

# Vérifier si on est root
if [ "$EUID" -ne 0 ]; then 
    echo "❌ Ce script doit être exécuté en tant que root (sudo)"
    exit 1
fi

# 1. Sauvegarder l'ancien fichier
echo "💾 Sauvegarde de l'ancien fichier systemd..."
cp /etc/systemd/system/dpri-impots.service /etc/systemd/system/dpri-impots.service.backup

# 2. Créer le nouveau fichier systemd
echo "📝 Création du nouveau fichier systemd..."
cat > /etc/systemd/system/dpri-impots.service <<'EOF'
[Unit]
Description=DPRI Impots Spring Boot API
After=network.target postgresql.service

[Service]
User=dpri
Group=dpri
EnvironmentFile=/etc/dpri-impots/env
WorkingDirectory=/opt/dpri-impots

# Laisser Spring Boot lire ses propres propriétés depuis application.properties et les variables d'environnement
ExecStart=/usr/bin/java -XX:+UseG1GC -Xms256m -Xmx512m -Duser.timezone=UTC -jar /opt/dpri-impots/app.jar

SuccessExitStatus=143
Restart=always
RestartSec=10
StandardOutput=journal
StandardError=journal
SyslogIdentifier=dpri-impots
LimitNOFILE=65535

[Install]
WantedBy=multi-user.target
EOF

echo "✅ Nouveau fichier systemd créé"

# 3. Recharger systemd
echo "🔄 Rechargement de systemd..."
systemctl daemon-reload

# 4. Redémarrer le service
echo "🔄 Redémarrage du service dpri-impots..."
systemctl restart dpri-impots

# 5. Attendre le démarrage
echo "⏳ Attente du démarrage (15 secondes)..."
sleep 15

# 6. Vérifier le statut
echo ""
echo "📊 Vérifications:"
echo ""

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
    netstat -tlnp | grep ":8443.*java"
else
    echo "⚠️  Java ne semble pas écouter sur le port 8443"
    echo "   Ports actuels:"
    netstat -tlnp | grep java || echo "   Aucun processus Java trouvé"
fi

echo ""
echo "🧪 Test de connexion locale..."
sleep 2
if curl -k -s -o /dev/null -w "%{http_code}" https://localhost:8443/api/auth/login 2>/dev/null | grep -q "40[01]"; then
    echo "✅ Le serveur répond localement sur HTTPS:8443"
else
    echo "⚠️  Le serveur ne répond pas encore sur HTTPS:8443"
    echo ""
    echo "📋 Voir les logs pour diagnostiquer:"
    echo "   sudo journalctl -u dpri-impots -n 50"
fi

echo ""
echo "✅ Configuration terminée!"
echo ""
echo "📝 Commandes utiles:"
echo "   sudo journalctl -u dpri-impots -f          # Logs en temps réel"
echo "   sudo systemctl status dpri-impots          # Statut du service"
echo "   sudo netstat -tlnp | grep java             # Ports Java"
echo ""
