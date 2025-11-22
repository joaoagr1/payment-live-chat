# Payment Chat - Frontend

Angular 17 application with Keycloak authentication integration.

## Features

- 🔐 Keycloak OAuth2/OIDC authentication
- 🎨 Beautiful, modern UI
- 🛡️ Protected routes with Auth Guard
- 👤 User profile display
- 🔑 JWT token management
- ✅ Login & Registration flows

## Project Structure

```
src/
├── app/
│   ├── auth/
│   │   ├── login/           # Login component
│   │   └── register/        # Registration component
│   ├── components/
│   │   └── home/            # Home/Dashboard (protected)
│   ├── guards/
│   │   └── auth.guard.ts    # Route protection
│   ├── services/
│   │   └── auth.service.ts  # Keycloak integration
│   ├── app.config.ts        # App configuration
│   └── app.routes.ts        # Route definitions
├── environments/
│   └── environment.ts       # Environment config
└── assets/
    └── silent-check-sso.html
```

## Quick Start

See `QUICK_START.md` for fastest setup.

## Installation

```bash
# Install dependencies
npm install

# Start development server
npm start

# Build for production
npm build
```

## Routes

| Route | Component | Protected | Description |
|-------|-----------|-----------|-------------|
| `/` | - | No | Redirects to /home |
| `/login` | LoginComponent | No | Login page |
| `/register` | RegisterComponent | No | Registration page |
| `/home` | HomeComponent | Yes | User dashboard |

## Keycloak Configuration

Located in `src/environments/environment.ts`:

```typescript
keycloak: {
  url: 'http://localhost:8081',
  realm: 'payment-chat',
  clientId: 'payment-chat-app'
}
```

## Components

### LoginComponent
- Beautiful landing page
- Login with Keycloak button
- Registration link
- Shows test accounts

### RegisterComponent
- Registration flow via Keycloak
- Returns to login after creation

### HomeComponent (Protected)
- User profile display
- JWT token viewer
- Logout functionality
- Success indicators

## Services

### AuthService
```typescript
login()              // Redirect to Keycloak login
register()           // Redirect to Keycloak registration
logout()             // Logout and clear session
isLoggedIn()         // Check authentication status
getProfile()         // Get user profile
getToken()           // Get JWT access token
getUsername()        // Get username
hasRole(role)        // Check user role
```

## Auth Guard

Protects routes requiring authentication. Automatically redirects to Keycloak login if user is not authenticated.

## Development

```bash
# Start dev server
ng serve

# Run on different port
ng serve --port 4201

# Build
ng build

# Build for production
ng build --configuration production
```

## Testing Keycloak

Complete testing guide: `KEYCLOAK_TESTING_GUIDE.md`

Quick tests:
1. Login with testuser/test123
2. Check protected route /home
3. View JWT token
4. Test logout
5. Register new user

## Dependencies

- Angular 17
- keycloak-angular 15
- keycloak-js 23
- RxJS 7

## Environment Variables

Edit `src/environments/environment.ts`:

```typescript
export const environment = {
  production: false,
  keycloak: {
    url: 'http://localhost:8081',
    realm: 'payment-chat',
    clientId: 'payment-chat-app'
  },
  apiUrl: 'http://localhost:8080/api'
};
```

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Troubleshooting

See `KEYCLOAK_TESTING_GUIDE.md` for detailed troubleshooting.

Common issues:
- **CORS errors**: Check Keycloak Web Origins
- **Invalid redirect**: Check Keycloak Valid Redirect URIs
- **Token errors**: Check Keycloak realm configuration

## Next Steps

After Keycloak is working:
1. Integrate with Spring Boot backend
2. Add chat interface
3. Implement friend system
4. Add payment features
5. WebSocket real-time updates

## License

MIT
