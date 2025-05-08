import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { ReviewDTO } from "../type/ReviewDTO";
import RatingInput from "../components/RatingInput";
import { Button, Spinner } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import ReviewService from "../services/ReviewService";
import { useUser } from "../context/User";

const ReviewPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const { user } = useUser();
  const [review, setReview] = useState<ReviewDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const fetchReview = async () => {
      try {
        const data = await ReviewService.getReviewById(Number(id));
        setReview(data);
      } catch (err) {
        setError("Não foi possível carregar o review: " + err);
      } finally {
        setLoading(false);
      }
    };
    fetchReview();
  }, [id]);

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
        {error || "Review não encontrado."}
      </div>
    );
  }

  const formattedDate = new Date(review.readAt).toLocaleDateString("pt-BR", {
    year: "numeric",
    month: "long",
  });

  return (
    <div className="container py-4">
      <div className="bg-light p-4 rounded shadow-sm">
        <div className="mb-2 text-muted small">Review ID: {review.id}</div>
        <h2 className="fw-bold mb-0">{review.book.title}</h2>
        <div className="text-muted mb-2">por {review.user.name}</div>

        <div className="mb-3">
          <strong>Autores:</strong>{" "}
          {review.book.authors.map((a) => a.name).join(", ")}
        </div>

        <div className="mb-3">
          <strong>Sinopse:</strong>
          <p className="mt-1">{review.synopsys}</p>
        </div>

        <div className="mb-3">
          <strong>Comentário:</strong>
          <p className="mt-1">{review.commentary}</p>
        </div>

        <div className="mb-3">
          <strong>Nota:</strong>
          <div className="mt-1">
            <RatingInput
              label=""
              name="score"
              value={review.score}
              onChange={() => {}}
            />
          </div>
        </div>

        <div className="mb-4">
          <strong>Lido em:</strong> {formattedDate}
        </div>

        {(user?.id === review.user.id || user?.role === "ADMIN") && (
          <div className="d-flex gap-2">
            <Button variant="outline-primary"
            onClick = {() => navigate(`/update_review/${review.id}`)}
            >Editar</Button>

            <Button
              variant="outline-danger"
              onClick={async () => {
                const confirmed = window.confirm(
                  "Tem certeza que deseja deletar este review?"
                );
                if (!confirmed) return;

                const message = await ReviewService.deleteReview(review.id);
                alert(message);

                navigate(`/user/reviews/${review.user.id}`);
              }}
            >
              Deletar
            </Button>
          </div>
        )}
      </div>
    </div>
  );
};

export default ReviewPage;
