import { Link } from 'react-router-dom';

interface PrimaryButtonProps {
  to: string;
  label: string;
}

function PrimaryButton({ to, label }: PrimaryButtonProps) {
  return (
    <Link to={to} className="btn btn-darksoft btn-lg px-4 mb-4">
      {label}
    </Link>
  );
}

export default PrimaryButton;
