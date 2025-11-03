# Plan d'action pour résoudre les problèmes de connexion

## 🔴 Problèmes identifiés

### 1. Backend inaccessible
```
❌ Le serveur à 178.170.94.25:8443 ne répond pas
❌ net::ERR_CONNECTION_TIMED_OUT
```

### 2. Frontend tente de définir l'en-tête Origin
```
❌ Refused to set unsafe header "Origin"
```

## ✅ Solutions

### ÉTAPE 1: Vérifier et démarrer le backend sur le VPS

Connectez-vous à votre VPS:

```bash
ssh user@178.170.94.25
```

#### A. Vérifier le statut du service

```bash
sudo systemctl status dprihkat-api
```

**Si le service n'existe pas**, créez-le (voir section "Configuration initiale" ci-dessous).

**Si le service est arrêté**:

```bash
sudo systemctl start dprihkat-api
sudo systemctl enable dprihkat-api  # Démarrage automatique
```

#### B. Vérifier les logs

```bash
# Logs en temps réel
sudo journalctl -u dprihkat-api -f

# Dernières 50 lignes
sudo journalctl -u dprihkat-api -n 50
```

#### C. Vérifier que le port est en écoute

```bash
sudo netstat -tlnp | grep 8443
# OU
sudo ss -tlnp | grep 8443
```

Vous devriez voir quelque chose comme:
```
tcp6  0  0 :::8443  :::*  LISTEN  12345/java
```

#### D. Vérifier le pare-feu

```bash
# UFW
sudo ufw status

# Si le port 8443 n'est pas autorisé:
sudo ufw allow 8443/tcp
sudo ufw reload
```

#### E. Tester localement sur le VPS

```bash
# Test HTTP
curl -k https://localhost:8443/api/auth/login -v

# Devrait retourner une erreur 401 ou 400 (c'est normal sans credentials)
# L'important est que le serveur réponde
```

---

### ÉTAPE 2: Configuration initiale du service (si nécessaire)

Si le service n'existe pas encore, suivez ces étapes:

#### A. Créer l'utilisateur système

```bash
sudo useradd -r -s /bin/false dprihkat
```

#### B. Créer les répertoires

```bash
sudo mkdir -p /opt/dprihkat-api
sudo chown dprihkat:dprihkat /opt/dprihkat-api
```

#### C. Créer le fichier de service systemd

```bash
sudo nano /etc/systemd/system/dprihkat-api.service
```

Contenu:

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
Environment="SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/dprihkat_db"
Environment="SPRING_DATASOURCE_USERNAME=dprihkat_user"
Environment="SPRING_DATASOURCE_PASSWORD=VOTRE_MOT_DE_PASSE_ICI"
Environment="JWT_SECRET=VOTRE_SECRET_JWT_TRES_LONG_ET_SECURISE_ICI"

[Install]
WantedBy=multi-user.target
```

#### D. Déployer le JAR

Depuis votre machine locale:

```bash
cd /home/amateur/IdeaProjects/DPRIHKAT_BACKEND_IMPOT

# Compiler le projet (déjà fait)
# mvn clean package -DskipTests

# Utiliser le script de déploiement
./deploy/vps/deploy.sh user@178.170.94.25
```

OU manuellement:

```bash
# Transférer le JAR
scp target/impots-0.0.1-SNAPSHOT.jar user@178.170.94.25:~/

# Sur le VPS
ssh user@178.170.94.25
sudo mv ~/impots-0.0.1-SNAPSHOT.jar /opt/dprihkat-api/
sudo chown dprihkat:dprihkat /opt/dprihkat-api/impots-0.0.1-SNAPSHOT.jar
```

#### E. Démarrer le service

```bash
sudo systemctl daemon-reload
sudo systemctl start dprihkat-api
sudo systemctl enable dprihkat-api
sudo systemctl status dprihkat-api
```

---

### ÉTAPE 3: Configurer le certificat SSL (IMPORTANT)

Votre backend utilise HTTPS sur le port 8443. Vous avez deux options:

#### Option A: Let's Encrypt (RECOMMANDÉ pour la production)

```bash
# Installer Certbot
sudo apt update
sudo apt install certbot

# Arrêter temporairement le backend
sudo systemctl stop dprihkat-api

# Obtenir le certificat
sudo certbot certonly --standalone -d 178.170.94.25.nip.io

# Les certificats seront dans:
# /etc/letsencrypt/live/178.170.94.25.nip.io/fullchain.pem
# /etc/letsencrypt/live/178.170.94.25.nip.io/privkey.pem
```

Puis configurez Spring Boot pour utiliser ces certificats.

#### Option B: Utiliser Nginx comme reverse proxy (RECOMMANDÉ)

Cette option est plus simple et plus flexible:

```bash
# Installer Nginx
sudo apt install nginx

# Créer la configuration
sudo nano /etc/nginx/sites-available/dprihkat-api
```

Contenu:

```nginx
server {
    listen 443 ssl http2;
    server_name 178.170.94.25.nip.io;

    ssl_certificate /etc/letsencrypt/live/178.170.94.25.nip.io/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/178.170.94.25.nip.io/privkey.pem;

    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    ssl_prefer_server_ciphers on;

    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}

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
sudo nginx -t
sudo systemctl reload nginx

