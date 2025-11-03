#!/bin/bash

# Script de déploiement automatique pour DPRIHKAT Backend
# Usage: ./deploy.sh [user@]host

set -e  # Arrêter en cas d'erreur

# Couleurs pour les messages
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Configuration
REMOTE_HOST="${1:-user@178.170.94.25}"
APP_DIR="/opt/dprihkat-api"
SERVICE_NAME="dprihkat-api"
JAR_NAME="impots-0.0.1-SNAPSHOT.jar"
LOCAL_JAR="target/${JAR_NAME}"

echo -e "${GREEN}=== Déploiement DPRIHKAT Backend ===${NC}"
echo ""

# Vérifier que le JAR existe
if [ ! -f "$LOCAL_JAR" ]; then
    echo -e "${RED}❌ Erreur: Le fichier $LOCAL_JAR n'existe pas${NC}"
    echo -e "${YELLOW}Exécutez d'abord: mvn clean package -DskipTests${NC}"
    exit 1
fi

echo -e "${GREEN}✓ JAR trouvé: $LOCAL_JAR${NC}"
echo ""

# Afficher la taille du JAR
JAR_SIZE=$(du -h "$LOCAL_JAR" | cut -f1)
echo -e "Taille du JAR: ${YELLOW}$JAR_SIZE${NC}"
echo ""

# Demander confirmation
read -p "Voulez-vous déployer vers $REMOTE_HOST ? (y/N) " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo -e "${YELLOW}Déploiement annulé${NC}"
    exit 0
fi

echo ""
echo -e "${GREEN}=== Étape 1: Transfert du JAR ===${NC}"

# Créer le répertoire temporaire sur le serveur distant
ssh "$REMOTE_HOST" "mkdir -p ~/dprihkat-deploy"

# Transférer le JAR
echo "Transfert de $LOCAL_JAR vers $REMOTE_HOST..."
scp "$LOCAL_JAR" "$REMOTE_HOST:~/dprihkat-deploy/"

echo -e "${GREEN}✓ Transfert terminé${NC}"
echo ""

echo -e "${GREEN}=== Étape 2: Arrêt du service ===${NC}"

# Arrêter le service
ssh "$REMOTE_HOST" "sudo systemctl stop $SERVICE_NAME || true"

echo -e "${GREEN}✓ Service arrêté${NC}"
echo ""

echo -e "${GREEN}=== Étape 3: Sauvegarde de l'ancien JAR ===${NC}"

# Sauvegarder l'ancien JAR
ssh "$REMOTE_HOST" "sudo mv $APP_DIR/$JAR_NAME $APP_DIR/${JAR_NAME}.backup.$(date +%Y%m%d_%H%M%S) 2>/dev/null || true"

echo -e "${GREEN}✓ Sauvegarde effectuée${NC}"
echo ""

echo -e "${GREEN}=== Étape 4: Installation du nouveau JAR ===${NC}"

# Déplacer le nouveau JAR
ssh "$REMOTE_HOST" "sudo mv ~/dprihkat-deploy/$JAR_NAME $APP_DIR/"
ssh "$REMOTE_HOST" "sudo chown dprihkat:dprihkat $APP_DIR/$JAR_NAME"
ssh "$REMOTE_HOST" "sudo chmod 755 $APP_DIR/$JAR_NAME"

echo -e "${GREEN}✓ Nouveau JAR installé${NC}"
echo ""

echo -e "${GREEN}=== Étape 5: Démarrage du service ===${NC}"

# Recharger systemd et démarrer le service
ssh "$REMOTE_HOST" "sudo systemctl daemon-reload"
ssh "$REMOTE_HOST" "sudo systemctl start $SERVICE_NAME"

echo -e "${GREEN}✓ Service démarré${NC}"
echo ""

echo -e "${GREEN}=== Étape 6: Vérification ===${NC}"

# Attendre quelques secondes pour que le service démarre
echo "Attente du démarrage du service (10 secondes)..."
sleep 10

# Vérifier le statut
echo ""
echo "Statut du service:"
ssh "$REMOTE_HOST" "sudo systemctl status $SERVICE_NAME --no-pager -l" || true

echo ""
echo "Dernières lignes des logs:"
ssh "$REMOTE_HOST" "sudo journalctl -u $SERVICE_NAME -n 20 --no-pager"

echo ""
echo -e "${GREEN}=== Déploiement terminé ===${NC}"
echo ""
echo -e "${YELLOW}Commandes utiles:${NC}"
echo -e "  Voir les logs en temps réel:"
echo -e "    ssh $REMOTE_HOST 'sudo journalctl -u $SERVICE_NAME -f'"
echo ""
echo -e "  Redémarrer le service:"
echo -e "    ssh $REMOTE_HOST 'sudo systemctl restart $SERVICE_NAME'"
echo ""
echo -e "  Vérifier le statut:"
echo -e "    ssh $REMOTE_HOST 'sudo systemctl status $SERVICE_NAME'"
echo ""
echo -e "  Tester l'API:"
echo -e "    curl -k https://178.170.94.25.nip.io:8443/api/auth/login -v"
echo ""
