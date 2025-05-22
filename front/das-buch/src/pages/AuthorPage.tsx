import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import AuthorService from "../services/AuthorService";
import { useUser } from "../context/User";
import { AuthorDTO } from "../type/AuthorDTO";
import { Spinner, Button } from "react-bootstrap";
import { useTranslation } from "react-i18next";

function AuthorPage() {
  const { id } = useParams<{ id: string }>();
  const { user } = useUser();
  const navigate = useNavigate();
  const { t } = useTranslation();

  const [author, setAuthor] = useState<AuthorDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchAuthor = async () => {
      if (!id) return;
      setLoading(true);
      try {
        const data = await AuthorService.getAuthorById(Number(id));
        setAuthor(data);
      } catch (err) {
        setError(t("authorPage.loadError") + " " + err);
      } finally {
        setLoading(false);
      }
    };
    fetchAuthor();
  }, [id, t]);

  if (loading) {
    return (
      <div className="container py-5 text-center">
        <Spinner animation="border" variant="primary" />
      </div>
    );
  }

  if (error || !author) {
    return (
      <div className="container py-5 text-center text-danger">
        {error || t("authorPage.notFound")}
      </div>
    );
  }

  return (
    <div className="container py-4">
      <div className="bg-light p-4 rounded shadow-sm mb-2">
        <div className="mb-2 text-muted small">
          {t("authorPage.idLabel", { id: author.id })}
        </div>
        <h2 className="fw-bold mb-0">{author.name}</h2>

        <div className="mt-3">
          <strong>{t("authorPage.books")}</strong>
          <ul className="mt-2">
            {author.books.length > 0 ? (
              author.books.map((book, index) => <li key={index}>{book}</li>)
            ) : (
              <li>{t("author.noBooks")}</li>
            )}
          </ul>
        </div>
      </div>

      {user?.role === "ADMIN" && (
        <div className="d-flex gap-2 mt-2 justify-content-center">
          <Button
            variant="outline-primary"
            onClick={(e) => {
              e.stopPropagation();
              navigate(`/update_author/${author.id}`);
            }}
          >
            {t("authorPage.edit")}
          </Button>

          <Button
            variant="outline-danger"
            onClick={async (e) => {
              e.stopPropagation();
              const confirmed = window.confirm(t("authorPage.confirmDelete"));
              if (!confirmed) return;

              const message = await AuthorService.deleteAuthor(author.id);
              alert(message);
              navigate(`/`);
            }}
          >
            {t("authorPage.delete")}
          </Button>
        </div>
      )}
    </div>
  );
}

export default AuthorPage;
