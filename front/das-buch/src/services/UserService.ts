import axios from "axios";
import { UserDTO } from "../type/UserDTO";
import { PagedResponse } from "../type/PagedResponse";
import { clearUser } from "../context/User";

class UserService {
  private BASE_URL = "https://das-buch-backend.onrender.com/users";
  //Register User
  async registerUser(user: {
    name: string;
    email: string;
    password: string;
  }): Promise<void> {
    await axios.post<UserDTO>(`${this.BASE_URL}/register`, user);
  }

  //Verify User
  async verifyUser(token: string): Promise<string> {
    const response = await axios.get(`${this.BASE_URL}/verify?token=${token}`, {
      responseType: "text",
    });
    return response.data;
  }

  //Login
  async loginUser(email: string, password: string): Promise<UserDTO> {
    const response = await axios.post<UserDTO>(`${this.BASE_URL}/login`, null, {
      params: { email, password },
      withCredentials: true,
    });
    return response.data;
  }

  //Search user by name
  async searchUsersByName(
    name: string,
    page: number = 0,
    size: number = 10
  ): Promise<PagedResponse<UserDTO>> {
    const response = await axios.get<PagedResponse<UserDTO>>(
      `${this.BASE_URL}/search`,
      {
        params: { name, page, size },
        withCredentials: true,
      }
    );
    return response.data;
  }

  //Update User
  async updateUser(name: string, password: string): Promise<UserDTO> {
    const response = await axios.patch<UserDTO>(
      `${this.BASE_URL}/update`,
      null,
      {
        params: { name, password },
        withCredentials: true,
      }
    );
    return response.data;
  }

  //Forgot Password Request
  async forgotPassword(email: string): Promise<string> {
    const response = await axios.post<string>(
      `${this.BASE_URL}/forgot-password`,
      null,
      {
        params: { email },
        responseType: "text",
      }
    );
    return response.data;
  }

  //Reset Password
  async resetPassword(token: string, password: string): Promise<string> {
    const response = await axios.patch(
      `${this.BASE_URL}/reset`,
      { token, password },
      {
        responseType: "text",
        withCredentials: true,
      }
    );
    return response.data;
  }
  //Logout
  async logout(): Promise<void> {
    await axios.get(`${this.BASE_URL}/logout`, {
      withCredentials: true,
    });
    clearUser();
  }

  //Delete user
  async deleteUser(id: number): Promise<string> {
    const response = await axios.delete(`${this.BASE_URL}/${id}`, {
      withCredentials: true,
    });

    if (response.data) {
      return "User deleted successfully!";
    }
    return "Failed to delete user";
  }

  //Get individual User by ID
  async getUserById(id: number): Promise<UserDTO> {
    const response = await axios.get<UserDTO>(`${this.BASE_URL}/${id}`, {
      withCredentials: true,
    });
    return response.data;
  }
}

export default new UserService();
