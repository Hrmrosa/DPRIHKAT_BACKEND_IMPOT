# ✅ Configuration Production - Prêt pour le VPS

## 🎯 Modifications Effectuées

### 1. Configuration CORS

**Fichiers modifiés:**
- `src/main/java/com/DPRIHKAT/config/CorsConfig.java`
- `src/main/java/com/DPRIHKAT/config/SecurityConfig.java`

**Changements:**
- ✅ Ajout de `https://mchangoapp.vercel.app` dans les origines autorisées
- ✅ Configuration des headers pour HTTPS
- ✅ Support des credentials (cookies, JWT)

### 2. Configuration Production

**Nouveau fichier créé:**
- `src/main/resources/application-prod.properties`

**Fonctionnalités:**
- ✅ Variables d'environnement pour la base de données
- ✅ Configuration SSL/HTTPS
- ✅ Headers de sécurité
- ✅ Logging optimisé pour la production
- ✅ Désactivation des informations sensibles dans les erreurs

### 3. Documentation

**Fichiers créés:**
- `DEPLOIEMENT_VPS.md` - Guide complet de déploiement
- `build-for-production.sh` - Script de build automatisé
- `CONFIGURATION_PRODUCTION.md` - Ce fichier

## 🔒 Points de Sécurité Vérifiés

### ✅ CORS
- Origine HTTPS autorisée: `https://mchangoapp.vercel.app`
- Headers configurés correctement
- Credentials activés pour JWT

### ✅ HTTPS
- Configuration Nginx avec SSL
- Redirection HTTP → HTTPS
- Headers de sécurité (HSTS, X-Frame-Options, etc.)
- Forward headers pour proxy inverse

### ✅ Base de Données
- Variables d'environnement pour les credentials
- Connexion sécurisée localhost uniquement
- PostGIS activé

### ✅ JWT
- Secret configurable via variable d'environnement
- Expiration configurée (24h pour access, 10 jours pour refresh)

### ✅ Logging
- Logs de production (INFO level)
- Pas d'exposition de stack traces
- Journalisation systemd

## 🚀 Déploiement Rapide

### Étape 1: Build
```bash
./build-for-production.sh
```

### Étape 2: Transfert
```bash
scp target/impots-0.0.1-SNAPSHOT.jar user@votre-vps:/home/user/dpri-api/
```

### Étape 3: Configuration VPS
Suivre le guide complet dans `DEPLOIEMENT_VPS.md`

## 🌐 URLs de Production

### Frontend
- **URL**: https://mchangoapp.vercel.app
- **CORS**: ✅ Configuré

### Backend (à configurer)
- **URL**: https://votre-domaine-api.com
- **SSL**: ⚠️ À configurer avec Certbot
- **Nginx**: ⚠️ À configurer

## 📋 Checklist Avant Déploiement

### Configuration
- [x] CORS configuré avec le domaine Vercel
- [x] HTTPS supporté
- [x] Variables d'environnement définies
- [x] Logging configuré
- [x] Headers de sécurité ajoutés

### VPS (à faire)
- [ ] Java 17 installé
- [ ] PostgreSQL + PostGIS installé
- [ ] Nginx installé
- [ ] Certificat SSL obtenu
- [ ] Base de données créée
- [ ] Service systemd configuré
- [ ] Firewall configuré

### Tests
- [ ] Application démarre correctement
- [ ] Connexion à la base de données OK
- [ ] Endpoints accessibles via HTTPS
- [ ] CORS fonctionne depuis Vercel
- [ ] WebSocket fonctionne
- [ ] JWT authentication fonctionne

## ⚙️ Variables d'Environnement Requises

```bash
# Base de données
DATABASE_URL=jdbc:postgresql://localhost:5432/dpri_impots
DATABASE_USERNAME=dpri_user
DATABASE_PASSWORD=VOTRE_MOT_DE_PASSE_FORT

# JWT (générer avec: openssl rand -base64 64)
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

## 🔧 Configuration Nginx Minimale

```nginx
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
}
```

## 🆘 Dépannage

### Erreur CORS
**Symptôme**: `Access-Control-Allow-Origin` error dans la console

**Solutions**:
1. Vérifier que `https://mchangoapp.vercel.app` est dans la config CORS
2. Vérifier les headers Nginx
3. Vérifier les logs: `sudo journalctl -u dpri-api -f`

### Erreur SSL
**Symptôme**: `Mixed content` ou `SSL certificate` error

**Solutions**:
1. Vérifier le certificat: `sudo certbot certificates`
2. Renouveler si nécessaire: `sudo certbot renew`
3. Vérifier la config Nginx: `sudo nginx -t`

### Application ne démarre pas
**Symptôme**: Service en erreur

**Solutions**:
1. Vérifier les logs: `sudo journalctl -u dpri-api -n 100`
2. Vérifier la connexion DB: `psql -h localhost -U dpri_user -d dpri_impots`
3. Vérifier les variables d'environnement dans le service systemd

## 📞 Support

Pour toute question:
1. Consulter `DEPLOIEMENT_VPS.md` pour le guide complet
2. Vérifier les logs de l'application et Nginx
3. Tester les endpoints avec curl:
   ```bash
   curl -I https://votre-domaine-api.com/api/auth/login
   ```

## ✅ Statut

- **Configuration Backend**: ✅ PRÊT
- **Configuration Frontend**: ✅ PRÊT (Vercel)
- **Déploiement VPS**: ⏳ EN ATTENTE

**Vous pouvez maintenant déployer sur le VPS!** 🚀
