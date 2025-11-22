import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="register-container">
      <div class="register-card">
        <div class="logo">
          <h1>✍️ Create Account</h1>
          <p>Join Payment Chat and start chatting with friends</p>
        </div>

        <div class="buttons">
          <button class="btn btn-primary" (click)="register()">
            Create Account with Keycloak
          </button>

          <button class="btn btn-secondary" (click)="goToLogin()">
            Already have an account? Login
          </button>
        </div>

        <div class="info">
          <h3>What you'll get:</h3>
          <ul>
            <li>✅ Secure account with Keycloak</li>
            <li>✅ Add friends and start chatting</li>
            <li>✅ Request and send payments</li>
            <li>✅ Real-time notifications</li>
          </ul>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .register-container {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(135deg, #48bb78 0%, #38a169 100%);
      padding: 20px;
    }

    .register-card {
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
      background: #48bb78;
      color: white;
    }

    .btn-primary:hover {
      background: #38a169;
      transform: translateY(-2px);
      box-shadow: 0 5px 15px rgba(72, 187, 120, 0.4);
    }

    .btn-secondary {
      background: #667eea;
      color: white;
    }

    .btn-secondary:hover {
      background: #5568d3;
      transform: translateY(-2px);
      box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
    }

    .info {
      background: #f7fafc;
      padding: 20px;
      border-radius: 8px;
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
  `]
})
export class RegisterComponent {
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  async register(): Promise<void> {
    await this.authService.register();
  }

  goToLogin(): void {
    this.router.navigate(['/login']);
  }
}
