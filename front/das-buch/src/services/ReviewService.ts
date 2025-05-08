import axios from "axios";
import { ReviewDTO } from "../type/ReviewDTO";
import { PagedResponse } from "../type/PagedResponse";

class ReviewService {
  private api = axios.create({
    baseURL: "http://localhost:8080/reviews",
    withCredentials: true,
  });

  //Create
  async createReview(review: {
    bookTitle: string;
    authorsNames: string[];
    synopsys: string;
    commentary: string;
    score: number;
    readAt: string;
  }): Promise<ReviewDTO> {
    const newReview = await this.api.post<ReviewDTO>("", review);
    return newReview.data;
  }

  //Read - Individual
  async getReviewById(id: number): Promise<ReviewDTO> {
    const response = await this.api.get<ReviewDTO>(`/${id}`);
    return response.data;
  }

  //Read - All by User (paginated)
  async getUserReviews(
    userId: number,
    page: number = 0,
    size: number = 10
  ): Promise<PagedResponse<ReviewDTO>> {
    const response = await this.api.get<PagedResponse<ReviewDTO>>(
      `/user/${userId}`,
      {
        params: { page, size },
      }
    );
    return response.data;
  }

  //Read - All by book
  async getBookReviews(
    bookId: number,
    page: number = 0,
    size: number = 10
  ): Promise<PagedResponse<ReviewDTO>> {
    const response = await this.api.get<PagedResponse<ReviewDTO>>(
      `/book/${bookId}`,
      {
        params: { page, size },
      }
    );
    return response.data;
  }

  //Update
  async updateReview(review: {
    id: number;
    book: string;
    authors: string[];
    synopsis: string;
    commentary: string;
    score: number;
    readAt: Date;
  }): Promise<ReviewDTO> {
    const newReview = await this.api.patch<ReviewDTO>(`${review.id}`, review);
    return newReview.data;
  }

  //Delete
  async deleteReview(id: number): Promise<string> {
    const response = await this.api.delete<boolean>(`/${id}`);
    if(response.data) {
      return "Review deleted successfully!"
    };
    return "Failed to delete review"
  }
}

export default new ReviewService();
