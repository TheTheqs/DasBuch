import { useState, useEffect } from "react";
import FormContainer from "./FormContainer";
import FormInput from "./FormInput";
import DynamicInputList from "./DynamicInputList";
import FormTextarea from "./FormTextarea";
import RatingInput from "./RatingInput";
import FormDateInput from "./FormDateInput";
import { useTranslation } from "react-i18next";

interface ReviewFormData {
  title: string;
  authorsNames: string[];
  synopsys: string;
  commentary: string;
  score: number;
  readDate: Date;
}

interface ReviewFormProps {
  initialData: ReviewFormData;
  onSubmit: (formData: ReviewFormData) => void;
  submitLabel: string;
  success?: string;
  error?: string;
}

function ReviewForm({
  initialData,
  onSubmit,
  submitLabel,
  success,
  error,
}: ReviewFormProps) {
  const [formData, setFormData] = useState(initialData);
  const { t } = useTranslation();

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

  const handleScoreChange = (score: number) => {
    setFormData((prev) => ({ ...prev, score }));
  };

  const handleDateChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newDate = new Date(`${e.target.value}-01`);
    setFormData((prev) => ({ ...prev, readDate: newDate }));
  };

  const handleFormSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit(formData);
  };

  const formattedReadDate =
    formData.readDate instanceof Date
      ? formData.readDate.toISOString().slice(0, 7)
      : "";

  return (
    <FormContainer
      title={t("review.formTitle")}
      submitMessage={submitLabel}
      onSubmit={handleFormSubmit}
    >
      <FormInput
        label={t("review.bookTitle")}
        placeholder={t("review.placeholderBookTitle")}
        type="text"
        name="title"
        value={formData.title}
        onChange={handleChange}
      />

      <DynamicInputList
        label={t("review.authors")}
        name="authorsNames"
        values={formData.authorsNames}
        onChange={(newAuthors) =>
          setFormData({ ...formData, authorsNames: newAuthors })
        }
      />

      <FormTextarea
        label={t("review.synopsis")}
        placeholder={t("review.placeholderSynopsis")}
        name="synopsys"
        value={formData.synopsys}
        onChange={handleChange}
        rows={10}
      />

      <FormTextarea
        label={t("review.comment")}
        placeholder={t("review.placeholderComment")}
        name="commentary"
        value={formData.commentary}
        onChange={handleChange}
        rows={10}
      />

      <RatingInput
        label={t("review.score")}
        value={formData.score}
        name="score"
        onChange={handleScoreChange}
      />

      <FormDateInput
        label={t("review.readDate")}
        name="readDate"
        value={formattedReadDate}
        onChange={handleDateChange}
      />

      {success && <p className="text-success mt-2">{success}</p>}
      {error && <p className="text-danger mt-2">{error}</p>}
    </FormContainer>
  );
}

export default ReviewForm;
