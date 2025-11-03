# Configuration SSL Let's Encrypt pour Spring Boot

## Option 1: Certificat auto-signé (RAPIDE - pour test)

### Utiliser le script automatique

**1. Transférer le script:**

```bash
scp deploy/vps/setup-ssl-springboot.sh root@45.136.70.97:/tmp/
```

**2. Exécuter sur le VPS:**

```bash
ssh root@45.136.70.97
sudo bash /tmp/setup-ssl-springboot.sh
```

Le script va:
- ✅ Créer un keystore PKCS12 avec certificat auto-signé
- ✅ Configurer Spring Boot pour utiliser SSL
- ✅ Ouvrir le port 8443 dans le pare-feu
- ✅ Redémarrer le service

**3. Tester:**

```bash
curl -k https://45.136.70.97.nip.io:8443/api/auth/login -v
```

---

## Option 2: Let's Encrypt (PRODUCTION - certificat valide)

### Prérequis

- Domaine pointant vers votre serveur (45.136.70.97.nip.io fonctionne)
- Port 80 accessible (pour la validation Let's Encrypt)

### Étape 1: Obtenir le certificat Let's Encrypt

```bash
ssh root@45.136.70.97

# Installer Certbot
sudo apt update
sudo apt install -y certbot

# Arrêter temporairement le service (pour libérer le port 80)
sudo systemctl stop dpri-impots

# Obtenir le certificat
sudo certbot certonly --standalone -d 45.136.70.97.nip.io

# Les certificats seront dans:
# /etc/letsencrypt/live/45.136.70.97.nip.io/fullchain.pem
# /etc/letsencrypt/live/45.136.70.97.nip.io/privkey.pem
```

### Étape 2: Convertir les certificats en keystore PKCS12

```bash
# Créer le répertoire pour le keystore
sudo mkdir -p /etc/dprihkat-ssl

# Convertir les certificats PEM en PKCS12
sudo openssl pkcs12 -export \
  -in /etc/letsencrypt/live/45.136.70.97.nip.io/fullchain.pem \
  -inkey /etc/letsencrypt/live/45.136.70.97.nip.io/privkey.pem \
  -out /etc/dprihkat-ssl/keystore.p12 \
  -name dprihkat \
  -passout pass:dprihkat2025SecurePassword

# Donner les permissions appropriées
sudo chown dpri:dpri /etc/dprihkat-ssl/keystore.p12
sudo chmod 600 /etc/dprihkat-ssl/keystore.p12
```

### Étape 3: Configurer Spring Boot

```bash
# Éditer le fichier de configuration
sudo nano /etc/dpri-impots/env
```

Ajouter ou modifier ces lignes:

```bash
# Configuration SSL
SERVER_PORT=8443
SERVER_SSL_ENABLED=true
SERVER_SSL_KEY_STORE=/etc/dprihkat-ssl/keystore.p12
SERVER_SSL_KEY_STORE_PASSWORD=dprihkat2025SecurePassword
SERVER_SSL_KEY_STORE_TYPE=PKCS12
SERVER_SSL_KEY_ALIAS=dprihkat
```

### Étape 4: Ouvrir les ports

```bash
# Port 80 pour le renouvellement Let's Encrypt
sudo ufw allow 80/tcp

# Port 8443 pour HTTPS
sudo ufw allow 8443/tcp

sudo ufw status
```

### Étape 5: Redémarrer le service

```bash
sudo systemctl restart dpri-impots
sudo journalctl -u dpri-impots -f
```

### Étape 6: Tester

```bash
# Test local
curl https://localhost:8443/api/auth/login -v

# Test externe (sans -k car le certificat est valide!)
curl https://45.136.70.97.nip.io:8443/api/auth/login -v
```

---

## Renouvellement automatique Let's Encrypt

### Créer un script de renouvellement

```bash
sudo nano /usr/local/bin/renew-dprihkat-cert.sh
```

Contenu:

```bash
#!/bin/bash

# Script de renouvellement du certificat Let's Encrypt pour DPRIHKAT

set -e

echo "🔄 Renouvellement du certificat Let's Encrypt"

# Arrêter le service pour libérer le port 80
systemctl stop dpri-impots

# Renouveler le certificat
certbot renew --standalone

# Convertir le nouveau certificat en PKCS12
openssl pkcs12 -export \
  -in /etc/letsencrypt/live/45.136.70.97.nip.io/fullchain.pem \
  -inkey /etc/letsencrypt/live/45.136.70.97.nip.io/privkey.pem \
  -out /etc/dprihkat-ssl/keystore.p12 \
  -name dprihkat \
  -passout pass:dprihkat2025SecurePassword

# Permissions
chown dpri:dpri /etc/dprihkat-ssl/keystore.p12
chmod 600 /etc/dprihkat-ssl/keystore.p12

# Redémarrer le service
systemctl start dpri-impots

echo "✅ Certificat renouvelé et service redémarré"
```

Rendre le script exécutable:

```bash
sudo chmod +x /usr/local/bin/renew-dprihkat-cert.sh
```

### Configurer le cron pour le renouvellement automatique

```bash
sudo crontab -e
```

Ajouter cette ligne (renouvellement tous les lundis à 3h du matin):

```bash
0 3 * * 1 /usr/local/bin/renew-dprihkat-cert.sh >> /var/log/dprihkat-cert-renew.log 2>&1
```

### Tester le renouvellement

```bash
# Test sans vraiment renouveler
sudo certbot renew --dry-run

# Test du script complet
sudo /usr/local/bin/renew-dprihkat-cert.sh
```

---

## Configuration avancée (optionnel)

### Forcer HTTPS uniquement

Dans `/etc/dpri-impots/env`, ajouter:

```bash
# Forcer HTTPS
SERVER_SSL_ENABLED=true
SECURITY_REQUIRE_SSL=true
```

### Configurer les protocoles SSL

```bash
# Protocoles SSL modernes uniquement
SERVER_SSL_ENABLED_PROTOCOLS=TLSv1.2,TLSv1.3

# Ciphers sécurisés
SERVER_SSL_CIPHERS=TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384
```

---

## Vérifications

### 1. Vérifier que le service écoute sur 8443

```bash
sudo netstat -tlnp | grep 8443
```

Devrait montrer:
```
tcp6  0  0  :::8443  :::*  LISTEN  <pid>/java
```

### 2. Vérifier le certificat

```bash
# Informations sur le certificat
openssl s_client -connect localhost:8443 -servername 45.136.70.97.nip.io < /dev/null 2>/dev/null | openssl x509 -noout -text

# Date d'expiration
openssl s_client -connect localhost:8443 -servername 45.136.70.97.nip.io < /dev/null 2>/dev/null | openssl x509 -noout -dates
```

### 3. Test CORS complet

```bash
curl -X OPTIONS https://45.136.70.97.nip.io:8443/api/auth/login \
  -H "Origin: https://dpri-impot-frontend.vercel.app" \
  -H "Access-Control-Request-Method: POST" \
  -v
```

---

## Dépannage

### Le service ne démarre pas

```bash
# Voir les logs
sudo journalctl -u dpri-impots -n 100 --no-pager

# Erreurs courantes:
# - Keystore password incorrect
# - Keystore file not found
# - Port 8443 déjà utilisé
```

### Erreur "keystore password was incorrect"

Vérifiez que le mot de passe dans `/etc/dpri-impots/env` correspond à celui utilisé lors de la création du keystore.

### Port 8443 déjà utilisé

```bash
# Trouver quel processus utilise le port
sudo netstat -tlnp | grep 8443

# Si c'est Nginx, l'arrêter
sudo systemctl stop nginx
sudo systemctl disable nginx
```

### Le certificat n'est pas valide

Pour Let's Encrypt:

```bash
# Vérifier les certificats
sudo certbot certificates

# Forcer le renouvellement
sudo certbot renew --force-renewal
sudo /usr/local/bin/renew-dprihkat-cert.sh
```

---

## Comparaison: Spring Boot SSL vs Nginx

### Spring Boot SSL (votre choix actuel)

**Avantages:**
- ✅ Configuration simple
- ✅ Pas de composant supplémentaire
- ✅ Tout géré par Spring Boot

**Inconvénients:**
- ⚠️ Renouvellement Let's Encrypt nécessite l'arrêt du service
- ⚠️ Moins flexible pour la configuration SSL avancée
- ⚠️ Pas de load balancing facile

### Nginx (alternative)

**Avantages:**
- ✅ Renouvellement Let's Encrypt sans arrêt du service
- ✅ Configuration SSL très flexible
- ✅ Load balancing et cache faciles
- ✅ Peut servir des fichiers statiques

**Inconvénients:**
- ⚠️ Composant supplémentaire à gérer
- ⚠️ Configuration plus complexe

---

## Résumé des commandes

### Installation rapide (certificat auto-signé)

```bash
# Sur votre machine
scp deploy/vps/setup-ssl-springboot.sh root@45.136.70.97:/tmp/

# Sur le VPS
ssh root@45.136.70.97
sudo bash /tmp/setup-ssl-springboot.sh
```

### Installation Let's Encrypt (production)

```bash
ssh root@45.136.70.97

# 1. Obtenir le certificat
sudo systemctl stop dpri-impots
sudo certbot certonly --standalone -d 45.136.70.97.nip.io

# 2. Convertir en PKCS12
sudo mkdir -p /etc/dprihkat-ssl
sudo openssl pkcs12 -export \
  -in /etc/letsencrypt/live/45.136.70.97.nip.io/fullchain.pem \
  -inkey /etc/letsencrypt/live/45.136.70.97.nip.io/privkey.pem \
  -out /etc/dprihkat-ssl/keystore.p12 \
  -name dprihkat \
  -passout pass:dprihkat2025SecurePassword
sudo chown dpri:dpri /etc/dprihkat-ssl/keystore.p12
sudo chmod 600 /etc/dprihkat-ssl/keystore.p12

# 3. Configurer Spring Boot
sudo nano /etc/dpri-impots/env
# Ajouter les variables SERVER_SSL_*

# 4. Redémarrer
sudo systemctl start dpri-impots
sudo journalctl -u dpri-impots -f
```

---

## Checklist

- [ ] Script SSL exécuté OU certificat Let's Encrypt obtenu
- [ ] Keystore PKCS12 créé
- [ ] Variables SSL ajoutées dans `/etc/dpri-impots/env`
- [ ] Port 8443 ouvert: `sudo ufw allow 8443/tcp`
- [ ] Service redémarré: `sudo systemctl restart dpri-impots`
- [ ] Service écoute sur 8443: `sudo netstat -tlnp | grep 8443`
- [ ] Test local réussi: `curl -k https://localhost:8443/api/auth/login -v`
- [ ] Test externe réussi: `curl -k https://45.136.70.97.nip.io:8443/api/auth/login -v`
- [ ] Script de renouvellement créé (Let's Encrypt uniquement)
- [ ] Cron configuré (Let's Encrypt uniquement)
