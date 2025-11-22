import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { FriendshipService } from '../../services/friendship.service';
import { User } from '../../models/user.model';
import { Friendship } from '../../models/friendship.model';

@Component({
  selector: 'app-friends',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './friends.component.html',
  styleUrls: ['./friends.component.css']
})
export class FriendsComponent implements OnInit {
  friends: User[] = [];
  pendingRequests: Friendship[] = [];
  searchUsername: string = '';
  searchResult: User | null = null;
  searchError: string = '';
  loading: boolean = false;
  currentUsername: string = '';

  constructor(
    private friendshipService: FriendshipService,
    private authService: AuthService,
    private router: Router
  ) {}

  async ngOnInit(): Promise<void> {
    this.currentUsername = this.authService.getUsername();

    // Sync current user to database
    await this.authService.syncUserToBackend();

    this.loadFriends();
    this.loadPendingRequests();
  }

  loadFriends(): void {
    this.loading = true;
    this.friendshipService.getFriends().subscribe({
      next: (friends) => {
        this.friends = friends;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading friends:', error);
        this.loading = false;
      }
    });
  }

  loadPendingRequests(): void {
    this.friendshipService.getPendingRequests().subscribe({
      next: (requests) => {
        this.pendingRequests = requests;
      },
      error: (error) => {
        console.error('Error loading pending requests:', error);
      }
    });
  }

  searchUser(): void {
    if (!this.searchUsername.trim()) {
      this.searchError = 'Please enter a username';
      return;
    }

    this.searchError = '';
    this.searchResult = null;
    this.loading = true;

    this.friendshipService.searchUserByUsername(this.searchUsername).subscribe({
      next: (user) => {
        this.searchResult = user;
        this.loading = false;
      },
      error: (error) => {
        this.searchError = error.status === 404
          ? 'User not found'
          : 'Error searching for user';
        this.loading = false;
      }
    });
  }

  sendFriendRequest(username: string): void {
    this.friendshipService.sendFriendRequest({ username }).subscribe({
      next: () => {
        this.searchResult = null;
        this.searchUsername = '';
        alert('Friend request sent successfully!');
        this.loadPendingRequests();
      },
      error: (error) => {
        const message = error.error?.message || 'Error sending friend request';
        alert(message);
      }
    });
  }

  acceptRequest(friendshipId: number): void {
    this.friendshipService.acceptFriendRequest(friendshipId).subscribe({
      next: () => {
        this.loadFriends();
        this.loadPendingRequests();
      },
      error: (error) => {
        console.error('Error accepting request:', error);
        alert('Error accepting friend request');
      }
    });
  }

  rejectRequest(friendshipId: number): void {
    this.friendshipService.rejectFriendRequest(friendshipId).subscribe({
      next: () => {
        this.loadPendingRequests();
      },
      error: (error) => {
        console.error('Error rejecting request:', error);
        alert('Error rejecting friend request');
      }
    });
  }

  openChat(friendId: number): void {
    this.router.navigate(['/chat', friendId]);
  }

  logout(): void {
    this.authService.logout();
  }
}
