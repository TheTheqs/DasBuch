import { useState } from 'react';
import { useNavigate } from "react-router-dom";
import { handleApiError } from '../utils/handleApiError';
import FormInput from '../components/FormInput';
import FormContainer from '../components/FormContainer';
import UserService from '../services/UserService';

function SignInPage() {
  
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    confirmEmail: '',
    password: '',
    confirmPassword: ''
  });

  const [success, setSuccess] = useState("");
  const [error, setError] = useState("");

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSuccess("");
    setError("");

    if (formData.email !== formData.confirmEmail) {
      setError("Os emails não coincidem.");
      return;
    }

    if (formData.password !== formData.confirmPassword) {
      setError("As senhas não coincidem.");
      return;
    }

    try {
      await UserService.registerUser({
        name: formData.name,
        email: formData.email,
        password: formData.password
      });
      setSuccess("Conta criada com sucesso!");
      navigate("/message?title=Conta+criada+com+sucesso!&subtitle=Vá+para+página+de+login+para+realizar+o+login!")
    } catch (error) {
      setError(handleApiError(error));
    }
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
      {success && <p style={{ color: "green" }}>{success}</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}
    </FormContainer>
  );
}

export default SignInPage;
