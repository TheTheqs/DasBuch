import { useState } from 'react';
import FormInput from '../components/FormInput';
import FormContainer from '../components/FormContainer';

function SignInPage() {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    confirmEmail: '',
    password: '',
    confirmPassword: ''
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    console.log('Dados enviados:', formData);
  };

  return (
    <FormContainer title="Dados Cadastrais" submitMessage='Cadastrar' onSubmit={handleSubmit}>
      <FormInput
        label="Nome"
        placeholder="Digite seu nome"
        type="text"
        name="name"
        value={formData.name}
        onChange={handleChange}
      />
      <FormInput
        label="Email"
        placeholder="Digite seu email"
        type="email"
        name="email"
        value={formData.email}
        onChange={handleChange}
      />

    <FormInput
        label="Confirme o email"
        placeholder="Digite seu email novamente"
        type="email"
        name="confirmEmail"
        value={formData.confirmEmail}
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
        
      <FormInput
        label="Confirme a Senha"
        placeholder="Digite sua senha novamente"
        type="password"
        name="confirmPassword"
        value={formData.confirmPassword}
        onChange={handleChange}
      />
    </FormContainer>
  );
}

export default SignInPage;
