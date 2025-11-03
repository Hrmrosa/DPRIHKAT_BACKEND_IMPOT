# Configuration Nginx + SSL pour DPRIHKAT API

## Situation actuelle

✅ **Backend fonctionne** sur `http://localhost:8080`  
❌ **Frontend essaie** `https://45.136.70.97.nip.io:8443`

**Solution:** Configurer Nginx comme reverse proxy pour gérer le SSL.

---

## Option 1: Certificat SSL auto-signé (RAPIDE - pour test)

### 1. Installer Nginx

```bash
ssh root@45.136.70.97

sudo apt update
sudo apt install -y nginx
```

### 2. Créer un certificat auto-signé

```bash
sudo mkdir -p /etc/nginx/ssl
sudo openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
  -keyout /etc/nginx/ssl/dprihkat.key \
  -out /etc/nginx/ssl/dprihkat.crt \
  -subj "/C=CD/ST=Kinshasa/L=Kinshasa/O=DPRIHKAT/CN=45.136.70.97.nip.io"
```

### 3. Copier la configuration Nginx

**Depuis votre machine locale:**

```bash
scp deploy/vps/nginx/dprihkat-api.conf root@45.136.70.97:/tmp/
```

**Sur le VPS:**

```bash
# Modifier la config pour utiliser le certificat auto-signé
sudo nano /tmp/dprihkat-api.conf
```

Commentez les lignes Let's Encrypt et décommentez les lignes certificat auto-signé:

```nginx
# Option 2: Certificat auto-signé
ssl_certificate /etc/nginx/ssl/dprihkat.crt;
ssl_certificate_key /etc/nginx/ssl/dprihkat.key;
```

Puis:

```bash
sudo mv /tmp/dprihkat-api.conf /etc/nginx/sites-available/dprihkat-api
sudo ln -s /etc/nginx/sites-available/dprihkat-api /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl reload nginx
```

### 4. Ouvrir le port 8443

```bash
sudo ufw allow 8443/tcp
sudo ufw status
```

### 5. Tester

```bash
# Depuis le VPS
curl -k https://localhost:8443/api/auth/login -v

# Depuis votre machine
curl -k https://45.136.70.97.nip.io:8443/api/auth/login -v
```

**Note:** Le `-k` ignore les erreurs de certificat (normal pour un certificat auto-signé).

---

## Option 2: Let's Encrypt (RECOMMANDÉ pour production)

### 1. Installer Nginx et Certbot

```bash
ssh root@45.136.70.97

sudo apt update
sudo apt install -y nginx certbot python3-certbot-nginx
```

### 2. Obtenir un certificat SSL

```bash
# Arrêter Nginx temporairement
sudo systemctl stop nginx

# Obtenir le certificat
sudo certbot certonly --standalone -d 45.136.70.97.nip.io

# Redémarrer Nginx
sudo systemctl start nginx
```

**Important:** Notez le chemin des certificats (généralement `/etc/letsencrypt/live/45.136.70.97.nip.io/`).

### 3. Copier et activer la configuration Nginx

**Depuis votre machine locale:**

```bash
scp deploy/vps/nginx/dprihkat-api.conf root@45.136.70.97:/tmp/
```

**Sur le VPS:**

```bash
sudo mv /tmp/dprihkat-api.conf /etc/nginx/sites-available/dprihkat-api
sudo ln -s /etc/nginx/sites-available/dprihkat-api /etc/nginx/sites-enabled/

# Tester la configuration
sudo nginx -t

# Recharger Nginx
sudo systemctl reload nginx
```

### 4. Ouvrir les ports

```bash
sudo ufw allow 80/tcp
sudo ufw allow 8443/tcp
sudo ufw status
```

### 5. Configurer le renouvellement automatique

```bash
# Tester le renouvellement
sudo certbot renew --dry-run

# Le renouvellement automatique est déjà configuré via systemd timer
sudo systemctl list-timers | grep certbot
```

### 6. Tester

```bash
# Depuis le VPS
curl https://localhost:8443/api/auth/login -v

# Depuis votre machine
curl https://45.136.70.97.nip.io:8443/api/auth/login -v
```

---

## Vérification finale

### 1. Vérifier que Nginx écoute sur le port 8443

