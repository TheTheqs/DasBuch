import { useState } from "react";
import FormInput from "../components/FormInput";
import FormContainer from "../components/FormContainer";
import BookCard from "../components/BookCard";
import SearchResultGrid from "../components/SearchResultsGrid";
import PaginationBar from "../components/PaginationBar";
import BookService from "../services/BookService";
import { BookDTO } from "../type/BookDTO";
import { handleApiError } from "../utils/handleApiError";

function SearchBookPage() {
  const [formData, setFormData] = useState({ title: "" });
  const [results, setResults] = useState<BookDTO[]>([]);
  const [pageInfo, setPageInfo] = useState({ page: 0, totalPages: 0 });
  const [error, setError] = useState("");

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError("");

    try {
      const response = await BookService.searchBookByTitle(
        formData.title,
        0,
        10
      );
      setResults(response.content);
      setPageInfo({ page: response.number, totalPages: response.totalPages });
    } catch (error) {
      setError(handleApiError(error));
    }
  };

  return (
    <div>
      <FormContainer
        title="Buscar Livro"
        submitMessage="Pesquisar"
        onSubmit={handleSubmit}
      >
        <FormInput
          label=""
          placeholder="Título do Livro"
          type="name"
          name="title"
          value={formData.title}
          onChange={handleChange}
        />
        {error && <p style={{ color: "red" }}>{error}</p>}
      </FormContainer>

      {formData.title !== "" && results.length === 0 && !error && (
        <div className="text-center text-muted mt-4">
          Nenhum livro encontrado.
        </div>
      )}

      {results.length > 0 && (
        <SearchResultGrid>
          {results.map((book) => (
            <BookCard key={book.id} book={book} />
          ))}

          <PaginationBar
            currentPage={pageInfo.page}
            totalPages={pageInfo.totalPages}
            onPageChange={async (newPage) => {
              const response = await BookService.searchBookByTitle(
                formData.title,
                newPage,
                10
              );
              setResults(response.content);
              setPageInfo({
                page: response.number,
                totalPages: response.totalPages,
              });
            }}
          />
        </SearchResultGrid>
      )}
    </div>
  );
}

export default SearchBookPage;
