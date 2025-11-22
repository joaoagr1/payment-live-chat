# Payment Live Chat

Real-time chat application with payment integration using PIX (Mercado Pago).

## Technologies

- **Backend:** Spring Boot 3.4.0, Java 21
- **Authentication:** Keycloak (OAuth2/OpenID Connect)
- **Real-time:** WebSocket + STOMP
- **Message Broker:** RabbitMQ
- **Database:** PostgreSQL
- **Payment Gateway:** Mercado Pago (PIX)

## Features

- ✅ User authentication via Keycloak
- ✅ Friend system (send/accept/reject invitations)
- ✅ Real-time 1:1 chat with WebSocket
- ✅ Message queuing with RabbitMQ
- ✅ PIX payment requests between friends
- ✅ Payment status tracking via webhooks

## Project Structure

```
src/main/java/com/payment_chat/project/
├── config/           # Security, WebSocket, RabbitMQ configs
├── controller/       # REST + WebSocket endpoints
├── dto/              # Data Transfer Objects
├── model/            # JPA entities
├── repository/       # Database repositories
└── service/          # Business logic
```

## Getting Started

### 1. Start Infrastructure

```bash
docker-compose up -d
```

This starts:
- PostgreSQL (port 5432)
- Keycloak (port 8081)
- RabbitMQ (port 5672, management UI 15672)

### 2. Configure Keycloak

**Option A - Import Realm (Recommended):**
1. Access http://localhost:8081 (admin/admin)
2. Click dropdown → "Create Realm"
3. Click "Browse" and select `keycloak-realm-export.json`
4. Click "Create"

**Option B - Manual Setup:**
1. Create realm: `payment-chat`
2. Create client: `payment-chat-app`
   - Client ID: `payment-chat-app`
   - Access Type: `public`
   - Valid Redirect URIs: `http://localhost:4200/*`
   - Web Origins: `http://localhost:4200`

**Test Users (from import):**
- Username: `testuser` / Password: `test123`
- Username: `testuser2` / Password: `test123`

### 3. Configure Mercado Pago

1. Get your access token from [Mercado Pago Developers](https://www.mercadopago.com/developers)
2. Copy `.env.example` to `.env`
3. Add your token: `MERCADOPAGO_ACCESS_TOKEN=YOUR_TOKEN`

### 4. Run Application

```bash
./mvnw spring-boot:run
```

Backend runs on http://localhost:8080

## API Endpoints

### Users
- `GET /api/users/me` - Get current user
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/username/{username}` - Get user by username

### Friendships
- `POST /api/friendships/request` - Send friend request
- `POST /api/friendships/{id}/accept` - Accept request
- `POST /api/friendships/{id}/reject` - Reject request
- `GET /api/friendships/friends` - List friends
- `GET /api/friendships/requests/pending` - List pending requests

### Messages
- `POST /api/messages` - Send message
- `GET /api/messages/conversation/{userId}` - Get conversation
- `POST /api/messages/{messageId}/read` - Mark as read

### Payments
- `POST /api/payments` - Create payment request
- `GET /api/payments` - List user payments
- `POST /api/payments/webhook` - Mercado Pago webhook (public)

### WebSocket

**Connect:** `/ws` (with SockJS)

**Subscribe:**
- `/user/queue/messages` - Receive messages
- `/user/queue/payments` - Receive payment updates

**Send:**
- `/app/chat.send` - Send message
- `/app/chat.connect` - User online
- `/app/chat.disconnect` - User offline

## Database Schema

### users
- id, keycloak_id, username, email, display_name, avatar_url, online, created_at

### friendships
- id, requester_id, addressee_id, status, created_at, accepted_at

### messages
- id, sender_id, recipient_id, content, type, payment_id, sent_at, read_at

### payments
- id, requester_id, payer_id, amount, description, status, method
- mercado_pago_payment_id, pix_qr_code, pix_qr_code_base64, pix_copy_paste
- created_at, expires_at, paid_at

## Environment Variables

See `.env.example` for required environment variables.

## Development Tools

- **Keycloak Admin:** http://localhost:8081 (admin/admin)
- **RabbitMQ Management:** http://localhost:15672 (guest/guest)
- **PostgreSQL:** localhost:5432 (postgres/postgres)

## Next Steps

1. Implement Angular frontend
2. Add push notifications
3. Implement group chats
4. Add file/image sharing
5. Implement read receipts
6. Add message search

## License

MIT
