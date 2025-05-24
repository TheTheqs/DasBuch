import axios from "axios";
import { BookDTO } from "../type/BookDTO";
import { PagedResponse } from "../type/PagedResponse";

class BookService {
  private api = axios.create({
    baseURL: `${import.meta.env.VITE_API_URL}/books`,
    withCredentials: true,
  });

  //There is no Create book - this action is internal

  //Read - Individual
  async getBookById(id: number): Promise<BookDTO> {
    const response = await this.api.get<BookDTO>(`/${id}`);
    return response.data;
  }

  //Read - search by title
  async searchBookByTitle(
    title: string,
    page: number = 0,
    size: number = 10
  ): Promise<PagedResponse<BookDTO>> {
    const url = `/search?title=${encodeURIComponent(
      title
    )}&page=${page}&size=${size}`;
    const response = await this.api.get<PagedResponse<BookDTO>>(url);
    return response.data;
  }

  //Read - all books by author
  async getAuthorBooks(
    id: number,
    page: number = 0,
    size: number = 10
  ): Promise<PagedResponse<BookDTO>> {
    const url = `/author/${id}?page=${page}&size=${size}`;
    const response = await this.api.get<PagedResponse<BookDTO>>(url);
    return response.data;
  }

  //Update
  async updateBook(
    id: number,
    updateData: {
      title?: string;
      authors?: string[];
    }
  ): Promise<BookDTO> {
    const url = `/${id}`;
    const response = await this.api.patch<BookDTO>(url, updateData);
    return response.data;
  }

  //Delete
  async deleteBook(id: number) : Promise<string> {
    const response = await this.api.delete<boolean>(`/${id}`);
    if(response.data) {
      return "Book deleted successfully!"
    }
    return "Failed to delete book!"
  }
}

export default new BookService();
