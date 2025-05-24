import { useEffect, useState, useCallback } from 'react';
import { useSearchParams } from 'react-router-dom';
import FormInput from '../components/FormInput';
import FormContainer from '../components/FormContainer';
import UserCard from '../components/UserCard';
import SearchResultGrid from '../components/SearchResultsGrid';
import PaginationBar from '../components/PaginationBar';
import UserService from '../services/UserService';
import { UserDTO } from '../type/UserDTO';
import { handleApiError } from '../utils/handleApiError';
import { useTranslation } from 'react-i18next';

function SearchUserPage() {
  const [searchParams, setSearchParams] = useSearchParams();
  const { t } = useTranslation();

  const [formData, setFormData] = useState({ name: '' });
  const [results, setResults] = useState<UserDTO[]>([]);
  const [pageInfo, setPageInfo] = useState({ page: 0, totalPages: 0 });
  const [error, setError] = useState('');

  const search = useCallback(
    async (name: string, page: number) => {
      try {
        const response = await UserService.searchUsersByName(name, page, 10);
        setResults(response.content);
        setPageInfo({ page: response.number, totalPages: response.totalPages });
      } catch (err) {
        setError(t(handleApiError(err)));
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
        title={t("searchUser.title")}
        submitMessage={t("form.search")}
        onSubmit={handleSubmit}
      >
        <FormInput
          label=""
          placeholder={t("searchUser.namePlaceholder")}
          type="name"
          name="name"
          value={formData.name}
          onChange={handleChange}
        />
        {error && <p style={{ color: "red" }}>{error}</p>}
      </FormContainer>

      {formData.name !== "" && results.length === 0 && !error && (
        <div className="text-center text-muted mt-4">
          {t("searchUser.noResults")}
        </div>
      )}

      {results.length > 0 && (
        <SearchResultGrid>
          {results.map(user => (
            <UserCard
              key={user.id}
              name={user.name}
              id={user.id}
              reviewCount={user.reviewCount}
            />
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

export default SearchUserPage;
