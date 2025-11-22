import { User } from './user.model';

export enum FriendshipStatus {
  PENDING = 'PENDING',
  ACCEPTED = 'ACCEPTED',
  REJECTED = 'REJECTED',
  BLOCKED = 'BLOCKED'
}

export interface Friendship {
  id: number;
  requester: User;
  addressee: User;
  status: FriendshipStatus;
  createdAt: string;
  acceptedAt?: string;
}

export interface FriendRequest {
  username: string;
}
