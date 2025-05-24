import { useState } from "react";
import FormContainer from "../components/FormContainer";
import FormInput from "../components/FormInput";
import { useSearchParams } from "react-router-dom";
import { handleApiError } from "../utils/handleApiError";
import UserService from "../services/UserService";
import { useTranslation } from "react-i18next";

export default function ResetPasswordPage() {
  const [searchParams] = useSearchParams();
  const { t } = useTranslation();

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
      setError(t("profile.passwordMismatch"));
      return;
    }

    if (!token) {
      setError(t("resetPassword.tokenMissing"));
      return;
    }

    try {
      const message = await UserService.resetPassword(token, formData.password);
      setMessage(message);
    } catch (err) {
      setError(t(handleApiError(err)));
    }
  };

  return (
    <FormContainer
      title={t("resetPassword.title")}
      submitMessage={t("form.send")}
      onSubmit={handleSubmit}
    >
      <FormInput
        label={t("form.password")}
        placeholder={t("resetPassword.passwordPlaceholder")}
        type="password"
        name="password"
        value={formData.password}
        onChange={handleChange}
      />

      <FormInput
        label={t("profile.confirmPassword")}
        placeholder={t("resetPassword.confirmPasswordPlaceholder")}
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
