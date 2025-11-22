# Keycloak Testing Guide 🔐

Complete guide to test your Keycloak integration locally.

## Prerequisites

### 1. Install Node.js and npm
```bash
# Check if already installed
node -v
npm -v

# If not installed (Ubuntu/Debian)
curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
sudo apt-get install -y nodejs
```

### 2. Install Angular CLI
```bash
npm install -g @angular/cli
```

## Setup Instructions

### Step 1: Start Backend Infrastructure

```bash
cd /home/joao/Projects/payment-live-chat/project
docker-compose up -d
```

**Wait ~30 seconds for services to start**, then verify:
- PostgreSQL: `docker ps | grep postgres`
- Keycloak: `docker ps | grep keycloak`
- RabbitMQ: `docker ps | grep rabbitmq`

### Step 2: Import Keycloak Realm

1. **Access Keycloak Admin Console:**
   - URL: http://localhost:8081
   - Username: `admin`
   - Password: `admin`

2. **Import Realm:**
   - Click the dropdown in top-left (says "Keycloak" or "Master")
   - Click "Create Realm"
   - Click "Browse" button
   - Select: `/home/joao/Projects/payment-live-chat/project/keycloak-realm-export.json`
   - Click "Create"

3. **Verify Realm Created:**
   - You should see "payment-chat" realm in the dropdown
   - Switch to "payment-chat" realm
   - Go to "Clients" → You should see `payment-chat-app`
   - Go to "Users" → You should see `testuser` and `testuser2`

### Step 3: Install Frontend Dependencies

```bash
cd /home/joao/Projects/payment-live-chat/frontend
npm install
```

This will install:
- Angular 17
- keycloak-angular
- keycloak-js
- All other dependencies

### Step 4: Start Angular Application

```bash
npm start
```

Application will start on **http://localhost:4200**

## Testing Scenarios

### Test 1: Login Page ✅

1. Open browser: http://localhost:4200
2. You should be redirected to http://localhost:4200/login
3. Verify you see:
   - "Payment Chat" title
   - "Login with Keycloak" button
   - "Create Account" button
   - Test accounts info (testuser/test123)

### Test 2: Keycloak Login Flow ✅

1. Click "🔐 Login with Keycloak" button
2. **Should redirect to Keycloak login page:**
   - URL: http://localhost:8081/realms/payment-chat/...
   - Keycloak login form appears

3. **Login with test user:**
   - Username: `testuser`
   - Password: `test123`
   - Click "Sign In"

4. **Should redirect back to Angular:**
   - URL: http://localhost:4200/home
   - You see welcome message with user info
   - Top-right shows username and logout button

### Test 3: Protected Route (Auth Guard) ✅

1. Logout (click "Logout" button)
2. Try accessing: http://localhost:4200/home directly
3. **Expected behavior:**
   - Should redirect to Keycloak login
   - After login, redirects back to /home

### Test 4: User Profile Display ✅

After logging in at http://localhost:4200/home, verify:
- ✅ Welcome message shows first name
- ✅ Profile section shows:
  - Username
  - Email
  - First Name
  - Last Name
  - User ID (Keycloak UUID)
- ✅ Access Token section shows JWT token
- ✅ Success checklist is displayed

### Test 5: Registration Flow ✅

1. Go to http://localhost:4200/login
2. Click "✍️ Create Account" button
3. **Should redirect to Keycloak registration:**
   - URL: http://localhost:8081/realms/payment-chat/.../register
   - Registration form appears

4. **Fill the form:**
   - Email: your@email.com
   - First name: John
   - Last name: Doe
   - Username: johndoe
   - Password: test123
   - Confirm Password: test123

5. Click "Register"
6. **Should redirect to app home:**
   - Logged in automatically
   - Profile shows your new account info

### Test 6: Multi-User Testing ✅

**Test friend requests between users:**

1. **Window 1 - testuser:**
   - Login as `testuser/test123`
   - Note the User ID in profile

2. **Window 2 - testuser2 (Incognito):**
   - Open incognito/private window
   - Login as `testuser2/test123`
   - Note the User ID in profile

3. **Both users authenticated independently** ✅

### Test 7: JWT Token Verification ✅

