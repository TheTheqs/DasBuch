import { useEffect, useState } from "react";
import { useUser } from "../context/User";
import { useNavigate } from "react-router-dom";
import FormContainer from "../components/FormContainer";
import FormInput from "../components/FormInput";
import { handleApiError } from "../utils/handleApiError";
import UserService from "../services/UserService";
import UserProfile from "../components/UserProfile";
import { useTranslation } from "react-i18next";

function ProfilePage() {
  const { user, setUser } = useUser();
  const navigate = useNavigate();
  const { t } = useTranslation();

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
      setError(t("profile.passwordMismatch"));
      return;
    }

    try {
      const updatedUser = await UserService.updateUser(
        formData.name,
        formData.password
      );
      setUser(updatedUser);
      setSuccess(t("profile.success"));
    } catch (err) {
      setError(t(handleApiError(err)));
    }
  };

  useEffect(() => {
    if (!user) {
      navigate(
        "/message?title=Um erro inesperado aconteceu!&subtitle=Tente fazer o login novamente"
      );
    }
  }, [user, navigate]);

  if (!user) return null;

  return (
    <div style={{ paddingTop: "0px" }}>
      <h1 style={{ textAlign: "center", marginBottom: "1.5rem" }}>
        {t("profile.title")}
      </h1>

      <UserProfile relatedUser={user} />

      <FormContainer title={t("profile.formTitle")} submitMessage={t("form.update")} onSubmit={handleSubmit}>
        <FormInput
          label={t("form.name")}
          placeholder={t("profile.namePlaceholder")}
          type="text"
          name="name"
          value={formData.name}
          onChange={handleChange}
        />

        <FormInput
          label={t("form.password")}
          placeholder={t("profile.passwordPlaceholder")}
          type="password"
          name="password"
          value={formData.password}
          onChange={handleChange}
        />

        <FormInput
          label={t("profile.confirmPassword")}
          placeholder={t("profile.confirmPasswordPlaceholder")}
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
