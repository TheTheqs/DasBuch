import { useState } from "react";
import FormInput from "../components/FormInput";
import FormContainer from "../components/FormContainer";
import AuthorCard from "../components/AuthorCard";
import SearchResultGrid from "../components/SearchResultsGrid";
import PaginationBar from "../components/PaginationBar";
import AuthorService from "../services/AuthorService";
import { AuthorDTO } from "../type/AuthorDTO";
import { handleApiError } from "../utils/handleApiError";

function SearchAuthorPage() {
  const [formData, setFormData] = useState({ name: "" });
  const [results, setResults] = useState<AuthorDTO[]>([]);
  const [pageInfo, setPageInfo] = useState({ page: 0, totalPages: 0 });
  const [error, setError] = useState("");

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError("");

    try {
      const response = await AuthorService.searchAuthorByName(
        formData.name,
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
        title="Buscar Autor"
        submitMessage="Pesquisar"
        onSubmit={handleSubmit}
      >
        <FormInput
          label=""
          placeholder="Nome do Autor"
          type="name"
          name="name"
          value={formData.name}
          onChange={handleChange}
        />
        {error && <p style={{ color: "red" }}>{error}</p>}
      </FormContainer>

      {formData.name !== "" && results.length === 0 && !error && (
        <div className="text-center text-muted mt-4">
          Nenhum autor encontrado.
        </div>
      )}

      {results.length > 0 && (
        <SearchResultGrid>
          {results.map((author) => (
            <AuthorCard key={author.id} author = {author} />
          ))}

          <PaginationBar
            currentPage={pageInfo.page}
            totalPages={pageInfo.totalPages}
            onPageChange={async (newPage) => {
              const response = await AuthorService.searchAuthorByName(
                formData.name,
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

export default SearchAuthorPage;
