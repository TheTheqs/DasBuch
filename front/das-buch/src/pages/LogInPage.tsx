import { useState } from "react";
import FormInput from "../components/FormInput";
import FormContainer from "../components/FormContainer";
import SimpleLink from "../components/SimpleLink";
import UserService from "../services/UserService";
import { useUser } from "../context/User";
import { handleApiError } from "../utils/handleApiError";
import { useNavigate } from "react-router-dom";

function LogInPage() {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const navigate = useNavigate();
  const { setUser } = useUser();
  const [errorMessage, setErrorMessage] = useState("");

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setErrorMessage("");
    try {
      const user = await UserService.loginUser(formData.email, formData.password);
      setUser(user);
      navigate("/");
    } catch (error) {
      setErrorMessage(handleApiError(error));
    }
  };

  return (
    <div>
      <FormContainer
        title="Login"
        submitMessage="Login"
        onSubmit={handleSubmit}
        afterFormContent={
          <SimpleLink to="/forgot-password" label="Esqueceu a senha?" />
        }
      >
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
        {errorMessage && <p style={{ color: "red" }}>{errorMessage}</p>}
      </FormContainer>
    </div>
  );
}

export default LogInPage;
