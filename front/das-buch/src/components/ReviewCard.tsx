import React from "react";
import { ReviewDTO } from "../type/ReviewDTO";
import { useNavigate } from "react-router-dom";

interface ReviewCardProps {
  review: ReviewDTO;
}

const ReviewCard: React.FC<ReviewCardProps> = ({ review }) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/review/${review.id}`);
  };

  const renderScore = (score: number) => {
    const outOfFive = Math.round(score * 10) / 10; // garante precisão correta (ex: 0.5 e não 0.3)
    return (
      <div className="fw-semibold text-dark">
        {outOfFive.toFixed(1)} / 5
      </div>
    );
  };

  return (
    <div
      className="card shadow-sm border-0 mb-3"
      style={{
        cursor: "pointer",
        backgroundColor: "#f8f9fa",
        minWidth: "280px",
        maxWidth: "360px",
        width: "100%" // Responsivo dentro de grids
      }}
      onClick={handleClick}
    >
      <div className="card-body">
        <small className="text-muted">{review.user.name}</small>
        <h5 className="card-title mt-1 mb-2">{review.book.title}</h5>
        <p className="card-subtitle text-muted mb-2">
          <em>{review.book.authors.map((a) => a.name).join(", ")}</em>
        </p>
        {renderScore(review.score)}
      </div>
    </div>
  );
};

export default ReviewCard;
