#!/bin/bash

# Script de test de connectivité pour le backend DPRIHKAT
# Usage: ./test-connection.sh

set -e

# Couleurs
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Configuration
BACKEND_HOST="178.170.94.25"
BACKEND_DOMAIN="178.170.94.25.nip.io"
BACKEND_PORT="8443"
BACKEND_URL="https://${BACKEND_DOMAIN}:${BACKEND_PORT}"
API_ENDPOINT="${BACKEND_URL}/api/auth/login"

echo -e "${BLUE}=== Test de connectivité DPRIHKAT Backend ===${NC}"
echo ""
echo -e "Host: ${YELLOW}${BACKEND_HOST}${NC}"
echo -e "Domain: ${YELLOW}${BACKEND_DOMAIN}${NC}"
echo -e "Port: ${YELLOW}${BACKEND_PORT}${NC}"
echo -e "URL: ${YELLOW}${BACKEND_URL}${NC}"
echo ""

# Test 1: Ping
echo -e "${BLUE}=== Test 1: Ping ===${NC}"
if ping -c 3 "$BACKEND_HOST" > /dev/null 2>&1; then
    echo -e "${GREEN}✓ Le serveur répond au ping${NC}"
else
    echo -e "${RED}✗ Le serveur ne répond pas au ping${NC}"
fi
echo ""

# Test 2: Port ouvert (telnet/nc)
echo -e "${BLUE}=== Test 2: Port ${BACKEND_PORT} ===${NC}"
if command -v nc > /dev/null 2>&1; then
    if nc -zv -w 5 "$BACKEND_HOST" "$BACKEND_PORT" 2>&1 | grep -q "succeeded\|open"; then
        echo -e "${GREEN}✓ Le port ${BACKEND_PORT} est ouvert${NC}"
    else
        echo -e "${RED}✗ Le port ${BACKEND_PORT} est fermé ou inaccessible${NC}"
        echo -e "${YELLOW}  Vérifiez le pare-feu et que le service est démarré${NC}"
    fi
else
    echo -e "${YELLOW}⚠ nc (netcat) n'est pas installé, test ignoré${NC}"
fi
echo ""

# Test 3: Certificat SSL
echo -e "${BLUE}=== Test 3: Certificat SSL ===${NC}"
if command -v openssl > /dev/null 2>&1; then
    SSL_OUTPUT=$(timeout 5 openssl s_client -connect "${BACKEND_HOST}:${BACKEND_PORT}" -servername "$BACKEND_DOMAIN" </dev/null 2>&1 || true)
    
    if echo "$SSL_OUTPUT" | grep -q "Verify return code: 0"; then
        echo -e "${GREEN}✓ Certificat SSL valide${NC}"
    elif echo "$SSL_OUTPUT" | grep -q "self signed certificate"; then
        echo -e "${YELLOW}⚠ Certificat auto-signé détecté${NC}"
        echo -e "${YELLOW}  Les navigateurs modernes bloqueront ce certificat${NC}"
        echo -e "${YELLOW}  Recommandation: Utilisez Let's Encrypt${NC}"
    elif echo "$SSL_OUTPUT" | grep -q "certificate verify failed"; then
        echo -e "${YELLOW}⚠ Problème de vérification du certificat${NC}"
    else
        echo -e "${RED}✗ Impossible de se connecter via SSL${NC}"
        echo -e "${YELLOW}  Le service n'est peut-être pas démarré${NC}"
    fi
else
    echo -e "${YELLOW}⚠ openssl n'est pas installé, test ignoré${NC}"
fi
echo ""

# Test 4: Requête HTTP OPTIONS (CORS preflight)
echo -e "${BLUE}=== Test 4: CORS Preflight (OPTIONS) ===${NC}"
if command -v curl > /dev/null 2>&1; then
    OPTIONS_RESPONSE=$(curl -X OPTIONS "$API_ENDPOINT" \
        -H "Origin: https://mchangoapp.vercel.app" \
        -H "Access-Control-Request-Method: POST" \
        -H "Access-Control-Request-Headers: Content-Type" \
        -k -s -i -m 10 2>&1 || echo "TIMEOUT")
    
    if echo "$OPTIONS_RESPONSE" | grep -q "TIMEOUT\|timed out\|Connection refused"; then
        echo -e "${RED}✗ Timeout ou connexion refusée${NC}"
        echo -e "${YELLOW}  Le serveur ne répond pas${NC}"
    elif echo "$OPTIONS_RESPONSE" | grep -q "HTTP/[0-9.]* 200\|HTTP/[0-9.]* 204"; then
        echo -e "${GREEN}✓ Requête OPTIONS réussie${NC}"
        
        if echo "$OPTIONS_RESPONSE" | grep -qi "Access-Control-Allow-Origin"; then
            ALLOW_ORIGIN=$(echo "$OPTIONS_RESPONSE" | grep -i "Access-Control-Allow-Origin" | cut -d: -f2- | tr -d '\r\n' | xargs)
            echo -e "${GREEN}  Access-Control-Allow-Origin: ${ALLOW_ORIGIN}${NC}"
        else
            echo -e "${YELLOW}  ⚠ En-tête Access-Control-Allow-Origin manquant${NC}"
        fi
        
        if echo "$OPTIONS_RESPONSE" | grep -qi "Access-Control-Allow-Credentials"; then
            echo -e "${GREEN}  ✓ Access-Control-Allow-Credentials présent${NC}"
        else
            echo -e "${YELLOW}  ⚠ En-tête Access-Control-Allow-Credentials manquant${NC}"
        fi
    else
        echo -e "${YELLOW}⚠ Réponse inattendue${NC}"
        echo "$OPTIONS_RESPONSE" | head -n 10
    fi
