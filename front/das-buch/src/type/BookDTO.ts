import { AuthorDTO } from "./AuthorDTO";

export interface BookDTO {
    id: number;
    title: string;
    authors: AuthorDTO[];
    reviewCount: number;
}