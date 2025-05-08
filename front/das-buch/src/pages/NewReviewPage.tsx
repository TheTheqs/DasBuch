import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { handleApiError } from "../utils/handleApiError";
import ReviewService from "../services/ReviewService";
import ReviewForm from "../components/ReviewForm";

function NewReviewPage() {
  const navigate = useNavigate();

  const [success, setSuccess] = useState("");
  const [error, setError] = useState("");

  const initialData = {
    title: "",
    authorsNames: [""],
    synopsys: "",
    commentary: "",
    score: 0,
    readDate: new Date(),
  };

  const handleCreate = async (formData: typeof initialData) => {
    setSuccess("");
    setError("");

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

  return (
    <ReviewForm
      initialData={initialData}
      onSubmit={handleCreate}
      submitLabel="Cadastrar"
      success={success}
      error={error}
    />
  );
}

export default NewReviewPage;
