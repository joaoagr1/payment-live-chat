# Quick Start - Keycloak Auth Testing

Get your Keycloak authentication up and running in 5 minutes!

## 🚀 Quick Setup

### 1. Install Dependencies

```bash
# Install Node.js 20 (if not installed)
curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
sudo apt-get install -y nodejs

# Install Angular CLI
npm install -g @angular/cli
```

### 2. Start Backend Services

```bash
cd /home/joao/Projects/payment-live-chat/project
docker-compose up -d
```

Wait 30 seconds for services to start.

### 3. Configure Keycloak

1. Open: http://localhost:8081
2. Login: `admin` / `admin`
3. Click dropdown (top-left) → "Create Realm"
4. Click "Browse" → Select `keycloak-realm-export.json` from project folder
5. Click "Create"

### 4. Start Angular App

```bash
cd /home/joao/Projects/payment-live-chat/frontend
npm install
npm start
```

### 5. Test It!

1. Open: http://localhost:4200
2. Click "🔐 Login with Keycloak"
3. Login with: `testuser` / `test123`
4. You should see your profile page! ✅

## 🧪 What to Test

| Test | Action | Expected Result |
|------|--------|-----------------|
| **Login** | Click "Login with Keycloak" | Redirects to Keycloak, then back to app |
| **Profile** | After login, check /home | Shows your username, email, JWT token |
| **Protected Route** | Try /home without login | Redirects to Keycloak login |
| **Logout** | Click "Logout" button | Logs out and returns to login page |
| **Register** | Click "Create Account" | Keycloak registration form opens |

## 📦 What's Included

- ✅ **Login Component** - Beautiful login page
- ✅ **Register Component** - Account creation flow
- ✅ **Home Component** - Protected page with profile
- ✅ **Auth Guard** - Route protection
- ✅ **Auth Service** - Keycloak integration
- ✅ **JWT Token Display** - See your access token

## 🎯 Test Users

| Username | Password | Email |
|----------|----------|-------|
| testuser | test123 | test@test.com |
| testuser2 | test123 | test2@test.com |

## 🔧 Troubleshooting

**Keycloak not loading?**
```bash
docker logs payment-chat-keycloak
```

**Angular errors?**
```bash
rm -rf node_modules
npm install
```

**Port already in use?**
```bash
# Change port in package.json or use:
ng serve --port 4201
```

## ✅ Success Checklist

- [ ] Docker services running
- [ ] Keycloak realm imported
- [ ] Angular app running on http://localhost:4200
- [ ] Can login with testuser/test123
- [ ] Profile page shows user data
- [ ] JWT token is visible

## 📖 Full Documentation

For detailed testing scenarios and troubleshooting, see:
- `KEYCLOAK_TESTING_GUIDE.md` - Complete testing guide

## 🔗 URLs

| Service | URL | Credentials |
|---------|-----|-------------|
| Angular App | http://localhost:4200 | - |
| Keycloak Admin | http://localhost:8081 | admin / admin |
| Backend API | http://localhost:8080/api | - |
| RabbitMQ Management | http://localhost:15672 | guest / guest |

---

**Ready?** Run these commands:

```bash
# Terminal 1: Start services
cd /home/joao/Projects/payment-live-chat/project
docker-compose up -d

# Terminal 2: Start Angular
cd /home/joao/Projects/payment-live-chat/frontend
npm install && npm start
```

Then open http://localhost:4200 and click Login! 🎉
