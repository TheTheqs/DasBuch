import React from "react";

interface RatingDisplayProps {
  value: number; // 0 a 10
}

const RatingDisplay: React.FC<RatingDisplayProps> = ({ value }) => {
  return <div className="text-dark fw-semibold">{value.toFixed(1)} / 5</div>;
};

export default RatingDisplay;
