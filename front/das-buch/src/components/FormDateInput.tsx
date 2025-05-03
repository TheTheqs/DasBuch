import React from "react";

interface FormMonthInputProps {
  label: string;
  name: string;
  value: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

function FormDateInput({ label, name, value, onChange }: FormMonthInputProps) {
  return (
    <div className="mb-3">
      <label className="form-label text-dark">{label}</label>
      <input
        type="month"
        name={name}
        value={value}
        onChange={onChange}
        className="form-control border rounded-0"
        required
      />
    </div>
  );
}

export default FormDateInput;
