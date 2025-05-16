import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import FormInput from "../components/FormInput";
import FormContainer from "../components/FormContainer";
import BookCard from "../components/BookCard";
import SearchResultGrid from "../components/SearchResultsGrid";
import PaginationBar from "../components/PaginationBar";
import BookService from "../services/BookService";
import { BookDTO } from "../type/BookDTO";
import { handleApiError } from "../utils/handleApiError";

function SearchBookPage() {
  const [searchParams, setSearchParams] = useSearchParams();

  const [formData, setFormData] = useState({ title: "" });
  const [results, setResults] = useState<BookDTO[]>([]);
  const [pageInfo, setPageInfo] = useState({ page: 0, totalPages: 0 });
  const [error, setError] = useState("");

  useEffect(() => {
    const title = searchParams.get("title") || "";
    const page = parseInt(searchParams.get("page") || "0", 10);

    if (title) {
      setFormData({ title });
      search(title, page);
    }
  }, [searchParams]);

  const search = async (title: string, page: number) => {
    try {
      const response = await BookService.searchBookByTitle(title, page, 10);
      setResults(response.content);
      setPageInfo({ page: response.number, totalPages: response.totalPages });
    } catch (error) {
      setError(handleApiError(error));
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError("");
    setSearchParams({ title: formData.title, page: "0" });
    search(formData.title, 0);
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
            onPageChange={(newPage) => {
              setSearchParams({
                title: formData.title,
                page: newPage.toString(),
              });
              search(formData.title, newPage);
            }}
          />
        </SearchResultGrid>
      )}
    </div>
  );
}

export default SearchBookPage;
