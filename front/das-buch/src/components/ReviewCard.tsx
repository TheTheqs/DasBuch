import React from "react";
import { ReviewDTO } from "../type/ReviewDTO";
import { useNavigate } from "react-router-dom";
import { Star } from "lucide-react";

interface ReviewCardProps {
  review: ReviewDTO;
}

const ReviewCard: React.FC<ReviewCardProps> = ({ review }) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/review/${review.id}`);
  };

  const renderStars = (score: number) => {
    const totalStars = 10;
    const filled = Math.floor(score);
    const half = score % 1 >= 0.5 ? 1 : 0;
    const empty = totalStars - filled - half;

    return (
      <div className="d-flex gap-1">
        {[...Array(filled)].map((_, i) => (
          <Star key={`f-${i}`} className="text-warning" size={16} fill="currentColor" />
        ))}
        {half === 1 && (
          <Star className="text-warning" size={16} fill="currentColor" stroke="#ccc" />
        )}
        {[...Array(empty)].map((_, i) => (
          <Star key={`e-${i}`} className="text-secondary" size={16} />
        ))}
      </div>
    );
  };

  return (
    <div
      className="card shadow-sm border-0 mb-3"
      style={{ cursor: "pointer", backgroundColor: "#f8f9fa" }}
      onClick={handleClick}
    >
      <div className="card-body">
        <small className="text-muted">{review.user.name}</small>
        <h5 className="card-title mt-1 mb-2">{review.book.title}</h5>
        <p className="card-subtitle text-muted mb-2">
          <em>{review.book.authors.map((a) => a.name).join(", ")}</em>
        </p>
        {renderStars(review.score)}
      </div>
    </div>
  );
};

export default ReviewCard;