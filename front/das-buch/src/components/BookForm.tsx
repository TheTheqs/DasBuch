import { useState, useEffect } from "react";
import FormContainer from "./FormContainer";
import FormInput from "./FormInput";
import DynamicInputList from "./DynamicInputList";
import { useTranslation } from "react-i18next";

interface BookFormData {
  title: string;
  authors: string[];
}

interface BookFormProps {
  initialData: BookFormData;
  onSubmit: (formData: BookFormData) => void;
  submitLabel: string;
  success?: string;
  error?: string;
}

function BookForm({
  initialData,
  onSubmit,
  submitLabel,
  success,
  error,
}: BookFormProps) {
  const [formData, setFormData] = useState(initialData);

  useEffect(() => {
    setFormData(initialData);
  }, [initialData]);

  const { t } = useTranslation();

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

  const handleFormSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit(formData);
  };

  return (
    <FormContainer
      title={t("book.edit")}
      submitMessage={submitLabel}
      onSubmit={handleFormSubmit}
    >
      <FormInput
        label={t("book.title")}
        placeholder={t("book.placeholderTitle")}
        type="text"
        name="title"
        value={formData.title}
        onChange={handleChange}
      />

      <DynamicInputList
        label={t("book.authors")}
        name="authors"
        values={formData.authors}
        onChange={(newAuthors) =>
          setFormData({ ...formData, authors: newAuthors })
        }
      />

      {success && <p className="text-success mt-2">{success}</p>}
      {error && <p className="text-danger mt-2">{error}</p>}
    </FormContainer>
  );
}

export default BookForm;
