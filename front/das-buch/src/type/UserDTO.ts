export type Role = "ADMIN" | "USER";

export interface UserDTO {
  id: number;
  name: string;
  email: string;
  role: Role;
  isActive: boolean;
  reviewCount: number;
}