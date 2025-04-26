import { useLocation } from 'react-router-dom';

function MessagePage() {
  const location = useLocation();
  const params = new URLSearchParams(location.search);
  const title = params.get('title') || "Mensagem";
  const subtitle = params.get('subtitle') || "";

  return (
    <div className="container d-flex flex-column justify-content-center align-items-center text-center" style={{ minHeight: '80vh' }}>
      <h1 className="mb-4">{title}</h1>
      <p className="lead">{subtitle}</p>
    </div>
  );
}

export default MessagePage;
