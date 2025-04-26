import { useState } from 'react';
import FormInput from '../components/FormInput';
import FormContainer from '../components/FormContainer';
import SimpleLink from '../components/SimpleLink';

function LogInPage() {
    const [formData, setFormData] = useState({
        email: '',
        password: ''
      });
    
      const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
      };
    
      const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        console.log('Dados enviados:', formData);
      };
    
      return (
        <div>
            <FormContainer title='Login' submitMessage='Login' onSubmit={handleSubmit}
                afterFormContent={<SimpleLink to="/forgot-password" label="Esqueceu a senha?" />}>
                <FormInput
                    label="Email"
                    placeholder="Digite seu email"
                    type="email"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                />
                <FormInput
                    label="Senha"
                    placeholder="Digite sua senha"
                    type="password"
                    name="password"
                    value={formData.password}
                    onChange={handleChange}
                />
            </FormContainer>
            
        </div>
      );
}

export default LogInPage;