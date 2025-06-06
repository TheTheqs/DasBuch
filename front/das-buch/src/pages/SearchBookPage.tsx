import { useEffect, useState, useCallback } from "react";
import { useSearchParams } from "react-router-dom";
import FormInput from "../components/FormInput";
import FormContainer from "../components/FormContainer";
import BookCard from "../components/BookCard";
import SearchResultGrid from "../components/SearchResultsGrid";
import PaginationBar from "../components/PaginationBar";
import BookService from "../services/BookService";
import { BookDTO } from "../type/BookDTO";
import { handleApiError } from "../utils/handleApiError";
import { useTranslation } from "react-i18next";
import { Spinner } from "react-bootstrap"; // Novo import

function SearchBookPage() {
  const [searchParams, setSearchParams] = useSearchParams();
  const { t } = useTranslation();

  const [formData, setFormData] = useState({ title: "" });
  const [results, setResults] = useState<BookDTO[]>([]);
  const [pageInfo, setPageInfo] = useState({ page: 0, totalPages: 0 });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false); // Novo estado

  const search = useCallback(
    async (title: string, page: number) => {
      setLoading(true); // Ativa loading
      try {
        const response = await BookService.searchBookByTitle(title, page, 10);
        setResults(response.content);
        setPageInfo({ page: response.number, totalPages: response.totalPages });
      } catch (err) {
        setError(t(handleApiError(err)));
      } finally {
        setLoading(false); // Finaliza loading
      }
    },
    [t]
  );

  useEffect(() => {
    const title = searchParams.get("title") || "";
    const page = parseInt(searchParams.get("page") || "0", 10);

    if (title) {
      setFormData({ title });
      search(title, page);
    }
  }, [searchParams, search]);

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
        title={t("searchBook.title")}
        submitMessage={t("form.search")}
        onSubmit={handleSubmit}
      >
        <FormInput
          label=""
          placeholder={t("searchBook.titlePlaceholder")}
          type="name"
          name="title"
          value={formData.title}
          onChange={handleChange}
        />
        {error && <p style={{ color: "red" }}>{error}</p>}
      </FormContainer>

      {loading && (
        <div className="text-center my-4">
          <Spinner animation="border" variant="primary" />
        </div>
      )}

      {!loading && formData.title !== "" && results.length === 0 && !error && (
        <div className="text-center text-muted mt-4">
          {t("searchBook.noResults")}
        </div>
      )}

      {!loading && results.length > 0 && (
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
