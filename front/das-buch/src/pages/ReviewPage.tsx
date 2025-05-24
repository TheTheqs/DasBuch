import React, { useEffect, useState } from "react";
import RatingDisplay from "../components/RatingDisplay";
import { useParams } from "react-router-dom";
import { ReviewDTO } from "../type/ReviewDTO";
import { Button, Spinner } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import ReviewService from "../services/ReviewService";
import { useUser } from "../context/User";
import { useTranslation } from "react-i18next";
import i18n from "../i18n";

const ReviewPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const { user } = useUser();
  const [review, setReview] = useState<ReviewDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const { t } = useTranslation();

  useEffect(() => {
    const fetchReview = async () => {
      try {
        const data = await ReviewService.getReviewById(Number(id));
        setReview(data);
      } catch (err) {
        const translated = t(err as string, { defaultValue: err as string });
        setError(t("reviewPage.loadErrorPrefix") + " " + translated);
      } finally {
        setLoading(false);
      }
    };
    fetchReview();
  }, [id, t]);

  if (loading) {
    return (
      <div className="container py-5 text-center">
        <Spinner animation="border" variant="primary" />
      </div>
    );
  }

  if (error || !review) {
    return (
      <div className="container py-5 text-center text-danger">
        {error || t("reviewPage.notFound")}
      </div>
    );
  }

  const formattedDate = new Date(review.readAt).toLocaleDateString(
    i18n.language,
    {
      year: "numeric",
      month: "long",
    }
  );

  return (
    <div className="container py-4">
      <div className="bg-light p-4 rounded shadow-sm">
        <div className="mb-2 text-muted small">
          {t("reviewPage.reviewIdPrefix")} {review.id}
        </div>
        <h2 className="fw-bold mb-0">{review.book.title}</h2>
        <div className="text-muted mb-2">
          {t("reviewPage.by")} {review.user.name}
        </div>

        <div className="mb-3">
          <strong>{t("reviewPage.authors")}:</strong>{" "}
          {review.book.authors.map((a) => a.name).join(", ")}
        </div>

        <div className="mb-3">
          <strong>{t("reviewPage.synopsis")}:</strong>
          <p className="mt-1">{review.synopsys}</p>
        </div>

        <div className="mb-3">
          <strong>{t("reviewPage.commentary")}:</strong>
          <p className="mt-1">{review.commentary}</p>
        </div>

        <div className="mb-3">
          <strong>{t("reviewPage.score")}:</strong>
          <div className="mt-1">
            <RatingDisplay value={review.score} />
          </div>
        </div>

        <div className="mb-4">
          <strong>{t("reviewPage.readAt")}:</strong> {formattedDate}
        </div>

        {(user?.id === review.user.id || user?.role === "ADMIN") && (
          <div className="d-flex gap-2">
            <Button
              variant="outline-primary"
              onClick={() => navigate(`/update_review/${review.id}`)}
            >
              {t("form.edit")}
            </Button>

            <Button
              variant="outline-danger"
              onClick={async () => {
                const confirmed = window.confirm(t("reviewPage.confirmDelete"));
                if (!confirmed) return;

                const message = await ReviewService.deleteReview(review.id);
                alert(message);
                navigate(`/user/reviews/${review.user.id}`);
              }}
            >
              {t("form.delete")}
            </Button>
          </div>
        )}
      </div>
    </div>
  );
};

export default ReviewPage;
