import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { ReviewDTO } from "../type/ReviewDTO";
import ReviewService from "../services/ReviewService";
import { handleApiError } from "../utils/handleApiError";
import ReviewForm from "../components/ReviewForm";
import { useTranslation } from "react-i18next";

function EditReviewPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { t } = useTranslation();

  const [review, setReview] = useState<ReviewDTO | null>(null);
  const [success, setSuccess] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchReview = async () => {
      try {
        const data = await ReviewService.getReviewById(Number(id));
        setReview(data);
      } catch (err) {
        setError(handleApiError(err));
      } finally {
        setLoading(false);
      }
    };

    fetchReview();
  }, [id]);

  const handleUpdate = async (formData: {
    title: string;
    authorsNames: string[];
    synopsys: string;
    commentary: string;
    score: number;
    readDate: Date;
  }) => {
    setSuccess("");
    setError("");

    try {
      await ReviewService.updateReview({
        id: Number(id),
        bookTitle: formData.title,
        authorsNames: formData.authorsNames,
        synopsys: formData.synopsys,
        commentary: formData.commentary,
        score: formData.score,
        readAt: formData.readDate.toISOString().slice(0, 19),
      });
      setSuccess(t("editReview.success"));
      navigate(`/review/${id}`);
    } catch (err) {
      setError(handleApiError(err));
    }
  };

  if (loading)
    return <p className="text-center mt-4">{t("editReview.loading")}</p>;

  if (error || !review)
    return (
      <p className="text-danger text-center mt-4">
        {error || t("editReview.notFound")}
      </p>
    );

  const initialData = {
    title: review.book.title,
    authorsNames: review.book.authors.map((a) => a.name),
    synopsys: review.synopsys,
    commentary: review.commentary,
    score: review.score,
    readDate: new Date(review.readAt),
  };

  return (
    <ReviewForm
      initialData={initialData}
      onSubmit={handleUpdate}
      submitLabel={t("form.update")}
      success={success}
      error={error}
    />
  );
}

export default EditReviewPage;