```bash
sudo netstat -tlnp | grep 8443
# OU
sudo ss -tlnp | grep 8443
```

Vous devriez voir:
```
tcp  0  0  0.0.0.0:8443  0.0.0.0:*  LISTEN  12345/nginx
```

### 2. Vérifier que le backend écoute sur le port 8080

```bash
sudo netstat -tlnp | grep 8080
```

Vous devriez voir:
```
tcp6  0  0  :::8080  :::*  LISTEN  67890/java
```

### 3. Tester le flux complet

```bash
# Test OPTIONS (CORS preflight)
curl -X OPTIONS https://45.136.70.97.nip.io:8443/api/auth/login \
  -H "Origin: https://dpri-impot-frontend.vercel.app" \
  -H "Access-Control-Request-Method: POST" \
  -v

# Test POST (login)
curl -X POST https://45.136.70.97.nip.io:8443/api/auth/login \
  -H "Content-Type: application/json" \
  -H "Origin: https://dpri-impot-frontend.vercel.app" \
  -d '{"login":"agent1","motDePasse":"test"}' \
  -v
```

Vous devriez voir:
```
< HTTP/1.1 200 OK
< Access-Control-Allow-Origin: https://dpri-impot-frontend.vercel.app
< Access-Control-Allow-Credentials: true
```

---

## Dépannage

### Nginx ne démarre pas

```bash
# Voir les logs
sudo journalctl -u nginx -n 50

# Tester la configuration
sudo nginx -t
```

### Le backend n'est pas accessible via Nginx

```bash
# Vérifier que le backend répond localement
curl http://localhost:8080/api/auth/login -v

# Voir les logs Nginx
sudo tail -f /var/log/nginx/dprihkat-api-error.log

# Voir les logs du backend
sudo journalctl -u dpri-impots -f
```

### Erreur de certificat

**Pour Let's Encrypt:**
```bash
# Vérifier les certificats
sudo certbot certificates

# Renouveler manuellement
sudo certbot renew
```

**Pour certificat auto-signé:**
- Le navigateur affichera un avertissement (normal)
- Utilisez `-k` avec curl pour ignorer l'erreur

### Port 8443 non accessible de l'extérieur

```bash
# Vérifier le pare-feu
sudo ufw status verbose

# Vérifier que Nginx écoute sur toutes les interfaces
sudo netstat -tlnp | grep 8443
# Doit montrer 0.0.0.0:8443, PAS 127.0.0.1:8443
```

---

## Commandes utiles

### Redémarrer les services

```bash
# Redémarrer Nginx
sudo systemctl restart nginx

# Redémarrer le backend
sudo systemctl restart dpri-impots

# Redémarrer les deux
sudo systemctl restart nginx dpri-impots
```

### Voir les logs

```bash
# Logs Nginx
sudo tail -f /var/log/nginx/dprihkat-api-access.log
sudo tail -f /var/log/nginx/dprihkat-api-error.log

# Logs backend
sudo journalctl -u dpri-impots -f
```

### Tester la configuration

```bash
# Tester Nginx
sudo nginx -t

# Recharger sans redémarrer
sudo systemctl reload nginx
```

---

## Résumé de l'architecture

```
Frontend (Vercel)
    ↓ HTTPS
    ↓ https://45.136.70.97.nip.io:8443/api/...
    ↓
Nginx (Port 8443 - SSL)
    ↓ HTTP
    ↓ http://localhost:8080/api/...
    ↓
Spring Boot Backend (Port 8080)
    ↓
PostgreSQL (Port 5432)
```

---

## Checklist

- [ ] Nginx installé
- [ ] Certificat SSL configuré (auto-signé ou Let's Encrypt)
- [ ] Configuration Nginx copiée et activée
- [ ] Nginx redémarré: `sudo systemctl reload nginx`
- [ ] Port 8443 ouvert: `sudo ufw allow 8443/tcp`
- [ ] Nginx écoute sur 8443: `sudo netstat -tlnp | grep 8443`
- [ ] Backend écoute sur 8080: `sudo netstat -tlnp | grep 8080`
- [ ] Test local réussi: `curl -k https://localhost:8443/api/auth/login -v`
- [ ] Test externe réussi: `curl -k https://45.136.70.97.nip.io:8443/api/auth/login -v`
- [ ] Test CORS réussi depuis le frontend
