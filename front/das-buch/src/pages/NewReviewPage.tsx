import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { handleApiError } from "../utils/handleApiError";
import ReviewService from "../services/ReviewService";
import ReviewForm from "../components/ReviewForm";
import BookService from "../services/BookService";
import { BookDTO } from "../type/BookDTO";
import { useTranslation } from "react-i18next";
import { Spinner } from "react-bootstrap";

function NewReviewPage() {
  const { bookId } = useParams<{ bookId: string }>();
  const navigate = useNavigate();
  const { t } = useTranslation();

  const [success, setSuccess] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(!!bookId);
  const [submitting, setSubmitting] = useState(false); // 👈 Novo estado

  const [initialData, setInitialData] = useState({
    title: "",
    authorsNames: [""],
    synopsys: "",
    commentary: "",
    score: 0,
    readDate: new Date(),
  });

  useEffect(() => {
    const fetchBook = async () => {
      if (!bookId) return;
      try {
        const book: BookDTO = await BookService.getBookById(Number(bookId));
        setInitialData((prev) => ({
          ...prev,
          title: book.title,
          authorsNames: book.authors.map((a) => a.name),
        }));
      } catch (err) {
        setError(t("newReview.loadErrorPrefix") + " " + handleApiError(err));
      } finally {
        setLoading(false);
      }
    };

    fetchBook();
  }, [bookId, t]);

  const handleCreate = async (formData: typeof initialData) => {
    setSuccess("");
    setError("");
    setSubmitting(true); // 👈 inicia carregamento

    try {
      const newReview = await ReviewService.createReview({
        bookTitle: formData.title,
        authorsNames: formData.authorsNames,
        synopsys: formData.synopsys,
        commentary: formData.commentary,
        score: formData.score * 2,
        readAt: formData.readDate.toISOString().slice(0, 19),
      });
      setSuccess(t("newReview.success"));
      navigate(`/review/${newReview.id}`);
    } catch (err) {
      setError(t(handleApiError(err)));
    } finally {
      setSubmitting(false); // 👈 encerra carregamento
    }
  };

  if (loading || submitting) {
    return (
      <div className="container py-5 text-center">
        <Spinner animation="border" variant="primary" />
      </div>
    );
  }

  return (
    <ReviewForm
      initialData={initialData}
      onSubmit={handleCreate}
      submitLabel={t("form.create")}
      success={success}
      error={error}
    />
  );
}

export default NewReviewPage;
