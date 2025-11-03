# Diagnostic: ERR_CONNECTION_TIMED_OUT

## Problème actuel

```
OPTIONS https://178.170.94.25.nip.io:8443/api/auth/login net::ERR_CONNECTION_TIMED_OUT
POST https://178.170.94.25.nip.io:8443/api/auth/login net::ERR_CONNECTION_TIMED_OUT
```

Le serveur backend **ne répond pas** aux requêtes. Plusieurs causes possibles:

## 1. Le service backend n'est pas démarré

### Vérification

```bash
# SSH vers votre VPS
ssh user@178.170.94.25

# Vérifier le statut du service
sudo systemctl status dprihkat-api

# Vérifier si le processus Java est en cours d'exécution
ps aux | grep java

# Vérifier les ports en écoute
sudo netstat -tlnp | grep 8443
# OU
sudo ss -tlnp | grep 8443
```

### Solution si le service n'est pas démarré

```bash
# Démarrer le service
sudo systemctl start dprihkat-api

# Vérifier les logs
sudo journalctl -u dprihkat-api -f

# Activer le démarrage automatique
sudo systemctl enable dprihkat-api
```

## 2. Le pare-feu bloque le port 8443

### Vérification

```bash
# Vérifier le pare-feu UFW
sudo ufw status

# Vérifier iptables
sudo iptables -L -n -v | grep 8443
```

### Solution

```bash
# Autoriser le port 8443
sudo ufw allow 8443/tcp

# OU avec iptables
sudo iptables -A INPUT -p tcp --dport 8443 -j ACCEPT
sudo iptables-save | sudo tee /etc/iptables/rules.v4
```

## 3. Le certificat SSL n'est pas configuré correctement

### Vérification

```bash
# Tester la connexion SSL
openssl s_client -connect 178.170.94.25:8443 -servername 178.170.94.25.nip.io

# Tester depuis le serveur lui-même
curl -k https://localhost:8443/api/auth/login -v
```

### Solution

Si vous utilisez un certificat auto-signé, les navigateurs modernes le bloqueront. Options:

#### Option A: Utiliser Let's Encrypt (RECOMMANDÉ)

```bash
# Installer Certbot
sudo apt update
sudo apt install certbot

# Obtenir un certificat pour votre domaine
sudo certbot certonly --standalone -d 178.170.94.25.nip.io

# Les certificats seront dans:
# /etc/letsencrypt/live/178.170.94.25.nip.io/fullchain.pem
# /etc/letsencrypt/live/178.170.94.25.nip.io/privkey.pem
```

Puis configurez Spring Boot pour utiliser ces certificats:

```properties
# application.properties
server.port=8443
server.ssl.enabled=true
server.ssl.certificate=/etc/letsencrypt/live/178.170.94.25.nip.io/fullchain.pem
server.ssl.certificate-private-key=/etc/letsencrypt/live/178.170.94.25.nip.io/privkey.pem
```

#### Option B: Utiliser un reverse proxy Nginx avec SSL

```nginx
# /etc/nginx/sites-available/dprihkat-api
server {
    listen 443 ssl http2;
    server_name 178.170.94.25.nip.io;

    ssl_certificate /etc/letsencrypt/live/178.170.94.25.nip.io/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/178.170.94.25.nip.io/privkey.pem;

    # Configuration SSL moderne
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    ssl_prefer_server_ciphers on;

    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # CORS headers (si nécessaire en plus du backend)
        add_header 'Access-Control-Allow-Origin' '$http_origin' always;
        add_header 'Access-Control-Allow-Credentials' 'true' always;
        add_header 'Access-Control-Allow-Methods' 'GET, POST, PUT, PATCH, DELETE, OPTIONS' always;
        add_header 'Access-Control-Allow-Headers' 'Authorization, Content-Type, Accept' always;
        
        if ($request_method = 'OPTIONS') {
            return 204;
        }
    }
}

# Redirection HTTP vers HTTPS
server {
    listen 80;
    server_name 178.170.94.25.nip.io;
    return 301 https://$server_name$request_uri;
}
```

Puis:

```bash
# Activer le site
sudo ln -s /etc/nginx/sites-available/dprihkat-api /etc/nginx/sites-enabled/

# Tester la configuration
sudo nginx -t

# Recharger Nginx
sudo systemctl reload nginx

# Modifier Spring Boot pour écouter sur HTTP en local
# application.properties
server.port=8080
# Supprimer les configurations SSL
```

## 4. Le serveur backend écoute sur le mauvais port ou interface

### Vérification

```bash
# Vérifier sur quelle interface le serveur écoute
sudo netstat -tlnp | grep java

# Devrait montrer:
# 0.0.0.0:8443 (écoute sur toutes les interfaces)
# OU
# :::8443 (IPv6, toutes les interfaces)

# Si vous voyez 127.0.0.1:8443, le serveur n'écoute que localement
```

### Solution

Dans `application.properties`:

```properties
# Écouter sur toutes les interfaces
server.address=0.0.0.0
server.port=8443
```

## 5. Configuration du fournisseur cloud/VPS

Si votre VPS est chez un fournisseur cloud (AWS, DigitalOcean, etc.), vérifiez:

### AWS EC2
- Security Groups: Autoriser le port 8443 en entrée
- Network ACLs: Vérifier les règles

