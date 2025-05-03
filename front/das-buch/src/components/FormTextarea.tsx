import React from "react";

interface FormTextareaProps {
  label: string;
  placeholder: string;
  name: string;
  value: string;
  onChange: (e: React.ChangeEvent<HTMLTextAreaElement>) => void;
  rows?: number;
}

function FormTextarea({
  label,
  placeholder,
  name,
  value,
  onChange,
  rows = 4,
}: FormTextareaProps) {
  return (
    <div className="mb-3">
      <label className="form-label text-dark">{label}</label>
      <textarea
        className="form-control border rounded-0"
        placeholder={placeholder}
        name={name}
        value={value}
        onChange={onChange}
        rows={rows}
        required
      />
    </div>
  );
}

export default FormTextarea;
