import axios from "axios";
import { UserDTO } from "../type/UserDTO";
import { PagedResponse } from "../type/PagedResponse";

class UserService {
  private BASE_URL = "http://localhost:8080/users";

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
        params: {name, password},
        withCredentials: true
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
        params: {email},
        responseType: "text"
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
        withCredentials: true
      }
    );
    return response.data;
  }

  async deleteUser(id: number): Promise<void> {
    await axios.delete(`${this.BASE_URL}/${id}`);
  }
}

export default new UserService();
