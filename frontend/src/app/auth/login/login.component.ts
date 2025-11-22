import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="login-container">
      <div class="login-card">
        <div class="logo">
          <h1>💬 Payment Chat</h1>
          <p>Chat with friends and request payments</p>
        </div>

        <div class="buttons">
          <button class="btn btn-primary" (click)="login()">
            🔐 Login with Keycloak
          </button>

          <button class="btn btn-secondary" (click)="register()">
            ✍️ Create Account
          </button>
        </div>

        <div class="info">
          <h3>Features:</h3>
          <ul>
            <li>✅ Secure authentication with Keycloak</li>
            <li>✅ Real-time chat with friends</li>
            <li>✅ Send payment requests via PIX</li>
            <li>✅ Track payment status</li>
          </ul>
        </div>

        <div class="test-accounts">
          <h4>🧪 Test Accounts:</h4>
          <p><strong>User 1:</strong> testuser / test123</p>
          <p><strong>User 2:</strong> testuser2 / test123</p>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .login-container {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      padding: 20px;
    }

    .login-card {
      background: white;
      border-radius: 16px;
      padding: 40px;
      max-width: 500px;
      width: 100%;
      box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
    }

    .logo {
      text-align: center;
      margin-bottom: 40px;
    }

    .logo h1 {
      font-size: 2.5rem;
      margin: 0 0 10px 0;
      color: #333;
    }

    .logo p {
      color: #666;
      font-size: 1rem;
      margin: 0;
    }

    .buttons {
      display: flex;
      flex-direction: column;
      gap: 15px;
      margin-bottom: 30px;
    }

    .btn {
      padding: 15px 30px;
      border: none;
      border-radius: 8px;
      font-size: 1.1rem;
      font-weight: 600;
      cursor: pointer;
      transition: all 0.3s ease;
    }

    .btn-primary {
      background: #667eea;
      color: white;
    }

    .btn-primary:hover {
      background: #5568d3;
      transform: translateY(-2px);
      box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
    }

    .btn-secondary {
      background: #48bb78;
      color: white;
    }

    .btn-secondary:hover {
      background: #38a169;
      transform: translateY(-2px);
      box-shadow: 0 5px 15px rgba(72, 187, 120, 0.4);
    }

    .info {
      background: #f7fafc;
      padding: 20px;
      border-radius: 8px;
      margin-bottom: 20px;
    }

    .info h3 {
      margin: 0 0 15px 0;
      color: #333;
      font-size: 1.2rem;
    }

    .info ul {
      margin: 0;
      padding-left: 20px;
    }

    .info li {
      margin: 8px 0;
      color: #555;
    }

    .test-accounts {
      background: #fff5f5;
      border: 2px solid #feb2b2;
      padding: 15px;
      border-radius: 8px;
      text-align: center;
    }

    .test-accounts h4 {
      margin: 0 0 10px 0;
      color: #c53030;
    }

    .test-accounts p {
      margin: 5px 0;
      font-family: monospace;
      color: #742a2a;
    }
  `]
})
export class LoginComponent {
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  async login(): Promise<void> {
    await this.authService.login();
  }

  async register(): Promise<void> {
    await this.authService.register();
  }
}
