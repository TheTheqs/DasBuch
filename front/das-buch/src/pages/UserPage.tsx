import { useState } from 'react';
import FormInput from '../components/FormInput';
import FormContainer from '../components/FormContainer';
import UserCard from '../components/UserCard';
import SearchResultGrid from '../components/SearchResultsGrid';
import PaginationBar from '../components/PaginationBar';
import UserService from '../services/UserService';
import { UserDTO } from '../type/UserDTO';

function UserPage() {
    const [formData, setFormData] = useState({name: ''});
    const [results, setResults] = useState<UserDTO[]>([]);
    const [pageInfo, setPageInfo] = useState({ page: 0, totalPages: 0 });

    
    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
      
        const response = await UserService.searchUsersByName(formData.name, 0, 10);
        setResults(response.content);
        setPageInfo({ page: response.number, totalPages: response.totalPages });
      };

    return (
        <div>
            <FormContainer title='Buscar Usuário' submitMessage='Pesquisar' onSubmit={handleSubmit}>
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
                onPageChange={async (newPage) => {
                    const response = await UserService.searchUsersByName(formData.name, newPage, 10);
                    setResults(response.content);
                    setPageInfo({
                    page: response.number,
                    totalPages: response.totalPages
                    });
                }}
                />
                </SearchResultGrid>
            )}
        </div>
    );
}

export default UserPage;