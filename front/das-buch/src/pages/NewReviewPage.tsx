import { useState } from "react";
import { handleApiError } from "../utils/handleApiError";
import FormInput from "../components/FormInput";
import FormContainer from "../components/FormContainer";
import DynamicInputList from "../components/DynamicInputList.tsx";
import FormTextarea from "../components/FormTextarea.tsx";
import RatingInput from "../components/RatingInput.tsx";
import FormDateInput from "../components/FormDateInput.tsx";
import ReviewService from "../services/ReviewService.ts";
import { useNavigate } from "react-router-dom";

function NewReviewPage() {
  const [formData, setFormData] = useState({
    title: "",
    authorsNames: [""],
    synopsys: "",
    commentary: "",
    score: 0,
    readDate: new Date(),
  });

  const navigate = useNavigate();
  const [success, setSuccess] = useState("");
  const [error, setError] = useState("");

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

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSuccess("");
    setError("");
    console.log(formData.title)
    try {
      const newReview = await ReviewService.createReview({
        bookTitle: formData.title,
        authorsNames: formData.authorsNames,
        synopsys: formData.synopsys,
        commentary: formData.commentary,
        score: formData.score,
        readAt: formData.readDate.toISOString().slice(0, 19),
      });
      setSuccess("Review criado com sucesso!");
      navigate(`/review/${newReview.id}`);
    } catch (error) {
      setError(handleApiError(error));
    }
  };

  const formattedReadDate = formData.readDate instanceof Date
    ? formData.readDate.toISOString().slice(0, 7)
    : "";

  return (
    <FormContainer
      title="Dados Cadastrais"
      submitMessage="Cadastrar"
      onSubmit={handleSubmit}
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

export default NewReviewPage;
