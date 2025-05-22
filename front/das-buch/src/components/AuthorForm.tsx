import { useState, useEffect } from "react";
import FormContainer from "./FormContainer";
import FormInput from "./FormInput";
import { useTranslation } from "react-i18next";

interface AuthorFormData {
  name: string;
}

interface AuthorFormProps {
  initialData: AuthorFormData;
  onSubmit: (formData: AuthorFormData) => void;
  submitLabel: string;
  success?: string;
  error?: string;
}

function AuthorForm({
  initialData,
  onSubmit,
  submitLabel,
  success,
  error,
}: AuthorFormProps) {
  const [formData, setFormData] = useState(initialData);

  useEffect(() => {
    setFormData(initialData);
  }, [initialData]);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value, type } = e.target;

    if (type === "month") {
      const date = new Date(`${value}-01`);
      setFormData((prev) => ({ ...prev, [name]: date }));
    } else {
      setFormData((prev) => ({ ...prev, [name]: value }));
    }
  };

  const { t } = useTranslation();

  const handleFormSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit(formData);
  };

  return (
    <FormContainer
      title={t("author.edit")}
      submitMessage={submitLabel}
      onSubmit={handleFormSubmit}
    >
      <FormInput
        label={t("author.name")}
        placeholder={t("author.placeholderName")}
        type="text"
        name="name"
        value={formData.name}
        onChange={handleChange}
      />
      {success && <p className="text-success mt-2">{success}</p>}
      {error && <p className="text-danger mt-2">{error}</p>}
    </FormContainer>
  );
}

export default AuthorForm;
