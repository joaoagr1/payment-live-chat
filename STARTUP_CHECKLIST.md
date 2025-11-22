# Payment Chat - Startup Checklist

## What You Need to Start (In Order)

### ✅ Already Running:
- [x] Angular Frontend (http://localhost:4200)

### ❌ Missing - Need to Start:

## Step 1: Start Docker Services ⚠️ REQUIRED

```bash
cd /home/joao/Projects/payment-live-chat/project
docker compose up -d
```

**Wait 30 seconds**, then verify:
```bash
docker ps
```

**Should show 3 containers:**
- payment-chat-db (PostgreSQL)
- payment-chat-keycloak (Keycloak)
- payment-chat-rabbitmq (RabbitMQ)

---

## Step 2: Import Keycloak Realm (One-time setup)

1. Open: http://localhost:8081
2. Login: `admin` / `admin`
3. Top-left dropdown → "Create Realm"
4. Browse → Select: `/home/joao/Projects/payment-live-chat/project/keycloak-realm-export.json`
5. Click "Create"

**Verify:** You should see "payment-chat" realm in dropdown

---

## Step 3: Start Spring Boot Backend

```bash
cd /home/joao/Projects/payment-live-chat/project
./mvnw spring-boot:run
```

**Backend will start on:** http://localhost:8080

---

## Final Status Check

Run this to verify everything:

```bash
# Check Docker services
docker ps

# Check all ports
netstat -tuln | grep -E '4200|5432|5672|8080|8081'
```

**Expected Result:**
```
✅ Port 4200 - Angular (Already running)
✅ Port 5432 - PostgreSQL (from Docker)
✅ Port 5672 - RabbitMQ (from Docker)
✅ Port 8081 - Keycloak (from Docker)
✅ Port 8080 - Spring Boot (after mvnw spring-boot:run)
```

---

## Service URLs

| Service | URL | Status |
|---------|-----|--------|
| Angular Frontend | http://localhost:4200 | ✅ Running |
| Keycloak Admin | http://localhost:8081 | ❌ Start Docker |
| Spring Boot API | http://localhost:8080 | ❌ Start after Docker |
| RabbitMQ Management | http://localhost:15672 | ❌ Start Docker |

---

## Quick Start Commands

**Option A: Start Everything at Once**
```bash
# Terminal 1: Start Docker
cd /home/joao/Projects/payment-live-chat/project
docker compose up -d

# Wait 30 seconds...

# Terminal 2: Start Backend (needs Java 21)
cd /home/joao/Projects/payment-live-chat/project
./mvnw spring-boot:run

# Terminal 3: Angular already running! ✅
```

**Option B: If Docker Not Installed**
```bash
# Install Docker
sudo apt update
sudo apt install docker.io docker-compose-v2
sudo systemctl start docker
sudo usermod -aG docker $USER
newgrp docker

# Then run Option A
```

---

## Troubleshooting

### "docker: command not found"
Install Docker:
```bash
sudo apt update
sudo apt install docker.io
sudo systemctl start docker
```

### "permission denied"
Add user to docker group:
```bash
sudo usermod -aG docker $USER
newgrp docker
```

### "JAVA_HOME not defined"
Install Java 21:
```bash
sudo apt update
sudo apt install openjdk-21-jdk
```

### Backend still fails
Check Docker logs:
```bash
docker logs payment-chat-db
docker logs payment-chat-rabbitmq
docker logs payment-chat-keycloak
```

---

## What's Currently Running

Based on your errors, currently running:
- ✅ Angular Frontend (port 4200)
- ❌ PostgreSQL - NOT RUNNING (backend needs this)
- ❌ RabbitMQ - NOT RUNNING (backend needs this)
- ❌ Keycloak - NOT RUNNING (Angular auth needs this)
- ❌ Spring Boot - CAN'T START (waiting for PostgreSQL + RabbitMQ)

**You MUST start Docker first!**

---

## Summary: What You're Missing

You need to run **ONE command** to fix everything:

```bash
cd /home/joao/Projects/payment-live-chat/project
docker compose up -d
```

This will start:
1. PostgreSQL database
2. RabbitMQ message broker
3. Keycloak authentication server

**Then** you can start the Spring Boot backend!
