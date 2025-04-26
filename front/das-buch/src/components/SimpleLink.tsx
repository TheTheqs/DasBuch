import { Link } from 'react-router-dom';

interface SimpleLinkProps {
  to: string;
  label: string;
}

function SimpleLink({ to, label }: SimpleLinkProps) {
  return (
    <div className="mt-4">
      <Link to={to} className="text-dark text-decoration-underline fs-5">
        {label}
      </Link>
    </div>
  );
}

export default SimpleLink;
