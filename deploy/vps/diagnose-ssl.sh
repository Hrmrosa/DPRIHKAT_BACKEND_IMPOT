#!/bin/bash

# Script de diagnostic SSL pour DPRIHKAT
# Usage: bash diagnose-ssl.sh

echo "🔍 Diagnostic SSL DPRIHKAT"
echo "=========================="
echo ""

echo "1️⃣ Statut du service dpri-impots:"
echo "-----------------------------------"
systemctl status dpri-impots --no-pager -l | head -20
echo ""

echo "2️⃣ Dernières erreurs dans les logs:"
echo "------------------------------------"
journalctl -u dpri-impots -n 50 --no-pager | grep -i -E "error|exception|failed|refused" | tail -20
echo ""

echo "3️⃣ Configuration SSL dans /etc/dpri-impots/env:"
echo "------------------------------------------------"
grep -E "SERVER_PORT|SERVER_SSL" /etc/dpri-impots/env 2>&1
echo ""

echo "4️⃣ Vérification du keystore:"
echo "-----------------------------"
if [ -f /etc/dprihkat-ssl/keystore.p12 ]; then
    echo "✅ Keystore existe: /etc/dprihkat-ssl/keystore.p12"
    ls -lh /etc/dprihkat-ssl/keystore.p12
    echo ""
    echo "Propriétaire et permissions:"
    stat -c "Owner: %U:%G, Permissions: %a" /etc/dprihkat-ssl/keystore.p12
else
    echo "❌ Keystore introuvable: /etc/dprihkat-ssl/keystore.p12"
fi
echo ""

echo "5️⃣ Ports en écoute:"
echo "-------------------"
echo "Port 8080 (HTTP):"
netstat -tlnp | grep 8080 || echo "  ❌ Aucun processus n'écoute sur le port 8080"
echo ""
echo "Port 8443 (HTTPS):"
netstat -tlnp | grep 8443 || echo "  ❌ Aucun processus n'écoute sur le port 8443"
echo ""

echo "6️⃣ Processus Java:"
echo "------------------"
ps aux | grep java | grep -v grep || echo "❌ Aucun processus Java en cours"
echo ""

echo "7️⃣ Pare-feu UFW:"
echo "----------------"
ufw status | grep -E "8080|8443"
echo ""

echo "8️⃣ Dernières 20 lignes des logs complets:"
echo "-----------------------------------------"
journalctl -u dpri-impots -n 20 --no-pager
echo ""

echo "=========================="
echo "🔍 Diagnostic terminé"
echo ""
echo "📋 Actions recommandées:"
echo ""
echo "Pour voir les logs complets:"
echo "  sudo journalctl -u dpri-impots -n 100 --no-pager"
echo ""
echo "Pour voir les logs en temps réel:"
echo "  sudo journalctl -u dpri-impots -f"
echo ""
echo "Pour redémarrer le service:"
echo "  sudo systemctl restart dpri-impots"
echo ""
