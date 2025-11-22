import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';
import { environment } from '../../environments/environment';
import { firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(
    private keycloak: KeycloakService,
    private http: HttpClient
  ) {}

  async login(): Promise<void> {
    await this.keycloak.login({
      redirectUri: window.location.origin + '/home'
    });
  }

  async register(): Promise<void> {
    await this.keycloak.register({
      redirectUri: window.location.origin + '/home'
    });
  }

  async logout(): Promise<void> {
    await this.keycloak.logout(window.location.origin);
  }

  async isLoggedIn(): Promise<boolean> {
    return await this.keycloak.isLoggedIn();
  }

  async getProfile(): Promise<KeycloakProfile> {
    return await this.keycloak.loadUserProfile();
  }

  getToken(): string {
    return this.keycloak.getKeycloakInstance().token || '';
  }

  getUsername(): string {
    return this.keycloak.getUsername();
  }

  hasRole(role: string): boolean {
    return this.keycloak.isUserInRole(role);
  }

  async syncUserToBackend(): Promise<void> {
    console.log('Syncing user to backend...');
    try {
      const token = this.getToken();

      // Debug: Log token payload to see what claims Keycloak is sending
      if (token) {
        const payload = JSON.parse(atob(token.split('.')[1]));
        console.log('JWT Token payload from Keycloak:', payload);
        console.log('Username from token:', payload.preferred_username || payload.username || payload.sub);
        console.log('Email from token:', payload.email);
      }

      const headers = new HttpHeaders({
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      });

      // Call /api/users/me to sync/create user in database
      await firstValueFrom(
        this.http.get(`${environment.apiUrl}/users/me`, { headers })
      );
      console.log('User synced to backend database');
    } catch (error) {
      console.error('Error syncing user to backend:', error);
    }
  }
}
