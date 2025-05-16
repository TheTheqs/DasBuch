import { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import FormInput from '../components/FormInput';
import FormContainer from '../components/FormContainer';
import UserCard from '../components/UserCard';
import SearchResultGrid from '../components/SearchResultsGrid';
import PaginationBar from '../components/PaginationBar';
import UserService from '../services/UserService';
import { UserDTO } from '../type/UserDTO';

function SearchUserPage() {
  const [searchParams, setSearchParams] = useSearchParams();

  const [formData, setFormData] = useState({ name: '' });
  const [results, setResults] = useState<UserDTO[]>([]);
  const [pageInfo, setPageInfo] = useState({ page: 0, totalPages: 0 });

  useEffect(() => {
    const name = searchParams.get("name") || "";
    const page = parseInt(searchParams.get("page") || "0", 10);

    if (name) {
      setFormData({ name });
      search(name, page);
    }
  }, [searchParams]);

  const search = async (name: string, page: number) => {
    const response = await UserService.searchUsersByName(name, page, 10);
    setResults(response.content);
    setPageInfo({ page: response.number, totalPages: response.totalPages });
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setSearchParams({ name: formData.name, page: "0" });
    search(formData.name, 0);
  };

  return (
    <div>
      <FormContainer title="Buscar Usuário" submitMessage="Pesquisar" onSubmit={handleSubmit}>
        <FormInput
          label=""
          placeholder="Nome do Usuário"
          type="name"
          name="name"
          value={formData.name}
          onChange={handleChange}
        />
      </FormContainer>

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
