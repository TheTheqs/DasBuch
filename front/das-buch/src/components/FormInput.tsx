interface FormInputProps {
    label: string;
    placeholder: string;
    type: string;
    name: string;
    value: string;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  }
  
  function FormInput({ label, placeholder, type, name, value, onChange }: FormInputProps) {
    return (
      <div className="mb-3">
        <label className="form-label text-dark">{label}</label>
        <input
          className="border rounded-0 form-control"
          type={type}
          name={name}
          placeholder={placeholder}
          value={value}
          onChange={onChange}
          required
        />
      </div>
    );
  }
  
  export default FormInput;