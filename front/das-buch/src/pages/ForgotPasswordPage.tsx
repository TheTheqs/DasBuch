import { useState } from "react";
import UserService from "../services/UserService";
import { useNavigate } from "react-router-dom";
import { handleApiError } from "../utils/handleApiError";
import FormInput from "../components/FormInput";
import FormContainer from "../components/FormContainer";
import { useTranslation } from "react-i18next";

function ForgotPasswordPage() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    email: "",
  });

  const [success, setSuccess] = useState("");
  const [error, setError] = useState("");
  const { t } = useTranslation();

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
      navigate(`/message?title=${t("message.accountRecoveryRequestTitle")}&subtitle=${t("message.accountRecoveryRequestSubtitle")}`);
    } catch (err) {
      setError(t(handleApiError(err)));
    }
  };

  return (
    <div>
      <FormContainer
        title={t("forgotPassword.title")}
        submitMessage={t("form.send")}
        onSubmit={handleSubmit}
      >
        <FormInput
          label={t("forgotPassword.emailLabel")}
          placeholder={t("forgotPassword.emailPlaceholder")}
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
