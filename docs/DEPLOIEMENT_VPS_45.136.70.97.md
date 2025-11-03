# Déploiement sur VPS 45.136.70.97

## ⚠️ PROBLÈME ACTUEL

Le backend à `https://45.136.70.97.nip.io:8443` ne répond pas (`ERR_CONNECTION_REFUSED`).

**Ce n'est PAS un problème CORS** - le serveur ne répond tout simplement pas.

## ✅ CORS CORRIGÉ

La configuration CORS a été mise à jour pour supporter:
- `https://mchangoapp.vercel.app`
- `https://dpri-impot-frontend.vercel.app`
- `https://*.vercel.app` (tous les sous-domaines Vercel)
- `http://localhost:*` (développement local)

Le JAR compilé est prêt: `target/impots-0.0.1-SNAPSHOT.jar`

---

## 🚀 ÉTAPES DE DÉPLOIEMENT

### 1. Transférer le JAR vers le VPS

```bash
# Depuis votre machine locale
scp target/impots-0.0.1-SNAPSHOT.jar root@45.136.70.97:/opt/dprihkat-api/
```

### 2. Se connecter au VPS

```bash
ssh root@45.136.70.97
```

### 3. Vérifier si le service existe

```bash
sudo systemctl status dprihkat-api
```

**Si le service n'existe pas**, passez à l'étape 4 (Configuration initiale).

**Si le service existe**, redémarrez-le:

```bash
sudo systemctl restart dprihkat-api
sudo systemctl status dprihkat-api
```

### 4. Configuration initiale (si le service n'existe pas)

#### A. Créer l'utilisateur système

```bash
sudo useradd -r -s /bin/false dprihkat
```

#### B. Créer les répertoires

```bash
sudo mkdir -p /opt/dprihkat-api
sudo chown dprihkat:dprihkat /opt/dprihkat-api
```

#### C. Déplacer le JAR

```bash
sudo mv /root/impots-0.0.1-SNAPSHOT.jar /opt/dprihkat-api/
sudo chown dprihkat:dprihkat /opt/dprihkat-api/impots-0.0.1-SNAPSHOT.jar
```

#### D. Créer le fichier de service systemd

```bash
sudo nano /etc/systemd/system/dprihkat-api.service
```

**Contenu du fichier:**

```ini
[Unit]
Description=DPRIHKAT API Backend
After=network.target postgresql.service

[Service]
Type=simple
User=dprihkat
Group=dprihkat
WorkingDirectory=/opt/dprihkat-api
ExecStart=/usr/bin/java -jar /opt/dprihkat-api/impots-0.0.1-SNAPSHOT.jar
Restart=always
RestartSec=10
StandardOutput=journal
StandardError=journal
SyslogIdentifier=dprihkat-api

# Variables d'environnement - MODIFIEZ CES VALEURS
Environment="SPRING_PROFILES_ACTIVE=production"
Environment="SERVER_PORT=8443"
Environment="SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/dprihkat_db"
Environment="SPRING_DATASOURCE_USERNAME=dprihkat_user"
Environment="SPRING_DATASOURCE_PASSWORD=VOTRE_MOT_DE_PASSE_POSTGRESQL"
Environment="JWT_SECRET=VOTRE_SECRET_JWT_TRES_LONG_ET_SECURISE_MINIMUM_64_CARACTERES"

# Configuration SSL (si vous utilisez HTTPS directement dans Spring Boot)
# Environment="SERVER_SSL_ENABLED=true"
# Environment="SERVER_SSL_KEY_STORE=/etc/letsencrypt/live/45.136.70.97.nip.io/keystore.p12"
# Environment="SERVER_SSL_KEY_STORE_PASSWORD=votre_password"
# Environment="SERVER_SSL_KEY_STORE_TYPE=PKCS12"

[Install]
WantedBy=multi-user.target
```

#### E. Démarrer le service

```bash
sudo systemctl daemon-reload
sudo systemctl start dprihkat-api
sudo systemctl enable dprihkat-api
sudo systemctl status dprihkat-api
```

### 5. Vérifier les logs

```bash
# Logs en temps réel
sudo journalctl -u dprihkat-api -f

# Dernières 50 lignes
sudo journalctl -u dprihkat-api -n 50
```

### 6. Vérifier que le port est en écoute

```bash
sudo netstat -tlnp | grep 8443
# OU
sudo ss -tlnp | grep 8443
```

Vous devriez voir:
```
tcp6  0  0 :::8443  :::*  LISTEN  12345/java
```

### 7. Configurer le pare-feu

```bash
# Vérifier le statut
sudo ufw status

# Autoriser le port 8443
sudo ufw allow 8443/tcp
sudo ufw reload
```

### 8. Tester localement sur le VPS

```bash
# Test simple
curl -k https://localhost:8443/api/auth/login -v

# Test avec données
curl -k -X POST https://localhost:8443/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"login":"agent1","motDePasse":"test"}' \
  -v
```

---

## 🔐 Configuration SSL (IMPORTANT)

Vous avez deux options pour HTTPS:

### Option A: Nginx comme reverse proxy (RECOMMANDÉ)

Cette option est plus simple et flexible.

#### 1. Installer Nginx

```bash
sudo apt update
sudo apt install nginx certbot python3-certbot-nginx
```

#### 2. Obtenir un certificat SSL

```bash
# Arrêter Nginx temporairement
sudo systemctl stop nginx

# Obtenir le certificat
sudo certbot certonly --standalone -d 45.136.70.97.nip.io

# Redémarrer Nginx
sudo systemctl start nginx
```

#### 3. Configurer Nginx

