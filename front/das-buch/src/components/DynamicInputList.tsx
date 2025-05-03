import React from "react";

interface DynamicInputListProps {
  label: string;
  name: string;
  values: string[];
  onChange: (values: string[]) => void;
  placeholder?: string;
}

const DynamicInputList: React.FC<DynamicInputListProps> = ({
  label,
  name,
  values,
  onChange,
  placeholder = "Digite um valor",
}) => {
  const handleChange = (index: number, value: string) => {
    const newValues = [...values];
    newValues[index] = value;
    onChange(newValues);
  };

  const handleAddField = () => {
    onChange([...values, ""]);
  };

  const handleRemoveField = (index: number) => {
    const newValues = values.filter((_, i) => i !== index);
    onChange(newValues);
  };

  return (
    <div className="mb-3">
      <label className="form-label text-dark d-block">{label}</label>
      {values.map((value, index) => (
        <div key={index} className="d-flex gap-2 mb-2 align-items-center">
          <input
            type="text"
            name={`${name}[${index}]`}
            value={value}
            onChange={(e) => handleChange(index, e.target.value)}
            placeholder={`${placeholder} ${index + 1}`}
            className="form-control"
            required
          />
          {values.length > 1 && (
            <button
              type="button"
              className="btn btn-outline-danger btn-sm"
              onClick={() => handleRemoveField(index)}
            >
              Remover
            </button>
          )}
        </div>
      ))}
      <button type="button" className="btn btn-outline-secondary btn-sm" onClick={handleAddField}>
        + Adicionar
      </button>
    </div>
  );
};

export default DynamicInputList;