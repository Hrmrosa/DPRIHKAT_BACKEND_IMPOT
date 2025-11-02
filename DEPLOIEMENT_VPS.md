# Guide de Déploiement sur VPS

## 📋 Prérequis sur le VPS

1. **Java 17** installé
2. **PostgreSQL** avec extension PostGIS
3. **Nginx** (pour reverse proxy et SSL)
4. **Certbot** (pour certificat SSL Let's Encrypt)

## 🔧 Configuration du VPS

### 1. Installation des dépendances

```bash
# Mise à jour du système
sudo apt update && sudo apt upgrade -y

# Installation de Java 17
sudo apt install openjdk-17-jdk -y

# Installation de PostgreSQL et PostGIS
sudo apt install postgresql postgresql-contrib postgis -y

# Installation de Nginx
sudo apt install nginx -y

# Installation de Certbot pour SSL
sudo apt install certbot python3-certbot-nginx -y
```

### 2. Configuration de PostgreSQL

```bash
# Se connecter à PostgreSQL
sudo -u postgres psql

# Créer la base de données
CREATE DATABASE dpri_impots;

# Créer l'utilisateur
CREATE USER dpri_user WITH ENCRYPTED PASSWORD 'VOTRE_MOT_DE_PASSE_FORT';

# Donner les privilèges
GRANT ALL PRIVILEGES ON DATABASE dpri_impots TO dpri_user;

# Se connecter à la base
\c dpri_impots

# Activer PostGIS
CREATE EXTENSION postgis;

# Quitter
\q
```

### 3. Configuration Nginx avec SSL

Créer le fichier `/etc/nginx/sites-available/dpri-api`:

```nginx
server {
    listen 80;
    server_name votre-domaine-api.com;  # Remplacer par votre domaine

    # Redirection HTTP vers HTTPS
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name votre-domaine-api.com;  # Remplacer par votre domaine

    # Certificats SSL (seront générés par Certbot)
    ssl_certificate /etc/letsencrypt/live/votre-domaine-api.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/votre-domaine-api.com/privkey.pem;

    # Configuration SSL recommandée
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    ssl_prefer_server_ciphers on;

    # Headers de sécurité
    add_header Strict-Transport-Security "max-age=31536000; includeSubDomains" always;
    add_header X-Frame-Options "SAMEORIGIN" always;
    add_header X-Content-Type-Options "nosniff" always;
    add_header X-XSS-Protection "1; mode=block" always;

    # Taille maximale des uploads
    client_max_body_size 50M;

    # Proxy vers l'application Spring Boot
    location / {
        proxy_pass http://localhost:8080;
        proxy_http_version 1.1;
        
        # Headers pour WebSocket
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
        
        # Headers standards
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
    }

    # Configuration spécifique pour WebSocket
    location /ws {
        proxy_pass http://localhost:8080/ws;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # Timeouts plus longs pour WebSocket
        proxy_connect_timeout 7d;
        proxy_send_timeout 7d;
        proxy_read_timeout 7d;
    }
}
```

Activer le site:

```bash
sudo ln -s /etc/nginx/sites-available/dpri-api /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

### 4. Obtenir le certificat SSL

```bash
sudo certbot --nginx -d votre-domaine-api.com
```

### 5. Déploiement de l'application

#### A. Compiler l'application localement

```bash
# Sur votre machine locale
cd /home/amateur/IdeaProjects/DPRIHKAT_BACKEND_IMPOT
mvn clean package -DskipTests
```

#### B. Transférer le JAR sur le VPS

```bash
# Sur votre machine locale
scp target/impots-0.0.1-SNAPSHOT.jar user@votre-vps:/home/user/dpri-api/
```

#### C. Créer un service systemd

Sur le VPS, créer `/etc/systemd/system/dpri-api.service`:

```ini
[Unit]
Description=DPRI Impots API
After=syslog.target network.target postgresql.service

[Service]
Type=simple
User=www-data
WorkingDirectory=/home/user/dpri-api
ExecStart=/usr/bin/java -jar \
    -Dspring.profiles.active=prod \
    -Xms512m -Xmx2048m \
    /home/user/dpri-api/impots-0.0.1-SNAPSHOT.jar

# Variables d'environnement
Environment="DATABASE_URL=jdbc:postgresql://localhost:5432/dpri_impots"
Environment="DATABASE_USERNAME=dpri_user"
Environment="DATABASE_PASSWORD=VOTRE_MOT_DE_PASSE"
Environment="JWT_SECRET=VOTRE_SECRET_JWT_TRES_LONG_ET_SECURISE"
Environment="PORT=8080"

# Restart policy
Restart=always
RestartSec=10

# Logging
StandardOutput=journal
StandardError=journal
SyslogIdentifier=dpri-api

[Install]
WantedBy=multi-user.target
```

#### D. Démarrer le service

```bash
# Recharger systemd
sudo systemctl daemon-reload

# Activer le service au démarrage
sudo systemctl enable dpri-api

# Démarrer le service
sudo systemctl start dpri-api

# Vérifier le statut
sudo systemctl status dpri-api

# Voir les logs
sudo journalctl -u dpri-api -f
```

## 🔒 Variables d'Environnement Importantes

Créer un fichier `.env` ou configurer directement dans le service systemd:

```bash
# Base de données
DATABASE_URL=jdbc:postgresql://localhost:5432/dpri_impots
DATABASE_USERNAME=dpri_user
DATABASE_PASSWORD=VOTRE_MOT_DE_PASSE_FORT

# JWT (générer une clé forte)
JWT_SECRET=VOTRE_SECRET_JWT_MINIMUM_256_CARACTERES

# Port
PORT=8080

# Mail (optionnel)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=votre-email@gmail.com
MAIL_PASSWORD=votre-mot-de-passe-app
MAIL_SMTP_AUTH=true
MAIL_STARTTLS=true
```

## 🔐 Sécurité

### Générer un JWT Secret fort

```bash
openssl rand -base64 64
```

### Firewall

```bash
# Autoriser SSH
sudo ufw allow 22/tcp

# Autoriser HTTP et HTTPS
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp

# Bloquer l'accès direct au port 8080 depuis l'extérieur
sudo ufw deny 8080/tcp

# Activer le firewall
sudo ufw enable
```

### PostgreSQL

```bash
# Éditer pg_hba.conf pour sécuriser l'accès
sudo nano /etc/postgresql/*/main/pg_hba.conf

# S'assurer que seul localhost peut se connecter
# local   all             all                                     peer
# host    all             all             127.0.0.1/32            md5
```

## 📊 Monitoring et Logs

### Voir les logs de l'application

```bash
# Logs en temps réel
sudo journalctl -u dpri-api -f

# Dernières 100 lignes
sudo journalctl -u dpri-api -n 100

# Logs d'aujourd'hui
sudo journalctl -u dpri-api --since today
```

### Voir les logs Nginx

```bash
# Logs d'accès
sudo tail -f /var/log/nginx/access.log

# Logs d'erreur
sudo tail -f /var/log/nginx/error.log
```

## 🔄 Mise à jour de l'application

```bash
# 1. Compiler la nouvelle version localement
mvn clean package -DskipTests

# 2. Transférer sur le VPS
scp target/impots-0.0.1-SNAPSHOT.jar user@votre-vps:/home/user/dpri-api/

# 3. Redémarrer le service
sudo systemctl restart dpri-api

# 4. Vérifier
sudo systemctl status dpri-api
```

## ✅ Checklist de déploiement

- [ ] Java 17 installé
- [ ] PostgreSQL avec PostGIS configuré
- [ ] Base de données créée
- [ ] Nginx installé et configuré
- [ ] Certificat SSL obtenu avec Certbot
- [ ] Variables d'environnement configurées
- [ ] Service systemd créé
- [ ] Firewall configuré
- [ ] Application démarrée
- [ ] Logs vérifiés
- [ ] Test depuis le frontend (https://mchangoapp.vercel.app)

## 🌐 URLs

- **Frontend**: https://mchangoapp.vercel.app
- **Backend API**: https://votre-domaine-api.com
- **Swagger UI**: https://votre-domaine-api.com/swagger-ui.html

## 🆘 Dépannage

### L'application ne démarre pas

```bash
# Vérifier les logs
sudo journalctl -u dpri-api -n 200

# Vérifier que le port 8080 est libre
sudo netstat -tlnp | grep 8080

# Vérifier la connexion à PostgreSQL
psql -h localhost -U dpri_user -d dpri_impots
```

### Erreurs CORS

- Vérifier que `https://mchangoapp.vercel.app` est bien dans la configuration CORS
- Vérifier les headers Nginx
- Vérifier les logs de l'application

### Erreurs SSL

```bash
# Renouveler le certificat
sudo certbot renew

# Tester la configuration Nginx
sudo nginx -t
```

## 📞 Support

En cas de problème, vérifier:
1. Les logs de l'application: `sudo journalctl -u dpri-api -f`
2. Les logs Nginx: `sudo tail -f /var/log/nginx/error.log`
3. La connexion à la base de données
4. Les variables d'environnement
