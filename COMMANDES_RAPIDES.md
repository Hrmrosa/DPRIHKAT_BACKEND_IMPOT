# 🚀 Commandes Rapides - Déploiement VPS

## 📦 Build Local

```bash
# Build pour production
./build-for-production.sh

# OU manuellement
mvn clean package -DskipTests
```

## 📤 Transfert vers VPS

```bash
# Transférer le JAR
scp target/impots-0.0.1-SNAPSHOT.jar user@IP_VPS:/home/user/dpri-api/

# Transférer plusieurs fichiers
scp target/impots-0.0.1-SNAPSHOT.jar \
    DEPLOIEMENT_VPS.md \
    user@IP_VPS:/home/user/dpri-api/
```

## 🔧 Configuration VPS (Première fois)

```bash
# 1. Installer Java 17
sudo apt update && sudo apt install openjdk-17-jdk -y

# 2. Installer PostgreSQL + PostGIS
sudo apt install postgresql postgresql-contrib postgis -y

# 3. Installer Nginx
sudo apt install nginx -y

# 4. Installer Certbot
sudo apt install certbot python3-certbot-nginx -y

# 5. Créer la base de données
sudo -u postgres psql
CREATE DATABASE dpri_impots;
CREATE USER dpri_user WITH ENCRYPTED PASSWORD 'VOTRE_MOT_DE_PASSE';
GRANT ALL PRIVILEGES ON DATABASE dpri_impots TO dpri_user;
\c dpri_impots
CREATE EXTENSION postgis;
\q

# 6. Obtenir le certificat SSL
sudo certbot --nginx -d votre-domaine-api.com

# 7. Créer le répertoire de l'application
mkdir -p /home/user/dpri-api
cd /home/user/dpri-api
```

## 🎯 Service Systemd

```bash
# Créer le fichier service
sudo nano /etc/systemd/system/dpri-api.service

# Contenu minimal (adapter les chemins et variables):
[Unit]
Description=DPRI Impots API
After=postgresql.service

[Service]
Type=simple
User=www-data
WorkingDirectory=/home/user/dpri-api
ExecStart=/usr/bin/java -jar \
    -Dspring.profiles.active=prod \
    -Xms512m -Xmx2048m \
    /home/user/dpri-api/impots-0.0.1-SNAPSHOT.jar

Environment="DATABASE_URL=jdbc:postgresql://localhost:5432/dpri_impots"
Environment="DATABASE_USERNAME=dpri_user"
Environment="DATABASE_PASSWORD=VOTRE_MOT_DE_PASSE"
Environment="JWT_SECRET=VOTRE_SECRET_JWT"
Environment="PORT=8080"

Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target

# Activer et démarrer
sudo systemctl daemon-reload
sudo systemctl enable dpri-api
sudo systemctl start dpri-api
```

## 🔄 Gestion du Service

```bash
# Démarrer
sudo systemctl start dpri-api

# Arrêter
sudo systemctl stop dpri-api

# Redémarrer
sudo systemctl restart dpri-api

# Statut
sudo systemctl status dpri-api

# Logs en temps réel
sudo journalctl -u dpri-api -f

# Dernières 100 lignes
sudo journalctl -u dpri-api -n 100
```

## 🌐 Configuration Nginx

```bash
# Créer la configuration
sudo nano /etc/nginx/sites-available/dpri-api

# Contenu minimal:
server {
    listen 80;
    server_name votre-domaine-api.com;
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name votre-domaine-api.com;

    ssl_certificate /etc/letsencrypt/live/votre-domaine-api.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/votre-domaine-api.com/privkey.pem;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location /ws {
        proxy_pass http://localhost:8080/ws;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
    }
}

# Activer le site
sudo ln -s /etc/nginx/sites-available/dpri-api /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

## 🔒 Firewall

```bash
# Autoriser SSH, HTTP, HTTPS
sudo ufw allow 22/tcp
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp

# Bloquer le port 8080 depuis l'extérieur
sudo ufw deny 8080/tcp

# Activer
sudo ufw enable

# Vérifier
sudo ufw status
```

## 🔐 Générer un JWT Secret

```bash
# Générer un secret fort (64 caractères)
openssl rand -base64 64
```

## 🔄 Mise à Jour de l'Application

```bash
# 1. Sur votre machine locale
./build-for-production.sh
scp target/impots-0.0.1-SNAPSHOT.jar user@IP_VPS:/home/user/dpri-api/

# 2. Sur le VPS
sudo systemctl restart dpri-api
sudo journalctl -u dpri-api -f
```

## 🧪 Tests

```bash
# Test de l'API
curl -I https://votre-domaine-api.com/api/auth/login

# Test depuis le frontend
# Ouvrir https://mchangoapp.vercel.app et tester la connexion

# Vérifier les logs
sudo journalctl -u dpri-api -f
sudo tail -f /var/log/nginx/error.log
```

## 📊 Monitoring

```bash
# Logs application
sudo journalctl -u dpri-api -f

# Logs Nginx
sudo tail -f /var/log/nginx/access.log
sudo tail -f /var/log/nginx/error.log

# Utilisation mémoire
free -h
htop

# Espace disque
df -h

# Processus Java
ps aux | grep java
```

## 🆘 Dépannage Rapide

```bash
# Application ne démarre pas
sudo journalctl -u dpri-api -n 200
sudo systemctl status dpri-api

# Erreur de connexion DB
psql -h localhost -U dpri_user -d dpri_impots

# Erreur Nginx
sudo nginx -t
sudo tail -f /var/log/nginx/error.log

# Vérifier les ports
sudo netstat -tlnp | grep -E '8080|80|443'

# Redémarrer tout
sudo systemctl restart dpri-api
sudo systemctl restart nginx
sudo systemctl restart postgresql
```

## 🔄 Renouvellement SSL

```bash
# Test de renouvellement
sudo certbot renew --dry-run

# Renouvellement réel
sudo certbot renew

# Automatique (cron déjà configuré par Certbot)
```

## 📝 Variables d'Environnement

```bash
# Éditer le service
sudo nano /etc/systemd/system/dpri-api.service

# Ajouter/Modifier les variables dans la section [Service]:
Environment="DATABASE_URL=jdbc:postgresql://localhost:5432/dpri_impots"
Environment="DATABASE_USERNAME=dpri_user"
Environment="DATABASE_PASSWORD=VOTRE_MOT_DE_PASSE"
Environment="JWT_SECRET=VOTRE_SECRET_JWT"

# Recharger et redémarrer
sudo systemctl daemon-reload
sudo systemctl restart dpri-api
```

## ✅ Checklist Déploiement

```bash
# Vérifier que tout fonctionne:
□ sudo systemctl status dpri-api          # Service actif
□ sudo systemctl status nginx             # Nginx actif
□ sudo systemctl status postgresql        # PostgreSQL actif
□ curl -I https://votre-domaine-api.com   # HTTPS fonctionne
□ sudo journalctl -u dpri-api -n 50       # Pas d'erreurs
□ Test depuis https://mchangoapp.vercel.app # Frontend connecté
```

## 🎯 Commande Tout-en-Un (Mise à jour)

```bash
# Sur votre machine locale
./build-for-production.sh && \
scp target/impots-0.0.1-SNAPSHOT.jar user@IP_VPS:/home/user/dpri-api/ && \
ssh user@IP_VPS "sudo systemctl restart dpri-api && sudo journalctl -u dpri-api -n 20"
```
