import axios from "axios";
import { AuthorDTO } from "../type/AuthorDTO";
import { PagedResponse } from "../type/PagedResponse";

class AuthorService {
  private api = axios.create({
    baseURL: "http://localhost:8080/authors",
    withCredentials: true,
  });
  //No create function - this is internal
  //Read individual
  async getAuthorById(id: number): Promise<AuthorDTO> {
    const response = await this.api.get<AuthorDTO>(`/${id}`);
    return response.data;
  }

  //Read - search by name
  async searchAuthorByName(
    name: string,
    page: number = 0,
    size: number = 10
  ): Promise<PagedResponse<AuthorDTO>> {
    const url = `/search?name=${encodeURIComponent(
      name
    )}&page=${page}&size=${size}`;
    const response = await this.api.get<PagedResponse<AuthorDTO>>(url);
    return response.data;
  }

  //Update
  async updateAuthor(
    id: number,
    updateData: {
      name: string;
    }
  ): Promise<AuthorDTO> {
    const url = `/${id}`;
    const response = await this.api.patch<AuthorDTO>(url, updateData);
    return response.data;
  }

  //Delete
  async deleteAuthor(id: number): Promise<string> {
    const response = await this.api.delete<boolean>(`/${id}`);
    if (response.data) {
      return "Author deleted successfully!";
    }
    return "Failed to delete author!";
  }
}

export default new AuthorService();
