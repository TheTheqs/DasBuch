import { BookDTO } from "./BookDTO";
import { UserDTO } from "./UserDTO";

export interface ReviewDTO {
  id: number;
  user: UserDTO;
  book: BookDTO;
  synopsys: string;
  commentary: string;
  score: number;
  readAt: string;
}
