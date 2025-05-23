import { useState } from "react";
import FormInput from "../components/FormInput";
import FormContainer from "../components/FormContainer";
import SimpleLink from "../components/SimpleLink";
import UserService from "../services/UserService";
import { useUser } from "../context/User";
import { handleApiError } from "../utils/handleApiError";
import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { Spinner } from "react-bootstrap";

function LogInPage() {
  const { t } = useTranslation();
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const navigate = useNavigate();
  const { setUser } = useUser();
  const [errorMessage, setErrorMessage] = useState("");
  const [loading, setLoading] = useState(false); // 👈 Adicionado

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setErrorMessage("");
    setLoading(true); // 👈 Ativa carregamento

    try {
      const user = await UserService.loginUser(formData.email, formData.password);
      setUser(user);
      navigate("/");
    } catch (err) {
      setErrorMessage(t(handleApiError(err)));
    } finally {
      setLoading(false); // 👈 Desativa carregamento
    }
  };

  if (loading) {
    return (
      <div className="container py-5 text-center">
        <Spinner animation="border" variant="primary" />
      </div>
    );
  }

  return (
    <div>
      <FormContainer
        title={t("login.title")}
        submitMessage={t("form.login")}
        onSubmit={handleSubmit}
        afterFormContent={
          <SimpleLink to="/forgot-password" label={t("login.forgotPassword")} />
        }
      >
        <FormInput
          label={t("form.email")}
          placeholder={t("form.emailPlaceholder")}
          type="email"
          name="email"
          value={formData.email}
          onChange={handleChange}
        />
        <FormInput
          label={t("form.password")}
          placeholder={t("form.passwordPlaceholder")}
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
