import { useState, useEffect } from "react";
import FormContainer from "./FormContainer";
import FormInput from "./FormInput";
import DynamicInputList from "./DynamicInputList";
import FormTextarea from "./FormTextarea";
import RatingInput from "./RatingInput";
import FormDateInput from "./FormDateInput";

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

function ReviewForm({ initialData, onSubmit, submitLabel, success, error }: ReviewFormProps) {
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

  const formattedReadDate = formData.readDate instanceof Date
    ? formData.readDate.toISOString().slice(0, 7)
    : "";

  return (
    <FormContainer
      title="Dados Cadastrais"
      submitMessage={submitLabel}
      onSubmit={handleFormSubmit}
    >
      <FormInput
        label="Livro"
        placeholder="Digite o título do livro"
        type="text"
        name="title"
        value={formData.title}
        onChange={handleChange}
      />

      <DynamicInputList
        label="Autores"
        name="authorsNames"
        values={formData.authorsNames}
        onChange={(newAuthors) =>
          setFormData({ ...formData, authorsNames: newAuthors })
        }
      />

      <FormTextarea
        label="Sinopse"
        placeholder="Escreva a sua sinopse do livro..."
        name="synopsys"
        value={formData.synopsys}
        onChange={handleChange}
        rows={10}
      />

      <FormTextarea
        label="Comentário"
        placeholder="Dê a sua opinião sobre o livro..."
        name="commentary"
        value={formData.commentary}
        onChange={handleChange}
        rows={10}
      />

      <RatingInput
        label="Nota"
        value={formData.score}
        name="score"
        onChange={handleScoreChange}
      />

      <FormDateInput
        label="Data de Leitura"
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
