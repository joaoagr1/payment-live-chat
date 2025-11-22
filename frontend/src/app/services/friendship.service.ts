import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Friendship, FriendRequest } from '../models/friendship.model';
import { User } from '../models/user.model';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class FriendshipService {
  private apiUrl = `${environment.apiUrl}/friendships`;
  private usersUrl = `${environment.apiUrl}/users`;

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  sendFriendRequest(request: FriendRequest): Observable<Friendship> {
    return this.http.post<Friendship>(
      `${this.apiUrl}/request`,
      request,
      { headers: this.getHeaders() }
    );
  }

  acceptFriendRequest(friendshipId: number): Observable<Friendship> {
    return this.http.post<Friendship>(
      `${this.apiUrl}/${friendshipId}/accept`,
      {},
      { headers: this.getHeaders() }
    );
  }

  rejectFriendRequest(friendshipId: number): Observable<void> {
    return this.http.post<void>(
      `${this.apiUrl}/${friendshipId}/reject`,
      {},
      { headers: this.getHeaders() }
    );
  }

  getFriends(): Observable<User[]> {
    return this.http.get<User[]>(
      `${this.apiUrl}/friends`,
      { headers: this.getHeaders() }
    );
  }

  getPendingRequests(): Observable<Friendship[]> {
    return this.http.get<Friendship[]>(
      `${this.apiUrl}/requests/pending`,
      { headers: this.getHeaders() }
    );
  }

  searchUserByUsername(username: string): Observable<User> {
    return this.http.get<User>(
      `${this.usersUrl}/username/${username}`,
      { headers: this.getHeaders() }
    );
  }
}