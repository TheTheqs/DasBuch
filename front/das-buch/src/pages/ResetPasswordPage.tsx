import { useState } from "react";
import FormContainer from "../components/FormContainer";
import FormInput from "../components/FormInput";
import { useSearchParams } from "react-router-dom";
import { handleApiError } from "../utils/handleApiError";
import UserService from "../services/UserService";

export default function ResetPasswordPage() {
  const [searchParams] = useSearchParams();
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const token = searchParams.get("token");

  const [formData, setFormData] = useState({
    password: "",
    confirmPassword: "",
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setMessage("");
    setError("");

    if (formData.password !== formData.confirmPassword) {
      setError("As senhas não coincidem.");
      return;
    }

    if (!token) {
      setError("Token de recuperação não encontrado.");
      return;
    }

    try {
      const message = await UserService.resetPassword(token, formData.password);
      setMessage(message);
    } catch (error) {
      setError(handleApiError(error));
    }
  };

  return (
    <FormContainer
      title="Recuperação de Senha"
      submitMessage="Enviar"
      onSubmit={handleSubmit}
    >


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
      {message && <p style={{ color: "green" }}>{message}</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}
    </FormContainer>
  );
}
