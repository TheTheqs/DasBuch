import React from "react";
import { useTranslation } from "react-i18next";

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
}) => {
  const handleChange = (index: number, value: string) => {
    const newValues = [...values];
    newValues[index] = value;
    onChange(newValues);
  };
  const { t } = useTranslation();

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
            placeholder={`${t("dynamicInput.placeholder")} ${index + 1}`}
            className="form-control"
            required
          />
          {values.length > 1 && (
            <button
              type="button"
              className="btn btn-outline-danger btn-sm"
              onClick={() => handleRemoveField(index)}
            >
              {t("dynamicInput.remove")}
            </button>
          )}
        </div>
      ))}
      <button
        type="button"
        className="btn btn-outline-secondary btn-sm"
        onClick={handleAddField}
      >
        {t("dynamicInput.add")}
      </button>
    </div>
  );
};

export default DynamicInputList;
