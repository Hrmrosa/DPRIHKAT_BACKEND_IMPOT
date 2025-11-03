# Correction de l'erreur CORS "Refused to set unsafe header 'Origin'"

## Problème

L'erreur `Refused to set unsafe header "Origin"` se produit lorsque votre code frontend tente de définir manuellement l'en-tête `Origin` dans les requêtes HTTP. **Ceci est interdit par les navigateurs** pour des raisons de sécurité.

## Solution côté Frontend

### ❌ Code INCORRECT (à éviter)

```javascript
// NE JAMAIS FAIRE CECI
axios.post('https://178.170.94.25.nip.io:8443/api/auth/login', data, {
  headers: {
    'Origin': 'https://mchangoapp.vercel.app',  // ❌ INTERDIT
    'Content-Type': 'application/json'
  },
  withCredentials: true
});

// OU
fetch('https://178.170.94.25.nip.io:8443/api/auth/login', {
  headers: {
    'Origin': 'https://mchangoapp.vercel.app',  // ❌ INTERDIT
    'Content-Type': 'application/json'
  },
  credentials: 'include'
});
```

### ✅ Code CORRECT

```javascript
// Le navigateur définit automatiquement l'en-tête Origin
axios.post('https://178.170.94.25.nip.io:8443/api/auth/login', data, {
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}` // Si nécessaire
  },
  withCredentials: true
});

// OU avec fetch
fetch('https://178.170.94.25.nip.io:8443/api/auth/login', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}` // Si nécessaire
  },
  credentials: 'include',
  body: JSON.stringify(data)
});
```

## Configuration Axios recommandée

### Configuration globale Axios

```javascript
// api/client.js ou config/axios.js
import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'https://178.170.94.25.nip.io:8443/api',
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
});

// Intercepteur pour ajouter le token JWT
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Intercepteur pour gérer les erreurs
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Rediriger vers la page de connexion
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default apiClient;
```

### Utilisation dans vos composants

```javascript
// pages/login.js
import apiClient from '@/api/client';

const login = async (credentials) => {
  try {
    const response = await apiClient.post('/auth/login', credentials);
    const { token, utilisateur } = response.data;
    
    // Sauvegarder le token
    localStorage.setItem('token', token);
    
    return { token, utilisateur };
  } catch (error) {
    console.error('Erreur de connexion:', error);
    throw error;
  }
};
```

## En-têtes à NE JAMAIS définir manuellement

Ces en-têtes sont gérés automatiquement par le navigateur et ne doivent **JAMAIS** être définis manuellement:

- ❌ `Origin`
- ❌ `Referer`
- ❌ `Host`
- ❌ `Connection`
- ❌ `Access-Control-*` (tous les en-têtes CORS)
- ❌ `Cookie` (utilisez `withCredentials: true` à la place)

## En-têtes que vous POUVEZ définir

- ✅ `Content-Type`
- ✅ `Accept`
- ✅ `Authorization`
- ✅ `X-Requested-With`
- ✅ En-têtes personnalisés (ex: `X-Custom-Header`)

## Vérification de votre code

Recherchez dans votre code frontend:

```bash
# Rechercher les occurrences de 'Origin' dans les headers
grep -r "Origin" --include="*.js" --include="*.jsx" --include="*.ts" --include="*.tsx"

# Rechercher les configurations axios suspectes
grep -r "headers.*Origin" --include="*.js" --include="*.jsx" --include="*.ts" --include="*.tsx"
```

## Configuration CORS Backend (déjà corrigée)

Le backend a été mis à jour pour:
- ✅ Accepter toutes les origines Vercel (`https://*.vercel.app`)
- ✅ Autoriser les credentials (`withCredentials: true`)
- ✅ Gérer correctement les requêtes preflight OPTIONS
- ✅ Cacher les requêtes preflight pendant 1 heure

## Test après correction

1. **Supprimez tout en-tête `Origin` de votre code frontend**
2. **Redéployez votre backend** avec les nouvelles configurations CORS
3. **Testez la connexion** depuis https://mchangoapp.vercel.app

### Commandes de test

```bash
# Tester depuis le terminal
curl -X POST https://178.170.94.25.nip.io:8443/api/auth/login \
  -H "Content-Type: application/json" \
  -H "Origin: https://mchangoapp.vercel.app" \
  -d '{"login":"agent1","motDePasse":"password"}' \
  -v

# Vérifier les en-têtes CORS dans la réponse
# Vous devriez voir:
# Access-Control-Allow-Origin: https://mchangoapp.vercel.app
# Access-Control-Allow-Credentials: true
```

## Problèmes courants supplémentaires

### 1. Certificat SSL auto-signé

Si vous utilisez un certificat auto-signé sur `178.170.94.25.nip.io`, les navigateurs modernes bloqueront les requêtes. Solutions:

- Utilisez Let's Encrypt pour un certificat valide
- Ou utilisez HTTP en développement (non recommandé en production)

### 2. Mixed Content

Si votre frontend est en HTTPS (Vercel) et votre backend en HTTP, les navigateurs bloqueront les requêtes. Votre backend **DOIT** être en HTTPS.

### 3. Cookies et SameSite

Si vous utilisez des cookies pour l'authentification:

```javascript
// Backend (Spring Boot)
// Dans votre contrôleur de login
Cookie cookie = new Cookie("token", jwtToken);
cookie.setHttpOnly(true);
cookie.setSecure(true); // HTTPS uniquement
cookie.setSameSite("None"); // Requis pour cross-origin
cookie.setPath("/");
response.addCookie(cookie);
```

## Support

Si le problème persiste après ces corrections:
1. Vérifiez la console du navigateur pour d'autres erreurs
2. Vérifiez les logs du backend Spring Boot
3. Utilisez les outils de développement du navigateur (onglet Network) pour inspecter les en-têtes
