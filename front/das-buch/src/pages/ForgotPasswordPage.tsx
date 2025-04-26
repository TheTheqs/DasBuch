import { useState } from 'react';
import FormInput from '../components/FormInput';
import FormContainer from '../components/FormContainer';

function ForgotPasswordPage() {
    const [formData, setFormData] = useState({
        email: ''
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
            <FormContainer title='Recuperar senha' submitMessage='Enviar' onSubmit={handleSubmit}>
                <FormInput
                    label="Email"
                    placeholder="Digite seu email"
                    type="email"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                />
            </FormContainer>
        </div>
      );
}

export default ForgotPasswordPage;