1. Login to http://localhost:4200/home
2. Open Browser DevTools (F12)
3. Go to **Application/Storage** → **Local Storage**
4. Look for Keycloak token entries
5. In home page, copy the Access Token shown
6. Go to https://jwt.io
7. Paste token in "Encoded" section
8. **Verify JWT contains:**
   - `sub`: User ID
   - `preferred_username`: Username
   - `email`: Email
   - `realm_access.roles`: User roles
   - `iss`: http://localhost:8081/realms/payment-chat

### Test 8: Token Refresh ✅

1. Login and stay on home page
2. Wait 5 minutes (default token lifetime)
3. **Keycloak should auto-refresh token**
4. Application continues working without re-login

### Test 9: Logout Flow ✅

1. Login to http://localhost:4200/home
2. Click "Logout" button
3. **Expected behavior:**
   - Redirects to Keycloak logout
   - Then redirects to http://localhost:4200
   - Keycloak session terminated
   - Trying to access /home redirects to login

## Common Issues & Solutions

### Issue 1: Keycloak Not Loading
**Problem:** http://localhost:8081 not accessible

**Solution:**
```bash
# Check if Keycloak container is running
docker ps | grep keycloak

# Check logs
docker logs payment-chat-keycloak

# Restart if needed
docker-compose restart keycloak
```

### Issue 2: CORS Errors
**Problem:** Console shows CORS errors

**Solution:**
- Verify Web Origins in Keycloak:
  - Go to Clients → payment-chat-app → Settings
  - Web Origins should have: `http://localhost:4200`
  - Save changes

### Issue 3: Invalid Redirect URI
**Problem:** Keycloak shows "Invalid redirect_uri"

**Solution:**
- Go to Clients → payment-chat-app → Settings
- Valid Redirect URIs should have:
  - `http://localhost:4200/*`
  - `http://localhost:4200`
- Save changes

### Issue 4: Angular Won't Start
**Problem:** `npm start` fails

**Solution:**
```bash
# Clear node_modules and reinstall
rm -rf node_modules package-lock.json
npm install

# Try again
npm start
```

### Issue 5: Test Users Not Found
**Problem:** Can't login with testuser/test123

**Solution:**
- Verify realm import was successful
- Go to Users in Keycloak admin
- If users missing, manually create:
  - Username: testuser
  - Email: test@test.com
  - First Name: Test
  - Last Name: User
  - Go to Credentials tab → Set Password: test123 (temporary: OFF)

## Verification Checklist

Use this checklist to verify everything works:

- [ ] Keycloak accessible at http://localhost:8081
- [ ] Realm "payment-chat" exists and is active
- [ ] Client "payment-chat-app" configured correctly
- [ ] Test users created (testuser, testuser2)
- [ ] Angular app starts without errors
- [ ] Login redirects to Keycloak
- [ ] Can login with testuser/test123
- [ ] After login, redirects to /home
- [ ] Profile info displays correctly
- [ ] JWT token visible and valid
- [ ] Protected routes require authentication
- [ ] Logout works correctly
- [ ] Can register new user
- [ ] Can login with newly registered user

## Next Steps After Successful Testing

Once Keycloak is working:

1. **Test Backend Integration:**
   - Start Spring Boot backend
   - Make authenticated API calls
   - Verify JWT validation works

2. **Implement Chat Features:**
   - Friend requests
   - Real-time messaging
   - Payment requests

3. **Add More Components:**
   - Friends list
   - Chat interface
   - Payment interface

## Useful Commands

```bash
# View Keycloak logs
docker logs -f payment-chat-keycloak

# Restart all services
docker-compose restart

# Stop all services
docker-compose down

# Start Angular with specific port
ng serve --port 4200

# Build Angular for production
ng build --configuration production
```

## Browser DevTools Debugging

**Console Tab:**
- Check for errors
- Look for Keycloak initialization messages

**Network Tab:**
- Monitor OAuth2 flow
- Check token exchange requests
- Verify API calls include Authorization header

**Application Tab:**
- Check Local Storage for Keycloak data
- View session information

## Success Indicators

You know it's working when you see:

✅ Keycloak login page appears when clicking login
✅ After authentication, redirected to Angular app
✅ User profile shows correct information
✅ JWT token is visible and contains user data
✅ Logout clears session and redirects
✅ Protected routes require authentication
✅ Can register new users successfully
✅ No CORS errors in console
✅ Token auto-refreshes on expiration

---

**Need help?** Check the logs:
- Angular: Browser console (F12)
- Keycloak: `docker logs payment-chat-keycloak`
- Backend: `./mvnw spring-boot:run` output
