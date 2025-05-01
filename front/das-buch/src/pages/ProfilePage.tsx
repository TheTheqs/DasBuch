import { useEffect } from "react";
import { useState } from "react";
import { useUser } from "../context/User";
import { useNavigate } from "react-router-dom";
import FormContainer from "../components/FormContainer";
import FormInput from "../components/FormInput";
import { handleApiError } from "../utils/handleApiError";
import UserService from "../services/UserService";
import UserProfile from "../components/UserProfile";

function ProfilePage() {
  const { user, setUser } = useUser();
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    name: "",
    password: "",
    confirmPassword: "",
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

    if (formData.password !== formData.confirmPassword) {
      setError("As senhas não coincidem.");
      return;
    }

    try {
      const updatedUser = await UserService.updateUser(
        formData.name,
        formData.password
      );
      setUser(updatedUser);
      setSuccess("Dados atualizados com sussesso!");
    } catch (error) {
      setError(handleApiError(error));
    }
  };

  useEffect(() => {
    if (!user) {
      navigate(
        "/message?title=Um erro inesperado aconteceu!&subtitle=Tente fazer o login novamente"
      );
    }
  }, [user, navigate]);

  if (!user) {
    return null;
  }

  return (
    <div style={{ paddingTop: "0px" }}>
      <h1 style={{ textAlign: "center", marginBottom: "1.5rem" }}>
        Meu Perfil
      </h1>

      <UserProfile user={user} />

      <FormContainer title="Atualizar Dados" submitMessage='Atualizar' onSubmit={handleSubmit}>
      <FormInput
        label="Nome"
        placeholder="Digite o novo nome"
        type="text"
        name="name"
        value={formData.name}
        onChange={handleChange}
      />

      <FormInput
        label="Senha"
        placeholder="Digite a nova senha"
        type="password"
        name="password"
        value={formData.password}
        onChange={handleChange}
      />
        
      <FormInput
        label="Confirme a Senha"
        placeholder="Digite a nova senha novamente"
        type="password"
        name="confirmPassword"
        value={formData.confirmPassword}
        onChange={handleChange}
      />
      {success && <p style={{ color: "green" }}>{success}</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}
    </FormContainer>
    </div>
  );
}

export default ProfilePage;
