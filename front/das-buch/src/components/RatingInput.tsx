import React from "react";
import { Star } from "lucide-react";

interface RatingInputProps {
  label: string;
  name: string;
  value: number;
  onChange: (value: number) => void;
}

const RatingInput: React.FC<RatingInputProps> = ({ label, value, onChange }) => {
  const totalStars = 5;

  const handleClick = (index: number, half: boolean) => {
    const newValue = half ? index * 2 - 1 : index * 2;
    onChange(newValue);
  };

  return (
    <div className="mb-3">
      <label className="form-label text-dark d-block mb-1">{label}</label>
      <div className="d-flex gap-1">
        {Array.from({ length: totalStars }, (_, i) => {
          const index = i + 1;
          const leftActive = value >= index * 2 - 1;
          const rightActive = value >= index * 2;

          return (
            <div key={index} className="position-relative" style={{ width: 24 }}>
              <Star
                size={20}
                className={`position-absolute top-0 start-0 ${leftActive ? "text-warning" : "text-secondary"}`}
                style={{ width: "50%", overflow: "hidden", cursor: "pointer" }}
                onClick={() => handleClick(index, true)}
                fill={leftActive ? "currentColor" : "none"}
              />
              <Star
                size={20}
                className={`${rightActive ? "text-warning" : "text-secondary"}`}
                style={{ marginLeft: "12px", cursor: "pointer" }}
                onClick={() => handleClick(index, false)}
                fill={rightActive ? "currentColor" : "none"}
              />
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default RatingInput;