### DigitalOcean
- Firewall: Autoriser le port 8443

### OVH/Autres
- Vérifier le panneau de contrôle pour les règles de pare-feu

## 6. Test de connectivité depuis votre machine locale

```bash
# Ping
ping 178.170.94.25

# Telnet sur le port 8443
telnet 178.170.94.25 8443

# Curl avec timeout
curl -v --max-time 10 https://178.170.94.25.nip.io:8443/api/auth/login

# Nmap pour scanner le port
nmap -p 8443 178.170.94.25
```

## Checklist de déploiement complète

### Étape 1: Préparer le JAR

```bash
# Sur votre machine locale
cd /home/amateur/IdeaProjects/DPRIHKAT_BACKEND_IMPOT
mvn clean package -DskipTests

# Le JAR est dans: target/impots-0.0.1-SNAPSHOT.jar
```

### Étape 2: Transférer vers le VPS

```bash
# Remplacer 'user' par votre nom d'utilisateur
scp target/impots-0.0.1-SNAPSHOT.jar user@178.170.94.25:/opt/dprihkat-api/

# OU si vous avez un chemin différent
scp target/impots-0.0.1-SNAPSHOT.jar user@178.170.94.25:~/
```

### Étape 3: Configurer le service systemd

```bash
# SSH vers le VPS
ssh user@178.170.94.25

# Créer le fichier de service
sudo nano /etc/systemd/system/dprihkat-api.service
```

Contenu du fichier:

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

# Variables d'environnement
Environment="SPRING_PROFILES_ACTIVE=production"
Environment="SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/dprihkat_db"
Environment="SPRING_DATASOURCE_USERNAME=dprihkat_user"
Environment="SPRING_DATASOURCE_PASSWORD=votre_mot_de_passe"
Environment="JWT_SECRET=votre_secret_jwt_tres_long_et_securise"

[Install]
WantedBy=multi-user.target
```

### Étape 4: Créer l'utilisateur et les répertoires

```bash
# Créer l'utilisateur système
sudo useradd -r -s /bin/false dprihkat

# Créer le répertoire de l'application
sudo mkdir -p /opt/dprihkat-api
sudo chown dprihkat:dprihkat /opt/dprihkat-api

# Déplacer le JAR
sudo mv ~/impots-0.0.1-SNAPSHOT.jar /opt/dprihkat-api/
sudo chown dprihkat:dprihkat /opt/dprihkat-api/impots-0.0.1-SNAPSHOT.jar
```

### Étape 5: Démarrer le service

```bash
# Recharger systemd
sudo systemctl daemon-reload

# Démarrer le service
sudo systemctl start dprihkat-api

# Vérifier le statut
sudo systemctl status dprihkat-api

# Voir les logs
sudo journalctl -u dprihkat-api -f

# Activer le démarrage automatique
sudo systemctl enable dprihkat-api
```

### Étape 6: Vérifier que le service écoute

```bash
# Vérifier le port
sudo netstat -tlnp | grep 8443

# Tester localement
curl -k https://localhost:8443/api/auth/login -v

# Tester depuis l'extérieur (depuis votre machine locale)
curl -v https://178.170.94.25.nip.io:8443/api/auth/login
```

### Étape 7: Configurer le pare-feu

```bash
# UFW
sudo ufw allow 8443/tcp
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw allow 22/tcp  # SSH
sudo ufw enable
sudo ufw status

# OU iptables
sudo iptables -A INPUT -p tcp --dport 8443 -j ACCEPT
sudo iptables -A INPUT -p tcp --dport 80 -j ACCEPT
sudo iptables -A INPUT -p tcp --dport 443 -j ACCEPT
sudo iptables-save | sudo tee /etc/iptables/rules.v4
```

## Logs de débogage

### Voir les logs en temps réel

```bash
sudo journalctl -u dprihkat-api -f
```

### Voir les dernières erreurs

```bash
sudo journalctl -u dprihkat-api -n 100 --no-pager
```

### Augmenter le niveau de log dans Spring Boot

Dans `application.properties`:

```properties
logging.level.root=INFO
logging.level.com.DPRIHKAT=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.springframework.security=DEBUG
```

## Test final

Une fois tout configuré:

```bash
# Test OPTIONS (preflight CORS)
curl -X OPTIONS https://178.170.94.25.nip.io:8443/api/auth/login \
  -H "Origin: https://mchangoapp.vercel.app" \
  -H "Access-Control-Request-Method: POST" \
  -H "Access-Control-Request-Headers: Content-Type" \
  -v

# Test POST (login)
curl -X POST https://178.170.94.25.nip.io:8443/api/auth/login \
  -H "Content-Type: application/json" \
  -H "Origin: https://mchangoapp.vercel.app" \
  -d '{"login":"agent1","motDePasse":"password"}' \
  -v
```

Vous devriez voir dans la réponse:

```
< HTTP/1.1 200 OK
< Access-Control-Allow-Origin: https://mchangoapp.vercel.app
< Access-Control-Allow-Credentials: true
< Content-Type: application/json
```

## Problème frontend persistant

N'oubliez pas de corriger le frontend pour **supprimer l'en-tête Origin**. Voir `docs/fix_cors_frontend.md` pour les détails.
