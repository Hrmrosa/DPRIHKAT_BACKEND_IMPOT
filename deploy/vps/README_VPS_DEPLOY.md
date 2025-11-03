    x²# Déploiement sur un VPS (sans Docker)

Ce guide installe PostgreSQL + PostGIS sur le serveur, configure l'application Spring Boot en service systemd et la démarre.

Prérequis: Ubuntu 22.04/24.04 (ou Debian), accès sudo.

---

## 1) Installer Java 17
```bash
sudo apt-get update
sudo apt-get install -y openjdk-17-jre-headless
java -version
```

## 2) Installer PostgreSQL 17 + PostGIS
> Si vous avez déjà PostgreSQL installé, adaptez les commandes (version, etc.).

Ajouter le dépôt PGDG (pour obtenir PostgreSQL 17):
```bash
sudo apt-get install -y curl ca-certificates gnupg lsb-release
curl -fsSL https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo gpg --dearmor -o /etc/apt/trusted.gpg.d/postgresql.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/trusted.gpg.d/postgresql.gpg] http://apt.postgresql.org/pub/repos/apt $(lsb_release -cs)-pgdg main" | sudo tee /etc/apt/sources.list.d/pgdg.list
sudo apt-get update
```
Installer PostgreSQL 17 + PostGIS:
```bash
sudo apt-get install -y postgresql-17 postgresql-17-postgis-3 postgis
```

Créer un utilisateur et une base applicative:
```bash
sudo -u postgres psql -c "CREATE USER dpri WITH PASSWORD 'ChangeMeStrong!';"
sudo -u postgres createdb -O dpri dpri_impots
```

Activer PostGIS dans la base:
```bash
sudo -u postgres psql -d dpri_impots -c "CREATE EXTENSION IF NOT EXISTS postgis;"
```

## 3) Builder l'application
Sur votre machine locale (ou sur le VPS si vous avez le code):
```bash
mvn -B -DskipTests package
```
Le JAR se trouve dans `target/impots-0.0.1-SNAPSHOT.jar`.

## 4) Déployer les fichiers sur le VPS
Copiez les fichiers sur le serveur (depuis votre machine locale):
```bash
scp target/impots-0.0.1-SNAPSHOT.jar <user>@<server>:/tmp/app.jar
scp deploy/vps/systemd/dpri-impots.service <user>@<server>:/tmp/dpri-impots.service
scp deploy/vps/env/dpri-impots.env <user>@<server>:/tmp/dpri-impots.env
```

Sur le VPS, préparez l'environnement d'exécution:
```bash
sudo useradd --system --no-create-home --shell /usr/sbin/nologin dpri || true
sudo mkdir -p /opt/dpri-impots
sudo mkdir -p /etc/dpri-impots
sudo mv /tmp/app.jar /opt/dpri-impots/app.jar
sudo mv /tmp/dpri-impots.env /etc/dpri-impots/env
sudo chmod 600 /etc/dpri-impots/env
sudo chown -R dpri:dpri /opt/dpri-impots
sudo mv /tmp/dpri-impots.service /etc/systemd/system/dpri-impots.service
```

## 5) Configurer et démarrer le service systemd
```bash
sudo systemctl daemon-reload
sudo systemctl enable --now dpri-impots
sudo journalctl -u dpri-impots -f
```

## 6) Variables d'environnement (édition de /etc/dpri-impots/env)
Contenu minimal:
```
PORT=8080
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/dpri_impots?sslmode=disable
SPRING_DATASOURCE_USERNAME=dpri
SPRING_DATASOURCE_PASSWORD=ChangeMeStrong!
JWT_SECRET=ChangeThisJWTSecretToAStrongRandomValue
```

Note:
- L'application lit ces variables via `application.properties`.
- Flyway exécutera automatiquement `db/migration/V1__enable_postgis.sql`. Comme PostGIS est installé, l'extension sera activée si nécessaire.

## 7) Configuration SSL (REQUIS pour HTTPS)

Si votre frontend utilise HTTPS (comme Vercel), vous devez configurer SSL. Deux options:

### Option A: SSL dans Spring Boot (SIMPLE - RECOMMANDÉ)

```bash
# Transférer le script depuis votre machine locale
scp deploy/vps/setup-ssl-springboot.sh root@<server>:/tmp/

# Sur le VPS, exécuter le script
sudo bash /tmp/setup-ssl-springboot.sh
```

Ce script configure SSL directement dans Spring Boot/Tomcat sur le port 8443.

**Documentation:** `deploy/vps/SETUP_SSL_LETSENCRYPT_SPRINGBOOT.md`

### Option B: Nginx comme reverse proxy

```bash
# Transférer le script depuis votre machine locale
scp deploy/vps/setup-nginx-quick.sh root@<server>:/tmp/

# Sur le VPS, exécuter le script
sudo bash /tmp/setup-nginx-quick.sh
```

Ce script configure Nginx avec SSL sur le port 8443 qui redirige vers Spring Boot sur le port 8080.

**Documentation:** `deploy/vps/SETUP_NGINX_SSL.md`

**Note:** Choisissez UNE seule option. Les deux ne peuvent pas fonctionner en même temps sur le port 8443.

## 8) Mise à jour (nouvelles versions)
```bash
# Sur votre machine locale
mvn -B -DskipTests package
scp target/impots-0.0.1-SNAPSHOT.jar <user>@<server>:/tmp/app.jar
# Sur le serveur
sudo systemctl stop dpri-impots
sudo mv /tmp/app.jar /opt/dpri-impots/app.jar
sudo chown dpri:dpri /opt/dpri-impots/app.jar
sudo systemctl start dpri-impots
sudo journalctl -u dpri-impots -f
```

## 9) Problèmes fréquents
- `type geometry does not exist`: vérifiez que le paquet PostGIS est installé et que l'extension est activée dans la base `dpri_impots`.
- `Flyway unsupported database`: déjà corrigé dans le `pom.xml` par la dépendance `flyway-database-postgresql`.
- L'appli n'écoute pas: vérifiez `/etc/dpri-impots/env` et le journal `journalctl -u dpri-impots`.

---

Vous pouvez personnaliser la mémoire Java dans le service systemd (Xms/Xmx). Ajustez les mots de passe et secrets avant prod.