# Modifier Spring Boot pour écouter sur HTTP:8080 au lieu de HTTPS:8443
# Dans application.properties:
# server.port=8080
# Supprimer les configurations SSL
```

---

### ÉTAPE 4: Corriger le code frontend

Le frontend ne doit **JAMAIS** définir l'en-tête `Origin`. Le navigateur le fait automatiquement.

#### Rechercher le problème dans votre code frontend

Recherchez dans votre projet frontend (probablement dans un autre répertoire):

```bash
# Rechercher où l'en-tête Origin est défini
grep -r "Origin" --include="*.js" --include="*.jsx" --include="*.ts" --include="*.tsx" /path/to/frontend/

# Rechercher la configuration axios
grep -r "headers.*Origin" --include="*.js" --include="*.jsx" /path/to/frontend/
```

#### Code à corriger

**❌ INCORRECT:**

```javascript
// Ne faites JAMAIS ceci
axios.post(url, data, {
  headers: {
    'Origin': 'https://mchangoapp.vercel.app',  // ❌ À SUPPRIMER
    'Content-Type': 'application/json'
  }
});
```

**✅ CORRECT:**

```javascript
// Le navigateur définit automatiquement Origin
axios.post(url, data, {
  headers: {
    'Content-Type': 'application/json'
  },
  withCredentials: true
});
```

#### Configuration Axios recommandée

Créez un fichier `api/client.js` ou similaire:

```javascript
import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'https://178.170.94.25.nip.io:8443/api',
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
});

// Intercepteur pour ajouter le token
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export default apiClient;
```

Puis utilisez-le:

```javascript
import apiClient from '@/api/client';

const login = async (credentials) => {
  const response = await apiClient.post('/auth/login', credentials);
  return response.data;
};
```

---

### ÉTAPE 5: Redéployer le backend avec les nouvelles configurations CORS

Le backend a déjà été mis à jour avec les bonnes configurations CORS. Vous devez le redéployer:

```bash
cd /home/amateur/IdeaProjects/DPRIHKAT_BACKEND_IMPOT

# Le JAR est déjà compilé avec les nouvelles configurations
# Déployez-le sur le VPS
./deploy/vps/deploy.sh user@178.170.94.25
```

---

### ÉTAPE 6: Tester la connexion

#### Depuis votre machine locale

```bash
cd /home/amateur/IdeaProjects/DPRIHKAT_BACKEND_IMPOT
./deploy/vps/test-connection.sh
```

Tous les tests devraient passer ✓

#### Test manuel avec curl

```bash
# Test OPTIONS (preflight)
curl -X OPTIONS https://178.170.94.25.nip.io:8443/api/auth/login \
  -H "Origin: https://mchangoapp.vercel.app" \
  -H "Access-Control-Request-Method: POST" \
  -v

# Test POST (login)
curl -X POST https://178.170.94.25.nip.io:8443/api/auth/login \
  -H "Content-Type: application/json" \
  -H "Origin: https://mchangoapp.vercel.app" \
  -d '{"login":"agent1","motDePasse":"password"}' \
  -v
```

Vous devriez voir:

```
< HTTP/1.1 200 OK
< Access-Control-Allow-Origin: https://mchangoapp.vercel.app
< Access-Control-Allow-Credentials: true
```

---

## 📋 Checklist complète

- [ ] **Backend VPS**
  - [ ] Service dprihkat-api créé et configuré
  - [ ] JAR déployé dans /opt/dprihkat-api/
  - [ ] Service démarré et actif
  - [ ] Port 8443 en écoute
  - [ ] Pare-feu autorise le port 8443
  - [ ] Certificat SSL configuré (Let's Encrypt ou Nginx)
  - [ ] Test local réussi (curl depuis le VPS)

- [ ] **Backend Code**
  - [x] Configuration CORS mise à jour (CorsConfig.java)
  - [x] Configuration CORS mise à jour (SecurityConfig.java)
  - [x] JAR compilé avec les nouvelles configurations
  - [ ] JAR déployé sur le VPS

- [ ] **Frontend**
  - [ ] En-tête `Origin` supprimé du code
  - [ ] Configuration Axios corrigée
  - [ ] Code redéployé sur Vercel

- [ ] **Tests**
  - [ ] Test de connectivité réussi (test-connection.sh)
  - [ ] Test CORS preflight réussi
  - [ ] Test login depuis le frontend réussi

---

## 🛠️ Commandes utiles

### Logs du backend

```bash
# Temps réel
ssh user@178.170.94.25 'sudo journalctl -u dprihkat-api -f'

# Dernières erreurs
ssh user@178.170.94.25 'sudo journalctl -u dprihkat-api -p err -n 50'
```

### Redémarrer le service

```bash
ssh user@178.170.94.25 'sudo systemctl restart dprihkat-api'
```

### Vérifier le statut

```bash
ssh user@178.170.94.25 'sudo systemctl status dprihkat-api'
```

---

## 📚 Documentation

- **Configuration CORS backend**: `docs/fix_cors_frontend.md`
- **Diagnostic connexion**: `docs/diagnostic_connexion_timeout.md`
- **Déploiement VPS**: `deploy/vps/README_VPS_DEPLOY.md`

---

## 🆘 Support

Si les problèmes persistent après avoir suivi toutes ces étapes:

1. Vérifiez les logs du backend: `sudo journalctl -u dprihkat-api -n 100`
2. Vérifiez les logs Nginx (si utilisé): `sudo tail -f /var/log/nginx/error.log`
3. Testez la connexion réseau: `./deploy/vps/test-connection.sh`
4. Vérifiez la console du navigateur pour d'autres erreurs
