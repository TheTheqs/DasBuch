import { Link } from "react-router-dom";
import { useTranslation } from "react-i18next";

function NotFoundPage() {
  const { t } = useTranslation();

  return (
    <div
      className="container d-flex flex-column justify-content-center align-items-center text-center"
      style={{ minHeight: "80vh" }}
    >
      <h1 className="display-1 fw-bold text-danger">{t("notFound.code")}</h1>
      <h2 className="mb-3">{t("notFound.title")}</h2>
      <p className="lead text-muted mb-4">{t("notFound.description")}</p>
      <Link to="/" className="btn btn-dark px-4">
        {t("notFound.backHome")}
      </Link>
    </div>
  );
}

export default NotFoundPage;
