import { useState } from "react";
import UserService from "../services/UserService";
import { handleApiError } from "../utils/handleApiError";
import FormInput from "../components/FormInput";
import FormContainer from "../components/FormContainer";

function ForgotPasswordPage() {
  const [formData, setFormData] = useState({
    email: "",
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
    try {
      const message = await UserService.forgotPassword(formData.email);
      setSuccess(message);
    } catch (error) {
      setError(handleApiError(error));
    }
  };

  return (
    <div>
      <FormContainer
        title="Recuperar senha"
        submitMessage="Enviar"
        onSubmit={handleSubmit}
      >
        <FormInput
          label="Email"
          placeholder="Digite seu email"
          type="email"
          name="email"
          value={formData.email}
          onChange={handleChange}
        />
        {success && <p style={{ color: "green" }}>{success}</p>}
        {error && <p style={{ color: "red" }}>{error}</p>}
      </FormContainer>
    </div>
  );
}

export default ForgotPasswordPage;
