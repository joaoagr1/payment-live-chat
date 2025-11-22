import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { KeycloakProfile } from 'keycloak-js';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="home-container">
      <nav class="navbar">
        <div class="nav-content">
          <h1>💬 Payment Chat</h1>
          <div class="user-info">
            <span class="username">👤 {{ username }}</span>
            <button class="btn-logout" (click)="logout()">Logout</button>
          </div>
        </div>
      </nav>

      <div class="content">
        <div class="welcome-card">
          <h2>Welcome, {{ profile?.firstName || username }}! 🎉</h2>
          <p>Your Keycloak authentication is working perfectly!</p>

          <div class="profile-info" *ngIf="profile">
            <h3>Your Profile:</h3>
            <div class="info-row">
              <strong>Username:</strong> {{ profile.username }}
            </div>
            <div class="info-row">
              <strong>Email:</strong> {{ profile.email }}
            </div>
            <div class="info-row">
              <strong>First Name:</strong> {{ profile.firstName }}
            </div>
            <div class="info-row">
              <strong>Last Name:</strong> {{ profile.lastName }}
            </div>
            <div class="info-row">
              <strong>User ID:</strong> {{ profile.id }}
            </div>
          </div>

          <div class="token-info">
            <h3>🔐 Access Token:</h3>
            <div class="token-box">
              <code>{{ token.substring(0, 100) }}...</code>
            </div>
            <p class="token-note">This token is sent to the backend API for authentication</p>
          </div>

          <div class="success-message">
            <h3>✅ Keycloak Integration Success!</h3>
            <ul>
              <li>✅ User authenticated with Keycloak</li>
              <li>✅ JWT token received</li>
              <li>✅ User profile loaded</li>
              <li>✅ Protected route working</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .home-container {
      min-height: 100vh;
      background: #f7fafc;
    }

    .navbar {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      padding: 20px;
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    }

    .nav-content {
      max-width: 1200px;
      margin: 0 auto;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .nav-content h1 {
      margin: 0;
      font-size: 1.8rem;
    }

    .user-info {
      display: flex;
      align-items: center;
      gap: 15px;
    }

    .username {
      font-weight: 600;
      font-size: 1.1rem;
    }

    .btn-logout {
      background: rgba(255, 255, 255, 0.2);
      border: 2px solid white;
      color: white;
      padding: 8px 20px;
      border-radius: 6px;
      font-weight: 600;
      cursor: pointer;
      transition: all 0.3s ease;
    }

    .btn-logout:hover {
      background: white;
      color: #667eea;
    }

    .content {
      max-width: 1200px;
      margin: 40px auto;
      padding: 0 20px;
    }

    .welcome-card {
      background: white;
      border-radius: 16px;
      padding: 40px;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
    }

    .welcome-card h2 {
      margin: 0 0 10px 0;
      color: #333;
      font-size: 2rem;
    }

    .welcome-card > p {
      color: #666;
      font-size: 1.1rem;
      margin-bottom: 30px;
    }

    .profile-info {
      background: #f7fafc;
      padding: 20px;
      border-radius: 8px;
      margin-bottom: 20px;
    }

    .profile-info h3 {
      margin: 0 0 15px 0;
      color: #333;
    }

    .info-row {
      padding: 10px 0;
      border-bottom: 1px solid #e2e8f0;
    }

    .info-row:last-child {
      border-bottom: none;
    }

    .info-row strong {
      color: #667eea;
      margin-right: 10px;
    }

    .token-info {
      background: #fff5f5;
      padding: 20px;
      border-radius: 8px;
      margin-bottom: 20px;
    }

    .token-info h3 {
      margin: 0 0 15px 0;
      color: #c53030;
    }

    .token-box {
      background: #2d3748;
      color: #48bb78;
      padding: 15px;
      border-radius: 6px;
      overflow-x: auto;
      margin-bottom: 10px;
    }

    .token-box code {
      font-family: 'Courier New', monospace;
      font-size: 0.9rem;
    }

    .token-note {
      color: #742a2a;
      font-size: 0.9rem;
      margin: 0;
      font-style: italic;
    }

    .success-message {
      background: #f0fff4;
      border: 2px solid #48bb78;
      padding: 20px;
      border-radius: 8px;
    }

    .success-message h3 {
      margin: 0 0 15px 0;
      color: #22543d;
    }

    .success-message ul {
      margin: 0;
      padding-left: 20px;
    }

    .success-message li {
      margin: 8px 0;
      color: #22543d;
      font-weight: 500;
    }
  `]
})
export class HomeComponent implements OnInit {
  username: string = '';
  token: string = '';
  profile: KeycloakProfile | null = null;

  constructor(private authService: AuthService) {}

  async ngOnInit(): Promise<void> {
    this.username = this.authService.getUsername();
    this.token = this.authService.getToken();
    this.profile = await this.authService.getProfile();

    // Sync user to backend database on login
    await this.authService.syncUserToBackend();
  }

  async logout(): Promise<void> {
    await this.authService.logout();
  }
}
