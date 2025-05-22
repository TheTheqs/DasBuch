import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { handleApiError } from "../utils/handleApiError";
import FormInput from "../components/FormInput";
import FormContainer from "../components/FormContainer";
import UserService from "../services/UserService";
import { useTranslation } from "react-i18next";

function SignInPage() {
  const navigate = useNavigate();
  const { t } = useTranslation();
  const title = encodeURIComponent(t("message.accountCreatedTitle"));
  const subtitle = encodeURIComponent(t("message.accountCreatedSubtitle"));

  const [formData, setFormData] = useState({
    name: "",
    email: "",
    confirmEmail: "",
    password: "",
    confirmPassword: "",
  });

  const [acceptedTerms, setAcceptedTerms] = useState(false);
  const [success, setSuccess] = useState("");
  const [error, setError] = useState("");

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSuccess("");
    setError("");

    if (!acceptedTerms) {
      setError(t("form.termsNotAccepted"));
      return;
    }

    if (formData.email !== formData.confirmEmail) {
      setError(t("form.emailMismatch"));
      return;
    }

    if (formData.password !== formData.confirmPassword) {
      setError(t("form.passwordMismatch"));
      return;
    }

    try {
      await UserService.registerUser({
        name: formData.name,
        email: formData.email,
        password: formData.password,
      });
      setSuccess(t("form.success"));
      navigate(`/message?title=${title}&subtitle=${subtitle}`);
    } catch (error) {
      setError(handleApiError(error));
    }
  };

  return (
    <FormContainer
      title={t("form.registrationData")}
      submitMessage={t("form.register")}
      onSubmit={handleSubmit}
    >
      <FormInput
        label={t("form.name")}
        placeholder={t("form.namePlaceholder")}
        type="text"
        name="name"
        value={formData.name}
        onChange={handleChange}
      />
      <FormInput
        label={t("form.email")}
        placeholder={t("form.emailPlaceholder")}
        type="email"
        name="email"
        value={formData.email}
        onChange={handleChange}
      />
      <FormInput
        label={t("form.confirmEmail")}
        placeholder={t("form.confirmEmailPlaceholder")}
        type="email"
        name="confirmEmail"
        value={formData.confirmEmail}
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
      <FormInput
        label={t("form.confirmPassword")}
        placeholder={t("form.confirmPasswordPlaceholder")}
        type="password"
        name="confirmPassword"
        value={formData.confirmPassword}
        onChange={handleChange}
      />
      <div className="form-check my-3">
        <input
          className="form-check-input"
          type="checkbox"
          id="termsCheckbox"
          checked={acceptedTerms}
          onChange={() => setAcceptedTerms(!acceptedTerms)}
        />
        <label className="form-check-label" htmlFor="termsCheckbox">
          {t("form.acceptTerms")}
        </label>
      </div>
      {success && <p style={{ color: "green" }}>{success}</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}
    </FormContainer>
  );
}

export default SignInPage;
