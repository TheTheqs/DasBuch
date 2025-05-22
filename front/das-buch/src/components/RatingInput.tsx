import React from "react";

interface RatingInputProps {
  label: string;
  name: string;
  value: number; // de 0 a 5, meio em meio (0.5)
  onChange: (value: number) => void;
}

const RatingInput: React.FC<RatingInputProps> = ({ label, name, value, onChange }) => {
  return (
    <div className="mb-3">
      <label htmlFor={name} className="form-label text-dark d-block mb-1">
        {label}: <strong>{(value).toFixed(1)} / 5</strong>
      </label>
      <input
        type="range"
        className="form-range"
        id={name}
        name={name}
        min={0}
        max={5}
        step={0.5}
        value={value}
        onChange={(e) => onChange(parseFloat(e.target.value))}
      />
    </div>
  );
};

export default RatingInput;
