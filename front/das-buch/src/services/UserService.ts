import axios from "axios";
import { UserDTO } from "../type/UserDTO";

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
            responseType: "text"
          });
        return response.data;
    }

    async getUserById(id: number): Promise<UserDTO> {
      const response = await axios.get<UserDTO>(`${this.BASE_URL}/${id}`);
      return response.data;
    }
  
    async createUser(user: Partial<UserDTO>): Promise<UserDTO> {
      const response = await axios.post<UserDTO>(this.BASE_URL, user);
      return response.data;
    }
  
    async updateUser(id: number, user: Partial<UserDTO>): Promise<UserDTO> {
      const response = await axios.put<UserDTO>(`${this.BASE_URL}/${id}`, user);
      return response.data;
    }
  
    async deleteUser(id: number): Promise<void> {
      await axios.delete(`${this.BASE_URL}/${id}`);
    }
  }
  
  export default new UserService();