```bash
sudo nano /etc/nginx/sites-available/dprihkat-api
```

**Contenu:**

```nginx
server {
    listen 443 ssl http2;
    server_name 45.136.70.97.nip.io;

    ssl_certificate /etc/letsencrypt/live/45.136.70.97.nip.io/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/45.136.70.97.nip.io/privkey.pem;

    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    ssl_prefer_server_ciphers on;

    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header X-Forwarded-Host $host;
        proxy_set_header X-Forwarded-Port $server_port;
    }
}

server {
    listen 80;
    server_name 45.136.70.97.nip.io;
    return 301 https://$server_name$request_uri;
}
```

#### 4. Activer le site

```bash
sudo ln -s /etc/nginx/sites-available/dprihkat-api /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl reload nginx
```

#### 5. Modifier le service pour écouter sur HTTP:8080

Dans `/etc/systemd/system/dprihkat-api.service`, changez:

```ini
Environment="SERVER_PORT=8080"
# Supprimer les variables SSL si présentes
```

Puis:

```bash
sudo systemctl daemon-reload
sudo systemctl restart dprihkat-api
```

### Option B: SSL directement dans Spring Boot

Si vous préférez gérer SSL dans Spring Boot, vous devez convertir les certificats Let's Encrypt en keystore PKCS12:

```bash
sudo openssl pkcs12 -export \
  -in /etc/letsencrypt/live/45.136.70.97.nip.io/fullchain.pem \
  -inkey /etc/letsencrypt/live/45.136.70.97.nip.io/privkey.pem \
  -out /etc/letsencrypt/live/45.136.70.97.nip.io/keystore.p12 \
  -name dprihkat \
  -password pass:VOTRE_PASSWORD_ICI

sudo chown dprihkat:dprihkat /etc/letsencrypt/live/45.136.70.97.nip.io/keystore.p12
```

Puis activez les variables SSL dans le service systemd (voir étape 4D).

---

## 🧪 Tests après déploiement

### Test 1: Depuis le VPS

```bash
curl -k https://localhost:8443/api/auth/login -v
```

### Test 2: Depuis votre machine locale

```bash
curl https://45.136.70.97.nip.io:8443/api/auth/login -v
```

### Test 3: Test CORS

```bash
curl -X OPTIONS https://45.136.70.97.nip.io:8443/api/auth/login \
  -H "Origin: https://dpri-impot-frontend.vercel.app" \
  -H "Access-Control-Request-Method: POST" \
  -v
```

Vous devriez voir:
```
< HTTP/1.1 200 OK
< Access-Control-Allow-Origin: https://dpri-impot-frontend.vercel.app
< Access-Control-Allow-Credentials: true
< Access-Control-Allow-Methods: GET, POST, PUT, PATCH, DELETE, OPTIONS
```

---

## 📋 Checklist de déploiement

- [ ] JAR transféré vers `/opt/dprihkat-api/`
- [ ] Service systemd créé et configuré
- [ ] Variables d'environnement configurées (DB, JWT_SECRET)
- [ ] Service démarré: `sudo systemctl start dprihkat-api`
- [ ] Service actif au démarrage: `sudo systemctl enable dprihkat-api`
- [ ] Port 8443 en écoute (vérifier avec `netstat` ou `ss`)
- [ ] Pare-feu configuré: `sudo ufw allow 8443/tcp`
- [ ] SSL configuré (Nginx ou Spring Boot)
- [ ] Test local réussi depuis le VPS
- [ ] Test externe réussi depuis votre machine
- [ ] Test CORS réussi

---

## 🔧 Commandes utiles

### Voir les logs

```bash
# Temps réel
sudo journalctl -u dprihkat-api -f

# Dernières erreurs
sudo journalctl -u dprihkat-api -p err -n 50

# Depuis le début
sudo journalctl -u dprihkat-api --no-pager
```

### Redémarrer le service

```bash
sudo systemctl restart dprihkat-api
```

### Vérifier le statut

```bash
sudo systemctl status dprihkat-api
```

### Vérifier les connexions

```bash
sudo netstat -tlnp | grep java
```

---

## ❓ Dépannage

### Le service ne démarre pas

```bash
# Voir les logs détaillés
sudo journalctl -u dprihkat-api -n 100 --no-pager

# Vérifier les permissions
ls -la /opt/dprihkat-api/

# Tester le JAR manuellement
cd /opt/dprihkat-api
sudo -u dprihkat java -jar impots-0.0.1-SNAPSHOT.jar
```

### Le port n'est pas accessible de l'extérieur

```bash
# Vérifier le pare-feu
sudo ufw status verbose

# Vérifier iptables
sudo iptables -L -n -v | grep 8443

# Vérifier que le service écoute sur toutes les interfaces
sudo netstat -tlnp | grep 8443
# Doit montrer 0.0.0.0:8443 ou :::8443, PAS 127.0.0.1:8443
```

### Erreur de connexion à la base de données

```bash
# Vérifier que PostgreSQL est démarré
sudo systemctl status postgresql

# Vérifier que la base existe
sudo -u postgres psql -l | grep dprihkat

# Tester la connexion
sudo -u postgres psql -d dprihkat_db -c "SELECT 1;"
```

---

## 📞 Support

Si le problème persiste:

1. Vérifiez les logs: `sudo journalctl -u dprihkat-api -n 100`
2. Vérifiez que le service est démarré: `sudo systemctl status dprihkat-api`
3. Vérifiez le pare-feu: `sudo ufw status`
4. Testez localement sur le VPS: `curl -k https://localhost:8443/api/auth/login -v`
