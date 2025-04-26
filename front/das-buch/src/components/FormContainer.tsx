import { ReactNode } from 'react';

interface FormContainerProps {
  title: string;
  submitMessage: string;
  afterFormContent?: React.ReactNode;
  children: ReactNode;
  onSubmit: (e: React.FormEvent<HTMLFormElement>) => void;
}

function FormContainer({ title, children, submitMessage, onSubmit, afterFormContent }: FormContainerProps) {
  return (
    <div className="container d-flex justify-content-center align-items-center" style={{ minHeight: '80vh' }}>
      <div className="col-md-6">
        <form onSubmit={onSubmit}>
          <h1 className="mb-4 text-center" style={{ fontSize: '24px' }}>
            {title}
          </h1>

          {children}

          <button
            className="btn btn-darksoft btn-lg px-4 w-100"
            type="submit"
            style={{minWidth: '150px', textAlign: 'center' }}
            >
            {submitMessage}
          </button>
        </form>
        {afterFormContent}
      </div>
    </div>
  );
}

export default FormContainer;