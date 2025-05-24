import { useEffect, useState, useCallback } from "react";
import { useSearchParams } from "react-router-dom";
import FormInput from "../components/FormInput";
import FormContainer from "../components/FormContainer";
import AuthorCard from "../components/AuthorCard";
import SearchResultGrid from "../components/SearchResultsGrid";
import PaginationBar from "../components/PaginationBar";
import AuthorService from "../services/AuthorService";
import { AuthorDTO } from "../type/AuthorDTO";
import { handleApiError } from "../utils/handleApiError";
import { useTranslation } from "react-i18next";
import { Spinner } from "react-bootstrap"; // novo import

function SearchAuthorPage() {
  const [searchParams, setSearchParams] = useSearchParams();
  const { t } = useTranslation();

  const [formData, setFormData] = useState({ name: "" });
  const [results, setResults] = useState<AuthorDTO[]>([]);
  const [pageInfo, setPageInfo] = useState({ page: 0, totalPages: 0 });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false); // novo estado

  const search = useCallback(
    async (name: string, page: number) => {
      setLoading(true); // ativa carregamento
      try {
        const response = await AuthorService.searchAuthorByName(name, page, 10);
        setResults(response.content);
        setPageInfo({ page: response.number, totalPages: response.totalPages });
      } catch (err) {
        setError(t(handleApiError(err)));
      } finally {
        setLoading(false); // desativa carregamento
      }
    },
    [t]
  );

  useEffect(() => {
    const name = searchParams.get("name") || "";
    const page = parseInt(searchParams.get("page") || "0", 10);

    if (name) {
      setFormData({ name });
      search(name, page);
    }
  }, [searchParams, search]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError("");
    setSearchParams({ name: formData.name, page: "0" });
    search(formData.name, 0);
  };

  return (
    <div>
      <FormContainer
        title={t("searchAuthor.title")}
        submitMessage={t("form.search")}
        onSubmit={handleSubmit}
      >
        <FormInput
          label=""
          placeholder={t("searchAuthor.namePlaceholder")}
          type="name"
          name="name"
          value={formData.name}
          onChange={handleChange}
        />
        {error && <p style={{ color: "red" }}>{error}</p>}
      </FormContainer>

      {loading && (
        <div className="text-center my-4">
          <Spinner animation="border" variant="primary" />
        </div>
      )}

      {!loading && formData.name !== "" && results.length === 0 && !error && (
        <div className="text-center text-muted mt-4">
          {t("searchAuthor.noResults")}
        </div>
      )}

      {!loading && results.length > 0 && (
        <SearchResultGrid>
          {results.map((author) => (
            <AuthorCard key={author.id} author={author} />
          ))}

          <PaginationBar
            currentPage={pageInfo.page}
            totalPages={pageInfo.totalPages}
            onPageChange={(newPage) => {
              setSearchParams({
                name: formData.name,
                page: newPage.toString(),
              });
              search(formData.name, newPage);
            }}
          />
        </SearchResultGrid>
      )}
    </div>
  );
}

export default SearchAuthorPage;