else
    echo -e "${YELLOW}⚠ curl n'est pas installé, test ignoré${NC}"
fi
echo ""

# Test 5: Requête HTTP POST (Login)
echo -e "${BLUE}=== Test 5: Requête POST (Login) ===${NC}"
if command -v curl > /dev/null 2>&1; then
    POST_RESPONSE=$(curl -X POST "$API_ENDPOINT" \
        -H "Content-Type: application/json" \
        -H "Origin: https://mchangoapp.vercel.app" \
        -d '{"login":"agent1","motDePasse":"test"}' \
        -k -s -i -m 10 2>&1 || echo "TIMEOUT")
    
    if echo "$POST_RESPONSE" | grep -q "TIMEOUT\|timed out\|Connection refused"; then
        echo -e "${RED}✗ Timeout ou connexion refusée${NC}"
        echo -e "${YELLOW}  Le serveur ne répond pas${NC}"
    elif echo "$POST_RESPONSE" | grep -q "HTTP/[0-9.]* 200\|HTTP/[0-9.]* 401\|HTTP/[0-9.]* 403"; then
        HTTP_CODE=$(echo "$POST_RESPONSE" | grep "HTTP/" | head -n 1 | awk '{print $2}')
        echo -e "${GREEN}✓ Le serveur répond (Code: ${HTTP_CODE})${NC}"
        
        if echo "$POST_RESPONSE" | grep -qi "Access-Control-Allow-Origin"; then
            echo -e "${GREEN}  ✓ En-têtes CORS présents${NC}"
        else
            echo -e "${YELLOW}  ⚠ En-têtes CORS manquants${NC}"
        fi
    else
        echo -e "${YELLOW}⚠ Réponse inattendue${NC}"
        echo "$POST_RESPONSE" | head -n 10
    fi
else
    echo -e "${YELLOW}⚠ curl n'est pas installé, test ignoré${NC}"
fi
echo ""

# Test 6: Résolution DNS
echo -e "${BLUE}=== Test 6: Résolution DNS ===${NC}"
if command -v nslookup > /dev/null 2>&1; then
    if nslookup "$BACKEND_DOMAIN" > /dev/null 2>&1; then
        RESOLVED_IP=$(nslookup "$BACKEND_DOMAIN" | grep "Address:" | tail -n 1 | awk '{print $2}')
        echo -e "${GREEN}✓ DNS résolu: ${RESOLVED_IP}${NC}"
        
        if [ "$RESOLVED_IP" = "$BACKEND_HOST" ]; then
            echo -e "${GREEN}  ✓ L'IP correspond${NC}"
        else
            echo -e "${YELLOW}  ⚠ L'IP ne correspond pas (attendu: ${BACKEND_HOST})${NC}"
        fi
    else
        echo -e "${RED}✗ Impossible de résoudre ${BACKEND_DOMAIN}${NC}"
    fi
else
    echo -e "${YELLOW}⚠ nslookup n'est pas installé, test ignoré${NC}"
fi
echo ""

# Résumé
echo -e "${BLUE}=== Résumé ===${NC}"
echo ""
echo -e "${YELLOW}Si tous les tests échouent:${NC}"
echo -e "  1. Vérifiez que le service est démarré sur le VPS"
echo -e "     ${BLUE}ssh user@${BACKEND_HOST} 'sudo systemctl status dprihkat-api'${NC}"
echo ""
echo -e "  2. Vérifiez les logs du service"
echo -e "     ${BLUE}ssh user@${BACKEND_HOST} 'sudo journalctl -u dprihkat-api -n 50'${NC}"
echo ""
echo -e "  3. Vérifiez le pare-feu"
echo -e "     ${BLUE}ssh user@${BACKEND_HOST} 'sudo ufw status'${NC}"
echo ""
echo -e "  4. Vérifiez que le port est en écoute"
echo -e "     ${BLUE}ssh user@${BACKEND_HOST} 'sudo netstat -tlnp | grep ${BACKEND_PORT}'${NC}"
echo ""
echo -e "${YELLOW}Si le test CORS échoue:${NC}"
echo -e "  1. Vérifiez la configuration CORS dans le backend"
echo -e "  2. Redéployez avec les nouvelles configurations"
echo -e "     ${BLUE}./deploy/vps/deploy.sh user@${BACKEND_HOST}${NC}"
echo ""
echo -e "${YELLOW}Documentation:${NC}"
echo -e "  - ${BLUE}docs/diagnostic_connexion_timeout.md${NC}"
echo -e "  - ${BLUE}docs/fix_cors_frontend.md${NC}"
echo ""
