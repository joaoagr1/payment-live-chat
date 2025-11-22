export interface User {
  id: number;
  keycloakId: string;
  username: string;
  email: string;
  displayName: string;
  avatarUrl?: string;
  online: boolean;
}
