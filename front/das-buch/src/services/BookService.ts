import axios from "axios";
import { BookDTO } from "../type/BookDTO";
import { PagedResponse } from "../type/PagedResponse";

class BookService {
  private api = axios.create({
    baseURL: "http://localhost:8080/books",
    withCredentials: true,
  });

  //There is no Create book - this action is internal

  //Read - Individual
  async geBookById(id: number): Promise<BookDTO> {
    const response = await this.api.get<BookDTO>(`/${id}`);
    return response.data;
  }
  //Read - search by title
  async searchBookByTitle(
    title: string,
    page: number = 0,
    size: number = 10
  ): Promise<PagedResponse<BookDTO>> {
    const url = `/search?title=${encodeURIComponent(title)}&page=${page}&size=${size}`;
    const response = await this.api.get<PagedResponse<BookDTO>>(url);
    return response.data;
  }

  //Update
  
}

export default new BookService();
